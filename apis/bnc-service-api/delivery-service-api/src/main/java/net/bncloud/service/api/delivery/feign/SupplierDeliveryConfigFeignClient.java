package net.bncloud.service.api.delivery.feign;

import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.delivery.feign.fallback.SupplierDeliveryConfigFeignClientFallbackFactory;
import net.bncloud.service.api.delivery.vo.SupplierDeliveryConfigVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * desc: 供应商送货配置接口
 *
 * @author Rao
 * @Date 2022/06/20
 **/
@AuthorizedFeignClient(name = "delivery",path = "delivery",fallbackFactory = SupplierDeliveryConfigFeignClientFallbackFactory.class)
public interface SupplierDeliveryConfigFeignClient {

    /**
     * 获取供应商送货配置列表
     * @param supplierCodeList
     * @return
     */
    @PostMapping("/supplierDeliveryConfig/getSupplierDeliveryConfigList")
    R<List<SupplierDeliveryConfigVo>> getSupplierDeliveryConfigListBySupplierCodeList(@RequestBody List<String> supplierCodeList);

}
