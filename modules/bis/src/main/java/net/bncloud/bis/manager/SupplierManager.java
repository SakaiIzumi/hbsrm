package net.bncloud.bis.manager;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.BisSyncConstants;
import net.bncloud.bis.constant.DatasourceConstants;
import net.bncloud.bis.dao.*;
import net.bncloud.bis.model.oa.UfHzhbxxb;
import net.bncloud.bis.model.oa.UfHzhbxxbDt2;
import net.bncloud.bis.model.oa.UfHzhbxxbDt3;
import net.bncloud.bis.model.oa.UfHzhbxxbDt4;
import net.bncloud.bis.service.*;
import net.bncloud.bis.srm.doc.manager.ContractFileManager;
import net.bncloud.common.api.R;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.common.base.globallock.lock.LockWrapper;
import net.bncloud.service.api.platform.supplier.dto.OaSupplierAccountDTO;
import net.bncloud.service.api.platform.supplier.dto.OaSupplierDTO;
import net.bncloud.service.api.platform.supplier.dto.OaSupplierExtDTO;
import net.bncloud.service.api.platform.supplier.dto.OaSupplierLinkManDTO;
import net.bncloud.service.api.platform.supplier.feign.OaSupplierFeignClient;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * desc: 供应商业务
 *
 * @author Rao
 * @Date 2022/01/17
 **/
@Slf4j
@Component
public class SupplierManager {

    @Resource
    private UfHzhbxxbDao ufHzhbxxbDao;
    @Resource
    private UfHzhbxxbDt1Dao ufHzhbxxbDt1Dao;
    @Resource
    private UfHzhbxxbDt2Dao ufHzhbxxbDt2Dao;
    @Resource
    private UfHzhbxxbDt3Dao ufHzhbxxbDt3Dao;
    @Resource
    private UfHzhbxxbDt4Dao ufHzhbxxbDt4Dao;
    @Resource
    private UfHzhbxxbDt5Dao ufHzhbxxbDt5Dao;
    @Resource
    private UfHzhbxxbDt6Dao ufHzhbxxbDt6Dao;
    @Resource
    private UfHzhbxxbDt7Dao ufHzhbxxbDt7Dao;
    @Resource
    private UfHzhbxxbDt8Dao ufHzhbxxbDt8Dao;
    @Resource
    private UfSrmContractDao ufSrmContractDao;
    @Resource
    private OaSupplierFeignClient oaSupplierFeignClient;

    @Resource(name = "oaUfHzhbxxbServiceImpl")
    private UfHzhbxxbService oaUfHzhbxxbService;
    @Resource(name = "bisUfHzhbxxbServiceImpl")
    private UfHzhbxxbService bisUfHzhbxxbService;

    @Resource(name = "oaUfHzhbxxbDt2ServiceImpl")
    private UfHzhbxxbDt2Service oaUfHzhbxxbDt2Service;
    @Resource(name = "bisUfHzhbxxbDt2ServiceImpl")
    private UfHzhbxxbDt2Service bisUfHzhbxxbDt2Service;

    @Resource(name = "oaUfHzhbxxbDt3ServiceImpl")
    private UfHzhbxxbDt3Service oaUfHzhbxxbDt3Service;
    @Resource(name = "bisUfHzhbxxbDt3ServiceImpl")
    private UfHzhbxxbDt3Service bisUfHzhbxxbDt3Service;

    @Resource(name = "oaUfHzhbxxbDt4ServiceImpl")
    private UfHzhbxxbDt4Service oaUfHzhbxxbDt4Service;
    @Resource(name = "bisUfHzhbxxbDt4ServiceImpl")
    private UfHzhbxxbDt4Service bisUfHzhbxxbDt4Service;

    @Resource(name = "oaUfSrmContractServiceImpl")
    private UfSrmContractService oaUfSrmContractServiceImpl;
    @Resource(name = "bisUfSrmContractServiceImpl")
    private UfSrmContractService bisUfSrmContractService;

