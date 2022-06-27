package net.bncloud.financial.service.impl;

import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.financial.entity.FinancialOperationLog;
import net.bncloud.financial.enums.OperationLogType;
import net.bncloud.financial.mapper.FinancialOperationLogMapper;
import net.bncloud.financial.service.FinancialOperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 对账单操作记录日志表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Service
public class FinancialOperationLogServiceImpl extends BaseServiceImpl<FinancialOperationLogMapper, FinancialOperationLog> implements FinancialOperationLogService {

    @Override
    public void recordOperationLog(OperationLogType operationLogType, Long billId, String context, String remark, LoginInfo loginInfo) {
        FinancialOperationLog financialOperationLog = new FinancialOperationLog()
                .setBillId(billId)
                .setBillType(operationLogType.getCode())
                .setContent(context)
                .setRemark(remark)
                .setOperationNo(loginInfo.getId())
                .setOperatorName(loginInfo.getName());
        this.save(financialOperationLog);
    }
}
