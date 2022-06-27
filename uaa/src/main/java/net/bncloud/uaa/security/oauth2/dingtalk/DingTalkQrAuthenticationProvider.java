package net.bncloud.uaa.security.oauth2.dingtalk;

import net.bncloud.security.oauth2.dingtalk.DingTalkQrAuthentication;
import net.bncloud.uaa.security.oauth2.AbstractAuthenticationProvider;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public class DingTalkQrAuthenticationProvider extends AbstractAuthenticationProvider {

    public DingTalkQrAuthenticationProvider() {
    }

    // ~ Methods
    // ========================================================================================================

    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  Authentication authentication)
            throws AuthenticationException {

    }




    @Override
    protected final UserDetails retrieveUser(Object principal, Authentication authentication)
            throws AuthenticationException {
        try {
            UserDetails loadedUser = this.getLoginUserDetailsService().getUserByDingTalkQrAuthCode((String) principal);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        }
        catch (UsernameNotFoundException ex) {
            throw ex;
        }
        catch (InternalAuthenticationServiceException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }


    @Override
    protected Authentication createSuccessAuthentication(Object principal,
                                                         Authentication authentication, UserDetails user) {

        return super.createSuccessAuthentication(principal, authentication, user);
    }

    @Override
    protected AbstractAuthenticationToken doCreateSuccessAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new DingTalkQrAuthentication(principal, credentials, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return DingTalkQrAuthentication.class.isAssignableFrom(authentication);
    }
}
