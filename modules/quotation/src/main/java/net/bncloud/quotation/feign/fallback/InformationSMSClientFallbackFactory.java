//package net.bncloud.quotation.feign.fallback;
//
//import net.bncloud.common.api.R;
//import net.bncloud.common.service.base.domain.SendMsgParam;
//import net.bncloud.quotation.feign.InformationSMSClient;
//import org.springframework.cloud.openfeign.FallbackFactory;
//import org.springframework.web.bind.annotation.PostMapping;
//
//public class InformationSMSClientFallbackFactory implements FallbackFactory<InformationSMSClient> {
//    @Override
//    public InformationSMSClient create(Throwable throwable) {
//        return new InformationSMSClient() {
//            @PostMapping("/zc-information-sms/save")
//            @Override
//            public  R save(SendMsgParam sendMsgParam) {
//                return R.fail("用户服务暂时不可用，获取用户信息失败，请稍后再试");
//            }
//        };
//    }
//}
