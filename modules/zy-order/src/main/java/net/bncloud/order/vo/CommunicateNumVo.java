package net.bncloud.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * 类名称:    CommunicateNumVo
 * 类描述:    TODO
 * 创建人:    lvxiangyi
 * 创建时间:  2021/5/14 6:27 下午
 */
@Data
public class CommunicateNumVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 第几次答交
	 */
	private Integer count;
	
	/**
	 * 产品ID
	 */
	private Long productDetailsId;
	
	/**
	 * 当前排序
	 */
	private Integer thisCount;
	
	
	
}
