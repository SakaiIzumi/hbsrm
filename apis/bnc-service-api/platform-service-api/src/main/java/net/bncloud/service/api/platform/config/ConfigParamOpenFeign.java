package net.bncloud.service.api.platform.config;

import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.platform.config.enums.CfgParamKeyEnum;
import net.bncloud.service.api.platform.config.fallbackfactory.ConfigParamFeignClientFallbackFactory;
import net.bncloud.service.api.platform.config.vo.CfgParamInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * desc: 配置类 接口
 *
 * @author Rao
 * @Date 2022/03/11
 **/
@AuthorizedFeignClient(name = "platform",contextId = "configParamOpenFeign", fallbackFactory = ConfigParamFeignClientFallbackFactory.class,decode404 = true)
public interface ConfigParamOpenFeign {
    /**
     * 查询配置 配置key枚举 和 组织ID
     * @param cfgParamKeyEnum
     * @param orgId 组织ID
     * @return
     */
    @GetMapping("/sys/cfg/param/findListByCodeAndOrgId")
    R<CfgParamInfo> findListByCodeAndOrgId(@RequestParam(name = "cfgParamKeyEnum") CfgParamKeyEnum cfgParamKeyEnum, @RequestParam(value = "orgId")Long orgId);
}
