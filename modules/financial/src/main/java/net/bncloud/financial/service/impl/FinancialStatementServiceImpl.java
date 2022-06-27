package net.bncloud.financial.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.bizutil.number.NumberFactory;
import net.bncloud.common.bizutil.number.NumberType;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.service.base.domain.HandlerMsgParam;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CalculateUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.enums.EventCode;
import net.bncloud.event.publisher.DefaultEventPublisher;
import net.bncloud.financial.annotation.OperationLog;
import net.bncloud.financial.constant.FinancialConstant;
import net.bncloud.financial.entity.*;
import net.bncloud.financial.enums.*;
import net.bncloud.financial.event.*;
import net.bncloud.financial.event.statement.StatementComfirmEvent;
import net.bncloud.financial.event.statement.StatementDetailChangeEvent;
import net.bncloud.financial.feign.InformationServiceFeignClient;
import net.bncloud.financial.mapper.FinancialStatementMapper;
import net.bncloud.financial.param.FinancialStatementParam;
import net.bncloud.financial.param.FinancialStatementSaveParam;
import net.bncloud.financial.service.*;
import net.bncloud.financial.vo.*;
import net.bncloud.financial.vo.event.FinancialStatementEvent;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.service.api.platform.supplier.dto.FinancialInfoOfSupplierDTO;
import net.bncloud.service.api.platform.supplier.dto.SupplierDTO;
import net.bncloud.service.api.platform.supplier.enums.SupplierRelevanceStatusEnum;
import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;
import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 对账单信息表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Service
@Slf4j
public class FinancialStatementServiceImpl extends BaseServiceImpl<FinancialStatementMapper, FinancialStatement> implements FinancialStatementService {

    @Autowired
    @Lazy
    private  FinancialDeliveryDetailService financialDeliveryDetailService;

    private final FinancialDeliveryBillService financialDeliveryBillService;

    private final FinancialDeliveryBillLineService financialDeliveryBillLineService;

    private final FinancialCostBillService financialCostBillService;

    private final FinancialCostBillLineService financialCostBillLineService;

    @Autowired
    @Lazy
    private FinancialCostDetailService financialCostDetailService;

    private final FinancialSettlementPoolService financialSettlementPoolService;

    private final FinancialStatementPoolRelService financialStatementPoolRelService;

    private final DefaultEventPublisher defaultEventPublisher;

    @Resource
    private NumberFactory numberFactory;
    @Resource
    private CfgParamResourceFeignClient cfgParamResourceFeignClient;

    @Resource
    private SupplierFeignClient supplierFeignClient;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private PurchaserFeignClient purchaserFeignClient;

    @Resource
    private InformationServiceFeignClient informationServiceFeignClient;
    @Autowired
    private FinancialOperationLogService operationLogService;

    public FinancialStatementServiceImpl(FinancialDeliveryDetailService financialDeliveryDetailService, FinancialDeliveryBillService financialDeliveryBillService, FinancialDeliveryBillLineService financialDeliveryBillLineService, FinancialCostBillService financialCostBillService, FinancialCostBillLineService financialCostBillLineService, FinancialSettlementPoolService financialSettlementPoolService,
                                         FinancialStatementPoolRelService financialStatementPoolRelService, DefaultEventPublisher defaultEventPublisher) {
        this.financialDeliveryDetailService = financialDeliveryDetailService;
        this.financialDeliveryBillService = financialDeliveryBillService;
        this.financialDeliveryBillLineService = financialDeliveryBillLineService;
        this.financialCostBillService = financialCostBillService;
        this.financialCostBillLineService = financialCostBillLineService;
        this.financialSettlementPoolService = financialSettlementPoolService;
        this.financialStatementPoolRelService = financialStatementPoolRelService;
        this.defaultEventPublisher = defaultEventPublisher;
    }


    /**
     * 修改对账单
     *
     * @param financialStatement
     */
    public void updateStatementById(FinancialStatement financialStatement) {
        checkSupplierStatus(Collections.singletonList(financialStatement.getSupplierCode()));
        updateById(financialStatement);
    }

    /**
     * 删除对账费用明细时，扣减对账单中的 费用含税金额、对账含税金额、未税金额和税额
     *
     * @param costDetailId 对账费用明细id
     */
    public void deleteCostDetailById(Long costDetailId) {

        FinancialCostDetail costDetail = financialCostDetailService.getById(costDetailId);
        if (costDetail == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }

        FinancialStatement statement = getById(costDetail.getStatementId());
        //费用含税金额
        statement.setCostIncludingTax(statement.getCostIncludingTax().subtract(costDetail.getCheckIncludeTax()));
        //费用汇总未含税金额
        statement.setCostNotIncludingTax(statement.getCostNotIncludingTax().subtract(costDetail.getCheckNotTaxAmount()));
        //费用汇总税额
        statement.setCostTaxAmount(statement.getCostTaxAmount().subtract(costDetail.getCheckTaxAmount()));

        //对账含税金额
        statement.setStatementIncludingTax(statement.getStatementIncludingTax().subtract(costDetail.getCheckIncludeTax()));
        //对账未税金额
        statement.setStatementNotTax(statement.getStatementNotTax().subtract(costDetail.getCheckNotTaxAmount()));
        //对账税额
        statement.setStatementTaxAmount(statement.getStatementTaxAmount().subtract(costDetail.getCheckTaxAmount()));
        updateById(statement);

        financialCostDetailService.removeById(costDetailId);
        financialSettlementPoolService.updateStatementCreatedByBillIdList(Collections.singletonList(costDetail.getCostBillId()), "N");
    }


    /**
     * 删除对账送货明细时，扣减对账单中的 送货含税金额、对账含税金额、未税金额和税额
     *
     * @param deliveryDetailId 对账送货明细id
     */
    public void deleteDeliveryDetailById(Long deliveryDetailId) {

        FinancialDeliveryDetail deliveryDetail = financialDeliveryDetailService.getById(deliveryDetailId);
        if (deliveryDetail == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }

        FinancialStatement statement = getById(deliveryDetail.getStatementId());
        //送货含税金额
        statement.setShipmentIncludingTax(statement.getShipmentIncludingTax().subtract(deliveryDetail.getCheckIncludeTax()));
        //送货汇总未含税金额
        statement.setShipmentNotTax(statement.getShipmentNotTax().subtract(deliveryDetail.getCheckNotTaxAmount()));
        //送货汇总税额
        statement.setShipmentTaxAmount(statement.getShipmentTaxAmount().subtract(deliveryDetail.getCheckTaxAmount()));

        //对账含税金额
        statement.setStatementIncludingTax(statement.getStatementIncludingTax().subtract(deliveryDetail.getCheckIncludeTax()));
        //对账未税金额
        statement.setStatementNotTax(statement.getStatementNotTax().subtract(deliveryDetail.getCheckNotTaxAmount()));
        //对账税额
        statement.setStatementTaxAmount(statement.getStatementTaxAmount().subtract(deliveryDetail.getCheckTaxAmount()));

        updateById(statement);

        financialDeliveryDetailService.removeById(deliveryDetailId);
        financialSettlementPoolService.updateStatementCreatedByBillIdList(Collections.singletonList(deliveryDetail.getDeliveryBillId()), "N");
    }

