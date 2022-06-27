package net.bncloud.saas.user.domain.vo;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class UserInfoVO {
    private Long id;
    private String code;
    private String name;
}
