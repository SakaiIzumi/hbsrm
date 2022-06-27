package net.bncloud.bis.service.api.feign;

import net.bncloud.bis.service.api.fallbackfactory.MrpResultSyncFeignClientFallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * desc: mrp结果同步feignclient
 *
 * @author Rao
 * @Date 2022/05/23
 **/
@AuthorizedFeignClient(name = "bis",path = "bis",fallbackFactory = MrpResultSyncFeignClientFallbackFactory.class)
public interface MrpResultSyncFeignClient {

    /**
     * 同步计划排程的送货计划接口
     * @return
     */
    @PostMapping("syncMrpPlanOrderByErp")
    R<Object> syncMrpPlanOrderByErp(@RequestParam(name = "computerNo") String computerNo);

}
