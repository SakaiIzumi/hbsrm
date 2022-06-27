package net.bncloud.order.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;

import java.io.Serializable;

/**
 *
 * 类名称:    ZcOrderEvent
 * 类描述:    智采取消挂起事件
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/22 2:39 下午
 */
public class ZcOrderStartEvent extends BizEvent {
	/**
	 * Create a new {@code ApplicationEvent}.
	 *
	 * @param source    the object on which the event initially occurred or with
	 *                  which the event is associated (never {@code null})
	 * @param loginInfo
	 * @param data
	 */
	public ZcOrderStartEvent(Object source, LoginInfo loginInfo, Serializable data) {
//		super(source, "zc:startOrder", loginInfo, data);
		super(source, EventCode.zc_startOrder.getCode(), loginInfo, data);
	}
	public ZcOrderStartEvent(Object source, LoginInfo loginInfo, Serializable data, String Sources, String SourcesName) {
		super(source,  EventCode.zc_startOrder.getCode(), loginInfo, data,Sources,SourcesName);
	}
}
