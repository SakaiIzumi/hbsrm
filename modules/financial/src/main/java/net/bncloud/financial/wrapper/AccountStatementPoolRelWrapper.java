package net.bncloud.financial.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialStatementPoolRel;
import net.bncloud.financial.vo.FinancialStatementPoolRelVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 对账单与结算单池单据关联关系表
 * </p>
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public class AccountStatementPoolRelWrapper extends BaseEntityWrapper<FinancialStatementPoolRel, FinancialStatementPoolRelVo> {

    public static AccountStatementPoolRelWrapper build() {
        return new AccountStatementPoolRelWrapper();
    }

    @Override
    public FinancialStatementPoolRelVo entityVO(FinancialStatementPoolRel entity) {
        FinancialStatementPoolRelVo entityVo = BeanUtil.copy(entity, FinancialStatementPoolRelVo.class);
        return entityVo;
    }


}
