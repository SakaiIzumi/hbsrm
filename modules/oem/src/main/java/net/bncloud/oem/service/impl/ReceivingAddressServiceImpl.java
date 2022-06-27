package net.bncloud.oem.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.bizutil.number.NumberFactory;
import net.bncloud.common.bizutil.number.NumberType;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.oem.domain.entity.OperationLog;
import net.bncloud.oem.domain.entity.PurchaseOrder;
import net.bncloud.oem.domain.entity.ReceivingAddress;
import net.bncloud.oem.domain.param.ReceivingAddressParam;
import net.bncloud.oem.domain.vo.ReceivingAddressVo;
import net.bncloud.oem.enums.AddressOperationLogEnum;
import net.bncloud.oem.enums.PurchaseOrderAddressStatus;
import net.bncloud.oem.mapper.PurchaseOrderMapper;
import net.bncloud.oem.mapper.ReceivingAddressMapper;
import net.bncloud.oem.service.OperationLogService;
import net.bncloud.oem.service.ReceivingAddressService;
import net.bncloud.oem.service.api.vo.OemReceivingAddressVo;
import net.bncloud.oem.wrapper.ReceivingAddressWrapper;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author ddh
 * @description
 * @since 2022/4/24
 */
@Slf4j
@Service
public class ReceivingAddressServiceImpl extends BaseServiceImpl<ReceivingAddressMapper, ReceivingAddress> implements ReceivingAddressService {

    /*@Autowired
    private ReceivingAddressMapper receivingAddressMapper;*/

    @Autowired
    private OperationLogService operationLogService;
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private NumberFactory numberFactory;

    @Override
    public IPage<ReceivingAddressVo> selectPage(IPage<ReceivingAddress> page, QueryParam<ReceivingAddressParam> param) {
        List<ReceivingAddress> receivingAddressList= baseMapper.selectListPage(page,param);
        page.setRecords(receivingAddressList);
        IPage<ReceivingAddressVo> addressPageVO = ReceivingAddressWrapper.build().pageVO(page);
        return addressPageVO;
    }

    @Override
    public void updateSupplier(ReceivingAddressParam param) {
        if( StrUtil.isBlank(param.getSupplierCode())||StrUtil.isBlank(param.getSupplierName()) ){
            throw new ApiException(500,"请填写正确的供应商");
        }
        LambdaQueryWrapper<ReceivingAddress> eq = Condition
                .getQueryWrapper(new ReceivingAddress())
                .lambda()
                .eq(ReceivingAddress::getId, param.getId());
        ReceivingAddress receivingAddress = baseMapper.selectOne(eq);

        String logContent=null;
        String remark=null;
        if(StrUtil.isBlank(receivingAddress.getSupplierCode())){
            //为空是新建
             logContent = AddressOperationLogEnum.CREATE_LOG.getName();
            remark=param.getSupplierCode()+" "+param.getSupplierName();
        }else{
            //不为空是修改
            logContent = AddressOperationLogEnum.EDIT_LOG.getName();
            remark="【"+receivingAddress.getSupplierCode()+"】"+"改为"+"【"+param.getSupplierCode()+"】"+
                    " "+"【"+receivingAddress.getSupplierName()+"】"+"改为"+"【"+param.getSupplierName()+"】";
        }

        receivingAddress.setStatus(PurchaseOrderAddressStatus.HAVE_MAINTAINED.getCode());
        receivingAddress.setSupplierCode(param.getSupplierCode());
        receivingAddress.setSupplierName(param.getSupplierName());
        baseMapper.updateById(receivingAddress);

        //更新后维护订单(第一层)的OEM供应商
        List<PurchaseOrder> purchaseOrderList = purchaseOrderMapper.selectList(Wrappers.<PurchaseOrder>lambdaQuery().eq(PurchaseOrder::getReceivingAddressCode, receivingAddress.getCode()));
        Optional.of(purchaseOrderList).ifPresent(purchaseOrders -> {
            purchaseOrders.forEach(purchaseOrder -> {
                purchaseOrder.setOemSupplierCode(param.getSupplierCode());
                purchaseOrder.setOemSupplierName(param.getSupplierName());
                purchaseOrderMapper.updateById(purchaseOrder);
            });
        });

        //操作记录
        saveLog(receivingAddress,logContent,remark);
    }

