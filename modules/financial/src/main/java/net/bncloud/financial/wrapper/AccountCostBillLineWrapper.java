package net.bncloud.financial.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialCostBillLine;
import net.bncloud.financial.vo.FinancialCostBillLineVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 费用明细信息表
 * </p>
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public class AccountCostBillLineWrapper extends BaseEntityWrapper<FinancialCostBillLine, FinancialCostBillLineVo> {

    public static AccountCostBillLineWrapper build() {
        return new AccountCostBillLineWrapper();
    }

    @Override
    public FinancialCostBillLineVo entityVO(FinancialCostBillLine entity) {
        FinancialCostBillLineVo entityVo = BeanUtil.copy(entity, FinancialCostBillLineVo.class);
        return entityVo;
    }


}
