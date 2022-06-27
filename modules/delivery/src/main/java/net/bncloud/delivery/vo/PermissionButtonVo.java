package net.bncloud.delivery.vo;

import lombok.Data;
import net.bncloud.common.api.R;
import net.bncloud.delivery.param.DeliveryNoteSaveParam;

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
	 * 申请发货
	 */
	private Boolean applyDelivery;

	/**
	 * 作废申请
	 */
	private Boolean invalidApply;

	/**
	 * 撤回申请
	 */
	private Boolean withdrawApply;

	/**
	 * 确认发出
	 */
	private Boolean confirmationIssued;

	/**
	 * 撤回送货
	 */
	private Boolean withdrawDelivery;

	/**
	 * 货物已发
	 */
	private Boolean delivered;

	/**
	 * 作废送货
	 */
	private Boolean invalidDelivery;

	/**
	 * 提醒
	 */
	private Boolean remind;


	/**
	 * 同意发货
	 */
	private Boolean agreeDelivery;

	/**
	 * 退回申请
	 */
	private Boolean returnApply;

	/**
	 * 退回送货
	 */
	private Boolean returnDelivery;

	/**
	 * 货物送达
	 *
	 */
	private Boolean goodDelivered;

	/**
	 * 确认签收
	 */
	private Boolean confirmReceipt;

	/**
	 * 撤回签收
	 */
	private Boolean withdrawReceipt;

	/**
	 * 打印
	 */
	private Boolean print;

	/**
	 * 删除
	 */
	private Boolean delete;

	/**
	 * 保存
	 */
	private Boolean save;

	/**
	 * 取消
	 */
	private Boolean cancel;


}
