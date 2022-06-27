package net.bncloud.saas.authorize.service.dto;


import lombok.Data;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.convert.base.BaseDTO;

import javax.persistence.Column;

@Data
public class DimensionPermDTO extends BaseDTO {

    private static final long serialVersionUID = -6781933071755294902L;

    private String dimensionType;

    private String permName;

    private String permValue;

    private String name;

    private String code;
}
