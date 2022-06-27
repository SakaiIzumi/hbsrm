package net.bncloud.financial.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialDeliveryBillLine;
import net.bncloud.financial.vo.FinancialDeliveryBillLineVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * 送货单抬头表
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public class AccountDeliveryBillLineWrapper extends BaseEntityWrapper<FinancialDeliveryBillLine, FinancialDeliveryBillLineVo> {

    public static AccountDeliveryBillLineWrapper build() {
        return new AccountDeliveryBillLineWrapper();
    }

    @Override
    public FinancialDeliveryBillLineVo entityVO(FinancialDeliveryBillLine entity) {
        FinancialDeliveryBillLineVo entityVo = BeanUtil.copy(entity, FinancialDeliveryBillLineVo.class);
        return entityVo;
    }


}
