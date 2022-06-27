package net.bncloud.quotation.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.service.base.domain.SendMsgParam;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;
import net.bncloud.event.SmsBizEvent;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.quotation.config.ExpirationTimeConfig;
import net.bncloud.quotation.config.SmsMsgTempConfig;
import net.bncloud.quotation.entity.*;
import net.bncloud.quotation.enums.EventParamsTypeEnum;
import net.bncloud.quotation.enums.QuotationAttBizEnum;
import net.bncloud.quotation.enums.QuotationStatusEnum;
import net.bncloud.quotation.event.vo.*;
import net.bncloud.quotation.mapper.PricingRecordMapper;
import net.bncloud.quotation.param.PricingRecordParam;
import net.bncloud.quotation.service.*;
import net.bncloud.quotation.vo.*;
import net.bncloud.service.api.file.dto.FileInfoDto;
import net.bncloud.service.api.file.feign.FileCenterFeignClient;
import net.bncloud.service.api.information.feign.InformationSMSClient;
import net.bncloud.service.api.platform.supplier.feign.SaasClient;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 定价请示记录信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
@Slf4j
public class PricingRecordServiceImpl extends BaseServiceImpl<PricingRecordMapper, PricingRecord> implements PricingRecordService {

	@Autowired
	private ExpirationTimeConfig expirationTimeConfig;

	@Autowired
	private SmsMsgTempConfig smsMsgTempConfig;

	@Autowired
	private IQuotationOperationLogService operationLogService;

	@Autowired
	private PricingRemarkService pricingRemarkService;

	@Autowired
	private QuotationBaseService quotationBaseService;

	@Resource
	private DefaultEventPublisher defaultEventPublisher;

	@Autowired
	private QuotationSupplierService quotationSupplierService;

	@Autowired
	private FileCenterFeignClient fileCenterFeignClient;

	@Autowired
	private QuotationAttachmentService quotationAttachmentService;

	@Autowired
	private SaasClient sasClient;

	@Resource
	@Lazy
    private InformationSMSClient informationSMSClient;

	@Autowired
	private ITRfqVerificationCodeService iTRfqVerificationCodeService;

	@Autowired
	private BidResponseService bidResponseService;



	@Override
	public IPage<PricingRecord> selectPage(IPage<PricingRecord> page, QueryParam<PricingRecordParam> pageParam) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		//notice.setTenantId(SecureUtil.getTenantId());
		return page.setRecords(baseMapper.selectListPage(page, pageParam));
	}

	@Override
	@Transactional
