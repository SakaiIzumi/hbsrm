package net.bncloud.financial.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CalculateUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.financial.entity.*;
import net.bncloud.financial.enums.FinancialBillTypeEnum;
import net.bncloud.financial.enums.FinancialResultCode;
import net.bncloud.financial.event.statement.StatementDetailChangeEvent;
import net.bncloud.financial.mapper.FinancialDeliveryDetailMapper;
import net.bncloud.financial.param.FinancialDeliveryDetailBatchSaveParam;
import net.bncloud.financial.param.FinancialDeliveryDetailParam;
import net.bncloud.financial.service.*;
import net.bncloud.financial.vo.FinancialDeliveryDetailVo;
import net.bncloud.support.Condition;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 出货明细表信息表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Service
public class FinancialDeliveryDetailServiceImpl extends BaseServiceImpl<FinancialDeliveryDetailMapper, FinancialDeliveryDetail> implements FinancialDeliveryDetailService {

    private final FinancialSettlementPoolService financialSettlementPoolService;
    private final FinancialStatementPoolRelService financialStatementPoolRelService;
    private final FinancialDeliveryBillLineService financialDeliveryBillLineService;
    private final FinancialDeliveryBillService financialDeliveryBillService;

    @Lazy
    @Autowired
    private  FinancialStatementService financialStatementService;

    public FinancialDeliveryDetailServiceImpl(FinancialSettlementPoolService financialSettlementPoolService, FinancialStatementPoolRelService financialStatementPoolRelService, FinancialDeliveryBillLineService financialDeliveryBillLineService, FinancialDeliveryBillService financialDeliveryBillService) {
        this.financialSettlementPoolService = financialSettlementPoolService;
        this.financialStatementPoolRelService = financialStatementPoolRelService;
        this.financialDeliveryBillLineService = financialDeliveryBillLineService;
        this.financialDeliveryBillService = financialDeliveryBillService;
    }


    @Override
    public IPage<FinancialDeliveryDetail> selectPage(IPage<FinancialDeliveryDetail> page, QueryParam<FinancialDeliveryDetailParam> pageParam) {
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }


    @Override
    public boolean saveDetails(Long statementId, List<FinancialDeliveryDetail> deliveryDetailList) {
        if (CollectionUtil.isNotEmpty(deliveryDetailList)) {
            int itemNo = 0;
            for (FinancialDeliveryDetail financialDeliveryDetail : deliveryDetailList) {
                financialDeliveryDetail.setId(null);
                financialDeliveryDetail.setStatementId(statementId);
                financialDeliveryDetail.setItemNo(++itemNo + "");
            }
            return saveBatch(deliveryDetailList);
        }
        return false;

    }

    /**
     * 生成对账单-收发货明细
     *
     * @param financialStatement 对账单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateDeliveryDetail(FinancialStatement financialStatement) {
        List<Long> deliveryIdList = getPoolIdList(financialStatement);
        assembleDeliveryDetail(financialStatement.getId(),deliveryIdList);
    }

    /**
     * 组装对账单送货明细
     * @param statementId
     * @param billId
     * @return
     */
    @Override
    public FinancialDeliveryDetailVo getBuildDeliveryDetail(Long statementId, Long billId){
        FinancialDeliveryBill one = financialDeliveryBillService.getById(billId);
        if(Objects.isNull(one)){
            return null;
        }
        FinancialDeliveryDetail deliveryDetail = buildDeliveryDetail(one);
        deliveryDetail.setStatementId(statementId);
        FinancialDeliveryDetailVo deliveryDetailVo = BeanUtil.copy(deliveryDetail, FinancialDeliveryDetailVo.class);
        return deliveryDetailVo;
    }

    private void assembleDeliveryDetail(Long statementId,List<Long> deliveryIdList){
        if (CollectionUtil.isEmpty(deliveryIdList)) {
            return;
        }
        List<FinancialDeliveryBill> deliveryBillList = financialDeliveryBillService.list(Condition.getQueryWrapper(new FinancialDeliveryBill()).lambda().in(FinancialDeliveryBill::getId, deliveryIdList));

        if (CollectionUtil.isNotEmpty(deliveryBillList)) {
            List<FinancialDeliveryDetail> deliveryDetailList = deliveryBillList.stream().map(this::buildDeliveryDetail).collect(Collectors.toList());
            saveDetails(statementId, deliveryDetailList);
        }
        financialSettlementPoolService.updateStatementCreatedByBillIdList(deliveryIdList, "Y");
    }


