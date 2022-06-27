package net.bncloud.delivery.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.SneakyThrows;
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
import net.bncloud.common.base.repeatrequest.RepeatRequestOperation;
import net.bncloud.common.bizutil.number.NumberFactory;
import net.bncloud.common.bizutil.number.NumberType;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.helper.DevelopEnvHelper;
import net.bncloud.common.security.*;
import net.bncloud.common.service.base.domain.HandlerMsgParam;
import net.bncloud.common.util.*;
import net.bncloud.delivery.annotation.OperationLog;
import net.bncloud.delivery.config.ImportDetailExcelConfig;
import net.bncloud.delivery.constant.DeliveryDetailExcelImportConstants;
import net.bncloud.delivery.entity.*;
import net.bncloud.delivery.enums.*;
import net.bncloud.delivery.event.*;
import net.bncloud.delivery.param.*;
import net.bncloud.delivery.utils.DeliveryDetailUtils;
import net.bncloud.serivce.api.order.dto.MrpOrderCreatePurchaseOrderBillDTO;
import net.bncloud.serivce.api.order.dto.MrpOrderCreatePurchaseOrderBillReturnParamDTO;
import net.bncloud.serivce.api.order.dto.OrderDetailDTO;
import net.bncloud.serivce.api.order.entity.OrderForDeliveryInfoDTO;
import net.bncloud.serivce.api.order.feign.ZcOrderServiceFeignClient;
import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import net.bncloud.delivery.feign.InformationServiceFeignClient;
import net.bncloud.delivery.mapper.DeliveryNoteMapper;
import net.bncloud.delivery.mapper.DeliveryPlanMapper;
import net.bncloud.delivery.service.*;
import net.bncloud.delivery.service.listener.DeliveryNoteExcelImportDetailListener;
import net.bncloud.delivery.vo.*;
import net.bncloud.delivery.vo.event.DeliveryNoteEvent;
import net.bncloud.enums.EventCode;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.service.api.delivery.dto.DeliveryDetailDTO;
import net.bncloud.service.api.delivery.dto.DeliveryDetailUpdateDTO;
import net.bncloud.service.api.delivery.dto.DeliveryMaterialNoticeDTO;
import net.bncloud.service.api.delivery.dto.DeliveryNoteDTO;
import net.bncloud.service.api.platform.config.ConfigParamOpenFeign;
import net.bncloud.service.api.platform.config.enums.CfgParamKeyEnum;
import net.bncloud.service.api.platform.config.vo.CfgParamInfo;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.service.api.platform.supplier.dto.SupplierDTO;
import net.bncloud.service.api.platform.supplier.enums.SupplierRelevanceStatusEnum;
import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;
import net.bncloud.support.Condition;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 送货单信息表 服务实现类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class DeliveryNoteServiceImplVS extends BaseServiceImpl<DeliveryNoteMapper, DeliveryNote> implements DeliveryNoteServiceVS {

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
    private DeliveryPlanMapper deliveryPlanMapper;

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


    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private Validator validator;
    @Resource
    private ConfigParamOpenFeign configParamOpenFeign;

    @Resource
    private ImportDetailExcelConfig importDetailExcelConfig;
    @Resource
    private NumberFactory numberFactory;
    @Resource
    private ZcOrderServiceFeignClient zcOrderServiceFeignClient;
    @Resource
    private DevelopEnvHelper developEnvHelper;

    /**
     * 根据参数编码，获取参数值
     *
     * @param code 参数编码
     * @return 参数值
     */
    private String getParamValue(String code) {
        R<CfgParamDTO> response = cfgParamResourceFeignClient.getParamByCode(code);
        log.info("根据参数编码，获取参数值,返回结果：{}", JSON.toJSONString(response));
        if (response.isSuccess()) {
            CfgParamDTO cfgParam = response.getData();
            if( cfgParam == null){
                log.warn("配置不存在，code: {}",code);
                return null;
            }
            return cfgParam.getValue();
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteDeliveryNote(Long id) {

        // 1、回滚项次的数据
        DeliveryNote deliveryNote = this.getById(id);
        Asserts.notNull(deliveryNote,"送货单信息已经删除，请刷新后重试！" );

        // 1、先删除明细，并回滚，后计算
        List<DeliveryDetail> oldDeliveryDetailList = deliveryDetailService.lambdaQuery().eq(DeliveryDetail::getDeliveryId, id).list();
        if (CollectionUtil.isNotEmpty( oldDeliveryDetailList)) {
            this.handleOldDeliveryDetailListFallback( oldDeliveryDetailList );
        }

        this.removeById(id);

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
     * 配置开关控制数量方法
     *
     * @return 开关
     */
    public Boolean quanlityControl(List<DeliveryDetail> deliveryDetailList,R<OrgIdDTO> orgIdDTO){
        //todo****************

        //控制保存开关,默认关闭
        Boolean saveFlag = false;
        Boolean remainingflag = false;//正常更新但是数量小于计划送货，调用收料通知单同步开关

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
                //实际收获数量比计划收获数量少
                normalFlag = true;
            }
            if(deliveryDetail.getRealDeliveryQuantity().compareTo(deliveryDetail.getPlanQuantity()) == -1){

            }
        }

        //获取同步配置
        R<List<CfgParamDTO>> listByCode = cfgParamResourceFeignClient.findListByCode(DeliveryCfgParam.DELIVERY_EXCESS_QUANTITY.getCode());
        if (!(listByCode.isSuccess())) {
            throw new RuntimeException("获取同步配置出现异常");
        }

        //获取data并且获取CfgParamEntity
        Boolean flag = false;
        BigDecimal value = null;
        List<CfgParamDTO> cfgParamDTOList = listByCode.getData();
        CfgParamDTO paramEntity = cfgParamDTOList.stream().filter(cfgParamDTO -> cfgParamDTO.getOrgId().equals(orgIdDTO.getData().getOrgId())).findFirst().orElse(null);
        if (paramEntity != null) {
            JSONObject jsonObject = JSON.parseObject(paramEntity.getValue());  // result数据源：JSON格式字符串
            // 获取值
            JSONArray data = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = JSON.parseObject(data.getString(0));
            flag = jsonObject1.getBoolean("value");
            if(flag==null){
                throw new RuntimeException("获取同步配置开关出现异常");
            }
            if(flag){//按钮开启才获取
                JSONObject jsonObject2 = JSON.parseObject(data.getString(1));
                value = jsonObject2.getBigDecimal("value");
            }
        }

        if(!quantityflag) {//数量和计划数量相等直接保存
            //正常执行保存操作
            //可以保存，保存按钮开启
            saveFlag=true;
        }
        else {//不相等，检查开关是否打开
            if (!flag) {//开关：关闭
                log.error("1/实际送货数量和收货数量不一致，需要修改实际送货数量，请在采购配置中允许实际送货数量和收货数量不一致,{}", JSON.toJSONString(flag));
                throw new RuntimeException("1/实际送货数量和收货数量不一致，需要修改实际送货数量，请在采购配置中允许实际送货数量和收货数量不一致");
            }else{
                if(value==null){
                    //开关打开但是数量没有填，当做收货数量和实际数量一致的情况
                    throw new RuntimeException("2/请在在采购配置中的配置数量值，请重新配置");
                }else {
                    //开关关闭但是数量填上了，需要判断
                    for (DeliveryDetail deliveryDetail : deliveryDetailList) {
                        if(deliveryDetail.getPlanQuantity().compareTo(deliveryDetail.getRealDeliveryQuantity())!=0){
                            BigDecimal onehundred = new BigDecimal("100");
                            BigDecimal present = value.divide(onehundred, 20, BigDecimal.ROUND_HALF_UP);

                            BigDecimal planQuantity = deliveryDetail.getPlanQuantity();
                            BigDecimal realDeliveryQuantity = deliveryDetail.getRealDeliveryQuantity();

                            BigDecimal multiplyPresent = planQuantity.multiply(present);//乘以百分比之后的数量
                            BigDecimal configQuantity = planQuantity.add(multiplyPresent);//开关控制的数量
                            //去掉小数位
                            int i = configQuantity.intValue();
                            BigDecimal configQuantityForNOSmallNum = new BigDecimal(i);


                            if(realDeliveryQuantity.compareTo(configQuantityForNOSmallNum)==1){
                                throw new RuntimeException("3/实际送货数量超过计划送货数量在采购配置中的配置值，请重新配置");
                            }
                        }
                    }
                    //可以保存，保存按钮开启
                    saveFlag=true;
                }
            }
        }
        //todo****************
        //保存明细完成
        return saveFlag;
    }
    /**
     * 配置开关控制数量方法V2
     *
     * @return 开关
     */
    public Boolean quanlityControlVS(BigDecimal realQuantity,R<OrgIdDTO> orgIdDTO,BigDecimal quantity){
        //todo****************

        //控制保存开关,默认关闭
        Boolean saveFlag = false;
        Boolean remainingflag = false;//正常更新但是数量小于计划送货，调用收料通知单同步开关

        //更新之前先判断计划数量和实际数量
        Boolean quantityflag = false;
        Boolean normalFlag = false;//正常更新但是数量小于计划送货，调用收料通知单同步开关




        //获取同步配置
        R<List<CfgParamDTO>> listByCode = cfgParamResourceFeignClient.findListByCode(DeliveryCfgParam.DELIVERY_EXCESS_QUANTITY.getCode());
        if (!(listByCode.isSuccess())) {
            throw new RuntimeException("获取同步配置出现异常");
        }

        //获取data并且获取CfgParamEntity
        Boolean flag = false;
        BigDecimal value = null;
        List<CfgParamDTO> cfgParamDTOList = listByCode.getData();
        CfgParamDTO paramEntity = cfgParamDTOList.stream().filter(cfgParamDTO -> cfgParamDTO.getOrgId().equals(orgIdDTO.getData().getOrgId())).findFirst().orElse(null);
        if (paramEntity != null) {
//                        if(true){
//                String result = "{\"data\":[{\"type\":\"BOOL\",\"value\":false},{\"type\":\"INPUT\",\"value\":\"12\"}]}";
//            JSONObject jsonObject = JSON.parseObject(result);  // result数据源：JSON格式字符串
            JSONObject jsonObject = JSON.parseObject(paramEntity.getValue());  // result数据源：JSON格式字符串
            // 获取值
            JSONArray data = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = JSON.parseObject(data.getString(0));
            flag = jsonObject1.getBoolean("value");
            if(flag==null){
                throw new RuntimeException("获取同步配置开关出现异常");
            }
            if(flag){//按钮开启才获取
                JSONObject jsonObject2 = JSON.parseObject(data.getString(1));
                value = jsonObject2.getBigDecimal("value");
            }
        }




            if (!flag) {//开关：关闭
                log.error("1/实际送货数量和收货数量不一致，需要修改实际送货数量，请在采购配置中允许实际送货数量和收货数量不一致,开关状态为关闭：{}", JSON.toJSONString(flag));
                throw new RuntimeException("1/采购配置开关状态为关闭，实际送货数量和收货数量不一致，需要修改实际送货数量，请在采购配置中允许实际送货数量和收货数量不一致");
            }else{//打开
                if(value==null){
                    //开关打开但是数量没有填，当做收货数量和实际数量一致的情况
                    throw new RuntimeException("2/采购配置开关打开，但还没设置数量值，请重新配置");
                }else {
                    //开关关闭但是数量填上了，需要计算
                    //if(quantity.compareTo(deliveryDetail.getRealDeliveryQuantity())!=0){

                            BigDecimal onehundred = new BigDecimal("100");
                            BigDecimal present = value.divide(onehundred, 20, BigDecimal.ROUND_HALF_UP);

                            BigDecimal realDeliveryQuantity = realQuantity;

                            BigDecimal multiplyPresent = quantity.multiply(present);//乘以百分比之后的数量
                            BigDecimal configQuantity = quantity.add(multiplyPresent);//开关控制的数量
                            //去掉小数位
                            int i = configQuantity.intValue();
                            BigDecimal configQuantityForNOSmallNum = new BigDecimal(i);

                            if(realDeliveryQuantity.compareTo(configQuantityForNOSmallNum)==1){
                                throw new RuntimeException("3/实际送货数量超过计划送货数量在采购配置中的配置值，请重新配置");
                            }
                        //}

                    //可以保存，保存按钮开启
                    saveFlag=true;
                }
            }



//        }
        //todo****************
        //保存明细完成
        return saveFlag;
    }


    /**
     * 设值have_attachment
     * @param note
     */
    private void setHaveAttachmentField(DeliveryNoteSaveParam note) {
        if (note == null) return;
        List<DeliveryDetailVo> detailList = note.getDeliveryDetailList();
        //送货明细的数量
        int detailLen = detailList.size();
        //存在附件的明细数
        AtomicInteger count = new AtomicInteger();
        if (CollectionUtil.isNotEmpty(detailList)) {
            detailList.forEach(detail -> {
                Optional.ofNullable(detail.getAttachmentList()).ifPresent(attachmentList -> {
                    if (attachmentList.size() > 0) {
                        count.getAndIncrement();
                    }
                });
            });
        }
        if (detailLen == 0 || count.get() == 0) {  //0表示没有送货明细 或明细中都没有附件
            note.setHaveAttachment(0);
        } else if (count.get() == detailLen) {  //2表示都有附件
            note.setHaveAttachment(2);
        } else if (count.get() < detailLen) { //1表示部分有，部分没有
            note.setHaveAttachment(1);
        }
    }

    /**
     * 保存送货单信息
     *
     * @param deliveryNoteSaveParam 保存送货单请求参数
     * @return 送货单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @RepeatRequestOperation
    public DeliveryNoteVo saveDeliveryNote(DeliveryNoteSaveParam deliveryNoteSaveParam) {

        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        //获取当前登录供应商编码
        String supplierCode = getLoginCurrentSupplierCode();

        // 校验供应商合作状态
        this.verifySupplierCooperationStatus( supplierCode );

        // 查询组织ID
        OrgIdDTO orgIdDto = this.getOrgIdDto(supplierCode, deliveryNoteSaveParam.getCustomerCode());
        this.saveDeliveryNoteInfo(deliveryNoteSaveParam, loginInfo, orgIdDto,MrpDeliveryNoteTypeEnum.NORMAL_DELIVERY.getCode());

        // 获取要保存的参数明细
        List<DeliveryDetailVo> deliveryDetailParamList = deliveryNoteSaveParam.getDeliveryDetailList();

        // 保存送货明细
        this.saveDeliveryDetailList( deliveryNoteSaveParam,orgIdDto.getOrgId(),deliveryDetailParamList );

        //记录保存操作记录
        this.saveOperationRecord(deliveryNoteSaveParam, "创建送货单");

        DeliveryNoteVo deliveryNoteVo = BeanUtil.copy(deliveryNoteSaveParam, DeliveryNoteVo.class);
        deliveryNoteVo.setDeliveryDetailList(deliveryDetailParamList);
        return deliveryNoteVo;
    }

    /**
     * 保存送货单信息 （包含附件处理）
     * @param deliveryNoteSaveParam
     * @param loginInfo
     * @param orgIdDto
     */
    private void saveDeliveryNoteInfo(DeliveryNoteSaveParam deliveryNoteSaveParam, LoginInfo loginInfo, OrgIdDTO orgIdDto,String isMrpCode) {
        // 处理送货单
        deliveryNoteSaveParam.setDeliveryNo(numberFactory.buildNumber(NumberType.ship));
        deliveryNoteSaveParam.setMrpStatus(isMrpCode);


        // 送货申请开启状态 false 关闭，true 开启
        boolean deliveryApplicationOnOff = Boolean.parseBoolean( Optional.ofNullable( this.getParamValue("delivery:delivery_application_on_off") ).orElse("false") );
        deliveryNoteSaveParam.setDeliveryApplication( deliveryApplicationOnOff );
        deliveryNoteSaveParam.setCreatedByName( loginInfo.getName() );
        // 处理送货单状态
        if (deliveryApplicationOnOff) {
            deliveryNoteSaveParam.setDeliveryStatusCode(DeliveryStatusEnum.DRAFT.getCode());
            deliveryNoteSaveParam.setLogisticsStatus(LogisticsStatusEnum.NOT_SHIPPED.getCode());
            deliveryNoteSaveParam.setErpSigningStatus(ErpSigningStatusEnum.NOT_SIGNED.getCode());
        }
        else {
            deliveryNoteSaveParam.setDeliveryStatusCode(DeliveryStatusEnum.DRAFT.getCode());
            deliveryNoteSaveParam.setLogisticsStatus(LogisticsStatusEnum.NOT_SHIPPED.getCode());
            deliveryNoteSaveParam.setErpSigningStatus(ErpSigningStatusEnum.NOT_SIGNED.getCode());
        }
        //设置orgId
        deliveryNoteSaveParam.setOrgId( orgIdDto.getOrgId() );
        //设置客户名字customName
        deliveryNoteSaveParam.setCustomerName( orgIdDto.getPurchaseName());

        //设值have_attachment成员
        this.setHaveAttachmentField( deliveryNoteSaveParam);
        // 保存送货单
        Asserts.isTrue( this.save( deliveryNoteSaveParam ),"保存成功！" ) ;

        Long deliveryNoteId = deliveryNoteSaveParam.getId();
        //保存送货单的同时保存附件
        attachmentRelService.clearAndSave( deliveryNoteId, deliveryNoteSaveParam.getAttachmentList() );
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
        fDetailEntityLink.setFDetailEntity_Link_FSBillId( Long.parseLong( deliveryPlan.getSourceId()) );
        // 设置源 采购订单的明细的ID
        fDetailEntityLink.setFDetailEntity_Link_FSId( Long.parseLong( deliveryPlanDetail.getSourceId()) );
        // 设置源 采购订单的明细的 交货量
        String planQuantity = deliveryPlanDetail.getPlanQuantity();
        fDetailEntityLink.setFDetailEntity_Link_FBaseUnitQtyOld( planQuantity );
        // 设置 此次交货量
        fDetailEntityLink.setFDetailEntity_Link_FBaseUnitQty( fDetailEntity.getFActReceiveQty() );

        // 设置 采购单号
        fDetailEntity.setFSrcBillNo( deliveryPlan.getBillNo() );

        fDetailEntity.setFDetailEntityLinkList( Collections.singletonList( fDetailEntityLink ) );
    }

    /**
     * 包装Mrp的送货单明细的关联关系信息
     *
     * @param fDetailEntity
     */
    private void wrapperMrpDetailEntryLink(PurchaseReceiveBillCreateOrderDto.FDetailEntity fDetailEntity,OrderForDeliveryInfoDTO deliveryPlan,OrderDetailDTO deliveryPlanDetail) {

        // rao -- 添加关联关系信息
        PurchaseReceiveBillCreateOrderDto.FDetailEntityLink fDetailEntityLink = new PurchaseReceiveBillCreateOrderDto.FDetailEntityLink();
        // 设置源 采购订单FID
        fDetailEntityLink.setFDetailEntity_Link_FSBillId( Long.parseLong( deliveryPlan.getSourceId()) );
        // 设置源 采购订单的明细的ID
        fDetailEntityLink.setFDetailEntity_Link_FSId( Long.parseLong( deliveryPlanDetail.getSourceId()) );
        // 设置源 采购订单的明细的 交货量
        String planQuantity = deliveryPlanDetail.getDeliveryQuantity();
        fDetailEntityLink.setFDetailEntity_Link_FBaseUnitQtyOld( planQuantity );
        // 设置 此次交货量
        fDetailEntityLink.setFDetailEntity_Link_FBaseUnitQty( fDetailEntity.getFActReceiveQty() );

        // 设置 采购单号
        fDetailEntity.setFSrcBillNo( deliveryPlan.getPurchaseOrderCode() );

        fDetailEntity.setFDetailEntityLinkList( Collections.singletonList( fDetailEntityLink ) );
    }



    /**
     * 更新收料通知单回传的erpId
     *
     * @param
     * @return
     */
    @Override
    public void updateErpId(Long id, Long erpId, String fNumber,String code) {
        deliveryNoteMapper.updateErpId(id, erpId, fNumber,code);
    }

    /**
     * 更新送货单信息
     *
     * @param deliveryNoteSaveParam
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RepeatRequestOperation
    public DeliveryNoteVo updateDeliveryNote(DeliveryNoteSaveParam deliveryNoteSaveParam) {

        log.info("========================================================");
        log.info("修改开始，数据为：{}",JSON.toJSONString(deliveryNoteSaveParam));
        log.info("========================================================");

        //获取当前登录供应商编码
        String supplierCode = getLoginCurrentSupplierCode();

        // 校验供应商合作状态
        this.verifySupplierCooperationStatus(supplierCode);

        Long deliveryNoteId = deliveryNoteSaveParam.getId();
        Asserts.notNull( deliveryNoteId ,"未指定送货单！" );

        // 查询组织ID
        Long orgId = this.remoteGetOrgId(supplierCode, deliveryNoteSaveParam.getCustomerCode() );

        //查询查询出此记录在数据库中没有更改前的数据，如果有erp，同时查询出erpId
        // 送货单信息
        DeliveryNote deliveryNote = this.getById( deliveryNoteSaveParam.getId() );
        Asserts.notNull( deliveryNote,"送货单信息获取失败！" );
        log.info("-------------------erpId----------------------------");
        log.info("deliveryNote info:{}",JSON.toJSONString(deliveryNote));
        log.info("-----------------------------------------------");


        //获取前端传入的送货明细信息
        List<DeliveryDetailVo> deliveryDetailParamList = deliveryNoteSaveParam.getDeliveryDetailList();
        log.info("[updateDeliveryNote] deliveryDetailParamList:{}", JSON.toJSONString( deliveryDetailParamList) );

        // 更新送货单的信息
        this.updateDeliveryNoteInfo(deliveryNoteSaveParam);

        // 待签收状态下可以更新送货明细信息
        // 1、先删除明细，并回滚，后计算
        List<DeliveryDetail> oldDeliveryDetailList = deliveryDetailService.lambdaQuery().eq(DeliveryDetail::getDeliveryId, deliveryNoteId).list();

        // 送货单是待签收状态
        boolean deliveryNoteIsToBeSigned = DeliveryStatusEnum.TO_BE_SIGNED.getCode().equals( deliveryNote.getDeliveryStatusCode());
        if( deliveryNoteIsToBeSigned && CollectionUtil.isEmpty( deliveryDetailParamList ) ){
            throw new ApiException("待签收状态下送货单明细不能为空！");
        }

        // 如果删除了明细或实际发货数量才进行删除后新增，否则直接走数据更新的逻辑
        if ( this.checkDeliveryDetailHaveUpdateRealDeliveryQuantity( oldDeliveryDetailList, deliveryDetailParamList )) {

            log.info("[updateDeliveryNote] 删除了明细或实际发货数量或明细为空或为新增 >> ");

            // 处理已经存在的送货明细  回滚
            this.handleOldDeliveryDetailListFallback(oldDeliveryDetailList);

            // 保存送货明细
            this.saveDeliveryDetailList( deliveryNoteSaveParam, orgId, deliveryDetailParamList );

        }
        // 直接走数据更新的逻辑
        else{
            log.info("[updateDeliveryNote] 直接走数据更新的逻辑 >>" );
            deliveryDetailParamList.forEach( deliveryDetailVo -> {
                Long deliveryDetailId = deliveryDetailVo.getId();
                deliveryDetailVo.setPlanUnit( deliveryNoteSaveParam.getPlanUnit() );
                // 更新送货单明细信息
                deliveryDetailService.updateById( deliveryDetailVo );
                // 附件处理
                attachmentRelService.clearAndSave(deliveryDetailId, deliveryDetailVo.getAttachmentList());
            } );

        }

        // 如果送货单是 待签收，则需要更新ERP
        if( deliveryNoteIsToBeSigned ){

            Asserts.isTrue( CollectionUtil.isNotEmpty(deliveryDetailParamList), "送货单明细不能为空！");

            // 1、校验是否有 erpId
            long count = deliveryDetailParamList.stream().map(DeliveryDetailVo::getErpId).filter(Objects::nonNull).count();
            Asserts.isTrue( count == deliveryDetailParamList.size() ,"待签收状态下不能删除送货单明细！" );

            //2、更新ERP
            Asserts.isTrue( Objects.nonNull( deliveryNote.getErpId() ) ,"送货单无对应ERP的ID！" );

            //3、更新信息 >> 此时是更新信息
            this.createPurchaseReceiveBillCallCreate( deliveryNoteSaveParam ,deliveryNote.getErpId());
        }

        this.saveOperationRecord( deliveryNoteSaveParam, "送货单修改记录");

        //发送消息通知
        this.sendMessageNotice(deliveryNoteSaveParam, orgId);

        DeliveryNoteVo deliveryNoteVo = BeanUtil.copy(deliveryNoteSaveParam, DeliveryNoteVo.class);
        deliveryNoteVo.setDeliveryDetailList(deliveryDetailParamList);
        return deliveryNoteVo;




    }

    /**
     * 处理旧送货明细列表回滚
     * @param oldDeliveryDetailList
     */
    private void handleOldDeliveryDetailListFallback(List<DeliveryDetail> oldDeliveryDetailList) {
        if( CollectionUtil.isNotEmpty( oldDeliveryDetailList ) ){

            // 回退送货计划项次剩余可发货数量
            oldDeliveryDetailList.forEach( deliveryDetail -> {

                DeliveryPlanDetailItem deliveryPlanDetailItem = deliveryPlanDetailItemService.getById(deliveryDetail.getDeliveryPlanDetailItemId());
                Asserts.notNull( deliveryPlanDetailItem ,"获取送货计划明细项次信息失败！");

                // 说明之前是有加上超额的
                if ( new BigDecimal( deliveryPlanDetailItem.getRemainingQuantity()).add( deliveryDetail.getRealDeliveryQuantity() ) .compareTo( new BigDecimal( deliveryPlanDetailItem.getDeliveryQuantity()) ) > 0){
                    DeliveryPlanDetailItem deliveryPlanDetailItemUpdate = new DeliveryPlanDetailItem();
                    deliveryPlanDetailItemUpdate.setId( deliveryPlanDetailItem.getId() );
                    deliveryPlanDetailItemUpdate.setRemainingQuantity( deliveryPlanDetailItem.getDeliveryQuantity() );
                    deliveryPlanDetailItemService.updateById( deliveryPlanDetailItemUpdate ) ;
                }
                // 否则就回滚数量
                else {
                    deliveryPlanDetailItemService.incrRemainingQuantity( deliveryDetail.getDeliveryPlanDetailItemId(),deliveryDetail.getRealDeliveryQuantity() );
                }

            });

            // 删除之前的送货明细
            Set<Long> oldDeliveryDetailIdSet = oldDeliveryDetailList.stream().map(DeliveryDetail::getId).collect(Collectors.toSet());
            deliveryDetailService.removeByIds( oldDeliveryDetailIdSet );
            // 移除附件
            oldDeliveryDetailIdSet.forEach( id -> attachmentRelService.clearAndSave( id,new ArrayList<>() ) );
        }
    }

    /**
     * 更新送货单
     * @param deliveryNoteSaveParam
     */
    private void updateDeliveryNoteInfo(DeliveryNoteSaveParam deliveryNoteSaveParam) {
        //设值have_attachment成员
        this.setHaveAttachmentField( deliveryNoteSaveParam);
        this.updateById( deliveryNoteSaveParam );
        //保存送货单的同时保存附件
        attachmentRelService.clearAndSave( deliveryNoteSaveParam.getId(), deliveryNoteSaveParam.getAttachmentList() );
    }

    /**
     * 检查是否更新主要信息
     *  1、明细没有修改
     *  3 现在的明细和参数明细数量不对等
     *  2、实际送货数量没有更改
     *  3、之前的送货明细为空
     *  4、新保存的送货明细为空
     * @param oldDeliveryDetailList
     * @param deliveryDetailParamList
     * @return true 有修改 false 无修改
     */
    private boolean checkDeliveryDetailHaveUpdateRealDeliveryQuantity(List<DeliveryDetail> oldDeliveryDetailList, List<DeliveryDetailVo> deliveryDetailParamList) {

        Set<Long> deliveryDetailIdSet = oldDeliveryDetailList.stream().map(DeliveryDetail::getId).collect(Collectors.toSet());

        if(CollectionUtil.isEmpty( deliveryDetailIdSet ) || CollectionUtil.isEmpty( deliveryDetailParamList ) ){
            return true;
        }

        if( oldDeliveryDetailList.size() != deliveryDetailParamList.size()){
            return true;
        }

        Map<Long, DeliveryDetail> idDeliveryDetailMap = oldDeliveryDetailList.stream().collect(Collectors.toMap(DeliveryDetail::getId, Function.identity()));

        for (DeliveryDetailVo deliveryDetailVo : deliveryDetailParamList) {

            Long deliveryDetailId = deliveryDetailVo.getId();
            if (Objects.isNull( deliveryDetailId )) {
                return true;
            }

            if(! deliveryDetailIdSet.contains( deliveryDetailId ) ){
                return true;
            }

            DeliveryDetail deliveryDetail = idDeliveryDetailMap.get(deliveryDetailId);
            if( deliveryDetail.getRealDeliveryQuantity().compareTo( deliveryDetailVo.getRealDeliveryQuantity() ) != 0 ){
                return true;
            }

        }
        return false;

    }

    /**
     * 保存送货明细
     * @param deliveryNoteSaveParam
     * @param orgId
     * @param deliveryDetailParamList
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveDeliveryDetailList(DeliveryNoteSaveParam deliveryNoteSaveParam, Long orgId, List<DeliveryDetailVo> deliveryDetailParamList) {

        if( CollectionUtil.isEmpty( deliveryDetailParamList )){
            return;
        }

        // 不可以同时选同一条送货计划记录保存  rao:330优化移除此校验
//        Map<Long, Long> deliveryPlanDetailItemIdCountMap = deliveryDetailParamList.stream().collect(Collectors.groupingBy(DeliveryDetailVo::getDeliveryPlanDetailItemId, Collectors.counting()));
//        deliveryPlanDetailItemIdCountMap.forEach( (k,v) -> Asserts.isTrue( v == 1,"不可以同时选同一条送货计划记录保存" ));

        // 在送货单保存之前先校验所选择的送货计划的字段
        Asserts.isTrue( deliveryDetailParamList.stream().map( DeliveryDetailVo::getOrderType ).collect(Collectors.toSet()).size() == 1 ,"送货计划订单类型不一致" );
        Asserts.isTrue( deliveryDetailParamList.stream().map( DeliveryDetailVo::getDeliveryUnitName ).collect(Collectors.toSet()).size() == 1 ,"送货计划计划单位不一致"  );
        Asserts.isTrue( deliveryDetailParamList.stream().map( DeliveryDetailVo::getCurrency ).collect(Collectors.toSet()).size() == 1 ,"送货计划结算币别不一致"  );
        Asserts.isTrue( deliveryDetailParamList.stream().map( DeliveryDetailVo::getMaterialClassification ).collect(Collectors.toSet()).size() == 1 ,"送货计划物料分类不一致"  );


        //查询可发数量比例
        BigDecimal ratioOfSentProportion = this.getRatioOfSentProportion(orgId );

        // 扣减项次的剩余发货数量
        int row = 1;
        for (DeliveryDetailVo deliveryDetailVo : deliveryDetailParamList) {

            // 填写的实际发货数量
            BigDecimal realDeliveryQuantity = deliveryDetailVo.getRealDeliveryQuantity();

            Long deliveryPlanDetailItemId = deliveryDetailVo.getDeliveryPlanDetailItemId();
            Asserts.notNull(deliveryPlanDetailItemId,"送货明细信息未指定送货计划明细的项次！" );
            // 查询项次信息
            DeliveryPlanDetailItem deliveryPlanDetailItem = deliveryPlanDetailItemService.getById( deliveryPlanDetailItemId);
            Asserts.notNull( deliveryPlanDetailItem,"获取送货计划明细的项次信息失败！" );
            BigDecimal remainingQuantity = new BigDecimal(deliveryPlanDetailItem.getRemainingQuantity());

            // 实际发货的值大于该项次的值
            if( realDeliveryQuantity.compareTo( remainingQuantity ) > 0 ){
                DeliveryDetailUtils.verifyRemainingShippableQuantityAccord( remainingQuantity.intValue(),ratioOfSentProportion,realDeliveryQuantity.longValue(), "第"+ row + "行明细，剩余可发货数量不足！");
            }

            // 当前剩余可发货数 - 实际 < 0 那么 剩余则需要设置为0
            if (new BigDecimal( "0").compareTo( new BigDecimal( deliveryPlanDetailItem.getRemainingQuantity()).subtract( realDeliveryQuantity ) ) > 0 ){
                DeliveryPlanDetailItem deliveryPlanDetailItemUpdate = new DeliveryPlanDetailItem();
                deliveryPlanDetailItemUpdate.setId( deliveryPlanDetailItemId );
                deliveryPlanDetailItemUpdate.setRemainingQuantity( "0" );
                deliveryPlanDetailItemService.updateById( deliveryPlanDetailItemUpdate );
            }
            // 否则就扣减数量
            else {
                deliveryPlanDetailItemService.incrRemainingQuantity( deliveryPlanDetailItemId, realDeliveryQuantity.multiply( new BigDecimal("-1")) );
            }

            // 计划单位/送货单位
            deliveryDetailVo.setPlanUnit( deliveryNoteSaveParam.getPlanUnit() );

            row++;
        }

        // 保存当前明细
        List<DeliveryDetailVo> deliveryDetailVos = deliveryDetailService.saveDetails( deliveryNoteSaveParam.getId(), deliveryDetailParamList);
        //保存明细中的附件  附件信息是前端传入的
        deliveryDetailVos.forEach( detail -> {
            //清除之前的附件关系并建立新的附件关联
            attachmentRelService.clearAndSave(detail.getId(), detail.getAttachmentList());
        });
    }

    /**
     * 获取当前登录供应商编码
     * @return
     */
    private String getLoginCurrentSupplierCode() {
        Optional<Supplier> currentSupplierOpt = SecurityUtils.getCurrentSupplier();
        Supplier supplier = currentSupplierOpt.orElseThrow(() -> new ApiException("非供应商无法访问此接口！"));
        String supplierCode = supplier.getSupplierCode();
        Asserts.isTrue( StrUtil.isNotBlank( supplierCode),"获取供应商编码失败，请联系管理员！" );
        return supplierCode;
    }

    private void sendMessageNotice(DeliveryNoteSaveParam deliveryNoteSaveParam, Long orgId) {
        deliveryNoteSaveParam.setOrgId(orgId);
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNoteSaveParam, DeliveryNoteEvent.class);
        deliveryNoteEvent.setBusinessId(deliveryNoteSaveParam.getId());
        deliveryNoteEvent.setDeliveryId(deliveryNoteSaveParam.getId());
        //发送消息通知，创建钉钉待办
        log.info("送货单编辑通知事件,送货数据{}", JSON.toJSONString(deliveryNoteEvent));
        DeliveryNoteEditEvent deliveryNoteEditEvent = new DeliveryNoteEditEvent(this, loginInfo, deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryNoteEditEvent);
    }

    /**
     * 保存操作记录
     * @param deliveryNoteSaveParam
     * @param msg
     */
    private void saveOperationRecord(DeliveryNoteSaveParam deliveryNoteSaveParam, String msg) {
        DeliveryOperationLog deliveryOperationLog = new DeliveryOperationLog();
        deliveryOperationLog.setBillId(deliveryNoteSaveParam.getId());
        deliveryOperationLog.setOperatorNo(deliveryNoteSaveParam.getDeliveryNo());
        deliveryOperationLog.setOperatorName(deliveryNoteSaveParam.getCreatedByName());
        deliveryOperationLog.setOperatorContent(msg);
        deliveryOperationLogService.save(deliveryOperationLog);
    }

    /**
     * 验证供应商的合作状态？
     * @param supplierCode
     */
    private void verifySupplierCooperationStatus(String supplierCode) {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCode(supplierCode);
        R<SupplierDTO> supplierR = supplierFeignClient.findOneSupplierByCode(supplierDTO);

        Asserts.isTrue( supplierR.isSuccess(),supplierR.getMsg() );
        SupplierDTO supplier = supplierR.getData();
        Asserts.notNull( supplier ,"供应商信息异常！");

        String relevanceStatus = supplier.getRelevanceStatus();
        log.info( "供应商合作状态打印：{}", relevanceStatus );
        try {
            if (!(SupplierRelevanceStatusEnum.RELEVANCE.getCode().equals(relevanceStatus) || SupplierRelevanceStatusEnum.SUSPEND_COOPERATION.getCode().equals(relevanceStatus))) {
                throw new Exception("供应商合作状态异常");
            }
        } catch (Exception e) {
            log.error("error!",e);
            throw new ApiException("供应商合作状态异常，请联系管理员！");
        }
    }

    /**
     * 非待签收下 -- 更新且发布
     * @param deliveryNoteSaveParam
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RepeatRequestOperation
    public DeliveryNoteVo updateSign(DeliveryNoteSaveParam deliveryNoteSaveParam) {
        log.info("========================================================");
        log.info("修改开始，数据为：{}",JSON.toJSONString(deliveryNoteSaveParam));
        log.info("========================================================");

        // 更新送货单信息， 并未创建收料通知单，因为此时送货单还不是 待签收状态
        DeliveryNoteVo deliveryNoteVo = this.updateDeliveryNote(deliveryNoteSaveParam);

        Asserts.isTrue( CollectionUtil.isNotEmpty(deliveryNoteVo.getDeliveryDetailList()),"送货单明细不能为空！");
        // 创建收料通知单
        this.createPurchaseReceiveBillCallCreate( deliveryNoteSaveParam, null);

        // 设置成 待签收状态
        deliveryNoteSaveParam.setDeliveryStatusCode(DeliveryStatusEnum.TO_BE_SIGNED.getCode());
        this.updateById( deliveryNoteSaveParam);

        return deliveryNoteVo;

    }

    /**
     * 创建收料通知单抽取方法
     * @param deliveryNote
     * @param erpId
     * @return
     */
    public R<PurchaseReceiveBillCallCreateVo>  createDeliveryOfCargoFromStorage(DeliveryNoteSaveParam deliveryNote ,Long erpId){

        log.info("will createDeliveryOfCargoFromStorage info :{},erpId:{}", JSON.toJSONString(deliveryNote),erpId);

        //判断是否按订单送货创建收料通知单 数据准备
        boolean isMrpDelivery = StrUtil.equals(deliveryNote.getMrpStatus(), MrpDeliveryNoteTypeEnum.MRP_DELIVERY.getCode());
        OrderForDeliveryInfoDTO orderDTO = null;
        //List<OrderDetailDTO> detailListDTO = null;
        Map<Long, OrderDetailDTO> detailRemoteMap = null;


        R<PurchaseReceiveBillCallCreateVo> purchaseReceiveBillOrder=null;
        List<DeliveryDetailVo> deliveryDetailList = deliveryNote.getDeliveryDetailList();
        PurchaseReceiveBillCreateOrderDto purchaseReceiveBillCreateOrderDto = new PurchaseReceiveBillCreateOrderDto();

            try {
    //            erpId不为空那么就是修改和更新
                if(erpId!=null){
                    purchaseReceiveBillCreateOrderDto.setFid(erpId);
                }
                purchaseReceiveBillCreateOrderDto.setSrmId(deliveryNote.getId());

                //设置预计到厂时间
                if(deliveryNote.getEstimatedTime()!=null){
                    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
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

                //这两个都是填采购组织,上面已经校验了,这里不需要为空校验
                //收料组织
                purchaseReceiveBillCreateOrderDto.setFStockOrgId(new Number(deliveryNote.getCustomerCode()));
                //需求组织
                purchaseReceiveBillCreateOrderDto.setFDemandOrgId(new Number(deliveryNote.getCustomerCode()));



                // TODO 收料单单据类型转换
//                purchaseReceiveBillCreateOrderDto.setFBillTypeId();

                //送货计划的明细字段
                List<PurchaseReceiveBillCreateOrderDto.FDetailEntity> fDetailEntities = new ArrayList<>();

                if(isMrpDelivery){
                    List<Long> orderDetailids = deliveryDetailList.stream().map(item -> item.getDeliveryPlanDetailItemId()).collect(Collectors.toList());
                    R<MrpOrderCreatePurchaseOrderBillDTO> purchaseOrderAndDetailsForRemoteR = zcOrderServiceFeignClient.getPurchaseOrderAndDetailsByDetailIds(orderDetailids);
                    Asserts.isTrue(purchaseOrderAndDetailsForRemoteR.isSuccess()&&ObjectUtil.isNotEmpty(purchaseOrderAndDetailsForRemoteR.getData()),"创建收料通知单时,远程获取订单主体和明细信息出现问题"+purchaseOrderAndDetailsForRemoteR.getMsg());
                    MrpOrderCreatePurchaseOrderBillDTO mrpOrderCreatePurchaseOrderBillDTO = purchaseOrderAndDetailsForRemoteR.getData();
                    orderDTO = mrpOrderCreatePurchaseOrderBillDTO.getOrderDTO();
                    List<OrderDetailDTO> detailListDTO = mrpOrderCreatePurchaseOrderBillDTO.getDetailListDTO();
                    detailRemoteMap = detailListDTO.stream().collect(Collectors.toMap(OrderDetailDTO::getId, item -> item));
                }

                for (DeliveryDetail detail : deliveryDetailList) {
                    PurchaseReceiveBillCreateOrderDto.FDetailEntity fDetailEntity = new PurchaseReceiveBillCreateOrderDto.FDetailEntity();

                    //单价
                    if(detail.getProductUnitPrice()==null){
                        log.info("单价为空，{}",JSON.toJSONString(deliveryDetailList));
                        throw  new RuntimeException("单价为空");
                    }
                    if(detail.getProductUnitPrice()!=null){
                        fDetailEntity.setFPrice( detail.getProductUnitPrice());
                    }

                    //含税单价
                    if(detail.getTaxUnitPrice()==null){
                        log.info("含税单价为空，{}",JSON.toJSONString(deliveryDetailList));
                        throw  new RuntimeException("单价为空");
                    }
                    if(detail.getTaxUnitPrice()!=null){
                        fDetailEntity.setFTaxPrice( detail.getTaxUnitPrice());
                    }

                    // //商家编码
//                     fDetailEntity.setFMssjbm(detail.getBarCode());

                    //预计预计到货日期
                    if(deliveryNote.getEstimatedTime()!=null){
                        SimpleDateFormat sdfForFPreDeliveryDatePattern = new SimpleDateFormat( "yyyy-MM-dd" );
                        String strForFPreDeliveryDatePattern = sdfForFPreDeliveryDatePattern.format(deliveryNote.getEstimatedTime());
                        fDetailEntity.setFPreDeliveryDate(strForFPreDeliveryDatePattern);
                    }


                    if ( detail.getErpId() != null) {
                        fDetailEntity.setFEntryId( detail.getErpId() );
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
                        fDetailEntity.setFActReceiveQty( detail.getRealDeliveryQuantity().toString());
                    }
                    //供应商交货数量
                    if (detail.getPlanQuantity() != null) {
                        fDetailEntity.setFsupdelqty( detail.getPlanQuantity().toString() );
                    }
                    //仓库
                    if (detail.getWarehouse() != null) {
                        //String purCode = deliveryNote.getCustomerCode();
                        String warehouse = detail.getWarehouse();
                        //String superWarehouse = StrUtil.equals(purCode, WarehouseEnum.DEFAULT_PUR_CODE.getCode()) ? warehouse : warehouse + WarehouseEnum.SYMBOL.getCode()+purCode ;

                        Number warehouseNumber = new Number();
                        warehouseNumber.setFNumber(warehouse);
                        //warehouseNumber.setFNumber(superWarehouse);
                        fDetailEntity.setFStockId(warehouseNumber);
                    }
                    //计价基本数量  =实际送货数量
                    if (detail.getRealDeliveryQuantity() != null) {
                        fDetailEntity.setFPriceBaseQty( detail.getRealDeliveryQuantity().toString());
                        //库存单位数量=实际送货数量
                        fDetailEntity.setFStockQty( detail.getRealDeliveryQuantity().toString());
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
                    if(isMrpDelivery){
                        //mrp送货单跑这里
                        OrderDetailDTO orderDetailDTO = detailRemoteMap.get(detail.getDeliveryPlanDetailItemId());
                        this.wrapperMrpDetailEntryLink(fDetailEntity,orderDTO,orderDetailDTO);
                    }else{
                        //普通按计划送货单跑这里
                        this.wrapperDetailEntryLink( fDetailEntity,detail);
                    }


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
                if(orderType==null){//没有单据类型
                    log.error("送货单详情没有传单据类型：orderType= ,{}", JSON.toJSONString(orderType));
                    throw new RuntimeException("没有单据类型"+orderType);
                }


                if(orderType!=null){
                    if(orderType.equals("CGDD01_SYS")){
                        purchaseReceiveBillCreateOrderDto.setFBillTypeId(new Number(FBillTypeEnum.FBILLTYPE_STANDAR.getCode()));

                    }else if(orderType.equals("CGDD02_SYS")){
                        purchaseReceiveBillCreateOrderDto.setFBillTypeId(new Number(FBillTypeEnum.FBILLTYPE_OUTSOURCING.getCode()));
                    }
                }

                //补充字段
    //        purchaseReceiveBillCreateOrderDto.setFMsWlfl(new Number(deliveryNote.getDeliveryTypeCode()));

                log.info("purchaseReceiveBillCreateOrderDto字段值：单据类型:{},"+ JSON.toJSONString(purchaseReceiveBillCreateOrderDto),purchaseReceiveBillCreateOrderDto.getFBillTypeId());
                purchaseReceiveBillOrder = purchaseReceiveBillOrderFeignClient.createPurchaseReceiveBillOrder(purchaseReceiveBillCreateOrderDto);
                if(!(purchaseReceiveBillOrder.isSuccess())){
                    throw new RuntimeException(purchaseReceiveBillOrder.getMsg());
                }

                if(purchaseReceiveBillOrder.getCode()!=200){

                    log.info("purchaseReceiveBillCreateOrderDto字段值："+ JSON.toJSONString(purchaseReceiveBillCreateOrderDto));
                    log.info("purchaseReceiveBillCreateOrderDto字段值："+ JSON.toJSONString(purchaseReceiveBillOrder));
                    throw new RuntimeException(""+JSON.toJSONString(purchaseReceiveBillCreateOrderDto));
                }

                if (purchaseReceiveBillOrder.getData() != null) {
                    Long fId = purchaseReceiveBillOrder.getData().getFId();
                    String fNumber = purchaseReceiveBillOrder.getData().getFNumber();
                    List<PurchaseReceiveBillEntryCallCreateVo> purchaseReceiveBillEntryCallCreateVoList = purchaseReceiveBillOrder.getData().getPurchaseReceiveBillEntryCallCreateVoList();


                    //创建成功 调用回写方法
                    writeBack(deliveryNote, deliveryDetailList, fId, fNumber, purchaseReceiveBillEntryCallCreateVoList);
                    /*if(isMrpDelivery){
                        mrpWriteBack(orderDTO, detailRemoteMap, fId, fNumber, purchaseReceiveBillEntryCallCreateVoList);
                    }else{
                        writeBack(deliveryNote, deliveryDetailList, fId, fNumber, purchaseReceiveBillEntryCallCreateVoList);
                    }*/

                }

                //***************************

            }catch (Exception e){
                log.error("收料通知单问题：传输的参数:{}", JSON.toJSONString(purchaseReceiveBillCreateOrderDto));
                log.error("收料通知单问题：返回数据：,{}", JSON.toJSONString(purchaseReceiveBillOrder));
                log.error("收料通知单问题：返回异常", e);
                throw new RuntimeException("收料通知单问题" + e.getMessage() );
            }

        return  purchaseReceiveBillOrder;


        }

    /**
     * 收料通知单创建成功后回写送货计划方法
     * */
    private void writeBack(DeliveryNoteSaveParam deliveryNote, List<DeliveryDetailVo> deliveryDetailList, Long fId, String fNumber, List<PurchaseReceiveBillEntryCallCreateVo> purchaseReceiveBillEntryCallCreateVoList) {
        //保存erpId和单据编码
        updateErpId(deliveryNote.getId(), fId, fNumber, ErpSigningStatusEnum.NOT_SIGNED.getCode());
        //保存detail的erpId
        int k=0;
        for (DeliveryDetail detail : deliveryDetailList) {
            for (PurchaseReceiveBillEntryCallCreateVo purchaseReceiveBillEntryCallCreateVo : purchaseReceiveBillEntryCallCreateVoList) {
                if (purchaseReceiveBillEntryCallCreateVo.getSrmId().equals(detail.getId())) {
                    detail.setErpId(purchaseReceiveBillEntryCallCreateVo.getErpId());
                    try{
                        ++k;
                        log.info(""+k+"保存的detail为：, {}", JSON.toJSONString(detail));
                        deliveryDetailService.updateById(detail);
                    }catch (Exception e){
                        log.error(""+k+"保存的detail为：, {}",JSON.toJSONString(detail));
                        throw new RuntimeException("第"+k+"次保存的时候出问题:"+e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * 收料通知单创建成功后回写送货计划方法
     * */
    private void mrpWriteBack(OrderForDeliveryInfoDTO orderDTO,Map<Long, OrderDetailDTO> detailRemoteMap, Long fId, String fNumber, List<PurchaseReceiveBillEntryCallCreateVo> purchaseReceiveBillEntryCallCreateVoList) {
        //保存erpId和单据编码
        MrpOrderCreatePurchaseOrderBillReturnParamDTO writeBackDTO = new MrpOrderCreatePurchaseOrderBillReturnParamDTO();
        writeBackDTO.setOrderId(orderDTO.getId());
        writeBackDTO.setFId(fId);
        writeBackDTO.setFNumber(fNumber);
        writeBackDTO.setErpStatus(ErpSigningStatusEnum.NOT_SIGNED.getCode());
        //updateErpId(deliveryNote.getId(), fId, fNumber, ErpSigningStatusEnum.NOT_SIGNED.getCode());
        //保存detail的erpId

        int k=0;
        List<MrpOrderCreatePurchaseOrderBillReturnParamDTO.returnDetail> returnDetailList=new ArrayList<>();

        for (PurchaseReceiveBillEntryCallCreateVo purchaseReceiveBillEntryCallCreateVo : purchaseReceiveBillEntryCallCreateVoList) {

            OrderDetailDTO detail = detailRemoteMap.get(purchaseReceiveBillEntryCallCreateVo.getSrmId());

                //detail.setErpId(purchaseReceiveBillEntryCallCreateVo.getErpId());
                try{
                    ++k;
                    log.info(""+k+"保存的detail为：, {}", JSON.toJSONString(detail));

                    MrpOrderCreatePurchaseOrderBillReturnParamDTO.returnDetail returnDetail = new MrpOrderCreatePurchaseOrderBillReturnParamDTO.returnDetail();
                    returnDetail.setId(detail.getId());
                    returnDetail.setErpId(purchaseReceiveBillEntryCallCreateVo.getErpId());
                    returnDetailList.add(returnDetail);

                    //deliveryDetailService.updateById(detail);
                }catch (Exception e){
                    log.error(""+k+"保存的detail为：, {}",JSON.toJSONString(detail));
                    throw new RuntimeException("第"+k+"次保存的时候出问题:"+e.getMessage());
                }
        }
        writeBackDTO.setReturnDetailList(returnDetailList);

        //远程调用更新
        R<Boolean> reWriteFlagR = zcOrderServiceFeignClient.updateMrpOrderAndDetails(writeBackDTO);
        Asserts.isTrue(reWriteFlagR.isSuccess(),"回写失败"+reWriteFlagR.getMsg());
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


        //设置附件
        buildAttachment(deliveryNoteVo);
        //获取明细信息
        DeliveryDetail deliveryDetail = DeliveryDetail.builder().deliveryId(id).build();
        QueryWrapper<DeliveryDetail> detailQueryWrapper = Condition.getQueryWrapper(deliveryDetail);
        List<DeliveryDetail> deliveryDetailList = deliveryDetailService.list(detailQueryWrapper);
        List<DeliveryDetailVo> detailVos = BeanUtil.copy(deliveryDetailList, DeliveryDetailVo.class);
        this.batchSetDeliveryDetailAttachment(detailVos);

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

        String tm = deliveryNote.getTransportMethod();
        if (StringUtil.equals(tm, DictCodeTransportMethodEnum.CAR.getCode())) {
            deliveryNoteVo.setTransportMethod(DictCodeTransportMethodEnum.CAR.getMsg());
        } else if (StringUtil.equals(tm, DictCodeTransportMethodEnum.RAILWAY.getCode())) {
            deliveryNoteVo.setTransportMethod(DictCodeTransportMethodEnum.RAILWAY.getMsg());
        } else if (StringUtil.equals(tm, DictCodeTransportMethodEnum.PLANE.getCode())) {
            deliveryNoteVo.setTransportMethod(DictCodeTransportMethodEnum.PLANE.getMsg());
        } else {
            deliveryNoteVo.setTransportMethod(DictCodeTransportMethodEnum.SHIP.getMsg());
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
    @RepeatRequestOperation
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
    @RepeatRequestOperation
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
    @RepeatRequestOperation
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
                        .select( DeliveryDetail::getId,DeliveryDetail::getDeliveryId )
                        .eq(DeliveryDetail::getErpId, erpId)
        );

        if(deliveryDetail == null){
            log.error("[syncErpDeliveryNoteDetailInfo] 查询送货单明细失败 ，参数：{} ",deliveryNoteUpdateDTO);
            return;
        }

        //查询送货单信息
        DeliveryNote deliveryNote = this.getOne(
                Wrappers.<DeliveryNote>lambdaQuery()
                        .select( DeliveryNote::getId )
                        .eq(DeliveryNote::getId,deliveryDetail.getDeliveryId() )
        );
        if( deliveryNote == null){
            log.error("[syncErpDeliveryNoteDetailInfo] 查询送货单失败 ，查询参数：{} ", deliveryNote);
            return;
        }

        // 更新送货单明细
        deliveryDetail.setReceiptQuantity( deliveryNoteUpdateDTO.getReceiptQuantity() );
        deliveryDetailService.updateById( deliveryDetail );

        deliveryNote.setSigningTime( deliveryNoteUpdateDTO.getWarehouseDate() );
        deliveryNote.setErpSigningStatus(ErpSigningStatusEnum.SIGNED.getCode()  );
        deliveryNote.setDeliveryStatusCode( DeliveryStatusEnum.COMPLETED.getCode() );
        deliveryNote.setReceiptNo(deliveryNoteUpdateDTO.getReceiptNo());
        this.updateById( deliveryNote );
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
    @Transactional(rollbackFor = Exception.class)
    @RepeatRequestOperation
    public Long batchDelivery(List<String> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            ArrayList<DeliveryPlanDetailItemVo> itemVos = new ArrayList<>();
            for (String id : ids) {
                DeliveryPlanDetailItemVo detailItemVo = deliveryPlanMapper.getPlanBoardDetailById(id);
                itemVos.add(detailItemVo);
            }
            //判断：关联了送货单的项次不能二次发货
            itemVos.forEach(itemVo->{
                if (StringUtil.isNotBlank(itemVo.getDeliveryNoteId())){
                    throw new ApiException(ResultCode.REQ_REJECT.getCode(),"关联了送货单的项次不能被二次发货");
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
            String supplierName=null;
            String supplierCode = SecurityUtils.getLoginInfo().get().getCurrentSupplier().getSupplierCode();
            SupplierDTO supplierDTO = new SupplierDTO();
            supplierDTO.setCode(supplierCode);
            R<SupplierDTO> oneSupplierByCode = supplierFeignClient.findOneSupplierByCode(supplierDTO);
            if(oneSupplierByCode.isSuccess()){
                supplierName = oneSupplierByCode.getData().getName();
            }

            if (flag.get()) {
                //新建送货单，给默认值
                DeliveryNote note = new DeliveryNote()
                        .setCustomerCode(purchaseCode)
                        .setCustomerName(purchaseName)
                        //送货单号
                        .setDeliveryNo(numberFactory.buildNumber(NumberType.ship))
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
                DeliveryPlanDetailItemVo deliveryPlanDetailItemVo=null;
                for (String id : ids) {
                    try{
                        deliveryAsPlanParamQueryParam.getParam().setId(Long.valueOf(id));
                        IPage<DeliveryPlanDetailItemVo> deliveryAsPlanOne = deliveryPlanService.getDeliveryAsPlanList(null, deliveryAsPlanParamQueryParam);
                        deliveryPlanDetailItemVo = deliveryAsPlanOne.getRecords().get(0);
                        log.info("获取到的deliveryAsPlan为：{}",JSON.toJSONString(deliveryPlanDetailItemVo));
                    }catch(Exception e){
                        log.info("查询送货计划接口出现问题");


                    }


                }
                List<DeliveryDetail> deliveryDetailList=new ArrayList<>();
                DeliveryNoteSaveParam deliveryNoteSaveParam = new DeliveryNoteSaveParam();

                try {
                    DeliveryNoteVo deliveryNoteVo = saveDeliveryNote(deliveryNoteSaveParam);
                }catch (Exception e){
                    log.info("批量创建出现异常，{}");
                }



//todo ******************************
                //保存送货单
                if (save(note)) {
                    AtomicReference<Integer> itemNo= new AtomicReference<>(1);
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
     * @param id
     */
    @Override
    @RepeatRequestOperation
    public void updateStatus(Long id ) {
        log.info("========================================================");
        log.info("确认发货开始，数据为：{}",JSON.toJSONString(id));
        log.info("========================================================");

        //确认发货修改送货单状态的同时记录操作
        DeliveryNote deliveryNote = getById(id);//通过id查询送货单
        Asserts.notNull( deliveryNote,"查询送货单信息不存在，请刷新后重试！");

        LambdaQueryWrapper<DeliveryDetail> detailLambdaQueryWrapper = Wrappers.lambdaQuery();
        LambdaQueryWrapper<DeliveryDetail> eq = detailLambdaQueryWrapper.eq(DeliveryDetail::getDeliveryId, id);
        List<DeliveryDetail> detailList = deliveryDetailService.list(eq);//通过送货单id查询送货单详情
        Asserts.isTrue(CollectionUtil.isNotEmpty(detailList), "送货单明细不能为空！");
        List<DeliveryDetailVo> deliveryDetailList = BeanUtil.copy( detailList, DeliveryDetailVo.class);

        DeliveryNoteSaveParam deliveryNoteSaveParam = new DeliveryNoteSaveParam();
        BeanUtils.copyProperties(deliveryNote,deliveryNoteSaveParam);
        deliveryNoteSaveParam.setDeliveryDetailList(deliveryDetailList);

        DeliveryOperationLog deliveryOperationLog = new DeliveryOperationLog();
        deliveryOperationLog.setBillId(deliveryNote.getId());

        LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
        deliveryOperationLog.setOperatorNo(deliveryNote.getDeliveryNo());
        deliveryOperationLog.setOperatorName(deliveryNote.getCreatedByName());
        deliveryOperationLog.setOperatorContent("发送送货单");
        deliveryOperationLog.setBillId(id);

        deliveryOperationLogService.save(deliveryOperationLog);
        //确认发货的同时发送消息，通知采购商,发送消息通知
        deliveryNote.setDeliveryStatusCode(DeliveryStatusEnum.TO_BE_SIGNED.getCode());
        DeliveryNoteEvent deliveryNoteEvent = BeanUtil.copy(deliveryNote, DeliveryNoteEvent.class);
        deliveryNoteEvent.setBusinessId(deliveryNote.getId());
        deliveryNoteEvent.setDeliveryId(deliveryNote.getId());
        //发送消息通知，创建钉钉待办
        log.info("送货单发出通知事件,送货数据{}", JSON.toJSONString(deliveryNoteEvent));
        DeliveryNoteConfirmEvent deliveryNoteConfirmEvent = new DeliveryNoteConfirmEvent(this, loginInfo, deliveryNoteEvent, deliveryNoteEvent.getSupplierCode(), deliveryNoteEvent.getSupplierName());
        defaultEventPublisher.publishEvent(deliveryNoteConfirmEvent);

        //调用收料通知单创建接口
        this.createDeliveryOfCargoFromStorage(deliveryNoteSaveParam, null);

        //更新送货单状态
        deliveryNoteMapper.updateStatus(id);

    }

    /**
     * 确认发布（把送货单状态改成待签收的状态）
     * @param deliveryNoteSaveParam
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RepeatRequestOperation
    public DeliveryNoteVo saveAndSign(DeliveryNoteSaveParam deliveryNoteSaveParam) {

        log.info("========================================================");
        log.info("发布开始，数据为：{}",JSON.toJSONString(deliveryNoteSaveParam));
        log.info("========================================================");

        // 发布明细不能为空
        List<DeliveryDetailVo> deliveryDetailList = deliveryNoteSaveParam.getDeliveryDetailList();
        Asserts.isTrue( CollectionUtil.isNotEmpty( deliveryDetailList ),"确认发布要求送货单明细不能为空！" );

        // 保存送货单和送货明细
        DeliveryNoteVo deliveryNoteVo = this.saveDeliveryNote(deliveryNoteSaveParam);

        // 创建收料通知单
        this.createPurchaseReceiveBillCallCreate( deliveryNoteSaveParam, null);

        // 设置成 待签收状态
        deliveryNoteSaveParam.setDeliveryStatusCode(DeliveryStatusEnum.TO_BE_SIGNED.getCode());
        this.updateById( deliveryNoteSaveParam);

        return deliveryNoteVo;

    }

    /**
     * 更新送货单状态
     * @param id
     * @param beSignedCode
     */
    private void updateDeliveryNoteDeliveryStatusCode(Long id, String beSignedCode) {
        DeliveryNote deliveryNote = new DeliveryNote();
        deliveryNote.setId( id );
        deliveryNote.setDeliveryStatusCode( beSignedCode);
        Asserts.isTrue( this.updateById( deliveryNote ), "更新送货单状态失败！" );
    }

    /**
     * 创建采购
     * @param deliveryNoteSaveParam
     * @param erpId
     */
    private void createPurchaseReceiveBillCallCreate(DeliveryNoteSaveParam deliveryNoteSaveParam, Long erpId) {
        R<PurchaseReceiveBillCallCreateVo> purchaseReceiveBillOrderR = this.createDeliveryOfCargoFromStorage( deliveryNoteSaveParam, erpId );
        Asserts.notNull( purchaseReceiveBillOrderR,"创建收料通知单失败，请联系管理员！" );

        PurchaseReceiveBillCallCreateVo purchaseReceiveBillOrderRData = purchaseReceiveBillOrderR.getData();
        Asserts.notNull(purchaseReceiveBillOrderRData,"创建收料通知单失败，请联系管理员！" );
        log.info("[createPurchaseReceiveBillCallCreate] purchaseReceiveBillOrderRData info:{}",purchaseReceiveBillOrderRData);
    }


    @Override
    @RepeatRequestOperation
    public void updateDeliveryNoteV2(Long id) {
        deliveryNoteMapper.updateSync(DeliveryStatusEnum.COMPLETED.getCode(),
                ErpSigningStatusEnum.SIGNED.getCode(),
                new Date(),
                id);

    }


    /**
     * 为送货明细设置附件
     * @param vo
     */
    @Override
    public void setDeliveryDetailAttachment(DeliveryDetailVo vo) {
        if (vo==null) {
            return;
        }
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
     * @param records
     */
    @Override
    public void batchSetDeliveryDetailAttachment(List<DeliveryDetailVo> records) {
        if (CollectionUtil.isNotEmpty(records)){
            records.forEach(this::setDeliveryDetailAttachment);
        }
    }

    /**
     * 导出，不分页查询所有数据
     * @param queryParam
     * @return
     */
    @Override
    public List<DeliveryNoteExcelModel> getDeliveryNoteList(QueryParam<DeliveryNoteParam> queryParam) {
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
                dn.setErpSigningStatus("未签收");
            } else if (StringUtil.equals(dn.getErpSigningStatus(), ErpSigningStatusEnum.SIGNED.getCode())) {
                dn.setErpSigningStatus("已签收");
            }
            //转换送货状态（导出需要中文的）
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

    /**
     * 逻辑：
     *    异步处理数据，如果匹配数据中，前端关闭上传数据的框，再次打开，若还在上传中，则需要返回结果信息。
     * @param file
     * @param deliveryNoteImportDetailParam
     * @return
     */
    @SneakyThrows
    @Override
    public DeliveryDetailExcelImportResult importDetail(MultipartFile file, DeliveryNoteImportDetailParam deliveryNoteImportDetailParam) {

        // 如果在导入中则提示不可导入
        String fileName = file.getName();
        long fileSize = file.getSize();

        Optional<Supplier> currentSupplierOpt = SecurityUtils.getCurrentSupplier();
        Supplier supplier = currentSupplierOpt.orElseThrow(() -> new ApiException("非供应商无法访问此接口！"));
        String supplierCode = supplier.getSupplierCode();

        Long deliveryNoteId = deliveryNoteImportDetailParam.getDeliveryNoteId();
        DeliveryNote deliveryNote = this.getById(deliveryNoteId);
        Asserts.notNull(deliveryNote,"送货单信息未查询到！" );
        // 采购方编码
        String customerCode = deliveryNote.getCustomerCode();

        // 校验供应商合作状态
        this.verifySupplierCooperationStatus(supplierCode);

        // 查询组织ID
        Long orgId = this.remoteGetOrgId(supplierCode, customerCode);
        //查询可发数量比例
        final BigDecimal ratioOfSentProportion = this.getRatioOfSentProportion(orgId);

        // 查询目前已存在的 物料编码、物料分类、订单类型、入货仓
        List<DeliveryPlanDetail> productCodeMerchantCodeList = deliveryPlanDetailService.listAllGroupByProductCodeAndMerchantCode();
        //Asserts.isTrue( CollectionUtil.isNotEmpty( productCodeMerchantCodeList ),"数据库中无条码与物料编码的数据，请联系管理员！" );
        Asserts.isTrue( CollectionUtil.isNotEmpty( productCodeMerchantCodeList ),"数据库中无物料编码的数据，请联系管理员！" );

        // 查询物料分类
        List<DeliveryPlan> deliveryPlanList = deliveryPlanMapper.listAllGroupByMaterialTypeAndOrderType();
        Asserts.isTrue( CollectionUtil.isNotEmpty( deliveryPlanList ),"数据库中无物料分类与订单类型的数据，请联系管理员！" );

        Set<String> allWarehouseSet = deliveryPlanDetailItemService.getAllWarehouse();
        Asserts.isTrue( CollectionUtil.isNotEmpty( allWarehouseSet ),"数据库中无入库仓信息，请联系管理员！");

        List<DeliveryNoteExcelImportDetailVo> deliveryNoteExcelImportDetailVoList = null;

        // 数据处理监听器
        DeliveryNoteExcelImportDetailListener deliveryNoteExcelImportDetailListener = new DeliveryNoteExcelImportDetailListener(
                validator,
                ratioOfSentProportion,
                deliveryPlanDetailItemService,
                deliveryNote,
                productCodeMerchantCodeList,
                deliveryPlanList,
                allWarehouseSet
        );
        try {
            deliveryNoteExcelImportDetailVoList = EasyExcel.read( file.getInputStream(), deliveryNoteExcelImportDetailListener)
                    .head(DeliveryNoteExcelImportDetailVo.class)
                    .sheet()
                    .doReadSync();
        } catch (Exception ex) {
            if (ex instanceof ApiException) {
                throw (ApiException) ex;
            }
            if (ex instanceof BizException) {
                throw (BizException) ex;
            }
            log.error("解析excel失败！", ex);
            throw new ApiException("解析excel失败，请检查模板格式或联系管理员！");
        }

        log.info("[deliveryNoteExcelImportDetailVoList] not filter will handle size:{}",deliveryNoteExcelImportDetailVoList.size() );
        deliveryNoteExcelImportDetailVoList = deliveryNoteExcelImportDetailVoList.stream().filter( DeliveryNoteExcelImportDetailVo::productCodeIsNotNull).collect(Collectors.toList());
        Asserts.isTrue( CollectionUtil.isNotEmpty( deliveryNoteExcelImportDetailVoList ),"导入Excel数据为空！" );
        log.info("[deliveryNoteExcelImportDetailVoList] after filter will handle size:{}",deliveryNoteExcelImportDetailVoList.size() );

        //  匹配导入的数据生成到明细
        Map<String, List<ShippableDeliveryPlanDetailItemVo>> productCodeShippableDeliveryPlanDetailItemListMap = deliveryNoteExcelImportDetailListener.getProductCodeShippableDeliveryPlanDetailItemListMap();
        final List<DeliveryNoteExcelImportDetailVo> finalDeliveryNoteExcelImportDetailVoList = deliveryNoteExcelImportDetailVoList;

        // 提交异步执行！
        DeliveryDetailExcelImportResult deliveryDetailExcelImportResult = new DeliveryDetailExcelImportResult().setFileName(fileName).setSize(fileSize).setDeliveryNoteId(deliveryNoteId).setStatus(DeliveryDetailExcelImportResult.UPLOADING);
        RMap<Long, DeliveryDetailExcelImportResult> deliveryDetailExcelImportRedissonMap = redissonClient.getMap(DeliveryDetailExcelImportConstants.DELIVERY_DETAIL_IMPORT_EXCEL_MAP_KEY, JsonJacksonCodec.INSTANCE);
        DeliveryDetailExcelImportResult deliveryDetailExcelImportResultOld = deliveryDetailExcelImportRedissonMap.get(deliveryNoteId);
        if( deliveryDetailExcelImportResultOld != null){
            Asserts.isFalse( DeliveryDetailExcelImportResult.UPLOADING.equals( deliveryDetailExcelImportResultOld.getStatus() ),"刚上传的excel还在处理中，请等待数据处理完成再重传！" );
        }
        deliveryDetailExcelImportRedissonMap.put( deliveryNoteId,deliveryDetailExcelImportResult);

        CompletableFuture.runAsync( () -> deliveryDetailService.matchImportExcelData( finalDeliveryNoteExcelImportDetailVoList,deliveryNote,productCodeShippableDeliveryPlanDetailItemListMap, ratioOfSentProportion) );
        return deliveryDetailExcelImportResult;
    }

    @Override
    public DeliveryDetailExcelImportResult getImportDetailStatus(Long deliveryNoteId) {
        RMap<Long, DeliveryDetailExcelImportResult> deliveryDetailExcelImportRedissonMap = redissonClient.getMap(DeliveryDetailExcelImportConstants.DELIVERY_DETAIL_IMPORT_EXCEL_MAP_KEY, JsonJacksonCodec.INSTANCE);
        return deliveryDetailExcelImportRedissonMap.get(deliveryNoteId);
    }

    @Override
    public Map<String,Long> getImportDetailExcelAttachmentId() {
        Map<String,Long> result = new HashMap<String,Long>();
        result.put("id",importDetailExcelConfig.getFileId());
        return result;
    }

    @Override
    public DeliveryDetailBatchDetailVo getDeliveryDetailBatchDetail(DeliveryDetailBatchDetailParams deliveryDetailBatchDetailParams) {

        // 送货计划明细项次
        Long deliveryPlanDetailItemId = deliveryDetailBatchDetailParams.getDeliveryPlanDetailItemId();
        DeliveryPlanDetailItem deliveryPlanDetailItem = deliveryPlanDetailItemService.getById( deliveryPlanDetailItemId);
        Asserts.notNull( deliveryPlanDetailItem,"获取送货计划明细项次信息失败！" );

        DeliveryPlanDetail deliveryPlanDetail = deliveryPlanDetailService.getById(deliveryPlanDetailItem.getDeliveryPlanDetailId());
        Asserts.notNull( deliveryPlanDetail,"获取送货计划明细信息失败！" );

        DeliveryPlan deliveryPlan = deliveryPlanService.getById(deliveryPlanDetail.getDeliveryPlanId());
        Asserts.notNull( deliveryPlan,"获取送货计划信息失败！" );

        // 建立返回对象
        DeliveryDetailBatchDetailVo deliveryDetailBatchDetailVo = new DeliveryDetailBatchDetailVo();
        BeanUtils.copyProperties( deliveryPlanDetailItem,deliveryDetailBatchDetailVo );
        // 送货计划单号
        deliveryDetailBatchDetailVo.setDeliveryPlanNo( deliveryPlan.getPlanNo() );
        // 订单单号
        deliveryDetailBatchDetailVo.setBillNo( deliveryPlan.getBillNo() );
        // 订单编号
        deliveryDetailBatchDetailVo.setPurchaseOrderNo( deliveryPlan.getPurchaseOrderNo() );
        // 产品编码
        deliveryDetailBatchDetailVo.setProductCode( deliveryPlan.getPurchaseCode() );
        // 条码
        deliveryDetailBatchDetailVo.setMerchantCode( deliveryPlanDetail.getMerchantCode() );
        // 产品名称
        deliveryDetailBatchDetailVo.setProductName( deliveryPlanDetail.getProductName() );
        // 送货日期
        Optional.ofNullable(deliveryPlanDetailItem.getDeliveryDate()).ifPresent( deliveryLoclDateTime -> deliveryDetailBatchDetailVo.setDeliveryDate( Date.from( deliveryLoclDateTime.atZone( ZoneId.systemDefault()).toInstant() ) ));

        Long deliveryNoteId = deliveryDetailBatchDetailParams.getDeliveryNoteId();
        if ( Objects.nonNull( deliveryNoteId )) {

            DeliveryNote deliveryNote = this.getById(deliveryNoteId);
            Asserts.notNull( deliveryNote,"获取送货单信息失败！" );

            // 查询送货单该项次的明细
            List<DeliveryDetail> deliveryDetailList = deliveryDetailService.lambdaQuery().eq(DeliveryDetail::getDeliveryId, deliveryNoteId).eq(DeliveryDetail::getDeliveryPlanDetailItemId, deliveryPlanDetailItemId).list();

            List<DeliveryDetailVo> detailVos = BeanUtil.copy(deliveryDetailList, DeliveryDetailVo.class);
            // 设置附件
            this.batchSetDeliveryDetailAttachment( detailVos);
            // 剩余可发货数量增加回显示
            for (DeliveryDetailVo deliveryDetailVo : detailVos) {
                deliveryDetailBatchDetailVo.addRemainingQuantity( deliveryDetailVo.getRealDeliveryQuantity() );
            }

            deliveryDetailBatchDetailVo.setDeliveryDetailList( detailVos );

        }

        return deliveryDetailBatchDetailVo;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DeliveryNoteVo saveDeliveryDetailBatchDetailSave(DeliveryNoteSaveParam deliveryNoteSaveParam) {

        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        //获取当前登录供应商编码
        String supplierCode = getLoginCurrentSupplierCode();
        // 校验供应商合作状态
        this.verifySupplierCooperationStatus( supplierCode );
        // 查询组织ID
        OrgIdDTO orgIdDto = this.getOrgIdDto(supplierCode, deliveryNoteSaveParam.getCustomerCode());

        // 获取送货单保存参数的ID
        Long deliveryNoteSaveParamId = deliveryNoteSaveParam.getId();
        // 更新送货单
        boolean isUpdateDeliveryNote = Objects.nonNull(deliveryNoteSaveParamId);
        if( isUpdateDeliveryNote ){
            // 更新送货单的信息
            this.updateDeliveryNoteInfo(deliveryNoteSaveParam);
            // 记录
            this.saveOperationRecord( deliveryNoteSaveParam, "送货单修改记录");
        }
        else{
            // 保存送货单信息
            this.saveDeliveryNoteInfo( deliveryNoteSaveParam,loginInfo,orgIdDto ,MrpDeliveryNoteTypeEnum.NORMAL_DELIVERY.getCode());
            // 赋值
            deliveryNoteSaveParamId = deliveryNoteSaveParam.getId();
            Asserts.notNull( deliveryNoteSaveParamId,"保存送货单信息失败！" );
            //记录保存操作记录
            this.saveOperationRecord(deliveryNoteSaveParam, "创建送货单");
        }

        // 保存批次明细 明细的
        List<DeliveryDetailVo> deliveryDetailVoList = deliveryNoteSaveParam.getDeliveryDetailList();
        Asserts.isTrue( CollectionUtil.isNotEmpty( deliveryDetailVoList ),"分批明细不能为空！" );

        Long deliveryPlanDetailItemId = deliveryDetailVoList.get(0).getDeliveryPlanDetailItemId();
        Asserts.notNull( deliveryPlanDetailItemId,"送货明细项次信息未关联！" );
        DeliveryPlanDetailItem deliveryPlanDetailItem = deliveryPlanDetailItemService.getById(deliveryPlanDetailItemId);
        Asserts.notNull( deliveryPlanDetailItem ,"送货明细项次信息获取失败！");

        // 先删除当前送货单与之前关联的 明细，然后重新绑定。
        if( isUpdateDeliveryNote ){

            // 构建条件
            LambdaQueryWrapper<DeliveryDetail> queryWrapper = Wrappers.<DeliveryDetail>lambdaQuery().eq(DeliveryDetail::getDeliveryId, deliveryNoteSaveParamId).eq(DeliveryDetail::getDeliveryPlanDetailItemId, deliveryNoteSaveParamId);
            // 之前存在表里的 明细 当前项次与当前送货单
            List<DeliveryDetail> oldDeliveryDetailList = deliveryDetailService.list(queryWrapper);
            // 处理已经存在的送货明细  回滚
            this.handleOldDeliveryDetailListFallback( oldDeliveryDetailList);

        }

        // 保存送货明细
        this.saveDeliveryDetailList( deliveryNoteSaveParam, orgIdDto.getOrgId(),deliveryDetailVoList );

        // 后置处理
        if( isUpdateDeliveryNote){
            //发送消息通知
            this.sendMessageNotice(deliveryNoteSaveParam, orgIdDto.getOrgId() );
        }

        DeliveryNoteVo deliveryNoteVo = BeanUtil.copy(deliveryNoteSaveParam, DeliveryNoteVo.class);
        return deliveryNoteVo;

    }

    /**
     * 查询可发送比例
     * @return
     */
    private BigDecimal getRatioOfSentProportion(Long orgId) {

        R<CfgParamInfo> cfgParamInfoR = configParamOpenFeign.findListByCodeAndOrgId(CfgParamKeyEnum.DELIVERY_EXCESS_QUANTITY,orgId );
        Asserts.isTrue( cfgParamInfoR.isSuccess(),cfgParamInfoR.getMsg() );
        CfgParamInfo cfgParamInfoRData = cfgParamInfoR.getData();
        Asserts.notNull( cfgParamInfoRData,"获取可发送比例值配置失败！" );
        String value = cfgParamInfoRData.getValue();
        Asserts.isTrue( StrUtil.isNotBlank( value),"获取到可发送比例值配置解析失败！" );
        JSONObject jsonObject = JSON.parseObject( value);
        // 获取值
        try {
            JSONArray data = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = JSON.parseObject(data.getString(0));
            boolean flag = Optional.ofNullable( jsonObject1.getBoolean("value") ).orElse(false);
            // 超出百分比
            BigDecimal exceededPercentage = new BigDecimal("0");
            if( flag ){//按钮开启才获取
                JSONObject jsonObject2 = JSON.parseObject(data.getString(1));
                exceededPercentage = jsonObject2.getBigDecimal("value");
            }
            return exceededPercentage;
        } catch (Exception ex){
            log.error("解析可发送比例配置异常。",ex);
            throw new ApiException("解析可发送比例配置异常！");
        }
    }

    /**
     * 获取组织ID
     * @param supplierCode
     * @param customerCode
     * @return
     */
    public Long remoteGetOrgId(String supplierCode, String customerCode) {
        return this.getOrgIdDto( supplierCode,customerCode).getOrgId();
    }

    /**
     * 获取组织信息
     * @param supplierCode
     * @param customerCode
     * @return
     */
    public OrgIdDTO getOrgIdDto(String supplierCode, String customerCode){
        R<OrgIdDTO> orgIdR = purchaserFeignClient.info(new OrgIdQuery().setPurchaseCode(customerCode).setSupplierCode(supplierCode));
        Asserts.isTrue( orgIdR.isSuccess(),orgIdR.getMsg() );
        Asserts.notNull( orgIdR.getData().getOrgId(),"获取组织ID失败！" );
        return orgIdR.getData();
    }

    /**
     * 保存mrp送货单信息
     *
     * @param deliveryNoteSaveParam 保存送货单请求参数
     * @return 送货单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @RepeatRequestOperation
    public DeliveryNoteVo saveMrpDeliveryNote(DeliveryNoteSaveParam deliveryNoteSaveParam) {

        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        //获取当前登录供应商编码
        String supplierCode = getLoginCurrentSupplierCode();

        // 校验供应商合作状态
        this.verifySupplierCooperationStatus( supplierCode );

        // 查询组织ID
        OrgIdDTO orgIdDto = this.getOrgIdDto(supplierCode, deliveryNoteSaveParam.getCustomerCode());
        this.saveDeliveryNoteInfo(deliveryNoteSaveParam, loginInfo, orgIdDto,MrpDeliveryNoteTypeEnum.MRP_DELIVERY.getCode());

        // 获取要保存的参数明细
        List<DeliveryDetailVo> deliveryDetailParamList = deliveryNoteSaveParam.getDeliveryDetailList();

        // 保存送货明细
        this.saveMrpDeliveryDetailList( deliveryNoteSaveParam,orgIdDto.getOrgId(),deliveryDetailParamList );

        //记录保存操作记录
        this.saveOperationRecord(deliveryNoteSaveParam, "创建mrp送货单");

        DeliveryNoteVo deliveryNoteVo = BeanUtil.copy(deliveryNoteSaveParam, DeliveryNoteVo.class);
        deliveryNoteVo.setDeliveryDetailList(deliveryDetailParamList);
        return deliveryNoteVo;
    }

    /**
     * 保存mrp送货明细
     * @param deliveryNoteSaveParam
     * @param orgId
     * @param deliveryDetailParamList
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveMrpDeliveryDetailList(DeliveryNoteSaveParam deliveryNoteSaveParam, Long orgId, List<DeliveryDetailVo> deliveryDetailParamList) {

        if( CollectionUtil.isEmpty( deliveryDetailParamList )){
            return;
        }

        // 在送货单保存之前先校验所选择的送货计划的字段
        Asserts.isTrue( deliveryDetailParamList.stream().map( DeliveryDetailVo::getOrderType ).collect(Collectors.toSet()).size() == 1 ,"送货计划订单类型不一致" );
        Asserts.isTrue( deliveryDetailParamList.stream().map( DeliveryDetailVo::getDeliveryUnitName ).collect(Collectors.toSet()).size() == 1 ,"送货计划计划单位不一致"  );
        Asserts.isTrue( deliveryDetailParamList.stream().map( DeliveryDetailVo::getCurrency ).collect(Collectors.toSet()).size() == 1 ,"送货计划结算币别不一致"  );
        Asserts.isTrue( deliveryDetailParamList.stream().map( DeliveryDetailVo::getMaterialClassification ).collect(Collectors.toSet()).size() == 1 ,"送货计划物料分类不一致"  );


        //查询可发数量比例
        BigDecimal ratioOfSentProportion = this.getRatioOfSentProportion(orgId );

        // 扣减项次的剩余发货数量
        int row = 1;

        List<Long> detailIds = deliveryDetailParamList.stream().map(item -> item.getDeliveryPlanDetailItemId()).collect(Collectors.toList());
        R<List<OrderDetailDTO>> listR = zcOrderServiceFeignClient.listMrpDetailByIds(detailIds);
        Asserts.isTrue(listR.isSuccess(),"远程获取采购订单明细失败"+listR.getMsg());

        List<OrderDetailDTO> deliveryDetailParamListForRemote = listR.getData();
        if(CollectionUtil.isEmpty(deliveryDetailParamListForRemote)){
            throw new ApiException(500,"远程调用成功 但获取数据为空");
        }

        Map<Long, OrderDetailDTO> detailForRemoteToMap = deliveryDetailParamListForRemote.stream().collect(Collectors.toMap(OrderDetailDTO::getId, item -> item));
        List<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
        for (DeliveryDetailVo deliveryDetailVo : deliveryDetailParamList) {

            deliveryDetailVo.setMrpStatus(MrpDeliveryNoteTypeEnum.MRP_DELIVERY.getCode());
            // 填写的实际发货数量
            BigDecimal realDeliveryQuantity = deliveryDetailVo.getRealDeliveryQuantity();

            Long deliveryPlanDetailItemId = deliveryDetailVo.getDeliveryPlanDetailItemId();
            Asserts.notNull(deliveryPlanDetailItemId,"送货明细信息未指定送货计划明细的项次！" );

            // 获取当前项次在采购订单明细的信息
            OrderDetailDTO deliveryPlanDetailItem = detailForRemoteToMap.get(deliveryPlanDetailItemId);
            Asserts.notNull(deliveryPlanDetailItem,"id为:"+deliveryPlanDetailItemId+"的明细信息在srm查询不到");

            //DeliveryPlanDetailItem deliveryPlanDetailItem = deliveryPlanDetailItemService.getById( deliveryPlanDetailItemId);
            //Asserts.notNull( deliveryPlanDetailItem,"获取送货计划明细的项次信息失败！" );
            BigDecimal remainingQuantity = new BigDecimal(deliveryPlanDetailItem.getRemainingQuantity());

            // 实际发货的值大于该项次的值
            if( realDeliveryQuantity.compareTo( remainingQuantity ) > 0 ){
                DeliveryDetailUtils.verifyRemainingShippableQuantityAccord( remainingQuantity.intValue(),ratioOfSentProportion,realDeliveryQuantity.longValue(), "第"+ row + "行明细，剩余可发货数量不足！");
            }

            // 当前剩余可发货数 - 实际 < 0 那么 剩余则需要设置为0
            if (new BigDecimal( "0").compareTo( new BigDecimal( deliveryPlanDetailItem.getRemainingQuantity()).subtract( realDeliveryQuantity ) ) > 0 ){
                buildOrderDetailDTO(orderDetailDTOS, deliveryPlanDetailItemId, "0");
            }
            // 否则就扣减数量
            else {
                BigDecimal subtract = remainingQuantity.subtract(realDeliveryQuantity);
                buildOrderDetailDTO(orderDetailDTOS, deliveryPlanDetailItemId, subtract.toString());
                //deliveryPlanDetailItemService.incrRemainingQuantity( deliveryPlanDetailItemId, realDeliveryQuantity.multiply( new BigDecimal("-1")) );
            }

            // 计划单位/送货单位
            deliveryDetailVo.setPlanUnit( deliveryNoteSaveParam.getPlanUnit() );

            row++;
        }

        //批量更新明细
        R<Boolean> RupdateData = zcOrderServiceFeignClient.updateBatchMrpDetailById(orderDetailDTOS);
        Asserts.isTrue(RupdateData.isSuccess(),RupdateData.getMsg());


        // 保存当前明细
        List<DeliveryDetailVo> deliveryDetailVos = deliveryDetailService.saveDetails( deliveryNoteSaveParam.getId(), deliveryDetailParamList);
        //保存明细中的附件  附件信息是前端传入的
        deliveryDetailVos.forEach( detail -> {
            //清除之前的附件关系并建立新的附件关联
            attachmentRelService.clearAndSave(detail.getId(), detail.getAttachmentList());
        });
    }

    private void buildOrderDetailDTO(List<OrderDetailDTO> orderDetailDTOS, Long id, String remainingQuantity) {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setId(id);
        orderDetailDTO.setRemainingQuantity( remainingQuantity );
        orderDetailDTOS.add(orderDetailDTO);
    }

    /**
     * mrp确认发布（把mrp送货单状态改成待签收的状态）
     * @param deliveryNoteSaveParam
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RepeatRequestOperation
    public DeliveryNoteVo mrpSaveAndSign(DeliveryNoteSaveParam deliveryNoteSaveParam) {
        log.info("========================================================");
        log.info("发布开始，数据为：{}",JSON.toJSONString(deliveryNoteSaveParam));
        log.info("========================================================");

        // 发布明细不能为空
        List<DeliveryDetailVo> deliveryDetailList = deliveryNoteSaveParam.getDeliveryDetailList();
        Asserts.isTrue( CollectionUtil.isNotEmpty( deliveryDetailList ),"确认发布要求送货单明细不能为空！" );

        // 保存送货单和送货明细
        DeliveryNoteVo deliveryNoteVo = this.saveMrpDeliveryNote(deliveryNoteSaveParam);

        // 创建收料通知单
        this.createPurchaseReceiveBillCallCreate( deliveryNoteSaveParam, null);

        // 设置成 待签收状态
        deliveryNoteSaveParam.setDeliveryStatusCode(DeliveryStatusEnum.TO_BE_SIGNED.getCode());
        this.updateById( deliveryNoteSaveParam);

        return deliveryNoteVo;
    }

    /**
     * 更新mrp送货单信息
     *
     * @param deliveryNoteSaveParam
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RepeatRequestOperation
    public DeliveryNoteVo updateMrpDeliveryNote(DeliveryNoteSaveParam deliveryNoteSaveParam) {

        log.info("========================================================");
        log.info("修改开始，数据为：{}",JSON.toJSONString(deliveryNoteSaveParam));
        log.info("========================================================");

        //获取当前登录供应商编码
        String supplierCode = getLoginCurrentSupplierCode();

        // 校验供应商合作状态
        this.verifySupplierCooperationStatus(supplierCode);

        Long deliveryNoteId = deliveryNoteSaveParam.getId();
        Asserts.notNull( deliveryNoteId ,"未指定送货单！" );

        // 查询组织ID
        Long orgId = this.remoteGetOrgId(supplierCode, deliveryNoteSaveParam.getCustomerCode() );

        //查询查询出此记录在数据库中没有更改前的数据，如果有erp，同时查询出erpId
        // 送货单信息
        DeliveryNote deliveryNote = this.getById( deliveryNoteSaveParam.getId() );
        Asserts.notNull( deliveryNote,"送货单信息获取失败！" );
        log.info("-------------------erpId----------------------------");
        log.info("deliveryNote info:{}",JSON.toJSONString(deliveryNote));
        log.info("-----------------------------------------------");


        //获取前端传入的送货明细信息
        List<DeliveryDetailVo> deliveryDetailParamList = deliveryNoteSaveParam.getDeliveryDetailList();
        log.info("[updateDeliveryNote] deliveryDetailParamList:{}", JSON.toJSONString( deliveryDetailParamList) );

        // 更新送货单的信息
        this.updateDeliveryNoteInfo(deliveryNoteSaveParam);

        // 待签收状态下可以更新送货明细信息
        // 1、先删除明细，并回滚，后计算
        List<DeliveryDetail> oldDeliveryDetailList = deliveryDetailService.lambdaQuery().eq(DeliveryDetail::getDeliveryId, deliveryNoteId).list();

        // 送货单是待签收状态
        boolean deliveryNoteIsToBeSigned = DeliveryStatusEnum.TO_BE_SIGNED.getCode().equals( deliveryNote.getDeliveryStatusCode());
        if( deliveryNoteIsToBeSigned && CollectionUtil.isEmpty( deliveryDetailParamList ) ){
            throw new ApiException("待签收状态下送货单明细不能为空！");
        }

        // 如果删除了明细或实际发货数量才进行删除后新增，否则直接走数据更新的逻辑
        if ( this.checkDeliveryDetailHaveUpdateRealDeliveryQuantity( oldDeliveryDetailList, deliveryDetailParamList )) {

            log.info("[updateDeliveryNote] 删除了明细或实际发货数量或明细为空或为新增 >> ");

            // 处理已经存在的送货明细  回滚
            this.handleOldMrpDeliveryDetailListFallback(oldDeliveryDetailList);

            // 保存送货明细
            this.saveMrpDeliveryDetailList( deliveryNoteSaveParam, orgId, deliveryDetailParamList );

        }
        // 直接走数据更新的逻辑
        else{
            log.info("[updateDeliveryNote] 直接走数据更新的逻辑 >>" );
            deliveryDetailParamList.forEach( deliveryDetailVo -> {
                Long deliveryDetailId = deliveryDetailVo.getId();
                deliveryDetailVo.setPlanUnit( deliveryNoteSaveParam.getPlanUnit() );
                // 更新送货单明细信息
                deliveryDetailService.updateById( deliveryDetailVo );
                // 附件处理
                attachmentRelService.clearAndSave(deliveryDetailId, deliveryDetailVo.getAttachmentList());
            } );

        }

        // 如果送货单是 待签收，则需要更新ERP
        if( deliveryNoteIsToBeSigned ){

            Asserts.isTrue( CollectionUtil.isNotEmpty(deliveryDetailParamList), "送货单明细不能为空！");

            // 1、校验是否有 erpId
            long count = deliveryDetailParamList.stream().map(DeliveryDetailVo::getErpId).filter(Objects::nonNull).count();
            Asserts.isTrue( count == deliveryDetailParamList.size() ,"待签收状态下不能删除送货单明细！" );

            //2、更新ERP
            Asserts.isTrue( Objects.nonNull( deliveryNote.getErpId() ) ,"mrp送货单无对应ERP的ID！" );

            //3、更新信息 >> 此时是更新信息
            this.createPurchaseReceiveBillCallCreate( deliveryNoteSaveParam ,deliveryNote.getErpId());
        }

        this.saveOperationRecord( deliveryNoteSaveParam, "mrp送货单修改记录");

        //发送消息通知
        this.sendMessageNotice(deliveryNoteSaveParam, orgId);

        DeliveryNoteVo deliveryNoteVo = BeanUtil.copy(deliveryNoteSaveParam, DeliveryNoteVo.class);
        deliveryNoteVo.setDeliveryDetailList(deliveryDetailParamList);
        return deliveryNoteVo;

    }

    /**
     * 处理旧送货明细列表回滚
     * @param oldDeliveryDetailList
     */
    private void handleOldMrpDeliveryDetailListFallback(List<DeliveryDetail> oldDeliveryDetailList) {
        if( CollectionUtil.isNotEmpty( oldDeliveryDetailList ) ){

            List<Long> detailIds = oldDeliveryDetailList.stream().map(item -> item.getDeliveryPlanDetailItemId()).collect(Collectors.toList());
            //Map<Long, DeliveryDetail> deliveryDetailMap = oldDeliveryDetailList.stream().collect(Collectors.toMap(DeliveryDetail::getId, item -> item));
            R<List<OrderDetailDTO>> listDetailListR = zcOrderServiceFeignClient.listMrpDetailByIds(detailIds);
            Asserts.isTrue(listDetailListR.isSuccess(),listDetailListR.getMsg());
            List<OrderDetailDTO> detailListForSelect = listDetailListR.getData();
            Map<Long, OrderDetailDTO> detailListForSelectMap = detailListForSelect.stream().collect(Collectors.toMap(OrderDetailDTO::getId, item -> item));

            // 回退送货计划项次剩余可发货数量
            List<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
            oldDeliveryDetailList.forEach( deliveryDetail -> {
                //orderDetailDTO
                OrderDetailDTO deliveryPlanDetailItem = detailListForSelectMap.get(deliveryDetail.getDeliveryPlanDetailItemId());
                Asserts.notNull(deliveryPlanDetailItem,"此明细:{}"+JSON.toJSONString(deliveryDetail)+"查询不到对应的明细信息");

                BigDecimal addQuantity = new BigDecimal(deliveryPlanDetailItem.getRemainingQuantity()).add(deliveryDetail.getRealDeliveryQuantity());
                // 说明之前是有加上超额的


                if (  addQuantity.compareTo( new BigDecimal( deliveryPlanDetailItem.getDeliveryQuantity()) ) > 0){
                    buildOrderDetailDTO(orderDetailDTOS,deliveryPlanDetailItem.getId(),deliveryPlanDetailItem.getDeliveryQuantity());
                    //DeliveryPlanDetailItem deliveryPlanDetailItemUpdate = new DeliveryPlanDetailItem();
                    //deliveryPlanDetailItemUpdate.setId( deliveryPlanDetailItem.getId() );
                    //deliveryPlanDetailItemUpdate.setRemainingQuantity( deliveryPlanDetailItem.getDeliveryQuantity() );
                    //deliveryPlanDetailItemService.updateById( deliveryPlanDetailItemUpdate ) ;
                }
                // 否则就回滚数量
                else {
                    buildOrderDetailDTO(orderDetailDTOS,deliveryPlanDetailItem.getId(),addQuantity.toString());
                    //deliveryPlanDetailItemService.incrRemainingQuantity( deliveryDetail.getDeliveryPlanDetailItemId(),deliveryDetail.getRealDeliveryQuantity() );
                }

            });

            R<Boolean> booleanR = zcOrderServiceFeignClient.updateBatchMrpDetailById(orderDetailDTOS);
            Asserts.isTrue(booleanR.isSuccess(),"更新mrp采购订单明细数量时出现问题");
            //Asserts.isTrue(booleanR.getData(),"更新mrp采购订单明细数量时出现问题");

            // 删除之前的送货明细
            Set<Long> oldDeliveryDetailIdSet = oldDeliveryDetailList.stream().map(DeliveryDetail::getId).collect(Collectors.toSet());
            deliveryDetailService.removeByIds( oldDeliveryDetailIdSet );
            // 移除附件
            oldDeliveryDetailIdSet.forEach( id -> attachmentRelService.clearAndSave( id,new ArrayList<>() ) );
        }
    }

    /**
     * mrp非待签收下 -- 更新且发布
     * @param deliveryNoteSaveParam
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @RepeatRequestOperation
    public DeliveryNoteVo mrpUpdateSign(DeliveryNoteSaveParam deliveryNoteSaveParam) {
        log.info("========================================================");
        log.info("修改开始，数据为：{}",JSON.toJSONString(deliveryNoteSaveParam));
        log.info("========================================================");

        // 更新送货单信息， 并未创建收料通知单，因为此时送货单还不是 待签收状态
        DeliveryNoteVo deliveryNoteVo = this.updateMrpDeliveryNote(deliveryNoteSaveParam);

        Asserts.isTrue( CollectionUtil.isNotEmpty(deliveryNoteVo.getDeliveryDetailList()),"送货单明细不能为空！");
        // 创建收料通知单
        this.createPurchaseReceiveBillCallCreate( deliveryNoteSaveParam, null);

        // 设置成 待签收状态
        deliveryNoteSaveParam.setDeliveryStatusCode(DeliveryStatusEnum.TO_BE_SIGNED.getCode());
        this.updateById( deliveryNoteSaveParam);

        return deliveryNoteVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteMrpDeliveryNote(Long id) {

        // 1、回滚项次的数据
        DeliveryNote deliveryNote = this.getById(id);
        Asserts.notNull(deliveryNote,"送货单信息已经删除，请刷新后重试！" );

        // 1、先删除明细，并回滚，后计算
        List<DeliveryDetail> oldDeliveryDetailList = deliveryDetailService.lambdaQuery().eq(DeliveryDetail::getDeliveryId, id).list();
        if (CollectionUtil.isNotEmpty( oldDeliveryDetailList)) {
            this.handleOldMrpDeliveryDetailListFallback( oldDeliveryDetailList );
        }

        this.removeById(id);

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DeliveryNoteVo saveMrpDeliveryDetailBatchDetailSave(DeliveryNoteSaveParam deliveryNoteSaveParam) {

        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        //获取当前登录供应商编码
        String supplierCode = getLoginCurrentSupplierCode();
        // 校验供应商合作状态
        this.verifySupplierCooperationStatus( supplierCode );
        // 查询组织ID
        OrgIdDTO orgIdDto = this.getOrgIdDto(supplierCode, deliveryNoteSaveParam.getCustomerCode());

        // 获取送货单保存参数的ID
        Long deliveryNoteSaveParamId = deliveryNoteSaveParam.getId();
        // 更新送货单
        boolean isUpdateDeliveryNote = Objects.nonNull(deliveryNoteSaveParamId);
        if( isUpdateDeliveryNote ){
            // 更新送货单的信息
            this.updateDeliveryNoteInfo(deliveryNoteSaveParam);
            // 记录
            this.saveOperationRecord( deliveryNoteSaveParam, "采购送货单修改记录");
        }
        else{
            // 保存送货单信息
            this.saveDeliveryNoteInfo( deliveryNoteSaveParam,loginInfo,orgIdDto ,MrpDeliveryNoteTypeEnum.MRP_DELIVERY.getCode());
            // 赋值
            deliveryNoteSaveParamId = deliveryNoteSaveParam.getId();
            Asserts.notNull( deliveryNoteSaveParamId,"保存采购送货单信息失败！" );
            //记录保存操作记录
            this.saveOperationRecord(deliveryNoteSaveParam, "创建采购送货单");
        }

        // 保存批次明细 明细的
        List<DeliveryDetailVo> deliveryDetailVoList = deliveryNoteSaveParam.getDeliveryDetailList();
        Asserts.isTrue( CollectionUtil.isNotEmpty( deliveryDetailVoList ),"分批明细不能为空！" );

        Long deliveryPlanDetailItemId = deliveryDetailVoList.get(0).getDeliveryPlanDetailItemId();
        Asserts.notNull( deliveryPlanDetailItemId,"采购送货明细项次信息未关联！" );


        R<OrderDetailDTO> mrpDetailByIdRData = zcOrderServiceFeignClient.getMrpDetailById(deliveryPlanDetailItemId);
        Asserts.isTrue(mrpDetailByIdRData.isSuccess(),mrpDetailByIdRData.getMsg());
        OrderDetailDTO deliveryPlanDetailItem = mrpDetailByIdRData.getData();
        //DeliveryPlanDetailItem deliveryPlanDetailItem = deliveryPlanDetailItemService.getById(deliveryPlanDetailItemId);
        Asserts.notNull( deliveryPlanDetailItem ,"采购订单的送货明细项次信息获取失败！");

        // 先删除当前送货单与之前关联的 明细，然后重新绑定。
        if( isUpdateDeliveryNote ){

            // 构建条件
            LambdaQueryWrapper<DeliveryDetail> queryWrapper = Wrappers.<DeliveryDetail>lambdaQuery().eq(DeliveryDetail::getDeliveryId, deliveryNoteSaveParamId).eq(DeliveryDetail::getDeliveryPlanDetailItemId, deliveryNoteSaveParamId);
            // 之前存在表里的 明细 当前项次与当前送货单
            List<DeliveryDetail> oldDeliveryDetailList = deliveryDetailService.list(queryWrapper);
            // 处理已经存在的送货明细  回滚
            this.handleOldMrpDeliveryDetailListFallback( oldDeliveryDetailList);

        }

        // 保存送货明细
        this.saveMrpDeliveryDetailList( deliveryNoteSaveParam, orgIdDto.getOrgId(),deliveryDetailVoList );

        // 后置处理
        if( isUpdateDeliveryNote){
            //发送消息通知
            this.sendMessageNotice(deliveryNoteSaveParam, orgIdDto.getOrgId() );
        }

        DeliveryNoteVo deliveryNoteVo = BeanUtil.copy(deliveryNoteSaveParam, DeliveryNoteVo.class);
        return deliveryNoteVo;
    }

    /**
     * 获取mrp分批详情
     * @param deliveryDetailBatchDetailParams
     * @return
     */
    @Override
    public MrpDeliveryOrderDetailVo getMrpDeliveryDetailBatchDetail(DeliveryDetailBatchDetailParams deliveryDetailBatchDetailParams) {

        // 送货计划明细项次
        Long deliveryPlanDetailItemId = deliveryDetailBatchDetailParams.getDeliveryPlanDetailItemId();

        //获取采购订单明细
        R<OrderDetailDTO> mrpDetailByIdRData = zcOrderServiceFeignClient.getMrpDetailById(deliveryPlanDetailItemId);
        Asserts.isTrue(mrpDetailByIdRData.isSuccess(),mrpDetailByIdRData.getMsg());
        OrderDetailDTO deliveryPlanDetailItem = mrpDetailByIdRData.getData();
        //DeliveryPlanDetailItem deliveryPlanDetailItem = deliveryPlanDetailItemService.getById( deliveryPlanDetailItemId);
        Asserts.notNull( deliveryPlanDetailItem,"获取采购订单明细项次信息失败！" );

        //DeliveryPlanDetail deliveryPlanDetail = deliveryPlanDetailService.getById(deliveryPlanDetailItem.getDeliveryPlanDetailId());
        //Asserts.notNull( deliveryPlanDetail,"获取送货计划明细信息失败！" );
        //通过采购单号获取采购订单
        R<OrderForDeliveryInfoDTO> purchaseOrderByCodeRData = zcOrderServiceFeignClient.getPurchaseOrderByCode(deliveryPlanDetailItem.getPurchaseOrderCode());
        Asserts.isTrue(purchaseOrderByCodeRData.isSuccess(),purchaseOrderByCodeRData.getMsg());
        OrderForDeliveryInfoDTO order = purchaseOrderByCodeRData.getData();
        Asserts.notNull(order,"获取采购订单为空! ");


        // 建立返回对象
        MrpDeliveryOrderDetailVo deliveryDetailBatchDetailVo = new MrpDeliveryOrderDetailVo();
        //BeanUtils.copyProperties( order,deliveryDetailBatchDetailVo );


        //DeliveryDetailBatchDetailVo deliveryDetailBatchDetailVo = new DeliveryDetailBatchDetailVo();
        //BeanUtils.copyProperties( deliveryPlanDetailItem,deliveryDetailBatchDetailVo );
        // 送货计划单号
        deliveryDetailBatchDetailVo.setDeliveryPlanNo( deliveryPlanDetailItem.getPlanNo() );
        // 订单单号
        deliveryDetailBatchDetailVo.setBillNo( deliveryPlanDetailItem.getPurchaseOrderCode() );
        // 订单编号
        deliveryDetailBatchDetailVo.setPurchaseOrderNo( order.getOrderNo() );
        // 产品编码
        deliveryDetailBatchDetailVo.setProductCode( deliveryPlanDetailItem.getProductCode() );
        // 条码
        deliveryDetailBatchDetailVo.setMerchantCode( deliveryPlanDetailItem.getMerchantCode() );
        // 产品名称
        deliveryDetailBatchDetailVo.setProductName( deliveryPlanDetailItem.getProductName() );
        // 送货日期
        //Optional.ofNullable(deliveryPlanDetailItem.getDeliveryDate()).ifPresent( deliveryLoclDateTime -> deliveryDetailBatchDetailVo.setDeliveryDate( Date.from( deliveryLoclDateTime.atZone( ZoneId.systemDefault()).toInstant() ) ));
        deliveryDetailBatchDetailVo.setDeliveryDate(deliveryPlanDetailItem.getDeliveryTime());
        //剩余数量
        deliveryDetailBatchDetailVo.setRemainingQuantity(deliveryPlanDetailItem.getRemainingQuantity());
        //计划数量
        deliveryDetailBatchDetailVo.setDeliveryQuantity(deliveryPlanDetailItem.getDeliveryQuantity());
        //id设置
        deliveryDetailBatchDetailVo.setId(deliveryPlanDetailItem.getId());
        //仓库
        deliveryDetailBatchDetailVo.setTakeOverWarehouse(deliveryPlanDetailItem.getTakeOverWarehouse());
        //地址
        deliveryDetailBatchDetailVo.setDeliveryAddress(deliveryPlanDetailItem.getDeliveryAddress());


        Long deliveryNoteId = deliveryDetailBatchDetailParams.getDeliveryNoteId();
        if ( Objects.nonNull( deliveryNoteId )) {

            DeliveryNote deliveryNote = this.getById(deliveryNoteId);
            Asserts.notNull( deliveryNote,"获取送货单信息失败！" );

            // 查询送货单该项次的明细
            List<DeliveryDetail> deliveryDetailList = deliveryDetailService.lambdaQuery().eq(DeliveryDetail::getDeliveryId, deliveryNoteId).eq(DeliveryDetail::getDeliveryPlanDetailItemId, deliveryPlanDetailItemId).list();

            List<DeliveryDetailVo> detailVos = BeanUtil.copy(deliveryDetailList, DeliveryDetailVo.class);
            // 设置附件
            this.batchSetDeliveryDetailAttachment( detailVos);
            // 剩余可发货数量增加回显示
            for (DeliveryDetailVo deliveryDetailVo : detailVos) {
                deliveryDetailBatchDetailVo.addRemainingQuantity( deliveryDetailVo.getRealDeliveryQuantity() );
            }

            deliveryDetailBatchDetailVo.setDeliveryDetailList( detailVos );

        }

        return deliveryDetailBatchDetailVo;

    }

}
