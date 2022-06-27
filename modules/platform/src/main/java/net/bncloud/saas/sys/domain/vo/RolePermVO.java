package net.bncloud.saas.sys.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RolePermVO implements Serializable {

    private Long id;

    private String name;

    private List<Long> menuIds;

    private List<Long> eventPermIds;

    private Boolean enabled;

}
