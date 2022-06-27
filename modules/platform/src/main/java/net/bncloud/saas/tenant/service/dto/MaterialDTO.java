package net.bncloud.saas.tenant.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 产品说的物料
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialDTO {
    private String name;
    private String code;
    private Long subjectId;
    private String subjectType;
}
