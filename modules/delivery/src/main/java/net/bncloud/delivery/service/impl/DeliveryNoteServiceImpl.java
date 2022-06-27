package net.bncloud.delivery.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.api.feign.saas.sys.DictCodeDeliveryMethodEnum;
import net.bncloud.api.feign.saas.sys.DictCodeTransportMethodEnum;
import net.bncloud.api.feign.saas.sys.DictItemDTO;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.bis.service.api.dto.PurchaseReceiveBillCreateOrderDto;
import net.bncloud.bis.service.api.feign.PurchaseInStockOrderFeignClient;
import net.bncloud.bis.service.api.feign.PurchaseOrderFeignClient;
import net.bncloud.bis.service.api.feign.PurchaseReceiveBillOrderFeignClient;
import net.bncloud.bis.service.api.vo.Number;
import net.bncloud.bis.service.api.vo.PurchaseReceiveBillCallCreateVo;
import net.bncloud.bis.service.api.vo.PurchaseReceiveBillEntryCallCreateVo;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.helper.DevelopEnvHelper;
import net.bncloud.common.security.*;
import net.bncloud.common.service.base.domain.HandlerMsgParam;
import net.bncloud.common.util.*;
import net.bncloud.delivery.annotation.OperationLog;
import net.bncloud.delivery.entity.*;
import net.bncloud.delivery.enums.*;
import net.bncloud.delivery.event.*;
import net.bncloud.delivery.feign.InformationServiceFeignClient;
import net.bncloud.delivery.mapper.DeliveryNoteMapper;
import net.bncloud.delivery.mapper.DeliveryPlanMapper;
import net.bncloud.delivery.param.DeliveryAsPlanParam;
import net.bncloud.delivery.param.DeliveryNoteParam;
import net.bncloud.delivery.param.DeliveryNoteSaveParam;
import net.bncloud.delivery.service.*;
import net.bncloud.delivery.vo.DeliveryDetailVo;
import net.bncloud.delivery.vo.DeliveryNoteVo;
import net.bncloud.delivery.vo.DeliveryPlanDetailItemVo;
import net.bncloud.delivery.vo.DeliveryStatisticsVo;
import net.bncloud.delivery.vo.event.DeliveryNoteEvent;
import net.bncloud.enums.EventCode;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.serivce.api.order.feign.ZcOrderServiceFeignClient;
import net.bncloud.service.api.delivery.dto.DeliveryDetailDTO;
import net.bncloud.service.api.delivery.dto.DeliveryDetailUpdateDTO;
import net.bncloud.service.api.delivery.dto.DeliveryMaterialNoticeDTO;
import net.bncloud.service.api.delivery.dto.DeliveryNoteDTO;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.service.api.platform.supplier.dto.SupplierDTO;
import net.bncloud.service.api.platform.supplier.enums.SupplierRelevanceStatusEnum;
import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;
import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import net.bncloud.support.Condition;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 送货单信息表 服务实现类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class DeliveryNoteServiceImpl extends BaseServiceImpl<DeliveryNoteMapper, DeliveryNote> implements DeliveryNoteService {

    @Resource
    private CfgParamResourceFeignClient cfgParamResourceFeignClient;

    @Resource
    private DeliveryDetailService deliveryDetailService;

    @Resource
    private AttachmentRelService attachmentRelService;

    @Resource
    private DeliveryCustomsInformationService deliveryCustomsInformationService;


    @Resource
    private DefaultEventPublisher defaultEventPublisher;

    @Resource
    private InformationServiceFeignClient informationServiceFeignClient;

    @Resource
    private DeliveryPlanService deliveryPlanService;
    @Resource
    private DeliveryPlanDetailService deliveryPlanDetailService;
    @Autowired
    private DeliveryPlanDetailItemService deliveryPlanDetailItemService;

    @Resource
    private DeliveryNoteMapper deliveryNoteMapper;

    @Resource
    private DeliveryPlanMapper planMapper;

    @Autowired
    private DeliveryOperationLogService deliveryOperationLogService;

    @Resource
    private PurchaseReceiveBillOrderFeignClient purchaseReceiveBillOrderFeignClient;

    @Resource
    private SupplierFeignClient supplierFeignClient;

    @Resource
    private PurchaserFeignClient purchaserFeignClient;

    @Resource
    private PurchaseOrderFeignClient purchaseOrderFeignClient;
    @Resource
    private PurchaseInStockOrderFeignClient purchaseInStockOrderController;
    @Resource
    private ZcOrderServiceFeignClient zcOrderServiceFeignClient;

    @Resource
    private DevelopEnvHelper developEnvHelper;



    /**
     * 送货申请开启状态 false 关闭，true 开启
     */
    private static boolean DELIVERY_APPLICATION_ON_OFF;

    /**
     * 根据参数编码，获取参数值
     *
     * @param code 参数编码
     * @return 参数值
     */
    private String getParamValue(String code) {
        //fixme 去掉companyId
        R<CfgParamDTO> response = cfgParamResourceFeignClient.getParamByCode(code);
        log.info("根据参数编码，获取参数值,返回结果：{}", JSON.toJSONString(response));
        if (response.isSuccess()) {
            CfgParamDTO cfgParam = response.getData();
            return cfgParam.getValue();
        }
        return null;
    }

    @Override
    public boolean deleteDeliveryNote(Long id) {
        removeById(id);

        //删除附件
        AttachmentRel attachmentRel = AttachmentRel.builder().businessFormId(id).build();
        QueryWrapper<AttachmentRel> queryWrapper = Condition.getQueryWrapper(attachmentRel);
        List<AttachmentRel> attachmentRelList = attachmentRelService.list(queryWrapper);
        List<Long> attachmentIds = attachmentRelList.stream().map(AttachmentRel::getId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(attachmentIds)) {
            attachmentRelService.removeByIds(attachmentIds);
        }

        //删除送货单明细
        DeliveryDetail deliveryDetail = DeliveryDetail.builder().deliveryId(id).build();
        QueryWrapper<DeliveryDetail> detailQueryWrapper = Condition.getQueryWrapper(deliveryDetail);

        //删除送货计划关联
        List<DeliveryDetail> deliveryDetailList = deliveryDetailService.list(detailQueryWrapper);
        for (DeliveryDetail deliveryDetail1 : deliveryDetailList) {
            DeliveryPlanDetailItem deliveryPlanDetailItem = new DeliveryPlanDetailItem();

//            deliveryPlanDetailItem.setDeliveryNoteId(null);
//            deliveryPlanDetailItem.setDeliveryNoteNo(null);
//            deliveryPlanDetailItem.setDeliveryStatus(null);

            deliveryPlanDetailItem.setId(deliveryDetail1.getDeliveryPlanDetailItemId());
            LambdaUpdateWrapper<DeliveryPlanDetailItem> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(DeliveryPlanDetailItem::getId, deliveryPlanDetailItem.getId());
            updateWrapper.set(DeliveryPlanDetailItem::getDeliveryNoteId, null);
            updateWrapper.set(DeliveryPlanDetailItem::getDeliveryNoteNo, null);
            updateWrapper.set(DeliveryPlanDetailItem::getDeliveryStatus, null);

            deliveryPlanDetailItemService.update(deliveryPlanDetailItem, updateWrapper);
        }
        deliveryDetailService.remove(detailQueryWrapper);

        //删除报关资料
        DeliveryCustomsInformation deliveryCustomsInformation = DeliveryCustomsInformation.builder().deliveryId(id).build();
        QueryWrapper<DeliveryCustomsInformation> deliveryCustomsInformationQueryWrapper = Condition.getQueryWrapper(deliveryCustomsInformation);
        List<DeliveryCustomsInformation> deliveryCustomsInformationList = deliveryCustomsInformationService.list(deliveryCustomsInformationQueryWrapper);
        List<Long> deliveryCustomsInformationIds = deliveryCustomsInformationList.stream().map(DeliveryCustomsInformation::getId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(deliveryCustomsInformationIds)) {
            deliveryCustomsInformationService.removeByIds(deliveryCustomsInformationIds);
        }
        return true;
    }

    /**
     * 销售工作台送货单自定义分页
     *
     * @param page
     * @param queryParam
     * @return
     */
    @Override
    public IPage<DeliveryNote> selectPage(IPage<DeliveryNote> page, QueryParam<DeliveryNoteParam> queryParam) {
        Optional<LoginInfo> loginInfo1 = SecurityUtils.getLoginInfo();
        LoginInfo loginInfo2 = loginInfo1.get();
        Supplier currentSupplier = loginInfo2.getCurrentSupplier();
        if (currentSupplier == null || currentSupplier.getSupplierCode() == null) {
            throw new RuntimeException("供应商编码有问题");
        }
        DeliveryNoteParam param = queryParam.getParam();
        if (param != null) {
            queryParam.getParam().setSupplierCode(currentSupplier.getSupplierCode());
        } else {
            DeliveryNoteParam deliveryNoteParam = new DeliveryNoteParam();
            deliveryNoteParam.setSupplierCode(currentSupplier.getSupplierCode());
            queryParam.setParam(deliveryNoteParam);
        }
        List<DeliveryNote> deliveryNotes = baseMapper.selectListPage(page, queryParam);
       /* for (DeliveryNote deliveryNote : deliveryNotes) {
            DeliveryDetail deliveryDetail = DeliveryDetail.builder().deliveryId(deliveryNote.getId()).build();
            QueryWrapper<DeliveryDetail> detailQueryWrapper = Condition.getQueryWrapper(deliveryDetail);
            List<DeliveryDetail> deliveryDetailList = deliveryDetailService.list(detailQueryWrapper);
        }
        */
        for (DeliveryNote dn : deliveryNotes) {
            if (StringUtil.equals(dn.getTransportMethod(), DictCodeTransportMethodEnum.CAR.getCode())) {
                dn.setTransportMethod(DictCodeTransportMethodEnum.CAR.getMsg());
            } else if (StringUtil.equals(dn.getTransportMethod(), DictCodeTransportMethodEnum.RAILWAY.getCode())) {
                dn.setTransportMethod(DictCodeTransportMethodEnum.RAILWAY.getMsg());
            } else if (StringUtil.equals(dn.getTransportMethod(), DictCodeTransportMethodEnum.PLANE.getCode())) {
                dn.setTransportMethod(DictCodeTransportMethodEnum.PLANE.getMsg());
            } else if (StringUtil.equals(dn.getTransportMethod(), DictCodeTransportMethodEnum.SHIP.getCode())) {
                dn.setTransportMethod(DictCodeTransportMethodEnum.SHIP.getMsg());
            }

            if (StringUtil.equals(dn.getDeliveryMethod(), DictCodeDeliveryMethodEnum.SUPPLIER_DELIVERY.getCode())) {
                dn.setDeliveryMethod(DictCodeDeliveryMethodEnum.SUPPLIER_DELIVERY.getMsg());
            } else if (StringUtil.equals(dn.getDeliveryMethod(), DictCodeDeliveryMethodEnum.EXPRESS.getCode())) {
                dn.setDeliveryMethod(DictCodeDeliveryMethodEnum.EXPRESS.getMsg());
            } else if (StringUtil.equals(dn.getDeliveryMethod(), DictCodeDeliveryMethodEnum.PROVIDED_BY_PURCHASER.getCode())) {
                dn.setDeliveryMethod(DictCodeDeliveryMethodEnum.PROVIDED_BY_PURCHASER.getMsg());
            }

            //erp签收状态字典返回
            //返回签收和未签收
            if (StringUtil.equals(dn.getErpSigningStatus(), ErpSigningStatusEnum.NOT_SIGNED.getCode())) {
                dn.setErpSigningStatus(ErpSigningStatusEnum.NOT_SIGNED.getName());
            } else if (StringUtil.equals(dn.getErpSigningStatus(), ErpSigningStatusEnum.SIGNED.getCode())) {
                dn.setErpSigningStatus(ErpSigningStatusEnum.SIGNED.getName());
            }
        }

        return page.setRecords(deliveryNotes);
        /*SecurityUtils.getLoginInfo().ifPresent(loginInfo -> {
            Supplier currentSupplier = loginInfo.getCurrentSupplier();
            if(currentSupplier != null){
                deliveryNoteParam.setSupplierCode(currentSupplier.getSupplierCode());
            }
        });*/
    }

    /**
     * 采购工作台送货单自定义分页
     *
     * @param page
     * @param queryParam
     * @return
     */
    @Override
    public IPage<DeliveryNote> selectPageByOrgId(IPage<DeliveryNote> page, QueryParam<DeliveryNoteParam> queryParam) {
        Optional<LoginInfo> loginInfo1 = SecurityUtils.getLoginInfo();
        LoginInfo loginInfo2 = loginInfo1.get();
        Org currentOrg = loginInfo2.getCurrentOrg();
        if (currentOrg == null || currentOrg.getId() == null) {
            throw new RuntimeException("采购商编码有问题");
        }

        DeliveryNoteParam param = queryParam.getParam();
        if (param != null) {
            queryParam.getParam().setOrgId(currentOrg.getId());
            if (queryParam.getParam().getDeliveryStatusCode() == null || queryParam.getParam().getDeliveryStatusCode().equals("")) {
//                queryParam.getParam().setDeliveryStatusCode("0");
                queryParam.getParam().setOrgTrue("true");
            } else if (queryParam.getParam().getDeliveryStatusCode() != null && queryParam.getParam().getDeliveryStatusCode().equals("1")) {
//                queryParam.getParam().setDeliveryStatusCode("100");
                queryParam.getParam().setOrgTrue("true");
            }


        } else {
            DeliveryNoteParam deliveryNoteParam = new DeliveryNoteParam();

            deliveryNoteParam.setOrgId(currentOrg.getId());
            queryParam.setParam(deliveryNoteParam);
            if (queryParam.getParam().getDeliveryStatusCode() == null || queryParam.getParam().getDeliveryStatusCode().equals("")) {
                queryParam.getParam().setOrgTrue("true");
//                queryParam.getParam().setDeliveryStatusCode("0");
            } else if (queryParam.getParam().getDeliveryStatusCode() != null && queryParam.getParam().getDeliveryStatusCode().equals("1")) {

//                queryParam.getParam().setDeliveryStatusCode("100");
                queryParam.getParam().setOrgTrue("true");
            }

        }

        List<DeliveryNote> deliveryNotes = baseMapper.selectListPage(page, queryParam);
//        List<DeliveryNote> collect = deliveryNotes.stream().
//                filter(d -> d.getDeliveryStatusCode().equals("100")).collect(Collectors.toList());
        for (DeliveryNote dn : deliveryNotes) {
            if (StringUtil.equals(dn.getTransportMethod(), DictCodeTransportMethodEnum.CAR.getCode())) {
                dn.setTransportMethod(DictCodeTransportMethodEnum.CAR.getMsg());
            } else if (StringUtil.equals(dn.getTransportMethod(), DictCodeTransportMethodEnum.RAILWAY.getCode())) {
                dn.setTransportMethod(DictCodeTransportMethodEnum.RAILWAY.getMsg());
            } else if (StringUtil.equals(dn.getTransportMethod(), DictCodeTransportMethodEnum.PLANE.getCode())) {
                dn.setTransportMethod(DictCodeTransportMethodEnum.PLANE.getMsg());
            } else if (StringUtil.equals(dn.getTransportMethod(), DictCodeTransportMethodEnum.SHIP.getCode())) {
                dn.setTransportMethod(DictCodeTransportMethodEnum.SHIP.getMsg());
            }

            if (StringUtil.equals(dn.getDeliveryMethod(), DictCodeDeliveryMethodEnum.SUPPLIER_DELIVERY.getCode())) {
                dn.setDeliveryMethod(DictCodeDeliveryMethodEnum.SUPPLIER_DELIVERY.getMsg());
            } else if (StringUtil.equals(dn.getDeliveryMethod(), DictCodeDeliveryMethodEnum.EXPRESS.getCode())) {
                dn.setDeliveryMethod(DictCodeDeliveryMethodEnum.EXPRESS.getMsg());
            } else if (StringUtil.equals(dn.getDeliveryMethod(), DictCodeDeliveryMethodEnum.PROVIDED_BY_PURCHASER.getCode())) {
                dn.setDeliveryMethod(DictCodeDeliveryMethodEnum.PROVIDED_BY_PURCHASER.getMsg());
            }

            //erp签收状态字典返回
            //返回签收和未签收
            if (StringUtil.equals(dn.getErpSigningStatus(), ErpSigningStatusEnum.NOT_SIGNED.getCode())) {
                dn.setErpSigningStatus(ErpSigningStatusEnum.NOT_SIGNED.getName());
            } else if (StringUtil.equals(dn.getErpSigningStatus(), ErpSigningStatusEnum.SIGNED.getCode())) {
                dn.setErpSigningStatus(ErpSigningStatusEnum.SIGNED.getName());
            }
        }

        return page.setRecords(deliveryNotes);
    }

    /**
     * 保存送货单信息
     *
     * @param deliveryNote 保存送货单请求参数
     * @return 送货单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public DeliveryNoteVo saveDeliveryNote(DeliveryNoteSaveParam deliveryNote) {

        int c;
        List<DeliveryDetailVo> deliveryDetailList1 = deliveryNote.getDeliveryDetailList();
        for (int i = 0; i < deliveryDetailList1.size(); i++) {
            c = i;
            for (int j = 0; j < deliveryDetailList1.size(); j++) {
                if (c != j) {
                    DeliveryDetail deliveryDetail = deliveryDetailList1.get(c);
                    DeliveryDetail deliveryDetail1 = deliveryDetailList1.get(j);
                    if (deliveryDetail.getDeliveryPlanDetailItemId().equals(deliveryDetail1.getDeliveryPlanDetailItemId())) {
                        throw new RuntimeException("不可以同时选同一条送货计划记录保存");
                    }
                }
            }
        }


        //判断供应商合作状态，冻结和停止合作不能新增
        String supplierCode = SecurityUtils.getLoginInfo().get().getCurrentSupplier().getSupplierCode();
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCode(supplierCode);
        R<SupplierDTO> oneSupplierByCode = supplierFeignClient.findOneSupplierByCode(supplierDTO);
        String relevanceStatus = oneSupplierByCode.getData().getRelevanceStatus();
        try {
            if (!SupplierRelevanceStatusEnum.RELEVANCE.getCode().equals(relevanceStatus)) {
                throw new Exception("供应商合作状态异常");
            }
        } catch (Exception e) {
            return null;
        }

//        deliveryNote.setId(null);
        deliveryNote.setDeliveryNo(generateDeliveryNo());
        DELIVERY_APPLICATION_ON_OFF = Boolean.parseBoolean(getParamValue("delivery:delivery_application_on_off"));
        deliveryNote.setDeliveryApplication(DELIVERY_APPLICATION_ON_OFF);
        Optional<LoginInfo> loginInfo = SecurityUtils.getLoginInfo();
        loginInfo.ifPresent(info -> deliveryNote.setCreatedByName(info.getName()));
        if (DELIVERY_APPLICATION_ON_OFF) {
            deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.DRAFT.getCode());
            deliveryNote.setLogisticsStatus(LogisticsStatusEnum.NOT_SHIPPED.getCode());
            deliveryNote.setErpSigningStatus(ErpSigningStatusEnum.NOT_SIGNED.getCode());
        } else {
            deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.DRAFT.getCode());
            deliveryNote.setLogisticsStatus(LogisticsStatusEnum.NOT_SHIPPED.getCode());
            deliveryNote.setErpSigningStatus(ErpSigningStatusEnum.NOT_SIGNED.getCode());
        }

        //获取orgId
        OrgIdQuery orgIdQuery = new OrgIdQuery();
        orgIdQuery.setSupplierCode(deliveryNote.getSupplierCode());
        orgIdQuery.setPurchaseCode(deliveryNote.getCustomerCode());
        R<OrgIdDTO> orgIdDTO = purchaserFeignClient.info(orgIdQuery);

        //如果没有建立关系的供应商和采购商不能建立送货单
        if (orgIdDTO == null || orgIdDTO.getData() == null || orgIdDTO.getData().getPurchaseName() == null) {
            throw new RuntimeException("此采购方没有和供应商建立关系");
        }


        if (orgIdDTO != null && orgIdDTO.getData() != null && orgIdDTO.getData().getOrgId() != null) {
            //设置orgId
            deliveryNote.setOrgId(orgIdDTO.getData().getOrgId());
            //设置客户名字customName
            deliveryNote.setCustomerName(orgIdDTO.getData().getPurchaseName());
        }
        if (orgIdDTO.getData() == null) {
            log.info("此供应商编码和采购编码没有对应的orgId");
        }

        save(deliveryNote);
        Long deliveryNoteId = deliveryNote.getId();
        String deliveryNo = deliveryNote.getDeliveryNo();
        String deliveryStatusCode = deliveryNote.getDeliveryStatusCode();
        List<DeliveryDetailVo> deliveryDetailList = deliveryNote.getDeliveryDetailList();

        //在送货单保存之前先校验所选择的送货计划的字段
        for (int i = 0; i < deliveryDetailList.size(); i++) {
            for (int j = 0; j < deliveryDetailList.size(); j++) {
                if (!(deliveryDetailList.get(i).getOrderType().equals(deliveryDetailList.get(j).getOrderType()))) {
                    throw new RuntimeException("送货计划订单类型不一致");
                }
                if (!(deliveryDetailList.get(i).getDeliveryUnitName().equals(deliveryDetailList.get(j).getDeliveryUnitName()))) {
                    throw new RuntimeException("送货计划计划单位不一致");
                }
                if (!(deliveryDetailList.get(i).getCurrency().equals(deliveryDetailList.get(j).getCurrency()))) {
                    throw new RuntimeException("送货计划结算币别不一致");
                }
                if (!(deliveryDetailList.get(i).getMaterialClassification().equals(deliveryDetailList.get(j).getMaterialClassification()))) {
                    throw new RuntimeException("送货计划物料分类不一致");
                }
            }

        }


        //保存和关联明细之前先判断配置开关
        //计划送货数量和实际送货数量
        //todo****************

        //更新之前先判断计划数量和实际数量
        Boolean quantityflag = false;

        Boolean normalFlag = false;//正常更新但是数量小于计划送货，调用收料通知单同步开关

        Boolean remainingflag = false;//正常更新但是数量小于计划送货，调用收料通知单同步开关

        //获取detaillist
//        List<DeliveryDetail> deliveryDetailList = deliveryNote.getDeliveryDetailList();
        for (DeliveryDetail deliveryDetail : deliveryDetailList) {
            if (deliveryDetail.getRealDeliveryQuantity().compareTo(deliveryDetail.getPlanQuantity()) == 1) {
                //实际收获数量比计划收获数量多
                quantityflag = true;
            }
            if (deliveryDetail.getRealDeliveryQuantity().compareTo(deliveryDetail.getPlanQuantity()) == -1) {
                //实际收获数量比计划收获数量少
                normalFlag = true;
            }
            if (deliveryDetail.getRealDeliveryQuantity().compareTo(deliveryDetail.getPlanQuantity()) == -1) {

            }
        }

        //获取同步配置
        //获取ordId
//        OrgIdQuery orgIdQuery = new OrgIdQuery();
//        orgIdQuery.setSupplierCode(deliveryNote.getSupplierCode());
//        orgIdQuery.setPurchaseCode(deliveryNote.getCustomerCode());
//        R<OrgIdDTO> orgIdDTO = purchaserFeignClient.info(orgIdQuery);
        R<List<CfgParamDTO>> listByCode = cfgParamResourceFeignClient.findListByCode(DeliveryCfgParam.DELIVERY_EXCESS_QUANTITY.getCode());
        if (!(listByCode.isSuccess())) {
            throw new RuntimeException("获取同步配置出现异常");
        }

        //查询出erpId
//        DeliveryNote deliveryNote1 = baseMapper.selectById((Serializable)(deliveryNote.getId()));
        //获取data并且获取CfgParamEntity
        Boolean flag = false;
        BigDecimal value = null;
        List<CfgParamDTO> cfgParamDTOList = listByCode.getData();
        CfgParamDTO paramEntity = cfgParamDTOList.stream().filter(cfgParamDTO -> cfgParamDTO.getOrgId().equals(orgIdDTO.getData().getOrgId())).findFirst().orElse(null);
        if (paramEntity != null) {
//            if(true){
//                String result = "{\"data\":[{\"type\":\"BOOL\",\"value\":false},{\"type\":\"INPUT\",\"value\":\"12\"}]}";
//            JSONObject jsonObject = JSON.parseObject(result);  // result数据源：JSON格式字符串
            JSONObject jsonObject = JSON.parseObject(paramEntity.getValue());  // result数据源：JSON格式字符串
            // 获取值
            JSONArray data = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = JSON.parseObject(data.getString(0));
            flag = jsonObject1.getBoolean("value");
            if (flag == null) {
                throw new RuntimeException("获取同步配置开关出现异常");
            }
            if (flag) {//按钮开启才获取
                JSONObject jsonObject2 = JSON.parseObject(data.getString(1));
                value = jsonObject2.getBigDecimal("value");
            }
        }

        if (!quantityflag) {//数量和计划数量相等直接保存

            //正常执行保存操作

            //调用送货计划服务把送货单送货状态（DeliveryStatusCode）存到送货计划（项次表中）
            //同时方便前端过滤，选中的送货计划不会再次在按计划送货的按钮中显示出来
            //保存成功之后把明细的id保存到计划表中
            for (DeliveryDetail deliveryDetail : deliveryDetailList) {
                DeliveryPlanDetailItem deliveryPlanDetailItem = new DeliveryPlanDetailItem();
                deliveryPlanDetailItem.setDeliveryNoteId(deliveryNoteId);

                //这两个字段先不要，达华说交给送货计划模块处理
//                deliveryPlanDetailItem.setDeliveryNoteNo(deliveryNo);
//                deliveryPlanDetailItem.setDeliveryStatus(deliveryStatusCode);

                deliveryPlanDetailItem.setId(deliveryDetail.getDeliveryPlanDetailItemId());
                deliveryPlanDetailItemService.updateById(deliveryPlanDetailItem);
            }

            //明细保存planunit
            for (DeliveryDetail deliveryDetail : deliveryDetailList) {
                deliveryDetail.setPlanUnit(deliveryNote.getPlanUnit());
            }


            //保存报关资料数据
            deliveryCustomsInformationService.save(DeliveryCustomsInformation.builder()
                    .deliveryId(deliveryNote.getId())
                    .deliveryNo(deliveryNote.getDeliveryNo())
                    .innerOrderNo(deliveryNote.getInnerOrderNo())
                    .build());


            //保存明细信息
            deliveryDetailService.saveDetails(deliveryNote.getId(), deliveryDetailList);

            //保存明细中的附件信息
            deliveryDetailList.forEach(detail->{
                Optional.ofNullable(detail.getAttachmentList()).ifPresent(attachmentList->{
                    attachmentList.forEach(attachment->{
                        AttachmentRel attachmentRel = new AttachmentRel()
                                .setAttachmentId(attachment.getId())
                                .setAttachmentName(attachment.getOriginalFilename())
                                .setAttachmentSize(attachment.getSize())
                                .setAttachmentUrl(attachment.getUrl())
                                .setBusinessFormId(detail.getId());
                        attachmentRelService.save(attachmentRel);
                    });
                });
            });

            /*if(normalFlag){
                //正常更新的话如果明细的实际收获和计划收货数量不对，这样也要去更新收料通知单的
                if(deliveryNote1.getErpId()!=null){
                    R<PurchaseReceiveBillCallCreateVo> deliveryOfCargoFromStorage = createDeliveryOfCargoFromStorage(deliveryNote, deliveryNote1.getErpId());
                }else{
                    R<PurchaseReceiveBillCallCreateVo> deliveryOfCargoFromStorage = createDeliveryOfCargoFromStorage(deliveryNote, null);
                }
            }*/

        } else {//不相等，检查开关是否打开
            if (!flag) {//开关：关闭
                log.error("1/实际送货数量和收货数量不一致，需要修改实际送货数量，请在采购配置中允许实际送货数量和收货数量不一致,{}", JSON.toJSONString(flag));
                throw new RuntimeException("1/实际送货数量和收货数量不一致，需要修改实际送货数量，请在采购配置中允许实际送货数量和收货数量不一致");
            } else {
                if (value == null) {
                    //开关打开但是数量没有填，当做收货数量和实际数量一致的情况
                    throw new RuntimeException("2/请在在采购配置中的配置数量值，请重新配置");
                } else {
                    //开关关闭但是数量填上了，需要判断
                    for (DeliveryDetail deliveryDetail : deliveryDetailList) {
                        if (deliveryDetail.getPlanQuantity().compareTo(deliveryDetail.getRealDeliveryQuantity()) != 0) {
                            BigDecimal onehundred = new BigDecimal("100");
                            BigDecimal present = value.divide(onehundred, 20, BigDecimal.ROUND_HALF_UP);

                            BigDecimal planQuantity = deliveryDetail.getPlanQuantity();
                            BigDecimal realDeliveryQuantity = deliveryDetail.getRealDeliveryQuantity();

                            BigDecimal multiplyPresent = planQuantity.multiply(present);//乘以百分比之后的数量
                            BigDecimal configQuantity = planQuantity.add(multiplyPresent);//开关控制的数量
                            //去掉小数位
                            int i = configQuantity.intValue();
                            BigDecimal configQuantityForNOSmallNum = new BigDecimal(i);


                            if (realDeliveryQuantity.compareTo(configQuantityForNOSmallNum) == 1) {
                                throw new RuntimeException("3/实际送货数量超过计划送货数量在采购配置中的配置值，请重新配置");
                            }
                        }
                    }
                    //判断通过，就可以保存
                    //调用送货计划服务把送货单送货状态（DeliveryStatusCode）存到送货计划（项次表中）
                    //同时方便前端过滤，选中的送货计划不会再次在按计划送货的按钮中显示出来
                    //保存成功之后把明细的id保存到计划表中
                    for (DeliveryDetail deliveryDetail : deliveryDetailList) {
                        DeliveryPlanDetailItem deliveryPlanDetailItem = new DeliveryPlanDetailItem();
                        deliveryPlanDetailItem.setDeliveryNoteId(deliveryNoteId);

                        //本来传的，达华说现在由送货计划进行关联
                        /*deliveryPlanDetailItem.setDeliveryNoteNo(deliveryNo);
                        deliveryPlanDetailItem.setDeliveryStatus(deliveryStatusCode);*/

                        deliveryPlanDetailItem.setId(deliveryDetail.getDeliveryPlanDetailItemId());
                        deliveryPlanDetailItemService.updateById(deliveryPlanDetailItem);
                    }

                    //明细保存planunit
                    for (DeliveryDetail deliveryDetail : deliveryDetailList) {
                        deliveryDetail.setPlanUnit(deliveryNote.getPlanUnit());
                    }


                    //保存报关资料数据
                    deliveryCustomsInformationService.save(DeliveryCustomsInformation.builder()
                            .deliveryId(deliveryNote.getId())
                            .deliveryNo(deliveryNote.getDeliveryNo())
                            .innerOrderNo(deliveryNote.getInnerOrderNo())
                            .build());


                    //保存明细信息
                    deliveryDetailService.saveDetails(deliveryNote.getId(), deliveryDetailList);
                    //保存明细中的附件信息
                    deliveryDetailList.forEach(detail->{
                        Optional.ofNullable(detail.getAttachmentList()).ifPresent(attachmentList->{
                            attachmentList.forEach(attachment->{
                                AttachmentRel attachmentRel = new AttachmentRel()
                                        .setAttachmentId(attachment.getId())
                                        .setAttachmentName(attachment.getOriginalFilename())
                                        .setAttachmentSize(attachment.getSize())
                                        .setAttachmentUrl(attachment.getUrl())
                                        .setBusinessFormId(detail.getId());
                                attachmentRelService.save(attachmentRel);
                            });
                        });
                    });

                }
            }
        }
        //保存明细完成
        //todo****************


        //调用送货计划服务把送货单送货状态（DeliveryStatusCode）存到送货计划（项次表中）
        //同时方便前端过滤，选中的送货计划不会再次在按计划送货的按钮中显示出来
        //保存成功之后把明细的id保存到计划表中
        /*for (DeliveryDetail deliveryDetail : deliveryDetailList) {
            DeliveryPlanDetailItem deliveryPlanDetailItem = new DeliveryPlanDetailItem();
            deliveryPlanDetailItem.setDeliveryNoteId(deliveryNoteId);

            deliveryPlanDetailItem.setDeliveryNoteNo(deliveryNo);
            deliveryPlanDetailItem.setDeliveryStatus(deliveryStatusCode);

            deliveryPlanDetailItem.setId(deliveryDetail.getDeliveryPlanDetailItemId());
            deliveryPlanDetailItemService.updateById(deliveryPlanDetailItem);
        }*/

        //明细保存planunit
        /*for (DeliveryDetail deliveryDetail : deliveryDetailList) {
            deliveryDetail.setPlanUnit(deliveryNote.getPlanUnit());
        }*/


        //保存报关资料数据
        /*deliveryCustomsInformationService.save(DeliveryCustomsInformation.builder()
                .deliveryId(deliveryNote.getId())
                .deliveryNo(deliveryNote.getDeliveryNo())
                .innerOrderNo(deliveryNote.getInnerOrderNo())
                .build());*/


        //保存明细信息
