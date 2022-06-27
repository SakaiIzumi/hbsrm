package net.bncloud.quotation.mapper;

import net.bncloud.quotation.entity.MaterialQuotationTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.quotation.vo.MaterialQuotationTemplateVo;
import net.bncloud.quotation.param.MaterialQuotationTemplateParam;
import net.bncloud.common.base.domain.QueryParam;
import java.util.List;
/**
 * <p>
 * 物料报价模板 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface MaterialQuotationTemplateMapper extends BaseMapper<MaterialQuotationTemplate> {
		/**
		 * <p>
		 * 自定义分页
		 * </p>
		 *
		 * @author Auto-generator
		 * @since 2022-02-14
		 */
		List<MaterialQuotationTemplate> selectListPage(IPage page, QueryParam<MaterialQuotationTemplateParam> pageParam);
}
