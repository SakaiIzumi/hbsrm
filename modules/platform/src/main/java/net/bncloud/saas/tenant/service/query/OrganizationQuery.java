package net.bncloud.saas.tenant.service.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrganizationQuery implements Serializable {

    private String qs;

    private Long id;

    private String name;

    private String purCode;

    private String supCode;

    private String scene;
}
