package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.MaterialQuotationTemplate;
import net.bncloud.quotation.vo.MaterialQuotationTemplateVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 物料报价模板
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class MaterialQuotationTemplateWrapper extends BaseEntityWrapper<MaterialQuotationTemplate,MaterialQuotationTemplateVo>  {

	public static MaterialQuotationTemplateWrapper build() {
		return new MaterialQuotationTemplateWrapper();
	}

	@Override
	public MaterialQuotationTemplateVo entityVO(MaterialQuotationTemplate entity) {
        MaterialQuotationTemplateVo entityVo = BeanUtil.copy(entity, MaterialQuotationTemplateVo.class);
		return entityVo;
	}



}
