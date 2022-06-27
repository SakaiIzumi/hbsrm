package net.bncloud.contract.param;


import net.bncloud.contract.entity.ContractType;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;


/**
 * <p>
 * 合同类型信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
@Data
public class ContractTypeParam extends ContractType implements Serializable {

    private static final long serialVersionUID = 1L;



}
