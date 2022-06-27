package net.bncloud.contract.service;

import net.bncloud.contract.entity.Attachment;
import net.bncloud.contract.entity.AttachmentRel;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.extension.service.IService;
import net.bncloud.contract.entity.FileInfo;

import java.util.List;

/**
 * <p>
 * 订单合同与附件关联关系表 服务类
 * </p>
 *
 * @author huangtao
 * @since 2021-03-13
 */
public interface AttachmentRelService extends BaseService<AttachmentRel> {

    /**
     * 构建附件关联关系
     * @param businessFormId 业务表单ID
     * @param attachmentList 附件列表
     * @return
     */
    List<AttachmentRel> buildAttachmentRelList(Long businessFormId, List<FileInfo> attachmentList);

    /**
     * 通过业务表单ID，删除附件
     * @param id 业务表单ID
     */
    void deleteByBusinessFormId(Long id);
}