//        deliveryDetailService.saveDetails(deliveryNote.getId(), deliveryDetailList);


        //保存送货单的同时保存附件
        if (deliveryNote.getAttachmentList() != null) {
            List<FileInfo> attachmentList = deliveryNote.getAttachmentList();
            attachmentRelService.clearAndSave(deliveryNoteId, attachmentList);
        }


        //记录保存操作记录
        DeliveryOperationLog deliveryOperationLog = new DeliveryOperationLog();
        deliveryOperationLog.setBillId(deliveryNote.getId());
        deliveryOperationLog.setOperatorNo(deliveryNote.getDeliveryNo());
        deliveryOperationLog.setOperatorName(deliveryNote.getCreatedByName());
        deliveryOperationLog.setOperatorContent("创建送货单");
        deliveryOperationLogService.save(deliveryOperationLog);

        DeliveryNoteVo deliveryNoteVo = BeanUtil.copy(deliveryNote, DeliveryNoteVo.class);
        deliveryNoteVo.setDeliveryDetailList(deliveryDetailList);
        return deliveryNoteVo;
    }

    /**
     * 包装关联关系信息
     *
     * @param fDetailEntity
     * @param detail
     */
    private void wrapperDetailEntryLink(PurchaseReceiveBillCreateOrderDto.FDetailEntity fDetailEntity, DeliveryDetail detail) {

        // 查询送货计划的明细项次信息
        DeliveryPlanDetailItem deliveryPlanDetailItem = deliveryPlanDetailItemService.getById(detail.getDeliveryPlanDetailItemId());
        DeliveryPlanDetail deliveryPlanDetail = deliveryPlanDetailService.getById(deliveryPlanDetailItem.getDeliveryPlanDetailId());
        DeliveryPlan deliveryPlan = deliveryPlanService.getById(deliveryPlanDetail.getDeliveryPlanId());

        // rao -- 添加关联关系信息
        PurchaseReceiveBillCreateOrderDto.FDetailEntityLink fDetailEntityLink = new PurchaseReceiveBillCreateOrderDto.FDetailEntityLink();
        // 设置源 采购订单FID
        fDetailEntityLink.setFDetailEntity_Link_FSBillId(Long.parseLong(deliveryPlan.getSourceId()));
        // 设置源 采购订单的明细的ID
        fDetailEntityLink.setFDetailEntity_Link_FSId(Long.parseLong(deliveryPlanDetail.getSourceId()));
        // 设置源 采购订单的明细的 交货量
        String planQuantity = deliveryPlanDetail.getPlanQuantity();
        fDetailEntityLink.setFDetailEntity_Link_FBaseUnitQtyOld(planQuantity);
        // 设置 此次交货量
        fDetailEntityLink.setFDetailEntity_Link_FBaseUnitQty(fDetailEntity.getFActReceiveQty());

        // 设置 采购单号
        fDetailEntity.setFSrcBillNo(deliveryPlan.getBillNo());

        fDetailEntity.setFDetailEntityLinkList(Collections.singletonList(fDetailEntityLink));
    }

    /**
     * 生成送货单号
     *
     * @return
     */
    private String generateDeliveryNo() {
        String format = DateUtil.format(new Date(), DateUtil.PATTERN_DATETIME_NUM);
        String random = NumberUtil.getRandom(4);
        return "S" + format + random;
    }

    /**
     * 更新收料通知单回传的erpId
     *
     * @param
     * @return
     */
    @Override
    public void updateErpId(Long id, Long erpId, String fNumber, String code) {
        deliveryNoteMapper.updateErpId(id, erpId, fNumber, code);
    }

    /**
     * 更新送货单信息
     *
     * @param deliveryNote
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeliveryNoteVo updateDeliveryNote(DeliveryNoteSaveParam deliveryNote) {
        log.info("========================================================");
        log.info("修改开始，数据为：{}", JSON.toJSONString(deliveryNote));
        log.info("========================================================");


        //判断供应商合作状态，冻结和停止合作不能新增
        String supplierCode = SecurityUtils.getLoginInfo().get().getCurrentSupplier().getSupplierCode();
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCode(supplierCode);
        R<SupplierDTO> oneSupplierByCode = supplierFeignClient.findOneSupplierByCode(supplierDTO);
        String relevanceStatus = oneSupplierByCode.getData().getRelevanceStatus();
        System.out.println(relevanceStatus);
        try {
            if (!(SupplierRelevanceStatusEnum.RELEVANCE.getCode().equals(relevanceStatus) || SupplierRelevanceStatusEnum.SUSPEND_COOPERATION.getCode().equals(relevanceStatus))) {
                throw new Exception("供应商合作状态异常");
            }
        } catch (Exception e) {
            return null;
        }
        Long id = deliveryNote.getId();
        if (id == null) {
            throw new BizException(DeliveryResultCode.PARAM_ERROR);
        }

        //更新之前先判断计划数量和实际数量
        Boolean quantityflag = false;

        Boolean normalFlag = false;//正常更新但是数量小于计划送货，调用收料通知单同步开关

        //获取detaillist
        // List<DeliveryDetail> deliveryDetailList = deliveryNote.getDeliveryDetailList();
        @Valid @NotEmpty(message = "请添加送货明细") List<DeliveryDetailVo> deliveryDetailList = deliveryNote.getDeliveryDetailList();
        for (DeliveryDetail deliveryDetail : deliveryDetailList) {
            if (deliveryDetail.getRealDeliveryQuantity().compareTo(deliveryDetail.getPlanQuantity()) > 0) {
                //实际收获数量比计划收获数量多
                quantityflag = true;
            }
            if (deliveryDetail.getRealDeliveryQuantity().compareTo(deliveryDetail.getPlanQuantity()) != 0) {
                //实际收获数量比计划收获数量少了
                normalFlag = true;
            }
        }

        //获取同步配置
        //获取ordId
        OrgIdQuery orgIdQuery = new OrgIdQuery();
        orgIdQuery.setSupplierCode(deliveryNote.getSupplierCode());
        orgIdQuery.setPurchaseCode(deliveryNote.getCustomerCode());
        R<OrgIdDTO> orgIdDTO = purchaserFeignClient.info(orgIdQuery);
        R<List<CfgParamDTO>> listByCode = cfgParamResourceFeignClient.findListByCode(DeliveryCfgParam.DELIVERY_EXCESS_QUANTITY.getCode());
        if (!(listByCode.isSuccess())) {
            throw new RuntimeException("获取同步配置出现异常");
        }

        //查询出erpId
        DeliveryNote deliveryNote1 = baseMapper.selectById(deliveryNote.getId());
        log.info("-------------------erpId----------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("erpId:,{}", JSON.toJSONString(deliveryNote1));
        log.info("erpId:,{}", JSON.toJSONString(deliveryNote1.getErpId()));
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        //获取data并且获取CfgParamEntity
        Boolean flag = false;
        BigDecimal value = null;
        List<CfgParamDTO> cfgParamDTOList = listByCode.getData();
        CfgParamDTO paramEntity = cfgParamDTOList.stream().filter(cfgParamDTO -> cfgParamDTO.getOrgId().equals(orgIdDTO.getData().getOrgId())).findFirst().orElse(null);
        if (paramEntity != null) {
//            if(true){
//                String result = "{\"data\":[{\"type\":\"BOOL\",\"value\":false},{\"type\":\"INPUT\",\"value\":\"12\"}]}";
//            JSONObject jsonObject = JSON.parseObject(result);  // result数据源：JSON格式字符串
            JSONObject jsonObject = JSON.parseObject(paramEntity.getValue());  // result数据源：JSON格式字符串
            // 获取值
            JSONArray data = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = JSON.parseObject(data.getString(0));
            flag = jsonObject1.getBoolean("value");
            if (flag == null) {
                throw new RuntimeException("获取同步配置开关出现异常");
            }
            if (flag) {//按钮开启才获取
                JSONObject jsonObject2 = JSON.parseObject(data.getString(1));
                value = jsonObject2.getBigDecimal("value");
            }
        }

        if (!quantityflag) {//数量和计划数量相等直接保存
            //正常执行更新操作
            updateById(deliveryNote);
            //删除基础信息附件，重新保存
            attachmentRelService.clearAndSave(deliveryNote.getId(), deliveryNote.getAttachmentList());
            //删除明细，重新保存
            QueryWrapper<DeliveryDetail> queryWrapper = Wrappers.query();
            queryWrapper.lambda().eq(DeliveryDetail::getDeliveryId, deliveryNote.getId());
            deliveryDetailService.remove(queryWrapper);
            deliveryDetailService.saveDetails(deliveryNote.getId(), deliveryDetailList);


            log.info("-----------------------------------------------");
            log.info("TO_BE_SIGNED:,{}", JSON.toJSONString(deliveryNote1.getDeliveryStatusCode()));
            log.info("normalFlag:,{}", JSON.toJSONString(normalFlag));
            log.info("-----------------------------------------------");
            if (deliveryNote1.getDeliveryStatusCode().equals(DeliveryStatusEnum.TO_BE_SIGNED.getCode())) {
                if (normalFlag) {
                    //正常更新的话如果明细的实际收获和计划收货数量不对，这样也要去更新收料通知单的
                    if (deliveryNote1.getErpId() != null) {
                        R<PurchaseReceiveBillCallCreateVo> deliveryOfCargoFromStorage = createDeliveryOfCargoFromStorage(deliveryNote, deliveryNote1.getErpId());
                    } else {
                        R<PurchaseReceiveBillCallCreateVo> deliveryOfCargoFromStorage = createDeliveryOfCargoFromStorage(deliveryNote, null);
                    }
                }
            }

        } else {//不相等，检查开关是否打开
            if (!flag) {//开关：关闭
                log.error("1/实际送货数量和收货数量不一致，需要修改实际送货数量，请在采购配置中允许实际送货数量和收货数量不一致,{}", JSON.toJSONString(flag));
                throw new RuntimeException("1/实际送货数量和收货数量不一致，需要修改实际送货数量，请在采购配置中允许实际送货数量和收货数量不一致");
            } else {
                if (value == null) {
                    //开关打开但是数量没有填，当做收货数量和实际数量一致的情况
                    throw new RuntimeException("2/请在在采购配置中的配置数量值，请重新配置");
                } else {
                    //开关关闭但是数量填上了，需要判断
                    for (DeliveryDetail deliveryDetail : deliveryDetailList) {
                        if (deliveryDetail.getPlanQuantity().compareTo(deliveryDetail.getRealDeliveryQuantity()) != 0) {
                            BigDecimal onehundred = new BigDecimal("100");
                            BigDecimal present = value.divide(onehundred, 20, BigDecimal.ROUND_HALF_UP);

                            BigDecimal planQuantity = deliveryDetail.getPlanQuantity();
                            BigDecimal realDeliveryQuantity = deliveryDetail.getRealDeliveryQuantity();

                            BigDecimal multiplyPresent = planQuantity.multiply(present);//乘以百分比之后的数量
                            BigDecimal configQuantity = planQuantity.add(multiplyPresent);//开关控制的数量
                            //去掉小数位
                            int i = configQuantity.intValue();
                            BigDecimal configQuantityForNOSmallNum = new BigDecimal(i);


                            if (realDeliveryQuantity.compareTo(configQuantityForNOSmallNum) == 1) {
                                throw new RuntimeException("3/实际送货数量超过计划送货数量在采购配置中的配置值，请重新配置");
                            }
                        }
                    }
                    updateById(deliveryNote);
                    //有附件移除，没有不进去
                    //删除基础信息附件，重新保存
                    if (deliveryNote.getAttachmentList() != null) {
                        attachmentRelService.clearAndSave(deliveryNote.getId(), deliveryNote.getAttachmentList());
                    }
                    //删除明细，重新保存
                    QueryWrapper<DeliveryDetail> queryWrapper = Wrappers.query();
                    queryWrapper.lambda().eq(DeliveryDetail::getDeliveryId, deliveryNote.getId());
                    deliveryDetailService.remove(queryWrapper);
                    deliveryDetailService.saveDetails(deliveryNote.getId(), deliveryDetailList);

                    //数量改变了，所以要同步erp收料通知单的数量
                    //调用bis远程接口
                    //todo***************


                    log.info("-----------------------------------------------");
                    log.info("TO_BE_SIGNED:,{}", JSON.toJSONString(deliveryNote1.getDeliveryStatusCode()));
                    log.info("normalFlag:,{}", JSON.toJSONString(normalFlag));
                    log.info("-----------------------------------------------");
                    if (deliveryNote1.getDeliveryStatusCode().equals(DeliveryStatusEnum.TO_BE_SIGNED.getCode())) {
                        //                    erpId不为空的时候进行保存
                        if (deliveryNote1.getErpId() != null) {
                            R<PurchaseReceiveBillCallCreateVo> deliveryOfCargoFromStorage = createDeliveryOfCargoFromStorage(deliveryNote, deliveryNote1.getErpId());
                        } else {
                            R<PurchaseReceiveBillCallCreateVo> deliveryOfCargoFromStorage = createDeliveryOfCargoFromStorage(deliveryNote, null);
                        }
                    }


                }
            }
        }
        //记录保存操作记录
        DeliveryOperationLog deliveryOperationLog = new DeliveryOperationLog();
        deliveryOperationLog.setBillId(deliveryNote.getId());
        deliveryOperationLog.setOperatorNo(deliveryNote.getDeliveryNo());
        deliveryOperationLog.setOperatorName(deliveryNote.getCreatedByName());
        deliveryOperationLog.setOperatorContent("送货单修改记录");
        deliveryOperationLogService.save(deliveryOperationLog);
        //发送消息通知

        deliveryNote.setOrgId(orgIdDTO.getData().getOrgId());
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteEvent.setBusinessId(deliveryNote.getId());
        deliveryNoteEvent.setDeliveryId(deliveryNote.getId());
        //发送消息通知，创建钉钉待办
        log.info("送货单编辑通知事件,送货数据{}", JSON.toJSONString(deliveryNoteEvent));
        DeliveryNoteEditEvent deliveryNoteEditEvent = new DeliveryNoteEditEvent(this, loginInfo, deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryNoteEditEvent);
        DeliveryNoteVo deliveryNoteVo = BeanUtil.copy(deliveryNote, DeliveryNoteVo.class);
        deliveryNoteVo.setDeliveryDetailList(deliveryDetailList);
        return deliveryNoteVo;
        /*updateById(deliveryNote);
        //删除基础信息附件，重新保存
        attachmentRelService.clearAndSave(deliveryNote.getId(), deliveryNote.getAttachmentList());

        //删除明细，重新保存
        QueryWrapper<DeliveryDetail> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(DeliveryDetail::getDeliveryId, deliveryNote.getId());
        deliveryDetailService.remove(queryWrapper);
        deliveryDetailService.saveDetails(deliveryNote.getId(), deliveryDetailList);



        //记录保存操作记录
        DeliveryOperationLog deliveryOperationLog = new DeliveryOperationLog();
        deliveryOperationLog.setBillId(deliveryNote.getId());
        deliveryOperationLog.setOperatorNo(deliveryNote.getDeliveryNo());
        deliveryOperationLog.setOperatorName(deliveryNote.getCreatedByName());
        deliveryOperationLog.setOperatorContent("送货单修改记录");
        deliveryOperationLogService.save(deliveryOperationLog);

        //发送消息通知
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteEvent.setBusinessId(deliveryNote.getId());
        deliveryNoteEvent.setDeliveryId(deliveryNote.getId());
        //发送消息通知，创建钉钉待办
        log.info("送货单编辑通知事件,送货数据{}", JSON.toJSONString(deliveryNoteEvent));
        DeliveryNoteEditEvent deliveryNoteEditEvent = new DeliveryNoteEditEvent(this, loginInfo, deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryNoteEditEvent);


        DeliveryNoteVo deliveryNoteVo = BeanUtil.copy(deliveryNote, DeliveryNoteVo.class);
        deliveryNoteVo.setDeliveryDetailList(deliveryDetailList);
        return deliveryNoteVo;*/
    }

    //todo，收料通知单抽取方法
    public R<PurchaseReceiveBillCallCreateVo> createDeliveryOfCargoFromStorage(DeliveryNoteSaveParam deliveryNote, Long erpId) {

        log.info("will createDeliveryOfCargoFromStorage info :{},erpId:{}", JSON.toJSONString(deliveryNote), erpId);

        R<PurchaseReceiveBillCallCreateVo> purchaseReceiveBillOrder = null;
        // List<DeliveryDetail> deliveryDetailList = deliveryNote.getDeliveryDetailList();
        List<DeliveryDetailVo> deliveryDetailList = deliveryNote.getDeliveryDetailList();
        PurchaseReceiveBillCreateOrderDto purchaseReceiveBillCreateOrderDto = new PurchaseReceiveBillCreateOrderDto();
        try {
            //            erpId不为空那么就是修改和更新
            if (erpId != null) {
                purchaseReceiveBillCreateOrderDto.setFid(erpId);
            }
            purchaseReceiveBillCreateOrderDto.setSrmId(deliveryNote.getId());

            //设置预计到厂时间
            if (deliveryNote.getEstimatedTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String str = sdf.format(deliveryNote.getEstimatedTime());
                purchaseReceiveBillCreateOrderDto.setFDate(str);
            }


            Number OrgIdNumber = new Number();
            OrgIdNumber.setFNumber(String.valueOf(deliveryNote.getCustomerCode()));
            purchaseReceiveBillCreateOrderDto.setFPurOrgId(OrgIdNumber);

            Number SupplierCodeNumber = new Number();
            SupplierCodeNumber.setFNumber(String.valueOf(deliveryNote.getSupplierCode()));
            purchaseReceiveBillCreateOrderDto.setFSupplierId(SupplierCodeNumber);
            //货主，使用的也是供应商的编码
            purchaseReceiveBillCreateOrderDto.setFOwnerIdHead(SupplierCodeNumber);

            //物料
            String materialClassification = deliveryDetailList.get(0).getMaterialClassification();
            if (materialClassification != null) {
                Number MaterialClassificationNumber = new Number();
                MaterialClassificationNumber.setFNumber(materialClassification);
                purchaseReceiveBillCreateOrderDto.setFMsWlfl(MaterialClassificationNumber);
            }

            // TODO 收料单单据类型转换
//                purchaseReceiveBillCreateOrderDto.setFBillTypeId();

            //送货计划的明细字段
            List<PurchaseReceiveBillCreateOrderDto.FDetailEntity> fDetailEntities = new ArrayList<>();
            for (DeliveryDetail detail : deliveryDetailList) {
                PurchaseReceiveBillCreateOrderDto.FDetailEntity fDetailEntity = new PurchaseReceiveBillCreateOrderDto.FDetailEntity();

                //单价
                if (detail.getProductUnitPrice() == null) {
                    log.info("单价为空，{}", JSON.toJSONString(deliveryDetailList));
                    throw new RuntimeException("单价为空");
                }
                if (detail.getProductUnitPrice() != null) {
                    fDetailEntity.setFPrice(detail.getProductUnitPrice());
                }

                //含税单价
                if (detail.getTaxUnitPrice() == null) {
                    log.info("含税单价为空，{}", JSON.toJSONString(deliveryDetailList));
                    throw new RuntimeException("单价为空");
                }
                if (detail.getTaxUnitPrice() != null) {
                    fDetailEntity.setFTaxPrice(detail.getTaxUnitPrice());
                }

                // //商家编码
                // fDetailEntity.setFMssjbm(detail.getBarCode());

                //预计预计到货日期
                if (deliveryNote.getEstimatedTime() != null) {
                    SimpleDateFormat sdfForFPreDeliveryDatePattern = new SimpleDateFormat("yyyy-MM-dd");
                    String strForFPreDeliveryDatePattern = sdfForFPreDeliveryDatePattern.format(deliveryNote.getEstimatedTime());
                    fDetailEntity.setFPreDeliveryDate(strForFPreDeliveryDatePattern);
                }

                if (detail.getErpId() != null) {
                    fDetailEntity.setFEntryId(detail.getErpId());
                }

                //物料编码
                if (detail.getProductCode() != null) {
                    Number productCodeNumber = new Number();
                    productCodeNumber.setFNumber(detail.getProductCode());
                    fDetailEntity.setFMaterialId(productCodeNumber);
                }

                //收料单位和计价单位 ： 对应planUnit
                //            if (detail.getPlanUnit() != null) {
                Number planUnitNumber = new Number();
                planUnitNumber.setFNumber(detail.getDeliveryUnitName());
                //暂时的解决方法，设置pcs
                //                planUnitNumber.setFNumber("pcs");
                fDetailEntity.setFUnitId(planUnitNumber);
                fDetailEntity.setFPriceUnitId(planUnitNumber);
                fDetailEntity.setFStockUnitId(planUnitNumber);
                //            }

                //交货数量
                if (detail.getRealDeliveryQuantity() != null) {
                    fDetailEntity.setFActReceiveQty(detail.getRealDeliveryQuantity().toString());
                }
                //供应商交货数量
                if (detail.getPlanQuantity() != null) {
                    fDetailEntity.setFsupdelqty(detail.getPlanQuantity().toString());
                }
                //仓库
                if (detail.getWarehouse() != null) {
                    Number warehouseNumber = new Number();
                    warehouseNumber.setFNumber(detail.getWarehouse());
                    fDetailEntity.setFStockId(warehouseNumber);
                }
                //计价基本数量  =实际送货数量
                if (detail.getRealDeliveryQuantity() != null) {
                    fDetailEntity.setFPriceBaseQty(detail.getRealDeliveryQuantity().toString());
                    //库存单位数量=实际送货数量
                    fDetailEntity.setFStockQty(detail.getRealDeliveryQuantity().toString());
                    //实到数量:  可以不传
                    fDetailEntity.setFActlandQty(detail.getRealDeliveryQuantity().toString());
                }
                //明细的id
                if (detail.getId() != null) {
                    fDetailEntity.setSrmId(detail.getId());
                }

                // 添加批次
                fDetailEntity.setFLot( new Number( detail.getBatchNo() ) );

                // 添加关联关系
                wrapperDetailEntryLink(fDetailEntity, detail);

                // 在美尚测试环境中如果送货单中的采购方为800（美尚（广州）化妆品制造有限公司 ），则需固定传入仓位编码值4000-0001
                developEnvHelper.nonProdIsRun( () -> {
                    if( "800".equals( deliveryNote.getCustomerCode()) ){
                        PurchaseReceiveBillCreateOrderDto.FStockLocId FStockLocId = new PurchaseReceiveBillCreateOrderDto.FStockLocId();
                        FStockLocId.setFstocklocidFf100002( new Number( "4000-0001") );
                        fDetailEntity.setStockLocId(FStockLocId);
                    }
                } );


                fDetailEntities.add(fDetailEntity);
            }
            purchaseReceiveBillCreateOrderDto.setFDetailEntity(fDetailEntities);

            //单据类型:默认值 SLD01_SYS
            String orderType = deliveryDetailList.get(0).getOrderType();
            if (orderType == null) {//没有单据类型
                log.error("送货单详情没有传单据类型：orderType= ,{}", JSON.toJSONString(orderType));
                throw new RuntimeException("没有单据类型" + orderType);
            }


            if (orderType != null) {
                if (orderType.equals("CGDD01_SYS")) {
                    purchaseReceiveBillCreateOrderDto.setFBillTypeId(new Number(FBillTypeEnum.FBILLTYPE_STANDAR.getCode()));

                } else if (orderType.equals("CGDD02_SYS")) {
                    purchaseReceiveBillCreateOrderDto.setFBillTypeId(new Number(FBillTypeEnum.FBILLTYPE_OUTSOURCING.getCode()));
                }
            }

            //补充字段
            //        purchaseReceiveBillCreateOrderDto.setFMsWlfl(new Number(deliveryNote.getDeliveryTypeCode()));

            log.info("purchaseReceiveBillCreateOrderDto字段值：单据类型:{}," + JSON.toJSONString(purchaseReceiveBillCreateOrderDto), purchaseReceiveBillCreateOrderDto.getFBillTypeId());
            purchaseReceiveBillOrder = purchaseReceiveBillOrderFeignClient.createPurchaseReceiveBillOrder(purchaseReceiveBillCreateOrderDto);
            if (!(purchaseReceiveBillOrder.isSuccess())) {
                throw new RuntimeException(purchaseReceiveBillOrder.getMsg());
            }

            if (purchaseReceiveBillOrder.getCode() != 200) {

                log.info("purchaseReceiveBillCreateOrderDto字段值：" + JSON.toJSONString(purchaseReceiveBillCreateOrderDto));
                log.info("purchaseReceiveBillCreateOrderDto字段值：" + JSON.toJSONString(purchaseReceiveBillOrder));
                throw new RuntimeException("" + JSON.toJSONString(purchaseReceiveBillCreateOrderDto));
            }


                       /* if (purchaseReceiveBillOrder.getData() != null) {
                            Long fId = purchaseReceiveBillOrder.getData().getFId();
                            String fNumber = purchaseReceiveBillOrder.getData().getFNumber();
                            List<PurchaseReceiveBillEntryCallCreateVo> purchaseReceiveBillEntryCallCreateVoList = purchaseReceiveBillOrder.getData().getPurchaseReceiveBillEntryCallCreateVoList();

                            //保存erpId和单据编码
                            updateErpId(deliveryNote.getId(), fId, fNumber);
                            //保存detail的erpId
                            for (DeliveryDetail detail : deliveryDetailList) {
                                for (PurchaseReceiveBillEntryCallCreateVo purchaseReceiveBillEntryCallCreateVo : purchaseReceiveBillEntryCallCreateVoList) {
                                    if (purchaseReceiveBillEntryCallCreateVo.getSrmId().equals(detail.getId())) {
                                        detail.setErpId(purchaseReceiveBillEntryCallCreateVo.getErpId());
                                        deliveryDetailService.updateById(detail);
                                    }
                                }
                            }
                            deliveryDetailService.updateDetailErpId(deliveryDetailList);
                        }*/
        } catch (Exception e) {
            log.error("收料通知单问题：传输的参数:{}", JSON.toJSONString(purchaseReceiveBillCreateOrderDto));
            log.error("收料通知单问题：返回数据：,{}", JSON.toJSONString(purchaseReceiveBillOrder));
            log.error("收料通知单问题：返回异常", e);
            throw new RuntimeException("收料通知单问题" + JSON.toJSONString(purchaseReceiveBillOrder));
        }

        return purchaseReceiveBillOrder;


    }


    /**
     * 送货单数量统计
     *
     * @return
     */
    @Override
    public DeliveryStatisticsVo statistics() {
        //申请退回数量
        QueryWrapper<DeliveryNote> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(DeliveryNote::getDeliveryStatusCode, DeliveryStatusEnum.APPLICATION_RETURN.getCode());
        SecurityUtils.getCurrentSupplier().ifPresent(supplier ->
                queryWrapper.lambda().eq(DeliveryNote::getSupplierCode, supplier.getSupplierCode()));
        int applySendBack = count(queryWrapper);

        //送货退回数量
        QueryWrapper<DeliveryNote> queryWrapper0 = Wrappers.query();
        queryWrapper0.lambda().eq(DeliveryNote::getDeliveryStatusCode, DeliveryStatusEnum.DELIVERY_RETURN.getCode());
        SecurityUtils.getCurrentSupplier().ifPresent(supplier ->
                queryWrapper0.lambda().eq(DeliveryNote::getSupplierCode, supplier.getSupplierCode()));
        int deliverySendBack = count(queryWrapper0);

        //待签收的数量
        QueryWrapper<DeliveryNote> queryWrapper1 = Wrappers.query();
        queryWrapper1.lambda().eq(DeliveryNote::getDeliveryStatusCode, DeliveryStatusEnum.TO_BE_SIGNED.getCode());
        SecurityUtils.getCurrentSupplier().ifPresent(supplier ->
                queryWrapper1.lambda().eq(DeliveryNote::getSupplierCode, supplier.getSupplierCode()));
        int toBeSignedNum = count(queryWrapper1);

        //申请待批准的数量
        QueryWrapper<DeliveryNote> queryWrapper2 = Wrappers.query();
        queryWrapper2.lambda().eq(DeliveryNote::getDeliveryStatusCode, DeliveryStatusEnum.APPLYING.getCode());
        SecurityUtils.getCurrentSupplier().ifPresent(supplier ->
                queryWrapper2.lambda().eq(DeliveryNote::getSupplierCode, supplier.getSupplierCode()));
        int applicationPendingNum = count(queryWrapper2);

        //待ERP签收数量
        QueryWrapper<DeliveryNote> queryWrapper3 = Wrappers.query();
        queryWrapper3.lambda().eq(DeliveryNote::getDeliveryStatusCode, DeliveryStatusEnum.SIGNING_AND_CONFIRMING.getCode());
        SecurityUtils.getCurrentSupplier().ifPresent(supplier ->
                queryWrapper3.lambda().eq(DeliveryNote::getSupplierCode, supplier.getSupplierCode()));
        int toBeSignedByERPNum = count(queryWrapper3);

        return DeliveryStatisticsVo.builder()
                .applySendBack(applySendBack)
                .deliverySendBack(deliverySendBack)
                .toBeSignedNum(toBeSignedNum)
                .applicationPendingNum(applicationPendingNum)
                .toBeSignedByERPNum(toBeSignedByERPNum).build();
    }

    @Override
    public DeliveryStatisticsVo selectToBeSignCount() {
        //待签收的数量
        QueryWrapper<DeliveryNote> queryWrapper1 = Wrappers.query();
        queryWrapper1.lambda().eq(DeliveryNote::getDeliveryStatusCode, DeliveryStatusEnum.TO_BE_SIGNED.getCode());
        SecurityUtils.getCurrentSupplier().ifPresent(supplier ->
                queryWrapper1.lambda().eq(DeliveryNote::getSupplierCode, supplier.getSupplierCode()));
        int toBeSignedNum = count(queryWrapper1);

        return DeliveryStatisticsVo.builder()
                .toBeSignedNum(toBeSignedNum)
                .build();
    }

    @Override
    public DeliveryStatisticsVo selectStatistics() {
        //送货单的数量
        QueryWrapper<DeliveryNote> queryWrapper1 = Wrappers.query();
        SecurityUtils.getCurrentSupplier().ifPresent(supplier ->
                queryWrapper1.lambda().eq(DeliveryNote::getSupplierCode, supplier.getSupplierCode()));
        int toBeSignedNum = count(queryWrapper1);

        return DeliveryStatisticsVo.builder()
                .toBeSignedNum(toBeSignedNum)
                .build();
    }

    /**
     * 根据送货单单号获取送货通知单详情
     *
     * @param deliveryNo
     * @return
     */
    @Override
    public DeliveryNoteVo getDeliveryNoteInfoByNo(String deliveryNo){
        QueryWrapper<DeliveryNote> queryWrapper1 = Wrappers.query();
        DeliveryNote one = getOne(queryWrapper1.lambda().eq(DeliveryNote::getDeliveryNo, deliveryNo));
        if (one == null) {
            throw new BizException(DeliveryResultCode.RESOURCE_NOT_FOUND);
        }
        return BeanUtil.copy(one, DeliveryNoteVo.class);
    }

    /**
     * 获取送货通知单详情
     *
     * @param id
     * @return
     */
    @Override
    public DeliveryNoteVo getDeliveryNoteInfo(Long id) {
        DeliveryNote deliveryNote = getById(id);
        if (deliveryNote == null) {
            throw new BizException(DeliveryResultCode.RESOURCE_NOT_FOUND);
        }
        DeliveryNoteVo deliveryNoteVo = BeanUtil.copy(deliveryNote, DeliveryNote.class, DeliveryNoteVo.class);

//        这里设置附件,待定
//        AttachmentRel attachmentRel = AttachmentRel.builder().businessFormId(id).build();
//        QueryWrapper<AttachmentRel> queryWrapper = Condition.getQueryWrapper(attachmentRel);
//        List<AttachmentRel> attachmentRelList = attachmentRelService.list(queryWrapper);
//        LambdaQueryWrapper<DeliveryNote> queryWrapper = Wrappers.lambdaQuery();
//        attachmentRelService.list(queryWrapper);


        //设置附件
        buildAttachment(deliveryNoteVo);
        //获取明细信息
        DeliveryDetail deliveryDetail = new DeliveryDetail().setDeliveryId(id);
        List<DeliveryDetail> deliveryDetailList = deliveryDetailService.list(Condition.getQueryWrapper(deliveryDetail));

        List<DeliveryDetailVo> detailVos = BeanUtil.copy(deliveryDetailList, DeliveryDetailVo.class);
        //设置送货明细中的COA附件
        batchSetDeliveryDetailAttachment(detailVos);

        // 查询送货项次的日期
        if( CollectionUtil.isNotEmpty( detailVos )){
            Set<Long> deliveryPlanDetailItemIdSet = detailVos.stream().map(DeliveryDetailVo::getDeliveryPlanDetailItemId).collect(Collectors.toSet());
            Collection<DeliveryPlanDetailItem> deliveryPlanDetailItemCollection = deliveryPlanDetailItemService.listByIds(deliveryPlanDetailItemIdSet);
            Map<Long, LocalDateTime> deliveryPlanDetailItemDeliveryDateMap = deliveryPlanDetailItemCollection.stream().collect(Collectors.toMap(DeliveryPlanDetailItem::getId, DeliveryPlanDetailItem::getDeliveryDate));
            detailVos.forEach( deliveryDetailVo -> {
                Optional.ofNullable(deliveryPlanDetailItemDeliveryDateMap.get(deliveryDetailVo.getDeliveryPlanDetailItemId())).ifPresent( date -> {
                    deliveryDetailVo.setDeliveryDate(  date.toLocalDate());
                } );
            } );
        }

        deliveryNoteVo.setDeliveryDetailList(detailVos);


        /*//设置结算币别、物料分类
        DeliveryAsPlanParam deliveryAsPlanParam = new DeliveryAsPlanParam();
        deliveryAsPlanParam.setPlanNo(deliveryDetailList.get(0).getPlanNo());
        QueryParam<DeliveryAsPlanParam> queryParam = new QueryParam<>();
        queryParam.setParam(deliveryAsPlanParam);
//        Pageable unpaged = Pageable.unpaged();
        Page<DeliveryPlanDetailItemVo> page = new Page<>();
        page.setCurrent(0);
        page.setSize(1);
        IPage<DeliveryPlanDetailItemVo> deliveryAsPlanList = deliveryPlanService.getDeliveryAsPlanList(page, queryParam);
        deliveryNoteVo.setDeliveryAsPlan(deliveryAsPlanList.getRecords());*/

        //获取供应商编码
//        String supplierName = SecurityUtils.getCurrentSupplier().get().getSupplierName();

//        数据字典
//        送货方式，对应字典delivery_delivery_method
        String deliveryMethod = deliveryNote.getDeliveryMethod();
        if (StringUtil.equals(deliveryMethod, DictCodeDeliveryMethodEnum.SUPPLIER_DELIVERY.getCode())) {
            deliveryNoteVo.setDeliveryMethod(DictCodeDeliveryMethodEnum.SUPPLIER_DELIVERY.getMsg());
        } else if (StringUtil.equals(deliveryMethod, DictCodeDeliveryMethodEnum.EXPRESS.getCode())) {
            deliveryNoteVo.setDeliveryMethod(DictCodeDeliveryMethodEnum.EXPRESS.getMsg());
        } else if (StringUtil.equals(deliveryMethod, DictCodeDeliveryMethodEnum.PROVIDED_BY_PURCHASER.getCode())) {
            deliveryNoteVo.setDeliveryMethod(DictCodeDeliveryMethodEnum.PROVIDED_BY_PURCHASER.getMsg());
        }

        //设值：运输方式
        String tm = deliveryNote.getTransportMethod();
        if (StringUtil.equals(tm, DictCodeTransportMethodEnum.CAR.getCode())) {
            deliveryNoteVo.setTransportMethod(DictCodeTransportMethodEnum.CAR.getMsg());
        } else if (StringUtil.equals(tm, DictCodeTransportMethodEnum.RAILWAY.getCode())) {
            deliveryNoteVo.setTransportMethod(DictCodeTransportMethodEnum.RAILWAY.getMsg());
        } else if (StringUtil.equals(tm, DictCodeTransportMethodEnum.PLANE.getCode())) {
            deliveryNoteVo.setTransportMethod(DictCodeTransportMethodEnum.PLANE.getMsg());
        }else if (StringUtil.equals(tm, DictCodeTransportMethodEnum.SHIP.getCode())){
            deliveryNoteVo.setTransportMethod(DictCodeTransportMethodEnum.SHIP.getMsg());
        } else {
            deliveryNoteVo.setTransportMethod(" ");
        }

        //设置报关资料
        DeliveryCustomsInformation deliveryCustomsInformationParam = DeliveryCustomsInformation.builder().deliveryId(id).build();
        QueryWrapper<DeliveryCustomsInformation> deliveryCustomsInformationQueryWrapper = Condition.getQueryWrapper(deliveryCustomsInformationParam);
        List<DeliveryCustomsInformation> list = deliveryCustomsInformationService.list(deliveryCustomsInformationQueryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            deliveryNoteVo.setDeliveryCustomsInformation(list.get(0));
        }

        //获取权限按钮
        deliveryNoteVo.setPermissionButton(DeliveryStatusOperateRel.operations(deliveryNoteVo.getDeliveryStatusCode(), deliveryNoteVo.getLogisticsStatus()));
//        deliveryNoteVo.setSupplierName(supplierName);
        return deliveryNoteVo;
    }

    /**
     * 申请发货
     *
     * @param id
     * @return
     */
    @Override
    @OperationLog(code = "apply_delivery", content = "申请发货")
    public R applyDelivery(Long id) {
        DeliveryNote deliveryNote = getById(id);
        deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.APPLYING.getCode());
        updateById(deliveryNote);
        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteEvent.setDeliveryId(deliveryNote.getId());
        deliveryNoteEvent.setBusinessId(deliveryNote.getId());
        //发送消息通知，创建钉钉待办
        log.info("送货申请发货事件,送货数据{}", JSON.toJSONString(deliveryNoteEvent));

        DeliveryApplyIssueEvent deliveryApplyIssueEvent = new DeliveryApplyIssueEvent(this, getLoginInfo(), deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryApplyIssueEvent);

        //结束送货申请退回
        HandlerMsgParam handlerMsgParam = new HandlerMsgParam();
        handlerMsgParam.setEventCode(EventCode.delivery_apply_returned.getCode());
        handlerMsgParam.setBusinessId(id);
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        handlerMsgParam.setReceiverType(loginInfo.getCurrentSubjectType());
        informationServiceFeignClient.handlerInformation(handlerMsgParam);


        return R.success();
    }

    private LoginInfo getLoginInfo() {
        LoginInfo loginInfo = null;
        //获取当前登录信息
        Optional<BncUserDetails> userDetailsOptional = SecurityUtils.getCurrentUser();
        if (userDetailsOptional.isPresent()) {
            loginInfo = SecurityUtils.getLoginInfoOrThrow();
        } else {
            log.warn("获取用户登录信息失败");
        }
        return loginInfo;
    }

    /**
     * 作废申请
     *
     * @param id 送货单主键ID
     * @return
     */
    @Override
    @OperationLog(code = "invalid_apply", content = "作废申请")
    public R invalidApply(Long id) {
        DeliveryNote deliveryNote = getById(id);
        deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.APPLICATION_INVALIDATION.getCode());
        updateById(deliveryNote);

        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteEvent.setBusinessId(deliveryNote.getId());
        //发送消息通知，创建钉钉待办
        log.info("送货作废申请事件,送货数据{}", JSON.toJSONString(deliveryNoteEvent));
        DeliveryApplyInvalidEvent deliveryApplyInvalidEvent = new DeliveryApplyInvalidEvent(this, getLoginInfo(), deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryApplyInvalidEvent);

        //结束送货申请退回
        HandlerMsgParam handlerMsgParam = new HandlerMsgParam();
        handlerMsgParam.setEventCode(EventCode.delivery_apply_returned.getCode());
        handlerMsgParam.setBusinessId(id);
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        handlerMsgParam.setReceiverType(loginInfo.getCurrentSubjectType());
        informationServiceFeignClient.handlerInformation(handlerMsgParam);


        return R.success();
    }

    /**
     * 撤回申请
     *
     * @param id
     * @return
     */
    @Override
    @OperationLog(code = "withdraw_apply", content = "撤回申请")
    public R withdrawApply(Long id) {
        DeliveryNote deliveryNote = getById(id);
        deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.APPLICATION_WITHDRAWAL.getCode());
        updateById(deliveryNote);
        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteEvent.setBusinessId(deliveryNote.getId());
        //发送消息通知，创建钉钉待办
        log.info("送货撤回申请事件,送货数据{}", JSON.toJSONString(deliveryNoteEvent));
        DeliveryApplyWithdrawEvent deliveryApplyWithdrawEvent = new DeliveryApplyWithdrawEvent(this, getLoginInfo(), deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryApplyWithdrawEvent);
        return R.success();
    }

    /**
     * 确认发出
     *
     * @param id
     * @return
     */
    @Override
    @OperationLog(code = "confirmation_issued", content = "确认发出")
    public R confirmationIssued(Long id) {
        DeliveryNote deliveryNote = getById(id);
        deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.TO_BE_SIGNED.getCode());
        updateById(deliveryNote);

        DeliveryNoteEvent deliveryNoteVo = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteVo.setDeliveryId(deliveryNote.getId());
        deliveryNoteVo.setBusinessId(deliveryNote.getId());

        //发送消息通知，创建钉钉待办
        log.info("送货通知单确认发出事件,送货数据{}", JSON.toJSONString(deliveryNoteVo));
        DeliveryNoteIssueEvent deliveryNoteIssueEvent = new DeliveryNoteIssueEvent(this, getLoginInfo(), deliveryNoteVo, deliveryNoteVo.getSupplierCode(), deliveryNoteVo.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryNoteIssueEvent);

        //结束送货通知单退回
        HandlerMsgParam handlerMsgParam = new HandlerMsgParam();
        handlerMsgParam.setEventCode(EventCode.delivery_note_returned.getCode());
        handlerMsgParam.setBusinessId(id);
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        handlerMsgParam.setReceiverType(loginInfo.getCurrentSubjectType());
        informationServiceFeignClient.handlerInformation(handlerMsgParam);

        return R.success();
    }

    /**
     * 撤回送货
     *
     * @param id
     * @return
     */
    @Override
    public R withdrawDelivery(Long id) {
        DeliveryNote deliveryNote = getById(id);
        deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.DELIVERY_WITHDRAWAL.getCode());
        updateById(deliveryNote);
        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteEvent.setBusinessId(deliveryNote.getId());
        //发送消息通知，创建钉钉待办
        log.info("送货通知单撤回送货事件,送货数据{}", JSON.toJSONString(deliveryNoteEvent));
        DeliveryNoteWithdrawEvent deliveryNoteWithdrawEvent = new DeliveryNoteWithdrawEvent(this, getLoginInfo(), deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryNoteWithdrawEvent);
        return R.success();
    }

    /**
     * 货物已发（待签收）
     *
     * @param id
     * @return
     */
    @Override
    public R delivered(Long id) {
        DeliveryNote deliveryNote = getById(id);
        deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.TO_BE_SIGNED.getCode());
        deliveryNote.setLogisticsStatus(LogisticsStatusEnum.TO_BE_DELIVERED.getCode());
        deliveryNote.setErpSigningStatus(ErpSigningStatusEnum.NOT_SIGNED.getCode());
        updateById(deliveryNote);
        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteEvent.setBusinessId(deliveryNote.getId());
        //发送消息通知，创建钉钉待办
        log.info("送货通知单货物已发事件,送货数据{}", JSON.toJSONString(deliveryNoteEvent));
        DeliveryNoteDeliveredEvent deliveryNoteDeliveredEvent = new DeliveryNoteDeliveredEvent(this, getLoginInfo(), deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryNoteDeliveredEvent);
        return R.success();
    }

    /**
     * 作废送货
     *
     * @param id
     * @return
     */
    @Override
    @OperationLog(code = "invalid_delivery", content = "作废送货")
    public R invalidDelivery(Long id) {
        DeliveryNote deliveryNote = getById(id);
        deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.DELIVERY_INVALIDATION.getCode());
        updateById(deliveryNote);
        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteEvent.setBusinessId(deliveryNote.getId());
        //发送消息通知，创建钉钉待办
        log.info("送货通知单作废送货事件,送货数据{}", JSON.toJSONString(deliveryNoteEvent));
        DeliveryNoteInvalidEvent deliveryNoteInvalidEvent = new DeliveryNoteInvalidEvent(this, getLoginInfo(), deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryNoteInvalidEvent);


        //结束送货通知单退回
        HandlerMsgParam handlerMsgParam = new HandlerMsgParam();
        handlerMsgParam.setEventCode(EventCode.delivery_note_returned.getCode());
        handlerMsgParam.setBusinessId(id);
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        handlerMsgParam.setReceiverType(loginInfo.getCurrentSubjectType());
        informationServiceFeignClient.handlerInformation(handlerMsgParam);


        return R.success();
    }

    /**
     * 提醒
     *
     * @param id
     * @return
     */
    @Override
    public R remind(Long id) {
        //发送消息通知，创建钉钉待办
        DeliveryNote deliveryNote = getById(id);

        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteEvent.setBusinessId(deliveryNote.getId());
        log.info("送货申请提醒事件,送货数据{}", JSON.toJSONString(deliveryNote));
        DeliveryApplyRemindEvent deliveryApplyRemindEvent = new DeliveryApplyRemindEvent(this, getLoginInfo(), deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryApplyRemindEvent);
        return R.success();
    }

    /**
     * 设置附件
     *
     * @param record
     */
    @Override
    public void buildAttachment(DeliveryNoteVo record) {
        if (record == null) {
            return;
        }
        AttachmentRel attachmentRel = AttachmentRel.builder().businessFormId(record.getId()).build();
        QueryWrapper<AttachmentRel> queryWrapper = Condition.getQueryWrapper(attachmentRel);
        List<AttachmentRel> attachmentRelList = attachmentRelService.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(attachmentRelList)) {
            List<FileInfo> attachmentList = new ArrayList<>();
            for (AttachmentRel rel : attachmentRelList) {
                FileInfo attachment = FileInfo.builder()
                        .id(rel.getAttachmentId())
                        .originalFilename(rel.getAttachmentName())
                        .url(rel.getAttachmentUrl())
                        .size(rel.getAttachmentSize())
                        .build();
                attachmentList.add(attachment);
            }
            record.setAttachmentList(attachmentList);
        } else {
            record.setAttachmentList(new ArrayList<>());
        }

    }

    /**
     * 批量设置附件
     *
     * @param records
     */
    @Override
    public void buildAttachmentBatch(List<DeliveryNoteVo> records) {
        if (CollectionUtil.isNotEmpty(records)) {
            for (DeliveryNoteVo record : records) {
                buildAttachment(record);
            }
        }

    }

    /**
     * 设置权限按钮
     *
     * @param records
     */
    @Override
    public void buildPermissionButtonBatch(List<DeliveryNoteVo> records) {
        for (DeliveryNoteVo record : records) {
            record.setPermissionButton(DeliveryStatusOperateRel.operations(record.getDeliveryStatusCode(), record.getLogisticsStatus()));
        }
    }

    @Override
    public List<DeliveryNote> queryList(String params) {
        log.info("查询列表，供feign调用,接受参数:{}", params);
        DeliveryNote deliveryNote = JSON.parseObject(params, DeliveryNote.class);
        QueryWrapper<DeliveryNote> queryWrapper = Condition.getQueryWrapper(deliveryNote);
        queryWrapper.orderByDesc("last_modified_date");
        queryWrapper.last("limit 100");
        List<DeliveryNote> list = list(queryWrapper);
        log.info("数据数量{}", list.size());
        return list;
    }

    @Override
    public boolean updateSettlementPoolSyncStatus(List<Long> deliveryIdList) {
        if (CollectionUtils.isNotEmpty(deliveryIdList)) {
            return update(Wrappers.<DeliveryNote>update().lambda().
                    set(DeliveryNote::getSettlementPoolSyncStatus, "Y")
                    .in(DeliveryNote::getId, deliveryIdList));
        }
        return false;
    }

    /**
     * 根据收、发货单ID，查询收发货单信息列表
     *
     * @param deliveryIdList 收、发货单ID
     * @return
     */
    @Override
    public List<DeliveryNote> queryListByDeliveryIds(List<Long> deliveryIdList) {
        if (CollectionUtils.isNotEmpty(deliveryIdList)) {
            LambdaQueryWrapper<DeliveryNote> queryWrapper = Condition.getQueryWrapper(new DeliveryNote()).lambda();
            queryWrapper.in(DeliveryNote::getId, deliveryIdList);
            return list(queryWrapper);
        }
        return null;
    }

    /**
     * 物料通知单
     *
     * @param deliveryId 送货通知 ID
     * @return 物料通知单信息
     */
    @Override
    public DeliveryMaterialNoticeDTO wrapMaterialNotice(Long deliveryId) {
        DeliveryMaterialNoticeDTO materialNoticeDTO = new DeliveryMaterialNoticeDTO();
        DeliveryNote deliveryNote = getById(deliveryId);
        DeliveryNoteDTO noteDTO = BeanUtil.copy(deliveryNote, DeliveryNoteDTO.class);
        LambdaQueryWrapper<DeliveryDetail> queryWrapper = Condition.getQueryWrapper(DeliveryDetail.builder().deliveryId(deliveryId).build()).lambda();
        List<DeliveryDetail> deliveryDetailList = deliveryDetailService.list(queryWrapper);
        List<DeliveryDetailDTO> detailDTOList = BeanUtil.copy(deliveryDetailList, DeliveryDetailDTO.class);
        materialNoticeDTO.setDeliveryNote(noteDTO);
        materialNoticeDTO.setDeliveryDetailList(detailDTOList);

        return materialNoticeDTO;
    }


    /**
     * 同步ERP送货通知单信息
     *
     * @param deliveryNoteUpdateDTO ERP送货通知单信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void syncErpDeliveryNoteDetailInfo(DeliveryDetailUpdateDTO deliveryNoteUpdateDTO) {
        /*
         * 接口SRM009：每半小时同步ERP入库单数据，更新智采送货单的（明细）收货数量。
         * 送货状态-已完成
         * ERP签收状态-已签收
         * 签收时间
         * 送货明细-收货数量
         * */
        log.info("[syncErpDeliveryNoteDetailInfo]接收参数deliveryNoteUpdateDTO为：{}", JSON.toJSONString(deliveryNoteUpdateDTO));
        if (deliveryNoteUpdateDTO == null) {
            log.error("同步ERP送货通知单信息，参数为null。");
            return;
        }

        // 送货单明细的erpId
        Long erpId = deliveryNoteUpdateDTO.getErpId();
        DeliveryDetail deliveryDetail = deliveryDetailService.getOne(
                Wrappers.<DeliveryDetail>lambdaQuery()
                        .select(DeliveryDetail::getId, DeliveryDetail::getDeliveryId,DeliveryDetail::getDeliveryPlanDetailItemId)
                        .eq(DeliveryDetail::getErpId, erpId)
        );

        if (deliveryDetail == null) {
            log.error("[syncErpDeliveryNoteDetailInfo] 查询送货单明细失败 ，参数：{} ", deliveryNoteUpdateDTO);
            return;
        }

        //查询送货单信息
        DeliveryNote deliveryNote = getOne(
                Wrappers.<DeliveryNote>lambdaQuery()
                        .select(DeliveryNote::getId)
                        .eq(DeliveryNote::getId, deliveryDetail.getDeliveryId())
        );
        if (deliveryNote == null) {
            log.error("[syncErpDeliveryNoteDetailInfo] 查询送货单失败 ，查询参数：{} ", deliveryDetail);
            return;
        }

        // 更新送货单明细
        deliveryDetail.setReceiptQuantity(deliveryNoteUpdateDTO.getReceiptQuantity());
        deliveryDetailService.updateById(deliveryDetail);

        deliveryNote.setSigningTime(deliveryNoteUpdateDTO.getWarehouseDate());
        deliveryNote.setErpSigningStatus(ErpSigningStatusEnum.SIGNED.getCode());
        deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.COMPLETED.getCode());
        updateById(deliveryNote);

        // 更新入库数量到订单明细中
        try {
            DeliveryPlanDetailItem deliveryPlanDetailItem = deliveryPlanDetailItemService.getById(deliveryDetail.getDeliveryPlanDetailItemId());
            DeliveryPlanDetail deliveryPlanDetail = deliveryPlanDetailService.getById(deliveryPlanDetailItem.getDeliveryPlanDetailId());
            // 订单明细来源ID
            String orderProductDetailSourceId = deliveryPlanDetail.getSourceId();
            R r = zcOrderServiceFeignClient.addOrderProductDetailInventoryQuantity( orderProductDetailSourceId, deliveryNoteUpdateDTO.getReceiptQuantity());
            log.info("addOrderProductDetailInventoryQuantity result info:{}",JSON.toJSONString(r));
            if (! r.isSuccess()) {
                log.error("[syncErpDeliveryNoteDetailInfo]更新入库数量到订单明细中失败，message:{}，需要手动补偿，订单产品明细来源ID：{},ReceiptQuantity:{}", r.getMsg(), orderProductDetailSourceId, deliveryNoteUpdateDTO.getReceiptQuantity());
            }
        } catch (Exception ex) {
            //更新入库数量到订单明细中失败异常！{"deliveryId":1499673796946243586,"id":1499673797382451202,"lastModifiedDate":1647242914403,"receiptQuantity":100}
            log.error("[syncErpDeliveryNoteDetailInfo]更新入库数量到订单明细中失败异常！{}", JSON.toJSONString(deliveryDetail), ex);
        }

    }

    /**
     * 待签收送货单数量统计
     */
    @Override
    public Integer selectCount() {
        return deliveryNoteMapper.selectByNotSign();
    }


    /**
     * 批量发货
     *
     * @param ids 计划明细项次id
     */
    @Override
    @Transactional
    public Long batchDelivery(List<String> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            ArrayList<DeliveryPlanDetailItemVo> itemVos = new ArrayList<>();
            for (String id : ids) {
                DeliveryPlanDetailItemVo detailItemVo = planMapper.getPlanBoardDetailById(id);
                itemVos.add(detailItemVo);
            }
            //判断：关联了送货单的项次不能二次发货
            itemVos.forEach(itemVo -> {
                if (StringUtil.isNotBlank(itemVo.getDeliveryNoteId())) {
                    throw new ApiException(ResultCode.REQ_REJECT.getCode(), "关联了送货单的项次不能被二次发货");
                }
            });


            //取出orderType
//            Long deliveryPlanId = itemVos.get(0).getDeliveryPlanId();
//            DeliveryPlan plan = deliveryPlanService.getById(deliveryPlanId);
//            String orderType = plan.getOrderType();

            //校验:采购方、结算币种、物料分类、送货地址（要相同）
            AtomicBoolean flag = new AtomicBoolean(false);
            String purchaseCode = itemVos.get(0).getPurchaseCode();
            String purchaseName = itemVos.get(0).getPurchaseName();
            String currency = itemVos.get(0).getCurrency();
            String materialClassification = itemVos.get(0).getMaterialClassification();
            String deliveryAddress = itemVos.get(0).getDeliveryAddress();

            itemVos.forEach(itemVo -> {
                if (purchaseCode.equals(itemVo.getPurchaseCode()) && purchaseName.equals(itemVo.getPurchaseName())
                        && currency.equals(itemVo.getCurrency()) && materialClassification.equals(itemVo.getMaterialClassification())
                        && deliveryAddress.equals(itemVo.getDeliveryAddress())) {
                    flag.set(true);
                }
            });


            //适应送货计划批量发货的接口，他那边批量发货的时候没有supplierName，由保存接口提供
            String supplierName = null;
            String supplierCode = SecurityUtils.getLoginInfo().get().getCurrentSupplier().getSupplierCode();
            SupplierDTO supplierDTO = new SupplierDTO();
            supplierDTO.setCode(supplierCode);
            R<SupplierDTO> oneSupplierByCode = supplierFeignClient.findOneSupplierByCode(supplierDTO);
            if (oneSupplierByCode.isSuccess()) {
                supplierName = oneSupplierByCode.getData().getName();
            }

            if (flag.get()) {
                //新建送货单，给默认值
                DeliveryNote note = new DeliveryNote()
                        .setCustomerCode(purchaseCode)
                        .setCustomerName(purchaseName)
                        //送货单号
                        .setDeliveryNo(generateDeliveryNo())
                        //预计到厂时间
                        .setEstimatedTime(new Date())
                        //送货类型
                        .setDeliveryTypeCode("1")
                        //送货状态类型
                        .setDeliveryStatusCode("1")
                        //供应商名字
                        .setSupplierName(supplierName)
                        .setSupplierCode(supplierCode)
//                        .setOrderType(orderType)
                        //送货地址
                        .setDeliveryAddress(itemVos.get(0).getDeliveryAddress());

//todo ******************************
                //批量新建送货单接口
                QueryParam<DeliveryAsPlanParam> deliveryAsPlanParamQueryParam = new QueryParam<>();
                DeliveryAsPlanParam deliveryAsPlanParam = new DeliveryAsPlanParam();
                deliveryAsPlanParamQueryParam.setParam(deliveryAsPlanParam);
                DeliveryPlanDetailItemVo deliveryPlanDetailItemVo = null;
                for (String id : ids) {
                    try {
                        deliveryAsPlanParamQueryParam.getParam().setId(Long.valueOf(id));
                        IPage<DeliveryPlanDetailItemVo> deliveryAsPlanOne = deliveryPlanService.getDeliveryAsPlanList(null, deliveryAsPlanParamQueryParam);
                        deliveryPlanDetailItemVo = deliveryAsPlanOne.getRecords().get(0);
                        log.info("获取到的deliveryAsPlan为：{}", JSON.toJSONString(deliveryPlanDetailItemVo));
                    } catch (Exception e) {
                        log.info("查询送货计划接口出现问题");


                    }


                }
                List<DeliveryDetail> deliveryDetailList = new ArrayList<>();
                DeliveryNoteSaveParam deliveryNoteSaveParam = new DeliveryNoteSaveParam();

                try {
                    DeliveryNoteVo deliveryNoteVo = saveDeliveryNote(deliveryNoteSaveParam);
                } catch (Exception e) {
                    log.info("批量创建出现异常，{}");
                }


//todo ******************************
                //保存送货单
                if (save(note)) {
                    AtomicReference<Integer> itemNo = new AtomicReference<>(1);
                    itemVos.forEach(itemVo -> {
                        //计划明细项次与送货单建立联系
                        DeliveryPlanDetailItem deliveryPlanDetailItem = new DeliveryPlanDetailItem();
                        deliveryPlanDetailItem
                                .setDeliveryNoteId(note.getId())
                                // .setDeliveryNoteNo(note.getDeliveryNo())
                                // .setDeliveryStatus(note.getDeliveryStatusCode())
                                .setId(itemVo.getDeliveryPlanDetailItemId());
                        deliveryPlanDetailItemService.updateById(deliveryPlanDetailItem);

                        //新建送货明细,关联送货单
                        DeliveryDetail deliveryDetail = new DeliveryDetail()
                                .setDeliveryId(note.getId())
                                .setPlanNo(itemVo.getDeliveryPlanNo())
                                .setPlanDetailItemSourceId(itemVo.getPlanDetailItemSourceId())
                                .setProductCode(itemVo.getProductCode())
                                .setProductName(itemVo.getProductName())
                                .setProductSpecs(itemVo.getProductSpecifications())
                                .setPlanQuantity(new BigDecimal(itemVo.getDeliveryQuantity()))
                                .setDeliveryUnitName(itemVo.getPlanUnit())
                                .setWarehouse(itemVo.getWarehousing())
                                .setCurrency(itemVo.getCurrency())
                                .setMaterialClassification(itemVo.getMaterialClassification())
                                .setBillNo(itemVo.getBillNo())
                                .setReceiptQuantity(new BigDecimal(itemVo.getDeliveryQuantity()))
                                .setItemNo(itemNo.get())
                                .setDeliveryPlanDetailItemId(itemVo.getDeliveryPlanDetailItemId());
                        deliveryDetailService.save(deliveryDetail);
                        itemNo.set(itemNo.get() + 1);
                    });
                    return note.getId();
                }
            }

        }
        return null;
    }


    /**
     * 确认发货
     *
     * @param id
     */
    @Override
    public void updateStatus(Long id) {
        log.info("========================================================");
        log.info("确认发货开始，数据为：{}", JSON.toJSONString(id));
        log.info("========================================================");


        //确认发货修改送货单状态的同时记录操作
        DeliveryNote deliveryNote = getById(id);//通过id查询送货单
        LambdaQueryWrapper<DeliveryDetail> detailLambdaQueryWrapper = Wrappers.lambdaQuery();
        LambdaQueryWrapper<DeliveryDetail> eq = detailLambdaQueryWrapper.eq(DeliveryDetail::getDeliveryId, id);
        List<DeliveryDetail> deliveryDetailList = deliveryDetailService.list(eq);//通过送货单id查询送货单详情

        DeliveryOperationLog deliveryOperationLog = new DeliveryOperationLog();
        deliveryOperationLog.setBillId(deliveryNote.getId());

//        LoginInfo infoTest = new LoginInfo();
//        infoTest.setId(new Long(1));
//        infoTest.setName("ces");
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
        deliveryOperationLog.setOperatorNo(deliveryNote.getDeliveryNo());
        deliveryOperationLog.setOperatorName(deliveryNote.getCreatedByName());
        deliveryOperationLog.setOperatorContent("发送送货单");
        deliveryOperationLog.setBillId(id);

        deliveryOperationLogService.save(deliveryOperationLog);
        //确认发货的同时发送消息，通知采购商,发送消息通知
        //角色待配置*******
        deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.TO_BE_SIGNED.getCode());
        //updateById(deliveryNote);
        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteEvent.setBusinessId(deliveryNote.getId());
        deliveryNoteEvent.setDeliveryId(deliveryNote.getId());
        //发送消息通知，创建钉钉待办
        log.info("送货单发出通知事件,送货数据{}", JSON.toJSONString(deliveryNoteEvent));
        DeliveryNoteConfirmEvent deliveryNoteConfirmEvent = new DeliveryNoteConfirmEvent(this, loginInfo, deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryNoteConfirmEvent);
        //角色待配置*******

        //***************************
        //因为是发送，所以同时调用bis接口创建收料通知单
        //调用bis远程接口
        PurchaseReceiveBillCreateOrderDto purchaseReceiveBillCreateOrderDto = new PurchaseReceiveBillCreateOrderDto();
        purchaseReceiveBillCreateOrderDto.setSrmId(deliveryNote.getId());

        Number OrgIdNumber = new Number();
        OrgIdNumber.setFNumber(String.valueOf(deliveryNote.getCustomerCode()));
        purchaseReceiveBillCreateOrderDto.setFPurOrgId(OrgIdNumber);

        Number SupplierCodeNumber = new Number();
        SupplierCodeNumber.setFNumber(String.valueOf(deliveryNote.getSupplierCode()));
        purchaseReceiveBillCreateOrderDto.setFSupplierId(SupplierCodeNumber);
        //货主，使用的也是供应商的编码
        purchaseReceiveBillCreateOrderDto.setFOwnerIdHead(SupplierCodeNumber);

        //设置预计到厂时间
        if (deliveryNote.getEstimatedTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String str = sdf.format(deliveryNote.getEstimatedTime());
            purchaseReceiveBillCreateOrderDto.setFDate(str);
        }


        //物料
        String materialClassification = deliveryDetailList.get(0).getMaterialClassification();
        if (materialClassification != null) {
            Number MaterialClassificationNumber = new Number();
            MaterialClassificationNumber.setFNumber(materialClassification);
            purchaseReceiveBillCreateOrderDto.setFMsWlfl(MaterialClassificationNumber);
        }

        //送货计划的明细字段
        List<PurchaseReceiveBillCreateOrderDto.FDetailEntity> fDetailEntities = new ArrayList<>();
        for (DeliveryDetail detail : deliveryDetailList) {
            PurchaseReceiveBillCreateOrderDto.FDetailEntity fDetailEntity = new PurchaseReceiveBillCreateOrderDto.FDetailEntity();


            //单价
            if (detail.getProductUnitPrice() == null) {
                log.info("单价为空，{}", JSON.toJSONString(deliveryDetailList));
                throw new RuntimeException("单价为空");
            }
            if (detail.getProductUnitPrice() != null) {
                fDetailEntity.setFPrice(detail.getProductUnitPrice());
            }

            //含税单价
            if (detail.getTaxUnitPrice() == null) {
                log.info("含税单价为空，{}", JSON.toJSONString(deliveryDetailList));
                throw new RuntimeException("单价为空");
            }
            if (detail.getTaxUnitPrice() != null) {
                fDetailEntity.setFTaxPrice(detail.getTaxUnitPrice());
            }


            //预计预计到货日期

            if (deliveryNote.getEstimatedTime() != null) {
                SimpleDateFormat sdfForFPreDeliveryDatePattern = new SimpleDateFormat("yyyy-MM-dd");
                String strForFPreDeliveryDatePattern = sdfForFPreDeliveryDatePattern.format(deliveryNote.getEstimatedTime());
                fDetailEntity.setFPreDeliveryDate(strForFPreDeliveryDatePattern);
            }


            if (detail.getErpId() != null) {
                fDetailEntity.setFEntryId(detail.getErpId());
            }

            //物料编码
            if (detail.getProductCode() != null) {
                Number productCodeNumber = new Number();
                productCodeNumber.setFNumber(detail.getProductCode());
                fDetailEntity.setFMaterialId(productCodeNumber);
            }

            //收料单位和计价单位 ： 对应planUnit
//            if (detail.getPlanUnit() != null) {
            Number planUnitNumber = new Number();
            planUnitNumber.setFNumber(detail.getDeliveryUnitName());
            //暂时的解决方法，设置pcs
//                planUnitNumber.setFNumber("pcs");
            fDetailEntity.setFUnitId(planUnitNumber);
            fDetailEntity.setFPriceUnitId(planUnitNumber);
            fDetailEntity.setFStockUnitId(planUnitNumber);
//            }

            //交货数量
            if (detail.getRealDeliveryQuantity() != null) {
                fDetailEntity.setFActReceiveQty(detail.getRealDeliveryQuantity().toString());
            }
            //供应商交货数量
            if (detail.getPlanQuantity() != null) {
                fDetailEntity.setFsupdelqty(detail.getPlanQuantity().toString());
            }
            //仓库
            if (detail.getWarehouse() != null) {
                Number warehouseNumber = new Number();
                warehouseNumber.setFNumber(detail.getWarehouse());
                fDetailEntity.setFStockId(warehouseNumber);
            }
            //计价基本数量  =实际送货数量
            if (detail.getRealDeliveryQuantity() != null) {
                fDetailEntity.setFPriceBaseQty(detail.getRealDeliveryQuantity().toString());
                //库存单位数量=实际送货数量
                fDetailEntity.setFStockQty(detail.getRealDeliveryQuantity().toString());
                //实到数量:  可以不传
                fDetailEntity.setFActlandQty(detail.getRealDeliveryQuantity().toString());
            }
            //明细的id
            if (detail.getId() != null) {
                fDetailEntity.setSrmId(detail.getId());
            }

            // 添加关联关系
            wrapperDetailEntryLink(fDetailEntity, detail);

            // 在美尚测试环境中如果送货单中的采购方为800（美尚（广州）化妆品制造有限公司 ），则需固定传入仓位编码值4000-0001
            developEnvHelper.nonProdIsRun( () -> {
                if( "800".equals( deliveryNote.getCustomerCode()) ){
                    PurchaseReceiveBillCreateOrderDto.FStockLocId FStockLocId = new PurchaseReceiveBillCreateOrderDto.FStockLocId();
                    FStockLocId.setFstocklocidFf100002( new Number( "4000-0001") );
                    fDetailEntity.setStockLocId(FStockLocId);
                }
            } );


            fDetailEntities.add(fDetailEntity);
        }
        purchaseReceiveBillCreateOrderDto.setFDetailEntity(fDetailEntities);

        //单据类型:默认值 SLD01_SYS
        String orderType = deliveryNote.getOrderType();
        if (orderType == null) {
            log.error("没有传单据类型：orderType= ,{}", JSON.toJSONString(orderType));
            throw new RuntimeException("没有单据类型");
        }
        if (orderType != null) {
            if (orderType.equals("CGDD01_SYS")) {
                purchaseReceiveBillCreateOrderDto.setFBillTypeId(new Number(FBillTypeEnum.FBILLTYPE_STANDAR.getCode()));

            } else if (orderType.equals("CGDD02_SYS")) {
                purchaseReceiveBillCreateOrderDto.setFBillTypeId(new Number(FBillTypeEnum.FBILLTYPE_OUTSOURCING.getCode()));
            }
        }


        log.info("purchaseReceiveBillCreateOrderDto字段值：单据类型:{}," + JSON.toJSONString(purchaseReceiveBillCreateOrderDto), purchaseReceiveBillCreateOrderDto.getFBillTypeId());
        R<PurchaseReceiveBillCallCreateVo> purchaseReceiveBillOrder = purchaseReceiveBillOrderFeignClient.createPurchaseReceiveBillOrder(purchaseReceiveBillCreateOrderDto);
        if (!(purchaseReceiveBillOrder.isSuccess())) {
            throw new RuntimeException(purchaseReceiveBillOrder.getMsg());
        }
        log.info("purchaseReceiveBillCreateOrderDto字段值：" + JSON.toJSONString(purchaseReceiveBillCreateOrderDto));

        if (purchaseReceiveBillOrder.getData() != null) {
            Long fId = purchaseReceiveBillOrder.getData().getFId();
            String fNumber = purchaseReceiveBillOrder.getData().getFNumber();
            List<PurchaseReceiveBillEntryCallCreateVo> purchaseReceiveBillEntryCallCreateVoList = purchaseReceiveBillOrder.getData().getPurchaseReceiveBillEntryCallCreateVoList();

            //保存erpId和单据编码
            updateErpId(deliveryNote.getId(), fId, fNumber, ErpSigningStatusEnum.NOT_SIGNED.getCode());
            //保存detail的erpId
            int k = 0;
            for (DeliveryDetail detail : deliveryDetailList) {
                for (PurchaseReceiveBillEntryCallCreateVo purchaseReceiveBillEntryCallCreateVo : purchaseReceiveBillEntryCallCreateVoList) {
                    if (purchaseReceiveBillEntryCallCreateVo.getSrmId().equals(detail.getId())) {
                        detail.setErpId(purchaseReceiveBillEntryCallCreateVo.getErpId());
                        try {
                            ++k;
                            log.info("" + k + "保存的detail为：, {}", JSON.toJSONString(detail));
                            deliveryDetailService.updateById(detail);
                        } catch (Exception e) {
                            log.error("" + k + "保存的detail为：, {}", JSON.toJSONString(detail));
                            throw new RuntimeException("第" + k + "次保存的时候出问题:" + e.getMessage());
                        }
                    }
                }
            }
        }
        //***************************

        //更新送货单状态
        deliveryNoteMapper.updateStatus(id);

        //修改计划明细项次的送货单状态
        LambdaQueryWrapper<DeliveryPlanDetailItem> queryWrapper = new LambdaQueryWrapper<DeliveryPlanDetailItem>()
                .eq(DeliveryPlanDetailItem::getDeliveryNoteId, id);
        List<DeliveryPlanDetailItem> items = deliveryPlanDetailItemService.list(queryWrapper);

        if (!items.isEmpty()) {
            items.forEach(item -> {
                //修改送货单状态
                item.setDeliveryStatus(DeliveryStatusEnum.TO_BE_SIGNED.getCode());
            });
            deliveryPlanDetailItemService.saveOrUpdateBatch(items);
        }
    }

    /**
     * 确认发布（把送货单状态改成待签收的状态）
     *
     * @param deliveryNote
     */
    @Override
