package net.bncloud.saas.authorize.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.constants.MenuNavType;
import net.bncloud.common.constants.MenuType;
import net.bncloud.common.constants.SysType;
import net.bncloud.common.domain.AbstractTreeEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ss_sys_menu")
@Getter
@Setter
public class Menu extends AbstractTreeEntity<Menu> {
    private static final long serialVersionUID = -1594056036856254889L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 菜单标识
     */
    private String name;
    /**
     * 是否可见 true可见 false不可见
     */
    private boolean hidden;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 菜单名称
     */
    private String title;
    /**
     * 菜单类型
     */
    @Enumerated(EnumType.STRING)
    private MenuType menuType;
    /**
     * 页面路径(路由地址)
     */
    private String path;
    /**
     * 重定向路由标识
     */
    private String redirect;
    /**
     * 路由标识（页面组件）
     */
    private String component;
    /**
     * 路由地址
     */
    private String route;

    /**
     * 权限标识
     */
    private String perm;
    /**
     * 是否参与权限分配
     */
    private boolean needAuth;

    //    /**
//     * 系统类型
//     */
    @Enumerated(value = EnumType.STRING)
    private SysType sysType;

    @ManyToMany(mappedBy = "menus", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Role> roles;

    @Enumerated(EnumType.STRING)
    private MenuNavType menuNavType;

    /**
     * 主体类型
     */
    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    private Boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
