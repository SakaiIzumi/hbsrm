package net.bncloud.saas.authorize.service.query;

import lombok.Data;
import net.bncloud.api.feign.saas.user.SubjectType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
public class DimensionPermQuery implements Serializable {

    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    private String dimensionType;

    private String permName;

    private String permValue;
}
