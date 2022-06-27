package net.bncloud.quotation.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.quotation.entity.MaterialGroupInfo;
import net.bncloud.quotation.entity.MaterialInfo;
import net.bncloud.quotation.vo.MaterialGroupInfoVo;
import net.bncloud.quotation.vo.MaterialInfoVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 物料信息表
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class MaterialGroupInfoWrapper extends BaseEntityWrapper<MaterialGroupInfo, MaterialGroupInfoVo>  {

	public static MaterialGroupInfoWrapper build() {
		return new MaterialGroupInfoWrapper();
	}

	@Override
	public MaterialGroupInfoVo entityVO(MaterialGroupInfo entity) {
		MaterialGroupInfoVo entityVo = BeanUtil.copy(entity, MaterialGroupInfoVo.class);
		return entityVo;
	}



}
