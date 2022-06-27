package net.bncloud.contract.event;

import net.bncloud.common.security.LoginInfo;
import net.bncloud.contract.entity.Contract;
import net.bncloud.event.BizEvent;
import net.bncloud.enums.EventCode;

/**
 * @ClassName ContractConfirmEvent
 * @Description: 合同确认事件
 * @Author Administrator
 * @Date 2021/3/24
 * @Version V1.0
 **/
public class ContractConfirmEvent extends BizEvent<Contract> {

//    private static final String eventCode = "contract:confirm";
private static final String eventCode = EventCode.contract_confirm.getCode();
    /**
     *
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param loginInfo 当前用户登录信息
     * @param data      业务数据
     */
    public ContractConfirmEvent(Object source, LoginInfo loginInfo, Contract data) {
        super(source, eventCode, loginInfo, data);
    }
    public ContractConfirmEvent(Object source, LoginInfo loginInfo, Contract data, String Sources, String SourcesName) {
        super(source, eventCode, loginInfo, data,Sources,SourcesName);
    }
}
