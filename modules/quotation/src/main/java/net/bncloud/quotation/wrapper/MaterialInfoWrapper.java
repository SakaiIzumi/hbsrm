package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.MaterialInfo;
import net.bncloud.quotation.vo.MaterialInfoVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 物料信息表
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class MaterialInfoWrapper extends BaseEntityWrapper<MaterialInfo,MaterialInfoVo>  {

	public static MaterialInfoWrapper build() {
		return new MaterialInfoWrapper();
	}

	@Override
	public MaterialInfoVo entityVO(MaterialInfo entity) {
        MaterialInfoVo entityVo = BeanUtil.copy(entity, MaterialInfoVo.class);
		return entityVo;
	}



}
