package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationOperationLog;
import net.bncloud.quotation.mapper.QuotationOperationLogMapper;
import net.bncloud.quotation.param.QuotationOperationLogParam;
import net.bncloud.quotation.service.IQuotationOperationLogService;
import net.bncloud.quotation.vo.QuotationOperationLogVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 询价单操作记录日志表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-02
 */
@Service
public class QuotationOperationLogServiceImpl extends BaseServiceImpl<QuotationOperationLogMapper, QuotationOperationLog> implements IQuotationOperationLogService {

    @Override
    public IPage<QuotationOperationLogVo> selectPage(IPage<QuotationOperationLogVo> page, QueryParam<QuotationOperationLogParam> pageParam) {
        // 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
        //notice.setTenantId(SecureUtil.getTenantId());
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }
}
