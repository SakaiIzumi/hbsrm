package net.bncloud.saas.tenant.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.saas.tenant.domain.OrganizationRecord;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrgManagerRecordDTO {
    private Long id;

    private String code;

    private String name;

    private Long managerId;

    private String managerType;

    private String mobile;

    private Boolean enabled;

    private List<String> orgNames;

    @JsonIgnore
    private List<OrganizationRecord> organizationRecords;

}
