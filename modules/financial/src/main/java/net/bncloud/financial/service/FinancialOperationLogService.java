package net.bncloud.financial.service;

import net.bncloud.base.BaseService;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.financial.entity.FinancialOperationLog;
import net.bncloud.financial.enums.OperationLogType;

/**
 * <p>
 * 对账单操作记录日志表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialOperationLogService extends BaseService<FinancialOperationLog> {

    /**
     * 记录操作记录
     */
    void recordOperationLog(OperationLogType operationLogType, Long billId, String context, String remark, LoginInfo loginInfo);

}
