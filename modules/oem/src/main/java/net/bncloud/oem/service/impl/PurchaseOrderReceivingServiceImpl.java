package net.bncloud.oem.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.bis.service.api.vo.PurchaseInStockOrderCreateVo;
import net.bncloud.common.api.R;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.oem.domain.entity.OperationLog;
import net.bncloud.oem.domain.entity.PurchaseOrder;
import net.bncloud.oem.domain.entity.PurchaseOrderMaterial;
import net.bncloud.oem.domain.entity.PurchaseOrderReceiving;
import net.bncloud.oem.domain.param.ReturnReceivingParam;
import net.bncloud.oem.domain.vo.PurchaseOrderMaterialVo;
import net.bncloud.oem.domain.vo.PurchaseOrderReceivingVo;
import net.bncloud.oem.domain.vo.PurchaseOrderVo;
import net.bncloud.oem.domain.vo.RemarkVo;
import net.bncloud.oem.enums.PurchaseOrderMaterialStatusEnum;
import net.bncloud.oem.enums.PurchaseOrderOperationLogEnum;
import net.bncloud.oem.enums.PurchaseOrderReceivingStatusEnum;
import net.bncloud.oem.mapper.PurchaseOrderMapper;
import net.bncloud.oem.mapper.PurchaseOrderMaterialMapper;
import net.bncloud.oem.mapper.PurchaseOrderReceivingMapper;
import net.bncloud.oem.service.CreatePurchaseInStockOrderService;
import net.bncloud.oem.service.OperationLogService;
import net.bncloud.oem.service.PurchaseOrderReceivingService;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ddh
 * @description
 * @since 2022/4/24
 */
@Service
@Slf4j
public class PurchaseOrderReceivingServiceImpl extends BaseServiceImpl<PurchaseOrderReceivingMapper, PurchaseOrderReceiving> implements PurchaseOrderReceivingService {
    @Autowired
    private OperationLogService operationLogService;
    @Autowired
    private PurchaseOrderReceivingMapper purchaseOrderReceivingMapper;
    @Autowired
    private PurchaseOrderMaterialMapper purchaseOrderMaterialMapper;
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private CreatePurchaseInStockOrderService createPurchaseInStockOrderService;

    /**
     * 统计已退回状态的收货
     *
     * @return
     */
    @Override
    public Integer statisticsReturnedQuantity() {
        return count(Wrappers.<PurchaseOrderReceiving>lambdaQuery()
                .eq(PurchaseOrderReceiving::getStatus, PurchaseOrderReceivingStatusEnum.RETURNED.getCode()));
    }

    @Override
    @Transactional
    public void confirmStatus(Long id) {
        PurchaseOrderReceiving purchaseOrderReceiving = this.getBaseMapper().selectById(id);
//        PurchaseOrderReceiving purchaseOrderReceiving = new PurchaseOrderReceiving();
        //purchaseOrderReceiving.setId(id);
        purchaseOrderReceiving.setStatus(PurchaseOrderReceivingStatusEnum.CONFIRM.getCode());
        this.updateById(purchaseOrderReceiving);

        //确认收货改变状态(第一层和第二层):
        maintainPurchase(purchaseOrderReceiving);

        //确认收货操作记录
        log(id, PurchaseOrderOperationLogEnum.CONFIRM_LOG.getCode());
        //确认收货回写erp
        createPurchaseInStockOrderService.createPurchaseInStockOrder(id);
    }

