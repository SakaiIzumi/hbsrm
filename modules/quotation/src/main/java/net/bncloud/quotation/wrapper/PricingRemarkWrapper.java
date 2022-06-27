package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.PricingRemark;
import net.bncloud.quotation.vo.PricingRemarkVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 定价说明信息
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class PricingRemarkWrapper extends BaseEntityWrapper<PricingRemark,PricingRemarkVo>  {

	public static PricingRemarkWrapper build() {
		return new PricingRemarkWrapper();
	}

	@Override
	public PricingRemarkVo entityVO(PricingRemark entity) {
        PricingRemarkVo entityVo = BeanUtil.copy(entity, PricingRemarkVo.class);
		return entityVo;
	}



}
