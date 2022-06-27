package net.bncloud.base;

import lombok.Data;
import net.bncloud.common.security.*;

import java.io.Serializable;
import java.util.Set;

/**
 * 类名称:    BaseUserEntity
 * 类描述:    当前用户实体类
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/15 2:51 下午
 */
@Data
public class BaseUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	private Long userId;
	
	/**
	 * 用户名称
	 */
	private String userName;
	
	private Platform platform;
	
	private Company currentCompany;
	
	private Org currentOrg;
	
	private Set<Role> roles;
	
	private Supplier currentSupplier;

	private Long orgId;

}
