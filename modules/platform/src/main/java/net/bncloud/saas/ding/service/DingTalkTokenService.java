package net.bncloud.saas.ding.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import net.bncloud.saas.ding.domain.DingTalkCorp;
import net.bncloud.saas.ding.domain.DingTalkOrgIntegrationConfig;
import net.bncloud.saas.ding.repository.DingTalkAppRepository;
import net.bncloud.saas.ding.repository.DingTalkOrgIntegrationConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class DingTalkTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DingTalkTokenService.class);

    private final DingTalkOrgIntegrationConfigRepository dingTalkOrgIntegrationConfigRepository;
    private final DingTalkAppRepository dingTalkAppRepository;

    public DingTalkTokenService(DingTalkOrgIntegrationConfigRepository dingTalkOrgIntegrationConfigRepository,
                                DingTalkAppRepository dingTalkAppRepository) {
        this.dingTalkOrgIntegrationConfigRepository = dingTalkOrgIntegrationConfigRepository;
        this.dingTalkAppRepository = dingTalkAppRepository;
    }

    public String getAppAccessToken(DingTalkCorp corp, Long agentId) {
        return dingTalkAppRepository.findOneByCorpAndAgentId(corp, agentId).map(dingTalkApp -> {
            System.out.println(dingTalkApp.needRefresh());
            if (dingTalkApp.needRefresh()) {
                try {
                    final String token = getCorpInternalToken(dingTalkApp.getAppKey(), dingTalkApp.getAppSecret());
                    LOGGER.info("原token {} 即将失效或已失效，刷新token: {}", dingTalkApp.getAccessToken(), token);
                    dingTalkApp.setAccessToken(token);
                    dingTalkApp.setCreateTokenAt(Instant.now());
                    dingTalkAppRepository.save(dingTalkApp);
                    return token;
                } catch (ApiException e) {
                    return dingTalkApp.getAccessToken();
                }
            } else {
                return dingTalkApp.getAccessToken();
            }
        }).orElse(null);
    }

    public String getCorpInternalAccessToken(String corpId) throws ApiException {
        Optional<DingTalkOrgIntegrationConfig> configOptional = dingTalkOrgIntegrationConfigRepository.findById(corpId);
        if (!configOptional.isPresent()) {
            throw new IllegalStateException("请配置APP"); // TODO
        }
        DingTalkOrgIntegrationConfig config = configOptional.get();
        config.checkNotBlank();
        return getAppAccessToken(config.getCorp(), config.getAgentId());
    }

    private String getCorpInternalToken(String appKey, String appSecret) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appKey);
        request.setAppsecret(appSecret);
        request.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(request);

        return response.getAccessToken();
    }
}
