package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.QuotationAttRequire;
import net.bncloud.quotation.vo.QuotationAttRequireVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 附件需求清单
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class QuotationAttRequireWrapper extends BaseEntityWrapper<QuotationAttRequire,QuotationAttRequireVo>  {

	public static QuotationAttRequireWrapper build() {
		return new QuotationAttRequireWrapper();
	}

	@Override
	public QuotationAttRequireVo entityVO(QuotationAttRequire entity) {
        QuotationAttRequireVo entityVo = BeanUtil.copy(entity, QuotationAttRequireVo.class);
		return entityVo;
	}



}
