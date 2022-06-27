package net.bncloud.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * 类名称:    PermissionButton
 * 类描述:    权限按钮类
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/18 2:59 下午
 */
@Data
public class PermissionButtonVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 发送
	 */
	private Boolean send;

	/**
	 * 确认变更
	 */
	private Boolean confirmChange;

	/**
	 * 确认
	 */
	private Boolean confirm;


}
