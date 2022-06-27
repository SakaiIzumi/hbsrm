package net.bncloud.bis.controller;

import lombok.AllArgsConstructor;
import net.bncloud.bis.manager.SyncMaterialManager;
import net.bncloud.bis.service.api.feign.SyncMaterialFeignClient;
import net.bncloud.common.api.R;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijiaju
 * @date 2022/2/21 17:14
 */
@RestController
@AllArgsConstructor
public class SyncMaterialControl implements SyncMaterialFeignClient {

    private final SyncMaterialManager syncMaterialManager;

    @Override
    public R<Object> syncAllMaterialData(Integer timeSyncCount) {
        return syncMaterialManager.syncAllMaterialData(timeSyncCount);
    }
}
