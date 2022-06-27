package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.MaterialTemplateExt;
import net.bncloud.quotation.vo.MaterialTemplateExtVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 物料报价模板扩展信息（物料、公式信息）
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class MaterialTemplateExtWrapper extends BaseEntityWrapper<MaterialTemplateExt,MaterialTemplateExtVo>  {

	public static MaterialTemplateExtWrapper build() {
		return new MaterialTemplateExtWrapper();
	}

	@Override
	public MaterialTemplateExtVo entityVO(MaterialTemplateExt entity) {
        MaterialTemplateExtVo entityVo = BeanUtil.copy(entity, MaterialTemplateExtVo.class);
		return entityVo;
	}



}
