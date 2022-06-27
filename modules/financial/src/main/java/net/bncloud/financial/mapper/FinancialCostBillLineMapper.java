package net.bncloud.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialCostBillLine;
import net.bncloud.financial.param.FinancialCostBillLineParam;

import java.util.List;

/**
 * <p>
 * 费用明细信息表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialCostBillLineMapper extends BaseMapper<FinancialCostBillLine> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2021-12-15
     */
    List<FinancialCostBillLine> selectListPage(IPage page, QueryParam<FinancialCostBillLineParam> pageParam);


}
