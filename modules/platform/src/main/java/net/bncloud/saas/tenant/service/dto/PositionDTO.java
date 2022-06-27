package net.bncloud.saas.tenant.service.dto;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.tenant.domain.Position;

@Getter
@Setter
public class PositionDTO {
    private Long id;
    private String name;
    private Boolean disabled;
    private Integer order;

    public Position create() {
        Position p = new Position();
        p.setId(id);
        p.setName(name);
        p.setDisabled(disabled);
        p.setOrder(order);
        return p;
    }
}
