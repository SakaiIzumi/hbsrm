package net.bncloud.financial.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialCostBill;
import net.bncloud.financial.vo.FinancialCostBillVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 费用单据信息表
 * </p>
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public class AccountCostBillWrapper extends BaseEntityWrapper<FinancialCostBill, FinancialCostBillVo> {

    public static AccountCostBillWrapper build() {
        return new AccountCostBillWrapper();
    }

    @Override
    public FinancialCostBillVo entityVO(FinancialCostBill entity) {
        FinancialCostBillVo entityVo = BeanUtil.copy(entity, FinancialCostBillVo.class);
        return entityVo;
    }


}
