package net.bncloud.service.api.platform.config.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.service.api.platform.config.ConfigParamOpenFeign;
import net.bncloud.service.api.platform.config.enums.CfgParamKeyEnum;
import net.bncloud.service.api.platform.config.vo.CfgParamInfo;
import org.springframework.stereotype.Component;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/03/11
 **/
@Slf4j
@Component
public class ConfigParamFeignClientFallbackFactory implements FallbackFactory<ConfigParamOpenFeign> {
    @Override
    public ConfigParamOpenFeign create(Throwable throwable) {
        log.error("ConfigParamOpenFeign remote error!",throwable);

        return new ConfigParamOpenFeign() {
            @Override
            public R<CfgParamInfo> findListByCodeAndOrgId(CfgParamKeyEnum cfgParamKeyEnum, Long orgId) {
                return R.fail("服务不可用，请稍后重试！");
            }
        };
    }
}