    @Override
    public RemarkVo queryRemark(Long id) {
        PurchaseOrderReceiving receive = this.getById(id);
        RemarkVo remarkVo = new RemarkVo();
        remarkVo.setBrandRemark(receive.getBrandRemark());
        remarkVo.setOemSupplierRemark(receive.getOemSupplierRemark());
        remarkVo.setReceiveDate(receive.getReceiveDate());
        remarkVo.setReceiveQuantity(receive.getReceiveQuantity());
        //物料
        LambdaQueryWrapper<PurchaseOrderMaterial> purchaseOrderMaterialQuery = Condition.getQueryWrapper(new PurchaseOrderMaterial())
                .lambda()
                .eq(PurchaseOrderMaterial::getId, receive.getPurchaseOrderMaterialId());
        PurchaseOrderMaterial purchaseOrderMaterial = purchaseOrderMaterialMapper.selectOne(purchaseOrderMaterialQuery);

        //采购订单
        LambdaQueryWrapper<PurchaseOrder> purchaseOrderQuery = Condition.getQueryWrapper(new PurchaseOrder())
                .lambda()
                .eq(PurchaseOrder::getId, purchaseOrderMaterial.getPurchaseOrderId());
        PurchaseOrder purchaseOrder = purchaseOrderMapper.selectOne(purchaseOrderQuery);

        remarkVo.setPurchaseOrderCode(purchaseOrder.getPurchaseOrderCode());
        return remarkVo;
    }


    /**
     * 待确认页面的批量确认按钮,
     * 因为前端不能把第一层的订单号回传,所以需要这样处理
     **/
    @Override
    public void toBeConfirmBatchConfirmReceiving(List<String> idsList) {
        //适应前端转成long类型
        List<Long> ids = new ArrayList<>();
        //需要传输的收获ids
        List<Long> receiveids = new ArrayList<>();
        for (String id : idsList) {
            ids.add(Long.parseLong(id));
            receiveids.add(Long.parseLong(id));
        }

        List<Long> materialids = new ArrayList<>();
        ids.forEach(id->{
            PurchaseOrderReceiving receive = this.getById(id);
            materialids.add(receive.getPurchaseOrderMaterialId());
        });
        ids = materialids.stream().distinct().collect(Collectors.toList());

        List<Long> orderids = new ArrayList<>();
        ids.forEach(id->{
            PurchaseOrderMaterial purchaseOrderMaterial = purchaseOrderMaterialMapper.selectById(id);
            orderids.add(purchaseOrderMaterial.getPurchaseOrderId());
        });
        ids = orderids.stream().distinct().collect(Collectors.toList());

        List<String> params = new ArrayList<>();
        for (Long id : ids) {
            params.add(id+"");
        }

        this.batchConfirmReceiving(params,receiveids);
        /*for (Long id : ids) {
            this.confirmStatus(id);
        }*/
    }

    /**
     * 统计待确认的数量
     *
     * @return
     */
    @Override
    public Integer statisticsToBeConfirmQuantity() {
        return count(Wrappers.<PurchaseOrderReceiving>lambdaQuery()
                .eq(PurchaseOrderReceiving::getStatus, PurchaseOrderReceivingStatusEnum.TO_BE_CONFIRM.getCode()));
    }

