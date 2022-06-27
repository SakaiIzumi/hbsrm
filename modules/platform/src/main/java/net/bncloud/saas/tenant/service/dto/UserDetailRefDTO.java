package net.bncloud.saas.tenant.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailRefDTO {

    private Long id;

    private String name;

    private Long subjectId;

    private String subjectType;

    private String jobNo;

    private String position;

    private String orgName;

    private List<String> roleName;

    private boolean enabled;

}
