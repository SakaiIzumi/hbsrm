package net.bncloud.contract.wrapper;


import net.bncloud.contract.entity.Contract;
import net.bncloud.contract.vo.ContractVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 合同信息表
 * </p>
 *包装类,返回视图层所需的字段
 * @author huangtao
 * @since 2021-03-12
 */
public class ContractWrapper extends BaseEntityWrapper<Contract,ContractVo>  {

	public static ContractWrapper build() {
		return new ContractWrapper();
	}

	@Override
	public ContractVo entityVO(Contract entity) {
        ContractVo entityVo = BeanUtil.copy(entity, ContractVo.class);
		return entityVo;
	}



}
