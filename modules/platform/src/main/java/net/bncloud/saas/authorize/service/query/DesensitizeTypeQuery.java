package net.bncloud.saas.authorize.service.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class DesensitizeTypeQuery implements Serializable {
    private static final long serialVersionUID = -1733317719913119446L;

    private String qs;

    private String dimensionType;

    private String name;

    private String value;
}
