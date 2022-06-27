package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.PricingRecord;
import net.bncloud.quotation.vo.PricingRecordVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 定价请示记录信息
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class PricingRecordWrapper extends BaseEntityWrapper<PricingRecord,PricingRecordVo>  {

	public static PricingRecordWrapper build() {
		return new PricingRecordWrapper();
	}

	@Override
	public PricingRecordVo entityVO(PricingRecord entity) {
        PricingRecordVo entityVo = BeanUtil.copy(entity, PricingRecordVo.class);
		return entityVo;
	}



}