    @Autowired
    private ContractFileManager contractFileManager;

    @Resource
    private RedissonClient redissonClient;
    @Autowired
    private DistributedLock distributedLock;


    /**
     * 同步OA数据
     * @param supplierIds 供应商ID集合
     * @return
     */
    public R<String> syncOaTableData(List<Integer> supplierIds){

        LockWrapper lockWrapper = new LockWrapper().setKey( BisSyncConstants.SYNC_LOCK_PREFIX_KEY + "syncOaTableData");
        return distributedLock.tryLock( lockWrapper, () -> {

            this.syncTableData(supplierIds, ufHzhbxxbs -> {

                CompletableFuture<Void> f2 = CompletableFuture.runAsync( () -> {
                    try {
                        DynamicDataSourceContextHolder.push( DatasourceConstants.MS_OA );
                        this.assemblyData( ufHzhbxxbs);
                    }finally {
                        DynamicDataSourceContextHolder.poll();
                    }


                });
                CompletableFuture.allOf(f2).join();
            });
            return R.success("同步成功！");
        }, () -> R.fail("正在同步中，请稍后重试！"));

    }

    /**
     * 同步表数据
     * @param recordList
     * @param ufHzhbxxbIdRedisSet
     */
    private void syncTableData(List<UfHzhbxxb> recordList, RSet<Integer> ufHzhbxxbIdRedisSet ){

        Set<Integer> ufHzhbxxbIdSet = ufHzhbxxbIdRedisSet.readAll();
        //更新 or 新增
        List<UfHzhbxxb> ufHzhxxbInsertList = new ArrayList<>();
        List<UfHzhbxxb> ufHzhxxbUpdateList = new ArrayList<>();

        boolean isEmpty = ufHzhbxxbIdSet.isEmpty();
        for (UfHzhbxxb ufHzhbxxb : recordList) {
            Integer id = ufHzhbxxb.getId();
            if( isEmpty || !ufHzhbxxbIdSet.contains( id)){
                ufHzhxxbInsertList.add( ufHzhbxxb);
            }else if( ufHzhbxxbIdSet.contains( id )){
                ufHzhxxbUpdateList.add( ufHzhbxxb );
            }
        }
        // 更新数据
        try {
            if(! ufHzhxxbUpdateList.isEmpty()){
                bisUfHzhbxxbService.updateBatchById( ufHzhxxbUpdateList );
            }
        }catch (Exception ex){
            log.error("[Supplier] batch update error! ",ex);
            return;
        }
        //保存数据
        try {
            if(! ufHzhxxbInsertList.isEmpty()){
                bisUfHzhbxxbService.saveBatch(ufHzhxxbInsertList);
            }
        } catch (Exception ex){
            log.error("[Supplier] batch insert error! ",ex);
            return;
        }

        Set<Integer> insertIdSet = ufHzhxxbInsertList.stream().map(UfHzhbxxb::getId).collect(Collectors.toSet());
        ufHzhbxxbIdRedisSet.addAll( insertIdSet );

        Set<Integer> updateIdSet = ufHzhxxbUpdateList.stream().map(UfHzhbxxb::getId).collect(Collectors.toSet());

        this.syncOtherTable( updateIdSet,insertIdSet );
    }

