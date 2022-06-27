package net.bncloud.saas.authorize.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
public class MenuVo implements Serializable {
    private static final long serialVersionUID = -8387658902089605682L;
    private String name;

    private String path;

    private Boolean hidden;

    private String redirect;

    private String component;

    private String perm;

    private Boolean alwaysShow;

    private MenuMetaVo meta;

    private List<MenuVo> children;

}