//    @Transactional(rollbackFor = Exception.class)
    public DeliveryNoteVo saveAndSign(DeliveryNoteSaveParam deliveryNote) {

        log.info("========================================================");
        log.info("发布开始，数据为：{}", JSON.toJSONString(deliveryNote));
        log.info("========================================================");

        // int c;
        // List<DeliveryDetail> deliveryDetailList1 = deliveryNote.getDeliveryDetailList();
        // for (int i=0;i<deliveryDetailList1.size();i++) {
        //     c=i;
        //     for(int j=0;i<deliveryDetailList1.size();i++){
        //         if(c!=j){
        //             DeliveryDetail deliveryDetail = deliveryDetailList1.get(c);
        //             DeliveryDetail deliveryDetail1 = deliveryDetailList1.get(j);
        //             if(deliveryDetail.getDeliveryPlanDetailItemId().equals(deliveryDetail1.getDeliveryPlanDetailItemId())){
        //                 throw new RuntimeException("不可以同时选同一条送货计划记录保存");
        //             }
        //         }
        //     }
        // }
        long count = deliveryNote.getDeliveryDetailList().stream().map(DeliveryDetail::getDeliveryPlanDetailItemId).count();
        long disCount = deliveryNote.getDeliveryDetailList().stream().map(DeliveryDetail::getDeliveryPlanDetailItemId).distinct().count();
        if (count != disCount) {
            log.error("一个送货单多次引用了同一个送货明细,送货单号为:{}", JSON.toJSONString(deliveryNote.getDeliveryNo()));
            throw new ApiException(500, "一个送货单不可以多次引用同一个送货明细");
        }


        if (deliveryNote.getId() != null) {
            return updateDeliveryNote(deliveryNote);
        }
        //判断供应商合作状态，冻结和停止合作不能新增
        String supplierCode = SecurityUtils.getLoginInfo().get().getCurrentSupplier().getSupplierCode();
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCode(supplierCode);
        R<SupplierDTO> oneSupplierByCode = supplierFeignClient.findOneSupplierByCode(supplierDTO);
        String relevanceStatus = oneSupplierByCode.getData().getRelevanceStatus();
        try {
            if (!SupplierRelevanceStatusEnum.RELEVANCE.getCode().equals(relevanceStatus)) {
                throw new Exception("供应商合作状态异常");
            }
        } catch (Exception e) {
            return null;
        }

        deliveryNote.setId(null);
        deliveryNote.setDeliveryNo(generateDeliveryNo());
        DELIVERY_APPLICATION_ON_OFF = Boolean.parseBoolean(getParamValue("delivery:delivery_application_on_off"));
        deliveryNote.setDeliveryApplication(DELIVERY_APPLICATION_ON_OFF);
        Optional<LoginInfo> loginInfo = SecurityUtils.getLoginInfo();
        loginInfo.ifPresent(info -> deliveryNote.setCreatedByName(info.getName()));
        if (DELIVERY_APPLICATION_ON_OFF) {
            deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.TO_BE_SIGNED.getCode());
            deliveryNote.setLogisticsStatus(LogisticsStatusEnum.NOT_SHIPPED.getCode());
            deliveryNote.setErpSigningStatus(ErpSigningStatusEnum.NOT_SIGNED.getCode());
        } else {
            deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.TO_BE_SIGNED.getCode());
            deliveryNote.setLogisticsStatus(LogisticsStatusEnum.NOT_SHIPPED.getCode());
            deliveryNote.setErpSigningStatus(ErpSigningStatusEnum.NOT_SIGNED.getCode());
        }

        //获取orgId
        OrgIdQuery orgIdQuery = new OrgIdQuery();
        orgIdQuery.setSupplierCode(deliveryNote.getSupplierCode());
        orgIdQuery.setPurchaseCode(deliveryNote.getCustomerCode());
        R<OrgIdDTO> orgIdDTO = purchaserFeignClient.info(orgIdQuery);

        //如果没有建立关系的供应商和采购商不能建立送货单
        if (orgIdDTO == null || orgIdDTO.getData() == null || orgIdDTO.getData().getPurchaseName() == null) {
            throw new RuntimeException("此采购方没有和供应商建立关系");
        }

        if (orgIdDTO != null && orgIdDTO.getData() != null && orgIdDTO.getData().getOrgId() != null) {
            //设置orgId
            deliveryNote.setOrgId(orgIdDTO.getData().getOrgId());
            //设置客户名字customName
            deliveryNote.setCustomerName(orgIdDTO.getData().getPurchaseName());
        }
        if (orgIdDTO.getData() == null) {
            log.info("此供应商编码和采购编码没有对应的orgId");
        }

        save(deliveryNote);
        Long deliveryNoteId = deliveryNote.getId();
        String deliveryNo = deliveryNote.getDeliveryNo();
        String deliveryStatusCode = deliveryNote.getDeliveryStatusCode();
        // List<DeliveryDetail> deliveryDetailList = deliveryNote.getDeliveryDetailList();
        @Valid @NotEmpty(message = "请添加送货明细") List<DeliveryDetailVo> deliveryDetailList = deliveryNote.getDeliveryDetailList();

        //在送货单保存之前先校验所选择的送货计划的字段
        for (int i = 0; i < deliveryDetailList.size(); i++) {
            for (int j = 0; j < deliveryDetailList.size(); j++) {
                if (!(deliveryDetailList.get(i).getOrderType().equals(deliveryDetailList.get(j).getOrderType()))) {
                    throw new RuntimeException("送货计划订单类型不一致");
                }
                if (!(deliveryDetailList.get(i).getDeliveryUnitName().equals(deliveryDetailList.get(j).getDeliveryUnitName()))) {
                    throw new RuntimeException("送货计划计划单位不一致");
                }
                if (!(deliveryDetailList.get(i).getCurrency().equals(deliveryDetailList.get(j).getCurrency()))) {
                    throw new RuntimeException("送货计划结算币别不一致");
                }
                if (!(deliveryDetailList.get(i).getMaterialClassification().equals(deliveryDetailList.get(j).getMaterialClassification()))) {
                    throw new RuntimeException("送货计划物料分类不一致");
                }
            }

        }


        //保存和关联明细之前先判断配置开关
        //计划送货数量和实际送货数量
        //todo****************

        //更新之前先判断计划数量和实际数量
        Boolean quantityflag = false;

        Boolean normalFlag = false;//正常更新但是数量小于计划送货，调用收料通知单同步开关

        //获取detaillist
