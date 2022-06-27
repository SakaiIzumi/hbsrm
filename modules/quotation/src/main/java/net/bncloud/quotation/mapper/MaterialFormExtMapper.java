package net.bncloud.quotation.mapper;

import net.bncloud.quotation.entity.MaterialFormExt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.quotation.param.MaterialFormExtParam;
import net.bncloud.common.base.domain.QueryParam;
import java.util.List;
/**
 * <p>
 * 物料表单扩展信息 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface MaterialFormExtMapper extends BaseMapper<MaterialFormExt> {
		/**
		 * <p>
		 * 自定义分页
		 * </p>
		 *
		 * @author Auto-generator
		 * @since 2022-02-14
		 */
		List<MaterialFormExt> selectListPage(IPage page, QueryParam<MaterialFormExtParam> pageParam);
}
