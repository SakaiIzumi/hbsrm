package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.QuotationAttRequireAttachment;
import net.bncloud.quotation.vo.QuotationAttRequireAttachmentVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 附件需求上传文件表
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-03-04
 */
public class QuotationAttRequireAttachmentWrapper extends BaseEntityWrapper<QuotationAttRequireAttachment,QuotationAttRequireAttachmentVo>  {

	public static QuotationAttRequireAttachmentWrapper build() {
		return new QuotationAttRequireAttachmentWrapper();
	}

	@Override
	public QuotationAttRequireAttachmentVo entityVO(QuotationAttRequireAttachment entity) {
        QuotationAttRequireAttachmentVo entityVo = BeanUtil.copy(entity, QuotationAttRequireAttachmentVo.class);
		return entityVo;
	}



}
