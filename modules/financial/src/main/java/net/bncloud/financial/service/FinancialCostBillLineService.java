package net.bncloud.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialCostBillLine;
import net.bncloud.financial.param.FinancialCostBillLineParam;

/**
 * <p>
 * 费用明细信息表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialCostBillLineService extends BaseService<FinancialCostBillLine> {

    /**
     * 自定义分页
     *
     * @param pageParam
     * @return
     */
    IPage<FinancialCostBillLine> selectPage(IPage<FinancialCostBillLine> page, QueryParam<FinancialCostBillLineParam> pageParam);


}
