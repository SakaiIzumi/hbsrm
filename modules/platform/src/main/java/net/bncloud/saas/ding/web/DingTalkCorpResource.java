package net.bncloud.saas.ding.web;

import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.saas.ding.domain.DingTalkCorp;
import net.bncloud.saas.ding.domain.DingTalkOrgIntegrationConfig;
import net.bncloud.saas.ding.service.DingTalkCorpService;
import net.bncloud.saas.ding.web.payload.IntegrationConfig;
import net.bncloud.saas.ding.web.payload.RelateDingTalkCorpPayload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ding-talk/corp")
public class DingTalkCorpResource {

    private final DingTalkCorpService dingTalkCorpService;

    public DingTalkCorpResource(DingTalkCorpService dingTalkCorpService) {
        this.dingTalkCorpService = dingTalkCorpService;
    }

    @ApiOperation("组织绑定钉钉组织")
    @PostMapping("/bind")
    public R<DingTalkCorp> orgBindDingTalkCorp(@RequestBody RelateDingTalkCorpPayload payload) {
        DingTalkCorp dingTalkCorp = dingTalkCorpService.bind(payload.getOrgId(), payload.getOrgName(), payload.getCorpId());
        return R.data(dingTalkCorp);
    }

    @ApiOperation("组织集成配置")
    @PostMapping("/integration-config")
    public R<DingTalkOrgIntegrationConfig> configIntegrationApp(@RequestBody IntegrationConfig config) {
        DingTalkOrgIntegrationConfig data = dingTalkCorpService.configIntegrationApp(config.getCorpId(), config.getAgentId(), config.getAppKey(), config.getAppSecret());
        return R.data(data);
    }
}
