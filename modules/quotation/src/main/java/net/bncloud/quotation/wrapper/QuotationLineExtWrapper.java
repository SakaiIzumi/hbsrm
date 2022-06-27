package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.QuotationLineExt;
import net.bncloud.quotation.vo.QuotationLineExtVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 询价行动态行扩展信息
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class QuotationLineExtWrapper extends BaseEntityWrapper<QuotationLineExt,QuotationLineExtVo>  {

	public static QuotationLineExtWrapper build() {
		return new QuotationLineExtWrapper();
	}

	@Override
	public QuotationLineExtVo entityVO(QuotationLineExt entity) {
        QuotationLineExtVo entityVo = BeanUtil.copy(entity, QuotationLineExtVo.class);
		return entityVo;
	}



}
