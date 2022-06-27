package net.bncloud.contract.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.contract.entity.Contract;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;

/**
 * @ClassName ContractSendEvent
 * @Description: 合同发送事件
 * @Author Administrator
 * @Date 2021/3/22
 * @Version V1.0
 **/
public class ContractSendEvent extends BizEvent<Contract> {

//    private static final String eventCode = "contract:send";
    private static final String eventCode = EventCode.contract_send.getCode();

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param loginInfo
     * @param data
     */
    public ContractSendEvent(Object source, LoginInfo loginInfo, Contract data) {
        super(source, eventCode, loginInfo, data);
    }
    public ContractSendEvent(Object source, LoginInfo loginInfo, Contract data, String SourcesCode, String SourcesName) {
        super(source, eventCode, loginInfo, data,SourcesCode,SourcesName);
    }
}
