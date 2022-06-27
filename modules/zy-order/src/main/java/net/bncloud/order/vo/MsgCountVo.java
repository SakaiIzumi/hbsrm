package net.bncloud.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * 类名称:    MsgCountVo
 * 类描述:    待办消息数
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/16 2:54 下午
 */
@Data
public class MsgCountVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 差异等处理数
	 */
	Integer differenceMsgCount;
	
	/**
	 * 待答交
	 */
	Integer waitingForAnswersCount;
	
	/**
	 * 未确认变更
	 */
	Integer unconfirmedChangeCount;
	
	/**
	 * 待确认变更
	 */
	Integer waitForUnconfirmedChangeCount;
	
	/**
	 * 确认变更 ··
	 */
	Integer confirmChangeCount;
	
	/**
	 * 退回
	 */
	Integer returnCount;
	
	/**
	 * 未完成订单
	 */
	Integer notFinishedCount;
	
	/**
	 * 暂停执行
	 */
	Integer stopCount;
	
	/**
	 * 草稿 ··
	 */
	Integer draftCount;
	
	/**
	 * 确认 ··
	 */
	Integer confirmCount;
	
	/**
	 * 完成 ··
	 */
	Integer completeCount;
	
	/**
	 * 变更 ··
	 */
	Integer changeCount;
	
	
	
}
