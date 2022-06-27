package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.QuotationLineBase;
import net.bncloud.quotation.vo.QuotationLineBaseVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 询价行基础信息
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class QuotationLineBaseWrapper extends BaseEntityWrapper<QuotationLineBase,QuotationLineBaseVo>  {

	public static QuotationLineBaseWrapper build() {
		return new QuotationLineBaseWrapper();
	}

	@Override
	public QuotationLineBaseVo entityVO(QuotationLineBase entity) {
        QuotationLineBaseVo entityVo = BeanUtil.copy(entity, QuotationLineBaseVo.class);
		return entityVo;
	}



}
