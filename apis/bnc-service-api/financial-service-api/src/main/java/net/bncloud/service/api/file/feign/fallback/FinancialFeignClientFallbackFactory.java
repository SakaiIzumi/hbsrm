package net.bncloud.service.api.file.feign.fallback;


import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.service.api.file.dto.FinancialCostBillDto;
import net.bncloud.service.api.file.dto.FinancialDeliveryBillDto;
import net.bncloud.service.api.file.dto.FinancialSettlementPoolDto;
import net.bncloud.service.api.file.feign.FinancialFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName FileServiceFeignClientFallbackFactory
 * @Description: 文件中心服务异常处理
 * @Author Administrator
 * @Date 2021/4/7
 * @Version V1.0
 **/
@Component
@Slf4j
public  class FinancialFeignClientFallbackFactory implements FallbackFactory<FinancialFeignClient> {

    @Override
    public FinancialFeignClient create(Throwable throwable) {
       return new FinancialFeignClient() {
           @Override
           public R<Void> batchSaveCostBill(List<FinancialCostBillDto> paramList) {
               log.error("feign error!",throwable);
               return R.fail("批量新增费用单不可用，请稍后重试！");
           }

           @Override
           public R<Void> batchSaveDeliveryBill(List<FinancialDeliveryBillDto> paramList) {
               log.error("feign error!",throwable);
               return R.fail("批量新增送货单不可用，请稍后重试！");
           }
       };
    }
}
