package net.bncloud.saas.tenant.service.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class OrganizationRecordDTO {

    private Long id;

    private Long orgId;

    private String orgName;
}
