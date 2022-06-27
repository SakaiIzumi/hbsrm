package net.bncloud.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialStatementPoolRel;
import net.bncloud.financial.param.FinancialStatementPoolRelParam;

import java.util.List;

/**
 * <p>
 * 对账单与结算单池单据关联关系表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialStatementPoolRelMapper extends BaseMapper<FinancialStatementPoolRel> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2021-12-15
     */
    List<FinancialStatementPoolRel> selectListPage(IPage page, QueryParam<FinancialStatementPoolRelParam> pageParam);
}
