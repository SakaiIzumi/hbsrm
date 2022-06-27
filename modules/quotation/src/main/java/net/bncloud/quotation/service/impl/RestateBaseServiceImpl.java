package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.RestateBase;
import net.bncloud.quotation.mapper.RestateBaseMapper;
import net.bncloud.quotation.param.RestateBaseParam;
import net.bncloud.quotation.service.IRestateBaseService;
import net.bncloud.quotation.vo.RestateBaseVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 询价重报基础信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RestateBaseServiceImpl extends BaseServiceImpl<RestateBaseMapper, RestateBase> implements IRestateBaseService {

		@Override
		public IPage<RestateBaseVo> selectPage(IPage<RestateBaseVo> page, QueryParam<RestateBaseParam> pageParam) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		//notice.setTenantId(SecureUtil.getTenantId());
		return page.setRecords(baseMapper.selectListPage(page, pageParam));
		}
}
