package net.bncloud.common.security;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class MenuNavDto implements Serializable {
    private Long id;

    private String title;

    private String menuNavType;

    private Integer orderNum;
}
