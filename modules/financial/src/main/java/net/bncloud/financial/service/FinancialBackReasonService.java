package net.bncloud.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialBackReason;
import net.bncloud.financial.param.FinancialBackReasonParam;

/**
 * <p>
 * 对账单退回原因信息表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialBackReasonService extends BaseService<FinancialBackReason> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<FinancialBackReason> selectPage(IPage<FinancialBackReason> page, QueryParam<FinancialBackReasonParam> pageParam);


}
