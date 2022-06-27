package net.bncloud.financial.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * desc: 对账单退回对象
 *
 * @author Rao
 * @Date 2022/04/01
 **/
@Data
public class StatementPurchaseRejectVo implements Serializable {
    private static final long serialVersionUID = 695753168425498918L;

    /**
     * 退回原因
     */
    private String remark;

}
