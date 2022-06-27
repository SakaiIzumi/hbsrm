package net.bncloud.oem.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.bizutil.number.NumberFactory;
import net.bncloud.common.bizutil.number.NumberType;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.oem.config.DownloadReceivingAddressModuleConfig;
import net.bncloud.oem.domain.entity.*;
import net.bncloud.oem.domain.param.PurchaseOrderParam;
import net.bncloud.oem.domain.param.PurchaseOrderReceivingParam;
import net.bncloud.oem.domain.vo.*;
import net.bncloud.oem.enums.PurchaseOrderMaterialStatusEnum;
import net.bncloud.oem.enums.PurchaseOrderReceivingOperateType;
import net.bncloud.oem.enums.PurchaseOrderReceivingStatusEnum;
import net.bncloud.oem.enums.PurchaseOrderReceivingStatusOperateRel;
import net.bncloud.oem.listener.ImportReceivingAddressListener;
import net.bncloud.oem.mapper.PurchaseOrderMapper;
import net.bncloud.oem.service.*;
import net.bncloud.oem.service.api.vo.OemPurchaseOrderVo;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.support.Condition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ddh
 * @description
 * @since 2022/4/24
 */
@Slf4j
@Service
@SuppressWarnings({"all"})
public class PurchaseOrderServiceImpl extends BaseServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {
    @Autowired
    private PurchaseOrderReceivingService receivingService;
    @Autowired
    private OperationLogService operationLogService;
    @Resource
    private PurchaseOrderMapper purchaseOrderMapper;
    @Autowired
    private PurchaseOrderMaterialService materialService;
    @Autowired
    private AttachmentRelService attachmentRelService;
    @Autowired
    private ReceivingAddressService receivingAddressService;

    @Autowired
    private DownloadReceivingAddressModuleConfig downloadModuleConfig;

    @Resource
    @Lazy
    private PurchaserFeignClient purchaserFeignClient;

    @Autowired
    private NumberFactory numberFactory;


    /**
     * oem供应商端的订单列表
     * 1.根据登录账号对订单进行数据过滤
     * 数据过滤：当前供应商只能看到自己的采购订单，一个OEM供应商 对应多个地址，一个地址对应一个采购订单
     *
     * @param page
     * @param queryParam
     * @return
     */
    @Override
    public PageImpl<PurchaseOrderVo> selectOemSupplierPage(IPage<PurchaseOrderVo> page, QueryParam<PurchaseOrderParam> queryParam) {
        HashMap<String, Boolean> map = Maps.newHashMap();
        map.put("receipt", false);
        List<PurchaseOrderVo> purchaseOrderVos = purchaseOrderMapper.selectOemSupplierPageList(page, queryParam);
        if (CollectionUtil.isNotEmpty(purchaseOrderVos)) {
            purchaseOrderVos.forEach(orderVo -> {
                orderVo.setPurchaseOrderId(orderVo.getId());
                List<PurchaseOrderMaterial> materialList = materialService.list(Wrappers.<PurchaseOrderMaterial>lambdaQuery().eq(PurchaseOrderMaterial::getPurchaseOrderId, orderVo.getId()));
                List<PurchaseOrderMaterialVo> materialVos = BeanUtil.copy(materialList, PurchaseOrderMaterialVo.class);
                materialVos.forEach(materialVo -> {
                    //冗余字段：采购单号
                    materialVo.setPurchaseOrderNum(orderVo.getPurchaseOrderCode());
                    //冗余字段：物料收货状态
                    materialVo.setMaterialReceiptStatus(materialVo.getTakeOverStatus());
                    //收货按钮
                    materialVo.setReceiptButton(map);
                    if (!PurchaseOrderMaterialStatusEnum.COMPLETE_RECEIVE_GOODS.getCode().equals(materialVo.getTakeOverStatus())) {
                        map.put("receipt", true);
                        materialVo.setReceiptButton(map);
                    }
                });
                orderVo.setChildren(materialVos);
            });

        }
        return PageUtils.result(page.setRecords(purchaseOrderVos));
    }

    /**
     * 分页查询采购订单
     * <p>
     * 采购端和供应商共用的分页方法,采购端需要查询三层,供应商两层
     * <p>
     * 两端查询的数据不影响,第三层供应商端不显示就可以了
     *
     * @param page
     * @param queryParam
     * @return
     */
    @Override
    public PageImpl<PurchaseOrderVo> selectList(IPage<PurchaseOrderVo> page, QueryParam<PurchaseOrderParam> queryParam) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("receipt", true);
        //第一层 采购订单
        List<PurchaseOrderVo> purchaseOrderVos = purchaseOrderMapper.selectPage(page, queryParam);
        purchaseOrderVos.forEach(item->item.setPurchaseOneLayer("1"));
        //第一层的mapper里面是连表查,如果有多条物料,会查出多张订单,所以,去重
        //purchaseOrderVos = purchaseOrderVos.stream().distinct().collect(Collectors.toList());
        //page.setTotal(purchaseOrderVos.size());

