package net.bncloud.contract.vo;


import net.bncloud.contract.entity.ContractHistoryRel;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * <p>
 * 合同与历史合同关联关系表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-22
 */
@Data
public class ContractHistoryRelVo extends ContractHistoryRel implements Serializable {

    private static final long serialVersionUID = 1L;



}
