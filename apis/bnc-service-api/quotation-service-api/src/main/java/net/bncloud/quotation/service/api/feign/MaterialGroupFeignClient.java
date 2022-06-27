package net.bncloud.quotation.service.api.feign;

import net.bncloud.quotation.service.api.dto.MaterialGroupInfoDTO;
import net.bncloud.quotation.service.api.fallbackfactory.MaterialFeignClientFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 同步物料组
 * @author lijiaju
 * @date 2022/2/22 9:18
 */
@AuthorizedFeignClient(name = "quotation", path = "/quotation", contextId= "materialGroupFeignClient", fallbackFactory = MaterialFeignClientFallbackFactory.class, decode404 = true)
public interface MaterialGroupFeignClient {
    @PostMapping("/material/syncMaterialGroupDataSaveOrUpdate")
    void syncMaterialGroupDataSaveOrUpdate(@RequestBody List<MaterialGroupInfoDTO> materialGroupInfoDTOS);
}
