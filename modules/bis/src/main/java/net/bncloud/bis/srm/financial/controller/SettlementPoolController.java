package net.bncloud.bis.srm.financial.controller;

import net.bncloud.bis.service.api.feign.SettlementPoolFeignClient;
import net.bncloud.bis.srm.financial.manager.SettlementPoolManager;
import net.bncloud.common.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: liulu
 * @Date: 2022-03-11 14:21
 */
@RestController
public class SettlementPoolController implements SettlementPoolFeignClient {

    @Autowired
    private SettlementPoolManager settlementPoolManager;

    @Override
    public R<Void> updateStatementStatus(List<Long> ids) {
        settlementPoolManager.updateStatementStatus(ids);
        return R.success();
    }
}
