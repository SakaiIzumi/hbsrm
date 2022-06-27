package net.bncloud.contract.service;

import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.contract.entity.ContractHistoryRel;
import net.bncloud.contract.param.ContractHistoryRelParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
		Page<ContractHistoryRel> selectPage(Pageable page, QueryParam<ContractHistoryRelParam> queryParam);


}
