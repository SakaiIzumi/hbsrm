package net.bncloud.quotation.wrapper;


import net.bncloud.quotation.entity.QuotationSupplier;
import net.bncloud.quotation.vo.QuotationSupplierVo;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 询价供应商信息
 * </p>
 *包装类,返回视图层所需的字段
 * @author Auto-generator
 * @since 2022-02-18
 */
public class QuotationSupplierWrapper extends BaseEntityWrapper<QuotationSupplier,QuotationSupplierVo>  {

    public static QuotationSupplierWrapper build() {
        return new QuotationSupplierWrapper();
    }

    @Override
    public QuotationSupplierVo entityVO(QuotationSupplier entity) {
        QuotationSupplierVo entityVo = BeanUtil.copy(entity, QuotationSupplierVo.class);
        return entityVo;
    }



}
