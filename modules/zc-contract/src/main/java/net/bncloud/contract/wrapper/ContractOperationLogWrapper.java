package net.bncloud.contract.wrapper;


import net.bncloud.contract.entity.ContractOperationLog;
import net.bncloud.contract.vo.ContractOperationLogVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 订单操作记录
 * </p>
 *包装类,返回视图层所需的字段
 * @author huangtao
 * @since 2021-03-12
 */
public class ContractOperationLogWrapper extends BaseEntityWrapper<ContractOperationLog,ContractOperationLogVo>  {

	public static ContractOperationLogWrapper build() {
		return new ContractOperationLogWrapper();
	}

	@Override
	public ContractOperationLogVo entityVO(ContractOperationLog entity) {
        ContractOperationLogVo entityVo = BeanUtil.copy(entity, ContractOperationLogVo.class);
		return entityVo;
	}



}
