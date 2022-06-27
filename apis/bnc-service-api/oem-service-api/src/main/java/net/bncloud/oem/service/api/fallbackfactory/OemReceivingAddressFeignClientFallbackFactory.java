package net.bncloud.oem.service.api.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.oem.service.api.feign.OemReceivingAddressFeignClient;
import net.bncloud.oem.service.api.vo.OemReceivingAddressVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/24
 **/
@Component
@Slf4j
public class OemReceivingAddressFeignClientFallbackFactory implements FallbackFactory<OemReceivingAddressFeignClient> {

    @Override
    public OemReceivingAddressFeignClient create(Throwable throwable) {
        log.error("OemReceivingAddressFeignClient >> Oem服务调用失败！",throwable);
        return new OemReceivingAddressFeignClient() {

            @Override
            public R<String> syncOemReceivingAddress(List<OemReceivingAddressVo> oemReceivingAddressVoList) {
                return R.fail();
            }
        };
    }
}
