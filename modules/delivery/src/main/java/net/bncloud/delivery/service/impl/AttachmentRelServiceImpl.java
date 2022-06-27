package net.bncloud.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.delivery.entity.Attachment;
import net.bncloud.delivery.entity.AttachmentRel;
import net.bncloud.delivery.entity.FileInfo;
import net.bncloud.delivery.mapper.AttachmentRelMapper;
import net.bncloud.delivery.service.AttachmentRelService;
import org.springframework.stereotype.Service;

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
            for (FileInfo attachment : attachmentList) {
                AttachmentRel attachmentRel = AttachmentRel.builder()
                        .attachmentId(attachment.getId())
                        .attachmentName(attachment.getOriginalFilename())
                        .attachmentUrl(attachment.getUrl())
                        .attachmentSize(attachment.getSize())
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

    /**
     * 删除附件，重新保存
     *
     * @param businessFormId 业务表单ID
     * @param attachmentList 附件列表
     */
    @Override
    public void clearAndSave(Long businessFormId, List<FileInfo> attachmentList) {
        //删除附件，重新保存
        LambdaQueryWrapper<AttachmentRel> relQueryWrapper = Wrappers.lambdaQuery();
        relQueryWrapper.eq(AttachmentRel::getBusinessFormId,businessFormId);
        remove(relQueryWrapper);

        if(CollectionUtil.isNotEmpty(attachmentList)){
            List<AttachmentRel> attachmentRelList = buildAttachmentRelList(businessFormId,attachmentList);
            saveBatch(attachmentRelList);
        }
    }
}
