package net.bncloud.saas.tenant.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeptVO {
    private Long id;
    private String name;

    public static DeptVO of(Long id, String name) {
        return new DeptVO(id, name);
    }
}