//        List<DeliveryDetail> deliveryDetailList = deliveryNote.getDeliveryDetailList();
        for (DeliveryDetail deliveryDetail : deliveryDetailList) {
            if (deliveryDetail.getRealDeliveryQuantity().compareTo(deliveryDetail.getPlanQuantity()) == 1) {
                //实际收获数量比计划收获数量多
                quantityflag = true;
            }
            if (deliveryDetail.getRealDeliveryQuantity().compareTo(deliveryDetail.getPlanQuantity()) == -1) {
                //实际收获数量比计划收获数量多
                normalFlag = true;
            }
        }

        //获取同步配置
        //获取ordId
//        OrgIdQuery orgIdQuery = new OrgIdQuery();
//        orgIdQuery.setSupplierCode(deliveryNote.getSupplierCode());
//        orgIdQuery.setPurchaseCode(deliveryNote.getCustomerCode());
//        R<OrgIdDTO> orgIdDTO = purchaserFeignClient.info(orgIdQuery);
        R<List<CfgParamDTO>> listByCode = cfgParamResourceFeignClient.findListByCode(DeliveryCfgParam.DELIVERY_EXCESS_QUANTITY.getCode());
        if (!(listByCode.isSuccess())) {
            throw new RuntimeException("获取同步配置出现异常");
        }

        //查询出erpId
