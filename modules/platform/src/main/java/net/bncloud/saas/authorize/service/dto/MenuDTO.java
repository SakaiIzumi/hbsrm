package net.bncloud.saas.authorize.service.dto;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.constants.MenuType;
import net.bncloud.common.constants.SysType;
import net.bncloud.convert.base.BaseDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Getter
@Setter
public class MenuDTO extends BaseDTO {
    private static final long serialVersionUID = -9002372364241788380L;
    private Long id;

    private Long parentId;
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
     * 路由地址
     */
    private String route;

    private String perm;
    
    
    /** 系统类型 */
    @Enumerated(value = EnumType.STRING)
    private SysType sysType;
    
    /**
     * 是否参与权限分配
     */
    private boolean needAuth;
    private List<MenuDTO> children;
}
