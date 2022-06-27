package net.bncloud.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialStatement;
import net.bncloud.financial.param.FinancialStatementParam;

import java.util.List;

/**
 * <p>
 * 对账单信息表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialStatementMapper extends BaseMapper<FinancialStatement> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2021-12-15
     */
    List<FinancialStatement> selectListPage(IPage page, QueryParam<FinancialStatementParam> pageParam);
}
