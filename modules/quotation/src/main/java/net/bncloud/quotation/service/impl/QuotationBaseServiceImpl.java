package net.bncloud.quotation.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.api.feign.saas.sys.DictCodeEnum;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.common.base.globallock.lock.LockWrapper;
import net.bncloud.common.bizutil.number.NumberFactory;
import net.bncloud.common.bizutil.number.NumberType;
import net.bncloud.common.constants.YesOrNoNumber;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.security.Supplier;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.excel.write.ExcelWrite;
import net.bncloud.quotation.annotation.OperationLog;
import net.bncloud.quotation.config.SmsMsgTempConfig;
import net.bncloud.quotation.entity.*;
import net.bncloud.quotation.enums.*;
import net.bncloud.quotation.event.vo.*;
import net.bncloud.quotation.manager.QuotationSupplierAlert;
import net.bncloud.quotation.mapper.QuotationBaseMapper;
import net.bncloud.quotation.param.QuotationBaseParam;
import net.bncloud.quotation.param.QuotationBaseRestateParam;
import net.bncloud.quotation.service.*;
import net.bncloud.quotation.utils.evalex.EvalexUtil;
import net.bncloud.quotation.vo.*;
import net.bncloud.quotation.wrapper.QuotationBaseWrapper;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import net.bncloud.service.api.platform.tenant.enums.ScopeEnum;
import net.bncloud.service.api.platform.tenant.feign.TenantRoleForQuotationFeign;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static net.bncloud.quotation.enums.QuotationResultCode.PARAM_ERROR;
import static net.bncloud.quotation.enums.QuotationResultCode.SUPPLIER_NOT_FOUND;

