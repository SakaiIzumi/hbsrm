package net.bncloud.saas.user.strategy.menunav;

import net.bncloud.common.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MenuNavStrategyFactory {

    @Autowired
    private Map<String, IMenuNavStrategy> strategies = new ConcurrentHashMap<>();

    public IMenuNavStrategy getStrategy(String name) {
        IMenuNavStrategy strategy = strategies.get(name);
        if (null == strategy) {
            throw new ApiException(500, "无法找到合适的策略类");
        }
        return strategy;
    }


}

