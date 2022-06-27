package net.bncloud.saas.user.strategy.switchsub;

import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@AllArgsConstructor
public class SwitchCurrentSubjectContext {

    private final SwitchCurrentSubjectFactory switchCurrentSubjectFactory;

    public SwitchCurrentUserInfo switchCurrentSubject(String strategyName, Long subId) {
        String name = strategyName + "SwitchCurrentSubjectStrategy";
        ISwitchCurrentSubjectStrategy iSwitchCurrentSubjectStrategy = switchCurrentSubjectFactory.get(name);
        return iSwitchCurrentSubjectStrategy.switchCurrentSubject(subId);
    }
}
