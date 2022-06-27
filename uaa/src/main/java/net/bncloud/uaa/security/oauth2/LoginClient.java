package net.bncloud.uaa.security.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginClient {
    private String clientId;
    private String clientSecret;
    private String grantType;

    public static LoginClient of(String clientId, String clientSecret, String grantType) {
        return new LoginClient(clientId, clientSecret, grantType);
    }

    public static LoginClient defaultClient() {
        return of("web", "123456", "password");
    }

    public static LoginClient dingTalkClient() {
        return of("ding-talk", "123456", "password");
    }

    public static LoginClient wxwork() {
        return of("wxwork", "123456", "password");
    }
}
