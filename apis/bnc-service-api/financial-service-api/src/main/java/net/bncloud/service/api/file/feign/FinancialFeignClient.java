package net.bncloud.service.api.file.feign;

import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;

import net.bncloud.security.client.AuthorizedFeignClient;

import net.bncloud.service.api.file.dto.FinancialCostBillDto;
import net.bncloud.service.api.file.dto.FinancialDeliveryBillDto;
import net.bncloud.service.api.file.dto.FinancialSettlementPoolDto;
import net.bncloud.service.api.file.feign.fallback.FinancialFeignClientFallbackFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@AuthorizedFeignClient(name = "financial",contextId = "financialFeignClient" ,fallbackFactory = FinancialFeignClientFallbackFactory.class, decode404 = true)
public interface FinancialFeignClient {

    /**
     * 批量新增费用单
     *
     * @param paramList
     * @return
     */
    @PostMapping("/financial/cost_bill/batchSave")
    @ApiOperation(value = "批量新增费用单")
    R<Void> batchSaveCostBill(@RequestBody List<FinancialCostBillDto> paramList);

    /**
     * 批量新增送货单
     *
     * @param paramList
     * @return
     */
    @PostMapping("/financial/delivery_bill/batchSave")
    @ApiOperation(value = "批量新增送货单")
    R<Void> batchSaveDeliveryBill(@RequestBody List<FinancialDeliveryBillDto> paramList);
}
