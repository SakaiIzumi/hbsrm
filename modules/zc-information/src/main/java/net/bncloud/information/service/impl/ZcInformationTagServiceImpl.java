package net.bncloud.information.service.impl;

import net.bncloud.information.entity.ZcInformationTag;
import net.bncloud.information.mapper.ZcInformationTagMapper;
import net.bncloud.information.service.IZcInformationTagService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.information.vo.ZcInformationTagVo;
import net.bncloud.information.param.ZcInformationTagParam;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;

/**
 * 智采消息标签 服务实现类
 * @author dr
 */
@Service
public class ZcInformationTagServiceImpl extends BaseServiceImpl<ZcInformationTagMapper, ZcInformationTag> implements IZcInformationTagService {
	@Override
	public IPage<ZcInformationTagVo> selectPage(IPage<ZcInformationTagVo> page, QueryParam<ZcInformationTagParam> queryParam) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		// notice.setTenantId(SecureUtil.getTenantId());
		return page.setRecords(baseMapper.selectListPage(page, queryParam));
	}

	@Override
	public ZcInformationTagVo findByTag(String tag) {
		return null;
	}
}