package net.bncloud.saas.user.strategy.switchsub;

//切换主体策略
public interface ISwitchCurrentSubjectStrategy {

    SwitchCurrentUserInfo switchCurrentSubject(Long subId);
}
