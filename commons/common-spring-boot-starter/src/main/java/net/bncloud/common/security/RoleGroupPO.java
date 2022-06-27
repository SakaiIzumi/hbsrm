package net.bncloud.common.security;

import java.io.Serializable;

/**
 *
 * 类名称:    RoleGroupPO
 * 类描述:    TODO
 * 创建人:    lvxiangyi
 * 创建时间:  2021/4/26 10:28 上午
 */
public class RoleGroupPO implements Serializable {
	private static final long serialVersionUID = 8424517537922621024L;
	
	private Long id;
	
	private String name;
	
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
