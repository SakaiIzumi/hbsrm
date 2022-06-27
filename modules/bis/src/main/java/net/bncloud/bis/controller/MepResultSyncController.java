package net.bncloud.bis.controller;

import net.bncloud.bis.manager.ExperimentSupplierManager;
import net.bncloud.bis.manager.MrpDeliveryPlanManager;
import net.bncloud.bis.service.api.feign.MrpResultSyncFeignClient;
import net.bncloud.common.api.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/23
 **/
@RestController
@RequestMapping
public class MepResultSyncController implements MrpResultSyncFeignClient {

    @Resource
    private ExperimentSupplierManager experimentSupplierManager;

    /**
     * 同步计划排程的送货计划接口
     * @param computerNo
     * @return
     */
    @Override
    public R<Object> syncMrpPlanOrderByErp(String computerNo) {

        return experimentSupplierManager.experimentSupplierSyncMrpPlanOrder(computerNo );
    }
}
