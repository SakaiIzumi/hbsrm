package net.bncloud.quotation.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.quotation.entity.*;
import net.bncloud.quotation.enums.QuotationStatusEnum;
import net.bncloud.quotation.enums.QuotationSupplierResponseStatus;
import net.bncloud.quotation.event.vo.QuotationInfoEventData;
import net.bncloud.quotation.event.vo.QuotationSupplierRejectEvent;
import net.bncloud.quotation.mapper.QuotationBaseMapper;
import net.bncloud.quotation.mapper.QuotationMarkMapper;
import net.bncloud.quotation.mapper.TRfqQuotationRecordMapper;
import net.bncloud.quotation.param.QuotationMarkParam;
import net.bncloud.quotation.service.IQuotationOperationLogService;
import net.bncloud.quotation.service.QuotationMarkService;
import net.bncloud.quotation.service.QuotationSupplierService;
import net.bncloud.quotation.utils.redis.RedisUtils;
import net.bncloud.quotation.vo.QuotationMarkVo;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.text.html.Option;
import java.util.*;

/**
 * <p>
 * 询价单应标关联表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-01
 */
@Service
@Slf4j
public class QuotationMarkServiceImpl extends BaseServiceImpl<QuotationMarkMapper, QuotationMark> implements QuotationMarkService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private TRfqQuotationRecordMapper recordMapper;

    @Resource
    private QuotationSupplierService quotationSupplierService;

    @Resource
    private QuotationBaseMapper quotationBaseMapper;

    private final IQuotationOperationLogService operationLogService;
    private final DefaultEventPublisher defaultEventPublisher;
    private final PurchaserFeignClient purchaserFeignClient;
    public QuotationMarkServiceImpl(IQuotationOperationLogService operationLogService, DefaultEventPublisher defaultEventPublisher, PurchaserFeignClient purchaserFeignClient) {
        this.operationLogService = operationLogService;
        this.defaultEventPublisher = defaultEventPublisher;
        this.purchaserFeignClient = purchaserFeignClient;
    }

    @Override
    public IPage<QuotationMark> selectPage(IPage<QuotationMark> page, QueryParam<QuotationMarkParam> pageParam) {
        // 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }

    @Override
    public QuotationMark selectByQuotationIdAndRoundNum(Long quotationId, Integer roundNum) throws Exception {
        QueryWrapper<QuotationMark> quotationMarkQueryWrapper = Condition.getQueryWrapper(new QuotationMark());
        BaseUserEntity user = AuthUtil.getUser();
        if (Objects.isNull(user.getCurrentSupplier().getSupplierId())) {
            throw new ApiException(500, "获取用户信息异常");
        }
        quotationMarkQueryWrapper.lambda().eq(QuotationMark::getQuotationId, quotationId)
                .eq(QuotationMark::getSupplierId, user.getCurrentSupplier().getSupplierId())
                .eq(QuotationMark::getRoundNum,roundNum);
        return baseMapper.selectOne(quotationMarkQueryWrapper);
    }


    @Override
    public void reject(QuotationMark quotationMark) {
        //验证
        if (Objects.isNull(quotationMark.getQuotationId())) {
            log.info("未传询报单ID");
            throw new ApiException(400, "未传询报单ID");
        }
        QueryWrapper<QuotationMark> quotationMarkQueryWrapper = Condition.getQueryWrapper(new QuotationMark());
        BaseUserEntity user = AuthUtil.getUser();
        if (Objects.isNull(user.getCurrentSupplier())) {
            log.info("用户非商户！！");
            throw new ApiException(400, "用户非商户！！");
        }
        quotationMarkQueryWrapper.lambda()
                .eq(QuotationMark::getRoundNum,quotationMark.getRoundNum())
                .eq(QuotationMark::getQuotationId, quotationMark.getQuotationId())
                .eq(QuotationMark::getSupplierId, user.getCurrentSupplier().getSupplierId());
        QuotationMark hasRejectQuotationMark = baseMapper.selectOne(quotationMarkQueryWrapper);
        QuotationBase base;
        if (!Objects.isNull(hasRejectQuotationMark)) {
            if (hasRejectQuotationMark.getMarkStatus().equals(QuotationMark.MARK_STATUS.REJECT)) {
                log.info("询价单已拒绝");
                throw new ApiException(400, "询价单已拒绝");
            } else if (hasRejectQuotationMark.getMarkStatus().equals(QuotationMark.MARK_STATUS.MARKED)) {
                log.info("询价单已应标");
                throw new ApiException(400, "询价单已应标");
            } else {
                log.info("未知询价单状态");
                throw new ApiException(400, "未知询价单状态");
            }
        }

        //获取基于redis的分布式锁，防止多创建拒绝数据
        String key = String.format("QuotationMark:Reject:%d-%d", quotationMark.getQuotationId(), user.getUserId());
        try {
            boolean setnx = redisUtils.setnx(key, null, 30);

            if (!setnx) {
                log.info("询价单正在拒绝,请勿重复请求");
                throw new ApiException(400, "询价单正在拒绝,请勿重复请求");
            }
            //创建拒绝数据
            base = quotationBaseMapper.selectById(quotationMark.getQuotationId());
            QuotationMark doRejectQuotationMark = new QuotationMark()
                    .setQuotationId(quotationMark.getQuotationId())
                    .setMarkStatus(QuotationMark.MARK_STATUS.REJECT)
                    .setSupplierId(user.getCurrentSupplier().getSupplierId())
                    .setRoundNum(base.getCurrentRoundNumber());
            this.save(doRejectQuotationMark);

            //修改询价行供应商信息的响应状态  t_rfq_quotation_supplier
            LambdaQueryWrapper<QuotationSupplier> eq = Condition.getQueryWrapper(new QuotationSupplier()).lambda()
                    .eq(QuotationSupplier::getSupplierId, user.getCurrentSupplier().getSupplierId())
                    .eq(QuotationSupplier::getQuotationBaseId, quotationMark.getQuotationId());
            QuotationSupplier one = quotationSupplierService.getOne(eq);
            if(ObjectUtil.isEmpty(one)){
                throw new ApiException(500,"此询价单查询不到供应商");
            }
            quotationSupplierService.updateResponseStatus(one.getQuotationBaseId(),one.getSupplierId(),QuotationSupplierResponseStatus.refused.name());

        } finally {
            redisUtils.del(key);
        }

        //根据供应商编码和采购商编码获取orgId
        OrgIdQuery orgIdQuery = new OrgIdQuery()
                .setSupplierCode(user.getCurrentSupplier().getSupplierCode())
                .setPurchaseCode(base.getCustomerCode());
        R<OrgIdDTO> info = purchaserFeignClient.info(orgIdQuery);
        if (!(info.isSuccess() && info.getData() != null)) {
            log.error("根据供应商编码{}，和采购方编码{}，获取orgId失败！", JSON.toJSONString(orgIdQuery.getSupplierCode()), JSON.toJSONString(orgIdQuery.getPurchaseCode()));
            throw new ApiException(ResultCode.FAILURE.getCode(), "根据供应商编码和采购方编码获取orgId失败！");
        }
        Optional.of(info.getData()).ifPresent(orgIdDTO -> {
            QuotationInfoEventData eventData = BeanUtil.copy(base, QuotationInfoEventData.class);
            //站内消息
            eventData.setOrgId(orgIdDTO.getOrgId())
                    .setBusinessId(base.getId())
                    .setSupplierCode(orgIdDTO.getSupplierCode())
                    .setSupplierName(orgIdDTO.getSupplierame());
            LoginInfo loginInfo = SecurityUtils.getLoginInfo().get();
            QuotationSupplierRejectEvent quotationSupplierRejectEvent = new QuotationSupplierRejectEvent(this, loginInfo, eventData, orgIdDTO.getSupplierCode(), orgIdDTO.getSupplierame());
            defaultEventPublisher.publishEvent(quotationSupplierRejectEvent);
            //操作日志
            QuotationOperationLog operationLog = new QuotationOperationLog()
                    .setBillId(quotationMark.getQuotationId())
                    .setBillType(orgIdDTO.getSupplierame())
                    .setContent("报价单应标拒绝")
                    .setOperationNo(user.getUserId())
                    .setOperatorName(user.getUserName());
            operationLogService.save(operationLog);
        });


    }

    @Override
    public boolean check(QuotationMark quotationMark) {

        //验证

        if (Objects.isNull(quotationMark.getQuotationId())) {
            log.info("未传询报单ID");
            throw new ApiException(400, "未传询报单ID");
        }

        BaseUserEntity user = AuthUtil.getUser();

        if (Objects.isNull(user.getCurrentSupplier())) {
            log.info("用户非商户！！");
            throw new ApiException(400, "用户非商户！！");
        }

        QueryWrapper<QuotationMark> quotationMarkQueryWrapper = Condition.getQueryWrapper(new QuotationMark());
        quotationMarkQueryWrapper.lambda()
                .eq(QuotationMark::getQuotationId, quotationMark.getQuotationId())
                .eq(QuotationMark::getMarkStatus, QuotationMark.MARK_STATUS.REJECT)
                .eq(QuotationMark::getSupplierId, user.getCurrentSupplier().getSupplierId());


        QuotationMark hasRejectQuotationMark = baseMapper.selectOne(quotationMarkQueryWrapper);
        if (!Objects.isNull(hasRejectQuotationMark)) {
            return !hasRejectQuotationMark.getMarkStatus().equals(QuotationMark.MARK_STATUS.REJECT);
        }


        return true;
    }

    @Override
    public List<QuotationMarkVo> markedSupplier(QuotationMark quotationMark) {
        if(Objects.isNull(quotationMark.getQuotationId())){
            log.info("未传询报单ID");
            throw new ApiException(400, "未传询报单ID");
        }

        QuotationBase base = quotationBaseMapper.selectById(quotationMark.getQuotationId());
        if(base.getQuotationStatus().equals(QuotationStatusEnum.HAVE_PRICING.getCode())){
            throw new ApiException(500, "询价单已经定价了");
        }


        LambdaQueryWrapper<TRfqQuotationRecord> queryWrapper = Condition.getQueryWrapper(new TRfqQuotationRecord()).lambda()
                .select(TRfqQuotationRecord::getSupplierId,TRfqQuotationRecord::getSupplierCode,TRfqQuotationRecord::getSupplierName)
                .eq(TRfqQuotationRecord::getQuotationBaseId, quotationMark.getQuotationId())
                .groupBy(TRfqQuotationRecord::getSupplierId,TRfqQuotationRecord::getSupplierCode,TRfqQuotationRecord::getSupplierName);
        List<Map<String, Object>> recordList = recordMapper.selectMaps(queryWrapper);

        List<QuotationMarkVo> result=new ArrayList<>();

        if(CollectionUtils.isNotEmpty(recordList)){
            recordList.forEach(item->{
                QuotationMarkVo target= new QuotationMarkVo();
                target.setSupplierCode(MapUtils.getString(item,"supplier_code"));
                target.setSupplierName(MapUtils.getString(item,"supplier_name"));
                target.setSupplierId(MapUtils.getLong(item,"supplier_id"));
                result.add(target);
            });
        }
        return result;
    }

}