    /**
     * 退回收货
     * 1.修改收货状态为 已退回
     * 2.更新备注
     * 3.记录 操作日志
     *
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnReceiving(ReturnReceivingParam param) {
        PurchaseOrderReceiving orderReceiving = getById(param.getReceivingId());
        if (orderReceiving != null) {
            if (StringUtil.isNotBlank(param.getRemark())) {
                orderReceiving.setBrandRemark(param.getRemark());
            }
            orderReceiving.setStatus(PurchaseOrderReceivingStatusEnum.RETURNED.getCode());
            updateById(orderReceiving);
        }
        log(param.getReceivingId(), PurchaseOrderOperationLogEnum.RETURN_LOG.getCode());
    }


    /**
     * 批量确认
     *
     * @param idsList 采购单号的id的list
     */
    @Override
    @Transactional
    public void batchConfirmReceiving(List<String> idsList,List<Long> receiveids) {
        log.info("=====开始批量确认=====");

        log.info("=====转换ids=====");
        List<Long> ids = new ArrayList<>();
        for (String id : idsList) {
            ids.add(Long.parseLong(id));
        }
        log.info("=====ids为:{}", JSON.toJSONString(ids));
        log.info("=====转换id成功!=====");

        List<PurchaseOrderReceiving> purchaseOrderReceivingUpdate = new ArrayList<>();//批量更新缓存桶
        List<PurchaseInStockOrderCreateVo> PurchaseInStockOrderCreateVoList = new ArrayList<>();//批量回写采购单的createVo
        List<PurchaseOrderReceivingVo> purchaseOrderReceivingListVo =new ArrayList<>();//查询出来的收获明细的vo
        List<PurchaseOrderVo> purchaseOrderReturnVolist = new ArrayList<>();//回写srm需要的采购订单

        //构造对象
        List<PurchaseOrder> purchaseOrderList = purchaseOrderMapper.selectBatchIds(ids);
        log.info("=====ids查询的采购订单列表为:{}",JSON.toJSONString(purchaseOrderList));
        List<PurchaseOrderVo> purchaseOrderListVo = BeanUtil.copy(purchaseOrderList, PurchaseOrderVo.class);

        for (PurchaseOrderVo purchaseOrderVo : purchaseOrderListVo) {
            List<PurchaseOrderMaterial> purchaseOrderMaterialList = purchaseOrderMaterialMapper
                    .selectList(Condition.getQueryWrapper(new PurchaseOrderMaterial())
                            .lambda()
                            .eq(PurchaseOrderMaterial::getPurchaseOrderId, purchaseOrderVo.getId()));
            log.info("=====根据采购订单查询出的物料列表为:{}",JSON.toJSONString(purchaseOrderMaterialList));
            List<PurchaseOrderMaterialVo> purchaseOrderMaterialListVo = BeanUtil.copy(purchaseOrderMaterialList, PurchaseOrderMaterialVo.class);
            //List<PurchaseOrderReceivingVo> receivingDtoList = new ArrayList<>();

            log.info("=====开始根据物料查询收货明细=====");
            for (PurchaseOrderMaterialVo purchaseOrderMaterialVo : purchaseOrderMaterialListVo) {

                List<PurchaseOrderReceivingVo> purchaseOrderReceivingListVoForCreateVo =new ArrayList<>();

                LambdaQueryWrapper<PurchaseOrderReceiving> receiveLambdaQueryWrapper = Condition.getQueryWrapper(new PurchaseOrderReceiving())
                        .lambda()
                        .eq(PurchaseOrderReceiving::getPurchaseOrderMaterialId, purchaseOrderMaterialVo.getId())
                        .eq(PurchaseOrderReceiving::getStatus, PurchaseOrderReceivingStatusEnum.TO_BE_CONFIRM.getCode());

                //待确认页面的批量确认按钮的转换逻辑
                if(!receiveids.isEmpty()&&receiveids.size()>0){
                    receiveLambdaQueryWrapper.in(PurchaseOrderReceiving::getId,receiveids);
                }

                List<PurchaseOrderReceiving> purchaseOrderReceivingList = purchaseOrderReceivingMapper
                        .selectList(receiveLambdaQueryWrapper);

                log.info("=====此物料为:{}",JSON.toJSONString(purchaseOrderMaterialVo));
                log.info("=====此物料查询出的收货明细列表为:{}",JSON.toJSONString(purchaseOrderReceivingList));

                //查询出来的收获明细先判断是否为空,因为有可能此条采购订单还没有填写明细,也有可能明细的状态不是待确认,都有可能导致明细不为空
                if (!(purchaseOrderReceivingList.isEmpty()) && purchaseOrderReceivingList.size() > 0) {
                    //把可以查出来的明细的状态改变为确认
                    List<PurchaseOrderReceiving> updateStatusCollect = purchaseOrderReceivingList.stream().map(item -> {
                        item.setStatus(PurchaseOrderReceivingStatusEnum.CONFIRM.getCode());
                        return item;
                    }).collect(Collectors.toList());
                    //每次循环添加改变状态的明细,最后批量更新状态
                    purchaseOrderReceivingUpdate.addAll(updateStatusCollect);

                    //构造批量回写采购单的vo
                    List<PurchaseOrderReceivingVo> purchaseOrderReceivingListVoCopy = BeanUtil.copy(purchaseOrderReceivingList, PurchaseOrderReceivingVo.class);
                    purchaseOrderReceivingListVo.addAll(purchaseOrderReceivingListVoCopy);
                    purchaseOrderReceivingListVoForCreateVo.addAll(purchaseOrderReceivingListVoCopy);
                    //todo***********回写需要开启
                    log.info("=====开始构建此物料回写erp的vo对象");
                    PurchaseInStockOrderCreateVo purchaseInStockOrderCreateVo = createPurchaseInStockOrderService.buildVo(purchaseOrderReceivingListVoForCreateVo, purchaseOrderMaterialVo, purchaseOrderVo);
                    log.info("=====此物料vo构建成功,此物料构建的回写erp的vo对象为:{}",JSON.toJSONString(purchaseInStockOrderCreateVo));
                    PurchaseInStockOrderCreateVoList.add(purchaseInStockOrderCreateVo);

                    //回写srm需要的采购订单
                    purchaseOrderVo.setReceivingChildren(purchaseOrderReceivingListVo);
                    purchaseOrderReturnVolist.add(purchaseOrderVo);
                }

            }
        }

        if(purchaseOrderReceivingUpdate.isEmpty()||purchaseOrderReceivingUpdate.size()<=0){
            return;
        }
        //批量更新
        this.updateBatchById(purchaseOrderReceivingUpdate);

        //批量保存操作日志
        List<Long> idList = purchaseOrderReceivingUpdate.stream().map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        logBatch(idList, PurchaseOrderOperationLogEnum.CONFIRM_LOG.getCode());

        //批量更新每个物料的剩余数量和收货次数
        //批量更新状态
        for (PurchaseOrderReceiving purchaseOrderReceiving : purchaseOrderReceivingUpdate) {
            maintainPurchase(purchaseOrderReceiving);
        }


        //批量创建回写erp入库单
        //回写srm需要的采购订单的条数大于0,代表才有发送的明细
        if( !(purchaseOrderListVo.isEmpty()) && purchaseOrderListVo.size()>0){
            log.info("=====开始批量回写erp,listVo为:{}",JSON.toJSONString(purchaseOrderListVo));
            createPurchaseInStockOrderService.batchCreateStockOrder(PurchaseInStockOrderCreateVoList,purchaseOrderReceivingListVo);
        }

        log.info("=====批量确认成功!=====");

    }