    /**
     * 批量保存送货明细时，
     * 将送货明细的 送货含税金额、对账含税金额、未税金额和税额 累加到对账单中
     * @param batchSaveParam 参数
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchSave(FinancialDeliveryDetailBatchSaveParam batchSaveParam) {
        long statementId = batchSaveParam.getStatementId();
        List<Long> deliveryIdList = batchSaveParam.getDeliveryIdList();
        assembleDeliveryDetail(statementId,deliveryIdList);

        List<FinancialSettlementPool> list = financialSettlementPoolService.list(Wrappers.<FinancialSettlementPool>lambdaQuery()
                .in(FinancialSettlementPool::getBillId, deliveryIdList));
        if (CollectionUtil.isNotEmpty(list)) {
            Set<Long> set = list.stream().map(FinancialSettlementPool::getId).collect(Collectors.toSet());
            financialStatementPoolRelService.batchSave(set, statementId);
        }

        Collection<FinancialDeliveryDetail> deliveryDetails = listByIds(deliveryIdList);
        FinancialStatement statement = financialStatementService.getById(statementId);

        //含税金额
        Optional<BigDecimal> checkIncludeTaxOpt = deliveryDetails.stream().map(FinancialDeliveryDetail::getCheckIncludeTax).reduce(BigDecimal::add);
        checkIncludeTaxOpt.ifPresent(checkIncludeTax->{
            //对账含税金额
            statement.setStatementIncludingTax(checkIncludeTax.add(statement.getStatementIncludingTax()));
            //送货含税金额
            statement.setShipmentIncludingTax(checkIncludeTax.add(statement.getShipmentIncludingTax()));
        });

        //未税金额
        Optional<BigDecimal> checkNotAmountOpt = deliveryDetails.stream().map(FinancialDeliveryDetail::getCheckNotTaxAmount).reduce(BigDecimal::add);
        checkNotAmountOpt.ifPresent(checkNotAmount->{
            //对账未税金额
            statement.setStatementNotTax(checkNotAmount.add(statement.getStatementNotTax()));
            //送货未税金额
            statement.setShipmentNotTax(checkNotAmount.add(statement.getShipmentNotTax()));
        });

        //税额
        Optional<BigDecimal> checkTaxAmountOpt = deliveryDetails.stream().map(FinancialDeliveryDetail::getCheckTaxAmount).reduce(BigDecimal::add);
        checkTaxAmountOpt.ifPresent(checkTaxAmount->{
            //对账税额
            statement.setStatementTaxAmount(checkTaxAmount.add(statement.getStatementTaxAmount()));
            //送货税额
            statement.setShipmentTaxAmount(checkTaxAmount.add(statement.getShipmentTaxAmount()));
        });

        financialStatementService.updateById(statement);

        applicationEventPublisher.publishEvent(new StatementDetailChangeEvent(this, statementId));
    }

    /**
     * 删除对账单，收货明细
     *
     * @param id 明细ID
     */
    @Override
    public void deleteDetail(long id) {
        FinancialDeliveryDetail deliveryDetail = getById(id);
        if (deliveryDetail == null) {
            throw new BizException(FinancialResultCode.RESOURCE_NOT_FOUND);
        }
        removeById(id);
        financialSettlementPoolService.updateStatementCreatedByBillIdList(Collections.singletonList(deliveryDetail.getDeliveryBillId()), "N");
    }

    /**
     * @param statementId
     */
    @Override
    public void deleteByStatementId(long statementId) {
        List<FinancialDeliveryDetail> list = list(Wrappers.<FinancialDeliveryDetail>query().lambda().eq(FinancialDeliveryDetail::getStatementId,statementId));
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<Long> collect = list.stream().map(FinancialDeliveryDetail::getId).collect(Collectors.toList());
        List<Long> deliveryBillIds = list.stream().map(FinancialDeliveryDetail::getDeliveryBillId).collect(Collectors.toList());
        removeByIds(collect);
        financialSettlementPoolService.updateStatementCreatedByBillIdList(deliveryBillIds, "N");
    }


