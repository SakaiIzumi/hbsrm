package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.MaterialFormExt;
import net.bncloud.quotation.vo.MaterialFormExtVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 物料表单扩展信息
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class MaterialFormExtWrapper extends BaseEntityWrapper<MaterialFormExt,MaterialFormExtVo>  {

	public static MaterialFormExtWrapper build() {
		return new MaterialFormExtWrapper();
	}

	@Override
	public MaterialFormExtVo entityVO(MaterialFormExt entity) {
        MaterialFormExtVo entityVo = BeanUtil.copy(entity, MaterialFormExtVo.class);
		return entityVo;
	}



}