    /**
     * 批量确认V2
     *
     * @param ids 采购单号的id的list
     */
    @Override
    @Transactional
    public void batchConfirmReceivingV2(List<Long> ids) {
        //构造对象
        List<PurchaseOrder> purchaseOrderList = purchaseOrderMapper.selectBatchIds(ids);
        List<PurchaseOrderVo> purchaseOrderListVo = BeanUtil.copy(purchaseOrderList, PurchaseOrderVo.class);
        for (PurchaseOrderVo purchaseOrderVo : purchaseOrderListVo) {
            List<PurchaseOrderMaterial> purchaseOrderMaterialList = purchaseOrderMaterialMapper
                    .selectList(Condition.getQueryWrapper(new PurchaseOrderMaterial())
                            .lambda()
                            .eq(PurchaseOrderMaterial::getPurchaseOrderId, purchaseOrderVo.getId()));

            List<PurchaseOrderMaterialVo> purchaseOrderMaterialListVo = BeanUtil.copy(purchaseOrderMaterialList, PurchaseOrderMaterialVo.class);
            for (PurchaseOrderMaterialVo purchaseOrderMaterialVo : purchaseOrderMaterialListVo) {
                List<PurchaseOrderReceiving> purchaseOrderReceivingList = purchaseOrderReceivingMapper
                        .selectList(Condition.getQueryWrapper(new PurchaseOrderReceiving())
                                .lambda()
                                .eq(PurchaseOrderReceiving::getPurchaseOrderMaterialId, purchaseOrderMaterialVo.getId())
                                .eq(PurchaseOrderReceiving::getStatus, PurchaseOrderReceivingStatusEnum.TO_BE_CONFIRM.getCode()));

                List<PurchaseOrderReceivingVo> purchaseOrderReceivingListVo = BeanUtil.copy(purchaseOrderReceivingList, PurchaseOrderReceivingVo.class);
                for (PurchaseOrderReceivingVo purchaseOrderReceivingVo : purchaseOrderReceivingListVo) {
                    boolean flag = true;

                    //构造vo
                    List<PurchaseOrderReceivingVo> purchaseOrderReceivingListDTO = new ArrayList();
                    purchaseOrderReceivingListDTO.add(purchaseOrderReceivingVo);
                    PurchaseInStockOrderCreateVo purchaseInStockOrderCreateVo = createPurchaseInStockOrderService.buildVo(purchaseOrderReceivingListDTO, purchaseOrderMaterialVo, purchaseOrderVo);

                    //批量创建回写erp入库单
                    try {
                        R<PurchaseInStockOrderCreateVo> purchaseInStockOrderCreateVoR = createPurchaseInStockOrderService.batchCreateStockOrderV2(purchaseInStockOrderCreateVo, purchaseOrderReceivingVo, purchaseOrderVo);
                        if (purchaseInStockOrderCreateVoR.isSuccess()) {
                            //成功,更新,并且记录此条日志
                            LambdaUpdateWrapper<PurchaseOrderReceiving> updateReceiveWrapper = Wrappers.lambdaUpdate(new PurchaseOrderReceiving())
                                    .set(PurchaseOrderReceiving::getStatus, PurchaseOrderReceivingStatusEnum.CONFIRM.getCode())
                                    .eq(PurchaseOrderReceiving::getId, purchaseOrderReceivingVo.getId());
                            this.update(updateReceiveWrapper);
                            log(purchaseOrderReceivingVo.getId(), PurchaseOrderOperationLogEnum.CONFIRM_LOG.getName());
                        } else {
                            flag = false;
                        }
                    } catch (Exception e) {
                        log.error(purchaseOrderReceivingVo.getId() + "创建采购订单失败", e);
                        flag = false;
                    }
                    if (!flag) {
                        LambdaUpdateWrapper<PurchaseOrderReceiving> updateReceiveWrapper = Wrappers.lambdaUpdate(new PurchaseOrderReceiving())
                                .set(PurchaseOrderReceiving::getStatus, "确认失败")
                                .eq(PurchaseOrderReceiving::getId, purchaseOrderReceivingVo.getId());
                        this.update(updateReceiveWrapper);
                    }
                }
            }
        }
    }

