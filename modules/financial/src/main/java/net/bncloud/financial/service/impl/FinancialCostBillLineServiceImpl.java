package net.bncloud.financial.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialCostBillLine;
import net.bncloud.financial.mapper.FinancialCostBillLineMapper;
import net.bncloud.financial.param.FinancialCostBillLineParam;
import net.bncloud.financial.service.FinancialCostBillLineService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 费用明细信息表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Service
public class FinancialCostBillLineServiceImpl extends BaseServiceImpl<FinancialCostBillLineMapper, FinancialCostBillLine> implements FinancialCostBillLineService {


    @Override
    public IPage<FinancialCostBillLine> selectPage(IPage<FinancialCostBillLine> page, QueryParam<FinancialCostBillLineParam> pageParam) {
        // 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
        //notice.setTenantId(SecureUtil.getTenantId());
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }


}
