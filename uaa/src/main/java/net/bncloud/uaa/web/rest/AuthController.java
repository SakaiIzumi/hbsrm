package net.bncloud.uaa.web.rest;

import cn.hutool.http.Header;
import cn.hutool.http.useragent.UserAgentUtil;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.util.IpAddressUtil;
import net.bncloud.security.annotation.AnonymousAccess;
import net.bncloud.uaa.security.AuthenticationService;
import net.bncloud.uaa.util.LoginLogContextUtil;
import net.bncloud.uaa.web.rest.payload.LoginForm;
import net.bncloud.uaa.web.rest.payload.MobilePasswordLogin;
import net.bncloud.uaa.web.rest.payload.MobileSmsCodeLogin;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @ApiOperation("通过手机号密码登录")
    @PostMapping("/mobile")
    public R<OAuth2AccessToken> authenticationByMobile(@RequestBody MobilePasswordLogin login, HttpServletRequest request) {
        LoginLogContextUtil.get().setUserAgent(UserAgentUtil.parse(request.getHeader(Header.USER_AGENT.getValue())))
                .setRequestIp(IpAddressUtil.getRequestIpAddress(request));
        OAuth2AccessToken token = authenticationService.mobilePasswordGrant(login.toPrincipal(), login.getPassword());
        return R.data(token);
    }

    @AnonymousAccess
    @ApiOperation("通过手机号/帐号密码登录")
    @PostMapping("/login")
    public R authenticationByLoginForm(@RequestBody LoginForm login, HttpServletRequest request) {
        LoginLogContextUtil.get().setUserAgent(UserAgentUtil.parse(request.getHeader(Header.USER_AGENT.getValue())))
                .setRequestIp(IpAddressUtil.getRequestIpAddress(request));
        OAuth2AccessToken token = authenticationService.mobilePasswordGrant(login.toPrincipal(), login.getPassword());
        return R.data(token);
    }

    @PostMapping("/mobileSms")
    public R<OAuth2AccessToken> authByMobileSmsCode(@RequestBody MobileSmsCodeLogin login, HttpServletRequest request) {
        LoginLogContextUtil.get().setUserAgent(UserAgentUtil.parse(request.getHeader(Header.USER_AGENT.getValue())))
                .setRequestIp(request.getRemoteAddr());
        OAuth2AccessToken token = authenticationService.authByMobileSmsCode(login.toPrincipal(), login.getSmsCode());
        return R.data(token);
    }

    /*@PostMapping("/ding-talk/internal")
    public R<OAuth2AccessToken> dingTalkInternalAuth(@RequestBody DingTalkCodeLogin login, HttpServletRequest request) {
        LoginLogContextUtil.get()
                .setUserAgent(UserAgentUtil.parse(request.getHeader(Header.USER_AGENT.getValue())))
                .setLogin(login.getCode()).setRequestIp(request.getRemoteAddr());
        OAuth2AccessToken token = authenticationService.authByDingTalk(login);
        return R.data(token);
    }*/
}
