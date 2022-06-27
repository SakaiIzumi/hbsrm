package net.bncloud.service.api.platform.sys.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.api.feign.saas.sys.DictItemDTO;
import net.bncloud.common.api.R;

import net.bncloud.service.api.platform.sys.dto.CfgParamDTO;
import net.bncloud.service.api.platform.sys.dto.MrpCfgDTO;
import net.bncloud.service.api.platform.sys.dto.SetSupplierConfigDTO;
import net.bncloud.service.api.platform.sys.feign.CfgParamResourceFeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 需要类
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/1/25
 */
@Slf4j
@Component
public class CfgParamFallbackFactory implements FallbackFactory<CfgParamResourceFeignClient> {

    @Override
    public CfgParamResourceFeignClient create(Throwable throwable) {
        log.error(" CfgParamResourceFeignClient open feign ",throwable);
        return new CfgParamResourceFeignClient() {
            @Override
            public R<CfgParamDTO> getParamByCode(String code) {
                return R.fail(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"获取协同配置数据接口调用失败！");
            }

            @Override
            public R<List<CfgParamDTO>> findListByCode(String code) {
                throwable.printStackTrace();
                return R.fail(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"获取协同配置数据接口调用失败！");
            }

            @Override
            public R<List<DictItemDTO>> findDictItemListByCode(String code) {
                return R.fail(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"获取数据字典接口调用失败！");
            }

            @Override
            public R setConfig(SetSupplierConfigDTO dto) {
                return R.fail(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"初始化供应商的订阅配置失败！");
            }

            @Override
            public R<MrpCfgDTO> getAllSubscribeConfig() {
                return R.fail(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"获取mrp所有自动订阅和默认工作日配置失败！");
            }


        };
    }
}
