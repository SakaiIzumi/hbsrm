package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.QuotationMark;
import net.bncloud.quotation.vo.QuotationMarkVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 询价单应标关联表
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-03-01
 */
public class QuotationMarkWrapper extends BaseEntityWrapper<QuotationMark,QuotationMarkVo>  {

	public static QuotationMarkWrapper build() {
		return new QuotationMarkWrapper();
	}

	@Override
	public QuotationMarkVo entityVO(QuotationMark entity) {
        QuotationMarkVo entityVo = BeanUtil.copy(entity, QuotationMarkVo.class);
		return entityVo;
	}



}
