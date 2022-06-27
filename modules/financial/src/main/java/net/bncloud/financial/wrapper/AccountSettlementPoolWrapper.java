package net.bncloud.financial.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialSettlementPool;
import net.bncloud.financial.vo.FinancialSettlementPoolVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 结算池单据信息表
 * </p>
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public class AccountSettlementPoolWrapper extends BaseEntityWrapper<FinancialSettlementPool, FinancialSettlementPoolVo> {

    public static AccountSettlementPoolWrapper build() {
        return new AccountSettlementPoolWrapper();
    }

    @Override
    public FinancialSettlementPoolVo entityVO(FinancialSettlementPool entity) {
        FinancialSettlementPoolVo entityVo = BeanUtil.copy(entity, FinancialSettlementPoolVo.class);
        return entityVo;
    }


}
