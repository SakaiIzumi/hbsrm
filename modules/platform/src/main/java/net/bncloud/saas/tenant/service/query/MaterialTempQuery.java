package net.bncloud.saas.tenant.service.query;

import lombok.Data;
import net.bncloud.api.feign.saas.user.SubjectType;

import java.io.Serializable;

@Data
public class MaterialTempQuery implements Serializable {
    private String searchValue;
    private Long subjectId; //主体id
    private SubjectType subjectType;
}