//        DeliveryNote deliveryNote1 = baseMapper.selectById((Serializable)(deliveryNote.getId()));
        //获取data并且获取CfgParamEntity
        Boolean flag = false;
        BigDecimal value = null;
        List<CfgParamDTO> cfgParamEntityList = listByCode.getData();
        CfgParamDTO paramEntity = cfgParamEntityList.stream().filter(cfgParamEntity -> cfgParamEntity.getOrgId().equals(orgIdDTO.getData().getOrgId())).findFirst().orElse(null);
        if (paramEntity != null) {
//            if(true){
//                String result = "{\"data\":[{\"type\":\"BOOL\",\"value\":false},{\"type\":\"INPUT\",\"value\":\"12\"}]}";
//            JSONObject jsonObject = JSON.parseObject(result);  // result数据源：JSON格式字符串
            JSONObject jsonObject = JSON.parseObject(paramEntity.getValue());  // result数据源：JSON格式字符串
            // 获取值
            JSONArray data = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = JSON.parseObject(data.getString(0));
            flag = jsonObject1.getBoolean("value");
            if (flag == null) {
                throw new RuntimeException("获取同步配置开关出现异常");
            }
            if (flag) {//按钮开启才获取
                JSONObject jsonObject2 = JSON.parseObject(data.getString(1));
                value = jsonObject2.getBigDecimal("value");
            }
        }

        if (!quantityflag) {//数量和计划数量相等直接保存

            //正常执行保存操作

            //调用送货计划服务把送货单送货状态（DeliveryStatusCode）存到送货计划（项次表中）
            //同时方便前端过滤，选中的送货计划不会再次在按计划送货的按钮中显示出来
            //保存成功之后把明细的id保存到计划表中
            for (DeliveryDetail deliveryDetail : deliveryDetailList) {
                DeliveryPlanDetailItem deliveryPlanDetailItem = new DeliveryPlanDetailItem();
                deliveryPlanDetailItem.setDeliveryNoteId(deliveryNoteId);

                //这两个字段先不要，达华说交给送货计划模块处理
//                deliveryPlanDetailItem.setDeliveryNoteNo(deliveryNo);
//                deliveryPlanDetailItem.setDeliveryStatus(deliveryStatusCode);

                deliveryPlanDetailItem.setId(deliveryDetail.getDeliveryPlanDetailItemId());
                deliveryPlanDetailItemService.updateById(deliveryPlanDetailItem);
            }

            //明细保存planunit
            for (DeliveryDetail deliveryDetail : deliveryDetailList) {
                deliveryDetail.setPlanUnit(deliveryNote.getPlanUnit());
            }


            //保存报关资料数据
            deliveryCustomsInformationService.save(DeliveryCustomsInformation.builder()
                    .deliveryId(deliveryNote.getId())
                    .deliveryNo(deliveryNote.getDeliveryNo())
                    .innerOrderNo(deliveryNote.getInnerOrderNo())
                    .build());


            //保存明细信息
            deliveryDetailService.saveDetails(deliveryNote.getId(), deliveryDetailList);

            /*if(normalFlag){
                //正常更新的话如果明细的实际收获和计划收货数量不对，这样也要去更新收料通知单的
                if(deliveryNote1.getErpId()!=null){
                    R<PurchaseReceiveBillCallCreateVo> deliveryOfCargoFromStorage = createDeliveryOfCargoFromStorage(deliveryNote, deliveryNote1.getErpId());
                }else{
                    R<PurchaseReceiveBillCallCreateVo> deliveryOfCargoFromStorage = createDeliveryOfCargoFromStorage(deliveryNote, null);
                }
            }*/


        } else {//不相等，检查开关是否打开
            if (!flag) {//开关：关闭
                log.error("1/实际送货数量和收货数量不一致，需要修改实际送货数量，请在采购配置中允许实际送货数量和收货数量不一致,{}", JSON.toJSONString(flag));
                throw new RuntimeException("1/实际送货数量和收货数量不一致，需要修改实际送货数量，请在采购配置中允许实际送货数量和收货数量不一致");
            } else {
                if (value == null) {
                    //开关打开但是数量没有填，当做收货数量和实际数量一致的情况
                    throw new RuntimeException("2/请在在采购配置中的配置数量值，请重新配置");
                } else {
                    //开关关闭但是数量填上了，需要判断
                    for (DeliveryDetail deliveryDetail : deliveryDetailList) {
                        if (deliveryDetail.getPlanQuantity().compareTo(deliveryDetail.getRealDeliveryQuantity()) != 0) {
                            BigDecimal onehundred = new BigDecimal("100");
                            BigDecimal present = value.divide(onehundred, 20, BigDecimal.ROUND_HALF_UP);

                            BigDecimal planQuantity = deliveryDetail.getPlanQuantity();
                            BigDecimal realDeliveryQuantity = deliveryDetail.getRealDeliveryQuantity();

                            BigDecimal multiplyPresent = planQuantity.multiply(present);//乘以百分比之后的数量
                            BigDecimal configQuantity = planQuantity.add(multiplyPresent);//开关控制的数量
                            //去掉小数位
                            int i = configQuantity.intValue();
                            BigDecimal configQuantityForNOSmallNum = new BigDecimal(i);


                            if (realDeliveryQuantity.compareTo(configQuantityForNOSmallNum) == 1) {
                                throw new RuntimeException("3/实际送货数量超过计划送货数量在采购配置中的配置值，请重新配置");
                            }
                        }
                    }
                    //判断通过，就可以保存
                    //调用送货计划服务把送货单送货状态（DeliveryStatusCode）存到送货计划（项次表中）
                    //同时方便前端过滤，选中的送货计划不会再次在按计划送货的按钮中显示出来
                    //保存成功之后把明细的id保存到计划表中
                    for (DeliveryDetail deliveryDetail : deliveryDetailList) {
                        DeliveryPlanDetailItem deliveryPlanDetailItem = new DeliveryPlanDetailItem();
                        deliveryPlanDetailItem.setDeliveryNoteId(deliveryNoteId);

                        //这两个字段先不要，达华说交给送货计划模块处理
//                        deliveryPlanDetailItem.setDeliveryNoteNo(deliveryNo);
//                        deliveryPlanDetailItem.setDeliveryStatus(deliveryStatusCode);

                        deliveryPlanDetailItem.setId(deliveryDetail.getDeliveryPlanDetailItemId());
                        deliveryPlanDetailItemService.updateById(deliveryPlanDetailItem);
                    }

                    //明细保存planunit
                    for (DeliveryDetail deliveryDetail : deliveryDetailList) {
                        deliveryDetail.setPlanUnit(deliveryNote.getPlanUnit());
                    }


                    //保存报关资料数据
                    deliveryCustomsInformationService.save(DeliveryCustomsInformation.builder()
                            .deliveryId(deliveryNote.getId())
                            .deliveryNo(deliveryNote.getDeliveryNo())
                            .innerOrderNo(deliveryNote.getInnerOrderNo())
                            .build());


                    //保存明细信息
                    deliveryDetailService.saveDetails(deliveryNote.getId(), deliveryDetailList);

                }
            }
        }
        //保存明细完成
        //todo****************


        //调用送货计划服务把送货单送货状态（DeliveryStatusCode）存到送货计划（项次表中）
        //同时方便前端过滤，选中的送货计划不会再次在按计划送货的按钮中显示出来
        //保存成功之后把明细的id保存到计划表中
        /*for (DeliveryDetail deliveryDetail : deliveryDetailList) {
            DeliveryPlanDetailItem deliveryPlanDetailItem = new DeliveryPlanDetailItem();
            deliveryPlanDetailItem.setDeliveryNoteId(deliveryNoteId);

            deliveryPlanDetailItem.setDeliveryNoteNo(deliveryNo);
            deliveryPlanDetailItem.setDeliveryStatus(deliveryStatusCode);

            deliveryPlanDetailItem.setId(deliveryDetail.getDeliveryPlanDetailItemId());
            deliveryPlanDetailItemService.updateById(deliveryPlanDetailItem);
        }*/

        //明细保存planunit
        /*for (DeliveryDetail deliveryDetail : deliveryDetailList) {
            deliveryDetail.setPlanUnit(deliveryNote.getPlanUnit());
        }*/


        //保存报关资料数据
        /*deliveryCustomsInformationService.save(DeliveryCustomsInformation.builder()
                .deliveryId(deliveryNote.getId())
                .deliveryNo(deliveryNote.getDeliveryNo())
                .innerOrderNo(deliveryNote.getInnerOrderNo())
                .build());*/

        //保存明细信息