        //第二层 设置采购订单对应的物料
        for (PurchaseOrderVo vo : purchaseOrderVos) {


//        purchaseOrderVos.forEach(vo -> {

            //查询地址
            LambdaQueryWrapper<ReceivingAddress> addressQuery = Condition.getQueryWrapper(new ReceivingAddress())
                    .lambda()
                    .eq(ReceivingAddress::getCode, vo.getReceivingAddressCode());
            List<ReceivingAddress> addressList = receivingAddressService.list(addressQuery);
            if (addressList != null && addressList.size() > 0) {
                vo.setAddress(addressList.get(0).getAddress());
            }


            List<PurchaseOrderMaterial> materials = materialService.list(Wrappers.<PurchaseOrderMaterial>lambdaQuery().eq(PurchaseOrderMaterial::getPurchaseOrderId, vo.getId()));
            List<PurchaseOrderMaterialVo> materialVos = BeanUtil.copy(materials, PurchaseOrderMaterialVo.class);
            //第三层 设置物料对应的收货地址
            for (PurchaseOrderMaterialVo item : materialVos) {

//            materialVos.forEach(item -> {
                //冗余字段 采购单号
                item.setPurchaseOrderNum(vo.getPurchaseOrderCode());

                item.setReceiptButton(map);
                //收货按钮
                if (PurchaseOrderMaterialStatusEnum.COMPLETE_RECEIVE_GOODS.getCode().equals(item.getTakeOverStatus())) {
                    item.setReceiptButton(map);
                }

                LambdaQueryWrapper<PurchaseOrderReceiving> eq = Wrappers.<PurchaseOrderReceiving>lambdaQuery()
                        .eq(PurchaseOrderReceiving::getPurchaseOrderMaterialId, item.getId())
                        .eq(PurchaseOrderReceiving::getIsDeleted, "0");

                List<PurchaseOrderReceiving> receivinglist = receivingService.list(eq);
                if (CollectionUtil.isNotEmpty(receivinglist)) {
                    List<PurchaseOrderReceivingVo> PurchaseOrderReceivingVoList = BeanUtil.copy(receivinglist, PurchaseOrderReceivingVo.class);
                    //构建附件
                    for (PurchaseOrderReceivingVo purchaseOrderReceivingVo : PurchaseOrderReceivingVoList) {
                        buildAttachment(purchaseOrderReceivingVo);
                    }

                    //构建按钮
                    for (PurchaseOrderReceivingVo receiveVo : PurchaseOrderReceivingVoList) {
                        addPermissionButton(receiveVo);
                    }
                    //设置
                    item.setChildren(PurchaseOrderReceivingVoList);
                    item.setPermissionButton(addPermissionButtonFirstAndSecondStage());
                }

//            });
            }
            vo.setChildren(materialVos);
            vo.setPermissionButton(addPermissionButtonFirstAndSecondStage());
//        });
        }
        return PageUtils.result(page.setRecords(purchaseOrderVos));
    }


    @Override
    public PageImpl<ToBeConfirmVo> selectToBeConfirmList(IPage<ToBeConfirmVo> page, QueryParam<PurchaseOrderParam> queryParam) {
        List<ToBeConfirmVo> ToBeConfirmVoList = purchaseOrderMapper.selectToBeConfirmList(page, queryParam);
        for (ToBeConfirmVo toBeConfirmVo : ToBeConfirmVoList) {
            //地址查询
            //查询地址
            LambdaQueryWrapper<ReceivingAddress> addressQuery = Condition.getQueryWrapper(new ReceivingAddress())
                    .lambda()
                    .eq(ReceivingAddress::getCode, toBeConfirmVo.getReceivingAddressCode());
            List<ReceivingAddress> addressList = receivingAddressService.list(addressQuery);
            if (addressList != null && addressList.size() > 0) {
                toBeConfirmVo.setAddress(addressList.get(0).getAddress());
            }
            //权限按钮
            Boolean comparisonDisplay = false;
            if (StrUtil.isNotBlank(toBeConfirmVo.getOemSupplierRemark()) || StrUtil.isNotBlank(toBeConfirmVo.getBrandRemark())) {
                comparisonDisplay = true;
            }
            toBeConfirmVo.setPermissionButton(PurchaseOrderReceivingStatusOperateRel.operations(comparisonDisplay, PurchaseOrderReceivingStatusOperateRel.TO_BE_CONFIRM));


            //设置附件
            List<FileInfo> fileInfos = Lists.newArrayList();
            List<AttachmentRel> attachmentRels = attachmentRelService.list(Wrappers.<AttachmentRel>lambdaQuery().eq(AttachmentRel::getBusinessFormId, toBeConfirmVo.getRid()));
            Optional.of(attachmentRels).ifPresent(attachmentRelList->{
                attachmentRelList.forEach(attachmentRel -> {
                    FileInfo fileInfo = new FileInfo().setId(attachmentRel.getAttachmentId())
                            .setFilename(attachmentRel.getAttachmentName())
                            .setOriginalFilename(attachmentRel.getAttachmentName())
                            .setSize(attachmentRel.getAttachmentSize())
                            .setUrl(attachmentRel.getAttachmentUrl());
                    fileInfos.add(fileInfo);
                });
            });
            toBeConfirmVo.setAttachmentList(fileInfos);
        }

        return PageUtils.result(page.setRecords(ToBeConfirmVoList));
    }

    /**
     * 采购订单详情
     * 1.详情信息
     *
     * @param id
     * @return
     */
    @Override
    public PurchaseOrderVo getOrderInfo(Long id) {
        PurchaseOrder order = getById(id);
        if (order == null) {
            return null;
        }
        PurchaseOrderVo purchaseOrderVo = BeanUtil.copy(order, PurchaseOrderVo.class);
        List<PurchaseOrderMaterial> materials = materialService.list(Wrappers.<PurchaseOrderMaterial>lambdaQuery()
                .eq(PurchaseOrderMaterial::getPurchaseOrderId, purchaseOrderVo.getId()));
        if (CollectionUtil.isNotEmpty(materials)) {
            List<PurchaseOrderMaterialVo> materialVos = BeanUtil.copy(materials, PurchaseOrderMaterialVo.class);

            List<PurchaseOrderMaterialVo> orderMaterialVos = materialVos.stream().sorted(Comparator.comparing(PurchaseOrderMaterialVo::getMaterialCode)).collect(Collectors.toList());
            orderMaterialVos.forEach(vo -> {
                vo.setPurchaseOrderCode(purchaseOrderVo.getPurchaseOrderCode());
                vo.setMaterialReceiptStatus(vo.getTakeOverStatus());
            });
            purchaseOrderVo.setChildren(orderMaterialVos);
        }
        return purchaseOrderVo;
    }

    /**
     * 获取采购订单下的所有收货
     *
     * @param id
     * @return
     */
    @Override
    public List<ReceivingRecordsVo> getReceivingByOrderId(Long id) {

        PurchaseOrder purchaseOrder = getById(id);

        ArrayList<ReceivingRecordsVo> receivingVoList = Lists.newArrayList();
        //物料信息
        List<PurchaseOrderMaterial> orderMaterials = materialService.list(Wrappers.<PurchaseOrderMaterial>lambdaQuery().eq(PurchaseOrderMaterial::getPurchaseOrderId, id));
        Optional.ofNullable(orderMaterials).ifPresent(materials -> {
            materials.forEach(material -> {
                List<PurchaseOrderReceiving> receivingList = receivingService.list(Wrappers.<PurchaseOrderReceiving>lambdaQuery().eq(PurchaseOrderReceiving::getPurchaseOrderMaterialId, material.getId()));
                List<ReceivingRecordsVo> receivingVos = BeanUtil.copy(receivingList, ReceivingRecordsVo.class);
                //设置按钮
                receivingVos.forEach(receivingVo -> {
                    receivingVo.setPurchaseOrderCode(purchaseOrder.getPurchaseOrderCode());
                    receivingVo.setRemainingQuantity(material.getRemainingQuantity());
                    receivingVo.setMaterialCode(material.getMaterialCode());
                    receivingVo.setMaterialName(material.getMaterialName());
                    receivingVo.setBarCode(material.getBarCode());
                    //非已确认
                    if (!PurchaseOrderReceivingStatusEnum.CONFIRM.getCode().equals(receivingVo.getStatus())) {
                        ConcurrentMap<String, Boolean> map = Maps.newConcurrentMap();
                        map.put("edit", true);
                        map.put("delete", true);
                        receivingVo.setOperationButton(map);
                    }

                    ArrayList<FileInfo> fileInfos = Lists.newArrayList();
                    //附件列表
                    List<AttachmentRel> attachmentRels = attachmentRelService.list(Wrappers.<AttachmentRel>lambdaQuery().eq(AttachmentRel::getBusinessFormId, receivingVo.getId()));
                    attachmentRels.forEach(attachmentRel -> {
                        FileInfo fileInfo = new FileInfo().setOriginalFilename(attachmentRel.getAttachmentName())
                                .setUrl(attachmentRel.getAttachmentUrl())
                                .setSize(attachmentRel.getAttachmentSize())
                                .setId(attachmentRel.getAttachmentId())
                                .setFilename(attachmentRel.getAttachmentName());
                        fileInfos.add(fileInfo);
                    });
                    receivingVo.setAttachmentList(fileInfos);
                    receivingVoList.add(receivingVo);
                });
            });
        });
        return receivingVoList;
    }

    /**
     * 批量删除收货
     * 1.删除收货信息
     * 2.修改物料表中的 数量 和 收货状态：1待收货，2部分收货，3已收货
     * 3记录操作日志
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteReceiving(List<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            ids.forEach(id -> {
                //收货信息
                PurchaseOrderReceiving orderReceiving = receivingService.getById(id);

                //物料信息
                PurchaseOrderMaterial orderMaterial = materialService.getById(orderReceiving.getPurchaseOrderMaterialId());
                //已收货数量
                long receivedQuantity = orderMaterial.getReceivedQuantity() - orderReceiving.getReceiveQuantity();
                orderMaterial.setReceivedQuantity(receivedQuantity);
                //剩余数量
                long remainingQuantity = orderMaterial.getPurchaseQuantity() - receivedQuantity;
                orderMaterial.setRemainingQuantity(remainingQuantity);
                //修改物料表中的收货状态：1待收货，2部分收货，3已收货
                if (orderMaterial.getReceivedQuantity() == 0) {
                    orderMaterial.setTakeOverStatus(PurchaseOrderMaterialStatusEnum.GOODS_TO_BE_RECEIVE.getCode());
                } else if (orderMaterial.getReceivedQuantity() >= orderMaterial.getPurchaseQuantity()) {
                    orderMaterial.setTakeOverStatus(PurchaseOrderMaterialStatusEnum.COMPLETE_RECEIVE_GOODS.getCode());
                } else {
                    orderMaterial.setTakeOverStatus(PurchaseOrderMaterialStatusEnum.PARTIAL_RECEIPT.getCode());
                }
                materialService.updateById(orderMaterial);

                //采购订单
                PurchaseOrder purchaseOrder = getById(orderMaterial.getPurchaseOrderId());

                //操作日志
                SecurityUtils.getCurrentUser().ifPresent(currentUser -> {
                    OperationLog operationLog = new OperationLog()
                            .setBillId(purchaseOrder.getId())
                            .setOperatorNo(currentUser.getId() + "")
                            .setOperatorName(currentUser.getName())
                            .setOperatorContent("删除收货")
                            .setPurchaseOrderCode(purchaseOrder.getPurchaseOrderCode())
                            .setMaterialCode(orderMaterial.getMaterialCode())
                            .setMaterialName(orderMaterial.getMaterialName())
                            .setDeliveryNoteNo(orderReceiving.getDeliveryNoteNo())
                            .setManufactureBatchNo(orderReceiving.getManufactureBatchNo())
                            .setReceiveBatchNo(orderReceiving.getReceiveBatchNo())
                            .setReceiveQuantity(orderReceiving.getReceiveQuantity())
                            .setReceiveDate(orderReceiving.getReceiveDate());
                    operationLogService.save(operationLog);
                });

                //删除收货
                receivingService.removeById(id);
            });
        }
    }

    /**
     * 批量  新增或修改（根据有无收货id判断是新增还是修改）
     * 1.新增或修改收货信息
     * 2.修改物料表中的 数量 和 收货状态：1待收货，2部分收货，3已收货
     * 3.收货确认状态 修改为待确认；(已确认的收货不允许编辑,不需考虑，控制按钮即可）
     * 3.保存附件列表
     * 4.记录操作日志
     *
     * @param params
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveReceiving(List<PurchaseOrderReceivingParam> params) {
        if (CollectionUtil.isNotEmpty(params)) {
            params.forEach(receivingVo -> {
                PurchaseOrderReceiving receiving = BeanUtil.copy(receivingVo, PurchaseOrderReceiving.class);
                //设置收货确认状态为: 待确认
                receiving.setStatus(PurchaseOrderReceivingStatusEnum.TO_BE_CONFIRM.getCode());
                //操作内容
                String operationContent = null;
                //修改
                if (receivingVo.getId() != null) {
                    operationContent = "编辑收货信息";
                    receivingService.updateById(receiving);
                } else { //新增
                    operationContent = "新增收货信息";
                    receivingService.save(receiving);
                }


                ////修改物料的数量和收货状态
                PurchaseOrderMaterial orderMaterial = materialService.getById(receivingVo.getPurchaseOrderMaterialId());
                ////收货数量
                //long receiveQuantity = orderMaterial.getReceivedQuantity() + receiving.getReceiveQuantity();
                //orderMaterial.setReceivedQuantity(receiveQuantity);
                ////剩余数量
                //long remainingQuantity = orderMaterial.getPurchaseQuantity() - receiveQuantity;
                //orderMaterial.setRemainingQuantity(remainingQuantity);

                //materialService.updateById(orderMaterial);

                //构造采购订单状态(第一层)
                PurchaseOrder purchaseOrder = this.getById(orderMaterial.getPurchaseOrderId());
                purchaseOrder.setTakeOverStatus(PurchaseOrderMaterialStatusEnum.GOODS_TO_BE_RECEIVE.getCode());
                List<PurchaseOrderMaterial> materialList = materialService.list(Condition.getQueryWrapper(new PurchaseOrderMaterial()).lambda()
                        .eq(PurchaseOrderMaterial::getPurchaseOrderId, purchaseOrder.getId()));

                int count = (int) (materialList.stream().filter(material -> material.getTakeOverStatus().equals(PurchaseOrderMaterialStatusEnum.PARTIAL_RECEIPT)).count());
                int countComplete = (int) (materialList.stream().filter(material -> material.getTakeOverStatus().equals(PurchaseOrderMaterialStatusEnum.COMPLETE_RECEIVE_GOODS)).count());
                if (count > 0) {
                    purchaseOrder.setTakeOverStatus(PurchaseOrderMaterialStatusEnum.PARTIAL_RECEIPT.getCode());
                } else if (countComplete == materialList.size()) {
                    purchaseOrder.setTakeOverStatus(PurchaseOrderMaterialStatusEnum.COMPLETE_RECEIVE_GOODS.getCode());
                }
                this.updateById(purchaseOrder);


                //保存附件
                Optional.ofNullable(receivingVo.getAttachmentList()).ifPresent(fileInfos -> {
                    fileInfos.forEach(fileInfo -> {
                        AttachmentRel attachmentRelServiceOne = attachmentRelService.getOne(Wrappers.<AttachmentRel>lambdaQuery().eq(AttachmentRel::getAttachmentId, fileInfo.getId()));
                        //为空，证明没有保存过该附件
                        if (attachmentRelServiceOne == null) {
                            AttachmentRel attachmentRel = new AttachmentRel()
                                    .setBusinessFormId(receiving.getId())
                                    .setAttachmentId(fileInfo.getId())
                                    .setAttachmentName(fileInfo.getOriginalFilename())
                                    .setAttachmentUrl(fileInfo.getFilename())
                                    .setAttachmentSize(fileInfo.getSize());
                            attachmentRelService.save(attachmentRel);
                        }
                    });
                });

                //记录操作日志
                String finalOperationContent = operationContent;
                SecurityUtils.getCurrentUser().ifPresent(currentUser -> {
                    OperationLog operationLog = new OperationLog()
                            .setOperatorNo(currentUser.getId() + "")
                            .setOperatorName(currentUser.getName())
                            .setOperatorContent(finalOperationContent)
                            .setBillId(purchaseOrder.getId())
                            .setPurchaseOrderCode(purchaseOrder.getPurchaseOrderCode())
                            .setMaterialCode(orderMaterial.getMaterialCode())
                            .setMaterialName(orderMaterial.getMaterialName())
                            .setDeliveryNoteNo(receivingVo.getDeliveryNoteNo())
                            .setManufactureBatchNo(receivingVo.getManufactureBatchNo())
                            .setReceiveBatchNo(receivingVo.getReceiveBatchNo())
                            .setReceiveQuantity(receivingVo.getReceiveQuantity())
                            .setReceiveDate(receivingVo.getReceiveDate());
                    operationLogService.save(operationLog);

                });
            });
        }
    }


    /**
     * 权限按钮
     *
     * @param
     */
    public void addPermissionButton(PurchaseOrderReceivingVo receiveVo) {
        String status = receiveVo.getStatus();
        if (StrUtil.isBlank(status)) {
            return;
        }
        Boolean comparisonDisplay = false;//默认不显示备注
        if (StrUtil.isNotBlank(receiveVo.getOemSupplierRemark()) || StrUtil.isNotBlank(receiveVo.getBrandRemark())) {
            comparisonDisplay = true;
        }
        PurchaseOrderReceivingStatusEnum enumType = PurchaseOrderReceivingStatusEnum.getTypeByCode(receiveVo.getStatus());
        switch (enumType) {
            case TO_BE_CONFIRM:
                receiveVo.setPermissionButton(PurchaseOrderReceivingStatusOperateRel.operations(comparisonDisplay, PurchaseOrderReceivingStatusOperateRel.TO_BE_CONFIRM));
                break;
            case CONFIRM:
                receiveVo.setPermissionButton(PurchaseOrderReceivingStatusOperateRel.operations(comparisonDisplay, PurchaseOrderReceivingStatusOperateRel.CONFIRM));
                break;
            case RETURNED:
                receiveVo.setPermissionButton(PurchaseOrderReceivingStatusOperateRel.operations(comparisonDisplay, PurchaseOrderReceivingStatusOperateRel.RETURNED));
                break;
        }
    }

    /**
     * 构造第一层和第二层的按钮
     */
    public Map<String, Boolean> addPermissionButtonFirstAndSecondStage() {
        Map<String, Boolean> permissions = new HashMap<>();
        permissions.put(PurchaseOrderReceivingOperateType.REMARK.getCode(), false);
        return permissions;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void syncOemPurchaseOrder(List<OemPurchaseOrderVo> oemPurchaseOrderVoList) {

        // TODO 查询供应商信息 哪些供应商的数据呢？

        //查询设置供应商和采购商名字和地址编码
        nameForsupPurAndCodeForAddr(oemPurchaseOrderVoList);


        oemPurchaseOrderVoList.forEach(oemPurchaseOrderVo -> {

            PurchaseOrder purchaseOrder = this.getBySourceErpId(oemPurchaseOrderVo.getSourceErpId());
            boolean databaseExist = purchaseOrder != null;
            PurchaseOrder purchaseOrderRecord = this.buildPurchaseOrder(oemPurchaseOrderVo);
            if (databaseExist) {
                purchaseOrderRecord.setId(purchaseOrder.getId());
            }
            if (!this.saveOrUpdate(purchaseOrderRecord)) {
                log.error("[syncOemPurchaseOrder] 此数据同步失败，info:{} ", JSON.toJSONString(oemPurchaseOrderVo));
                return;
            }

            // 处理订单物料明细
            List<OemPurchaseOrderVo.PurchaseOrderMaterial> purchaseOrderMaterialList = oemPurchaseOrderVo.getPurchaseOrderMaterialList();


            purchaseOrderMaterialList.forEach(oemMaterialVo -> {
                PurchaseOrderMaterial material = BeanUtil.copy(oemMaterialVo, PurchaseOrderMaterial.class);
                PurchaseOrderMaterial materialForGet=this.getMaterialBySourceErpId(oemMaterialVo.getSourceErpId());
                boolean databaseExistMaterial = materialForGet != null;
                PurchaseOrderMaterial purchaseOrderMaterial = this.buildPurchaseOrderMaterial(oemMaterialVo,purchaseOrderRecord.getId());
                if (databaseExistMaterial) {
                    //更新

                    //设置id
                    purchaseOrderMaterial.setId(materialForGet.getId());

                    //同步剩余数量
                    purchaseOrderMaterial.setRemainingQuantity(materialForGet.getPurchaseQuantity());

                    //同步收货次数
                    purchaseOrderMaterial.setReceivingTimes(materialForGet.getReceivingTimes());

                    //同步已收货数量
                    purchaseOrderMaterial.setReceivedQuantity(materialForGet.getReceivedQuantity());

                    //同步物料收货状态
                    purchaseOrderMaterial.setTakeOverStatus(materialForGet.getTakeOverStatus());

                }
                if (!materialService.saveOrUpdate(purchaseOrderMaterial)) {
                    log.error("[syncOemPurchaseOrderMaterial] 此物料数据同步失败，info:{} ", JSON.toJSONString(oemMaterialVo));
                    return;
                }
            });


        });

    }

    /**
     * 设置供应商和采购商的名字 地址编码(注意去重)
     **/
    private void nameForsupPurAndCodeForAddr(List<OemPurchaseOrderVo> oemPurchaseOrderVoList) {

        //根据采购方和供应商编码去重
        List<OemPurchaseOrderVo> oemPurchaseOrderVoListForDistinct = oemPurchaseOrderVoList.stream().filter(CollectionUtil.distinctByKey(distinctByKeyFunctionForSyn())).collect(Collectors.toList());
        List<OrgIdQuery> orgIdQueryList = BeanUtil.copy(oemPurchaseOrderVoListForDistinct, OrgIdQuery.class);
        //根据采购方和供应商编码批量获取组织id
        log.info("采购方编码和供应编码的集合,{}",JSON.toJSONString(orgIdQueryList));
        R<List<OrgIdDTO>> infos = purchaserFeignClient.infos(orgIdQueryList);
        log.info("获取到的采购方和供应方的信息：{}",JSON.toJSONString(infos));

        if (infos.isSuccess()) {
            Map<String, String> purchaseMap = infos.getData().stream()
                    .filter(CollectionUtil.distinctByKey(OrgIdDTO::getPurchaseCode))
                    .collect(Collectors.toMap(OrgIdDTO::getPurchaseCode, OrgIdDTO::getPurchaseName));
            Map<String, String> supplierMap = infos.getData().stream()
                    .filter(CollectionUtil.distinctByKey(OrgIdDTO::getSupplierCode))
                    .collect(Collectors.toMap(OrgIdDTO::getSupplierCode, OrgIdDTO::getSupplierame));
            oemPurchaseOrderVoList.forEach(oemPurchaseOrderVo -> {
                oemPurchaseOrderVo.setPurchaseName(purchaseMap.get(oemPurchaseOrderVo.getPurchaseCode()));
                oemPurchaseOrderVo.setSupplierName(supplierMap.get(oemPurchaseOrderVo.getSupplierCode()));
            });

            //设置地址编码(注意去重)
            fixAddressCode(oemPurchaseOrderVoList);
        }
    }

    /**
     * 设置地址编码(注意去重)
     **/
    private void fixAddressCode(List<OemPurchaseOrderVo> oemPurchaseOrderVoList) {

        //获取地址,并且去重
        List<String> addressList = oemPurchaseOrderVoList.stream().map(vo -> {
            return vo.getOemSupplierAddress();
        }).distinct()
                .collect(Collectors.toList());

        //构建地址和地址编码的map
        /*Map<String, String> addrMap = addressList.stream().map(address -> {
            AddressCodeAndNameParam addressParam = new AddressCodeAndNameParam();
            addressParam.setAddress(address);
            //生成地址编码
            String addressCode = numberFactory.buildNumber(NumberType.oem_address_template);
            addressParam.setCode(addressCode);
            return addressParam;
        }).collect(Collectors.toMap(AddressCodeAndNameParam::getAddress, AddressCodeAndNameParam::getCode));
*/
        //同步地址  返回地址和地址code组成的map
        Map<String, ReceivingAddress> addrMap = receivingAddressService.syncOemAddress(addressList);

        //设置采购订单的地址编码字段
        for (OemPurchaseOrderVo oemPurchaseOrderVo : oemPurchaseOrderVoList) {
            ReceivingAddress address = addrMap.get(oemPurchaseOrderVo.getOemSupplierAddress());
            oemPurchaseOrderVo.setReceivingAddressCode(address.getCode());
            //地址的供应商如果不为空  维护同步的采购订单的oem供应商和编码
            if(StrUtil.isNotBlank(address.getSupplierCode())){
                oemPurchaseOrderVo.setOemSupplierCode(address.getSupplierCode());
                oemPurchaseOrderVo.setOemSupplierName(address.getSupplierName());
            }
        }
    }

    private Function<OemPurchaseOrderVo, String> distinctByKeyFunctionForSyn() {
        return (OemPurchaseOrderVo oemPurchaseOrderVo) -> oemPurchaseOrderVo.getPurchaseCode() + "-" + oemPurchaseOrderVo.getSupplierCode();
    }

    private PurchaseOrderMaterial buildPurchaseOrderMaterial(OemPurchaseOrderVo.PurchaseOrderMaterial oemMaterialVo,Long orderId) {
        PurchaseOrderMaterial purchaseOrderMaterial = new PurchaseOrderMaterial();
        BeanUtils.copyProperties(oemMaterialVo, purchaseOrderMaterial);

        //采购订单id
        purchaseOrderMaterial.setPurchaseOrderId(orderId);

        //初始化剩余数量
        purchaseOrderMaterial.setRemainingQuantity(purchaseOrderMaterial.getPurchaseQuantity());

        //初始化收货次数
        purchaseOrderMaterial.setReceivingTimes(0L);

        //初始化已收货数量
        purchaseOrderMaterial.setReceivedQuantity(0L);

        //初始化物料收货状态
        purchaseOrderMaterial.setTakeOverStatus(PurchaseOrderMaterialStatusEnum.GOODS_TO_BE_RECEIVE.getCode());

        //货主类型
        //purchaseOrderMaterial.setOwnerType(oemMaterialVo.getOwnerTypeid());

        //入库仓
        purchaseOrderMaterial.setWarehouse(oemMaterialVo.getWarehouse());

        //采购数量
        purchaseOrderMaterial.setPurchaseQuantity(Long.valueOf(oemMaterialVo.getPurchaseQuantity()));

        //条码
        purchaseOrderMaterial.setBarCode(oemMaterialVo.getBarCode());

        //含税单价
        purchaseOrderMaterial.setTaxPrice(oemMaterialVo.getTaxPrice());


        return purchaseOrderMaterial;
    }

    private PurchaseOrderMaterial getMaterialBySourceErpId(String sourceErpId) {
        return materialService.getOne(Wrappers.<PurchaseOrderMaterial>lambdaQuery().eq(PurchaseOrderMaterial::getSourceErpId, sourceErpId));
    }

    /**
     * 构建新增对象
     *
     * @param oemPurchaseOrderVo
     * @return
     */
    private PurchaseOrder buildPurchaseOrder(OemPurchaseOrderVo oemPurchaseOrderVo) {

        PurchaseOrder purchaseOrder = new PurchaseOrder();

        BeanUtils.copyProperties(oemPurchaseOrderVo, purchaseOrder);

        //srm生成订单号
        String orderNo = numberFactory.buildNumber(NumberType.oem_template);
        purchaseOrder.setOrderNo(orderNo);
        //订单初始化状态
        purchaseOrder.setTakeOverStatus(PurchaseOrderMaterialStatusEnum.GOODS_TO_BE_RECEIVE.getCode());

        //确认时间就是采购时间?
        purchaseOrder.setConfirmDate(purchaseOrder.getPurchaseDate());

        //单据类型
        purchaseOrder.setOrderType(oemPurchaseOrderVo.getOrderType());

        return purchaseOrder;

    }


    @Override
    public PurchaseOrder getBySourceErpId(String sourceErpId) {
        return this.getOne(Wrappers.<PurchaseOrder>lambdaQuery().eq(PurchaseOrder::getSourceErpId, sourceErpId));
    }

    /**
     * 下载附件模板
     *
     * @return
     */
    @Override
    public Map<String, Long> getReceivingAddressExcelAttachmentId() {
        Map<String, Long> result = new HashMap<String, Long>();
        result.put("id", downloadModuleConfig.getFileId());
        return result;
    }

    /**
     * 导入地址
     *
     * @param file
     */
    @Override
    public FileInfo importReceivingAddress(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), new ImportReceivingAddressListener(receivingAddressService, operationLogService))
                .head(AddressModule.class)
                .sheet()
                .doRead();

        return new FileInfo().setOriginalFilename(file.getOriginalFilename())
                .setSize(file.getSize())
                .setContentType(file.getContentType());
    }

    /**
     * 被退回
     *
     * @param page
     * @param queryParam
     * @return
     */
    @Override
    public PageImpl<ReturnedReceiptsVo> getReturnedReceiptsPage(IPage<ReturnedReceiptsVo> page, QueryParam<PurchaseOrderParam> queryParam) {
        List<ReturnedReceiptsVo> returnedReceiptsVos = purchaseOrderMapper.selectReturnedReceiptPage(page, queryParam);
        //设置附件信息
        Optional.ofNullable(returnedReceiptsVos).ifPresent(vos -> {
            vos.forEach(vo -> {
                ArrayList<FileInfo> fileInfos = Lists.newArrayList();
                List<AttachmentRel> attachmentRels = attachmentRelService.list(Wrappers.<AttachmentRel>lambdaQuery()
                        .eq(AttachmentRel::getBusinessFormId, vo.getReceivingId()));
                Optional.ofNullable(attachmentRels).ifPresent(attachments -> {
                    attachments.forEach(attachment -> {
                        FileInfo fileInfo = new FileInfo().setId(attachment.getAttachmentId())
                                .setFilename(attachment.getAttachmentName())
                                .setOriginalFilename(attachment.getAttachmentName())
                                .setUrl(attachment.getAttachmentUrl())
                                .setSize(attachment.getAttachmentSize());
                        fileInfos.add(fileInfo);
                    });

                });
                vo.setAttachmentList(fileInfos);
            });
        });
        return PageUtils.result(page.setRecords(returnedReceiptsVos));
    }

    /**
     * 订单收货
     *
     * @param id
     * @return
     */
    @Override
    public PurchaseOrderVo getOrderReceipt(Long id) {
        PurchaseOrder purchaseOrder = getById(id);
        AtomicReference<PurchaseOrderVo> orderVo = new AtomicReference<>();
        Optional.ofNullable(purchaseOrder).ifPresent(order -> {
            orderVo.set(BeanUtil.copy(order, PurchaseOrderVo.class));
            List<PurchaseOrderMaterial> materialList = materialService.list(Wrappers.<PurchaseOrderMaterial>lambdaQuery()
                    .eq(PurchaseOrderMaterial::getPurchaseOrderId, order.getId()));
            Optional.ofNullable(materialList).ifPresent(materials -> {
                List<PurchaseOrderMaterialVo> materialVos = BeanUtil.copy(materials, PurchaseOrderMaterialVo.class);
                orderVo.get().setChildren(materialVos);
            });
        });
        return orderVo.get();
    }

    /**
     * 批量编辑界面
     *
     * @param ids
     * @return
     */
    @Override
    public List<ReturnedReceiptsVo> batchEditPageList(List<Long> ids) {
        ArrayList<ReturnedReceiptsVo> returnedReceiptsVos = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(ids)) {
            ids.forEach(id -> {
                ReturnedReceiptsVo receiptsVo = purchaseOrderMapper.getReturnedReceiptById(id);
                //附件
                ArrayList<FileInfo> fileInfos = Lists.newArrayList();
                List<AttachmentRel> attachmentRels = attachmentRelService.list(Wrappers.<AttachmentRel>lambdaQuery().eq(AttachmentRel::getBusinessFormId, id));
                attachmentRels.forEach(attachmentRel -> {
                    FileInfo fileInfo = new FileInfo().setId(attachmentRel.getAttachmentId())
                            .setOriginalFilename(attachmentRel.getAttachmentName())
                            .setFilename(attachmentRel.getAttachmentName())
                            .setSize(attachmentRel.getAttachmentSize())
                            .setUrl(attachmentRel.getAttachmentUrl());
                    fileInfos.add(fileInfo);
                });
                receiptsVo.setAttachmentList(fileInfos);
                returnedReceiptsVos.add(receiptsVo);
            });
        }
        return returnedReceiptsVos;
    }

    /**
     * 采购列表页面附件回显构建方法
     */
    public void buildAttachment(PurchaseOrderReceivingVo purchaseOrderReceivingVo) {
        LambdaQueryWrapper<AttachmentRel> eq = Condition.getQueryWrapper(new AttachmentRel())
                .lambda()
                .eq(AttachmentRel::getBusinessFormId, purchaseOrderReceivingVo.getId());
        List<AttachmentRel> attList = attachmentRelService.list(eq);


        if (CollectionUtil.isNotEmpty(attList)) {
            List<FileInfo> attachmentList = new ArrayList<>();
            for (AttachmentRel attach : attList) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setId(attach.getAttachmentId());
                fileInfo.setUrl(attach.getAttachmentUrl());
                fileInfo.setOriginalFilename(attach.getAttachmentName());
                attachmentList.add(fileInfo);
            }
            purchaseOrderReceivingVo.setAttachmentList(attachmentList);
        }
    }
}
