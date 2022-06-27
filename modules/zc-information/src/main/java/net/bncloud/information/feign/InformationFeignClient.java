package net.bncloud.information.feign;

import net.bncloud.common.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("zc-information")
public interface InformationFeignClient {
    //测试
    @GetMapping("/zc-information-msg/feigntestdata/{id}")
    R<Object> feigntest(@PathVariable(value = "id") Long id);
}