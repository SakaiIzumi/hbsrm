package net.bncloud.service.api.platform.sys.feign;

import net.bncloud.api.feign.saas.sys.DictItemDTO;
import net.bncloud.common.api.R;

import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.dto.MrpCfgDTO;
import net.bncloud.service.api.platform.sys.dto.SetSupplierConfigDTO;
import net.bncloud.service.api.platform.sys.feign.fallback.CfgParamFallbackFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/1/25
 */
@AuthorizedFeignClient(name = "platform", contextId = "cfgParamResourceFeignClient", fallbackFactory = CfgParamFallbackFactory.class)
public interface CfgParamResourceFeignClient {


    /**
     * @param code
     * @return
     */
    @GetMapping ("/sys/cfg/param/getByCode")
    R<CfgParamDTO> getParamByCode(@RequestParam(value = "code") String code);


    @GetMapping ("/sys/cfg/param/findListByCode")
    R<List<CfgParamDTO>> findListByCode(@RequestParam(name = "code") String code);

    /**
     * 查字典
     * @param code
     * @return
     */
    @GetMapping ("/sys/dict-item/items/{code}")
    R<List<DictItemDTO>> findDictItemListByCode(@PathVariable(name = "code") String code);

    /**
     * 初始化新的供应商设置订阅节假日配置
     * @return
     */
    @PostMapping("/sys/dict-item/items/set")
    R setConfig(SetSupplierConfigDTO dto);


    /**
     * @return
     */
    @PostMapping ("/sys/cfg/param/MrpAllConfig")
    R<MrpCfgDTO> getAllSubscribeConfig();


}
