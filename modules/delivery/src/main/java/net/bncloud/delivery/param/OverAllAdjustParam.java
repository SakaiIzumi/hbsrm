package net.bncloud.delivery.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class OverAllAdjustParam implements Serializable {
    /**
     * 全局工作日开始时间
     * */
    private Integer start;
    /**
     * 全局工作日结束时间
     * */
    private Integer end;
}