    /**
     * 批量退回
     *
     * @param ids 收货id集合
     */
    @Override
    @Transactional
    public void batchRejectReceiving(List<String> ids) {
        List<PurchaseOrderReceiving> purchaseOrderReceivingList = ids.stream().map(id -> {
            PurchaseOrderReceiving purchaseOrderReceiving = new PurchaseOrderReceiving();
            purchaseOrderReceiving.setId(Long.valueOf(id));
            purchaseOrderReceiving.setStatus(PurchaseOrderReceivingStatusEnum.RETURNED.getCode());
            return purchaseOrderReceiving;

        }).collect(Collectors.toList());
        this.updateBatchById(purchaseOrderReceivingList);

        List<Long> collect = ids.stream().map(id -> {
            return Long.valueOf(id);
        }).collect(Collectors.toList());
        logBatch(collect, PurchaseOrderOperationLogEnum.RETURN_LOG.getCode());
    }

    /**
     * 收货和退回的日志操作接口
     *
     * @param receivingId 明细单号id
     * @param operation   确认/退回
     */
    public void log(Long receivingId, String operation) {
        //收货明细
        PurchaseOrderReceiving purchaseOrderReceiving = purchaseOrderReceivingMapper.selectById(receivingId);

        //物料
        LambdaQueryWrapper<PurchaseOrderMaterial> purchaseOrderMaterialQuery = Condition.getQueryWrapper(new PurchaseOrderMaterial())
                .lambda()
                .eq(PurchaseOrderMaterial::getId, purchaseOrderReceiving.getPurchaseOrderMaterialId());
        PurchaseOrderMaterial purchaseOrderMaterial = purchaseOrderMaterialMapper.selectOne(purchaseOrderMaterialQuery);

        //采购订单
        LambdaQueryWrapper<PurchaseOrder> purchaseOrderQuery = Condition.getQueryWrapper(new PurchaseOrder())
                .lambda()
                .eq(PurchaseOrder::getId, purchaseOrderMaterial.getPurchaseOrderId());
        PurchaseOrder purchaseOrder = purchaseOrderMapper.selectOne(purchaseOrderQuery);

        SecurityUtils.getLoginInfo().ifPresent(loginInfo -> {
            OperationLog operationLog = new OperationLog().setBillId(receivingId)
                    .setOperatorContent(operation)
                    .setOperatorNo(loginInfo.getId() + "")
                    .setOperatorName(loginInfo.getName())
                    .setPurchaseOrderCode(purchaseOrder.getPurchaseOrderCode())
                    .setMaterialCode(purchaseOrderMaterial.getMaterialCode())
                    .setMaterialName(purchaseOrderMaterial.getMaterialName())
                    .setDeliveryNoteNo(purchaseOrderReceiving.getDeliveryNoteNo())
                    .setManufactureBatchNo(purchaseOrderReceiving.getManufactureBatchNo())
                    .setReceiveBatchNo(purchaseOrderReceiving.getReceiveBatchNo())
                    .setReceiveQuantity(purchaseOrderReceiving.getReceiveQuantity())
                    .setReceiveDate(purchaseOrderReceiving.getReceiveDate());
            operationLogService.save(operationLog);
        });
    }