    /**
     * 根据对账单ID，查询金额汇总信息
     *
     * @param statementId 对账单ID
     * @return 金额汇总信息
     */
    @Override
    public FinancialStatement querySummaryAmountByStatementId(Long statementId) {
        return this.baseMapper.querySummaryAmountByStatementId(statementId);
    }

    /**
     * 由送货单构建送货明细
     *
     * @param bill 送货单信息
     * @return 送货明细
     */
    private FinancialDeliveryDetail buildDeliveryDetail(FinancialDeliveryBill bill) {
        if (bill != null) {
            List<FinancialDeliveryBillLine> deliveryBillLineList = financialDeliveryBillLineService.list(Condition.getQueryWrapper(new FinancialDeliveryBillLine()).lambda().eq(FinancialDeliveryBillLine::getDeliveryBillId, bill.getId()));
            FinancialDeliveryDetail deliveryDetail = new FinancialDeliveryDetail();
            BigDecimal shipmentIncludingTax = new BigDecimal(0);
            BigDecimal shipmentNotTax = new BigDecimal(0);
            BigDecimal shipmentTaxAmount = new BigDecimal(0);
            BigDecimal taxRate = new BigDecimal(0);
            boolean haveTax = false;

            for(FinancialDeliveryBillLine billLine : deliveryBillLineList){
                shipmentIncludingTax = CalculateUtil.add(shipmentIncludingTax, billLine.getTaxIncludedAmount());
                shipmentNotTax = CalculateUtil.add(shipmentNotTax, billLine.getNotTaxAmount());
                shipmentTaxAmount = CalculateUtil.add(shipmentTaxAmount, billLine.getTaxAmount());
                taxRate = billLine.getTaxRate();
                haveTax = billLine.getHaveTax();
            }
            deliveryDetail.setErpBillId(bill.getErpBillId());
            deliveryDetail.setDeliveryBillId(bill.getId());
            deliveryDetail.setBillNo(bill.getDeliveryBillNo());
            deliveryDetail.setErpBillNo(bill.getErpBillNo());
            deliveryDetail.setDeliveryNum(bill.getDeliveryNum());
            deliveryDetail.setDeliveryDate(bill.getDeliveryDate());
            deliveryDetail.setDeliveryAmount(bill.getDeliveryAmount());
            deliveryDetail.setConfirmedAmount(bill.getDeliveryAmount());
            deliveryDetail.setHaveTax(haveTax);
            deliveryDetail.setTaxRate(taxRate);
            deliveryDetail.setCheckAmount(bill.getDeliveryAmount());
            deliveryDetail.setCheckQuantity(bill.getDeliveryNum());
            deliveryDetail.setCheckIncludeTax(shipmentIncludingTax);
            deliveryDetail.setCheckNotTaxAmount(shipmentNotTax);
            deliveryDetail.setCheckTaxAmount(shipmentTaxAmount);
            deliveryDetail.setRemark(bill.getRemark());
            return deliveryDetail;
        }
        return null;
    }

    private List<Long> getPoolIdList(FinancialStatement financialStatement) {
        LambdaQueryWrapper<FinancialStatementPoolRel> queryWrapper = Condition.getQueryWrapper(new FinancialStatementPoolRel()).lambda();
        queryWrapper.eq(FinancialStatementPoolRel::getStatementId, financialStatement.getId());
        List<FinancialStatementPoolRel> poolRelList = financialStatementPoolRelService.list(queryWrapper);
        if (CollectionUtils.isEmpty(poolRelList)) {
            return null;
        }

        Set<Long> poolIds = poolRelList.stream().map(FinancialStatementPoolRel::getSettlementPoolId).collect(Collectors.toSet());
        LambdaQueryWrapper<FinancialSettlementPool> queryWrapper2 = Condition.getQueryWrapper(new FinancialSettlementPool()).lambda();
        queryWrapper2.in(FinancialSettlementPool::getId, poolIds);
        queryWrapper2.eq(FinancialSettlementPool::getBillType, FinancialBillTypeEnum.DELIVERY.name());

        List<FinancialSettlementPool> list = financialSettlementPoolService.list(queryWrapper2);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.stream().map(FinancialSettlementPool::getBillId).collect(Collectors.toList());
        }

        return null;
    }

    /**
     * 查询下一个项次
     *
     * @param statementId 对账单ID
     * @return 下一个项次编号
     */
    @Override
    public String queryNextItemNo(Long statementId){
        return this.baseMapper.queryNextItemNo(statementId);
    }


}
