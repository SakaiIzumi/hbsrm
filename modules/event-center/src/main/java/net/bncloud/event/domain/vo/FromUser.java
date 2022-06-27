package net.bncloud.event.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FromUser {
    private Long id;
    private String name;
    private Long orgId;
//    public static FromUser of(Long id, String name) {
//        return new FromUser(id, name);
//    }

    public static FromUser of(Long id, String name,Long orgId) {
        return new FromUser(id, name,orgId);
    }
}
