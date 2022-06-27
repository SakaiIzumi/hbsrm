package net.bncloud.contract.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.contract.entity.Contract;
import net.bncloud.enums.EventCode;
import net.bncloud.event.BizEvent;

/**
 * @ClassName ContractInvalidEvent
 * @Description: 合同作废事件
 * @Author Administrator
 * @Date 2021/3/22
 * @Version V1.0
 **/
public class ContractInvalidEvent extends BizEvent<Contract> {

//    private static final String eventCode = "contract:invalid";
    private static final String eventCode = EventCode.contract_invalid.getCode();
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param loginInfo
     * @param data
     */
    public ContractInvalidEvent(Object source, LoginInfo loginInfo, Contract data) {
        super(source, eventCode, loginInfo, data);
    }
    public ContractInvalidEvent(Object source, LoginInfo loginInfo, Contract data, String Sources, String SourcesName) {
        super(source, eventCode, loginInfo, data,Sources,SourcesName);
    }
}
