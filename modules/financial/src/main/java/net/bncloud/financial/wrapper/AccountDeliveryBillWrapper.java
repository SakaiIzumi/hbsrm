package net.bncloud.financial.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialDeliveryBill;
import net.bncloud.financial.vo.FinancialDeliveryBillVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * 送货单抬头表
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public class AccountDeliveryBillWrapper extends BaseEntityWrapper<FinancialDeliveryBill, FinancialDeliveryBillVo> {

    public static AccountDeliveryBillWrapper build() {
        return new AccountDeliveryBillWrapper();
    }

    @Override
    public FinancialDeliveryBillVo entityVO(FinancialDeliveryBill entity) {
        FinancialDeliveryBillVo entityVo = BeanUtil.copy(entity, FinancialDeliveryBillVo.class);
        return entityVo;
    }


}
