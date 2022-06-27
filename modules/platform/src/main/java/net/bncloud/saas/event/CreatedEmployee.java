package net.bncloud.saas.event;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CreatedEmployee implements Serializable {

    private Long id;
    private String name;
    private Long userId;
    private Long orgId;
    private String orgName;
    private Long deptId;
    private String deptName;
    private List<Long> roleIds;
}
