package net.bncloud.order.feign;

import net.bncloud.common.api.R;
import net.bncloud.common.service.base.domain.HandlerMsgParam;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName FileServiceFeignClient
 * @Description: FileServiceFeignClient
 * @Author Administrator
 * @Date 2021/3/19
 * @Version V1.0
 **/

@AuthorizedFeignClient(name = "zc-information", contextId = "informationServiceFeignClient", decode404 = true)
public interface InformationServiceFeignClient {


    @PostMapping("/information-handler/handlerInformation")
    R handlerInformation(@RequestBody HandlerMsgParam handlerMsgParam);
}
