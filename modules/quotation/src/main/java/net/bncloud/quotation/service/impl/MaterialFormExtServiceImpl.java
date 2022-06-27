package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.bncloud.quotation.entity.MaterialFormExt;
import net.bncloud.quotation.mapper.MaterialFormExtMapper;
import net.bncloud.quotation.service.MaterialFormExtService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.quotation.vo.MaterialFormExtVo;
import net.bncloud.quotation.param.MaterialFormExtParam;
import net.bncloud.support.Condition;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;

/**
 * <p>
 * 物料表单扩展信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
public class MaterialFormExtServiceImpl extends BaseServiceImpl<MaterialFormExtMapper, MaterialFormExt> implements MaterialFormExtService {

		@Override
		public IPage<MaterialFormExt> selectPage(IPage<MaterialFormExt> page, QueryParam<MaterialFormExtParam> pageParam) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		//notice.setTenantId(SecureUtil.getTenantId());
		return page.setRecords(baseMapper.selectListPage(page, pageParam));
		}

	@Override
	public void deleteByFromId(Long id) {
		LambdaQueryWrapper<MaterialFormExt> queryWrapper = Condition.getQueryWrapper(new MaterialFormExt()).lambda().eq(MaterialFormExt::getMaterialFormId, id);
		baseMapper.delete(queryWrapper);
	}
}
