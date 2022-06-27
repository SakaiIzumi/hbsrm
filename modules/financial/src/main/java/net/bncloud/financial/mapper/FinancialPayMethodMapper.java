package net.bncloud.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialPayMethod;
import net.bncloud.financial.param.FinancialPayMethodParam;

import java.util.List;

/**
 * <p>
 * 支付方式信息表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialPayMethodMapper extends BaseMapper<FinancialPayMethod> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2021-12-15
     */
    List<FinancialPayMethod> selectListPage(IPage page, QueryParam<FinancialPayMethodParam> pageParam);
}