    /**
     * 收货和退回的日志操作接口
     *
     * @param ids       明细单号id的list
     * @param operation 确认/退回
     */
    public void logBatch(List<Long> ids, String operation) {
        List<OperationLog> opList = new ArrayList();
        SecurityUtils.getLoginInfo().ifPresent(loginInfo -> {
            ids.forEach(id -> {
                OperationLog operationLog = new OperationLog().setBillId(id)
                        .setOperatorContent(operation)
                        .setOperatorNo(loginInfo.getId() + "")
                        .setOperatorName(loginInfo.getName());
                opList.add(operationLog);
            });

        });

        operationLogService.saveBatch(opList);
    }

    /**
     * 确认收货同时维护采购订单状态和物料数量的方法
     */
    public void maintainPurchase(PurchaseOrderReceiving purchaseOrderReceiving) {
        //修改物料(第二层)的剩余数量和收货次数
        //Long receiveQuantityForLong = purchaseOrderReceiving.getReceiveQuantity();//本条收货数量
        //BigDecimal receiveQuantity = new BigDecimal(receiveQuantityForLong);

        PurchaseOrderMaterial purchaseOrderMaterial = purchaseOrderMaterialMapper.selectById(purchaseOrderReceiving.getPurchaseOrderMaterialId());
        //收货次数增加1
        if (purchaseOrderMaterial.getReceivingTimes() == null) {
            purchaseOrderMaterial.setReceivingTimes(1L);
        } else {
            purchaseOrderMaterial.setReceivingTimes(purchaseOrderMaterial.getReceivingTimes() + 1L);
        }
        //Long purchaseQuantityForLong = purchaseOrderMaterial.getPurchaseQuantity();//订单数量
        //BigDecimal purchaseQuantity = new BigDecimal(purchaseQuantityForLong);
        //
        ////修改接受了的数量
        //BigDecimal reQuantity = new BigDecimal(purchaseOrderMaterial.getReceivedQuantity());
        //BigDecimal add = receiveQuantity.add(reQuantity);
        //Long receivedQuantity = add.longValue();
        //purchaseOrderMaterial.setReceivedQuantity(receivedQuantity);
        //
        ////总数量减总共收货的数量  就是剩余数量
        //BigDecimal subtract = purchaseQuantity.subtract(add);
        //int i = subtract.compareTo(BigDecimal.ZERO);
        //if (i == -1) {
        //    subtract = BigDecimal.ZERO;
        //}
        //
        //Long remainQuantity = subtract.longValue();
        //purchaseOrderMaterial.setRemainingQuantity(remainQuantity);
        //
        //String code = remainQuantity.equals(0L) ?
        //        PurchaseOrderMaterialStatusEnum.COMPLETE_RECEIVE_GOODS.getCode() :
        //        PurchaseOrderMaterialStatusEnum.PARTIAL_RECEIPT.getCode();
        //purchaseOrderMaterial.setTakeOverStatus(code);

        this.recalculateMaterialQuantity(purchaseOrderMaterial);

        purchaseOrderMaterialMapper.updateById(purchaseOrderMaterial);



        //修改第一层状态
        PurchaseOrder purchaseOrder = purchaseOrderMapper.selectById(purchaseOrderMaterial.getPurchaseOrderId());
        //同步的时候应该是待收货状态,确认操作时默认全部收货,然后下面判断是否为部分收货

        //这里有一个坑,如果已经为全部确认收货了,然后供应商那边又添加了一条明细,需要改变状态,这个要由供应商那边改
        purchaseOrder.setTakeOverStatus(PurchaseOrderMaterialStatusEnum.COMPLETE_RECEIVE_GOODS.getCode());
        List<PurchaseOrderMaterial> purchaseOrderMaterialList = purchaseOrderMaterialMapper.selectList(Condition.getQueryWrapper(new PurchaseOrderMaterial())
                .lambda()
                .eq(PurchaseOrderMaterial::getPurchaseOrderId, purchaseOrder.getId()));
        for (PurchaseOrderMaterial orderMaterial : purchaseOrderMaterialList) {
            //只要有一条不等于全部收货(部分收货/待收货),都变成部分收货,并且直接跳出循环
            if (!StrUtil.equals(orderMaterial.getTakeOverStatus(), PurchaseOrderMaterialStatusEnum.COMPLETE_RECEIVE_GOODS.getCode())) {
                purchaseOrder.setTakeOverStatus(PurchaseOrderMaterialStatusEnum.PARTIAL_RECEIPT.getCode());
                break;
            }
        }
        purchaseOrderMapper.updateById(purchaseOrder);
    }


