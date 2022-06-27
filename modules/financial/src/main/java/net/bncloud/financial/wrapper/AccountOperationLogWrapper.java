package net.bncloud.financial.wrapper;


import net.bncloud.common.util.BeanUtil;
import net.bncloud.financial.entity.FinancialOperationLog;
import net.bncloud.financial.vo.FinancialOperationLogVo;
import net.bncloud.support.BaseEntityWrapper;


/**
 * <p>
 * 对账单操作记录日志表
 * </p>
 * 包装类,返回视图层所需的字段
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public class AccountOperationLogWrapper extends BaseEntityWrapper<FinancialOperationLog, FinancialOperationLogVo> {

    public static AccountOperationLogWrapper build() {
        return new AccountOperationLogWrapper();
    }

    @Override
    public FinancialOperationLogVo entityVO(FinancialOperationLog entity) {
        FinancialOperationLogVo entityVo = BeanUtil.copy(entity, FinancialOperationLogVo.class);
        return entityVo;
    }


}
