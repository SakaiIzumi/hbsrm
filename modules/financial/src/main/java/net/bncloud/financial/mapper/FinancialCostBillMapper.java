package net.bncloud.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialCostBill;
import net.bncloud.financial.param.FinancialCostBillParam;

import java.util.List;

/**
 * <p>
 * 费用单据信息表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialCostBillMapper extends BaseMapper<FinancialCostBill> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2021-12-15
     */
    List<FinancialCostBill> selectListPage(IPage page, QueryParam<FinancialCostBillParam> pageParam);
}
