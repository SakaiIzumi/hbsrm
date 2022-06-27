package net.bncloud.order.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;

import java.io.Serializable;

/**
 *
 * 类名称:    ZcOrderEvent
 * 类描述:    智采挂起事件
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/22 2:39 下午
 */
public class ZcOrderStopEvent extends BizEvent {
	/**
	 * Create a new {@code ApplicationEvent}.
	 *
	 * @param source    the object on which the event initially occurred or with
	 *                  which the event is associated (never {@code null})
	 * @param loginInfo
	 * @param data
	 */
	public ZcOrderStopEvent(Object source, LoginInfo loginInfo, Serializable data) {
//		super(source, "zc:stopOrder", loginInfo, data);
		super(source, EventCode.zc_stopOrder.getCode(), loginInfo, data);
	}
	public ZcOrderStopEvent(Object source, LoginInfo loginInfo, Serializable data, String Sources, String SourcesName) {
		super(source, EventCode.zc_stopOrder.getCode(), loginInfo, data,Sources,SourcesName);
	}
}
