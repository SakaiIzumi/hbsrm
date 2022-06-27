package net.bncloud.uaa.security;

import com.alibaba.fastjson.JSONObject;
import net.bncloud.common.exception.BizException;
import net.bncloud.logging.LogConstants;
import net.bncloud.security.oauth2.dingtalk.DingTalkQrAuthentication;
import net.bncloud.security.oauth2.mobile.MobilePasswordAuthentication;
import net.bncloud.security.oauth2.wx.work.WxWorkAuthentication;
import net.bncloud.uaa.security.oauth2.LoginClient;
import net.bncloud.uaa.security.oauth2.mobile.MobilePrincipal;
import net.bncloud.uaa.util.LoginLogContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final ClientDetailsService clientDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthorizationServerTokenServices authorizationServerTokenServices;
    private final StringRedisTemplate stringRedisTemplate;

    public AuthenticationService(ClientDetailsService clientDetailsService,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 AuthorizationServerTokenServices authorizationServerTokenServices,
                                 StringRedisTemplate stringRedisTemplate) {
        this.clientDetailsService = clientDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.authorizationServerTokenServices = authorizationServerTokenServices;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public OAuth2AccessToken mobilePasswordGrant(MobilePrincipal principal, String password) {
        try {
            MobilePasswordAuthentication token = new MobilePasswordAuthentication(principal, password);
            return getOAuth2AccessToken(LoginClient.defaultClient(), token);
        } catch (Exception e) {
            throw new BizException(AuthResultCode.LOGIN_ERROR);
        }
    }

    //spring-security-oauth2提供，传入对应k-v获取spring-security-oauth2提供的客户端类（按其标准）
    private ClientDetails getClient(String clientId, String clientSecret) {
        //查询自己的业务库结合spring-security-oauth2提供的类得到客户端类
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("clientId对应的信息不存在");
        } else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配");
        }
        return clientDetails;
    }

    //通过spring-security的构建类+自己定义的凭证=构建oauth2-token（返回给调用方使用）
    private OAuth2AccessToken getOAuth2AccessToken(LoginClient loginClient, AbstractAuthenticationToken token) {
        if (loginClient == null) {
            loginClient = LoginClient.defaultClient();
        }
        LoginLogContextUtil.get().setLoginAt(Instant.now());

        OAuth2AccessToken oAuth2AccessToken = null;
        try {
            //查询自己的业务库结合spring-security-oauth2提供的类得到客户端类
            ClientDetails clientDetails = getClient(loginClient.getClientId(), loginClient.getClientSecret());
            //设置这个TokenRequest的requestParameters映射到所提供的一个不可修改的版本（获取spring-security提供的token中转类，以备对接spring-security-oauth2）
            //@param请求分隔符
            //@param客户端Id
            //@paran范围
            //@param授权类型
            TokenRequest tokenRequest = new TokenRequest(Collections.emptyMap(), loginClient.getClientId(), clientDetails.getScope(), loginClient.getGrantType());
            //创建对应的spring-security-oauth2-request
            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
            //尝试验证传递的token身份验证，返回一个完全填充 身份验证 对象(包括已授予的权限)
            //如果成功 AuthenticationManager 必须遵守以下关于*例外情况的合同:
            //DisabledException 必须抛出
            //如果帐户被禁用 AuthenticationManager 可以测试此状态
            //LockedException 必须抛出
            //如果帐户被锁定 AuthenticationManager 可以测试帐户锁定
            //我如果出现错误的凭证，必须抛出elink BadCredentialsException 虽然上面的例外是可选的，一个 codeAuthenticationManager 必须 测试凭据
            //应按表示的顺序测试异常，如果适用则抛出异常*以上(即，如果一个帐户被禁用或锁定，认证请求是*立即被拒绝，不执行证书测试过程)
            //这*防止凭证被测试针对禁用或锁定帐户
            //authentication认证请求对象
            //完全经过身份验证的对象，包括凭据
            //////白话文：拿之前spring-security生成的token做验证，验证通过后存入redis，验证不通过报错回滚
            Authentication authentication = authenticationManager.authenticate(token);//（对应本地当前线程）
            //获取当前上下文，存入（更替）凭证
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //构造OAuth 2身份验证
            //由于某些grant类型不需要用户身份验证，user身份验证可能为空
            //@param storedRequest授权请求(不能为空)
            //用户身份验证(可能为nul1)
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
            //创建与指定凭据相关联的访问令牌
            //@param authentication与访问令牌关联的凭据,返回访问令牌。
            //如果凭证不充分，则@throws AuthenticationException
            oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
            //开启验证
            oAuth2Authentication.setAuthenticated(true);

            LoginLogContextUtil.get().setSuccess(true).setToken(oAuth2AccessToken.getValue());

        } catch (AuthenticationException e) {
            e.printStackTrace();
            LoginLogContextUtil.get().setSuccess(false).setFailReason(e.getMessage());
            throw e;
        } finally {
            sendLoginLog(JSONObject.toJSONString(LoginLogContextUtil.get()));
            LoginLogContextUtil.clear();
        }
        return oAuth2AccessToken;
    }

    private void sendLoginLog(String log) {
        LOGGER.info("发送登录日志: {}", log);
        if (stringRedisTemplate == null) {
            return;
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                stringRedisTemplate.convertAndSend(LogConstants.REDIS_CHANNEL_LOGIN_LOG, log);
            }
        });
    }

    public OAuth2AccessToken authByMobileSmsCode(MobilePrincipal principal, String smsCode) {
        try {
            MobilePasswordAuthentication token = new MobilePasswordAuthentication(principal, smsCode);
            return getOAuth2AccessToken(LoginClient.defaultClient(), token);
        } catch (Exception e) {
            throw new BizException(AuthResultCode.LOGIN_ERROR);
        }
    }

    public OAuth2AccessToken authByWxWorkCode(String code) {
        return getOAuth2AccessToken(LoginClient.wxwork(), new WxWorkAuthentication(code, code));
    }

    public OAuth2AccessToken authByDingTalkAuthCode(String snsAuthCode) {
        return getOAuth2AccessToken(LoginClient.dingTalkClient(), new DingTalkQrAuthentication(snsAuthCode, snsAuthCode));
    }

    public OAuth2AccessToken authByDingTalkCode(String code) {
        return getOAuth2AccessToken(LoginClient.dingTalkClient(), new net.bncloud.security.oauth2.dingtalk.DingTalkAuthentication(code, code));
    }
}