//	@OperationLog(code = "quotation_save_pricing",content = "询价单定价")
	public void savePricingInfo(PricingRecordAndRemark pricingRecordAndRemark) {
		pricingRecordAndRemark.setQuotationBaseId(pricingRecordAndRemark.getQuotationId());
		//进入定价页面点击确认按钮时没有选择供应商
		if( ObjectUtil.isEmpty(pricingRecordAndRemark.getPricingRecords()) ){
			throw new ApiException(500,"请选择供应商后保存定价");
		}

		//构造份额
		List<PricingRecordVo> undefined = pricingRecordAndRemark.getUndefined();
		Map<String, BigDecimal> amountMap = new HashMap<>();
		undefined.forEach(item->{
			amountMap.put(item.getSupplierId(),item.getQuoteAmount());
		});

		//保存份额
		pricingRecordAndRemark.getPricingRecords().stream().forEach(record ->{
			record.setQuotationBaseId(Long.valueOf(pricingRecordAndRemark.getQuotationBaseId()));
			LambdaQueryWrapper<PricingRecord> query = Condition.getQueryWrapper(new PricingRecord())
					.lambda()
					.eq(PricingRecord::getId, record.getId());
			List<PricingRecord> pricingRecords = this.getBaseMapper().selectList(query);
			if(pricingRecords!=null&&pricingRecords.size()>0){
				throw new ApiException(500,"已经保存过定价信息了");
			}
			//设置份额
			record.setQuoteAmount(amountMap.get(record.getSupplierId()));

			save(record);
		});

		//保存评论
		PricingRemark pricingRemark = new PricingRemark();
		pricingRemark.setPricingRemark(pricingRecordAndRemark.getPricingRemark());
		pricingRemark.setQuotationBaseId(Long.valueOf(pricingRecordAndRemark.getQuotationBaseId()));
		pricingRemarkService.save(pricingRemark);

		//保存定价单上传附件信息
		saveAttachmentInfo(pricingRecordAndRemark.getAttachmentList(),String.valueOf(pricingRecordAndRemark.getQuotationBaseId()));

		//修改询价单状态为已定价
		LambdaUpdateWrapper<QuotationBase> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.set(QuotationBase::getQuotationStatus,QuotationStatusEnum.HAVE_PRICING.getCode())
				.eq(QuotationBase::getId,pricingRecordAndRemark.getQuotationId());
		quotationBaseService.update(updateWrapper);

		//给供应商发送站内消息/短信
		sendWebInfo(pricingRecordAndRemark);

		BaseUserEntity user = AuthUtil.getUser();
		Long userId = user.getUserId();
		String userName = user.getUserName();
//		LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
//		Long id = loginInfo.getCurrentOrg().getCompanyId();
//		String name = loginInfo.getCurrentOrg().getCompanyName();
		//操作完成记录日志
		QuotationOperationLog operationLog = QuotationOperationLog.builder()
				.billId(Long.valueOf(pricingRecordAndRemark.getQuotationId()))
				.content("询价单定价")
				.operationNo(userId)
				.operatorName(userName)
				.build();
		operationLog.setCreatedDate(new Date());
		operationLogService.save(operationLog);
	}

	public void sendWebInfo(PricingRecordAndRemark pricingRecordAndRemark){
		LambdaQueryWrapper<QuotationSupplier> quotationSupplierQueryWrapper = Condition.getQueryWrapper(new QuotationSupplier())
				.lambda().eq(QuotationSupplier::getQuotationBaseId, pricingRecordAndRemark.getQuotationBaseId());
		List<QuotationSupplier> quotationSupplierLists = quotationSupplierService.list(quotationSupplierQueryWrapper);
		//中标者
		List<QuotationSupplier>  winners = filterWinner(quotationSupplierLists,pricingRecordAndRemark.getPricingRecords());
		//失败者
		List<QuotationSupplier>  losers =  filterLosers(quotationSupplierLists,pricingRecordAndRemark.getPricingRecords());
		//发送中标信息
		sendEventMsgAndSms(winners,pricingRecordAndRemark.getQuotationBaseId(),true);
		//发送失败信息
		sendEventMsgAndSms(losers,pricingRecordAndRemark.getQuotationBaseId(),false);
	}

	public void sendEventMsgAndSms(List<QuotationSupplier>  quotationSuppliers,String quotationBaseId,Boolean isSuccess){
		QuotationBase quotationBase = quotationBaseService.getById(Long.valueOf(quotationBaseId));
		Map<String,Object> params =  new HashMap<>();
		params.put("inquiryNo",quotationBase.getQuotationNo());
		quotationSuppliers.stream().forEach(quotationSupplier -> {
			if(isSuccess) {
				QuotationInfoEventData quotationInfoEventData = convertEvent(quotationSupplier.getSupplierCode(),quotationSupplier.getSupplierName(),
						smsMsgTempConfig.getSupplierNotificationOfAwardResults(), convertSmsMsg(params), quotationBase);
				BizEvent<QuotationBase> quotationBaseBizEvent = convertMsgEvent(quotationInfoEventData, EventCode.quotation_supplier_winner.getCode());
				SmsBizEvent<QuotationBase> quotationBaseSmsBizEvent = convertSmsEvent(quotationInfoEventData, EventCode.quotation_winner_sms.getCode());
				//发送事件消息
				sendEvent(quotationBaseBizEvent);
				//发送短信事件消息
				sendSmsEvent(quotationBaseSmsBizEvent);
			}else{
				QuotationInfoEventData quotationInfoEventData = convertEvent(quotationSupplier.getSupplierCode(),quotationSupplier.getSupplierName(),
						smsMsgTempConfig.getSupplierNotificationOfUnsuccessfulResults(), convertSmsMsg(params), quotationBase);
				BizEvent<QuotationBase> quotationBaseBizEvent = convertMsgEvent(quotationInfoEventData, EventCode.quotation_supplier_loser.getCode());
				SmsBizEvent<QuotationBase> quotationBaseSmsBizEvent = convertSmsEvent(quotationInfoEventData, EventCode.quotation_loser_sms.getCode());
				//发送事件消息
				sendEvent(quotationBaseBizEvent);
				//发送短信事件消息
				sendSmsEvent(quotationBaseSmsBizEvent);
			}
		});
	}

	public QuotationInfoEventData convertEvent(String  supplierCode,String supplierName,
							 String smsTmpCode,String msg,QuotationBase quotationBase){
		//发送消息通知
		LoginInfo loginInfo = getLoginInfo();
		QuotationInfoEventData quotationInfoEventData = new QuotationInfoEventData();
		if(loginInfo != null){
			BeanUtil.copyProperties(quotationBase,quotationInfoEventData);
			quotationInfoEventData.setSupplierCode(supplierCode);
			quotationInfoEventData.setSupplierName(supplierName);
			quotationInfoEventData.setOrgId(loginInfo.getCurrentOrg().getId());
			quotationInfoEventData.setSmsMsgType(1);
			quotationInfoEventData.setSmsParams(msg);
			quotationInfoEventData.setSmsTempCode(smsTmpCode);
		}
		return quotationInfoEventData;
	}

	public BizEvent<QuotationBase> convertMsgEvent(QuotationInfoEventData quotationInfoEventData, String key){
		if (quotationInfoEventData!=null){
			quotationInfoEventData.setBusinessId(quotationInfoEventData.getId());
		}
		//发送消息和代办
		LoginInfo loginInfo = getLoginInfo();
		//发送中标事件
		if(key.equals(EventCode.quotation_supplier_winner.getCode())){
			return new QuotationWinnerInfoEvent(this,loginInfo,quotationInfoEventData,quotationInfoEventData.getCustomerCode(),quotationInfoEventData.getCustomerName());
			//发送未中标事件
		}else if(key.equals(EventCode.quotation_supplier_loser.getCode())){
			return new QuotationLoserInfoEvent(this,loginInfo,quotationInfoEventData,quotationInfoEventData.getCustomerCode(),quotationInfoEventData.getCustomerName());
			//询价单重报站内信
		}else if(key.equals(EventCode.quotation_supplier_restate.getCode())){
			return new QuotationRestateInfoEvent(this,loginInfo,quotationInfoEventData,quotationInfoEventData.getCustomerCode(),quotationInfoEventData.getCustomerName());
		}else{
			return new QuotationSupplierInfoEvent(this,loginInfo,quotationInfoEventData,quotationInfoEventData.getCustomerCode(),quotationInfoEventData.getCustomerName());
		}
	}

	public SmsBizEvent<QuotationBase> convertSmsEvent(QuotationInfoEventData quotationInfoEventData, String key){
		//发送短信
		LoginInfo loginInfo = getLoginInfo();
		if (key.equals(EventCode.quotation_loser_sms.getCode())){
			return new QuotationLoserInfoSmsEvent(this,loginInfo,quotationInfoEventData,quotationInfoEventData.getCustomerCode(),quotationInfoEventData.getCustomerName());
		}else if (key.equals(EventCode.quotation_winner_sms.getCode())){
			return new QuotationWinnerInfoSmsEvent(this,loginInfo,quotationInfoEventData,quotationInfoEventData.getCustomerCode(),quotationInfoEventData.getCustomerName());
		}else if (key.equals(EventCode.quotation_restate_sms.getCode())){
			return new QuotationRestateInfoSmsEvent(this,loginInfo,quotationInfoEventData,quotationInfoEventData.getCustomerCode(),quotationInfoEventData.getCustomerName());
		}else{
			return new QuotationSupplierInfoSmsEvent(this,loginInfo,quotationInfoEventData,quotationInfoEventData.getCustomerCode(),quotationInfoEventData.getCustomerName());
		}
	}

	public void sendEvent(BizEvent<QuotationBase> biz){
		//发送事件通知
		defaultEventPublisher.publishEvent(biz);
	}

	public void sendSmsEvent(SmsBizEvent<QuotationBase> biz){
		//发送事件通知
		defaultEventPublisher.publishEvent(biz);
	}

    @Override
    public void sendRestateMsg(List<QuotationSupplier> quotationSuppliers, Long quotationBaseId) {
        quotationSuppliers.stream().forEach(quotationSupplier -> {
            //发送消息通知
            LoginInfo loginInfo = getLoginInfo();
            if (loginInfo != null) {

				QuotationBase quotationBase = quotationBaseService.getById(Long.valueOf(quotationBaseId));
				Map<String,Object> params =  new HashMap<>();
				params.put("inquiryNo",quotationBase.getQuotationNo());
//				QuotationInfoEventData quotationInfoEventData = convertEvent(quotationSupplier.getSupplierCode(),
//						SmsMsgTempEnum.SMS_235496390.getCode(), convertSmsMsg(params), quotationBase);
				QuotationInfoEventData quotationInfoEventData = convertEvent(quotationSupplier.getSupplierCode(),quotationSupplier.getSupplierName(),
						smsMsgTempConfig.getSupplierReQuotationNotice(), convertSmsMsg(params), quotationBase);
				quotationInfoEventData.setSupplierName(quotationSupplier.getSupplierName());
//				QuotationLoserInfoEvent quotationLoserInfoEvent = (QuotationLoserInfoEvent)convertMsgEvent(quotationInfoEventData, EventParamsTypeEnum.loser_event.getCode());
				//消息和代办
				QuotationRestateInfoEvent quotationRestateInfoEvent = (QuotationRestateInfoEvent)convertMsgEvent(quotationInfoEventData, EventCode.quotation_supplier_restate.getCode());
				//短信
				QuotationRestateInfoSmsEvent quotationRestateInfoSmsEvent = (QuotationRestateInfoSmsEvent)convertSmsEvent(quotationInfoEventData, EventCode.quotation_restate_sms.getCode());
				//发送事件消息
				sendEvent(quotationRestateInfoEvent);
				//发送短信事件消息
				sendSmsEvent(quotationRestateInfoSmsEvent);

            }
        });
    }


	public String convertSmsMsg(Map<String,Object> params){
		return JSON.toJSONString(params);
	}

	public List<QuotationSupplier> filterLosers(List<QuotationSupplier> quotationSuppliers,List<PricingRecordVo> pricingRecords){
		Iterator<QuotationSupplier> iterator = quotationSuppliers.iterator();
		while (iterator.hasNext()){
			QuotationSupplier next = iterator.next();
			pricingRecords.stream().forEach(record -> {
				if(next.getSupplierId().toString().equals(record.getSupplierId())){
					iterator.remove();
				}
			});
		}
		return quotationSuppliers;
	}

	public List<QuotationSupplier> filterWinner(List<QuotationSupplier> quotationSuppliers,List<PricingRecordVo> pricingRecords){
		//中标者
		List<QuotationSupplier>  winners = new ArrayList<>();
		quotationSuppliers.stream().forEach(quotationSupplier->{
			pricingRecords.stream().forEach(record-> {
				if(record.getSupplierId().
						equals(quotationSupplier.getSupplierId().toString())){
					winners.add(quotationSupplier);
				}
			});
		});
		return winners;
	}
	public void saveAttachmentInfo(List<QuotationAttachmentVo> quotationAttachmentsList,String id){
		if(quotationAttachmentsList != null && !quotationAttachmentsList.isEmpty()){
			List<QuotationAttachment> quotationAttachmentsForList = new ArrayList<>();
			for (QuotationAttachmentVo quotationAttachmentVo : quotationAttachmentsList){

				QuotationAttachment quotationAttachment = new QuotationAttachment();

				quotationAttachment.setBusinessType(QuotationAttBizEnum.QUOTATION_PRICING.getCode());
				quotationAttachment.setBusinessFormId(Long.valueOf(id));
				quotationAttachment.setFileId(quotationAttachmentVo.getId());
				quotationAttachment.setFileType(quotationAttachmentVo.getContentType());
				quotationAttachment.setFileName(quotationAttachmentVo.getOriginalFilename());
				quotationAttachment.setFileUrl(quotationAttachmentVo.getUrl());

				BaseUserEntity user = AuthUtil.getUser();
				quotationAttachment.setCreatedBy(user.getUserId());
				quotationAttachment.setCreatedByName(user.getUserName());
				quotationAttachment.setLastModifiedBy(user.getUserId());

				quotationAttachmentsForList.add(quotationAttachment);
			}
			quotationAttachmentService.saveBatch(quotationAttachmentsForList);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<QuotationAttachment> saveFile(MultipartFile[] files) throws Exception {
		List<QuotationAttachment> quotationAttachmentList = new ArrayList<>();
		R<List<FileInfoDto>> resultFileList = fileCenterFeignClient.upload(files);
		if(R.success().isSuccess()){
			List<FileInfoDto> fileInfoDtos = resultFileList.getData();
			for (FileInfoDto fileInfoDto : fileInfoDtos) {
				QuotationAttachment quotationAttachment = new QuotationAttachment();
				quotationAttachment.setFileId(fileInfoDto.getId());
				quotationAttachment.setFileUrl(fileInfoDto.getUrl());
				quotationAttachment.setFileName(fileInfoDto.getFilename());
				quotationAttachment.setFileType(fileInfoDto.getContentType());
				quotationAttachmentList.add(quotationAttachment);
			}
		}else{
			throw new Exception("feign client error");
		}
		return quotationAttachmentList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
//	@OperationLog(code = "quotation_verify_open_pricing",content = "询价单开标")
	public Boolean verifyOpenPricingInfo(TRfqVerificationCodeVo tRfqVerificationCodeVo) {
		Boolean isValable = true;
		Integer count=0;
		//判断是否需要开标
		String id = tRfqVerificationCodeVo.getQuotationId();
		QuotationBase quotationBaseForLogic = quotationBaseService.getBaseMapper().selectById(id);
		if(quotationBaseForLogic.getNeedOpenBid().equals("0")){//不需要开标
			isValable=true;
		}else{
			//需要开标
			if(ObjectUtil.isNotEmpty(tRfqVerificationCodeVo.getTRfqVerificationCode())){
				for (TRfqVerificationCode tRfqVerificationCode : tRfqVerificationCodeVo.getTRfqVerificationCode()){
					++count;
					isValable = isAvailable(tRfqVerificationCode,count);
					if(!isValable){
						if(count == 1){
							throw new ApiException(500,"采购人员验证码错误");
						}else if(count ==2){
							throw new ApiException(500,"财务人员验证码错误");
						}else if(count==3){
							throw new ApiException(500,"审计人员验证码错误");
						}else{
							throw new ApiException(500,"验证码错误");
						}

					}
				}
			}
		}

		if(isValable){
			String quotationId = tRfqVerificationCodeVo.getQuotationId();
			QuotationBase quotationBase = new QuotationBase();
			quotationBase.setId(Long.valueOf(quotationId));
			quotationBase.setQuotationStatus(QuotationStatusEnum.COMPARISON.getCode());
			quotationBaseService.updateById(quotationBase);

			BaseUserEntity user = AuthUtil.getUser();
			Long userId = user.getUserId();
			String userName = user.getUserName();
			//操作完成记录日志
			QuotationOperationLog operationLog = QuotationOperationLog.builder()
					.billId(Long.valueOf(tRfqVerificationCodeVo.getQuotationId()))
					.content("询价单开标确定")
					.operationNo(userId)
					.operatorName(userName)
					.build();
			operationLog.setCreatedDate(new Date());
			operationLogService.save(operationLog);

		}



		return isValable;
	}


	public Boolean isAvailable(TRfqVerificationCode tRfqVerificationCode,Integer count) {
		count++;
		Long uid = tRfqVerificationCode.getUid();

//		String verifyCode = generateVerifyCode();
//		String uid = tRfqVerificationCodeVo.getUid();
		R<Map<String, Object>> userInfoByUid = sasClient.getUserInfoByUid(String.valueOf(uid));
		if(!userInfoByUid.isSuccess()){
			log.error("获取供应商电话出现异常,{}",JSON.toJSONString(userInfoByUid));
			throw new RuntimeException("获取供应商电话出现异常");
		}
		String mobile =(String) userInfoByUid.getData().get("mobile");
		Wrapper<TRfqVerificationCode> query = new QueryWrapper<TRfqVerificationCode>().lambda().
				eq(TRfqVerificationCode :: getMobile,mobile)
				.eq(TRfqVerificationCode :: getVerificationCode,tRfqVerificationCode.getVerificationCode())
				.eq(TRfqVerificationCode :: getQuotationId,tRfqVerificationCode.getQuotationId());

		//可能有多条的验证码的情况，区分
		List<TRfqVerificationCode> list = iTRfqVerificationCodeService.list(query);
		if(list != null && !list.isEmpty()){
			for (TRfqVerificationCode t : list){
				Date effectiveTime = t.getEffectiveTime();
				Date expirationTime = t.getExpirationTime();
//				OffsetDateTime now = OffsetDateTime.now();
				Date now = new Date();
				if(now.compareTo(effectiveTime) > 0 && now.compareTo(expirationTime) < 0){
					return true;
				}
			}
		}
		return false;
	}


	@Override
	public Boolean sendVerifyCode(TRfqVerificationCodeVo tRfqVerificationCodeVo) {
		//判断是否可以提前开标
		String quotationId = tRfqVerificationCodeVo.getQuotationId();
		QuotationBase quotationBase = new QuotationBase();
		quotationBase.setId(Long.valueOf(quotationId));
		LambdaQueryWrapper<QuotationBase> lambda = Condition.getQueryWrapper(quotationBase).lambda();
		QuotationBase quotationBaseForSelect = quotationBaseService.getBaseMapper().selectOne(lambda);
		String needOpenBid = quotationBaseForSelect.getNeedOpenBid();
		Date cutOffTime = quotationBaseForSelect.getCutOffTime();//截止时间
		//可以提前开标，修改截止时间
		Date now = new Date();
		if(needOpenBid.equals("1")&&now.compareTo(cutOffTime)<=0){
			updateCutOffTime(now, quotationId);
		}

		String verifyCode = generateVerifyCode();
		String uid = tRfqVerificationCodeVo.getUid();
		R<Map<String, Object>> userInfoByUid = sasClient.getUserInfoByUid(uid);
		if(!userInfoByUid.isSuccess()){
			throw new RuntimeException("系统查询不到当前用户");
		}
		if(userInfoByUid.isSuccess()){
//		if("200".equals(userInfoByUid.getCode()) && userInfoByUid.getData()!=null
//				&& !userInfoByUid.getData().isEmpty()){

			sendVerifyCodeSms(userInfoByUid.getData(),verifyCode,tRfqVerificationCodeVo.getQuotationId());
			//保存验证码信息
			saveSendVerifyCode(userInfoByUid.getData(), verifyCode,tRfqVerificationCodeVo.getQuotationId(),tRfqVerificationCodeVo.getMemberType());
		}
		return true;
	}

	/**
	 * 提前开标时当前时间小于截止时间，修改截止时间方法
	 **/
	private void updateCutOffTime(Date now, String quotationId) {
//		QuotationBase quotationBase = new QuotationBase();
//		quotationBase.setCutOffTime(now);
//		quotationBase.setId(Long.valueOf(quotationId));


		LambdaUpdateWrapper<QuotationBase> quotationBaseLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		quotationBaseLambdaUpdateWrapper.eq(QuotationBase::getId,quotationId).set(QuotationBase::getCutOffTime,now);
		quotationBaseService.update(quotationBaseLambdaUpdateWrapper);
	}


	@Override
	public Boolean sendVerifyCode2Supplier(TRfqVerificationCodeVo tRfqVerificationCodeVo) {
		String quotationId = tRfqVerificationCodeVo.getQuotationId();
		QuotationBase quotationBase = quotationBaseService.getById(Long.valueOf(quotationId));
		Map<String,Object> params =  new HashMap<>();
		params.put("inquiryNo",quotationBase.getQuotationNo());
		params.put("companyName",quotationBase.getCustomerName());
		QuotationInfoEventData quotationInfoEventData = convertEvent(tRfqVerificationCodeVo.getSupplierCode(),tRfqVerificationCodeVo.getSupplierName(),
				smsMsgTempConfig.getSupplierPleaseInformOfInquiry(), convertSmsMsg(params), quotationBase);
		quotationInfoEventData.setSupplierName(tRfqVerificationCodeVo.getSupplierName());
		BizEvent<QuotationBase> quotationBaseBizEvent = convertMsgEvent(quotationInfoEventData, EventCode.quotation_supplier_notice_bid.getCode());
		SmsBizEvent<QuotationBase> quotationBaseSmsBizEvent = convertSmsEvent(quotationInfoEventData, EventParamsTypeEnum.quotation_supplier_event_sms.getCode());
		//1：短信
		//2：站内信
		if(tRfqVerificationCodeVo.getMessageType().getValue().equals("1")){
			log.info("通知供应商应标发送短信----消息体为:{}",JSON.toJSONString(quotationBaseSmsBizEvent));
			//发送短信事件消息
			sendSmsEvent(quotationBaseSmsBizEvent);
		}else{
			log.info("通知供应商应标发送站内信");
			//发送事件消息
			sendEvent(quotationBaseBizEvent);
		}
		return true;
	}

	public void sendVerifyCodeSms(Map<String, Object> userInfoByUid,String verifyCode,String quotationId) {
		List<Map<String,Object>> userInfos = new ArrayList<>();
		userInfos.add(userInfoByUid);
		SendMsgParam sendMsgParam  = new SendMsgParam();
//		sendMsgParam.setSmsTempCode(SmsMsgTempEnum.SMS_235491617.getCode());
		sendMsgParam.setSmsTempCode(smsMsgTempConfig.getPurchaserBidOpeningVerification());
		Map<String,Object> params = new HashMap<>();
		params.put("idenfityCode",verifyCode);
		sendMsgParam.setSmsParams(convertSmsMsg(params));
		sendMsgParam.setSmsMsgType(2);
		sendMsgParam.setUserInfos(userInfos);

		QuotationBaseVo infoById = quotationBaseService.getInfoById(Long.valueOf(quotationId));
		DataJsonVo dataJsonVo = new DataJsonVo();
		dataJsonVo.setQuotationId(quotationId);
		dataJsonVo.setQuotationNo(infoById.getQuotationNo());
		String s = JSON.toJSONString(dataJsonVo);
		sendMsgParam.setDataJson(s);

		informationSMSClient.save(sendMsgParam);
	}

	/**
	 *
	 * 开标发送短信versionSecond
	 *
	 * **/
	public void sendVerifyCodeSmsVs(Map<String, Object> userInfoByUid,String verifyCode) {
		List<Map<String,Object>> userInfos = new ArrayList<>();
		userInfos.add(userInfoByUid);
		SendMsgParam sendMsgParam  = new SendMsgParam();
//		sendMsgParam.setSmsTempCode(SmsMsgTempEnum.SMS_235491617.getCode());
		sendMsgParam.setSmsTempCode(smsMsgTempConfig.getPurchaserBidOpeningVerification());
		Map<String,Object> params = new HashMap<>();
		params.put("idenfityCode",verifyCode);
		sendMsgParam.setSmsParams(convertSmsMsg(params));
		sendMsgParam.setSmsMsgType(2);
		sendMsgParam.setUserInfos(userInfos);
		informationSMSClient.save(sendMsgParam);
	}


	public void saveSendVerifyCode(Map<String, Object> userInfoByUid,String verifyCode,String quotationId,String memberType) {
		TRfqVerificationCode tRfqVerificationCode = new TRfqVerificationCode();
		String uid =(String)userInfoByUid.get("uid");
		tRfqVerificationCode.setUid(Long.valueOf(uid));
		tRfqVerificationCode.setQuotationId(quotationId);
		tRfqVerificationCode.setVerificationCode(verifyCode);
		Date effectiveTime = new Date();//验证码生效时间
		Date expirationTime = new Date();//过期时间
		//tRfqVerificationCode.setEffectiveTime(date2OffsetDateTime(time));
		tRfqVerificationCode.setEffectiveTime(effectiveTime);
//		expirationTime.setTime( effectiveTime.getTime() + 15*60*1000);
		expirationTime.setTime( effectiveTime.getTime() + expirationTimeConfig.getExpirationTime());
		//tRfqVerificationCode.setExpirationTime(date2OffsetDateTime(time));
		tRfqVerificationCode.setExpirationTime(expirationTime);
		tRfqVerificationCode.setMobile((String)userInfoByUid.get("mobile"));
		tRfqVerificationCode.setMemberType(memberType);
		iTRfqVerificationCodeService.save(tRfqVerificationCode);
	}

	/**
	 * 通知供应商应标
	 *
	 */
	@Override
	public Boolean bidResponse(TRfqQuotationSupplierVo quotationSupplierVo) {

		Map<String, Object> supplierSmsMap =new HashMap<>();
		supplierSmsMap.put("uid",quotationSupplierVo.getSupplierId());
		supplierSmsMap.put("mobile",quotationSupplierVo.getPhone());

		List<Map<String,Object>> userInfos = new ArrayList<>();
		userInfos.add(supplierSmsMap);
		//创建消息对象准备发送短信
		SendMsgParam sendMsgParam  = new SendMsgParam();

		//供应商-请询价通知  SMS_235481494  ${companyName}请您参与物料询价，询价单号${inquiryNo}
		//sendMsgParam.setSmsTempCode(SmsMsgTempEnum.SMS_235481494.getCode());
		sendMsgParam.setSmsTempCode(smsMsgTempConfig.getSupplierPleaseInformOfInquiry());
		String companyName = quotationSupplierVo.getSupplierName();
		//获取询价单单号quotationBaseId
		QuotationBaseVo infoById = quotationBaseService.getInfoById(quotationSupplierVo.getQuotationBaseId());
		String inquiryNo = infoById.getQuotationNo();

		//把短信模板需要的参数封装到map中
		Map<String,Object> params = new HashMap<>();
		params.put("companyName",companyName);
		params.put("inquiryNo",inquiryNo);

		sendMsgParam.setSmsParams(convertSmsMsg(params));
		sendMsgParam.setSmsMsgType(1);//1:发送短信  2：发送验证码
		sendMsgParam.setUserInfos(userInfos);
		informationSMSClient.save(sendMsgParam);

		//保存通知供应商应标的信息
		BidResponse bidResponse = BidResponse.builder()
				.quotationNo(inquiryNo)
				.mobile(quotationSupplierVo.getPhone())
				.supplierId(quotationSupplierVo.getSupplierId())
				.supplierCode(quotationSupplierVo.getSupplierCode())
				.supplierName(quotationSupplierVo.getSupplierName())
				.supplierType(quotationSupplierVo.getSupplierType())
				.build();
		bidResponse.setQuotationNo(inquiryNo);
		bidResponseService.save(bidResponse);
		return true;
	}



	public OffsetDateTime date2OffsetDateTime(Date date) {
		Instant instant1 = date.toInstant();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault());
		return localDateTime.atOffset(ZoneOffset.UTC);
	}

	public String generateVerifyCode() {
		Random r = new Random(new Date().getTime());
		int i = r.nextInt(10000);
		return String.valueOf(i);
	}
	/**
	 * 版本2有英文字母的验证码
	 *
	 * **/
	public String generateVerifyCodeVs() {
		String verifyCode = UUID.randomUUID().toString().
				replace("-", "").substring(0, 4);
		return verifyCode;
	}

	private LoginInfo getLoginInfo(){
		LoginInfo loginInfo = null;
		//获取当前登录信息
		Optional<LoginInfo> optional = SecurityUtils.getLoginInfo();
		if(optional.isPresent()){
			loginInfo = optional.get();
		}else{
			log.warn("获取用户登录信息失败");
		}
		return  loginInfo;
	}

	@Autowired
	private  BiddingLineExtService biddingLineExtService;

	@Override
	public List<QuotationSupplierVo> getPricingInfo(Long id) {
		PricingRecord pricingRecord = new PricingRecord();
		pricingRecord.setQuotationBaseId(id);
		List<PricingRecord> pricingRecords = this.getBaseMapper().selectList(Condition.getQueryWrapper(pricingRecord));
		HashMap<String, PricingRecord> stringPricingRecordHashMap = new HashMap<>();
		for (PricingRecord record : pricingRecords) {
			stringPricingRecordHashMap.put(record.getSupplierId(),record);
		}

		List<String> supplierIds = pricingRecords.stream().map(item -> {
			return item.getSupplierId();
		}).collect(Collectors.toList());
		List<QuotationSupplierVo> list = biddingLineExtService.quotationLineExtlist(id, true,supplierIds);

		for (QuotationSupplierVo quotationSupplierVo : list) {
			quotationSupplierVo.setPricingRecordVo(stringPricingRecordHashMap.get(quotationSupplierVo.getSupplierId().toString()));
		}

		PricingRemark pricingRemark = pricingRemarkService.getById(id);
		LambdaQueryWrapper<QuotationAttachment> quotationAttachmentLambdaQueryWrapper = Condition.getQueryWrapper(new QuotationAttachment()).lambda()
				.eq(QuotationAttachment::getBusinessFormId, id)
				.eq(QuotationAttachment::getBusinessType, QuotationAttBizEnum.QUOTATION_PRICING.getCode());
		List<QuotationAttachment> quotationAttachments = quotationAttachmentService.getBaseMapper().selectList(quotationAttachmentLambdaQueryWrapper);

		return list;
	}
}
