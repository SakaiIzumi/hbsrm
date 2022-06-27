package net.bncloud.common.domain;

/**
 * 排序，作为优先级时数字越大优先级越低
 */
public interface Ordered {

    int HIGHEST_PRECEDENCE = 1;

    /**
     *
     */
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    void setOrder(Integer order);

    Integer getOrder();
}
