package net.bncloud.contract.service.impl;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.contract.entity.ContractHistoryRel;
import net.bncloud.contract.mapper.ContractHistoryRelMapper;
import net.bncloud.contract.service.ContractHistoryRelService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.contract.param.ContractHistoryRelParam;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;

/**
 * <p>
 * 合同与历史合同关联关系表 服务实现类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-22
 */
@Service
public class ContractHistoryRelServiceImpl extends BaseServiceImpl<ContractHistoryRelMapper, ContractHistoryRel> implements ContractHistoryRelService {

		@Override
		public IPage<ContractHistoryRel> selectPage(IPage<ContractHistoryRel> page, QueryParam<ContractHistoryRelParam> queryParam) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		//notice.setTenantId(SecureUtil.getTenantId());
		return page.setRecords(baseMapper.selectListPage(page, queryParam));
		}
}
