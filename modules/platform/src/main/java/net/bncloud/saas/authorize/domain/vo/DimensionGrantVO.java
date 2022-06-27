package net.bncloud.saas.authorize.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DimensionGrantVO implements Serializable {
    private static final long serialVersionUID = 6296549221515351047L;

    private String name;

    private String code;
}
