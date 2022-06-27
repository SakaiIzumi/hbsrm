package net.bncloud.event.service.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventTypeQuery {
    private Long id;
    private String code;
    private String name;
    private String module;
    private String scene;
    private String description;
}
