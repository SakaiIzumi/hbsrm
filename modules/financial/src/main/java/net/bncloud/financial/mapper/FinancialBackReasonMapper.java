package net.bncloud.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialBackReason;
import net.bncloud.financial.param.FinancialBackReasonParam;

import java.util.List;

/**
 * <p>
 * 对账单退回原因信息表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialBackReasonMapper extends BaseMapper<FinancialBackReason> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2021-12-15
     */
    List<FinancialBackReason> selectListPage(IPage page, QueryParam<FinancialBackReasonParam> pageParam);
}
