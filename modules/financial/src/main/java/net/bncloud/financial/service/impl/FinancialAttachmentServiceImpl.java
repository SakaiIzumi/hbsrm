package net.bncloud.financial.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.financial.entity.FileInfo;
import net.bncloud.financial.entity.FinancialAttachment;
import net.bncloud.financial.mapper.FinancialAttachmentMapper;
import net.bncloud.financial.param.FinancialAttachmentParam;
import net.bncloud.financial.service.FinancialAttachmentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 对账附件信息表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@Service
public class FinancialAttachmentServiceImpl extends BaseServiceImpl<FinancialAttachmentMapper, FinancialAttachment> implements FinancialAttachmentService {

    @Override
    public List<FinancialAttachment> buildAttachmentRelList(Long businessFormId, List<FileInfo> attachmentList) {
        List<FinancialAttachment> financialAttachmentList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(attachmentList)) {
            for (FileInfo fileInfo : attachmentList) {
                FinancialAttachment financialAttachment = new FinancialAttachment().setAttachmentId(fileInfo.getId().toString())
                        .setAttachmentName(fileInfo.getOriginalFilename())
                        .setAttachmentUrl(fileInfo.getUrl())
                        .setBusinessFormId(businessFormId.toString());
                financialAttachmentList.add(financialAttachment);
            }
        }
        return financialAttachmentList;
    }

    @Override
    public IPage<FinancialAttachment> selectPage(IPage<FinancialAttachment> page, QueryParam<FinancialAttachmentParam> pageParam) {
        // 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
        //notice.setTenantId(SecureUtil.getTenantId());
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }
}