    @Override
    public void batchUpdateSupplier(List<ReceivingAddressParam> receivingAddressParamList) {
        if( !(receivingAddressParamList.size()>0) ){
            throw new ApiException(500,"请批量编辑供应商后再点击批量保存");
        }
        List<ReceivingAddress> receivingAddressesList = new ArrayList<>();
        for (ReceivingAddressParam receivingAddressParam : receivingAddressParamList) {
            this.updateSupplier(receivingAddressParam);
        }
    }

    /**
     *
     * 记录地址的维护操作日志
     *
     * */
    public void saveLog(ReceivingAddress receivingAddress,String content,String remark){
        BaseUserEntity user = AuthUtil.getUser();

        OperationLog log = OperationLog.builder()
                .billId(receivingAddress.getId())
                .operatorContent(content)
                .operatorName(user.getUserName())
                .operatorNo(user.getUserId() + "")
                .address(receivingAddress.getAddress())
                .addressCode(receivingAddress.getCode())
                .remark(remark)
                .build();

        operationLogService.save( log);
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void syncOemReceivingAddress(List<OemReceivingAddressVo> oemReceivingAddressVoList) {

        oemReceivingAddressVoList.forEach( oemReceivingAddressVo -> {

            ReceivingAddress receivingAddress = this.getByCode(oemReceivingAddressVo.getCode());

            if( receivingAddress == null) {
                ReceivingAddress receivingAddressInsert = new ReceivingAddress();
                BeanUtil.copy(oemReceivingAddressVo,receivingAddressInsert);

//                receivingAddressInsert.setCode(oemReceivingAddressVo.getCode())
                receivingAddressInsert .setStatus("0");
                this.save( receivingAddressInsert);

                return;
            }

            //更新逻辑
            //receivingAddress.setStatus("0");
            //this.updateById(receivingAddress);

        });

    }

    /**
     * 同步甲供物料订单收货协同地址信息
     * 使用这个方法同步地址
     **/
    @Override
    public Map<String, ReceivingAddress> syncOemAddress(List<String> addressList) {
//        Set<Map.Entry<String, String>> entries = addrMap.entrySet();
        Map<String, ReceivingAddress> addrMap = new HashMap<>();
        try (SqlSession batchSqlSession = sqlSessionBatch()) {

            for (String address: addressList) {
                //根据地址查询是否存在数据库

                //todo 正常只有一个,测试这里先默认返回多个,后续要把旧数据清空
                List<ReceivingAddress> addrForSelectList = this.list(Wrappers
                        .<ReceivingAddress>lambdaQuery()
                        .eq(ReceivingAddress::getAddress, address));
                ReceivingAddress addrForSelect=null;
                if( !(addrForSelectList.isEmpty()) && addrForSelectList.size()>0 ){
                    addrForSelect = addrForSelectList.get(0);
                }


                if (ObjectUtil.isNotEmpty(addrForSelect)) {
                    //不为空,更新
                    addrMap.put(address,addrForSelect);

                    addrForSelect.setLastModifiedDate(new Date());
                    addrForSelect.setIsDeleted(0);
                    MapperMethod.ParamMap<ReceivingAddress> param = new MapperMethod.ParamMap<>();
                    param.put(Constants.ENTITY, addrForSelect);
                    batchSqlSession.update(sqlStatement(SqlMethod.UPDATE_BY_ID), param);
                    //只更新日期吗?还有什么字段需要修改

                } else {
                    //为空,新增
                    ReceivingAddress receivingAddress = new ReceivingAddress();
                    receivingAddress.setAddress(address);

                    //生成地址编码
                    String addressCode = numberFactory.buildNumber(NumberType.oem_address_template);
                    receivingAddress.setCode(addressCode);



                    receivingAddress.setCreatedBy(-1L);
                    receivingAddress.setLastModifiedBy(-1L);

                    Date now = DateUtil.now();
                    receivingAddress.setLastModifiedDate(now);
                    receivingAddress.setCreatedDate(now);
                    receivingAddress.setIsDeleted(0);
                    batchSqlSession.insert(sqlStatement(SqlMethod.INSERT_ONE), receivingAddress);

                    addrMap.put(address,receivingAddress);
                }
            }
            batchSqlSession.flushStatements();
        }
        return addrMap;
    }



    /**
     * 通过code获取
     * @param code
     * @return
     */
    @Override
    public ReceivingAddress getByCode(String code) {
        return this.getOne(Wrappers.<ReceivingAddress>lambdaQuery().eq(ReceivingAddress::getCode, code));
    }

}
