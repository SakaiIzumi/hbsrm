package net.bncloud.logging.service.query;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class LoginLogQuery {

    private String name;
    private String mobile;
    private String browserName;
    private String osName;
    private Boolean success;
    private Boolean mobileClient;

    private Instant start;
    private Instant end;
}
