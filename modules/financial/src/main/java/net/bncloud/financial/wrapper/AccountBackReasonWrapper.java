package net.bncloud.financial.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialBackReason;
import net.bncloud.financial.vo.FinancialBackReasonVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 对账单退回原因信息表
 * </p>
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public class AccountBackReasonWrapper extends BaseEntityWrapper<FinancialBackReason, FinancialBackReasonVo> {

    public static AccountBackReasonWrapper build() {
        return new AccountBackReasonWrapper();
    }

    @Override
    public FinancialBackReasonVo entityVO(FinancialBackReason entity) {
        FinancialBackReasonVo entityVo = BeanUtil.copy(entity, FinancialBackReasonVo.class);
        return entityVo;
    }


}
