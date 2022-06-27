package net.bncloud.saas.authorize.domain;

import lombok.*;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.constants.MenuNavType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ss_sys_menu_nav")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuNav implements Serializable {

    @Id
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private MenuNavType menuNavType;

    private String type;

    private String subjectType;

    @Column(name = "need_auth")
    private Boolean needAuth;

    @Column(name = "order_num")
    private Integer orderNum;
}
