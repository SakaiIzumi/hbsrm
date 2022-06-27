package net.bncloud.financial.service.design.strategy;

/**
 * 策略
 *
 * @author Rao
 * @Date 2021/12/30
 **/
public interface Strategy<P, R> {

    /**
     * 做处理
     *
     * @param param
     * @return
     */
    R doHandle(P param);

    /**
     * 策略key
     *
     * @return
     */
    String strategyKey();

}
