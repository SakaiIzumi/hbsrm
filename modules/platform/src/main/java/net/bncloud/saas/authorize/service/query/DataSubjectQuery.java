package net.bncloud.saas.authorize.service.query;

import lombok.Data;

@Data
public class DataSubjectQuery {
    private Long id;

    private Long appConfigId;

    private String name;

    private String keyValue;

    private String keyType;

}
