package net.bncloud.contract.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.contract.entity.ContractHistoryRel;
import net.bncloud.contract.mapper.ContractHistoryRelMapper;
import net.bncloud.contract.param.ContractHistoryRelParam;
import net.bncloud.contract.service.ContractHistoryRelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
		public Page<ContractHistoryRel> selectPage(Pageable pageable, QueryParam<ContractHistoryRelParam> queryParam) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		//notice.setTenantId(SecureUtil.getTenantId());
			final IPage<ContractHistoryRel> page = PageUtils.toPage(pageable);
			page.setRecords(baseMapper.selectListPage(page, queryParam));
			return PageUtils.result(page);
		}
}
