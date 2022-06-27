package net.bncloud.financial.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FileInfo;
import net.bncloud.financial.entity.FinancialAttachment;
import net.bncloud.financial.param.FinancialAttachmentParam;

import java.util.List;

/**
 * <p>
 * 对账附件信息表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialAttachmentService extends BaseService<FinancialAttachment> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<FinancialAttachment> selectPage(IPage<FinancialAttachment> page, QueryParam<FinancialAttachmentParam> pageParam);

    /**
     * 构建附件关联关系
     *
     * @param businessFormId 业务表单ID
     * @param attachmentList 附件列表
     * @return
     */
    List<FinancialAttachment> buildAttachmentRelList(Long businessFormId, List<FileInfo> attachmentList);

}
