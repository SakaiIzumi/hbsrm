package net.bncloud.saas.user.strategy.selector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SideTreeSelectorStrategyFactory {

    @Autowired
    private Map<String, ISelectorStrategy> sideTreeSelectorStrategyMap = new ConcurrentHashMap<>();

    public ISelectorStrategy get(String name) {
        return sideTreeSelectorStrategyMap.get(name);
    }

}
