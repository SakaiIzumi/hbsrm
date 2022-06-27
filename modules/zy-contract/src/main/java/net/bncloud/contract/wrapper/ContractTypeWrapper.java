package net.bncloud.contract.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.contract.entity.ContractType;
import net.bncloud.contract.vo.ContractTypeVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 合同类型信息表
 * </p>
 *包装类,返回视图层所需的字段
 * @author huangtao
 * @since 2021-03-12
 */
public class ContractTypeWrapper extends BaseEntityWrapper<ContractType,ContractTypeVo>  {

	public static ContractTypeWrapper build() {
		return new ContractTypeWrapper();
	}

	@Override
	public ContractTypeVo entityVO(ContractType entity) {
        ContractTypeVo entityVo = BeanUtil.copy(entity, ContractTypeVo.class);
		return entityVo;
	}



}
