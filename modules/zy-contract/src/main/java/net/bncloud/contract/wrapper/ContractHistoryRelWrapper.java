package net.bncloud.contract.wrapper;


import net.bncloud.contract.entity.ContractHistoryRel;
import net.bncloud.contract.vo.ContractHistoryRelVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 合同与历史合同关联关系表
 * </p>
 *包装类,返回视图层所需的字段
 * @author huangtao
 * @since 2021-03-22
 */
public class ContractHistoryRelWrapper extends BaseEntityWrapper<ContractHistoryRel,ContractHistoryRelVo>  {

	public static ContractHistoryRelWrapper build() {
		return new ContractHistoryRelWrapper();
	}

	@Override
	public ContractHistoryRelVo entityVO(ContractHistoryRel entity) {
        ContractHistoryRelVo entityVo = BeanUtil.copy(entity, ContractHistoryRelVo.class);
		return entityVo;
	}



}
