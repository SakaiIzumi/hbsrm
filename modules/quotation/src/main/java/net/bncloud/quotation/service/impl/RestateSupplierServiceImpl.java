package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.RestateSupplier;
import net.bncloud.quotation.mapper.RestateSupplierMapper;
import net.bncloud.quotation.param.RestateSupplierParam;
import net.bncloud.quotation.service.IRestateSupplierService;
import net.bncloud.quotation.vo.RestateSupplierVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 询价重报供应商邀请信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RestateSupplierServiceImpl extends BaseServiceImpl<RestateSupplierMapper, RestateSupplier> implements IRestateSupplierService {

		@Override
		public IPage<RestateSupplierVo> selectPage(IPage<RestateSupplierVo> page, QueryParam<RestateSupplierParam> pageParam) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		//notice.setTenantId(SecureUtil.getTenantId());
		return page.setRecords(baseMapper.selectListPage(page, pageParam));
		}
}
