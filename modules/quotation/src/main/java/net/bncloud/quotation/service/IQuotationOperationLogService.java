package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationOperationLog;
import net.bncloud.quotation.param.QuotationOperationLogParam;
import net.bncloud.quotation.vo.QuotationOperationLogVo;

/**
 * <p>
 * 询价单操作记录日志表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-02
 */
public interface IQuotationOperationLogService extends BaseService<QuotationOperationLog> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<QuotationOperationLogVo> selectPage(IPage<QuotationOperationLogVo> page, QueryParam<QuotationOperationLogParam> pageParam);


}