/**
 * <p>
 * 询价基础信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class QuotationBaseServiceImpl extends BaseServiceImpl<QuotationBaseMapper, QuotationBase> implements QuotationBaseService {

    private static final String QUOTATION_PUBLISH_PREFIX_KEY = "QUOTATION_PUBLISH_LOCK:quotationPublish";

    private final QuotationEquipmentService quotationEquipmentService;

    private final BncDictItemService dictItemService;
    @Resource
    private NumberFactory numberFactory;

    @Resource
    private PurchaserFeignClient purchaserFeignClient;

    @Autowired
    private SmsMsgTempConfig smsMsgTempConfig;

    @Autowired
    private DefaultEventPublisher defaultEventPublisher;

    @Autowired
    private IQuotationOperationLogService operationLogService;

    @Autowired
    private QuotationLineExtService quotationLineExtService;

    @Autowired
    private BiddingLineExtService biddingLineExtService;

    @Autowired
    private QuotationSupplierService quotationSupplierService;

    @Autowired
    private QuotationLineBaseService quotationLineBaseService;

    @Autowired
    private QuotationAttRequireService quotationAttRequireService;

    @Autowired
    private QuotationAttachmentService quotationAttachmentService;

    @Autowired
    private ITRfqQuotationRecordService tTRfqQuotationRecordService;

    @Autowired
    private QuotationMarkService quotationMarkService;

    @Autowired
    private MaterialQuotationTemplateService materialQuotationTemplateService;

    @Autowired
    private IQuotationAttRequireAttachmentService iQuotationAttRequireAttachmentService;

    @Autowired
    private EvalexUtil evalexUtil;

    @Autowired
    private IRestateBaseService restateBaseService;

    @Autowired
    private IRestateSupplierService restateSupplierService;

    @Autowired
    private DistributedLock distributedLock;

    @Autowired
    private TenantRoleForQuotationFeign tenantRoleForQuotationFeign;

    // @Autowired
    // private QuotationBaseMapper quotationBaseMapper;

    @Lazy
    @Autowired
    private PricingRecordService pricingRecordService;

    @Resource
    private CfgParamResourceFeignClient cfgParamResourceFeignClient;

    public QuotationBaseServiceImpl(QuotationEquipmentService quotationEquipmentService, BncDictItemService dictItemService) {
        this.quotationEquipmentService = quotationEquipmentService;
        this.dictItemService = dictItemService;
    }

    @Override
    public IPage<QuotationBaseVo> selectQuotationBaseSalePage(IPage<QuotationBase> page, QueryParam<QuotationBaseParam> pageParam) throws Exception {
        if (Objects.isNull(pageParam)) {
            pageParam = new QueryParam<>();
        }
        if (Objects.isNull(pageParam.getParam())) {
            pageParam.setParam(new QuotationBaseParam());
        }
        //没获取到supplierId直接抛异常
        Long supplierId = null;
        supplierId = AuthUtil.getUser().getCurrentSupplier().getSupplierId();
        if (supplierId == null) {
            throw new BizException(SUPPLIER_NOT_FOUND);
        }
        pageParam.getParam().setSupplierId(supplierId);

        List<QuotationBase> quotationBases = baseMapper.selectQuotationBaseSalePage(page, pageParam);
        page.setRecords(quotationBases);

        IPage<QuotationBaseVo> pageVO = QuotationBaseWrapper.build().pageVO(page);
        //添加权限按钮
        for (QuotationBaseVo record : pageVO.getRecords()) {
            addPermissionButton(record);
        }

        return pageVO;
    }


    @Override
    public IPage<QuotationBaseVo> selectPage(IPage<QuotationBase> page, QueryParam<QuotationBaseParam> pageParam) {

        //校验当前采购登录用户的查询询价单的权限(注意后期如果开放，还有统计数量那里也需要开放)
        roleForQuotationPermission(pageParam);

        List<QuotationBase> quotationBases = baseMapper.selectListPage(page, pageParam);

        IPage<QuotationBase> quotationBaseIPage = page.setRecords(quotationBases);
        IPage<QuotationBaseVo> pageVO = QuotationBaseWrapper.build().pageVO(quotationBaseIPage);
        pageVO.getRecords().stream().map(this::addPermissionButton).map(this::buildBiddingNum).collect(Collectors.toList());
        //回显中标供应商信息和份额，如果有的话
        for (QuotationBaseVo record : pageVO.getRecords()) {
            //如果是已经定价的询价单，需要取出定价的供应商和份额
            if (record.getQuotationStatus().equals(QuotationStatusEnum.HAVE_PRICING.getCode())) {
                LambdaQueryWrapper<PricingRecord> query = Condition.getQueryWrapper(new PricingRecord()).lambda()
                        .eq(PricingRecord::getQuotationBaseId, record.getId());
                List<PricingRecord> pricingRecords = pricingRecordService.getBaseMapper().selectList(query);
                List<PricingRecordVo> pricingRecordVos = new ArrayList<>();
                for (PricingRecord pricingRecord : pricingRecords) {
                    PricingRecordVo copy = BeanUtil.copy(pricingRecord, PricingRecordVo.class);
                    //查询公司名字
                    LambdaQueryWrapper<QuotationSupplier> eq = Condition.getQueryWrapper(new QuotationSupplier()).lambda()
                            .eq(QuotationSupplier::getQuotationBaseId, pricingRecord.getQuotationBaseId())
                            .eq(QuotationSupplier::getSupplierId, pricingRecord.getSupplierId());
                    QuotationSupplier one = quotationSupplierService.getOne(eq);
                    copy.setCompanyName(one.getSupplierName());
                    pricingRecordVos.add(copy);
                }
                record.setPricingRecordList(pricingRecordVos);

            }
        }

        //添加权限按钮
        for (QuotationBaseVo record : pageVO.getRecords()) {
            addPermissionButton(record);
        }
        return pageVO;
    }

    /**
     * 统计已报价供应商数量
     *
     * @param quotationBaseVo 询价单信息
     * @return 询价单信息
     */
    private QuotationBaseVo buildBiddingNum(QuotationBaseVo quotationBaseVo) {
        if (quotationBaseVo != null) {
            Integer biddingNum = quotationSupplierService.countBiddingNum(quotationBaseVo.getId());
            quotationBaseVo.setBiddingNum(biddingNum);
        }
        return quotationBaseVo;
    }

    /**
     * 查询询价单信息
     *
     * @param id
     * @return
     */
    @Override
    public QuotationBaseVo getSaleInfoById(Long id) throws Exception {
        Long supplierId = 0L;
        try {
            supplierId = AuthUtil.getUser().getCurrentSupplier().getSupplierId();
            if (supplierId == null) {
                throw new ApiException(500, SUPPLIER_NOT_FOUND.getMessage());
            }
        } catch (Exception e) {
            throw new ApiException(500, SUPPLIER_NOT_FOUND.getMessage());
        }

        QuotationBase quotationBase = getById(id);
        QuotationBaseVo resultQuotationBaseVo = new QuotationBaseVo();

        //当前供应商的询价单应标关联信息
        QuotationMark quotationMark = quotationMarkService.getOne(Wrappers.<QuotationMark>lambdaQuery()
                .eq(QuotationMark::getQuotationId, id)
                .eq(QuotationMark::getSupplierId, supplierId));
        if (quotationMark == null) {
            resultQuotationBaseVo.setQuotationMarkStatus(0);//未应标
        } else {
            int quotationMarkStatus = quotationMark.getMarkStatus() == QuotationMark.MARK_STATUS.MARKED ? 1 : -1;
            resultQuotationBaseVo.setQuotationMarkStatus(quotationMarkStatus);//已应标
            //拒绝状态，提示“无权限”查看询价单详情
            if (quotationMarkStatus == -1) {
                throw new ApiException(ResultCode.REQ_REJECT.getCode(), "您暂无权限查看该询价单！");
            }
        }

        //当前供应商，当前的轮次的报价记录
        List<TRfqQuotationRecord> quotationRecords = tTRfqQuotationRecordService.list(Wrappers.<TRfqQuotationRecord>lambdaQuery()
                .eq(TRfqQuotationRecord::getQuotationBaseId, id)
                .eq(TRfqQuotationRecord::getSupplierId, supplierId)
                .eq(TRfqQuotationRecord::getRoundNumber, quotationBase.getCurrentRoundNumber()));
        // 当前轮次当前的供应商是否已经报价  false没有报价 true已经报过价了
        resultQuotationBaseVo.setQuotedOfTheCurrentRound(quotationRecords != null && !quotationRecords.isEmpty());

        String quotationStatus = quotationBase.getQuotationStatus();

        //设置按钮
        buildQuotationButton(id, supplierId, quotationBase, resultQuotationBaseVo, quotationStatus);


        BeanUtil.copyProperties(quotationBase, resultQuotationBaseVo);
        //物料报价模板
        QuotationLineBase quotationLineBase = quotationLineBaseService.getOne(new LambdaQueryWrapper<QuotationLineBase>().eq(QuotationLineBase::getQuotationBaseId, id));
        if (ObjectUtil.isNotEmpty(quotationLineBase)) {
            MaterialQuotationTemplate materialQuotationTemplate = materialQuotationTemplateService.getById(quotationLineBase.getTemplateId());

            if (ObjectUtil.isNotEmpty(materialQuotationTemplate)) {
                resultQuotationBaseVo.setMaterialQuotationTemplate(materialQuotationTemplate);
            }

            if (materialQuotationTemplate != null) {
                resultQuotationBaseVo.setTemplateName(materialQuotationTemplate.getTemplateName());
            }
        }


        //供应商附件需求清单
        List<QuotationAttRequire> quotationAttRequireList = quotationAttRequireService.list(Wrappers.<QuotationAttRequire>lambdaQuery()
                .eq(QuotationAttRequire::getQuotationBaseId, id)
                .orderByAsc(QuotationAttRequire::getCreatedDate));
        List<QuotationAttRequireVo> quotationAttRequireVos = new ArrayList<>();
        for (int i = 0; i < quotationAttRequireList.size(); i++) {
            QuotationAttRequireVo quotationAttRequireVo = new QuotationAttRequireVo();
            BeanUtil.copyProperties(quotationAttRequireList.get(i), quotationAttRequireVo);
            //添加标号
            quotationAttRequireVo.setItemNo(i + 1);
            //添加附件列表
            quotationAttRequireVo.setQuotationAttRequireAttachmentVos(iQuotationAttRequireAttachmentService.getFilesByQuotationIdAndAttRequireId(quotationAttRequireVo.getQuotationBaseId(), quotationAttRequireVo.getId()));
            quotationAttRequireVos.add(quotationAttRequireVo);
        }
        resultQuotationBaseVo.setQuotationAttRequireVos(quotationAttRequireVos);

        //附件列表
        List<QuotationAttachment> quotationAttachmentList = quotationAttachmentService.list(Wrappers.<QuotationAttachment>lambdaQuery()
                .eq(QuotationAttachment::getBusinessFormId, id)
                .orderByAsc(QuotationAttachment::getCreatedDate));
        List<QuotationAttachmentVo> quotationAttachmentVos = new ArrayList<>();
        for (int i = 0; i < quotationAttachmentList.size(); i++) {
            QuotationAttachmentVo quotationAttachmentVo = new QuotationAttachmentVo();
            BeanUtil.copyProperties(quotationAttachmentList.get(i), quotationAttachmentVo);
            quotationAttachmentVo.setItemNo(i + 1);
            quotationAttachmentVos.add(quotationAttachmentVo);
        }
        resultQuotationBaseVo.setQuotationAttachmentVos(quotationAttachmentVos);


        //询价行基础信息
        List<QuotationLineBase> quotationLineBaseList = quotationLineBaseService.list(Wrappers.<QuotationLineBase>lambdaQuery()
                .eq(QuotationLineBase::getQuotationBaseId, id));
        List<QuotationLineBaseVo> quotationLineBaseVoList = BeanUtil.copyProperties(quotationLineBaseList, QuotationLineBaseVo.class);
        //询价行的物料报价信息
        quotationLineBaseVoList.forEach(quotationLineBaseVo -> {
            List<QuotationLineExt> quotationLineExts = quotationLineExtService.list(Wrappers.<QuotationLineExt>lambdaQuery()
                    .eq(QuotationLineExt::getLineId, quotationLineBaseVo.getId())
                    .orderByAsc(QuotationLineExt::getOrderValue));
            quotationLineBaseVo.setQuotationLineExtList(quotationLineExts);
        });
        resultQuotationBaseVo.setQuotationLineBaseVoList(quotationLineBaseVoList);


        //当前报价
        List<BiddingLineExt> biddingLineNormalExts = new ArrayList<>();
        List<BiddingLineExt> biddingLineExpressionExts = new ArrayList<>();
        //上一轮报价
        List<BiddingLineExt> lastReportBiddingLineExtList = new ArrayList<>();
        //客户期望报价
        List<BiddingLineExt> exceptBiddingLineExtList = new ArrayList<>();
        //询价单当前轮次
        Integer currentRoundNumber = quotationBase.getCurrentRoundNumber();
        //询价行信息
        List<QuotationLineExt> quotationLineNormalExts = quotationLineExtService.list(new LambdaQueryWrapper<QuotationLineExt>().eq(QuotationLineExt::getQuotationBaseId, id).eq(QuotationLineExt::getDataType, "normal"));
        List<QuotationLineExt> quotationLineExpressionExts = quotationLineExtService.list(new LambdaQueryWrapper<QuotationLineExt>().eq(QuotationLineExt::getQuotationBaseId, id).eq(QuotationLineExt::getDataType, "expression"));
        //当前供应商的所有轮次的报价记录
        List<TRfqQuotationRecord> records = tTRfqQuotationRecordService.list(Wrappers.<TRfqQuotationRecord>lambdaQuery()
                .eq(TRfqQuotationRecord::getQuotationBaseId, quotationBase.getId())
                .eq(TRfqQuotationRecord::getSupplierId, supplierId));
        //第一轮
        if (currentRoundNumber == 1) {
            //当前供应商、当前轮次是否报过价:false没有报价，true已报价
            if (!resultQuotationBaseVo.getQuotedOfTheCurrentRound()) { //没报价,招标行等价于询价行
                biddingLineNormalExts = BeanUtil.copyProperties(quotationLineNormalExts, BiddingLineExt.class);
                biddingLineExpressionExts = BeanUtil.copyProperties(quotationLineExpressionExts, BiddingLineExt.class);
            } else { //已报价
                biddingLineNormalExts = biddingLineExtService.list(new LambdaQueryWrapper<BiddingLineExt>()
                        .eq(BiddingLineExt::getQuotationBaseId, id)
                        .eq(BiddingLineExt::getDataType, "normal")
                        .eq(BiddingLineExt::getQuoteRound, 1)
                        .eq(BiddingLineExt::getSupplierId, supplierId));
                biddingLineExpressionExts = biddingLineExtService.list(new LambdaQueryWrapper<BiddingLineExt>()
                        .eq(BiddingLineExt::getQuotationBaseId, id)
                        .eq(BiddingLineExt::getDataType, "expression")
                        .eq(BiddingLineExt::getQuoteRound, 1)
                        .eq(BiddingLineExt::getSupplierId, supplierId));
            }

            //第二轮及之后的轮次
        } else {
            //当前轮次，当前供应商，是否进行了重报
            RestateSupplier restateSupplier = restateSupplierService.getOne(Wrappers.<RestateSupplier>lambdaQuery()
                    .eq(RestateSupplier::getQuotationBaseId, id)
                    .eq(RestateSupplier::getSupplierId, supplierId)
                    .eq(RestateSupplier::getRoundNumber, quotationBase.getCurrentRoundNumber() - 1));
            //已重报
            if (restateSupplier != null) {
                //重报之后，是否推送每行最低报价
                RestateBase restateBase = restateBaseService.getOne(Wrappers.<RestateBase>lambdaQuery()
                        .eq(RestateBase::getQuotationBaseId, quotationBase.getId())
                        .eq(RestateBase::getRoundNumber, currentRoundNumber - 1));
                //推送（有客户期望报价）
                if (restateBase != null && "1".equals(restateBase.getPushCheapest())) {
                    //客户期望报价
                    exceptBiddingLineExtList = biddingLineExtService.queryQuotationExceptBidding(id, BiddingLineExtBizTypeEnum.EXCEPT, "normal");
                }
                //没推送（无客户期望报价）
                //重报之后，当前供应商当前轮次是否已报过价
                TRfqQuotationRecord quotationRecord = tTRfqQuotationRecordService.getOne(Wrappers.<TRfqQuotationRecord>lambdaQuery()
                        .eq(TRfqQuotationRecord::getQuotationBaseId, quotationBase.getId())
                        .eq(TRfqQuotationRecord::getSupplierId, supplierId)
                        .eq(TRfqQuotationRecord::getRoundNumber, quotationBase.getCurrentRoundNumber()));
                //已报价
                if (quotationRecord != null) {
                    //当前报价
                    biddingLineNormalExts = biddingLineExtService.list(new LambdaQueryWrapper<BiddingLineExt>()
                            .eq(BiddingLineExt::getQuotationBaseId, id)
                            .eq(BiddingLineExt::getDataType, "normal")
                            .eq(BiddingLineExt::getQuoteRound, quotationBase.getCurrentRoundNumber())
                            .eq(BiddingLineExt::getSupplierId, supplierId));
                    biddingLineExpressionExts = biddingLineExtService.list(new LambdaQueryWrapper<BiddingLineExt>()
                            .eq(BiddingLineExt::getQuotationBaseId, id)
                            .eq(BiddingLineExt::getDataType, "expression")
                            .eq(BiddingLineExt::getQuoteRound, quotationBase.getCurrentRoundNumber())
                            .eq(BiddingLineExt::getSupplierId, supplierId));

                    //排除当前的报价记录之后，当前供应商的所有报价记录
                    List<TRfqQuotationRecord> quotationRecordList = tTRfqQuotationRecordService.list(Wrappers.<TRfqQuotationRecord>lambdaQuery()
                            .eq(TRfqQuotationRecord::getQuotationBaseId, quotationBase.getId())
                            .eq(TRfqQuotationRecord::getSupplierId, supplierId)
                            .notIn(TRfqQuotationRecord::getRoundNumber, quotationBase.getCurrentRoundNumber()));
                    //最近的上一次报价的轮次
                    Integer recentNumber = quotationRecordList.stream()
                            .sorted(Comparator.comparing(TRfqQuotationRecord::getRoundNumber).reversed())
                            .collect(Collectors.toList())
                            .get(0)
                            .getRoundNumber();
                    //最近的上一轮报价
                    lastReportBiddingLineExtList = biddingLineExtService.list(new LambdaQueryWrapper<BiddingLineExt>()
                            .eq(BiddingLineExt::getQuotationBaseId, id)
                            .eq(BiddingLineExt::getDataType, "normal")
                            .eq(BiddingLineExt::getQuoteRound, recentNumber)
                            .eq(BiddingLineExt::getSupplierId, supplierId));

                } else {//未报价（当前报价和上一轮报价一样）
                    //最近的报价的轮次
                    Integer recentRoundNumber = records.stream()
                            .sorted(Comparator.comparing(TRfqQuotationRecord::getRoundNumber).reversed())
                            .collect(Collectors.toList())
                            .get(0)
                            .getRoundNumber();
                    //当前报价
                    biddingLineNormalExts = biddingLineExtService.list(new LambdaQueryWrapper<BiddingLineExt>()
                            .eq(BiddingLineExt::getQuotationBaseId, id)
                            .eq(BiddingLineExt::getDataType, "normal")
                            .eq(BiddingLineExt::getQuoteRound, recentRoundNumber)
                            .eq(BiddingLineExt::getSupplierId, supplierId));
                    biddingLineExpressionExts = biddingLineExtService.list(new LambdaQueryWrapper<BiddingLineExt>()
                            .eq(BiddingLineExt::getQuotationBaseId, id)
                            .eq(BiddingLineExt::getDataType, "expression")
                            .eq(BiddingLineExt::getQuoteRound, recentRoundNumber)
                            .eq(BiddingLineExt::getSupplierId, supplierId));
                    //上一轮报价
                    lastReportBiddingLineExtList = biddingLineNormalExts;
                }
            } else { //没重报，当前轮次不能报价
                //有报价记录（最近的上一次报价作为当前报价）
                if (CollectionUtil.isNotEmpty(records)) {
                    //最近的报价的轮次
                    Integer recentRoundNumber = records.stream()
                            .sorted(Comparator.comparing(TRfqQuotationRecord::getRoundNumber).reversed())
                            .collect(Collectors.toList())
                            .get(0)
                            .getRoundNumber();
                    biddingLineNormalExts = biddingLineExtService.list(Wrappers.<BiddingLineExt>lambdaQuery()
                            .eq(BiddingLineExt::getQuotationBaseId, id)
                            .eq(BiddingLineExt::getDataType, "normal")
                            .eq(BiddingLineExt::getQuoteRound, recentRoundNumber)
                            .eq(BiddingLineExt::getSupplierId, supplierId));
                    biddingLineExpressionExts = biddingLineExtService.list(Wrappers.<BiddingLineExt>lambdaQuery()
                            .eq(BiddingLineExt::getQuotationBaseId, id)
                            .eq(BiddingLineExt::getDataType, "expression")
                            .eq(BiddingLineExt::getQuoteRound, recentRoundNumber)
                            .eq(BiddingLineExt::getSupplierId, supplierId));
                } else {//没有报价记录，没报过价，招标行等价于询价行
                    biddingLineNormalExts = BeanUtil.copyProperties(quotationLineNormalExts, BiddingLineExt.class);
                    biddingLineExpressionExts = BeanUtil.copyProperties(quotationLineExpressionExts, BiddingLineExt.class);
                }
            }
        }

        //if (CollectionUtil.isEmpty(quotationRecords)) {
        //    //还没有报过价
        //    Integer currentRoundNumber = quotationBase.getCurrentRoundNumber();
        //    if(currentRoundNumber>1){//当前不是第一轮，那就查询上一轮的报价，上一轮也没有，上上轮
        //        for (int i = currentRoundNumber-1; i >0; i--) {
        //            //当前的轮次的报价记录
        //            List<TRfqQuotationRecord> list = tTRfqQuotationRecordService.list(new LambdaQueryWrapper<TRfqQuotationRecord>()
        //                    .eq(TRfqQuotationRecord::getQuotationBaseId,id)
        //                    .eq(TRfqQuotationRecord::getSupplierId,supplierId)
        //                    .eq(TRfqQuotationRecord::getRoundNumber,i));
        //
        //            if(list.size()>0){
        //                //当前轮次有报价
        //                biddingLineNormalExts = biddingLineExtService.list(new LambdaQueryWrapper<BiddingLineExt>()
        //                        .eq(BiddingLineExt::getQuotationBaseId, id)
        //                        .eq(BiddingLineExt::getDataType, "normal")
        //                        .eq(BiddingLineExt::getQuoteRound,i)
        //                        .eq(BiddingLineExt::getSupplierId,supplierId));
        //                biddingLineExpressionExts = biddingLineExtService.list(new LambdaQueryWrapper<BiddingLineExt>()
        //                        .eq(BiddingLineExt::getQuotationBaseId, id)
        //                        .eq(BiddingLineExt::getDataType, "expression")
        //                        .eq(BiddingLineExt::getQuoteRound,i)
        //                        .eq(BiddingLineExt::getSupplierId,supplierId));
        //
        //                break;
        //
        //            }
        //
        //
        //        }
        //    }
        //
        //    // 都没有报价，就直接使用扩展行字段
        //    if (biddingLineNormalExts.size()<=0&&biddingLineExpressionExts.size()<=0) {
        //        List<QuotationLineExt> quotationLineNormalExts = quotationLineExtService.list(new LambdaQueryWrapper<QuotationLineExt>().eq(QuotationLineExt::getQuotationBaseId, id).eq(QuotationLineExt::getDataType,"normal"));
        //        List<QuotationLineExt> quotationLineExpressionExts = quotationLineExtService.list(new LambdaQueryWrapper<QuotationLineExt>().eq(QuotationLineExt::getQuotationBaseId, id).eq(QuotationLineExt::getDataType,"expression"));
        //        biddingLineNormalExts = BeanUtil.copyProperties(quotationLineNormalExts,BiddingLineExt.class);
        //        biddingLineExpressionExts = BeanUtil.copyProperties(quotationLineExpressionExts,BiddingLineExt.class);
        //    }
        //
        //} else {
        //    //要是报过价了 就去查询当前轮次的报价记录
        //    biddingLineNormalExts = biddingLineExtService.list(new LambdaQueryWrapper<BiddingLineExt>()
        //            .eq(BiddingLineExt::getQuotationBaseId, id)
        //            .eq(BiddingLineExt::getDataType, "normal")
        //            .eq(BiddingLineExt::getQuoteRound,quotationBase.getCurrentRoundNumber())
        //            .eq(BiddingLineExt::getSupplierId,supplierId));
        //    biddingLineExpressionExts = biddingLineExtService.list(new LambdaQueryWrapper<BiddingLineExt>()
        //            .eq(BiddingLineExt::getQuotationBaseId, id)
        //            .eq(BiddingLineExt::getDataType, "expression")
        //            .eq(BiddingLineExt::getQuoteRound,quotationBase.getCurrentRoundNumber())
        //            .eq(BiddingLineExt::getSupplierId,supplierId));
        //}
        //lastReportBiddingLineExtList = biddingLineExtService.queryQuotationLastBidding(id, supplierId, BiddingLineExtBizTypeEnum.NORMAL,"normal");
        //exceptBiddingLineExtList = biddingLineExtService.queryQuotationExceptBidding(id,  BiddingLineExtBizTypeEnum.EXCEPT,"normal");

        //上一轮报价
        resultQuotationBaseVo.setLastReportBiddingLineExtList(lastReportBiddingLineExtList);
        //客户期望报价
        resultQuotationBaseVo.setExceptBiddingLineExtList(exceptBiddingLineExtList);
        //当前报价
        resultQuotationBaseVo.setQuotationLineNormalExts(biddingLineNormalExts);
        resultQuotationBaseVo.setQuotationLineExpressionExts(biddingLineExpressionExts);

        //询价单设备信息字段
        LambdaQueryWrapper<QuotationEquipment> queryWrapper = Condition.getQueryWrapper(new QuotationEquipment())
                .lambda().eq(QuotationEquipment::getQuotationBaseId, id);
        List<QuotationEquipment> equipmentList = quotationEquipmentService.list(queryWrapper);
        resultQuotationBaseVo.setEquipmentList(equipmentList);

        return resultQuotationBaseVo;
    }

    /**
     * 获取当前时间（服务器需要加上8个小时）
     *
     * @param nowDate 服务器当前时间
     * @return 服务器需要加上8个小时
     */
    public Date getDateAdd(Date nowDate) {
        long rightTime8 = (long) (nowDate.getTime() + (8 * 60 * 60 * 1000) );
        Date date8 = new Date(rightTime8);//加了8个小时的当前时间
        return date8;
    }

    /**
     * 获取当前时间LocalDate（服务器需要加上8个小时）
     *
     * @param date8 服务器当前时间加8小时
     * @return 服务器需要加上8个小时
     */
    public LocalDateTime getDateAddForLocalDateTime(Date date8) {
        //date转换成LocalDateTime
        LocalDateTime date8ForLocalDate = date8.toInstant()
                .atZone( ZoneId.systemDefault() )
                .toLocalDateTime();
        return date8ForLocalDate;
    }

    /**
     * 设置询价单的按钮
     * @param quotationBaseId 询价单id
     * @param supplierId 供应商id
     * @param quotationBase 询价单
     * @param resultQuotationBaseVo 销售工作台的询价单视图
     * @param quotationStatus 询价单状态
     */
    private void buildQuotationButton(Long quotationBaseId, Long supplierId, QuotationBase quotationBase, QuotationBaseVo resultQuotationBaseVo, String quotationStatus) {

        log.info("=============查询时间====================");
        log.info("服务器时间date：{}",JSON.toJSONString(new Date().toLocaleString()));
        log.info("服务器时间localdateTime：{}",JSON.toJSONString(LocalDateTime.now()));
        log.info("=============查询时间====================");
        //获取当前时间（服务器需要加上8个小时）
        /*Date nowDate = new Date();

        Date date8 = getDateAdd(nowDate);

        LocalDateTime localDateTime8Now = getDateAddForLocalDateTime(date8);*/

        log.info("=============构建按钮====================");
        if (!(quotationBase !=null &&quotationBase.getQuotationStartTime() !=null && quotationBase.getCutOffTime() !=null)){
            log.error("异常的询价单{}，报价开始时间和报价截止时间为必填项，不应为空",JSON.toJSONString(quotationBaseId));
            throw new ApiException(ResultCode.FAILURE.getCode(),"异常数据，报价开始时间和报价截止时间为必填项，不应为空！");
        }
        //询价单状态：报价中

        if (QuotationStatusEnum.QUOTATION.getCode().equals(quotationStatus)) {
            log.info("报价中");
            //供应商应标状态 :0未应标未拒绝
            if (resultQuotationBaseVo.getQuotationMarkStatus() == 0) {
                //未到报价截止时间
                if (new Date().before(quotationBase.getCutOffTime())){
                    log.info("未到报价截止时间");
                    resultQuotationBaseVo.setQuotationSaleDetailButtonStatus(new QuotationSaleDetailButtonStatus(true, true, true, false));
                }else { //报价截止
                    log.info("报价截止");
                    resultQuotationBaseVo.setQuotationSaleDetailButtonStatus(new QuotationSaleDetailButtonStatus(false, false, true, false));
                }
            } else if (resultQuotationBaseVo.getQuotationMarkStatus() == 1) {//1应标
                log.info("1应标");
                //当前轮次是否报过价
                if (resultQuotationBaseVo.getQuotedOfTheCurrentRound()) {//报过价
                    log.info("当前轮次报过价");
                    resultQuotationBaseVo.setQuotationSaleDetailButtonStatus(new QuotationSaleDetailButtonStatus(false, false, true, false));
                } else {//未报过价
                    log.info("当前轮次未报过价");
                    //没到询价开始时间
                    if ((LocalDateTime.now()).isBefore(quotationBase.getQuotationStartTime())){
//                    if ((localDateTime8Now).isBefore(quotationBase.getQuotationStartTime())){
                        log.info("没到询价开始时间");
                        resultQuotationBaseVo.setQuotationSaleDetailButtonStatus(new QuotationSaleDetailButtonStatus(false, false, true, false));
                    }else {//已经到了询价开始时间
                        log.info("已经到了询价开始时间");
                        //未到报价截止时间
                        if (new Date().before(quotationBase.getCutOffTime())){
//                        if (date8.before(quotationBase.getCutOffTime())){
                            log.info("未到报价截止时间");
                            resultQuotationBaseVo.setQuotationSaleDetailButtonStatus(new QuotationSaleDetailButtonStatus(false, false, true, true));
                        }else {//报价截止
                            log.info("报价截止");
                            resultQuotationBaseVo.setQuotationSaleDetailButtonStatus(new QuotationSaleDetailButtonStatus(false, false, true, false));
                        }
                    }
                }
            } else {//-1拒绝
                log.info("-1拒绝");
                throw new ApiException(ResultCode.REQ_REJECT.getCode(), "您暂无权限查看该询价单！");
            }
            //新的轮次
        } else if (QuotationStatusEnum.FRESH.getCode().equals(quotationStatus)) {
            //当前轮次，当前供应商，是否进行重报了
            RestateSupplier restateSupplier = restateSupplierService.getOne(Wrappers.<RestateSupplier>lambdaQuery()
                    .eq(RestateSupplier::getQuotationBaseId, quotationBaseId)
                    .eq(RestateSupplier::getSupplierId, supplierId)
                    .eq(RestateSupplier::getRoundNumber, quotationBase.getCurrentRoundNumber() - 1));
            //当前供应商没有重报：没有应标，没有报价，没有拒绝按钮
            if (restateSupplier == null) {
                //供应商应标状态：拒绝
                if (resultQuotationBaseVo.getQuotationMarkStatus() == -1) {
                    throw new ApiException(ResultCode.REQ_REJECT.getCode(), "您暂无权限查看该询价单！");
                }
                resultQuotationBaseVo.setQuotationSaleDetailButtonStatus(new QuotationSaleDetailButtonStatus(false, false, false, false));
            } else {//新的轮次，重报的供应商，不需要再次应标,可以再次报价
                //当前轮次是否报过价
                if (resultQuotationBaseVo.getQuotedOfTheCurrentRound()) {//报过价
                    resultQuotationBaseVo.setQuotationSaleDetailButtonStatus(new QuotationSaleDetailButtonStatus(false, false, false, false));
                } else {//未报过价
                    resultQuotationBaseVo.setQuotationSaleDetailButtonStatus(new QuotationSaleDetailButtonStatus(false, false, false, true));
                }
            }
        } else {
            //供应商应标状态：拒绝
            if (resultQuotationBaseVo.getQuotationMarkStatus() == -1) {
                throw new ApiException(ResultCode.REQ_REJECT.getCode(), "您暂无权限查看该询价单！");
            }
            resultQuotationBaseVo.setQuotationSaleDetailButtonStatus(new QuotationSaleDetailButtonStatus(false, false, false, false));
        }
        log.info("=============构建按钮结束====================");
    }

    @Override
    public QuotationBaseVo getInfoById(Long id) {
        QuotationBase quotationBase = getById(id);
        if (quotationBase == null) {
            throw new BizException(QuotationResultCode.SOURCE_NOT_FOUND);
        }
        //询价单设备信息字段
        QuotationBaseVo baseVo = BeanUtil.copy(quotationBase, QuotationBaseVo.class);
        LambdaQueryWrapper<QuotationEquipment> queryWrapper = Condition.getQueryWrapper(new QuotationEquipment())
                .lambda().eq(QuotationEquipment::getQuotationBaseId, id);
        List<QuotationEquipment> equipmentList = quotationEquipmentService.list(queryWrapper);
        baseVo.setEquipmentList(equipmentList);

        //加上扩展字段 QuotationLineBase QuotationLineExt
        LambdaQueryWrapper<QuotationLineBase> quotationLineBaseQueryWrapper = Condition.getQueryWrapper(new QuotationLineBase())
                .lambda().eq(QuotationLineBase::getQuotationBaseId, id);
        List<QuotationLineBase> quotationLineBaseList = quotationLineBaseService.list(quotationLineBaseQueryWrapper);
        List<QuotationLineBaseVo> quotationLineBaseVoList = BeanUtil.copyProperties(quotationLineBaseList, QuotationLineBaseVo.class);
        quotationLineBaseVoList.forEach(quotationLineBaseVo -> {
            LambdaQueryWrapper<QuotationLineExt> quotationLineExtQueryWrapper = Condition.getQueryWrapper(new QuotationLineExt()).lambda().eq(QuotationLineExt::getLineId, quotationLineBaseVo.getId()).orderByAsc(QuotationLineExt::getOrderValue);
            List<QuotationLineExt> quotationLineExts = quotationLineExtService.list(quotationLineExtQueryWrapper);
            quotationLineBaseVo.setQuotationLineExtList(quotationLineExts);
        });
        baseVo.setQuotationLineBaseVoList(quotationLineBaseVoList);

        //询价单报价供应商 QuotationSupplier
        LambdaQueryWrapper<QuotationSupplier> quotationSupplierQueryWrapper = Condition.getQueryWrapper(new QuotationSupplier())
                .lambda().eq(QuotationSupplier::getQuotationBaseId, id);
        List<QuotationSupplier> quotationSupplierList = quotationSupplierService.list(quotationSupplierQueryWrapper);
        baseVo.setQuotationSupplierList(quotationSupplierList);

        //附件需求清单
        LambdaQueryWrapper<QuotationAttRequire> quotationAttRequireQueryWrapper = Condition.getQueryWrapper(new QuotationAttRequire())
                .lambda().eq(QuotationAttRequire::getQuotationBaseId, id);
        List<QuotationAttRequire> quotationAttRequireList = quotationAttRequireService.list(quotationAttRequireQueryWrapper);
        baseVo.setQuotationAttRequireList(quotationAttRequireList);

        //附件需求清单 文件表
        LambdaQueryWrapper<QuotationAttachment> quotationAttachmentQueryWrapper = Condition.getQueryWrapper(new QuotationAttachment())
                .lambda().eq(QuotationAttachment::getBusinessFormId, id);
        List<QuotationAttachment> quotationAttachmentList = quotationAttachmentService.list(quotationAttachmentQueryWrapper);
        baseVo.setQuotationAttachmentList(quotationAttachmentList);

        //返回如果开标状态为不需要开标,把所有开标人员的数据清除再返回
        String needOpenBid = baseVo.getNeedOpenBid();
        if(StrUtil.equals("0",needOpenBid)){
            baseVo.setPurchaseUserId(null);
            baseVo.setPurchaseName(null);
            baseVo.setPurchaseEmployeeId(null);

            baseVo.setFinancialUserId(null);
            baseVo.setFinancialUserName(null);
            baseVo.setFinancialEmployeeId(null);

            baseVo.setAuditUserId(null);
            baseVo.setAuditUserName(null);
            baseVo.setAuditEmployeeId(null);

        }

        //添加按钮权限
        baseVo = addPermissionButton(baseVo);
        return baseVo;
    }

    /**
     * 查询详情用于Copy数据
     *
     * @param id 主键ID
     * @return 询价基础信息
     */
    @Override
    public QuotationBaseVo getInfoById4Copy(Long id) {
        return getInfoById(id);
    }

    @Override
    public Object calculateQuotationInfo(Long quotationId, List<QuotationLineExt> quotationLineExtList) {
        QuotationLineBase quotationLineBase = quotationLineBaseService.getByQuotationBaseId(quotationId);

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long saveInfo(QuotationBaseVo quotationBase) {
        quotationBase.setId(null);
        //quotationBase.setQuotationNo(new NumberFactory(NumberType.quotation).buildNumber());
        quotationBase.setQuotationNo(numberFactory.buildNumber(NumberType.quotation));
        quotationBase.setIsSendWarning(0);//0没有发送预警 1 已经发送预警
        quotationBase.setSupplierWarningSwitch(EarlyWranningEnum.CLOSE.getCode());//开关默认关闭
        save(quotationBase);
        cascadeSaveEquipment(quotationBase);

        BaseUserEntity user = AuthUtil.getUser();
        Long userId = user.getUserId();
        String userName = user.getUserName();

        //操作完成记录日志
        QuotationOperationLog operationLog = QuotationOperationLog.builder()
                .billId(quotationBase.getId())
                .content("询价单保存草稿")
                .operationNo(userId)
                .operatorName(userName)
                .build();
        operationLog.setCreatedDate(new Date());
        operationLogService.save(operationLog);
        return quotationBase.getId();
    }

    @Override
    public List<QuotationBaseData> convertDictCode(List<QuotationBase> list) {
        if (CollectionUtil.isNotEmpty(list)) {
            return list.stream().map(item -> {
                QuotationBaseData quotationBaseData = new QuotationBaseData();
                BeanUtil.copyProperties(item, quotationBaseData);
                quotationBaseData.setQuotationScopeName(dictItemService.getDictItemLabel(DictCodeEnum.quotation_scope.name(), quotationBaseData.getQuotationScope()));
                quotationBaseData.setQuotationStatusName(dictItemService.getDictItemLabel(DictCodeEnum.quotation_status.name(), quotationBaseData.getQuotationStatus()));
                quotationBaseData.setQuotationTypeName(dictItemService.getDictItemLabel(DictCodeEnum.quotation_type.name(), quotationBaseData.getQuotationType()));
                return quotationBaseData;
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateInfo(QuotationBaseVo quotationBase) {
        checkQuotationExist(quotationBase.getId());
        clearSupplierIfNecessary(quotationBase);
        updateById(quotationBase);
        clearEquipment(quotationBase.getId());
        cascadeSaveEquipment(quotationBase);

    }

    /**
     * 清空供应商信息，如有必要
     *
     * @param quotationBase 询价单基础信息
     */
    private void clearSupplierIfNecessary(QuotationBaseVo quotationBase) {
        QuotationBase quotationBaseDB = getById(quotationBase.getId());
        String quotationScope = quotationBaseDB.getQuotationScope() == null ? QuotationScopeEnum.SPECIFIED.getCode() : quotationBaseDB.getQuotationScope();
        //询价范围变更，清空供应商信息
        boolean necessary = !StringUtils.equals(quotationScope, quotationBase.getQuotationScope());
        if (necessary) {
            quotationSupplierService.deleteByQuotationBaseId(quotationBase.getId());
        }
    }


    /**
     * 导出销售协同的询价基础订单信息
     */
    @Override
    public void exportSaleExcelData(String fileName, QueryParam<QuotationBaseParam> pageParam, HttpServletResponse servletResponse) throws Exception {
        IPage<QuotationBaseVo> quotationBaseIPage = selectQuotationBaseSalePage(new Page<QuotationBase>(1, 100000), pageParam);
        List<QuotationBaseVo> quotationBases = quotationBaseIPage.getRecords();


        List<String> headColumnList = Lists.newArrayList("编号", "标题", "发布时间", "报价截止日期", "发布人", "审批状态");
        List<List<String>> dataList = new ArrayList<>();
        for (QuotationBase quotationBase : quotationBases) {
            List<String> quotationData = new ArrayList<>();
            quotationData.add(quotationBase.getQuotationNo());
            quotationData.add(quotationBase.getTitle());

            quotationData.add(Objects.isNull(quotationBase.getPublishTime()) ? "" : DateUtil.formatDateTime(quotationBase.getPublishTime()));
            quotationData.add(Objects.isNull(quotationBase.getCutOffTime()) ? "" : DateUtil.formatDateTime(quotationBase.getCutOffTime()));
            quotationData.add(quotationBase.getPublisher());
            quotationData.add(QuotationStatusEnum.getNameByCode(quotationBase.getQuotationStatus()));
            dataList.add(quotationData);
        }
        ExcelWrite.simpleWrite(servletResponse, fileName, "销售协同列表", dataList, headColumnList);
    }


    /**
     * 清空设备信息
     *
     * @param quotationBaseId 询价单基础信息主键ID
     */
    private void clearEquipment(Long quotationBaseId) {
        quotationEquipmentService.deleteByQuotationBaseId(quotationBaseId);
    }


    /**
     * 级联保存设备信息
     *
     * @param quotationBase 询价单基础信息
     */
    private void cascadeSaveEquipment(QuotationBaseVo quotationBase) {

        List<QuotationEquipment> equipmentList = quotationBase.getEquipmentList();
        if (CollectionUtil.isNotEmpty(equipmentList)) {
            List<QuotationEquipment> quotationEquipments = equipmentList.stream().map(item -> {
                QuotationEquipment quotationEquipment = new QuotationEquipment();
                BeanUtil.copyProperties(item, quotationEquipment, "id");
                quotationEquipment.setQuotationBaseId(quotationBase.getId());
                return quotationEquipment;
            }).collect(Collectors.toList());
            quotationEquipmentService.saveBatch(quotationEquipments);
        }
    }

    /**
     * 复制询价单详情
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void copyQuotation(Long id) {
        QuotationBaseVo infoById = getInfoById4Copy(id);
        initQuotation(infoById);
        //保存询价单基础信息
        save(infoById);
        //保存设备信息
        cascadeSaveEquipment(infoById);

        //保存询价单行以及扩展字段信息
        List<QuotationLineBaseVo> quotationLineBaseVos = infoById.getQuotationLineBaseVoList();

        for (QuotationLineBaseVo quotationLineBaseVo : quotationLineBaseVos) {
            quotationLineBaseVo.setId(null);
            quotationLineBaseVo.setQuotationBaseId(infoById.getId());
            quotationLineBaseService.save(quotationLineBaseVo);
            List<QuotationLineExt> quotationLineExts = quotationLineBaseVo.getQuotationLineExtList();
            quotationLineExts.forEach(quotationLineExt -> {
                quotationLineExt.setId(null);
                quotationLineExt.setQuotationBaseId(infoById.getId());
                quotationLineExt.setLineId(quotationLineBaseVo.getId());
            });
            quotationLineExtService.saveBatch(quotationLineExts);
        }
        //保存询价单供应商信息
        for (QuotationSupplier quotationSupplier : infoById.getQuotationSupplierList()) {
            quotationSupplier.setId(null);
            quotationSupplier.setResponseTime(null);
            quotationSupplier.setResponseStatus("no_response");

            quotationSupplier.setResponseStatus(QuotationSupplierResponseStatus.no_response.name());
            quotationSupplier.setQuotationBaseId(infoById.getId());
        }
        quotationSupplierService.saveBatch(infoById.getQuotationSupplierList());
        //保存询价附件清单
        for (QuotationAttRequire quotationAttRequire : infoById.getQuotationAttRequireList()) {
            quotationAttRequire.setId(null);
            quotationAttRequire.setQuotationBaseId(infoById.getId());
        }
        quotationAttRequireService.saveBatch(infoById.getQuotationAttRequireList());
        //保存询价附件
        for (QuotationAttachment quotationAttachment : infoById.getQuotationAttachmentList()) {
            quotationAttachment.setId(null);
            quotationAttachment.setBusinessFormId(infoById.getId());
        }
        quotationAttachmentService.saveBatch(infoById.getQuotationAttachmentList());
    }

    /**
     * 初始化询价单
     *
     * @param targetQuotation
     */
    private void initQuotation(QuotationBaseVo targetQuotation) {
        targetQuotation.setSourceQuotationBaseId(targetQuotation.getId());
        targetQuotation.setId(null);
        //targetQuotation.setQuotationNo(new NumberFactory(NumberType.quotation).buildNumber());
        targetQuotation.setQuotationNo(numberFactory.buildNumber(NumberType.quotation));
        targetQuotation.setQuotationStatus(QuotationStatusEnum.DRAFT.getCode());
        targetQuotation.setResponseNum(0);
        targetQuotation.setBiddingNum(0);
        //当前报价轮次，默认1
        targetQuotation.setCurrentRoundNumber(1);
        targetQuotation.setPublisher(null);
        targetQuotation.setPublishTime(null);

    }

    @Override
    public void updateDate(QuotationUpdateDateVo quotationUpdateDateVo) {
        QuotationBase quotationBase = this.getOne(new LambdaQueryWrapper<QuotationBase>().eq(QuotationBase::getId, quotationUpdateDateVo.getId()));
        if (quotationBase == null) {
            throw new ApiException(500, "查询不到这个询价单");
        }
        quotationBase.setCutOffTime(quotationUpdateDateVo.getCutOffTime());
        if (QuotationStatusEnum.FAILURE_BID.getCode().equals(quotationBase.getQuotationStatus()) ||
                QuotationStatusEnum.HAVE_PRICING.getCode().equals(quotationBase.getQuotationStatus()) ||
                QuotationStatusEnum.OBSOLETE.getCode().equals(quotationBase.getQuotationStatus()) ||
                QuotationStatusEnum.COMPARISON.getCode().equals(quotationBase.getQuotationStatus())
        ) {
            throw new BizException(QuotationResultCode.FORBIDDEN_CHANGE_DATE);
        }
        super.updateById(quotationBase);
    }

    public List<List<BiddingLineExt>> getTotal(List<List<BiddingLineExt>> data02) {


        for (List<BiddingLineExt> biddingLineExts : data02) {
            BigDecimal total = new BigDecimal("0");

            if (biddingLineExts.size() > 0) {

                //计算total
                for (BiddingLineExt biddingLineExt : biddingLineExts) {


                    if (!StringUtil.isEmpty(biddingLineExt.getExpression())) {
                        total = total.add(biddingLineExt.getExpressionValue());
                    }
                }

                BiddingLineExt biddingLineExtForTotal = new BiddingLineExt();
                biddingLineExtForTotal.setTitle("合计");
                biddingLineExtForTotal.setField("合计");
                biddingLineExtForTotal.setValue(total.toString());
                biddingLineExtForTotal.setExpression("合计");
                biddingLineExtForTotal.setExpressionValue(total);
                biddingLineExtForTotal.setOrderValue(biddingLineExts.size() + 1);
                biddingLineExtForTotal.setDataType("expression");

                biddingLineExts.add(biddingLineExtForTotal);


            }
        }

        return data02;
    }

    @Override
    public void exportData(QuotationBase quotationBase, HttpServletResponse response) {

        //获取列名称(
        QuotationSupplier quotationSupplier = new QuotationSupplier();
        quotationSupplier.setQuotationBaseId(quotationBase.getId());
        List<QuotationSupplier> quotationSuppliers = quotationSupplierService.queryList(quotationSupplier, null);
        //创建表头行的数据columns
        List<List<String>> columns = getColumns(quotationSuppliers);

        //获取data01
        QuotationLineExt quotationLineExt = new QuotationLineExt();
        quotationLineExt.setQuotationBaseId(quotationBase.getId());
        //查询询价单的报价信息模板
        List<QuotationLineExt> preData01 = quotationLineExtService.queryList(quotationLineExt);
        List<List<String>> data01 = convertQuotationLineExtDataExcel(preData01);

        //获取data02
        List<List<BiddingLineExt>> data02 = new ArrayList<>();
        for (QuotationSupplier q : quotationSuppliers) {
            try {
                //获取最新报价的记录id
                Long lastQuotationRecordId = getLastQuotationRecordId(quotationBase.getId(), q.getSupplierId());

                //然后获取每个供应商的报价信息
                BiddingLineExt biddingLineExt = new BiddingLineExt();
                biddingLineExt.setQuotationBaseId(quotationBase.getId());
                biddingLineExt.setSupplierId(q.getSupplierId());
                biddingLineExt.setQuotationRecordId(lastQuotationRecordId);
                List<BiddingLineExt> preData02 = biddingLineExtService.queryList(biddingLineExt);

                convertBiddingLineExtsDataExcel(preData02);//排序
                data02.add(preData02);//data02为多个供应商报价数据的列列
            } catch (Exception ex) {
                //可能会没有报价记录 那就不管了
                log.error("[导出报价对比] 处理数据error!", ex);
                throw new ApiException(ex.getMessage());
            }
        }
        //m没有合计，所以会报数据越界异常，代码自己生成合计
        data02 = getTotal(data02);

        //合并数据
        List<List<String>> mergeData = mergeExcelData(data01, data02);


        ExcelWrite.simpleWrite(response, "报价单详细页导出", "报价单详细页"
                , mergeData
                , columns
                , getMergeOffset(preData01)//计算出有公式的数据的个数（总价，人工成本等）
        );
    }


    public Long getLastQuotationRecordId(Long quotationBaseId, Long supplierId) {
        TRfqQuotationRecord tRfqQuotationRecord = new TRfqQuotationRecord();
        tRfqQuotationRecord.setQuotationBaseId(quotationBaseId);
        tRfqQuotationRecord.setSupplierId(supplierId);
        List<TRfqQuotationRecord> tRfqQuotationRecordQuerylist = tTRfqQuotationRecordService.querylistOrderByTimes(tRfqQuotationRecord, false);
        if (CollectionUtil.isNotEmpty(tRfqQuotationRecordQuerylist)) {
            return tRfqQuotationRecordQuerylist.get(0).getId();
        }
        return null;
    }

    public Integer getMergeOffset(List<QuotationLineExt> preData01) {
        Integer i = 0;
        for (QuotationLineExt quotationLineExt : preData01) {
            if ("expression".equals(quotationLineExt.getDataType())) {
                i++;
            }
        }
        i++;
        return i;
    }

    public List<List<String>> mergeExcelData(List<List<String>> data01, List<List<BiddingLineExt>> data02) {
        for (int i = 0; i < data01.size(); i++) {
            List<String> all = data01.get(i);
            for (List<BiddingLineExt> supplierInfo : data02) {
                if (supplierInfo.size() > 0) {
                    all.add(supplierInfo.get(i).getValue());
                }
            }
        }
        return data01;
    }


    public List<List<String>> convertQuotationLineExtDataExcel(List<QuotationLineExt> biddingLineExts) {
        Collections.sort(biddingLineExts, new Comparator<QuotationLineExt>() {
            @Override
            public int compare(QuotationLineExt o1, QuotationLineExt o2) {
                BigDecimal order01 = new BigDecimal(o1.getOrderValue());
                BigDecimal order02 = new BigDecimal(o2.getOrderValue());
                return order01.compareTo(order02);
            }
        });

        //排好序之后（orderValue从小到大）
        List<List<String>> all = new ArrayList();
        for (int i = 0; i < biddingLineExts.size(); i++) {
            QuotationLineExt quotationLineExt = biddingLineExts.get(i);
            List<String> list = new ArrayList();
            if (StringUtils.isEmpty(quotationLineExt.getExpression())) {
                list.add("物料比价");
                list.add(quotationLineExt.getTitle());
            } else {
                list.add("其他比价");
                list.add(quotationLineExt.getExpression());
            }



            /*if (i <= biddingLineExts.size() - 5) {
                list.add("物料比价");
                list.add(quotationLineExt.getValue());
            } else if (i > biddingLineExts.size() - 5) {
                list.add("其他比价");
                list.add(quotationLineExt.getValue());
            }*/
            all.add(list);
            //这里添加的是一行一个list
        }
        List<String> buttom = new ArrayList();
        buttom.add("总报价");
        buttom.add("总报价");
        all.add(buttom);
        return all;
    }


    public void convertBiddingLineExtsDataExcel(List<BiddingLineExt> biddingLineExts) {
        Collections.sort(biddingLineExts, new Comparator<BiddingLineExt>() {
            @Override
            public int compare(BiddingLineExt o1, BiddingLineExt o2) {
                BigDecimal order01 = new BigDecimal(o1.getOrderValue());
                BigDecimal order02 = new BigDecimal(o2.getOrderValue());
                return order01.compareTo(order02);
            }
        });
    }

    public List<List<String>> getColumns(List<QuotationSupplier> preColumns) {
        List<List<String>> columns = new ArrayList();
        List<String> data01 = new ArrayList();
        data01.add("模块信息");
        List<String> data02 = new ArrayList();
        data02.add("模板字段名");
        columns.add(data01);
        columns.add(data02);
        for (QuotationSupplier quotationSupplier : preColumns) {
            List<String> ext = new ArrayList();
            ext.add(quotationSupplier.getSupplierName());
            columns.add(ext);
        }
        return columns;
    }


    @Override
    @OperationLog(code = "quotation_disable", content = "询价单作废")
    public void updateDisableStatus(Long quotationId) {
        QuotationBase quotationBase = new QuotationBase();
        quotationBase.setId(quotationId);

        QuotationBase base = this.getById(quotationId);
        String quotationStatus = base.getQuotationStatus();
        if (quotationStatus.equals(QuotationStatusEnum.DRAFT.getCode())) {
            //如果是草稿状态下作废，设置草稿状态下作废标记（供应商进行过滤）
            quotationBase.setDraftForObsolete("1");
        }
        quotationBase.setQuotationStatus(QuotationStatusEnum.OBSOLETE.getCode());
        super.updateById(quotationBase);
    }


    @Override
    @OperationLog(code = "quotation_invalide", content = "询价单流标")
    public void updateInvalideStatus(Long quotationId) {
        QuotationBase quotationBase = new QuotationBase();
        quotationBase.setId(quotationId);
        quotationBase.setQuotationStatus(QuotationStatusEnum.FAILURE_BID.getCode());
        super.updateById(quotationBase);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @OperationLog(code = "quotation_save_quote_price_info", content = "供应商报价")
    public void saveQuotePriceInfo(Long quotationId, List<QuotationLineExtVo> quotationLineExtList) {
        //校验字段是否必填
        for (QuotationLineExtVo quotationLineExtVo : quotationLineExtList) {
            if (!StrUtil.isEmpty(quotationLineExtVo.getRequired())
                    && quotationLineExtVo.getRequired().equals("true")
                    && StrUtil.isEmpty(quotationLineExtVo.getValue())) {
                throw new ApiException(500, quotationLineExtVo.getTitle() + "必填");
            }
        }

        //保存之前校验是否有必须上传的文件
        LambdaQueryWrapper<QuotationAttRequire> query = Condition.getQueryWrapper(new QuotationAttRequire())
                .lambda()
                .eq(QuotationAttRequire::getQuotationBaseId, quotationId);
        //查询出所有的需求文件
        List<QuotationAttRequire> list = quotationAttRequireService.list(query);
        for (QuotationAttRequire quotationAttRequire : list) {
            //如果是必须需要
            if (quotationAttRequire.getRequired().equals("Y")) {
                Long requireId = quotationAttRequire.getId();

                LambdaQueryWrapper<QuotationAttRequireAttachment> queryAtt = Condition.getQueryWrapper(new QuotationAttRequireAttachment())
                        .lambda()
                        .eq(QuotationAttRequireAttachment::getQuotationAttRequireId, requireId);
                List<QuotationAttRequireAttachment> quotationAttRequireAttachments = iQuotationAttRequireAttachmentService.getBaseMapper().selectList(queryAtt);
                if (quotationAttRequireAttachments.size() == 0) {

                    throw new ApiException(500, "需要的需求文件没有上传");
                }
            }

        }

        BaseUserEntity baseUserEntity = AuthUtil.getUser();
        Supplier supplier = baseUserEntity.getCurrentSupplier();
        if (supplier == null) {
            throw new ApiException(500, "供应商信息异常");
        }

        QuotationBase quotationBase = getById(quotationId);
        if (quotationBase == null) {
            throw new ApiException(500, "报价单信息异常");
        }
        LambdaQueryWrapper<TRfqQuotationRecord> tRfqQuotationRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tRfqQuotationRecordLambdaQueryWrapper.eq(TRfqQuotationRecord::getQuotationBaseId, quotationId)
                .eq(TRfqQuotationRecord::getSupplierId, supplier.getSupplierId())
                .eq(TRfqQuotationRecord::getRoundNumber, quotationBase.getCurrentRoundNumber());
        //当前的轮次的报价记录
        List<TRfqQuotationRecord> tRfqQuotationRecords = tTRfqQuotationRecordService.list(tRfqQuotationRecordLambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(tRfqQuotationRecords)) {
            throw new ApiException(500, "当前轮次 您已经报过价了");
        }


        //校验必须上传的附件是否上传完毕
        List<QuotationAttRequire> quotationAttRequireList = quotationAttRequireService.list(new LambdaQueryWrapper<QuotationAttRequire>().eq(QuotationAttRequire::getQuotationBaseId, quotationId).eq(QuotationAttRequire::getRequired, 1));
        quotationAttRequireList.forEach(quotationAttRequire -> {
            List<QuotationAttRequireAttachmentVo> filesByQuotationIdAndAttRequireId = iQuotationAttRequireAttachmentService.getFilesByQuotationIdAndAttRequireId(quotationId, quotationAttRequire.getId());
            if (Objects.isNull(filesByQuotationIdAndAttRequireId) || filesByQuotationIdAndAttRequireId.isEmpty()) {
                throw new ApiException(500, quotationAttRequire.getRemark() + "的附件必须要上传");
            }
        });

        TRfqQuotationRecord tRfqQuotationRecord = new TRfqQuotationRecord();
        tRfqQuotationRecord.setQuotationBaseId(quotationId);

        //保存新的记录
        TRfqQuotationRecord saveTRfqQuotationRecord = new TRfqQuotationRecord();
        saveTRfqQuotationRecord.setRoundNumber(quotationBase.getCurrentRoundNumber());
        saveTRfqQuotationRecord.setExtContent(JSON.toJSONString(quotationLineExtList));
        saveTRfqQuotationRecord.setQuotationNo(quotationBase.getQuotationNo());
        saveTRfqQuotationRecord.setQuoteTime(OffsetDateTime.now());
        saveTRfqQuotationRecord.setQuotationBaseId(quotationId);
        saveTRfqQuotationRecord.setTenderer(baseUserEntity.getUserName());
//        saveTRfqQuotationRecord.setStatus("PENDING");
        saveTRfqQuotationRecord.setSupplierName(supplier.getSupplierName());
        saveTRfqQuotationRecord.setSupplierCode(supplier.getSupplierCode());
        saveTRfqQuotationRecord.setSupplierId(supplier.getSupplierId());
        tTRfqQuotationRecordService.save(saveTRfqQuotationRecord);


        //计算字段 计算
        List<QuotationLineExt> inQuotationLineExt = BeanUtil.copy(quotationLineExtList, QuotationLineExt.class);
        List<QuotationLineExtVo> computedLineExtVos = computeLineExtPrice(quotationId, inQuotationLineExt);
        quotationLineExtList.addAll(computedLineExtVos);

        List<BiddingLineExt> biddingLineExtList = new ArrayList<>();
        AtomicInteger orderValue = new AtomicInteger();
        for (QuotationLineExt quotationLineExt : quotationLineExtList) {
            if (YesOrNoNumber.YES.getCode().equals(quotationLineExt.getRequired()) && StringUtils.isEmpty(quotationLineExt.getValue())) {
                throw new ApiException(500, quotationLineExt.getTitle() + "必输哦~");
            }
            BiddingLineExt biddingLineExt = new BiddingLineExt();
            BeanUtil.copyProperties(quotationLineExt, biddingLineExt);
            biddingLineExt.setId(null);
            biddingLineExt.setQuotationBaseId(quotationId);
            biddingLineExt.setQuotationRecordId(saveTRfqQuotationRecord.getId());
            biddingLineExt.setSupplierId(supplier.getSupplierId());
            biddingLineExt.setQuoteRound(Integer.toString(quotationBase.getCurrentRoundNumber()));
            biddingLineExt.setBizType(BiddingLineExtBizTypeEnum.NORMAL.name());
            biddingLineExt.setOrderValue(orderValue.incrementAndGet());
            biddingLineExtList.add(biddingLineExt);
        }

        biddingLineExtService.saveBatch(biddingLineExtList);

        //修改供应商响应状态为 已报价
        quotationSupplierService.updateResponseStatus(quotationId, supplier.getSupplierId(), QuotationSupplierResponseStatus.bid.name());

        //模板{companyName}对{inquiryNo}询价单进行报价
        BaseUserEntity user = AuthUtil.getUser();
        String companyName = user.getCurrentSupplier().getSupplierName();
        Map<String, Object> params = new HashMap<>();
        params.put("inquiryNo", quotationBase.getQuotationNo());
        params.put("companyName", companyName);

        //获取当前登录用户
        LoginInfo loginInfo = null;
        Optional<LoginInfo> optional = SecurityUtils.getLoginInfo();
        if (optional.isPresent()) {
            loginInfo = optional.get();
        } else {
            log.warn("获取用户登录信息失败");
        }

        //构建消息体data
        QuotationSupplierQuotedPriceEventData quotationSupplierQuotedPriceEventData = convertEvent(user.getCurrentSupplier().getSupplierCode(), user.getCurrentSupplier().getSupplierName(), smsMsgTempConfig.getSupplierPleaseInformOfInquiry(),
                JSON.toJSONString(params), quotationBase, loginInfo);

        //创建消息事件
        QuotationSupplierQuotedPriceEvent quotationSupplierQuotedPriceEvent = new QuotationSupplierQuotedPriceEvent(this, loginInfo, quotationSupplierQuotedPriceEventData,
                quotationSupplierQuotedPriceEventData.getSupplierCode(), quotationSupplierQuotedPriceEventData.getSupplierName());


        QuotationSupplierQuotedPriceSmsEvent quotationSupplierQuotedPriceSmsEvent = new QuotationSupplierQuotedPriceSmsEvent(this, loginInfo, quotationSupplierQuotedPriceEventData,
                quotationSupplierQuotedPriceEventData.getSupplierCode(), quotationSupplierQuotedPriceEventData.getSupplierName());


        //发送消息通知
        defaultEventPublisher.publishEvent(quotationSupplierQuotedPriceEvent);
//        defaultEventPublisher.publishEvent(quotationSupplierQuotedPriceSmsEvent);


    }


    public QuotationSupplierQuotedPriceEventData convertEvent(String supplierCode, String supplierName,
                                                              String smsTmpCode, String msg, QuotationBase quotationBase, LoginInfo loginInfo) {
        //发送消息通知
        log.info("发送消息的参数为{}",JSON.toJSONString(supplierCode+","+supplierName+","+ smsTmpCode+","+ msg+","+  quotationBase +","+ loginInfo) );

        //获取orgId
        Long orgId = null;
        OrgIdQuery orgIdQuery = new OrgIdQuery();
        orgIdQuery.setSupplierCode(supplierCode);
        orgIdQuery.setPurchaseCode(quotationBase.getCustomerCode());
        R<OrgIdDTO> orgIdDTO = purchaserFeignClient.info(orgIdQuery);
        log.info("orgIdDTO:{}",JSON.toJSONString(orgIdDTO));
        if (orgIdDTO.isSuccess()) {
            if(ObjectUtil.isEmpty(orgIdDTO.getData())){
                //没有建立关系不发送消息
                orgId=null;
                log.info("供应商"+supplierName+"没有建立关系，不能发送消息",JSON.toJSONString(supplierName+":"+orgIdDTO));
            }else{
                orgId = orgIdDTO.getData().getOrgId();
            }
            //orgId= ObjectUtil.isEmpty(orgIdDTO.getData())? 112L : orgIdDTO.getData().getOrgId();
            //orgId = orgIdDTO.getData().getOrgId();
        } else {
            throw new ApiException(500, "获取orgId异常");
        }

        QuotationSupplierQuotedPriceEventData quotationSupplierQuotedPriceEventData = new QuotationSupplierQuotedPriceEventData();
        if (loginInfo != null) {
            BeanUtil.copyProperties(quotationBase, quotationSupplierQuotedPriceEventData);
            quotationSupplierQuotedPriceEventData.setSupplierCode(supplierCode);
            quotationSupplierQuotedPriceEventData.setSupplierName(supplierName);
            quotationSupplierQuotedPriceEventData.setOrgId(orgId);
            quotationSupplierQuotedPriceEventData.setSmsMsgType(1);
            quotationSupplierQuotedPriceEventData.setSmsParams(msg);
            quotationSupplierQuotedPriceEventData.setSmsTempCode(smsTmpCode);
            quotationSupplierQuotedPriceEventData.setBusinessId(quotationBase.getId());
        }
        return quotationSupplierQuotedPriceEventData;
    }

    @Override
    @OperationLog(code = "quotation_restate", content = "询价单重报")
    public void restateDo(Long quotationBaseId, QuotationBaseRestateParam quotationBaseRestateParam) {

        //QuotationBaseRestateParam 参数校验
        quotationBaseRestateParamCheck(quotationBaseId, quotationBaseRestateParam);
        //查看用户有无权限重报,查看询价单重报次数是否已上限，
        QuotationBase quotationBase = restateCheck(quotationBaseId);
        //[t_rfq_quotation_base] 询价单表当前重报次数+1和修改当前询价单的状态为新的伦次
        Integer currentRoundNumber = updateCurrentRoundNumber(quotationBase);
        //[t_rfq_bidding_line_ext]招标行信息表插入期望的物料数据,期望类型，供应商为空，计算公式、调用工具计算结果
        biddingLineExtService.saveRestateBiddingLineExt(quotationBaseId, currentRoundNumber);
        //插入 [isPushCheapest]字段，`要新建表保存`
        saveRestateBase(quotationBaseRestateParam.getPushCheapest(), quotationBaseId, currentRoundNumber);
        //插入重新报价的供应商ID,`要新建表保存`
        List<QuotationSupplier> quotationSuppliers = saveRestateSupplier(quotationBaseRestateParam.getSupplierIds(), quotationBaseId, currentRoundNumber);
        //发送站内信和短信
        pricingRecordService.sendRestateMsg(quotationSuppliers, quotationBaseId);
        //修改供应商响应状态为 confirmed 已确认
        quotationSupplierService.updateResponseStatusBatch(quotationBaseId, quotationBaseRestateParam.getSupplierIds(), QuotationSupplierResponseStatus.confirmed.name());


    }


    private void quotationBaseRestateParamCheck(Long quotationBaseId, QuotationBaseRestateParam quotationBaseRestateParam) {
        Long[] supplierIds = quotationBaseRestateParam.getSupplierIds();
        if (Objects.isNull(supplierIds) || supplierIds.length == 0) {
            throw new ApiException(400, "请选择重新报价的供应商");
        }
        QuotationMark quotationMark = new QuotationMark();
        quotationMark.setQuotationId(quotationBaseId);
        List<QuotationMarkVo> quotationMarkVos = quotationMarkService.markedSupplier(quotationMark);
        Map<Long, QuotationMarkVo> quotationMarkVosHashMap = new HashMap<>();

        for (QuotationMarkVo quotationMarkVo : quotationMarkVos) {
            quotationMarkVosHashMap.put(quotationMarkVo.getSupplierId(), quotationMarkVo);
        }
        for (Long supplierId : supplierIds) {
            if (quotationMarkVosHashMap.get(supplierId) == null) {
                log.info("请选择参与过报价的供应商 :{}", supplierId);
                throw new ApiException(400, "请选择参与过报价的供应商");
            }
        }
        String pushCheapest = quotationBaseRestateParam.getPushCheapest();
        if (StringUtils.isEmpty(pushCheapest) || !(pushCheapest.equals("0") || pushCheapest.equals("1"))) {
            throw new ApiException(400, "请选择是否推送每行最低报价行");
        }
    }

    private List<QuotationSupplier> saveRestateSupplier(Long[] supplierIds, Long quotationBaseId, Integer currentRoundNumber) {
        Long userId = AuthUtil.getUser().getUserId();
        List<QuotationSupplier> quotationSuppliers = quotationSupplierService.getBaseMapper().selectList(Wrappers.<QuotationSupplier>lambdaQuery()
                .in(QuotationSupplier::getSupplierId, supplierIds)
                .eq(QuotationSupplier::getQuotationBaseId, quotationBaseId));
        List<RestateSupplier> restateSupplierList = new ArrayList<>();
        RestateSupplier restateSupplier;

        for (QuotationSupplier quotationSupplier : quotationSuppliers) {

            restateSupplier = new RestateSupplier();
            restateSupplier.setSupplierId(quotationSupplier.getSupplierId());
            restateSupplier.setSupplierCode(quotationSupplier.getSupplierCode());
            restateSupplier.setSupplierName(quotationSupplier.getSupplierName());
            restateSupplier.setSupplierAccount(quotationSupplier.getSupplierAccount());
            restateSupplier.setQuotationBaseId(quotationBaseId);
            restateSupplier.setRoundNumber(currentRoundNumber);
            restateSupplier.setCreatedBy(userId);
            restateSupplier.setLastModifiedBy(userId);

            restateSupplierList.add(restateSupplier);
        }
        restateSupplierService.saveBatch(restateSupplierList);

        return quotationSuppliers;
    }

    private void saveRestateBase(String pushCheapest, Long quotationBaseId, Integer roundNumber) {
        RestateBase restateBase = new RestateBase();
        restateBase.setPushCheapest(pushCheapest);
        restateBase.setQuotationBaseId(quotationBaseId);
        restateBase.setRoundNumber(roundNumber);
        restateBaseService.save(restateBase);
    }

    private Integer updateCurrentRoundNumber(QuotationBase quotationBase) {
        Integer currentRoundNumber = quotationBase.getCurrentRoundNumber();
        baseMapper.updateCurrentRoundNumber(quotationBase.getId());
        return currentRoundNumber;
    }

    @Override
    public QuotationBase restateCheck(Long quotationBaseId) {
        QuotationBase quotationBase = baseMapper.selectById(quotationBaseId);
        if (Objects.isNull(quotationBase)) {
            throw new ApiException(400, "询价单不存在");
        }
        if (quotationBase.getRoundNumber() < quotationBase.getCurrentRoundNumber()) {
            throw new ApiException(400, "已超出本次询价重新报价次数！");
        }
        BaseUserEntity baseUserEntity = AuthUtil.getUser();
        Long userId = baseUserEntity.getUserId();

        /*if (!quotationBase.getCreatedBy().equals(userId)) {
            throw new ApiException(400, "用户无权限重新报价");
        }*/

        return quotationBase;
    }

    @Override
    public Object restateEnableSupplier(Long quotationBaseId) {
        return null;
    }

    @Override
    public QuotationStaticCountVo getQuotationStaticCount() {
        Long supplierId;
        try {
            supplierId = AuthUtil.getUser().getCurrentSupplier().getSupplierId();
        } catch (Exception e) {
            throw new BizException(SUPPLIER_NOT_FOUND);
        }
        if (supplierId == null) {
            throw new BizException(SUPPLIER_NOT_FOUND);
        }
        QuotationStaticCountVo quotationStaticCountVo = new QuotationStaticCountVo();

        quotationStaticCountVo.setNoQuotationCount(baseMapper.queryQuotationStatusCount(supplierId, QuotationStatusEnum.NO_QUOTATION.getCode()));
        quotationStaticCountVo.setQuotationCount(baseMapper.queryQuotationStatusCount(supplierId, QuotationStatusEnum.QUOTATION.getCode()));
        quotationStaticCountVo.setComparisonCount(baseMapper.queryQuotationStatusCount(supplierId, QuotationStatusEnum.COMPARISON.getCode()));
        quotationStaticCountVo.setDraftCount(baseMapper.queryQuotationStatusCount(supplierId, QuotationStatusEnum.DRAFT.getCode()));
        quotationStaticCountVo.setFreshCount(baseMapper.queryQuotationStatusCount(supplierId, QuotationStatusEnum.FRESH.getCode()));
        quotationStaticCountVo.setBidOpeningCount(baseMapper.queryQuotationStatusCount(supplierId, QuotationStatusEnum.BID_OPENING.getCode()));
        quotationStaticCountVo.setObsoleteCount(baseMapper.draftForObsoleteCount(supplierId));
        quotationStaticCountVo.setFailureBidCount(baseMapper.queryQuotationStatusCount(supplierId, QuotationStatusEnum.FAILURE_BID.getCode()));
        quotationStaticCountVo.setHavePricingCount(baseMapper.queryQuotationStatusCount(supplierId, QuotationStatusEnum.HAVE_PRICING.getCode()));

        return quotationStaticCountVo;
    }

    @Override
    public Integer getQuotationSingleStaticCount(String status) {
        Long supplierId;
        try {
            supplierId = AuthUtil.getUser().getCurrentSupplier().getSupplierId();
        } catch (Exception e) {
            throw new BizException(SUPPLIER_NOT_FOUND);
        }
        if (supplierId == null) {
            throw new BizException(SUPPLIER_NOT_FOUND);
        }
        return baseMapper.queryQuotationStatusCount(supplierId, status);
    }

    /**
     * 询价单数量统计
     *
     * @return 统计信息
     */
    @Override
    public QuotationStaticCountVo statics() {
        QuotationStaticCountVo staticCountVo = new QuotationStaticCountVo();
        staticCountVo.setDraftCount(countByStatus(QuotationStatusEnum.DRAFT.getCode()));
        staticCountVo.setQuotationCount(countByStatus(QuotationStatusEnum.QUOTATION.getCode()));
        staticCountVo.setBidOpeningCount(countByStatus(QuotationStatusEnum.BID_OPENING.getCode()));
        staticCountVo.setComparisonCount(countByStatus(QuotationStatusEnum.COMPARISON.getCode()));
        staticCountVo.setFailureBidCount(countByStatus(QuotationStatusEnum.FAILURE_BID.getCode()));
        staticCountVo.setHavePricingCount(countByStatus(QuotationStatusEnum.HAVE_PRICING.getCode()));
        staticCountVo.setObsoleteCount(countByStatus(QuotationStatusEnum.OBSOLETE.getCode()));
        staticCountVo.setFreshCount(countByStatus(QuotationStatusEnum.FRESH.getCode()));
        staticCountVo.setNoQuotationCount(countByStatus(QuotationStatusEnum.NO_QUOTATION.getCode()));
        return staticCountVo;
    }

    /**
     * 列表查询
     *
     * @param queryParam 查询参数
     * @return 询价单列表
     */
    @Override
    public List<QuotationBase> queryList(QueryParam<QuotationBaseParam> queryParam) {
        return baseMapper.selectListPage(null, queryParam);
    }

    /**
     * 列表查询-销售工作台
     *
     * @param queryParam
     * @return
     */
    @Override
    public List<QuotationBase> querySaleList(QueryParam<QuotationBaseParam> queryParam) {
        Long supplierId = AuthUtil.getUser().getCurrentSupplier().getSupplierId();
        if (supplierId == null) {
            throw new BizException(SUPPLIER_NOT_FOUND);
        }
        if (queryParam.getParam() == null) {
            throw new BizException(PARAM_ERROR);
        }

        queryParam.getParam().setSupplierId(supplierId);
        return baseMapper.selectQuotationBaseSalePage(null, queryParam);
    }



    @OperationLog(content = "发布询价单", code = "quotation_publish")
    @Override
    public Long publish(Long quotationBaseId) {
        String lockKey  = QUOTATION_PUBLISH_PREFIX_KEY + quotationBaseId;

        LockWrapper lockWrapper = new LockWrapper().setKey(lockKey).setWaitTime(0).setLeaseTime(10).setUnit(TimeUnit.MINUTES);;
        distributedLock.tryLock(lockWrapper,()->{
            QuotationBase quotationBase = getById(quotationBaseId);
            if (quotationBase == null) {
                throw new BizException(QuotationResultCode.SOURCE_NOT_FOUND);
            }
            //修改询价单状态
            update(Wrappers.<QuotationBase>update().lambda()
                    .set(QuotationBase::getQuotationStatus, QuotationStatusEnum.QUOTATION.getCode())
                    .set(QuotationBase::getPublishTime, new Date())
                    .set(QuotationBase::getPublisher, AuthUtil.getUser().getUserName())
                    .eq(QuotationBase::getId, quotationBaseId));

            //询价单发布 发送消息通知供应商
            try{
                quotationPublishNoticeSupplier(quotationBaseId,quotationBase );
            }catch (Exception e){
                log.info("询价单"+quotationBaseId+"发布发送消息时发生异常，{}",JSON.toJSONString(e.getMessage()));
            }
            return quotationBaseId;
        },()->{
            throw new RuntimeException("询价单正在发布，不能发布多次");
        });
        return quotationBaseId;
    }

    /**
     * 询价单发布后发送消息通知供应商
     *
     * @param quotationBaseId 询价单基础信息ID
     * @param quotationBase 询价单基础信息
     * @return 构建好的消息体
     */
    void quotationPublishNoticeSupplier(Long quotationBaseId,QuotationBase quotationBase){
        //发布成功，发送消息通知供应商
        //获取当前登录用户
        BaseUserEntity user = AuthUtil.getUser();

        LoginInfo loginInfo = null;
        Optional<LoginInfo> optional = SecurityUtils.getLoginInfo();
        if (optional.isPresent()) {
            loginInfo = optional.get();
        } else {
            log.warn("获取用户登录信息失败");
        }

        //获取供应商code
        LambdaQueryWrapper<QuotationSupplier> lambda = Condition.getQueryWrapper(new QuotationSupplier())
                .lambda()
                .eq(QuotationSupplier::getQuotationBaseId, quotationBaseId);
        List<QuotationSupplier> list = quotationSupplierService.list(lambda);
        log.info("供应商信息list为：{}",JSON.toJSONString(list));
        for (QuotationSupplier quotationSupplier : list) {
            //循环供应商发送消息
            log.info("------开始发送消息------");
            log.info("此供应商为：{}",JSON.toJSONString(quotationSupplier));
            //构建消息体
            if(  StrUtil.isEmpty( quotationSupplier.getSupplierCode()  )){
                log.info("供应商编码为空{}",JSON.toJSONString(quotationSupplier));
                throw new ApiException(501,"供应商编码为空:"+JSON.toJSONString(quotationSupplier));
            }

            QuotationSupplierQuotedPriceEventData eventData = convertEvent(quotationSupplier.getSupplierCode(), quotationSupplier.getSupplierName(),
                    smsMsgTempConfig.getSupplierPleaseInformOfInquiry(), JSON.toJSONString(new HashMap<String, Object>()), quotationBase, loginInfo);

            QuotationInfopublishEvent quotationInfopublishEvent = new QuotationInfopublishEvent(this, loginInfo, eventData, eventData.getCustomerCode(), eventData.getCustomerName());
            //发送消息通知
            defaultEventPublisher.publishEvent(quotationInfopublishEvent);
        }
    }

    /**
     * 供应商应标数量 +1
     *
     * @param quotationBaseId 询价单基础信息ID
     * @return 影响的记录数
     */
    @Override
    public Integer responseNumIncrease(Long quotationBaseId) {
        return this.baseMapper.responseNumIncrease(quotationBaseId);
    }

    /**
     * 统计指定状态的单据数量
     *
     * @param statusCode 单据状态
     * @return 数量
     */
    private int countByStatus(String statusCode) {
        QuotationBase quotationBase = new QuotationBase();
        quotationBase.setQuotationStatus(statusCode);

        //权限优化，查询出自己权限的所以这里也要统计自己可以查询的
        Long userId = AuthUtil.getUser().getUserId();
        Boolean flag = roleForQuotationPermissionInvoking(userId);
        if(flag){
            //本人权限只能查询本人
            quotationBase.setCreatedBy(userId);
        }

        QueryWrapper<QuotationBase> qWrapper = Condition.getQueryWrapper(quotationBase);
        LambdaQueryWrapper<QuotationBase> queryWrapper = qWrapper.lambda();
        return count(queryWrapper);
    }

    @Override
    public List<QuotationLineExtVo> computeLineExtPrice(Long quotationId, List<QuotationLineExt> quotationLineExtList) {
        List<QuotationLineExtVo> resultQuotationLineExtVos = new ArrayList<>();
        //查询出来需要计算的字段
        List<QuotationLineExt> quotationLineExpressionExts = quotationLineExtService.list(new LambdaQueryWrapper<QuotationLineExt>().eq(QuotationLineExt::getQuotationBaseId, quotationId).eq(QuotationLineExt::getDataType, "expression"));
        quotationLineExpressionExts.forEach(quotationLineExt -> {
            QuotationLineExtVo quotationLineExtVo = new QuotationLineExtVo();
            evalexUtil.calculateInfo(quotationLineExt, quotationLineExtList);
            BeanUtil.copyProperties(quotationLineExt, quotationLineExtVo);
            resultQuotationLineExtVos.add(quotationLineExtVo);
        });
        return resultQuotationLineExtVos;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R<String> confirmMarked(Long quotationId) {
        BaseUserEntity baseUserEntity = AuthUtil.getUser();
        Supplier currentSupplier = baseUserEntity.getCurrentSupplier();
        if (currentSupplier == null) {
            throw new ApiException(500, "供应商信息异常");
        }
        Long supplierId = currentSupplier.getSupplierId();
        QuotationBase quotationBase = getById(quotationId);
        QueryWrapper<QuotationMark> quotationMarkQueryWrapper = Condition.getQueryWrapper(new QuotationMark());
        quotationMarkQueryWrapper.lambda().eq(QuotationMark::getQuotationId, quotationId)
                .eq(QuotationMark::getSupplierId, supplierId)
                .eq(QuotationMark::getRoundNum, quotationBase.getCurrentRoundNumber());
        QuotationMark quotationMark = quotationMarkService.getOne(quotationMarkQueryWrapper);
        if (quotationMark == null) {
            quotationMark = new QuotationMark()
                    .setMarkStatus(QuotationMark.MARK_STATUS.MARKED)
                    .setQuotationId(quotationId)
                    .setSupplierId(supplierId)
                    .setRoundNum(quotationBase.getCurrentRoundNumber());
            quotationMarkService.save(quotationMark);
            this.responseNumIncrease(quotationId);
            //修改供应商响应状态为已确认并且增加响应时间
            quotationSupplierService.updateResponseStatus(quotationId, supplierId, QuotationSupplierResponseStatus.confirmed.name());
            //给采购方发送供应商应标的站内消息
            SecurityUtils.getLoginInfo().ifPresent(loginInfo -> {
                QuotationInfoEventData eventData = BeanUtil.copy(quotationBase, QuotationInfoEventData.class);
                OrgIdQuery orgIdQuery = new OrgIdQuery()
                        .setSupplierCode(loginInfo.getCurrentSupplier().getSupplierCode())
                        .setPurchaseCode(quotationBase.getCustomerCode());
                R<OrgIdDTO> orgIdDTO = purchaserFeignClient.info(orgIdQuery);
                if (!(orgIdDTO.isSuccess() && orgIdDTO.getData() != null)) {
                    log.info("根据供应商编码：{}，和采购方编码：{}，获取orgId失败！", JSON.toJSONString(orgIdQuery.getSupplierCode()), JSON.toJSONString(orgIdQuery.getPurchaseCode()));
                    throw new ApiException(ResultCode.FAILURE.getCode(), "根据供应商编码和采购方编码，获取orgId失败！");
                }
                // 业务id和组织id
                eventData.setBusinessId(quotationId)
                        .setOrgId(orgIdDTO.getData().getOrgId())
                        .setSupplierCode(orgIdDTO.getData().getSupplierCode())
                        .setSupplierName(orgIdDTO.getData().getSupplierame());
                QuotationSupplierResponseNoticeEvent quotationSupplierResponseNoticeEvent = new QuotationSupplierResponseNoticeEvent(this, loginInfo, eventData, loginInfo.getCurrentSupplier().getSupplierCode(), loginInfo.getCurrentSupplier().getSupplierName());
                defaultEventPublisher.publishEvent(quotationSupplierResponseNoticeEvent);

                //操作日志
                QuotationOperationLog operationLog = new QuotationOperationLog()
                        .setBillId(quotationId)
                        .setBillType(loginInfo.getCurrentSupplier().getSupplierName())
                        .setContent("报价单应标确认")
                        .setOperationNo(baseUserEntity.getUserId())
                        .setOperatorName(baseUserEntity.getUserName());
                operationLogService.save(operationLog);
            });

            return R.success();
        } else {
            if (quotationMark.getMarkStatus().equals(QuotationMark.MARK_STATUS.MARKED)) {
                throw new ApiException(500, "此单已经应标了");
            } else if (quotationMark.getMarkStatus().equals(QuotationMark.MARK_STATUS.REJECT)) {
                throw new ApiException(500, "此单已经拒绝 不能继续参与应标");
            }
            //如果都没匹配到 直接抛异常
            throw new ApiException(500, "未匹配到Mark状态");
        }
    }

    //添加权限按钮
    public QuotationBaseVo addPermissionButton(QuotationBaseVo baseVo) {

        QuotationStatusEnum statusEnum = QuotationStatusEnum.getTypeByCode(baseVo.getQuotationStatus());
        Date cutOffTime = baseVo.getCutOffTime();
        String viewPrice = baseVo.getViewPrice();

        //两个日期相差的时长 = 结束日期 - 开始日期
        long between = DateUtil.between(new Date(), cutOffTime, DateUnit.SECOND, false);
        //显示比价按钮
        Boolean comparisonDisplay = false;
        if (between <= 0 || viewPrice.equals(YesOrNoNumber.YES.getCode())) {
            comparisonDisplay = true;
        }
        QuotationStatusEnum quotationStatusEnum = statusEnum == null ? QuotationStatusEnum.DRAFT : statusEnum;
        switch (quotationStatusEnum) {
            case DRAFT:
                baseVo.setPermissionButton(QuotationStatementStatusOperateRel.operations(false, QuotationStatementStatusOperateRel.TO_DRAFT));
                break;
            case QUOTATION:
                baseVo.setPermissionButton(QuotationStatementStatusOperateRel.operations(comparisonDisplay, QuotationStatementStatusOperateRel.TO_QUOTATION));
                //判断是否添加开标按钮
                addBidOpeningOfQuoting(baseVo);
                break;
            case BID_OPENING:
                baseVo.setPermissionButton(QuotationStatementStatusOperateRel.operations(comparisonDisplay, QuotationStatementStatusOperateRel.TO_BID_OPENING));
                break;
            case COMPARISON:
                baseVo.setPermissionButton(QuotationStatementStatusOperateRel.operations(comparisonDisplay, QuotationStatementStatusOperateRel.TO_BE_ANSWERED));
                break;
            case FRESH:
                baseVo.setPermissionButton(QuotationStatementStatusOperateRel.operations(comparisonDisplay, QuotationStatementStatusOperateRel.TO_BE_ANSWERED3));
                break;
            case OBSOLETE:
            case HAVE_PRICING:
            case FAILURE_BID:
                baseVo.setPermissionButton(QuotationStatementStatusOperateRel.operations(comparisonDisplay, QuotationStatementStatusOperateRel.TO_BE_ANSWERED2));
                break;
            default:
                break;
        }
        return baseVo;
    }

    /**
     * 提前结束报价，添加开标按钮
     *
     * @param baseVo 询价单
     */
    private void addBidOpeningOfQuoting(QuotationBaseVo baseVo) {
        if (baseVo == null || StringUtils.isBlank(baseVo.getClosingQuotationEarly())) {
            return;
        }
        String closingQuotationEarly = baseVo.getClosingQuotationEarly();
        if (YesOrNoNumber.YES.getCode().equals(closingQuotationEarly)) {
            Map<String, Boolean> permissionButton = baseVo.getPermissionButton();
            permissionButton.put(QuotationOperateType.QUOTATION_BID_OPENING.getCode(), true);
        }
    }

    /**
     * 定时任务检查询价单报价是否到达截止时间并修改状态
     */
    @Override
    public void modifyQuotationStatus() {
        Date now = new Date();
        LambdaUpdateWrapper<QuotationBase> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .set(QuotationBase::getQuotationStatus, QuotationStatusEnum.BID_OPENING.getCode())
                .eq(QuotationBase::getQuotationStatus, QuotationStatusEnum.QUOTATION)
                .le(QuotationBase::getCutOffTime, now);

        this.update(updateWrapper);

    }

    @Override
    public List<QuotationBaseSaleData> convertSaleDictCode(List<QuotationBase> list) {
        if (CollectionUtil.isNotEmpty(list)) {
            return list.stream().map(item -> {
                QuotationBaseSaleData quotationBaseData = new QuotationBaseSaleData();
                BeanUtil.copyProperties(item, quotationBaseData);
                quotationBaseData.setQuotationStatusName(dictItemService.getDictItemLabel(DictCodeEnum.quotation_status.name(), quotationBaseData.getQuotationStatus()));
                quotationBaseData.setQuotationTypeName(dictItemService.getDictItemLabel(DictCodeEnum.quotation_type.name(), quotationBaseData.getQuotationType()));
                return quotationBaseData;
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 修改询价单预警设置
     */
    @Override
    public Boolean earlyWranningSwitch(Long quotationBaseId) {
        //先查询
        QuotationBase base = this.getById(quotationBaseId);
        String supplierWarningSwitch = base.getSupplierWarningSwitch();
        if (supplierWarningSwitch == null) {
            throw new RuntimeException("没有设置预警开关");
        }

        if (supplierWarningSwitch.equals(EarlyWranningEnum.OPEN.getCode())) {
            base.setSupplierWarningSwitch(EarlyWranningEnum.CLOSE.getCode());
        } else {
            base.setSupplierWarningSwitch("OPEN");
        }
        Boolean flag = this.updateById(base);
        return flag;
    }

    /**
     * 获取预警信息接口
     */
    @Override
    public EarlyWranningVo selectEarlyWranning(Long quotationBaseId) {
        EarlyWranningVo earlyWranningVo = new EarlyWranningVo();
        QuotationBase base = this.getById(quotationBaseId);

        earlyWranningVo.setCutOffTime(base.getCutOffTime());
        if (base.getSupplierWarningSwitch() != null) {
            earlyWranningVo.setSupplierWarningSwitch(base.getSupplierWarningSwitch());
        }

        //获取协同配置的设置时间和发送类型
        Boolean flag = null;//开关是否开启
        Long nearTime = null;//小时数
        Integer type = null;//1 短信  2 站内信

        R<List<CfgParamDTO>> listByCode = cfgParamResourceFeignClient.findListByCode(QuotationCfgParam.QUOTATION_SUPPLIER_EARLY_WARNING.getCode());
        if (!(listByCode.isSuccess())) {
            log.error("获取同步配置出现异常,{}", JSON.toJSONString(listByCode));
            throw new RuntimeException("获取同步配置出现异常");
        }

        List<Integer> integerList = new ArrayList<>();
        List<CfgParamDTO> cfgParamDTOList = listByCode.getData();
        CfgParamDTO paramEntity = cfgParamDTOList.get(0);
        if (paramEntity != null) {
            JSONObject jsonObject = JSON.parseObject(paramEntity.getValue());  // result数据源：JSON格式字符串

            // 获取值
            nearTime = jsonObject.getLong("hour");
            flag = jsonObject.getBoolean("quotationSwitch");
            JSONArray data = jsonObject.getJSONArray("type");
            int size = data.size();


            for (int i = 0; i < size; i++) {
                String s = (String) data.get(i);
                integerList.add(Integer.valueOf(s));

            }
        }

        if (nearTime != null) {
            earlyWranningVo.setNearTime(nearTime);
        }

        List<String> stringList = new ArrayList<>();
        if (!integerList.isEmpty()) {
            earlyWranningVo.setType(integerList);

            for (Integer integer : integerList) {
                if (integer == 1) {
                    stringList.add("短信");
                } else if (integer == 2) {
                    stringList.add("站内信");
                }
            }
        }
        earlyWranningVo.setTypeString(stringList);
        return earlyWranningVo;
    }

    /**
     * 校验询价单是否存在
     *
     * @param quotationBaseId 询价单基础信息ID
     */
    @Override
    public void checkQuotationExist(Long quotationBaseId) {
        QuotationBase quotationBase = getById(quotationBaseId);
        if (quotationBase == null) {
            throw new BizException(QuotationResultCode.SOURCE_NOT_FOUND);
        }
    }


    /**
     * 校验当前采购登录用户的查询询价单的权限
     * @param pageParam 查询参数
     *
     */
    private void roleForQuotationPermission(QueryParam<QuotationBaseParam> pageParam) {
        Long userId = AuthUtil.getUser().getUserId();
        Boolean flag = roleForQuotationPermissionInvoking(userId);
        if(flag){
            //本人权限只能查询本人
            pageParam.getParam().setCreatedBy(userId);
        }

    }

    /**
     * 调用获取权限方法（统计数量的接口会用到，根据权限来查询）
     * @param userId 当前登录人的id
     *
     */
    private Boolean roleForQuotationPermissionInvoking(Long userId) {
        Boolean flag=false;
        String scope=null;
        R<String> roleForQuotation = tenantRoleForQuotationFeign.getRoleForQuotation(userId);
        if(roleForQuotation.isSuccess()){
            scope = roleForQuotation.getData();
        }else{
            log.info("获取当前采购商用户询价单权限出现异常，{}",JSON.toJSONString(roleForQuotation));
        }

        if(StrUtil.equals(scope, ScopeEnum.SELF.getCode())){
            flag=true;
        }
        return flag;
    }




}
