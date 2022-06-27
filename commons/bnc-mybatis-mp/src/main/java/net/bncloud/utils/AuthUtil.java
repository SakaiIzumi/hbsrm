package net.bncloud.utils;

import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.security.BncUserDetails;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;

import java.util.Optional;

/**
 * 类名称:    AuthUtil
 * 类描述:    获取当前用户类
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/15 2:56 下午
 */
public class AuthUtil {
	/**
	 * 获取当前用户对象
	 */
	public static BaseUserEntity getUser() {
		BaseUserEntity baseUserEntity = new BaseUserEntity();
		Optional<LoginInfo> loginInfo = SecurityUtils.getLoginInfo();
		if (loginInfo.isPresent()) {
			LoginInfo user = loginInfo.get();
			baseUserEntity.setUserId(user.getId());
			baseUserEntity.setUserName(user.getName());
			baseUserEntity.setCurrentCompany(user.getCurrentCompany());
			baseUserEntity.setCurrentOrg(user.getCurrentOrg());
			baseUserEntity.setPlatform(user.getPlatform());
			baseUserEntity.setRoles(user.getRoles());
			baseUserEntity.setCurrentSupplier(user.getCurrentSupplier());
		}
		return baseUserEntity;
	}
}
