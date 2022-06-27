package net.bncloud.financial.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialPayMethod;
import net.bncloud.financial.vo.FinancialPayMethodVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 支付方式信息表
 * </p>
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public class AccountPayMethodWrapper extends BaseEntityWrapper<FinancialPayMethod, FinancialPayMethodVo> {

    public static AccountPayMethodWrapper build() {
        return new AccountPayMethodWrapper();
    }

    @Override
    public FinancialPayMethodVo entityVO(FinancialPayMethod entity) {
        FinancialPayMethodVo entityVo = BeanUtil.copy(entity, FinancialPayMethodVo.class);
        return entityVo;
    }


}
