package net.bncloud.saas.user.strategy.switchmenunav;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChangeWorkbenchStrategyFactory {
    @Autowired
    private Map<String, IChangeWorkbenchStrategy> changeWorkbenchStrategyMap = new ConcurrentHashMap();

    public IChangeWorkbenchStrategy get(String key) {
        return changeWorkbenchStrategyMap.get(key);
    }
}
