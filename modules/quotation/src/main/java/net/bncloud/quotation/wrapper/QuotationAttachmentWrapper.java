package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.QuotationAttachment;
import net.bncloud.quotation.vo.QuotationAttachmentVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 附件信息
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-14
 */
public class QuotationAttachmentWrapper extends BaseEntityWrapper<QuotationAttachment,QuotationAttachmentVo>  {

	public static QuotationAttachmentWrapper build() {
		return new QuotationAttachmentWrapper();
	}

	@Override
	public QuotationAttachmentVo entityVO(QuotationAttachment entity) {
        QuotationAttachmentVo entityVo = BeanUtil.copy(entity, QuotationAttachmentVo.class);
		return entityVo;
	}



}
