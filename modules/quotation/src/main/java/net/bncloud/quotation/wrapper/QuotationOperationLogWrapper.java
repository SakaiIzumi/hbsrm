package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.QuotationOperationLog;
import net.bncloud.quotation.vo.QuotationOperationLogVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 询价单操作记录日志表
 * </p>
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2022-03-02
 */
public class QuotationOperationLogWrapper extends BaseEntityWrapper<QuotationOperationLog, QuotationOperationLogVo> {

    public static QuotationOperationLogWrapper build() {
        return new QuotationOperationLogWrapper();
    }

    @Override
    public QuotationOperationLogVo entityVO(QuotationOperationLog entity) {
        QuotationOperationLogVo entityVo = BeanUtil.copy(entity, QuotationOperationLogVo.class);
        return entityVo;
    }


}
