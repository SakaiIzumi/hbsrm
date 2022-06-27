package net.bncloud.common.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class BncUserDetails extends User {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Long id;
    private final String name;
    private final String mobile;

    public BncUserDetails(Long id, String name, String mobile, String password,
                          Collection<? extends GrantedAuthority> authorities) {
        super(mobile, password, authorities);
        this.id = id;
        this.name = name;
        this.mobile = mobile;
    }

    public BncUserDetails(Long id, String name, String mobile, String password,
                          boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                          boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(mobile, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.name = name;
        this.mobile = mobile;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }
}
