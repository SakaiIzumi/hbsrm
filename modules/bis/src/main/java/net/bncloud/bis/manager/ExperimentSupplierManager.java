package net.bncloud.bis.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.constant.BisSyncConstants;
import net.bncloud.bis.constant.DatasourceConstants;
import net.bncloud.bis.dao.ExperimentSupplierDao;
import net.bncloud.bis.model.entity.ExperimentSupplier;
import net.bncloud.common.api.R;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.common.base.globallock.lock.LockWrapper;
import net.bncloud.common.exception.Asserts;
import net.bncloud.service.api.platform.config.ConfigParamOpenFeign;
import net.bncloud.service.api.platform.config.enums.CfgParamKeyEnum;
import net.bncloud.service.api.platform.config.vo.CfgParamInfo;
import net.bncloud.service.api.platform.config.vo.configvo.DeliveryCollaborationMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * desc: 试点供应商manager  一个个供应商的目的是为了区分每个供应商的同步时间处理
 *
 * @author Rao
 * @Date 2022/02/22
 **/
@DS(DatasourceConstants.BIS)
@Slf4j
@Component
public class ExperimentSupplierManager {

    @Resource
    private ExperimentSupplierDao experimentSupplierDao;
    @Resource
    private PurchaseOrderManager purchaseOrderManager;
    @Resource
    private PurchaseInStockOrderManager purchaseInStockOrderManager;
    @Resource
    private DistributedLock distributedLock;
    @Resource
    private MrpDeliveryPlanManager mrpDeliveryPlanManager;
    @Resource
    private ConfigParamOpenFeign configParamOpenFeign;

    /**
     * 正在同步异常
     */
    private static final RuntimeException SYNC_EXCEPTION = new RuntimeException("正在同步！");

    /**
     * 试点供应商同步采购订单
     * @param params
     */
    public void experimentSupplierSyncPurchaseOrder(String params) {

        String lockKey = BisSyncConstants.SYNC_LOCK_PREFIX_KEY + BisSyncConstants.PURCHASE_ORDER_SYNC_ERP_TASK + "experimentSupplierSyncPurchaseOrder";
        LockWrapper lockWrapper = LockWrapper.builder().key(lockKey).leaseTime(30).unit(TimeUnit.MINUTES).build();
        distributedLock.tryLock( lockWrapper, () -> {
            this.doGetExperimentSupplierAfterExecute( params,experimentSupplier -> purchaseOrderManager.experimentSupplierSyncPurchaseOrder(experimentSupplier ) );
            return "执行成功！";
        },() -> SYNC_EXCEPTION);

    }

    /**
     * 试点供应商同步采购入库单
     * @param params
     */
    public void experimentSupplierSyncInStockOrder(String params) {
        String lockKey = BisSyncConstants.SYNC_LOCK_PREFIX_KEY + BisSyncConstants.PURCHASE_IN_STOCK_ORDER_SYNC_ERP_TASK + "experimentSupplierSyncPurchaseOrder";
        LockWrapper lockWrapper = LockWrapper.builder().key(lockKey).leaseTime(30).unit(TimeUnit.MINUTES).build();
        distributedLock.tryLock( lockWrapper, () -> {
            this.doGetExperimentSupplierAfterExecute( params,experimentSupplier -> purchaseInStockOrderManager.experimentSupplierSyncPurchaseInStockOrder( experimentSupplier));
            return "执行成功！";
        },() -> SYNC_EXCEPTION);

    }

    /**
     * 获取试点供应商列表
     * @param supplierCodeList 供应商code
     * @return
     */
    private List<ExperimentSupplier> getExperimentSupplierList(List<String> supplierCodeList){

        // 留意取反
        boolean supplierCodeListNotEmpty = ! CollectionUtils.isEmpty(supplierCodeList);
        List<ExperimentSupplier> experimentSuppliers = experimentSupplierDao.selectList(
                Wrappers.<ExperimentSupplier>lambdaQuery()
                        // 非禁用的
                        .eq(ExperimentSupplier::getStatus, 0)
                        // 非空
                        .in( supplierCodeListNotEmpty, ExperimentSupplier::getSupplierCode,supplierCodeList )
        );

        boolean experimentSupplierListEmpty = CollectionUtils.isEmpty( experimentSuppliers);
        if( supplierCodeListNotEmpty && experimentSupplierListEmpty ){
            XxlJobHelper.log("[getExperimentSupplierList] 未查询到相关的试点供应商信息，请先添加试点供应商数据，即可指定同步这个供应商的数据, codes:{} ", JSON.toJSONString( supplierCodeList ));
        }
        // 校验是否存在
        else if( supplierCodeListNotEmpty ) {
            Set<String> supplierCodeSet = experimentSuppliers.stream().map(ExperimentSupplier::getSupplierCode).collect(Collectors.toSet());
            supplierCodeList.forEach(code -> {
                if(! supplierCodeSet.contains( code ) ){
                    XxlJobHelper.log("[getExperimentSupplierCodeSet] 参数内的code值在数据库中未查找到，此打印表示该code [{}]同步失败 ",code);
                }
            } );
        }

        return experimentSuppliers;

    }

