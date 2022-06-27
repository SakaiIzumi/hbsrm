package net.bncloud.saas.user.strategy.userInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserInfoStrategyFactory {

    @Autowired
    private Map<String, IUserInfoStrategy> userInfoStrategyMaps = new ConcurrentHashMap<>();

    public IUserInfoStrategy get(String key) {
        return userInfoStrategyMaps.get(key);
    }

}
