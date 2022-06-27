package net.bncloud.contract.service.impl;

import net.bncloud.common.util.CollectionUtil;
import net.bncloud.contract.entity.Attachment;
import net.bncloud.contract.entity.AttachmentRel;
import net.bncloud.contract.entity.FileInfo;
import net.bncloud.contract.mapper.AttachmentRelMapper;
import net.bncloud.contract.service.AttachmentRelService;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单合同与附件关联关系表 服务实现类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-13
 */
@Service
public class AttachmentRelServiceImpl extends BaseServiceImpl<AttachmentRelMapper, AttachmentRel> implements AttachmentRelService {

    /**
     * 构建附件关联关系
     * @param businessFormId 业务表单ID
     * @param attachmentList 附件列表
     * @return
     */
    @Override
    public List<AttachmentRel> buildAttachmentRelList(Long businessFormId,List<FileInfo> attachmentList) {
        List<AttachmentRel> attachmentRelList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(attachmentList)){
            for (FileInfo fileInfo : attachmentList) {
                AttachmentRel attachmentRel = AttachmentRel.builder()
                        .attachmentId(fileInfo.getId().toString())
                        .attachmentName(fileInfo.getOriginalFilename())
                        .attachmentUrl(fileInfo.getUrl())
                        .businessFormId(businessFormId)
                        .build();
                attachmentRelList.add(attachmentRel);
            }

        }
        return attachmentRelList;
    }

    @Override
    public void deleteByBusinessFormId(Long id) {

    }
}
