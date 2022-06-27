package net.bncloud.financial.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialStatement;
import net.bncloud.financial.vo.FinancialStatementVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 对账单信息表
 * </p>
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public class AccountStatementWrapper extends BaseEntityWrapper<FinancialStatement, FinancialStatementVo> {

    public static AccountStatementWrapper build() {
        return new AccountStatementWrapper();
    }

    @Override
    public FinancialStatementVo entityVO(FinancialStatement entity) {
        FinancialStatementVo entityVo = BeanUtil.copy(entity, FinancialStatementVo.class);
        return entityVo;
    }


}