    /**
     * 同步其他表数据
     * @param updateIdSet
     * @param insertIdSet
     */
    private void syncOtherTable(Set<Integer> updateIdSet,Set<Integer> insertIdSet ){
        if(! CollectionUtils.isEmpty( updateIdSet)){

            try {
                DynamicDataSourceContextHolder.push( DatasourceConstants.MS_OA );
                List<UfHzhbxxbDt2> ufHzhbxxbDt2List = oaUfHzhbxxbDt2Service.lambdaQuery().in(UfHzhbxxbDt2::getMainid, updateIdSet).list();
                if(! CollectionUtils.isEmpty(ufHzhbxxbDt2List)){
                    try {
                        DynamicDataSourceContextHolder.push( DatasourceConstants.BIS );
                        bisUfHzhbxxbDt2Service.updateBatchById( ufHzhbxxbDt2List );
                    }finally {
                        DynamicDataSourceContextHolder.poll();
                    }

                }
            }finally {
                DynamicDataSourceContextHolder.poll();
            }

            try {
                DynamicDataSourceContextHolder.push( DatasourceConstants.MS_OA );
                List<UfHzhbxxbDt3> ufHzhbxxbDt3List = oaUfHzhbxxbDt3Service.lambdaQuery().in(UfHzhbxxbDt3::getMainid, updateIdSet).list();
                if(! CollectionUtils.isEmpty(ufHzhbxxbDt3List)){
                    try {
                        DynamicDataSourceContextHolder.push( DatasourceConstants.BIS );
                        bisUfHzhbxxbDt3Service.updateBatchById( ufHzhbxxbDt3List );
                    }finally {
                        DynamicDataSourceContextHolder.poll();
                    }
                }
            }finally {
                DynamicDataSourceContextHolder.poll();
            }

            try {
                DynamicDataSourceContextHolder.push( DatasourceConstants.MS_OA );
                List<UfHzhbxxbDt4> ufHzhbxxbDt4List = oaUfHzhbxxbDt4Service.lambdaQuery().in(UfHzhbxxbDt4::getMainid, updateIdSet).list();
                if(! CollectionUtils.isEmpty( ufHzhbxxbDt4List )){
                    try {
                        DynamicDataSourceContextHolder.push( DatasourceConstants.BIS );
                        bisUfHzhbxxbDt4Service.updateBatchById( ufHzhbxxbDt4List );
                    }finally {
                        DynamicDataSourceContextHolder.poll();
                    }

                }
            }finally {
                DynamicDataSourceContextHolder.poll();
            }


        }

        if(! CollectionUtils.isEmpty( insertIdSet ) ){

            try {
                DynamicDataSourceContextHolder.push( DatasourceConstants.MS_OA );
                List<UfHzhbxxbDt2> ufHzhbxxbDt2List = oaUfHzhbxxbDt2Service.lambdaQuery().in(UfHzhbxxbDt2::getMainid, insertIdSet).list();
                if(! CollectionUtils.isEmpty(ufHzhbxxbDt2List)){
                    try {
                        DynamicDataSourceContextHolder.push( DatasourceConstants.BIS );
                        bisUfHzhbxxbDt2Service.saveBatch( ufHzhbxxbDt2List );
                    }finally {
                        DynamicDataSourceContextHolder.poll();
                    }

                }
            }finally {
                DynamicDataSourceContextHolder.poll();
            }

            try {
                DynamicDataSourceContextHolder.push( DatasourceConstants.MS_OA );
                List<UfHzhbxxbDt3> ufHzhbxxbDt3List = oaUfHzhbxxbDt3Service.lambdaQuery().in(UfHzhbxxbDt3::getMainid, insertIdSet).list();
                if(! CollectionUtils.isEmpty(ufHzhbxxbDt3List)){
                    try {
                        DynamicDataSourceContextHolder.push( DatasourceConstants.BIS );
                        bisUfHzhbxxbDt3Service.saveBatch( ufHzhbxxbDt3List );
                    }finally {
                        DynamicDataSourceContextHolder.poll();
                    }

                }
            }finally {
                DynamicDataSourceContextHolder.poll();
            }

            try {
                DynamicDataSourceContextHolder.push( DatasourceConstants.MS_OA );
                List<UfHzhbxxbDt4> ufHzhbxxbDt4List = oaUfHzhbxxbDt4Service.lambdaQuery().in(UfHzhbxxbDt4::getMainid, insertIdSet).list();
                if(! CollectionUtils.isEmpty( ufHzhbxxbDt4List )){
                    try {
                        DynamicDataSourceContextHolder.push( DatasourceConstants.BIS );
                        bisUfHzhbxxbDt4Service.saveBatch( ufHzhbxxbDt4List );
                    }finally {
                        DynamicDataSourceContextHolder.poll();
                    }

                }
            }finally {
                DynamicDataSourceContextHolder.poll();
            }

        }

    }

