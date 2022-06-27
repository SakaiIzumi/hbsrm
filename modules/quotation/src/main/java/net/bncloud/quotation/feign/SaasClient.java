//package net.bncloud.quotation.feign;
//
//import net.bncloud.common.api.R;
//import net.bncloud.quotation.feign.fallback.SaasClientFallbackFactory;
//import net.bncloud.security.client.AuthorizedFeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.Map;
//
//@AuthorizedFeignClient(name = "platform", contextId = "platformEventClient", fallbackFactory = SaasClientFallbackFactory.class, decode404 = true)
//public interface SaasClient {
//
//    @PostMapping("/user-center/users/getUserInfoByUid")
//    R<Map<String,Object>> getUserInfoByUid(@RequestParam("uid") String uid);
//
//}
