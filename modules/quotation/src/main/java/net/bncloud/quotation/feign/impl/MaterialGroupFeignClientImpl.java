package net.bncloud.quotation.feign.impl;

import lombok.AllArgsConstructor;
import net.bncloud.quotation.service.MaterialGroupInfoService;
import net.bncloud.quotation.service.MaterialInfoService;
import net.bncloud.quotation.service.api.dto.MaterialGroupInfoDTO;
import net.bncloud.quotation.service.api.dto.MaterialInfoDTO;
import net.bncloud.quotation.service.api.feign.MaterialFeignClient;
import net.bncloud.quotation.service.api.feign.MaterialGroupFeignClient;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lijiaju
 * @date 2022/2/22 9:37
 */
@RestController
@AllArgsConstructor
public class MaterialGroupFeignClientImpl implements MaterialGroupFeignClient {

    private MaterialGroupInfoService materialGroupInfoService;

    @Override
    public void syncMaterialGroupDataSaveOrUpdate(List<MaterialGroupInfoDTO> materialGroupInfoDTOS) {
        materialGroupInfoService.syncMaterialGroupDataSaveOrUpdate(materialGroupInfoDTOS);
    }
}
