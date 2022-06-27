package net.bncloud.saas.ding.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserGetuserinfoRequest;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserGetuserinfoResponse;
import com.taobao.api.ApiException;
import net.bncloud.api.feign.saas.user.DingTalkAuthCode;
import net.bncloud.common.util.ApplicationContextProvider;
import net.bncloud.saas.ding.domain.DingTalkCorp;
import net.bncloud.saas.ding.domain.DingUser;
import net.bncloud.saas.ding.domain.pk.DingUserPK;
import net.bncloud.saas.ding.repository.DingTalkCorpRepository;
import net.bncloud.saas.ding.repository.DingUserRepository;
import net.bncloud.saas.event.DingUserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DingTalkUserAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DingTalkUserAuthService.class);

    private final DingTalkTokenService dingTalkTokenService;
    private final DingUserRepository dingUserRepository;
    private final DingTalkCorpRepository dingTalkCorpRepository;

    public DingTalkUserAuthService(DingTalkTokenService dingTalkTokenService,
                                   DingUserRepository dingUserRepository,
                                   DingTalkCorpRepository dingTalkCorpRepository) {
        this.dingTalkTokenService = dingTalkTokenService;
        this.dingUserRepository = dingUserRepository;
        this.dingTalkCorpRepository = dingTalkCorpRepository;
    }

    public DingUser authByCode(DingTalkAuthCode authCode) {

        final String corpId = authCode.getCorpId();
        final Long agentId = authCode.getAgentId();

        return dingTalkCorpRepository.findById(corpId)
                .map(dingTalkCorp -> getCorpUser(dingTalkCorp, agentId, authCode.getCode()))
                .orElse(null);
        // TODO 是否需要同步钉钉用户到系统，正常流程是系统用户同步到钉钉
    }

    private DingUser getCorpUser(DingTalkCorp corp, Long agentId, String code) {
        try {
            String accessToken = dingTalkTokenService.getAppAccessToken(corp, agentId);
            LOGGER.info("获取accessToken: {}, corpId: {}, agentId: {}, code: {}", accessToken, corp.getCorpId(), agentId, code);
            String userId = getUserIdByAuthCode(code, accessToken);

            LOGGER.info("获取到DingTalk userId: {}", userId);

            Optional<DingUser> dingUserOptional = dingUserRepository.findById(DingUserPK.of(corp.getCorpId(), userId));
            return dingUserOptional.orElseGet(() -> bindDingUser(corp.getCorpId(), userId, accessToken));
        } catch (ApiException e) {
            return null;
        }
    }

    private DingUser bindDingUser(String corpId, String userId, String accessToken) {
        try {
            final DingUser dingUser = getUserByUserId(corpId, userId, accessToken);
            LOGGER.info("从dingTalk获取到的用户信息: {}", dingUser);
            dingTalkCorpRepository.findById(corpId).ifPresent(dingUser::setCorp);
            dingUserRepository.save(dingUser);
            ApplicationContextProvider.getApplicationContext().publishEvent(new DingUserCreatedEvent(this, dingUser));
            return dingUser;
        } catch (ApiException e) {
            return null;
        }
    }

    private String getUserIdByAuthCode(String code, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getuserinfo");
        OapiV2UserGetuserinfoRequest req = new OapiV2UserGetuserinfoRequest();
        req.setCode(code);
        OapiV2UserGetuserinfoResponse response = client.execute(req, accessToken);

        OapiV2UserGetuserinfoResponse.UserGetByCodeResponse result = response.getResult();

        return result.getUserid();
    }

    private DingUser getUserByUserId(String corpId, String userId, String accessToken) throws ApiException {

        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
        OapiV2UserGetRequest req = new OapiV2UserGetRequest();
        req.setUserid(userId);
        req.setLanguage("zh_CN");
        OapiV2UserGetResponse rsp = client.execute(req, accessToken);
        OapiV2UserGetResponse.UserGetResponse result = rsp.getResult();
        DingUser u = new DingUser();
        u.setId(DingUserPK.of(corpId, userId));
        u.setUnionId(result.getUnionid());
        u.setName(result.getName());
        u.setAvatar(result.getAvatar());
        u.setStateCode(result.getStateCode());
        u.setMobile(result.getMobile());
        u.setHideMobile(result.getHideMobile());
        u.setJobNumber(result.getJobNumber());
        u.setTitle(result.getTitle());
        u.setEmail(result.getEmail());
        u.setWorkPlace(result.getWorkPlace());
        u.setRemark(result.getRemark());
        u.setActive(result.getActive());
        u.setRealAuthed(result.getRealAuthed());
        u.setSenior(result.getSenior());
        u.setAdmin(result.getAdmin());
        u.setBoss(result.getBoss());
        final List<Long> deptIdList = result.getDeptIdList();
        u.setDeptIdList(String.valueOf(deptIdList));
        return u;
    }
}