    /**
     * 对账单状态数量统计
     *
     * @return
     */
    @Override
    public FinancialStatementStaticsVo getStatisticsInfo(String workBench) {
        //待发送的对账单的数量
        ArrayList<String> statementStatusList = new ArrayList<>();
        statementStatusList.add(StatementStatus.DRAFT.getCode());
        statementStatusList.add(StatementStatus.RETURNED.getCode());
        statementStatusList.add(StatementStatus.WITHDRAWN.getCode());
        LambdaQueryWrapper<FinancialStatement> queryWrapper = new LambdaQueryWrapper<FinancialStatement>().in(FinancialStatement::getStatementStatus, statementStatusList);
        int toBeSendNum = count(queryWrapper);

        //已确认的对账单的数量
        FinancialStatement statement = new FinancialStatement();
        // 指定当前供应商
        if( WorkBench.SUPPLIER.getCode().equals(workBench) ){
            SecurityUtils.getCurrentSupplier().ifPresent( supplier -> statement.setSupplierCode( supplier.getSupplierCode() ));
        }

        statement.setStatementStatus(StatementStatus.CONFIRMED.getCode());
        int confirmedNum = count(Condition.getQueryWrapper(statement));

        //待确认的对账单的数量
        int toBeConfirmedNum;
        if (WorkBench.SUPPLIER.getCode().equals(workBench)) {
            // 销售工作台
            statement.setStatementStatus(StatementStatus.TO_BE_SUPPLIER_CONFIRM.getCode());
        } else { // 采购工作台
            statement.setStatementStatus(StatementStatus.TO_BE_CUSTOMER_CONFIRM.getCode());
        }
        toBeConfirmedNum = count(Condition.getQueryWrapper(statement));

        return new FinancialStatementStaticsVo().setToBeSentNum(toBeSendNum).setToBeConfirmedNum(toBeConfirmedNum).setConfirmedNum(confirmedNum);
    }

