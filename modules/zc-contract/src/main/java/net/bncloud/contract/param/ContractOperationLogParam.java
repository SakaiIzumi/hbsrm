package net.bncloud.contract.param;


import lombok.Builder;
import net.bncloud.contract.entity.ContractOperationLog;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
public class ContractOperationLogParam extends ContractOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;



}
