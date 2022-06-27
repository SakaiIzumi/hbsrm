package net.bncloud.uaa.service;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import me.chanjar.weixin.cp.bean.WxCpUser;
import net.bncloud.api.feign.saas.user.Mobile;
import net.bncloud.service.api.platform.user.dto.AccountStatus;
import net.bncloud.service.api.platform.user.dto.Password;
import net.bncloud.service.api.platform.user.dto.UserInfoDTO;
import net.bncloud.common.api.R;
import net.bncloud.common.security.BncUserDetails;
import net.bncloud.service.api.platform.user.feign.LoginUserServiceFeignClient;
import net.bncloud.uaa.security.oauth2.mobile.MobilePrincipal;
import net.bncloud.uaa.util.LoginLogContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class LoginUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginUserDetailsService.class);

    private final LoginUserServiceFeignClient loginUserServiceFeignClient;
    private final WxCpService wxCpService;
    private final DingTalkService dingTalkService;

    public LoginUserDetailsService(LoginUserServiceFeignClient loginUserServiceFeignClient, WxCpService wxCpService, DingTalkService dingTalkService) {
        this.loginUserServiceFeignClient = loginUserServiceFeignClient;
        this.wxCpService = wxCpService;
        this.dingTalkService = dingTalkService;
    }

    public BncUserDetails getUserByMobile(MobilePrincipal principal) {
        LoginLogContextUtil.get().setLogin(principal.getMobile());

        R<UserInfoDTO> r = loginUserServiceFeignClient.getUserByMobile(principal.getMobile());
        return buildUserDetails(r);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails getUserByDingTalkQrAuthCode(String code) {
        try {
            final String mobile = dingTalkService.getUserInfoByQrCode(code);
            final R<UserInfoDTO> r = loginUserServiceFeignClient.getUserByMobile(mobile);
            return buildUserDetails(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserDetails getUserByWxWorkAuthCode(String code) {
        try {
            final WxCpOauth2UserInfo userInfo = wxCpService.getOauth2Service().getUserInfo(code);
            final WxCpUser wxCpUser = wxCpService.getUserService().getById(userInfo.getUserId());
            LoginLogContextUtil.get().setLogin("openId:" + userInfo.getOpenId() + ", userId:" + userInfo.getUserId())
                    .setMobile(wxCpUser.getMobile());
            final R<UserInfoDTO> r = loginUserServiceFeignClient.getUserByMobile(wxCpUser.getMobile());
            return buildUserDetails(r);
        } catch (WxErrorException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BncUserDetails buildUserDetails(R<UserInfoDTO> r) {
        if (r.isSuccess()) {
            UserInfoDTO userInfo = r.getData();

            AccountStatus accountStatus = userInfo.getAccountStatus();
            if (accountStatus == null) {
                accountStatus = AccountStatus.of(false, false, false);
            }
            Password password = userInfo.getPassword();

            LOGGER.info("userInfo: {}", userInfo);
            //远程调用插入userInfo
            Mobile mobile = new Mobile();
            mobile.setMobile(userInfo.getMobile());
            loginUserServiceFeignClient.cacheLoginInfo(mobile);
            return new BncUserDetails(userInfo.getId(), userInfo.getName(), userInfo.getMobile(),
                    password.getPassword(),
                    !accountStatus.isDisabled(),
                    !accountStatus.isAccountExpired(),
                    true,
                    !accountStatus.isAccountLocked(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        }
        LOGGER.error("获取用户信息失败: " + r.getMsg());
        return null;
    }

    public UserDetails getUserByDingTalkCode(String code) {
        try {
            String mobile = dingTalkService.getUserMobileByCode(code);
            LoginLogContextUtil.get().setLogin(mobile)
                    .setMobile(mobile);
            final R<UserInfoDTO> r = loginUserServiceFeignClient.getUserByMobile(mobile);
            return buildUserDetails(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
