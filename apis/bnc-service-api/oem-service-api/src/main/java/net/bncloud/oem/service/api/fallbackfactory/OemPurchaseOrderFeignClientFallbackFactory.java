package net.bncloud.oem.service.api.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.oem.service.api.feign.OemPurchaseOrderFeignClient;
import net.bncloud.oem.service.api.vo.OemPurchaseOrderVo;
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
public class OemPurchaseOrderFeignClientFallbackFactory implements FallbackFactory<OemPurchaseOrderFeignClient> {

    @Override
    public OemPurchaseOrderFeignClient create(Throwable throwable) {
        log.error("OemPurchaseOrderFeignClient >> Oem服务调用失败！",throwable);
        return new OemPurchaseOrderFeignClient(){

            @Override
            public R<String> syncOemPurchaseOrder(List<OemPurchaseOrderVo> oemPurchaseOrderVoList) {
                return R.fail();
            }
        };
    }
}
