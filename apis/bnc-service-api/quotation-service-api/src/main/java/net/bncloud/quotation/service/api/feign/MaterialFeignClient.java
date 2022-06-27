package net.bncloud.quotation.service.api.feign;

import net.bncloud.common.api.R;
import net.bncloud.quotation.service.api.dto.MaterialInfoDTO;
import net.bncloud.quotation.service.api.dto.MaterialInfoDetailDTO;
import net.bncloud.quotation.service.api.fallbackfactory.MaterialFeignClientFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author lijiaju
 * @date 2022/2/22 9:18
 */
@AuthorizedFeignClient(name = "quotation", path = "/quotation", contextId= "materialFeignClient", fallbackFactory = MaterialFeignClientFallbackFactory.class, decode404 = true)
public interface MaterialFeignClient {

    /**
     * 同步物料数据接口
     * */
    @PostMapping("/material/syncMaterialDataSaveOrUpdate")
    void syncMaterialDataSaveOrUpdate(@RequestBody List<MaterialInfoDTO> materialInfoDTOS);

    /**
     * OEM模块使用 回写时获取物料分类编码
     * */
    @PostMapping("/material/getMaterialDetailByCode")
    R<MaterialInfoDetailDTO> getMaterialDetailByCode(@RequestBody String code);

    /**
     * order delivery模块使用  获取排程供应商所属分组所拥有的物料
     * */
    @PostMapping("/material/selectMaterialCodeByGroupIds")
    R<List<String>> selectMaterialCodeByGroupIds(@RequestBody List<Long>materialGroupIds);
}
