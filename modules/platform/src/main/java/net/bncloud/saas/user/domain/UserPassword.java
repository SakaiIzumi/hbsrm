package net.bncloud.saas.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bncloud.common.util.ApplicationContextProvider;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.time.Instant;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPassword {

    private String password;

    /** 上次修改密码的时间 **/
    private Instant lastPasswordChangeTime;

    /*** 是否已重置密码 **/
    private Boolean resetPassword;

    private Instant nextResetPasswordTime;

    public static UserPassword empty() {
        return new UserPassword(null, null, false, Instant.now());
    }

    public static UserPassword random() {
        final String randomPass = RandomStringUtils.randomAlphanumeric(10);
        return new UserPassword(noopEncode(randomPass), Instant.now(), false, Instant.now());
    }

    public static UserPassword init(@NonNull String password) {
        PasswordEncoder passwordEncoder = ApplicationContextProvider.getBean(PasswordEncoder.class);
        if (passwordEncoder == null) {
            throw new IllegalStateException("系统配置错误，PasswordEncoder不能为空");
        }
        return new UserPassword(passwordEncoder.encode(password), Instant.now(), false, nextResetPasswordTime());
    }
    public static UserPassword plain(@NonNull String password) {
        return new UserPassword(noopEncode(password), Instant.now(), false, nextResetPasswordTime());
    }

    private static String noopEncode(String raw) {
        return "{noop}" + raw;
    }

    private static Instant nextResetPasswordTime() {
        return Instant.now().plusSeconds(3600 * 24 * 30);
    }


    public boolean needRestPassword() {
        return StringUtils.isBlank(password) || nextResetPasswordTime.isBefore(Instant.now());
    }

    @Transient
    public boolean isCredentialsExpired() {
        return StringUtils.isNoneBlank(password) && nextResetPasswordTime.isBefore(Instant.now());
    }
}