    /**
     * 同步主表数据
     * @param consumer
     */
    public void syncTableData(List<Integer> supplierIds, Consumer<List<UfHzhbxxb>> consumer ){

        String supplierSyncOaTaskKey = BisSyncConstants.SUPPLIER_SYNC_OA_TASK;

        // 获取上一次同步的时间蹉
        RMap<String, Date> syncDataTimeMap = redissonClient.getMap( BisSyncConstants.TASK_SYNC_DATA_TIME_MAP_KEY, JsonJacksonCodec.INSTANCE);
        Date lastSyncDataTime = syncDataTimeMap.get( supplierSyncOaTaskKey );

        // 同步主表
        // 分页式查询数据后
        boolean continueQuery;
        long current = 1;
        int size = 100;
        Date startUpdateDate = new Date();
        do {
            // 合作伙伴信息表
            IPage<UfHzhbxxb> ufHzhbxxbPageResult = oaUfHzhbxxbService.page(new Page<>(current, size),
                    Wrappers.<UfHzhbxxb>lambdaQuery()
                            .in(!CollectionUtils.isEmpty(supplierIds), UfHzhbxxb::getId, supplierIds)
                            /**
                             * 建档成功是
                             */
                            .eq(UfHzhbxxb::getJdcg, "是")
                            /**
                             * 金蝶编码 不为null
                             */
                            .isNotNull(UfHzhbxxb::getJdnumber)
                            ///**
                            // * 企业全称 不为null
                            // */
                            //.isNotNull( UfHzhbxxb::getQyqc )
                            /**
                             * 合作性质  0- 供应商
                             */
                            .eq(UfHzhbxxb::getHzxz, 0)
                            // 更新时间
                            .and( lastSyncDataTime != null,qw -> qw.gt( UfHzhbxxb::getModedatacreatedate, DateUtil.format(lastSyncDataTime, DatePattern.NORM_DATE_FORMAT) )
                                    .gt( UfHzhbxxb::getModedatacreatetime, DateUtil.format( lastSyncDataTime,DatePattern.NORM_TIME_FORMAT )  ).or().ge( UfHzhbxxb::getModedatamodifydatetime,lastSyncDataTime )
                            )
            );

            List<UfHzhbxxb> recordList = ufHzhbxxbPageResult.getRecords();
            boolean empty = CollectionUtils.isEmpty(recordList);
            if (!empty) {
                continueQuery = true;
                current++;
                consumer.accept( recordList );
            } else {
                continueQuery = false;
            }

        } while (continueQuery);

        syncDataTimeMap.fastPut( supplierSyncOaTaskKey, startUpdateDate );

    }

    /**
     * 同步供应商信息
     */
    @DS( DatasourceConstants.MS_OA)
    @Deprecated
    public void syncSupplierInfo(List<Integer> ids) {
        this.syncTableData( ids, this::assemblyData );
    }

