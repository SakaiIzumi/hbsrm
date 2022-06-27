package net.bncloud.saas.authorize.service.query;

import lombok.Data;

@Data
public class DataDimensionQuery {

    private Long id;

    private String dimensionName;

    private String dimensionCode;

    private String keyType;
}
