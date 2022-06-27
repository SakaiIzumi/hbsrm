package net.bncloud.financial.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: liulu
 * @Date: 2022-03-10 18:01
 */
@Getter
@Setter
public class ReconciliationTimeConfig {
    private Long id;
    private String item;
    private String startDate;
    private String endDate;
    private boolean financialSwitch;
}
