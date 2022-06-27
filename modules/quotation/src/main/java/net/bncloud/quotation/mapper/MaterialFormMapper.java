package net.bncloud.quotation.mapper;

import net.bncloud.quotation.entity.MaterialForm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;


import net.bncloud.quotation.vo.MaterialFormVo;
import net.bncloud.quotation.param.MaterialFormParam;
import net.bncloud.common.base.domain.QueryParam;
import java.util.List;
/**
 * <p>
 * 物料表单信息表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface MaterialFormMapper extends BaseMapper<MaterialForm> {
		/**
		 * <p>
		 * 自定义分页
		 * </p>
		 *
		 * @author Auto-generator
		 * @since 2022-02-14
		 */
		List<MaterialForm> selectListPage(IPage page, QueryParam<MaterialFormParam> pageParam);
}
