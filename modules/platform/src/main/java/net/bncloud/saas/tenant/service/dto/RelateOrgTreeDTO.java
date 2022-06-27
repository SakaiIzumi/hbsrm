package net.bncloud.saas.tenant.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelateOrgTreeDTO {
    public Long id;

    private String name;

    private String type;

    public Long parentId;

    public List<RelateOrgTreeDTO> children;
}
