package net.bncloud.saas.supplier.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserStaffVO implements Serializable {

    private static final long serialVersionUID = 4211844635919753284L;

    private Long id;

    private Long supplierId;

    private String position;

    private String jobNo;

    private Long orgId;

    private List<Long> roleIds;

    private Boolean enabled;
}
