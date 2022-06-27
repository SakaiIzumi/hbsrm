package net.bncloud.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialOperationLog;
import net.bncloud.financial.param.FinancialOperationLogParam;

import java.util.List;

/**
 * <p>
 * 对账单操作记录日志表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialOperationLogMapper extends BaseMapper<FinancialOperationLog> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2021-12-15
     */
    List<FinancialOperationLog> selectListPage(IPage page, QueryParam<FinancialOperationLogParam> pageParam);
}
