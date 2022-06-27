package net.bncloud.saas.authorize.service.query;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.constants.MenuType;
import net.bncloud.common.constants.SysType;

@Getter
@Setter
public class MenuQuery {

    private Long id;
    /**
     * 菜单标识
     */
    private String name;
    /**
     * 是否可见 true可见 false不可见
     */
    private Boolean hidden;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 菜单名称
     */
    private String title;
    /**
     * 菜单类型 1、菜单 2、按钮
     */
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
     * 是否参与权限分配
     */
    private Boolean needAuth;

    /**
     * 系统类型
     */
    private SysType sysType;

    private SubjectType subjectType;

}
