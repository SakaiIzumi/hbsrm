package net.bncloud.saas.user.service.dto;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.authorize.service.dto.MenuDTO;
import net.bncloud.saas.authorize.service.dto.RoleSmallDTO;
import net.bncloud.saas.tenant.service.dto.UserDetailRefDTO;
import net.bncloud.saas.user.domain.vo.SupplierVO;
import net.bncloud.saas.user.domain.vo.UserOrgVO;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class CurrentUserInfoDTO {
    private String name;
    private String nickname;
    private String mobile;
    private String email;
    /**
     * 微信号
     */
    private String weChatCode;

    /**
     * QQ号
     */
    private String qqCode;

    private String gender;
    private String avatar;
    /** 上次登录的IP **/
    private String lastLoginIp;
    /** 上次登录的时间 **/
    private Instant lastLoginTime;

    private List<RoleSmallDTO> roles;

    private List<MenuDTO> menus;

    private UserOrgVO org;
    private List<UserOrgVO> orgList;

    private SupplierVO supplier;
    private List<SupplierVO> supplierList;

    List<UserDetailRefDTO> userDetailRefs;
}
