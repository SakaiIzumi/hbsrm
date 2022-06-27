package net.bncloud.logging.spi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bncloud.common.security.Platform;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Principal {

    public static final Principal LOG_UNKNOWN = new Principal(null, "log:unknown", "log:unknown", null);

    private Long userId;
    private String name;
    private String login;
    private Platform platform;
}
