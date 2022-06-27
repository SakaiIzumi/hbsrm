package net.bncloud.saas.user.service.dto;

import java.io.Serializable;

public class UserInfoDTO implements Serializable {
    private static final long serialVersionUID = 1206965315276100984L;

    private BaseUserInfoDTO userInfo = new BaseUserInfoDTO();
    private PermissionInfoDTO permissionInfo = new PermissionInfoDTO();




    public BaseUserInfoDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(BaseUserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }

    public PermissionInfoDTO getPermissionInfo() {
        return permissionInfo;
    }

    public void setPermissionInfo(PermissionInfoDTO permissionInfo) {
        this.permissionInfo = permissionInfo;
    }
}
