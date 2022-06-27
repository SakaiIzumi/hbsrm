package net.bncloud.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialSettlementPool;
import net.bncloud.financial.param.FinancialSettlementPoolParam;

import java.util.List;

/**
 * <p>
 * 结算池单据信息表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialSettlementPoolMapper extends BaseMapper<FinancialSettlementPool> {
    /**
     * 自定义分页
     *
     * @param page      分页参数
     * @param pageParam 查询参数
     * @return 结算池
     */
    List<FinancialSettlementPool> selectListPage(IPage page, QueryParam<FinancialSettlementPoolParam> pageParam);
}
