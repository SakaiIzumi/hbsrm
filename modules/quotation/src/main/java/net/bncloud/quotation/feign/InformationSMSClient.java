//package net.bncloud.quotation.feign;
//
//import net.bncloud.common.api.R;
//import net.bncloud.common.service.base.domain.SendMsgParam;
//import net.bncloud.quotation.feign.fallback.InformationSMSClientFallbackFactory;
//import net.bncloud.security.client.AuthorizedFeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@AuthorizedFeignClient(name = "zc-information", contextId = "informationSMSClient", fallbackFactory = InformationSMSClientFallbackFactory.class, decode404 = true)
//public interface InformationSMSClient {
//
//    @PostMapping("/zc-information-sms/save")
//    R save(@RequestBody SendMsgParam sendMsgParam);
//
//
//
//}