    /**
     * 组装数据
     */
    private void assemblyData(List<UfHzhbxxb> recordList) {
        // 合作伙伴信息表 3
        Set<Integer> hzhbxxbIdSet = recordList.stream().map(UfHzhbxxb::getId).collect(Collectors.toSet());

//        CompletableFuture<Map<Integer, UfHzhbxxbDt1>> future1 = CompletableFuture
//                .supplyAsync(() -> ufHzhbxxbDt1Dao.selectList(Wrappers.<UfHzhbxxbDt1>lambdaQuery().in(UfHzhbxxbDt1::getMainid, hzhbxxbIdSet)))
//                .thenApply( list -> list.stream().collect(Collectors.toMap(UfHzhbxxbDt1::getMainid, Function.identity())));

        CompletableFuture<Map<Integer, List<UfHzhbxxbDt2>>> future2 = CompletableFuture
                .supplyAsync(() -> ufHzhbxxbDt2Dao.selectList(Wrappers.<UfHzhbxxbDt2>lambdaQuery().in(UfHzhbxxbDt2::getMainid, hzhbxxbIdSet)))
                .thenApply(list -> list.stream().collect(Collectors.groupingBy(UfHzhbxxbDt2::getMainid)));

        CompletableFuture<Map<Integer, List<UfHzhbxxbDt3>>> future3 = CompletableFuture
                .supplyAsync(() -> ufHzhbxxbDt3Dao.selectList(Wrappers.<UfHzhbxxbDt3>lambdaQuery().in(UfHzhbxxbDt3::getMainid, hzhbxxbIdSet)))
                .thenApply(list -> list.stream().collect(Collectors.groupingBy(UfHzhbxxbDt3::getMainid)));

        CompletableFuture<Map<Integer, UfHzhbxxbDt4>> future4 = CompletableFuture
                .supplyAsync(() -> ufHzhbxxbDt4Dao.selectList(Wrappers.<UfHzhbxxbDt4>lambdaQuery().in(UfHzhbxxbDt4::getMainid, hzhbxxbIdSet)))
                .thenApply(list -> list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(UfHzhbxxbDt4::getMainid))), ArrayList::new)).stream().collect(Collectors.toMap(UfHzhbxxbDt4::getMainid, Function.identity())));

