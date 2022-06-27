package net.bncloud.saas.user.strategy.menunav;

import net.bncloud.saas.authorize.domain.MenuNav;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class MenuNavStrategyContext {

    private final MenuNavStrategyFactory menuNavStrategyFactory;

    public MenuNavStrategyContext(MenuNavStrategyFactory menuNavStrategyFactory) {
        this.menuNavStrategyFactory = menuNavStrategyFactory;
    }

    private final List<String> strategyNames = new ArrayList<String>() {{
        add("orgMenuNavStrategy");
        add("supplierMenuNavStrategy");
        add("platformMenuNavStrategy");
    }};


    public List<MenuNav> getMeuNavs(Long userId) {
        List<MenuNav> meuNavs = new ArrayList<>();
        List<String> strategyNames = this.strategyNames;
        for (String key : strategyNames) {
            IMenuNavStrategy strategy = menuNavStrategyFactory.getStrategy(key);
            meuNavs.addAll(strategy.getMeuNavs(userId));
        }
        meuNavs.sort(Comparator.comparing(MenuNav::getOrderNum));
        return meuNavs;
    }
}
