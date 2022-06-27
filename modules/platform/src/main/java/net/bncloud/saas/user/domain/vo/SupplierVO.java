package net.bncloud.saas.user.domain.vo;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class SupplierVO {
    private Long id;
    private String code;
    private String name;

    public static SupplierVO of(Long id, String code, String name) {
        return new SupplierVO(id, code, name);
    }
}
