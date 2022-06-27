package net.bncloud.financial.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialBackReason;
import net.bncloud.financial.mapper.FinancialBackReasonMapper;
import net.bncloud.financial.param.FinancialBackReasonParam;
import net.bncloud.financial.service.FinancialBackReasonService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 对账单退回原因信息表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Service
public class FinancialBackReasonServiceImpl extends BaseServiceImpl<FinancialBackReasonMapper, FinancialBackReason> implements FinancialBackReasonService {

    @Override
    public IPage<FinancialBackReason> selectPage(IPage<FinancialBackReason> page, QueryParam<FinancialBackReasonParam> pageParam) {
        // 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
        //notice.setTenantId(SecureUtil.getTenantId());
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }
}
