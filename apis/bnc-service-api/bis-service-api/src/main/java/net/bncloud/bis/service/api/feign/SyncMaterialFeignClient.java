package net.bncloud.bis.service.api.feign;

import net.bncloud.bis.service.api.fallbackfactory.SyncMaterialFeignClientFallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 物料信息表
 * @author lijiaju
 * @date 2022/2/21 17:15
 */
@AuthorizedFeignClient(name = "bis",path = "bis", contextId = "syncMaterialFeignClient", fallbackFactory = SyncMaterialFeignClientFallbackFactory.class)
public interface SyncMaterialFeignClient {
    /**
     * 同步全部物料信息
     * @param timeSyncCount 每次同步的数据 全量同步时候需要关注 建议每次100条
     * @return
     */
    @GetMapping("/syncMaterial/syncAllMaterialData")
    R<Object> syncAllMaterialData(@RequestParam(required = false,defaultValue = "500") Integer timeSyncCount);
}
