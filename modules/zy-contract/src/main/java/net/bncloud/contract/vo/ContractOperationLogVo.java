package net.bncloud.contract.vo;


import lombok.Data;
import net.bncloud.contract.entity.ContractOperationLog;

import java.io.Serializable;

/**
 * <p>
 * 订单操作记录
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
@Data
public class ContractOperationLogVo extends ContractOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;



}
