package net.bncloud.contract.service;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.contract.entity.ContractHistoryRel;
import net.bncloud.contract.param.ContractHistoryRelParam;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 合同与历史合同关联关系表 服务类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-22
 */
public interface ContractHistoryRelService extends BaseService<ContractHistoryRel> {

		/**
         * 自定义分页
         * @param page
         * @param queryParam
         * @return
         */
		IPage<ContractHistoryRel> selectPage(IPage<ContractHistoryRel> page, QueryParam<ContractHistoryRelParam> queryParam);


}
