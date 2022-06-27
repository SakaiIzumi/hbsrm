package net.bncloud.saas.user.strategy.switchmenunav;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ChangeWorkbenchStrategyContext {

    private final ChangeWorkbenchStrategyFactory changeWorkbenchStrategyFactory;

    public void changeWorkbench(String menuNavType) {
        String keyName = menuNavType + "ChangeWorkbenchStrategy";
        IChangeWorkbenchStrategy iChangeWorkbenchStrategy = changeWorkbenchStrategyFactory.get(keyName);
        if (iChangeWorkbenchStrategy != null) {
            iChangeWorkbenchStrategy.changeWorkbench();
        }
    }
}
