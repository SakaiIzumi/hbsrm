package net.bncloud.information.config;

import com.swytec.ding.oapi.DingTalkApiService;
import com.swytec.ding.oapi.DingTalkApiServiceImpl;
import com.swytec.ding.oapi.client.DefaultDingTalkClientFactory;
import com.swytec.ding.oapi.client.DingTalkClientFactory;
import com.swytec.ding.oapi.provider.DingAppDetails;
import com.swytec.ding.oapi.provider.DingAppDetailsService;
import com.swytec.ding.oapi.provider.InMemoryDingAppDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * demo
 * TODO 如果后续没有用了要删除
 */
@Configuration
public class DingTalkConfiguration {
    private final ApplicationProperties applicationProperties;

    public DingTalkConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public DingAppDetailsService dingAppDetailsService() {
        final ApplicationProperties.Ding ding = applicationProperties.getDing();
        final InMemoryDingAppDetailsService dingAppDetailsService = new InMemoryDingAppDetailsService();
        dingAppDetailsService.addAppDetails(new DingAppDetails()
                .setAppId(ding.getCorpId() + ":" + ding.getAgentId())
                .setCorpId(ding.getCorpId())
                .setAgentId(ding.getAgentId())
                .setAppKey(ding.getAppKey())
                .setAppSecret(ding.getAppSecret())
        );
        return dingAppDetailsService;
    }

    @Bean
    public DingTalkApiService dingTalkApiService() {
        DingTalkClientFactory dingTalkClientFactory = new DefaultDingTalkClientFactory();
        DingTalkApiServiceImpl dingTalkApiServiceImpl = new DingTalkApiServiceImpl();
        dingTalkApiServiceImpl.setDingAppDetailsService(dingAppDetailsService());
        dingTalkApiServiceImpl.setDingTalkClientFactory(dingTalkClientFactory);
        return dingTalkApiServiceImpl;
    }
}
