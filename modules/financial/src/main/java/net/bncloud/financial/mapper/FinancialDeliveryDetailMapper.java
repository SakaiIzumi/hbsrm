package net.bncloud.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialDeliveryDetail;
import net.bncloud.financial.entity.FinancialStatement;
import net.bncloud.financial.param.FinancialDeliveryDetailParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 出货明细表信息表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialDeliveryDetailMapper extends BaseMapper<FinancialDeliveryDetail> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2021-12-15
     */
    List<FinancialDeliveryDetail> selectListPage(IPage page, QueryParam<FinancialDeliveryDetailParam> pageParam);

    /**
     * 根据对账单ID，查询金额汇总信息
     *
     * @param statementId 对账单ID
     * @return
     */
    FinancialStatement querySummaryAmountByStatementId(@Param("statementId") Long statementId);

    /**
     * 查询下一个项次
     *
     * @param statementId 对账单ID
     * @return 下一个项次编号
     */
    String queryNextItemNo(Long statementId);
}
