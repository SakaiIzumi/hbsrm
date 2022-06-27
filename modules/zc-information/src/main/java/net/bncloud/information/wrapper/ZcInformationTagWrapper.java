package net.bncloud.information.wrapper;


import net.bncloud.information.entity.ZcInformationTag;
import net.bncloud.information.vo.ZcInformationTagVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 智采消息标签
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2021-03-22
 */
public class ZcInformationTagWrapper extends BaseEntityWrapper<ZcInformationTag,ZcInformationTagVo>  {

	public static ZcInformationTagWrapper build() {
		return new ZcInformationTagWrapper();
	}

	@Override
	public ZcInformationTagVo entityVO(ZcInformationTag entity) {
        ZcInformationTagVo entityVo = BeanUtil.copy(entity, ZcInformationTagVo.class);
		return entityVo;
	}



}