//        deliveryDetailService.saveDetails(deliveryNote.getId(), deliveryDetailList);


        //保存送货单的同时保存附件
        List<FileInfo> attachmentList = deliveryNote.getAttachmentList();
        attachmentRelService.clearAndSave(deliveryNoteId, attachmentList);


        R<PurchaseReceiveBillCallCreateVo> purchaseReceiveBillOrder = createDeliveryOfCargoFromStorage(deliveryNote, null);


        if (purchaseReceiveBillOrder.getData() != null) {
            Long fId = purchaseReceiveBillOrder.getData().getFId();
            String fNumber = purchaseReceiveBillOrder.getData().getFNumber();
            List<PurchaseReceiveBillEntryCallCreateVo> purchaseReceiveBillEntryCallCreateVoList = purchaseReceiveBillOrder.getData().getPurchaseReceiveBillEntryCallCreateVoList();

            //保存erpId和单据编码
            updateErpId(deliveryNote.getId(), fId, fNumber, ErpSigningStatusEnum.NOT_SIGNED.getCode());
            //保存detail的erpId
            int k = 0;
            for (DeliveryDetail detail : deliveryDetailList) {

                for (PurchaseReceiveBillEntryCallCreateVo purchaseReceiveBillEntryCallCreateVo : purchaseReceiveBillEntryCallCreateVoList) {
                    if (purchaseReceiveBillEntryCallCreateVo.getSrmId().equals(detail.getId())) {
                        detail.setErpId(purchaseReceiveBillEntryCallCreateVo.getErpId());

                        try {
                            ++k;
                            log.info("" + k + "保存的detail为：, {}", JSON.toJSONString(detail));
                            deliveryDetailService.updateById(detail);
                        } catch (Exception e) {
                            log.error("" + k + "保存的detail为：, {}", JSON.toJSONString(detail));
                            throw new RuntimeException("第" + k + "次保存的时候出问题:" + e.getMessage());
                        }


                    }
                }
            }
        }

        //记录发布操作记录
        DeliveryOperationLog deliveryOperationLog = new DeliveryOperationLog();
        deliveryOperationLog.setBillId(deliveryNote.getId());
        deliveryOperationLog.setOperatorNo(deliveryNote.getDeliveryNo());
        deliveryOperationLog.setOperatorName(deliveryNote.getCreatedByName());
        deliveryOperationLog.setOperatorContent("发布送货单");
        deliveryOperationLogService.save(deliveryOperationLog);


        //确认发货的同时发送消息，通知采购商,发送消息通知
        //角色待配置*******
        deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.TO_BE_SIGNED.getCode());
        //updateById(deliveryNote);
        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteEvent.setBusinessId(deliveryNote.getId());
        deliveryNoteEvent.setDeliveryId(deliveryNote.getId());
        //发送消息通知，创建钉钉待办
        log.info("送货单发出通知事件,送货数据{}", JSON.toJSONString(deliveryNoteEvent));
        DeliveryNoteConfirmEvent deliveryNoteConfirmEvent = new DeliveryNoteConfirmEvent(this, loginInfo.get(), deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryNoteConfirmEvent);
        //角色待配置*******


        DeliveryNoteVo deliveryNoteVo = BeanUtil.copy(deliveryNote, DeliveryNoteVo.class);
        deliveryNoteVo.setDeliveryDetailList(deliveryDetailList);
        return deliveryNoteVo;


    }


    @Override
    public void updateDeliveryNoteV2(Long id) {

        deliveryNoteMapper.updateSync(DeliveryStatusEnum.COMPLETED.getCode(),
                ErpSigningStatusEnum.SIGNED.getCode(),
                new Date(),
                id);

    }


    /**
     * 更新送货单信息（集成update和updateStatus的方法）
     *
     * @param deliveryNote
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeliveryNoteVo updateSign(DeliveryNoteSaveParam deliveryNote) {
        log.info("========================================================");
        log.info("修改后发布开始，数据为：{}", JSON.toJSONString(deliveryNote));
        log.info("========================================================");


        //判断供应商合作状态，冻结和停止合作不能新增
        R<PurchaseReceiveBillCallCreateVo> deliveryOfCargoFromStorage = null;
        String supplierCode = SecurityUtils.getLoginInfo().get().getCurrentSupplier().getSupplierCode();
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCode(supplierCode);
        R<SupplierDTO> oneSupplierByCode = supplierFeignClient.findOneSupplierByCode(supplierDTO);
        String relevanceStatus = oneSupplierByCode.getData().getRelevanceStatus();
        System.out.println(relevanceStatus);
        try {
            if (!(SupplierRelevanceStatusEnum.RELEVANCE.getCode().equals(relevanceStatus) || SupplierRelevanceStatusEnum.SUSPEND_COOPERATION.getCode().equals(relevanceStatus))) {
                throw new Exception("供应商合作状态异常");
            }
        } catch (Exception e) {
            return null;
        }
        Long id = deliveryNote.getId();
        if (id == null) {
            throw new BizException(DeliveryResultCode.PARAM_ERROR);
        }

        //更新之前先判断计划数量和实际数量
        Boolean quantityflag = false;

        Boolean normalFlag = false;//正常更新但是数量小于计划送货，调用收料通知单同步开关

        //获取detaillist
        // List<DeliveryDetail> deliveryDetailList = deliveryNote.getDeliveryDetailList();
        List<DeliveryDetailVo> deliveryDetailList = deliveryNote.getDeliveryDetailList();
        for (DeliveryDetail deliveryDetail : deliveryDetailList) {
            if (deliveryDetail.getRealDeliveryQuantity().compareTo(deliveryDetail.getPlanQuantity()) > 0) {
                //实际收获数量比计划收获数量多
                quantityflag = true;
            }
            if (deliveryDetail.getRealDeliveryQuantity().compareTo(deliveryDetail.getPlanQuantity()) != 0) {
                //实际收获数量比计划收获数量少了
                normalFlag = true;
            }
        }

        //获取同步配置
        //获取ordId
        OrgIdQuery orgIdQuery = new OrgIdQuery();
        orgIdQuery.setSupplierCode(deliveryNote.getSupplierCode());
        orgIdQuery.setPurchaseCode(deliveryNote.getCustomerCode());
        R<OrgIdDTO> orgIdDTO = purchaserFeignClient.info(orgIdQuery);
        R<List<CfgParamDTO>> listByCode = cfgParamResourceFeignClient.findListByCode(DeliveryCfgParam.DELIVERY_EXCESS_QUANTITY.getCode());
        if (!(listByCode.isSuccess())) {
            throw new RuntimeException("获取同步配置出现异常");
        }

        //查询出erpId
        DeliveryNote deliveryNote1 = baseMapper.selectById(deliveryNote.getId());
        log.info("-------------------erpId----------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("erpId:,{}", JSON.toJSONString(deliveryNote1));
        log.info("erpId:,{}", JSON.toJSONString(deliveryNote1.getErpId()));
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        log.info("-----------------------------------------------");
        //获取data并且获取CfgParamEntity
        Boolean flag = false;
        BigDecimal value = null;
        List<CfgParamDTO> cfgParamDTOList = listByCode.getData();
        CfgParamDTO paramEntity = cfgParamDTOList.stream().filter(cfgParamDTO -> cfgParamDTO.getOrgId().equals(orgIdDTO.getData().getOrgId())).findFirst().orElse(null);
        if (paramEntity != null) {
//        if(true){
//            String result = "{\"data\":[{\"type\":\"BOOL\",\"value\":false},{\"type\":\"INPUT\",\"value\":\"12\"}]}";
//            JSONObject jsonObject = JSON.parseObject(result);  // result数据源：JSON格式字符串
            JSONObject jsonObject = JSON.parseObject(paramEntity.getValue());  // result数据源：JSON格式字符串
            // 获取值
            JSONArray data = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = JSON.parseObject(data.getString(0));
            flag = jsonObject1.getBoolean("value");
            if (flag == null) {
                throw new RuntimeException("获取同步配置开关出现异常");
            }
            if (flag) {//按钮开启才获取
                JSONObject jsonObject2 = JSON.parseObject(data.getString(1));
                value = jsonObject2.getBigDecimal("value");
            }
        }

        if (!quantityflag) {//数量和计划数量相等直接保存
            //正常执行更新操作
            updateById(deliveryNote);
            //更新后更新状态id
            deliveryNoteMapper.updateStatus(deliveryNote.getId());
            //删除基础信息附件，重新保存
            attachmentRelService.clearAndSave(deliveryNote.getId(), deliveryNote.getAttachmentList());
            //删除明细，重新保存
            QueryWrapper<DeliveryDetail> queryWrapper = Wrappers.query();
            queryWrapper.lambda().eq(DeliveryDetail::getDeliveryId, deliveryNote.getId());
            deliveryDetailService.remove(queryWrapper);
            deliveryDetailService.saveDetails(deliveryNote.getId(), deliveryDetailList);


            log.info("-----------------------------------------------");
            log.info("TO_BE_SIGNED:,{}", JSON.toJSONString(deliveryNote1.getDeliveryStatusCode()));
            log.info("normalFlag:,{}", JSON.toJSONString(normalFlag));
            log.info("-----------------------------------------------");
            //正常更新的话如果明细的实际收获和计划收货数量不对，这样也要去更新收料通知单的
            deliveryOfCargoFromStorage = createDeliveryOfCargoFromStorage(deliveryNote, null);

        } else {//不相等，检查开关是否打开
            if (!flag) {//开关：关闭
                log.error("1/实际送货数量和收货数量不一致，需要修改实际送货数量，请在采购配置中允许实际送货数量和收货数量不一致,{}", JSON.toJSONString(flag));
                throw new RuntimeException("1/实际送货数量和收货数量不一致，需要修改实际送货数量，请在采购配置中允许实际送货数量和收货数量不一致");
            } else {
                if (value == null) {
                    //开关打开但是数量没有填，当做收货数量和实际数量一致的情况
                    throw new RuntimeException("2/请在在采购配置中的配置数量值，请重新配置");
                } else {
                    //开关关闭但是数量填上了，需要判断
                    for (DeliveryDetail deliveryDetail : deliveryDetailList) {
                        if (deliveryDetail.getPlanQuantity().compareTo(deliveryDetail.getRealDeliveryQuantity()) != 0) {
                            BigDecimal onehundred = new BigDecimal("100");
                            BigDecimal present = value.divide(onehundred, 20, BigDecimal.ROUND_HALF_UP);

                            BigDecimal planQuantity = deliveryDetail.getPlanQuantity();
                            BigDecimal realDeliveryQuantity = deliveryDetail.getRealDeliveryQuantity();

                            BigDecimal multiplyPresent = planQuantity.multiply(present);//乘以百分比之后的数量
                            BigDecimal configQuantity = planQuantity.add(multiplyPresent);//开关控制的数量
                            //去掉小数位
                            int i = configQuantity.intValue();
                            BigDecimal configQuantityForNOSmallNum = new BigDecimal(i);


                            if (realDeliveryQuantity.compareTo(configQuantityForNOSmallNum) == 1) {
                                throw new RuntimeException("3/实际送货数量超过计划送货数量在采购配置中的配置值，请重新配置");
                            }
                        }
                    }
                    updateById(deliveryNote);
                    //更新签收状态
                    deliveryNoteMapper.updateStatus(deliveryNote.getId());
                    //有附件移除，没有不进去
                    //删除基础信息附件，重新保存
                    if (deliveryNote.getAttachmentList() != null) {
                        attachmentRelService.clearAndSave(deliveryNote.getId(), deliveryNote.getAttachmentList());
                    }
                    //删除明细，重新保存
                    QueryWrapper<DeliveryDetail> queryWrapper = Wrappers.query();
                    queryWrapper.lambda().eq(DeliveryDetail::getDeliveryId, deliveryNote.getId());
                    deliveryDetailService.remove(queryWrapper);
                    deliveryDetailService.saveDetails(deliveryNote.getId(), deliveryDetailList);

                    //数量改变了，所以要同步erp收料通知单的数量
                    //调用bis远程接口
                    //todo***************


                    log.info("-----------------------------------------------");
                    log.info("TO_BE_SIGNED:,{}", JSON.toJSONString(deliveryNote1.getDeliveryStatusCode()));
                    log.info("normalFlag:,{}", JSON.toJSONString(normalFlag));
                    log.info("-----------------------------------------------");
                    deliveryOfCargoFromStorage = createDeliveryOfCargoFromStorage(deliveryNote, null);


                }
            }
        }


        //签收并且更新状态成功后，送货单关联收料通知单
        if (!(deliveryOfCargoFromStorage.isSuccess())) {
            throw new RuntimeException("创建收料通知单出现异常");
        }

        if (deliveryOfCargoFromStorage.getData() != null) {
            Long fId = deliveryOfCargoFromStorage.getData().getFId();
            String fNumber = deliveryOfCargoFromStorage.getData().getFNumber();
            List<PurchaseReceiveBillEntryCallCreateVo> purchaseReceiveBillEntryCallCreateVoList = deliveryOfCargoFromStorage.getData().getPurchaseReceiveBillEntryCallCreateVoList();

            //保存erpId和单据编码
            updateErpId(deliveryNote.getId(), fId, fNumber, ErpSigningStatusEnum.NOT_SIGNED.getCode());
            //保存detail的erpId
            int k = 0;
            for (DeliveryDetail detail : deliveryDetailList) {

                for (PurchaseReceiveBillEntryCallCreateVo purchaseReceiveBillEntryCallCreateVo : purchaseReceiveBillEntryCallCreateVoList) {
                    if (purchaseReceiveBillEntryCallCreateVo.getSrmId().equals(detail.getId())) {
                        detail.setErpId(purchaseReceiveBillEntryCallCreateVo.getErpId());

                        try {
                            ++k;
                            log.info("" + k + "保存的detail为：, {}", JSON.toJSONString(detail));
                            deliveryDetailService.updateById(detail);
                        } catch (Exception e) {
                            log.error("" + k + "保存的detail为：, {}", JSON.toJSONString(detail));
                            throw new RuntimeException("第" + k + "次保存的时候出问题:" + e.getMessage());
                        }
                    }
                }
            }
        }

        //记录保存操作记录
        DeliveryOperationLog deliveryOperationLog = new DeliveryOperationLog();
        deliveryOperationLog.setBillId(deliveryNote.getId());
        deliveryOperationLog.setOperatorNo(deliveryNote.getDeliveryNo());
        deliveryOperationLog.setOperatorName(deliveryNote.getCreatedByName());
        deliveryOperationLog.setOperatorContent("送货单发布记录");
        deliveryOperationLogService.save(deliveryOperationLog);
        //发送消息通知

        deliveryNote.setOrgId(orgIdDTO.getData().getOrgId());
        LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteEvent.setBusinessId(deliveryNote.getId());
        deliveryNoteEvent.setDeliveryId(deliveryNote.getId());
        //发送消息通知，创建钉钉待办
        log.info("送货单编辑通知事件,送货数据{}", JSON.toJSONString(deliveryNoteEvent));
        DeliveryNoteIssueEvent deliveryNoteIssueEvent = new DeliveryNoteIssueEvent(this, loginInfo, deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryNoteIssueEvent);
        DeliveryNoteVo deliveryNoteVo = BeanUtil.copy(deliveryNote, DeliveryNoteVo.class);
        deliveryNoteVo.setDeliveryDetailList(deliveryDetailList);
        return deliveryNoteVo;
    }


    /**
     * 为送货明细设置附件
     *
     * @param vo
     */
    @Override
    public void setDeliveryDetailAttachment(DeliveryDetailVo vo) {
        if (vo == null) return;
        ArrayList<FileInfo> fileInfos = new ArrayList<>();
        List<AttachmentRel> attachmentRelList = attachmentRelService.list(new LambdaQueryWrapper<AttachmentRel>().eq(AttachmentRel::getBusinessFormId, vo.getId()));
        attachmentRelList.forEach(attachmentRel -> {
            FileInfo fileInfo = new FileInfo().setId(attachmentRel.getAttachmentId())
                    .setOriginalFilename(attachmentRel.getAttachmentName())
                    .setUrl(attachmentRel.getAttachmentUrl())
                    .setSize(attachmentRel.getAttachmentSize());
            fileInfos.add(fileInfo);
        });
        vo.setAttachmentList(fileInfos);
    }

    /**
     * 批量设置送货明细附件
     *
     * @param records
     */
    @Override
    public void batchSetDeliveryDetailAttachment(List<DeliveryDetailVo> records) {
        if (CollectionUtil.isNotEmpty(records)) {
            records.forEach(this::setDeliveryDetailAttachment);
        }
    }

    /**
     * 导出
     *
     * @param queryParam
     * @return
     */
    @Override
    public List<DeliveryNoteExcelModel> getDeliveryNoteList(QueryParam<DeliveryNoteParam> queryParam) {
        Optional<LoginInfo> loginInfo1 = SecurityUtils.getLoginInfo();
        LoginInfo loginInfo2 = loginInfo1.get();
        Org currentOrg = loginInfo2.getCurrentOrg();
        if (currentOrg == null || currentOrg.getId() == null) {
            throw new RuntimeException("采购商编码有问题");
        }

        DeliveryNoteParam param = queryParam.getParam();
        if (param != null) {
            queryParam.getParam().setOrgId(currentOrg.getId());
            if (queryParam.getParam().getDeliveryStatusCode() == null || queryParam.getParam().getDeliveryStatusCode().equals("")) {
                queryParam.getParam().setOrgTrue("true");
            } else if (queryParam.getParam().getDeliveryStatusCode() != null && queryParam.getParam().getDeliveryStatusCode().equals("1")) {
                queryParam.getParam().setOrgTrue("true");
            }


        } else {
            DeliveryNoteParam deliveryNoteParam = new DeliveryNoteParam();

            deliveryNoteParam.setOrgId(currentOrg.getId());
            queryParam.setParam(deliveryNoteParam);
            if (queryParam.getParam().getDeliveryStatusCode() == null || queryParam.getParam().getDeliveryStatusCode().equals("")) {
                queryParam.getParam().setOrgTrue("true");
            } else if (queryParam.getParam().getDeliveryStatusCode() != null && queryParam.getParam().getDeliveryStatusCode().equals("1")) {
                queryParam.getParam().setOrgTrue("true");
            }

        }

        List<DeliveryNote> deliveryNotes = baseMapper.selectListPage(null, queryParam);
        for (DeliveryNote dn : deliveryNotes) {
            if (StringUtil.equals(dn.getTransportMethod(), DictCodeTransportMethodEnum.CAR.getCode())) {
                dn.setTransportMethod(DictCodeTransportMethodEnum.CAR.getMsg());
            } else if (StringUtil.equals(dn.getTransportMethod(), DictCodeTransportMethodEnum.RAILWAY.getCode())) {
                dn.setTransportMethod(DictCodeTransportMethodEnum.RAILWAY.getMsg());
            } else if (StringUtil.equals(dn.getTransportMethod(), DictCodeTransportMethodEnum.PLANE.getCode())) {
                dn.setTransportMethod(DictCodeTransportMethodEnum.PLANE.getMsg());
            } else if (StringUtil.equals(dn.getTransportMethod(), DictCodeTransportMethodEnum.SHIP.getCode())) {
                dn.setTransportMethod(DictCodeTransportMethodEnum.SHIP.getMsg());
            }

            if (StringUtil.equals(dn.getDeliveryMethod(), DictCodeDeliveryMethodEnum.SUPPLIER_DELIVERY.getCode())) {
                dn.setDeliveryMethod(DictCodeDeliveryMethodEnum.SUPPLIER_DELIVERY.getMsg());
            } else if (StringUtil.equals(dn.getDeliveryMethod(), DictCodeDeliveryMethodEnum.EXPRESS.getCode())) {
                dn.setDeliveryMethod(DictCodeDeliveryMethodEnum.EXPRESS.getMsg());
            } else if (StringUtil.equals(dn.getDeliveryMethod(), DictCodeDeliveryMethodEnum.PROVIDED_BY_PURCHASER.getCode())) {
                dn.setDeliveryMethod(DictCodeDeliveryMethodEnum.PROVIDED_BY_PURCHASER.getMsg());
            }

            //erp签收状态字典返回
            //返回签收和未签收
            if (StringUtil.equals(dn.getErpSigningStatus(), ErpSigningStatusEnum.NOT_SIGNED.getCode())) {
                dn.setErpSigningStatus(ErpSigningStatusEnum.NOT_SIGNED.getName());
            } else if (StringUtil.equals(dn.getErpSigningStatus(), ErpSigningStatusEnum.SIGNED.getCode())) {
                dn.setErpSigningStatus(ErpSigningStatusEnum.SIGNED.getName());
            }

            //送货单状态（由code转换成中文）
            setDeliveryStatusByCode(dn);
        }
        //查询入库仓的字典
        R<List<DictItemDTO>> dictItemListR = cfgParamResourceFeignClient.findDictItemListByCode("warehousing_enum");

        Map<String, String> warehouseMap = new HashMap<>();

        if (dictItemListR!=null&&dictItemListR.isSuccess()){
            warehouseMap = dictItemListR.getData().stream().collect(Collectors.toMap(DictItemDTO::getValue, DictItemDTO::getLabel));
        }
        ArrayList<DeliveryNoteExcelModel> excelModels = new ArrayList<>();
        List<DeliveryNoteVo> deliveryNoteVoList = BeanUtil.copy(deliveryNotes, DeliveryNoteVo.class);
        //设置送货明细数据
        Map<String, String> finalWarehouseMap = warehouseMap;
        deliveryNoteVoList.forEach(deliveryNoteVo -> {
            List<DeliveryDetail> detailList = deliveryDetailService.list(new LambdaQueryWrapper<DeliveryDetail>().eq(DeliveryDetail::getDeliveryId, deliveryNoteVo.getId()));
            List<DeliveryDetailVo> detailVoList = BeanUtil.copy(detailList, DeliveryDetailVo.class);
            //设置明细中的COA附件列表
            batchSetDeliveryDetailAttachment(detailVoList);
            //设置 是否有COA附件 属性的值
            detailVoList.forEach(detail -> {
                if (detail != null) {
                    if (detail.getAttachmentList().size() > 0)
                        detail.setAttachment("是");
                    else if (detail.getAttachmentList().size() <= 0)
                        detail.setAttachment("否");
                }
                DeliveryNoteExcelModel excelModel = BeanUtil.copy(detail, DeliveryNoteExcelModel.class);
                BeanUtil.copyProperties(deliveryNoteVo,excelModel);
                //设置入库仓
                excelModel.setWarehouse(finalWarehouseMap.get(excelModel.getWarehouse()));
                excelModels.add(excelModel);
            });
            deliveryNoteVo.setDeliveryDetailList(detailVoList);
        });

        return excelModels;
    }

    /**
     * 送货单状态：根据code返回name
     * @param note
     */
    private void setDeliveryStatusByCode(DeliveryNote note){
        if (note==null) return;
        if (note.getDeliveryStatusCode()==null){
            log.error("存在没有送货状态的送货单，该送货单号：{}",JSON.toJSONString(note.getDeliveryNo()));
            throw new ApiException(ResultCode.FAILURE.getCode(),"业务错误");
        }
        switch (note.getDeliveryStatusCode()){
            case "1":
                note.setDeliveryStatusCode("草稿");
                break;
            case "2":
                note.setDeliveryStatusCode("待发货");
                break;
            case "3":
                note.setDeliveryStatusCode("申请中");
                break;
            case "4":
                note.setDeliveryStatusCode("申请作废");
                break;
            case "5":
                note.setDeliveryStatusCode("申请撤回");
                break;
            case "6":
                note.setDeliveryStatusCode("申请退回");
                break;
            case "7":
                note.setDeliveryStatusCode("已同意");
                break;
            case "8":
                note.setDeliveryStatusCode("部分同意");
                break;
            case "9":
                note.setDeliveryStatusCode("待签收");
                break;
            case "10":
                note.setDeliveryStatusCode("送货撤回");
                break;
            case "11":
                note.setDeliveryStatusCode("送货作废");
                break;
            case "12":
                note.setDeliveryStatusCode("送货退回");
                break;
            case "13":
                note.setDeliveryStatusCode("已完成");
                break;
            case "14":
                note.setDeliveryStatusCode("已冻结");
                break;
            case "15":
                note.setDeliveryStatusCode("签收确认中");
                break;
        }

    }
}
