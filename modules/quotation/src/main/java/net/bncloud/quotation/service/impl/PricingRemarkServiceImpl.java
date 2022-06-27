package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.PricingRemark;
import net.bncloud.quotation.mapper.PricingRemarkMapper;
import net.bncloud.quotation.param.PricingRemarkParam;
import net.bncloud.quotation.service.PricingRemarkService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定价说明信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
public class PricingRemarkServiceImpl extends BaseServiceImpl<PricingRemarkMapper, PricingRemark> implements PricingRemarkService {

		@Override
		public IPage<PricingRemark> selectPage(IPage<PricingRemark> page, QueryParam<PricingRemarkParam> pageParam) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		//notice.setTenantId(SecureUtil.getTenantId());
		return page.setRecords(baseMapper.selectListPage(page, pageParam));
		}
}
