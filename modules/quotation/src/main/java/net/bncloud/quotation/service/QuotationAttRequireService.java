package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationAttRequire;
import net.bncloud.quotation.param.QuotationAttRequireParam;
import net.bncloud.quotation.param.SupplierAttRequireParam;
import net.bncloud.quotation.vo.SupplierAttachmentRequireVo;

/**
 * <p>
 * 附件需求清单 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface QuotationAttRequireService extends BaseService<QuotationAttRequire> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<QuotationAttRequire> selectPage(IPage<QuotationAttRequire> page, QueryParam<QuotationAttRequireParam> pageParam);

    /**
     * 分页查询供应商的附件需求清单
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<SupplierAttachmentRequireVo> selectSupplierAttRequirePage(IPage<SupplierAttachmentRequireVo> page, QueryParam<SupplierAttRequireParam> pageParam);

}
