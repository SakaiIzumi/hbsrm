package net.bncloud.saas.user.strategy.switchsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SwitchCurrentSubjectFactory {

    @Autowired
    private Map<String, ISwitchCurrentSubjectStrategy> strategies = new ConcurrentHashMap<>();

    public ISwitchCurrentSubjectStrategy get(String name) {
        return strategies.get(name);
    }
}
