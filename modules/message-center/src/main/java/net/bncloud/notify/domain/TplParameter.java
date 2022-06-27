package net.bncloud.notify.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TplParameter {
    private Long id;
    private String paramKey;
    private String description;
    private String defaultValue;
}
