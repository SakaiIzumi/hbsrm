package net.bncloud.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialCostDetail;
import net.bncloud.financial.entity.FinancialStatement;
import net.bncloud.financial.param.FinancialCostDetailParam;

import java.util.List;

/**
 * <p>
 * 费用信息表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialCostDetailMapper extends BaseMapper<FinancialCostDetail> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2021-12-15
     */
    List<FinancialCostDetail> selectListPage(IPage page, QueryParam<FinancialCostDetailParam> pageParam);

    /**
     * 查询对账金额信息
     *
     * @param statementId 对账单ID
     * @return 对账金额信息
     */
    FinancialStatement querySummaryAmountByStatementId(Long statementId);

    /**
     * 查询下一个项次
     *
     * @param statementId 对账单ID
     * @return 下一个项次编号
     */
    String queryNextItemNo(Long statementId);
}
