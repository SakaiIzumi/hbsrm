package net.bncloud.uaa.security.oauth2.mobile;

import net.bncloud.security.oauth2.mobile.MobileSmsCodeAuthentication;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class MobileSmsCodeAuthenticationProvider extends MobilePasswordAuthenticationProvider {


    @Override
    public boolean supports(Class<?> authentication) {
        return MobileSmsCodeAuthentication.class.isAssignableFrom(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, Authentication authentication) throws AuthenticationException {
        // TODO 校验验证码
        MobileSmsCodeAuthentication token = (MobileSmsCodeAuthentication) authentication;
        String username = userDetails.getUsername();
    }

    @Override
    protected AbstractAuthenticationToken doCreateSuccessAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new MobileSmsCodeAuthentication(principal, credentials, authorities);
    }

}