    @Override
    public IPage<FinancialStatement> selectPage(IPage<FinancialStatement> page, QueryParam<FinancialStatementParam> queryParam) {
        return page.setRecords(baseMapper.selectListPage(page, queryParam));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public FinancialStatementVo saveStatement(FinancialStatementSaveParam accountStatementSaveParam) {
        accountStatementSaveParam.setId(null);
        accountStatementSaveParam.setStatementStatus(StatementStatus.DRAFT.getCode());
        //accountStatementSaveParam.setStatementNo(new NumberFactory(NumberType.statement).buildNumber());
        accountStatementSaveParam.setStatementNo(numberFactory.buildNumber(NumberType.statement));
        //设置供应商的税号、银行卡号、银行名称
        R<FinancialInfoOfSupplierDTO> result = supplierFeignClient.queryFinancialInfoOfSupplier(new SupplierDTO().setCode(accountStatementSaveParam.getSupplierCode()));
        if (result.isSuccess()) {
            FinancialInfoOfSupplierDTO resultData = result.getData();
            if (resultData != null) {
                accountStatementSaveParam.setTaxpayerId(resultData.getTaxpayerNo());
            }
        } else {
            throw new ApiException(ResultCode.FAILURE.getCode(), "服务调用失败");
        }

        save(accountStatementSaveParam);
        FinancialStatementVo accountStatementVo = new FinancialStatementVo();
        BeanUtil.copy(accountStatementSaveParam, accountStatementVo);

        return accountStatementVo;
    }

    /**
     * 生成对账单明细
     *
     * @param statementId 对账单ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void statementDetailGenerate(Long statementId) {
        FinancialStatement financialStatement = getById(statementId);
        if (financialStatement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        //客户编码-供应商-对账周期为key
        String customerCode = financialStatement.getCustomerCode();
        String supplierCode = financialStatement.getSupplierCode();
//        String periodStart = DateUtil.format(financialStatement.getPeriodStart(), DateUtil.PATTERN_DATE);
//        String periodEnd = DateUtil.format(financialStatement.getPeriodEnd(), DateUtil.PATTERN_DATE);
//        String key = customerCode+"|"+supplierCode+"|"+periodStart+"|"+periodEnd;
        String key = customerCode + "|" + supplierCode;

        //1、对账周期内的出货明细（结算池内的收发货单据ID，调用收发货服务，获取列表）

        //2、对账周期内的费用明细
        RLock rLock = redissonClient.getLock(key);
        try {
            boolean isLock = rLock.tryLock();
            //成功获取到锁
            if (isLock) {
                //doBusiness
                financialDeliveryDetailService.generateDeliveryDetail(financialStatement);

                financialCostDetailService.generateCostDetail(financialStatement);
            }
        } catch (Exception e) {
            log.error("生成对账单明细异常", e);
        } finally {
            rLock.unlock();
        }

    }

    /**
     * 校验对账周期
     * @return true 不在对账周期内 / false
     */
    private boolean checkReconciliationTime() {
        Integer startDate = 0;
        Integer endDate = 0;
        int today = 0;
        R<CfgParamDTO> cfg = cfgParamResourceFeignClient.getParamByCode(FinancialConstant.RECONCILIATION_TIME);
        if (cfg.isSuccess() && Objects.nonNull(cfg.getData())) {
            ReconciliationTimeConfig reconciliationTimeConfig = JSONObject.parseObject(cfg.getData().getValue(), ReconciliationTimeConfig.class);

            if (!reconciliationTimeConfig.isFinancialSwitch()) {
                return true;
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            ReconciliationTimeEnum obj = ReconciliationTimeEnum.getEnum(reconciliationTimeConfig.getItem());
            Asserts.notNull( obj,"获取枚举配置信息异常！" );
            switch (obj) {
                case WEEKLY:
                    startDate = Objects.requireNonNull(WeekEnum.getWeekEnum(reconciliationTimeConfig.getStartDate())).getValue();
                    endDate = Objects.requireNonNull(WeekEnum.getWeekEnum(reconciliationTimeConfig.getEndDate())).getValue();

                    today = cal.get(Calendar.DAY_OF_WEEK)-1;
                    break;
                case MONTHLY:
                    startDate = StringUtil.isNotBlank(reconciliationTimeConfig.getStartDate()) ? Integer.parseInt(reconciliationTimeConfig.getStartDate()) : 0;
                    endDate = StringUtil.isNotBlank(reconciliationTimeConfig.getEndDate()) ? Integer.parseInt(reconciliationTimeConfig.getEndDate()) : 0;
                    today = cal.get(Calendar.DAY_OF_MONTH);
                    break;
                default:
                    break;
            }
        }

        if( endDate < startDate){
            return ! ( today >= startDate || today <= endDate );
        }

        return today < startDate || today > endDate;
    }

    @Override
    public void checkSupplierStatus(List<String> supplierCodeList) {
        List<SupplierDTO> OaSupplierDTOList = new ArrayList<>();
        supplierCodeList.forEach(item -> {
            SupplierDTO oaSupplierDTO = new SupplierDTO();
            oaSupplierDTO.setCode(item);
            OaSupplierDTOList.add(oaSupplierDTO);
        });

        //获取供应商的合作状态
        R<List<SupplierDTO>> supplierByCode = supplierFeignClient.findSupplierByCode(OaSupplierDTOList);
        List<SupplierDTO> supplierList = new ArrayList<>();
        if (supplierByCode.isSuccess()) {
            supplierList = supplierByCode.getData();
            if (supplierList.isEmpty()) {
                throw new ApiException(500, "供应商不存在");
            }
        }
        for (SupplierDTO supplier : supplierList) {
            if (SupplierRelevanceStatusEnum.FROZEN.getCode().equals(supplier.getRelevanceStatus())) {
                throw new BizException(FinancialResultCode.SETTLEMENT_SUPPLIER_FROZEN);
            }
            if (SupplierRelevanceStatusEnum.SUSPEND_COOPERATION.getCode().equals(supplier.getRelevanceStatus())) {
                throw new BizException(FinancialResultCode.SETTLEMENT_SUPPLIER_SUSPEND_COOPERATION);
            }
        }

    }

    @Override
    public void checkPurchaserStatus(List<String> purchaserCodeList) {
        R<List<String>> r = purchaserFeignClient.queryAllPurchaser();
        List<String> data = r.getData();
        if (CollectionUtils.isNotEmpty(data)) {
            for (String code : purchaserCodeList) {
                if (!data.contains(code)) {
                    throw new BizException(FinancialResultCode.SETTLEMENT_PURCHASER_CLOSED);
                }
            }
        } else {
            throw new BizException(FinancialResultCode.SETTLEMENT_PURCHASER_CLOSED);
        }
    }

    @Override
    public List<FinancialStatementVo> batchStatementGenerateAuto(List<String> ids, String workBench) {
        BaseUserEntity user = AuthUtil.getUser();
        Optional<Org> currentOrgOptional = SecurityUtils.getCurrentOrg();
        String createdName = "";
        if (user != null) {
            createdName = user.getUserName();
        }
        List<FinancialStatementVo> resultList = new ArrayList<>();
        if (CollectionUtils.isEmpty(ids)) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        Collection<FinancialSettlementPool> pools = financialSettlementPoolService.listByIds(ids);

        if (CollectionUtils.isEmpty(pools)) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        List<String> purchaserCodeList = pools.stream().map(FinancialSettlementPool::getCustomerCode).collect(Collectors.toList());
        checkPurchaserStatus(purchaserCodeList);

        List<String> supplierCodeList = pools.stream().map(FinancialSettlementPool::getSupplierCode).collect(Collectors.toList());
        checkSupplierStatus(supplierCodeList);

        boolean statementCreated = pools.stream().anyMatch(item -> "Y".equals(item.getStatementCreated()));
        if (statementCreated) {
            throw new BizException(FinancialResultCode.SETTLEMENT_RECONCILE_REPEAT);
        }

        if(WorkBench.SUPPLIER.getCode().equals(workBench)){
            if( this.checkReconciliationTime()){
                throw new BizException(FinancialResultCode.SETTLEMENT_NOT_PERIOD);
            }
        }

        // 把在途结算池单据放进redis
        RSet<Long> rSet = redissonClient.getSet(FinancialConstant.FINANCIAL_ON_PASSAGE_POOL_DOCUMENT_KEY, JsonJacksonCodec.INSTANCE);
        boolean exists = pools.stream().anyMatch(item -> rSet.contains(item.getBillId()));
        if (exists) {
            throw new BizException(FinancialResultCode.SETTLEMENT_SELETED);
        }

        Set<Long> costBillIds = pools
                .stream()
                .filter(item -> item.getBillType().equals(FinancialBillTypeEnum.COST.name()))
                .map(FinancialSettlementPool::getBillId)
                .collect(Collectors.toSet());

        Set<Long> deliveryBillIds = pools
                .stream()
                .filter(item -> item.getBillType().equals(FinancialBillTypeEnum.DELIVERY.name()))
                .map(FinancialSettlementPool::getBillId)
                .collect(Collectors.toSet());

        //把将要对账的单据id集合放入redis在途对账单集合中
        rSet.addAll(costBillIds);
        rSet.addAll(deliveryBillIds);

        List<FinancialCostBillLine> costLineList = getCostIdListFinancialCostBillLine(costBillIds);
        List<FinancialDeliveryBillLine> deliveryLineList = getDeliveryIdListFinancialDeliveryBillLine(deliveryBillIds);

        LocalDate maxDate = pools.stream().map(FinancialSettlementPool::getConfirmTime).max(LocalDate::compareTo).get();
        LocalDate minDate = pools.stream().map(FinancialSettlementPool::getConfirmTime).min(LocalDate::compareTo).get();
        Set<Long> poolIds = new HashSet<>();
        for (FinancialSettlementPool pool : pools) {
            if (poolIds.contains(pool.getId())) {
                continue;
            }
            FinancialStatementSaveParam statementSaveParam = new FinancialStatementSaveParam();

            //获取ordId
            OrgIdQuery orgIdQuery = new OrgIdQuery();
            orgIdQuery.setSupplierCode(pool.getSupplierCode());
            orgIdQuery.setPurchaseCode(pool.getCustomerCode());
            R<OrgIdDTO> orgIdDTO = purchaserFeignClient.info(orgIdQuery);
            if (!orgIdDTO.isSuccess()) {
                log.error("获取当前组织ID出现异常, {}", JSON.toJSONString(orgIdDTO));
                throw new RuntimeException("批量对账获取当前组织ID出现异常");
            }
            statementSaveParam.setOrgId(orgIdDTO.getData().getOrgId());

            statementSaveParam.setPeriodEnd(maxDate);
            statementSaveParam.setPeriodStart(minDate);
            statementSaveParam.setCreatedName(createdName);
            statementSaveParam.setCustomerCode(pool.getCustomerCode());
            statementSaveParam.setCustomerName(pool.getCustomerName());
            statementSaveParam.setSupplierCode(pool.getSupplierCode());
            statementSaveParam.setSupplierName(pool.getSupplierName());
            statementSaveParam.setCurrencyCode(pool.getCurrencyCode());
            statementSaveParam.setCurrencyName(pool.getCurrencyName());
            statementSaveParam.setSourceType(workBench);

            BigDecimal shipmentIncludingTax = new BigDecimal(0);
            BigDecimal shipmentNotTax = new BigDecimal(0);
            BigDecimal shipmentTaxAmount = new BigDecimal(0);
            BigDecimal costIncludingTax = new BigDecimal(0);
            BigDecimal costNotIncludingTax = new BigDecimal(0);
            BigDecimal costTaxAmount = new BigDecimal(0);
            BigDecimal sumStatementIncludingTax = new BigDecimal(0);
            BigDecimal sumStatementNotTax = new BigDecimal(0);
            BigDecimal sumStatementTaxAmount = new BigDecimal(0);
            Set<Long> set = new HashSet<>();
            for (FinancialSettlementPool item : pools) {
                if (pool.getCustomerCode().equals(item.getCustomerCode())
                        && pool.getSupplierCode().equals(item.getSupplierCode())) {
                    switch (FinancialBillTypeEnum.valueOf(item.getBillType())) {
                        case DELIVERY:
                            for (FinancialDeliveryBillLine line : deliveryLineList) {
                                if (line.getDeliveryBillId().equals(item.getBillId().toString())) {
                                    shipmentIncludingTax = CalculateUtil.add(shipmentIncludingTax, line.getTaxIncludedAmount());
                                    shipmentNotTax = CalculateUtil.add(shipmentNotTax, line.getNotTaxAmount());
                                    shipmentTaxAmount = CalculateUtil.add(shipmentTaxAmount, line.getTaxAmount());
                                    sumStatementIncludingTax = CalculateUtil.add(sumStatementIncludingTax, line.getTaxIncludedAmount());
                                    sumStatementNotTax = CalculateUtil.add(sumStatementNotTax, line.getNotTaxAmount());
                                    sumStatementTaxAmount = CalculateUtil.add(sumStatementTaxAmount, line.getTaxAmount());
                                }
                            }
                            break;
                        case COST:
                            for (FinancialCostBillLine line : costLineList) {
                                if (line.getCostBillId().equals(item.getBillId().toString())) {
                                    costIncludingTax = CalculateUtil.add(costIncludingTax, line.getTaxIncludedAmount());
                                    costNotIncludingTax = CalculateUtil.add(costNotIncludingTax, line.getNotTaxAmount());
                                    costTaxAmount = CalculateUtil.add(costTaxAmount, line.getTaxAmount());
                                    sumStatementIncludingTax = CalculateUtil.add(sumStatementIncludingTax, line.getTaxIncludedAmount());
                                    sumStatementNotTax = CalculateUtil.add(sumStatementNotTax, line.getNotTaxAmount());
                                    sumStatementTaxAmount = CalculateUtil.add(sumStatementTaxAmount, line.getTaxAmount());
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    poolIds.add(item.getId());
                    set.add(item.getId());
                }
            }

            //送货汇总含税金额
            statementSaveParam.setShipmentIncludingTax(shipmentIncludingTax);
            //送货汇总不含税金额
            statementSaveParam.setShipmentNotTax(shipmentNotTax);
            //送货汇总税额
            statementSaveParam.setShipmentTaxAmount(shipmentTaxAmount);
            //费用汇总含税金额
            statementSaveParam.setCostIncludingTax(costIncludingTax);
            //费用汇总不含税金额
            statementSaveParam.setCostNotIncludingTax(costNotIncludingTax);
            //费用汇总税额
            statementSaveParam.setCostTaxAmount(costTaxAmount);
            //对账含税金额
            statementSaveParam.setStatementIncludingTax(sumStatementIncludingTax);
            //对账不含税金额
            statementSaveParam.setStatementNotTax(sumStatementNotTax);
            //对账税额
            statementSaveParam.setStatementTaxAmount(sumStatementTaxAmount);

            FinancialStatementVo financialStatementVo = saveStatement(statementSaveParam);
            financialStatementPoolRelService.batchSave(set, financialStatementVo.getId());
            statementDetailGenerate(financialStatementVo.getId());
            resultList.add(financialStatementVo);
        }
        //生成后把redis中的在途单据删除
        rSet.removeAll(costBillIds);
        rSet.removeAll(deliveryBillIds);
        return resultList;
    }

    private List<FinancialCostBillLine> getCostIdListFinancialCostBillLine(Set<Long> ids) {
        FinancialCostBillLine financialCostBillLine = new FinancialCostBillLine();
        LambdaQueryWrapper<FinancialCostBillLine> queryWrapper = Condition.getQueryWrapper(financialCostBillLine).lambda();
        queryWrapper.in(CollectionUtils.isNotEmpty(ids), FinancialCostBillLine::getCostBillId, ids);

        return financialCostBillLineService.list(queryWrapper);
    }

    private List<FinancialDeliveryBillLine> getDeliveryIdListFinancialDeliveryBillLine(Set<Long> ids) {
        FinancialDeliveryBillLine financialDeliveryBillLine = new FinancialDeliveryBillLine();
        LambdaQueryWrapper<FinancialDeliveryBillLine> queryWrapper2 = Condition.getQueryWrapper(financialDeliveryBillLine).lambda();
        queryWrapper2.in(CollectionUtils.isNotEmpty(ids), FinancialDeliveryBillLine::getDeliveryBillId, ids);

        return financialDeliveryBillLineService.list(queryWrapper2);
    }

    /**
     * 通过id删除对账单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        financialCostDetailService.deleteByStatementId(id);
        financialDeliveryDetailService.deleteByStatementId(id);
        financialStatementPoolRelService.deleteByStatementId(id);
        removeById(id);

    }

    @Override
    public FinancialStatementVo getStatementInfo(Long id) {
        FinancialStatement statement = getById(id);
        if (statement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        FinancialStatementVo statementVo = BeanUtil.copy(statement, FinancialStatementVo.class);

        //获取费用明细
        FinancialCostDetail financialCostDetail = new FinancialCostDetail();
        financialCostDetail.setStatementId(statement.getId());
        List<FinancialCostDetail> list = financialCostDetailService.list(Condition.getQueryWrapper(financialCostDetail));
        List<FinancialCostDetailVo> copyList = BeanUtil.copy(list, FinancialCostDetailVo.class);
        statementVo.setFinancialReceivableDiscountDetailList(copyList);

        //获取送货明细
        FinancialDeliveryDetail financialDeliveryDetail = new FinancialDeliveryDetail();
        financialDeliveryDetail.setStatementId(statement.getId());
        List<FinancialDeliveryDetail> list2 = financialDeliveryDetailService.list(Condition.getQueryWrapper(financialDeliveryDetail));
        List<FinancialDeliveryDetailVo> copyList2 = BeanUtil.copy(list2, FinancialDeliveryDetailVo.class);
        statementVo.setFinancialDeliveryDetailList(copyList2);

        //设置权限按钮
        buildPermissionButton(statementVo);
        return statementVo;
    }


    @Override
    @OperationLog(code = "statement_purchase:send", content = "发送对账单", billType = "0")
    public boolean purchaseSend(Long statementId) {
        FinancialStatement statement = getById(statementId);
        if (statement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        //对账费用明细
        List<FinancialCostDetail> costDetails = financialCostDetailService.list(Wrappers
                .<FinancialCostDetail>lambdaQuery()
                .eq(FinancialCostDetail::getStatementId, statementId));
        //对账送货明细
        List<FinancialDeliveryDetail> deliveryDetails = financialDeliveryDetailService.list(Wrappers
                .<FinancialDeliveryDetail>lambdaQuery()
                .eq(FinancialDeliveryDetail::getStatementId, statementId));
        if (CollectionUtil.isEmpty(costDetails) && CollectionUtil.isEmpty(deliveryDetails)) {
            throw new ApiException(ResultCode.FAILURE.getCode(), "送货明细和费用明细为空，无法发送");
        }


        //送货明细和费用明细都为空，不能发送
        checkSupplierStatus(Arrays.asList(statement.getSupplierCode()));
        FinancialStatementEvent statementEvent = BeanUtil.copy(statement, FinancialStatementEvent.class);
        statementEvent.setBusinessId(statement.getId());

        //修改对账单状态
        update(Wrappers.<FinancialStatement>update().lambda()
                .set(FinancialStatement::getStatementStatus, StatementStatus.TO_BE_SUPPLIER_CONFIRM.getCode())
                .set(FinancialStatement::getPublishTime, new Date())
                .eq(FinancialStatement::getId, statementId));

        //发送消息通知
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            defaultEventPublisher.publishEvent(new FinancialStatementPurchaseSendEvent(this, loginInfo, statementEvent, statement.getCustomerCode(), statement.getCustomerName()));
            handlerInformation(statement.getId(), EventCode.statement_supplier_reject.getCode(), WorkBench.ORG.getCode());
        }
        return true;
    }

    /**
     * 采购方撤回对账单
     *
     * @param statementId 对账单ID
     * @return
     */
    @Override
    @OperationLog(code = "statement_purchase:withdraw", content = "撤回对账单", billType = "0")
    public boolean purchaseWithdraw(Long statementId) {
        FinancialStatement statement = getById(statementId);
        if (statement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        if (!StatementStatus.TO_BE_SUPPLIER_CONFIRM.getCode().equals(statement.getStatementStatus())) {
            throw new BizException(FinancialResultCode.SETTLEMENT_NOT_TO_BE_CONFIRM);
        }
        checkSupplierStatus(Arrays.asList(statement.getSupplierCode()));
        FinancialStatementEvent statementEvent = BeanUtil.copy(statement, FinancialStatementEvent.class);
        statementEvent.setBusinessId(statement.getId());

        //修改对账单状态
        update(Wrappers.<FinancialStatement>update().lambda()
                .set(FinancialStatement::getStatementStatus, StatementStatus.WITHDRAWN.getCode())
                .eq(FinancialStatement::getId, statementId));

        //发送消息通知
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            defaultEventPublisher.publishEvent(new FinancialStatementPurchaseWithdrawEvent(this, loginInfo, statementEvent, statement.getCustomerCode(), statement.getCustomerName()));
            handlerInformation(statement.getId(), EventCode.statement_purchase_send.getCode(), WorkBench.SUPPLIER.getCode());
        }
        return true;
    }

    /**
     * 采购方作废对账单
     *
     * @param statementId 对账单ID
     * @return
     */
    @Override
    @OperationLog(code = "statement_purchase:invalid", content = "作废对账单", billType = "0")
    public boolean purchaseInvalid(Long statementId) {
        FinancialStatement statement = getById(statementId);
        if (statement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        checkSupplierStatus(Arrays.asList(statement.getSupplierCode()));

        invalidHandler(statementId);
        //发送消息通知
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            FinancialStatementEvent statementEvent = BeanUtil.copy(statement, FinancialStatementEvent.class);
            statementEvent.setBusinessId(statement.getId());
            defaultEventPublisher.publishEvent(new FinancialStatementPurchaseInvalidEvent(this, loginInfo, statementEvent, statement.getCustomerCode(), statement.getCustomerName()));
            handlerInformation(statement.getId(), EventCode.statement_supplier_reject.getCode(), WorkBench.ORG.getCode());
        }
        return true;
    }

    private void invalidHandler(Long statementId) {
        //修改对账单状态
        update(Wrappers.<FinancialStatement>update().lambda()
                .set(FinancialStatement::getStatementStatus, StatementStatus.INVALID.getCode())
                .eq(FinancialStatement::getId, statementId));
        List<FinancialStatementPoolRel> list = financialStatementPoolRelService.list(Wrappers.<FinancialStatementPoolRel>query()
                .lambda().eq(FinancialStatementPoolRel::getStatementId, statementId));
        List<Long> collect = list.stream().map(FinancialStatementPoolRel::getSettlementPoolId).collect(Collectors.toList());
        financialSettlementPoolService.updateStatementCreatedByIdList(collect, "N");
    }

    /**
     * 采购方确认对账单
     *
     * @param statementId 对账单
     * @return
     */
    @Override
    @OperationLog(code = "statement_purchase:confirm", content = "确认对账单", billType = "0")
    public boolean purchaseConfirm(Long statementId) {
        FinancialStatement statement = getById(statementId);
        if (statement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        if (!StatementStatus.TO_BE_CUSTOMER_CONFIRM.getCode().equals(statement.getStatementStatus())) {
            throw new BizException(FinancialResultCode.SETTLEMENT_NOT_TO_BE_CONFIRM);
        }
        checkSupplierStatus(Arrays.asList(statement.getSupplierCode()));
        FinancialStatementEvent statementEvent = BeanUtil.copy(statement, FinancialStatementEvent.class);
        statementEvent.setBusinessId(statement.getId());
        LoginInfo loginInfo = getLoginInfo();
        //修改对账单状态
        update(Wrappers.<FinancialStatement>update().lambda()
                .set(FinancialStatement::getStatementStatus, StatementStatus.CONFIRMED.getCode())
                .set(Objects.nonNull(loginInfo), FinancialStatement::getAuditName, loginInfo.getName())
                .set(FinancialStatement::getAuditTime, new Date())
                .eq(FinancialStatement::getId, statementId));

        applicationEventPublisher.publishEvent(new StatementComfirmEvent(this, statementId));
        applicationEventPublisher.publishEvent(new StatementDetailChangeEvent(this, statementId));

        //发送消息通知
        if (loginInfo != null) {
            defaultEventPublisher.publishEvent(new FinancialStatementPurchaseConfirmEvent(this, loginInfo, statementEvent, statement.getCustomerCode(), statement.getCustomerName()));
            handlerInformation(statement.getId(), EventCode.statement_supplier_send.getCode(), WorkBench.ORG.getCode());
        }
        return true;
    }

    /**
     * 采购方提醒
     *
     * @param statementId 对账单ID
     * @return
     */
    @Override
    public boolean purchaseRemind(Long statementId) {
        FinancialStatement statement = getById(statementId);
        if (statement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        checkSupplierStatus(Arrays.asList(statement.getSupplierCode()));
        FinancialStatementEvent statementEvent = BeanUtil.copy(statement, FinancialStatementEvent.class);
        statementEvent.setBusinessId(statement.getId());


        //发送消息通知
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            defaultEventPublisher.publishEvent(new FinancialStatementPurchaseRemindEvent(this, loginInfo, statementEvent, statement.getCustomerCode(), statement.getCustomerName()));
        }
        return true;
    }

    /**
     * 采购方退回
     *
     * @param statementId 对账单ID
     * @param statementPurchaseRejectVo
     * @return
     */
    @Override
    public boolean purchaseReject(Long statementId, StatementPurchaseRejectVo statementPurchaseRejectVo) {
        FinancialStatement statement = getById(statementId);
        if (statement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        if (!StatementStatus.TO_BE_CUSTOMER_CONFIRM.getCode().equals(statement.getStatementStatus())) {
            throw new BizException(FinancialResultCode.SETTLEMENT_NOT_TO_BE_CONFIRM);
        }
        checkSupplierStatus(Arrays.asList(statement.getSupplierCode()));
        FinancialStatementEvent statementEvent = BeanUtil.copy(statement, FinancialStatementEvent.class);
        statementEvent.setBusinessId(statement.getId());


        //修改对账单状态
        update(Wrappers.<FinancialStatement>update().lambda()
                .set(FinancialStatement::getStatementStatus, StatementStatus.RETURNED.getCode())
                .eq(FinancialStatement::getId, statementId));

        // 记录日志
        operationLogService.recordOperationLog( OperationLogType.STATEMENT_REJECT,statementId,"退回对账单",statementPurchaseRejectVo.getRemark(), SecurityUtils.getLoginInfoOrThrow() );

        //发送消息通知
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            defaultEventPublisher.publishEvent(new FinancialStatementPurchaseRejectEvent(this, loginInfo, statementEvent, statement.getCustomerCode(), statement.getCustomerName()));
            handlerInformation(statement.getId(), EventCode.statement_supplier_send.getCode(), WorkBench.ORG.getCode());
        }
        return true;
    }

    private void handlerInformation(Long id, String eventCode, String receiverType) {
        HandlerMsgParam handlerMsgParam = new HandlerMsgParam();
        handlerMsgParam.setEventCode(eventCode);
        handlerMsgParam.setBusinessId(id);
        handlerMsgParam.setReceiverType(receiverType);
        informationServiceFeignClient.handlerInformation(handlerMsgParam);
    }

    /**
     * 供应商发送：
     * 送货明细和费用明细为空，不能发送
     *
     * @param statementId
     * @return
     */
    @Override
    @OperationLog(code = "statement_supplier:send", content = "发送对账单", billType = "0")
    public boolean supplierSend(Long statementId) {

        FinancialStatement statement = getById(statementId);
        if (statement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        //对账送货明细
        List<FinancialDeliveryDetail> deliveryDetails = financialDeliveryDetailService.list(Wrappers
                .<FinancialDeliveryDetail>lambdaQuery()
                .eq(FinancialDeliveryDetail::getStatementId, statementId));
        //对账费用明细
        List<FinancialCostDetail> costDetails = financialCostDetailService.list(Wrappers.
                <FinancialCostDetail>lambdaQuery()
                .eq(FinancialCostDetail::getStatementId, statementId));
        if (CollectionUtil.isEmpty(deliveryDetails) && CollectionUtil.isEmpty(costDetails)) {
            throw new ApiException(ResultCode.FAILURE.getCode(), "对账费用明细和对账送货明细为空，无法发送");
        }

        checkSupplierStatus(Arrays.asList(statement.getSupplierCode()));
        FinancialStatementEvent statementEvent = BeanUtil.copy(statement, FinancialStatementEvent.class);
        statementEvent.setBusinessId(statement.getId());

        //修改对账单状态
        update(Wrappers.<FinancialStatement>update().lambda()
                .set(FinancialStatement::getStatementStatus, StatementStatus.TO_BE_CUSTOMER_CONFIRM.getCode())
                .eq(FinancialStatement::getId, statementId));


        //发送消息通知
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            defaultEventPublisher.publishEvent(new FinancialStatementSupplierSendEvent(this, loginInfo, statementEvent, statement.getSupplierCode(), statement.getSupplierName()));
            handlerInformation(statement.getId(), EventCode.statement_purchase_reject.getCode(), WorkBench.SUPPLIER.getCode());
        }
        return true;
    }

    /**
     * 供应商撤回
     *
     * @param statementId 对账单ID
     * @return
     */
    @Override
    @OperationLog(code = "statement_supplier:withdraw", content = "撤回对账单", billType = "0")
    public boolean supplierWithdraw(Long statementId) {
        FinancialStatement statement = getById(statementId);
        if (statement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }

        if (!StatementStatus.TO_BE_CUSTOMER_CONFIRM.getCode().equals(statement.getStatementStatus())) {
            throw new BizException(FinancialResultCode.SETTLEMENT_NOT_TO_BE_CONFIRM);
        }
        checkSupplierStatus(Arrays.asList(statement.getSupplierCode()));
        FinancialStatementEvent statementEvent = BeanUtil.copy(statement, FinancialStatementEvent.class);
        statementEvent.setBusinessId(statement.getId());

        //修改对账单状态
        update(Wrappers.<FinancialStatement>update().lambda()
                .set(FinancialStatement::getStatementStatus, StatementStatus.WITHDRAWN.getCode())
                .eq(FinancialStatement::getId, statementId));

        //发送消息通知
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            defaultEventPublisher.publishEvent(new FinancialStatementSupplierWithdrawEvent(this, loginInfo, statementEvent, statement.getSupplierCode(), statement.getSupplierName()));
            handlerInformation(statement.getId(), EventCode.statement_supplier_send.getCode(), WorkBench.ORG.getCode());
        }
        return true;
    }

    /**
     * 供应商作废
     *
     * @param statementId 对账单ID
     * @return
     */
    @Override
    @OperationLog(code = "statement_supplier:invalid", content = "作废对账单", billType = "0")
    public boolean supplierInvalid(Long statementId) {
        FinancialStatement statement = getById(statementId);
        if (statement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        checkSupplierStatus(Arrays.asList(statement.getSupplierCode()));
        FinancialStatementEvent statementEvent = BeanUtil.copy(statement, FinancialStatementEvent.class);
        statementEvent.setBusinessId(statement.getId());

        invalidHandler(statementId);
        //发送消息通知
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            defaultEventPublisher.publishEvent(new FinancialStatementSupplierInvalidEvent(this, loginInfo, statementEvent, statement.getSupplierCode(), statement.getSupplierName()));
            handlerInformation(statement.getId(), EventCode.statement_purchase_reject.getCode(), WorkBench.SUPPLIER.getCode());
        }
        return true;
    }

    /**
     * 供应商确认
     *
     * @param statementId 对账单ID
     * @return
     */
    @Override
    @OperationLog(code = "statement_supplier:confirm", content = "确认对账单", billType = "0")
    public boolean supplierConfirm(Long statementId) {
        FinancialStatement statement = getById(statementId);
        if (statement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        if (!StatementStatus.TO_BE_SUPPLIER_CONFIRM.getCode().equals(statement.getStatementStatus())) {
            throw new BizException(FinancialResultCode.SETTLEMENT_NOT_TO_BE_CONFIRM);
        }
        checkSupplierStatus(Arrays.asList(statement.getSupplierCode()));
        FinancialStatementEvent statementEvent = BeanUtil.copy(statement, FinancialStatementEvent.class);
        statementEvent.setBusinessId(statement.getId());
        LoginInfo loginInfo = getLoginInfo();

        //修改对账单状态
        update(Wrappers.<FinancialStatement>update().lambda()
                .set(FinancialStatement::getStatementStatus, StatementStatus.CONFIRMED.getCode())
                .set(Objects.nonNull(loginInfo), FinancialStatement::getAuditName, loginInfo.getName())
                .set(FinancialStatement::getAuditTime, new Date())
                .eq(FinancialStatement::getId, statementId));

        applicationEventPublisher.publishEvent(new StatementComfirmEvent(this, statementId));
        applicationEventPublisher.publishEvent(new StatementDetailChangeEvent(this, statementId));

        //发送消息通知
        if (loginInfo != null) {
            defaultEventPublisher.publishEvent(new FinancialStatementSupplierConfirmEvent(this, loginInfo, statementEvent, statement.getSupplierCode(), statement.getSupplierName()));
            handlerInformation(statement.getId(), EventCode.statement_purchase_send.getCode(), WorkBench.SUPPLIER.getCode());
        }

        return true;
    }

    /**
     * 供应商提醒
     *
     * @param statementId 对账单ID
     * @return
     */
    @Override
    @OperationLog(code = "statement_supplier:remind", content = "提醒对账单", billType = "0")
    public boolean supplierRemind(Long statementId) {
        FinancialStatement statement = getById(statementId);
        if (statement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        checkSupplierStatus(Arrays.asList(statement.getSupplierCode()));
        FinancialStatementEvent statementEvent = BeanUtil.copy(statement, FinancialStatementEvent.class);
        statementEvent.setBusinessId(statement.getId());


        //发送消息通知
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            defaultEventPublisher.publishEvent(new FinancialStatementSupplierRemindEvent(this, loginInfo, statementEvent, statement.getSupplierCode(), statement.getSupplierName()));
        }

        return true;
    }

    /**
     * 供应商退回
     *
     * @param statementId 对账单ID
     * @return
     */
    @Override
//    @OperationLog(code = "statement_supplier:reject", content = "退回对账单", billType = "0")
    public boolean supplierReject(Long statementId,StatementPurchaseRejectVo statementPurchaseRejectVo) {
        FinancialStatement statement = getById(statementId);
        if (statement == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        if (!StatementStatus.TO_BE_SUPPLIER_CONFIRM.getCode().equals(statement.getStatementStatus())) {
            throw new BizException(FinancialResultCode.SETTLEMENT_NOT_TO_BE_CONFIRM);
        }
        checkSupplierStatus(Arrays.asList(statement.getSupplierCode()));
        FinancialStatementEvent statementEvent = BeanUtil.copy(statement, FinancialStatementEvent.class);
        statementEvent.setBusinessId(statement.getId());


        //修改对账单状态
        update(Wrappers.<FinancialStatement>update().lambda()
                .set(FinancialStatement::getStatementStatus, StatementStatus.RETURNED.getCode())
                .eq(FinancialStatement::getId, statementId));
        //记录日志
        operationLogService.recordOperationLog( OperationLogType.STATEMENT_REJECT,statementId,"退回对账单",statementPurchaseRejectVo.getRemark(), SecurityUtils.getLoginInfoOrThrow() );
        //发送消息通知
        LoginInfo loginInfo = getLoginInfo();
        if (loginInfo != null) {
            defaultEventPublisher.publishEvent(new FinancialStatementSupplierRejectEvent(this, loginInfo, statementEvent, statement.getSupplierCode(), statement.getSupplierName()));
            handlerInformation(statement.getId(), EventCode.statement_purchase_send.getCode(), WorkBench.SUPPLIER.getCode());
        }
        return true;
    }

    /**
     * 设置权限按钮
     *
     * @param records 记录
     */
    @Override
    public void buildPermissionButtonBatch(List<FinancialStatementVo> records) {
        if (CollectionUtil.isNotEmpty(records)) {
            for (FinancialStatementVo statementVo : records) {
                buildPermissionButton(statementVo);
            }
        }
    }

    /**
     * 构建对账单
     *
     * @param settlementPool 结算池单据
     * @return 对账单
     */
    private FinancialStatement buildStatement(FinancialSettlementPool settlementPool) {
        if (settlementPool == null) {
            throw new BizException(FinancialResultCode.PARAM_ERROR);
        }
        FinancialStatement financialStatement = new FinancialStatement();
        Optional<Org> optionalOrg = SecurityUtils.getCurrentOrg();
        if (optionalOrg.isPresent()) {
            Org org = optionalOrg.get();
            financialStatement.setOrgId(org.getId());
            financialStatement.setOrgName(org.getName());
        }

        financialStatement.setCustomerCode(settlementPool.getCustomerCode());
        financialStatement.setCustomerName(settlementPool.getCustomerName());
        //financialStatement.setStatementNo(new NumberFactory(NumberType.statement).buildNumber());
        financialStatement.setStatementNo(numberFactory.buildNumber(NumberType.statement));
        financialStatement.setSupplierCode(settlementPool.getSupplierCode());
        financialStatement.setSupplierName(settlementPool.getSupplierName());
        financialStatement.setStatementStatus(StatementStatus.DRAFT.getCode());
        financialStatement.setSourceType(SourceType.PURCHASE.getCode());

        return financialStatement;
    }

    private LoginInfo getLoginInfo() {
        LoginInfo loginInfo = null;
        //获取当前登录信息
        Optional<LoginInfo> optional = SecurityUtils.getLoginInfo();
        if (optional.isPresent()) {
            loginInfo = optional.get();
        } else {
            log.warn("获取用户登录信息失败");
        }
        return loginInfo;
    }

    private void buildPermissionButton(FinancialStatementVo statementVo) {
        if (statementVo == null) {
            return;
        }
        try {
            checkSupplierStatus(Arrays.asList(statementVo.getSupplierCode()));
            checkPurchaserStatus(Arrays.asList(statementVo.getCustomerCode()));
        } catch (Exception e) {
            return;
        }
        StatementStatus statementStatus = StatementStatus.getTypeByCode(statementVo.getStatementStatus());
        SourceType sourceType = SourceType.getTypeByCode(statementVo.getSourceType());
        if (statementStatus == null || sourceType == null) {
            return;
        }
        if (sourceType.getCode().equals(SourceType.PURCHASE.getCode())) {
            buildButtonIfSourceTypeIsPurchase(statementVo);
        } else if (sourceType.getCode().equals(SourceType.SUPPLIER.getCode())) {
            buildButtonIfSourceTypeIsSupplier(statementVo);
        }
    }

    private void buildButtonIfSourceTypeIsSupplier(FinancialStatementVo statementVo) {
        if (statementVo == null) {
            return;
        }
        StatementStatus statementStatus = StatementStatus.getTypeByCode(statementVo.getStatementStatus());
        SourceType sourceType = SourceType.getTypeByCode(statementVo.getSourceType());
        if (statementStatus == null || sourceType == null) {
            return;
        }

        switch (statementStatus) {
            case DRAFT:
                statementVo.setPurchasePermissionButton(SupplierStatementStatusOperateRel.getCustomerOperations(SupplierStatementStatusOperateRel.DRAFT));
                statementVo.setSupplierPermissionButton(SupplierStatementStatusOperateRel.getSupplierOperations(SupplierStatementStatusOperateRel.DRAFT));
                break;
            case TO_BE_CUSTOMER_CONFIRM:
                statementVo.setPurchasePermissionButton(SupplierStatementStatusOperateRel.getCustomerOperations(SupplierStatementStatusOperateRel.TO_BE_CUSTOMER_CONFIRM));
                statementVo.setSupplierPermissionButton(SupplierStatementStatusOperateRel.getSupplierOperations(SupplierStatementStatusOperateRel.TO_BE_CUSTOMER_CONFIRM));
                break;
            case WITHDRAWN:
                statementVo.setPurchasePermissionButton(SupplierStatementStatusOperateRel.getCustomerOperations(SupplierStatementStatusOperateRel.WITHDRAWN));
                statementVo.setSupplierPermissionButton(SupplierStatementStatusOperateRel.getSupplierOperations(SupplierStatementStatusOperateRel.WITHDRAWN));
                break;
            case INVALID:
                statementVo.setPurchasePermissionButton(SupplierStatementStatusOperateRel.getCustomerOperations(SupplierStatementStatusOperateRel.INVALID));
                statementVo.setSupplierPermissionButton(SupplierStatementStatusOperateRel.getSupplierOperations(SupplierStatementStatusOperateRel.INVALID));
                break;
            case RETURNED:
                statementVo.setPurchasePermissionButton(SupplierStatementStatusOperateRel.getCustomerOperations(SupplierStatementStatusOperateRel.RETURNED));
                statementVo.setSupplierPermissionButton(SupplierStatementStatusOperateRel.getSupplierOperations(SupplierStatementStatusOperateRel.RETURNED));
                break;
            case CONFIRMED:
                statementVo.setPurchasePermissionButton(SupplierStatementStatusOperateRel.getCustomerOperations(SupplierStatementStatusOperateRel.CONFIRMED));
                statementVo.setSupplierPermissionButton(SupplierStatementStatusOperateRel.getSupplierOperations(SupplierStatementStatusOperateRel.CONFIRMED));
                break;
            case TO_BE_SUPPLIER_CONFIRM:
                statementVo.setPurchasePermissionButton(SupplierStatementStatusOperateRel.getCustomerOperations(SupplierStatementStatusOperateRel.TO_BE_SUPPLIER_CONFIRM));
                statementVo.setSupplierPermissionButton(SupplierStatementStatusOperateRel.getSupplierOperations(SupplierStatementStatusOperateRel.TO_BE_SUPPLIER_CONFIRM));
                break;
            default:
                break;
        }

    }

    private void buildButtonIfSourceTypeIsPurchase(FinancialStatementVo statementVo) {
        if (statementVo == null) {
            return;
        }
        StatementStatus statementStatus = StatementStatus.getTypeByCode(statementVo.getStatementStatus());
        SourceType sourceType = SourceType.getTypeByCode(statementVo.getSourceType());
        if (statementStatus == null || sourceType == null) {
            return;
        }

        switch (statementStatus) {
            case DRAFT:
                statementVo.setPurchasePermissionButton(CustomerStatementStatusOperateRel.getCustomerOperations(CustomerStatementStatusOperateRel.DRAFT));
                statementVo.setSupplierPermissionButton(CustomerStatementStatusOperateRel.getSupplierOperations(CustomerStatementStatusOperateRel.DRAFT));
                break;
            case TO_BE_CUSTOMER_CONFIRM:
                statementVo.setPurchasePermissionButton(CustomerStatementStatusOperateRel.getCustomerOperations(CustomerStatementStatusOperateRel.TO_BE_CUSTOMER_CONFIRM));
                statementVo.setSupplierPermissionButton(CustomerStatementStatusOperateRel.getSupplierOperations(CustomerStatementStatusOperateRel.TO_BE_CUSTOMER_CONFIRM));
                break;
            case WITHDRAWN:
                statementVo.setPurchasePermissionButton(CustomerStatementStatusOperateRel.getCustomerOperations(CustomerStatementStatusOperateRel.WITHDRAWN));
                statementVo.setSupplierPermissionButton(CustomerStatementStatusOperateRel.getSupplierOperations(CustomerStatementStatusOperateRel.WITHDRAWN));
                break;
            case INVALID:
                statementVo.setPurchasePermissionButton(CustomerStatementStatusOperateRel.getCustomerOperations(CustomerStatementStatusOperateRel.INVALID));
                statementVo.setSupplierPermissionButton(CustomerStatementStatusOperateRel.getSupplierOperations(CustomerStatementStatusOperateRel.INVALID));
                break;
            case RETURNED:
                statementVo.setPurchasePermissionButton(CustomerStatementStatusOperateRel.getCustomerOperations(CustomerStatementStatusOperateRel.RETURNED));
                statementVo.setSupplierPermissionButton(CustomerStatementStatusOperateRel.getSupplierOperations(CustomerStatementStatusOperateRel.RETURNED));
                break;
            case CONFIRMED:
                statementVo.setPurchasePermissionButton(CustomerStatementStatusOperateRel.getCustomerOperations(CustomerStatementStatusOperateRel.CONFIRMED));
                statementVo.setSupplierPermissionButton(CustomerStatementStatusOperateRel.getSupplierOperations(CustomerStatementStatusOperateRel.CONFIRMED));
                break;
            case TO_BE_SUPPLIER_CONFIRM:
                statementVo.setPurchasePermissionButton(CustomerStatementStatusOperateRel.getCustomerOperations(CustomerStatementStatusOperateRel.TO_BE_SUPPLIER_CONFIRM));
                statementVo.setSupplierPermissionButton(CustomerStatementStatusOperateRel.getSupplierOperations(CustomerStatementStatusOperateRel.TO_BE_SUPPLIER_CONFIRM));
                break;
            default:
                break;
        }

    }


}
