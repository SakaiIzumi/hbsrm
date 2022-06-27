package net.bncloud.financial.event.statement.listener;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.service.api.feign.SettlementPoolFeignClient;
import net.bncloud.financial.entity.FinancialCostDetail;
import net.bncloud.financial.entity.FinancialDeliveryDetail;
import net.bncloud.financial.entity.FinancialStatement;
import net.bncloud.financial.event.statement.StatementComfirmEvent;
import net.bncloud.financial.service.FinancialCostDetailService;
import net.bncloud.financial.service.FinancialDeliveryDetailService;
import net.bncloud.financial.service.FinancialStatementService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Toby
 */
@Component
@Slf4j
public class StatementComfirmEventListener {

    private final FinancialStatementService statementService;

    private final FinancialDeliveryDetailService deliveryDetailService;

    private final FinancialCostDetailService costDetailService;

    @Resource
    private SettlementPoolFeignClient settlementPoolFeignClient;


    public StatementComfirmEventListener(FinancialStatementService statementService, FinancialDeliveryDetailService deliveryDetailService, FinancialCostDetailService costDetailService) {
        this.statementService = statementService;
        this.deliveryDetailService = deliveryDetailService;
        this.costDetailService = costDetailService;
    }

    @EventListener(StatementComfirmEvent.class)
    public void statementDetailChangeListener(StatementComfirmEvent comfirmEvent) {
        Long statementId = comfirmEvent.getStatementId();
        FinancialStatement statement = statementService.getById(statementId);
        if (statement == null) {
            log.warn("未查询到对账单信息");
            return;
        }
        List<FinancialDeliveryDetail> deliveryDetailList = deliveryDetailService.list(Wrappers.<FinancialDeliveryDetail>query()
                .lambda().eq(FinancialDeliveryDetail::getStatementId,statementId));
        List<FinancialCostDetail> costDetailList = costDetailService.list(Wrappers.<FinancialCostDetail>query()
                .lambda().eq(FinancialCostDetail::getStatementId,statementId));

        List<Long> ids = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(deliveryDetailList)){
            Set<Long> collect = deliveryDetailList.stream().map(FinancialDeliveryDetail::getErpBillId).collect(Collectors.toSet());
            ids.addAll(collect);
        }
        if(CollectionUtils.isNotEmpty(costDetailList)){
            Set<Long> collect = costDetailList.stream().map(FinancialCostDetail::getErpBillId).collect(Collectors.toSet());
            ids.addAll(collect);
        }
        settlementPoolFeignClient.updateStatementStatus(ids);
    }


}
