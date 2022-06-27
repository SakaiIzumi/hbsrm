package net.bncloud.uaa.web.rest;

import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgentUtil;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import net.bncloud.common.util.IpAddressUtil;
import net.bncloud.security.annotation.AnonymousAccess;
import net.bncloud.security.annotation.web.AnonymousGetMapping;
import net.bncloud.uaa.security.AuthenticationService;
import net.bncloud.uaa.service.DingTalkService;
import net.bncloud.uaa.util.LoginLogContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@RequestMapping("/sso/third")
public class ThirdPartOauthController {

    private static final String STATE_KEY_PREFIX = "BNC:STATE:";

    @Value("${bnc.system.base-url}")
    private String baseUrl;

    @Value("${bnc.system.third-auth}")
    private String thirdAuth;


    private final AuthenticationService authenticationService;
    private final DingTalkService dingTalkService;
    private final WxCpService wxCpService;
    private final StringRedisTemplate stringRedisTemplate;

    public ThirdPartOauthController(AuthenticationService authenticationService,
                                    DingTalkService dingTalkService,
                                    WxCpService wxCpService,
                                    StringRedisTemplate stringRedisTemplate) {
        this.authenticationService = authenticationService;
        this.dingTalkService = dingTalkService;
        this.wxCpService = wxCpService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @GetMapping("/clients")
    public void authClients() {

    }

    @AnonymousAccess
    @GetMapping("/snsqr")
    public String snsQr(@RequestParam(required = false) String type) {
        if (StringUtils.isBlank(type) || StringUtils.equalsIgnoreCase(type, "WXWORK_QR")) {
            final String url = wxCpService.buildQrConnectUrl(thirdAuth, setState(ThirdAuthType.WXWORK_QR));
            return "redirect:" + url;
        }
        if (StringUtils.equalsIgnoreCase(type, "dingtalk_qr")) {
            final String url = dingTalkService.buildQrConnectUrl(thirdAuth, setState(ThirdAuthType.DINGTALK_QR));
            return "redirect:" + url;
        }
        return "";
    }

    @AnonymousAccess
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model,
                        @RequestParam(required = false) String type,
                        @RequestParam(required = false) String sourceUrl) {
        String sourceType = type;
        if (StringUtils.isBlank(sourceType)) {
            final String userAgent = request.getHeader(Header.USER_AGENT.getValue());
            System.out.println("UserAgent: " + userAgent);
            if (StringUtils.containsIgnoreCase(userAgent, "dingtalk")) {
                sourceType = "dingtalk";
            } else if (StringUtils.containsIgnoreCase(userAgent, "wxwork")) {
                sourceType = "wxwork";
            }
        }
        if (StringUtils.equalsIgnoreCase(sourceType, "dingtalk")) {
            model.addAttribute("sourceUrl", sourceUrl);
            model.addAttribute("corpId", dingTalkService.getCorpId());
            model.addAttribute("baseUrl", baseUrl);
            model.addAttribute("state", setState(ThirdAuthType.DINGTALK));
            return "dingtalk/dingSSO";
        }

        if (StringUtils.equalsIgnoreCase(sourceType, "wxwork")) {
            final String url = wxCpService.getOauth2Service().buildAuthorizationUrl(thirdAuth, setState(ThirdAuthType.WXWORK));
            return "redirect:" + url;
        }

        return null;
    }

    private String setState(ThirdAuthType type) {
        final String key = UUID.randomUUID().toString().replace("-", "");
        stringRedisTemplate.opsForValue().set(STATE_KEY_PREFIX + key, type.name());
        return key;
    }

    private ThirdAuthType getState(String stateKey) {
        final String type = stringRedisTemplate.opsForValue().get(STATE_KEY_PREFIX + stateKey);
        return ThirdAuthType.valueOf(type);
    }

    @AnonymousGetMapping("/auth")
    public String auth(HttpServletRequest request) throws WxErrorException {
        LoginLogContextUtil.get().setUserAgent(UserAgentUtil.parse(request.getHeader(Header.USER_AGENT.getValue())))
                .setRequestIp(IpAddressUtil.getRequestIpAddress(request));
        OAuth2AccessToken oAuth2AccessToken = null;
        final String state = request.getParameter("state");
        final ThirdAuthType thirdAuthType = getState(state);
        System.out.println("STATE ENUM: " + thirdAuthType);
        switch (thirdAuthType) {
            case DINGTALK:
                LoginLogContextUtil.get().setLoginType("钉钉免登");
                oAuth2AccessToken = authenticationService.authByDingTalkCode(request.getParameter("code"));
                break;
            case DINGTALK_QR:
                LoginLogContextUtil.get().setLoginType("钉钉扫码登录");
                final String snsAuthCode = request.getParameter("authCode");
                oAuth2AccessToken = authenticationService.authByDingTalkAuthCode(snsAuthCode);
                break;
            case WXWORK:
                LoginLogContextUtil.get().setLoginTypeIfAbsent("企微免登");
            case WXWORK_QR:
                LoginLogContextUtil.get().setLoginTypeIfAbsent("企微扫码登录");
                final String code = request.getParameter("code");
                oAuth2AccessToken = authenticationService.authByWxWorkCode(code);
                break;
            case FEISHU:
            case FEISHU_QR:
                break;
        }
        if (oAuth2AccessToken != null) {
            String redirectUrl = baseUrl + "/#login-sso";
            return "redirect:" + redirectUrl + "?access_token=" + oAuth2AccessToken.getValue();
        }
        return null;
    }


    enum ThirdAuthType {
        DINGTALK,
        DINGTALK_QR,
        WXWORK,
        WXWORK_QR,
        FEISHU,
        FEISHU_QR
    }
}
