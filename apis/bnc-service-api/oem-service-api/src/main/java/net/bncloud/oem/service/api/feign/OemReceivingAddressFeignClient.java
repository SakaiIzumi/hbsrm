package net.bncloud.oem.service.api.feign;

import net.bncloud.common.api.R;
import net.bncloud.oem.service.api.fallbackfactory.OemReceivingAddressFeignClientFallbackFactory;
import net.bncloud.oem.service.api.vo.OemReceivingAddressVo;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/24
 **/
@AuthorizedFeignClient(name = "oem",path = "/oem/receiving/address", contextId = "oemReceivingAddressFeignClient", fallbackFactory = OemReceivingAddressFeignClientFallbackFactory.class)
public interface OemReceivingAddressFeignClient {

    /**
     * 同步Oem收货地址
     */
    @PostMapping("syncOemReceivingAddress")
    R<String> syncOemReceivingAddress(@RequestBody List<OemReceivingAddressVo> oemReceivingAddressVoList);

}
