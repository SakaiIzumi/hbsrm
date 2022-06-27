package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.TRfqQuotationRecord;
import net.bncloud.quotation.vo.TRfqQuotationRecordVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 报价记录信息
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-25
 */
public class TRfqQuotationRecordWrapper extends BaseEntityWrapper<TRfqQuotationRecord,TRfqQuotationRecordVo>  {

	public static TRfqQuotationRecordWrapper build() {
		return new TRfqQuotationRecordWrapper();
	}

	@Override
	public TRfqQuotationRecordVo entityVO(TRfqQuotationRecord entity) {
        TRfqQuotationRecordVo entityVo = BeanUtil.copy(entity, TRfqQuotationRecordVo.class);
		return entityVo;
	}



}
