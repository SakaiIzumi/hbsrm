package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.QuotationBase;
import net.bncloud.quotation.vo.QuotationBaseVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 询价基础信息
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class QuotationBaseWrapper extends BaseEntityWrapper<QuotationBase,QuotationBaseVo>  {

	public static QuotationBaseWrapper build() {
		return new QuotationBaseWrapper();
	}

	@Override
	public QuotationBaseVo entityVO(QuotationBase entity) {
        QuotationBaseVo entityVo = BeanUtil.copy(entity, QuotationBaseVo.class);
		return entityVo;
	}



}
