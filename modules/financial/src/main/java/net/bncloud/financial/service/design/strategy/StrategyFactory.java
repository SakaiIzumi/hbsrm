package net.bncloud.financial.service.design.strategy;

import net.bncloud.common.exception.Asserts;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Rao
 * @Date 2021/12/30
 **/
public class StrategyFactory<P, R> {

    private final Map<String, Strategy<P, R>> keyStrategyMap;

    public StrategyFactory(List<Strategy<P, R>> strategyList) {
        this.keyStrategyMap = strategyList.stream().collect(Collectors.toMap(Strategy::strategyKey, Function.identity()));
    }

    /**
     * 策略获取
     *
     * @param strategyKey
     * @return
     */
    public Strategy<P, R> getStrategy(String strategyKey) {
        Strategy<P, R> prStrategy = this.keyStrategyMap.get(strategyKey);
        Asserts.notNull(prStrategy, "执行业务失败，策略未实现！");
        return prStrategy;
    }

    /**
     * 策略opt获取
     *
     * @param strategyKey
     * @return
     */
    public Optional<Strategy<P, R>> getStrategyOpt(String strategyKey) {
        Strategy<P, R> prStrategy = this.keyStrategyMap.get(strategyKey);
        return Optional.ofNullable(prStrategy);
    }

}