    /**
     * 重新计算物料的数量
     * 1.从已确认状态的收货信息中重新计算已收货数量和剩余数量
     * 2.根据数量修改物料的收货状态
     *
     * @param purchaseOrderMaterial
     */
    private void recalculateMaterialQuantity(PurchaseOrderMaterial purchaseOrderMaterial) {
        //已确认的收货信息
        List<PurchaseOrderReceiving> orderReceivingList = list(Wrappers.<PurchaseOrderReceiving>lambdaQuery()
                .eq(PurchaseOrderReceiving::getPurchaseOrderMaterialId, purchaseOrderMaterial.getId())
                .eq(PurchaseOrderReceiving::getStatus, PurchaseOrderReceivingStatusEnum.CONFIRM.getCode()));
        Optional.of(orderReceivingList).ifPresent(receivingList -> {
            //某一物料已收货数量之和
            long receiveQuantitySum = receivingList.stream().mapToLong(PurchaseOrderReceiving::getReceiveQuantity).sum();
            purchaseOrderMaterial.setReceivedQuantity(receiveQuantitySum);
            //剩余数量=订单数量 - 已收货数量
            long remainingQuantity = purchaseOrderMaterial.getPurchaseQuantity() - receiveQuantitySum;
            purchaseOrderMaterial.setRemainingQuantity(remainingQuantity);
        });

        //修改物料表中的收货状态：1待收货，2部分收货，3已收货
        if (purchaseOrderMaterial.getReceivedQuantity() == 0) {
            purchaseOrderMaterial.setTakeOverStatus(PurchaseOrderMaterialStatusEnum.GOODS_TO_BE_RECEIVE.getCode());
        } else if (purchaseOrderMaterial.getReceivedQuantity() >= purchaseOrderMaterial.getPurchaseQuantity()) {
            purchaseOrderMaterial.setTakeOverStatus(PurchaseOrderMaterialStatusEnum.COMPLETE_RECEIVE_GOODS.getCode());
        } else {
            purchaseOrderMaterial.setTakeOverStatus(PurchaseOrderMaterialStatusEnum.PARTIAL_RECEIPT.getCode());
        }

    }

}