    /**
     * 初始化供应商的采购订单
     * @param params
     */
    public void initExperimentSupplierSyncPurchaseOrder(String params) {

        LockWrapper lockWrapper = new LockWrapper().setKey(BisSyncConstants.SYNC_LOCK_PREFIX_KEY + BisSyncConstants.INIT_EXPERIMENT_SUPPLIER_SYNC_PURCHASE_ORDER).setWaitTime(0).setLeaseTime(10).setUnit(TimeUnit.MILLISECONDS);

        distributedLock.tryLock(lockWrapper, () -> {
            this.doGetExperimentSupplierAfterExecute(params, experimentSupplier -> {

                // 订单和送货计划
                purchaseOrderManager.initExperimentSupplierSyncPurchaseOrder(experimentSupplier);

            });
            return "success!";
        }, () -> "no!");

    }


    /**
     * 处理
     * @param params  依据params参数指定需要同步的供应商，若params为空，则同步所有试点供应商的单据；若params指定了code，则从试点供应商表尝试获取，若存在则同步，不存在的则表示不属于试点供应商，不予同步。
     */
    public void doGetExperimentSupplierAfterExecute(String params, Consumer<ExperimentSupplier> experimentSupplierConsumer ){

        List<String> supplierCodeList = new ArrayList<>();
        if(StrUtil.isNotBlank( params )){
            supplierCodeList = Arrays.asList( params.split(",") );
        }

        List<ExperimentSupplier> experimentSupplierList = this.getExperimentSupplierList(supplierCodeList);
        log.info("doGetExperimentSupplierAfterExecute experimentSupplierList: {}",experimentSupplierList);
        experimentSupplierList.forEach( experimentSupplier -> {
            try {
                experimentSupplierConsumer.accept( experimentSupplier );
            }catch (Exception ex){
                log.error("[doGetExperimentSupplierAfterExecute] error! ",ex);
                XxlJobHelper.log( ex);
            }

        } );


    }

    /**
     * 试点供应商同步 mrp计划订单
     * @param computerNo 运算编码
     * @return
     */
    public R<Object> experimentSupplierSyncMrpPlanOrder(String computerNo) {
        Asserts.isTrue( StrUtil.isNotBlank( computerNo ),"运算编码未填！");

        // 查询送货协同方式配置
        R<CfgParamInfo> cfgParamInfoR = configParamOpenFeign.findListByCodeAndOrgId( CfgParamKeyEnum.DELIVERY_DELIVERY_COLLABORATION_METHOD, 112L);
        Assert.isTrue( cfgParamInfoR.isSuccess() && cfgParamInfoR.getData() != null,"获取送货协同方式配置失败！" );
        DeliveryCollaborationMethod deliveryCollaborationMethod = JSON.parseObject( cfgParamInfoR.getData().getValue(), DeliveryCollaborationMethod.class);

        log.info("[计划订单同步] deliveryCollaborationMethod ：{}", JSON.toJSONString( deliveryCollaborationMethod));
        Asserts.isTrue( deliveryCollaborationMethod.planSchedulingValue(),"计划排程未启用，同步失败！" );

        String lockKey = BisSyncConstants.SYNC_LOCK_PREFIX_KEY + BisSyncConstants.PLAN_ORDER_SYNC_ERP_TASK + "_experimentSupplierSyncMrpPlanOrder";
        LockWrapper lockWrapper = LockWrapper.builder().key(lockKey).leaseTime(30).unit(TimeUnit.MINUTES).build();
        return distributedLock.tryLock(lockWrapper, () -> {
            List<ExperimentSupplier> experimentSupplierList = this.getExperimentSupplierList(new ArrayList<>());
            XxlJobHelper.log("同步试点供应商的计划订单 [{}]", JSON.toJSONString(experimentSupplierList));
            return mrpDeliveryPlanManager.syncPlanOrder(experimentSupplierList,computerNo);
        }, () -> R.success("正在同步中！"));
    }
}
