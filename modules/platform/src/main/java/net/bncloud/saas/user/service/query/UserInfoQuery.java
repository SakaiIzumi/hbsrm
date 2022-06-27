package net.bncloud.saas.user.service.query;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * 类名称:    UserInfoQuery
 * 类描述:    TODO
 * 创建人:    lvxiangyi
 * 创建时间:  2021/4/27 7:12 下午
 */
@Getter
@Setter
public class UserInfoQuery {
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 手机
	 */
	private String mobile;
	
	/**
	 * 部门ID
	 */
	private String deptId;
	
	
}