//        CompletableFuture<Map<Integer, UfHzhbxxbDt5>> future5 = CompletableFuture
//                .supplyAsync(() -> ufHzhbxxbDt5Dao.selectList(Wrappers.<UfHzhbxxbDt5>lambdaQuery().in(UfHzhbxxbDt5::getMainid, hzhbxxbIdSet)))
//                .thenApply(list -> list.stream().collect(Collectors.toMap(UfHzhbxxbDt5::getMainid, Function.identity())));
//
//        CompletableFuture<Map<Integer, UfHzhbxxbDt6>> future6 = CompletableFuture
//                .supplyAsync(() -> ufHzhbxxbDt6Dao.selectList(Wrappers.<UfHzhbxxbDt6>lambdaQuery().in(UfHzhbxxbDt6::getMainid, hzhbxxbIdSet)))
//                .thenApply(list -> list.stream().collect(Collectors.toMap(UfHzhbxxbDt6::getMainid, Function.identity())));
//
//        CompletableFuture<Map<Integer, UfHzhbxxbDt7>> future7 = CompletableFuture
//                .supplyAsync(() -> ufHzhbxxbDt7Dao.selectList(Wrappers.<UfHzhbxxbDt7>lambdaQuery().in(UfHzhbxxbDt7::getMainid, hzhbxxbIdSet)))
//                .thenApply(list -> list.stream().collect(Collectors.toMap(UfHzhbxxbDt7::getMainid, Function.identity())));
//
//        CompletableFuture<Map<Integer, UfHzhbxxbDt8>> future8 = CompletableFuture
//                .supplyAsync(() -> ufHzhbxxbDt8Dao.selectList(Wrappers.<UfHzhbxxbDt8>lambdaQuery().in( UfHzhbxxbDt8::getMainid, hzhbxxbIdSet)))
//                .thenApply(list -> list.stream().collect(Collectors.toMap(UfHzhbxxbDt8::getMainid, Function.identity())));
//        CompletableFuture.allOf( future1,future2,future3,future4,future5,future6,future7,future8 ).join();

        CompletableFuture.allOf(future2, future3,future4).join();

        try {

            Map<Integer, List<UfHzhbxxbDt2>> idUfHzhbxxbDt2ListMap = future2.get();
            Map<Integer, List<UfHzhbxxbDt3>> idUfHzhbxxbDt3ListMap = future3.get();
            Map<Integer, UfHzhbxxbDt4> idUfHzhbxxbDt4Map = future4.get();

            List<OaSupplierDTO> oaSupplierDTOList = recordList.stream().map(ufHzhbxxb -> {
                Integer mainId = ufHzhbxxb.getId();

                // 供应商信息
                OaSupplierDTO oaSupplierDTO = new OaSupplierDTO();
                // 金蝶编码
                oaSupplierDTO.setCode( ufHzhbxxb.getJdnumber() );
                // oa编码
                oaSupplierDTO.setOaCode( ufHzhbxxb.getHzfbm() );
                oaSupplierDTO.setName(ufHzhbxxb.getQyqc());
                if( ufHzhbxxb.getGyslb() != null){
                    oaSupplierDTO.setOaType(  ufHzhbxxb.getGyslb()+ "");
                }
                oaSupplierDTO.setCreditCode( ufHzhbxxb.getTyshxydm());

                // 银行
                Optional.ofNullable( idUfHzhbxxbDt3ListMap.get(mainId)).ifPresent( ufHzhbxxbDt3List -> {

                    List<OaSupplierAccountDTO> oaSupplierAccountDtoList = ufHzhbxxbDt3List.stream().map(ufHzhbxxbDt3 -> {

                        OaSupplierAccountDTO oaSupplierAccountDTO = new OaSupplierAccountDTO();
                        oaSupplierAccountDTO.setBankAccount(ufHzhbxxbDt3.getYhzh());
                        oaSupplierAccountDTO.setBankAccountName(ufHzhbxxbDt3.getYhzhmc());
                        oaSupplierAccountDTO.setBankOutlet(ufHzhbxxbDt3.getKhhqc());
                        oaSupplierAccountDTO.setBankDeposit("");
                        return oaSupplierAccountDTO;

                    }).collect(Collectors.toList());

                    oaSupplierDTO.setOaSupplierAccounts( oaSupplierAccountDtoList );

                });

                // 联系人
                Optional.ofNullable(idUfHzhbxxbDt2ListMap.get(mainId)).ifPresent( ufHzhbxxbDt2List -> {

                    List<OaSupplierLinkManDTO> oaSupplierLinkManDtoList = ufHzhbxxbDt2List.stream().map(ufHzhbxxbDt2 -> {
                        OaSupplierLinkManDTO oaSupplierLinkManDTO = new OaSupplierLinkManDTO();
                        oaSupplierLinkManDTO.setName(ufHzhbxxbDt2.getXm());
                        oaSupplierLinkManDTO.setPosition(ufHzhbxxbDt2.getZw());
                        oaSupplierLinkManDTO.setMobile(ufHzhbxxbDt2.getSjhm());
                        oaSupplierLinkManDTO.setEmail(ufHzhbxxbDt2.getYxdz());
                        oaSupplierLinkManDTO.setWechatId(ufHzhbxxbDt2.getWxhm());
                        return oaSupplierLinkManDTO;
                    }).collect(Collectors.toList());
                    oaSupplierDTO.setOaSupplierLinkMen( oaSupplierLinkManDtoList);

                });

                OaSupplierExtDTO oaSupplierExtDTO = new OaSupplierExtDTO();
                oaSupplierExtDTO.setSupplierNickName( ufHzhbxxb.getQyjc() );
                oaSupplierExtDTO.setPaymentClause( "" );
                oaSupplierExtDTO.setBourseCurrency("");
                oaSupplierExtDTO.setTermsExchange( "");

                Optional.ofNullable(idUfHzhbxxbDt4Map.get(mainId)).ifPresent( ufHzhbxxbDt4 -> {
                    oaSupplierExtDTO.setTaxpayerNo( ufHzhbxxbDt4.getNsdjh() );
                } );
                oaSupplierDTO.setOaSupplierExtDTO( oaSupplierExtDTO );


                return oaSupplierDTO;
            }).collect(Collectors.toList());

            try {
                R result = oaSupplierFeignClient.batchHandlePush(oaSupplierDTOList);
                Assert.isTrue(result.isSuccess(),result.getMsg());
            } catch (Exception ex) {
                log.error("[Supplier] remote feign client error!", ex);
            }


        } catch (Exception ex) {
            log.error("[Supplier] assemblyData error!", ex);
        }

    }


}
