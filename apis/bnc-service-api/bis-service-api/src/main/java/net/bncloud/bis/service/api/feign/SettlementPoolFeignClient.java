package net.bncloud.bis.service.api.feign;

import net.bncloud.bis.service.api.fallbackfactory.SettlementPoolFeignClientFallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 物料信息表
 * @author lijiaju
 * @date 2022/2/21 17:15
 */
@AuthorizedFeignClient(name = "bis",path = "bis", contextId = "settlementPoolFeignClient", fallbackFactory = SettlementPoolFeignClientFallbackFactory.class)
public interface SettlementPoolFeignClient {
    /**
     * 更新对账单状态到erp
     * @param ids erp单据id集合
     * @return
     */
    @PostMapping("/financial/updateStatementStatus")
    R<Void> updateStatementStatus(@RequestBody List<Long> ids);
}
