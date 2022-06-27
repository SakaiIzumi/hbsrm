package net.bncloud.financial.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialCostDetail;
import net.bncloud.financial.vo.FinancialCostDetailVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 费用信息表
 * </p>
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public class AccountCostDetailWrapper extends BaseEntityWrapper<FinancialCostDetail, FinancialCostDetailVo> {

    public static AccountCostDetailWrapper build() {
        return new AccountCostDetailWrapper();
    }

    @Override
    public FinancialCostDetailVo entityVO(FinancialCostDetail entity) {
        FinancialCostDetailVo entityVo = BeanUtil.copy(entity, FinancialCostDetailVo.class);
        return entityVo;
    }


}
