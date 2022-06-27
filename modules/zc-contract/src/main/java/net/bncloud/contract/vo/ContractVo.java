package net.bncloud.contract.vo;


import lombok.*;
import net.bncloud.contract.entity.Attachment;
import net.bncloud.contract.entity.Contract;
import net.bncloud.contract.entity.FileInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合同信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
@Data
@NoArgsConstructor
public class ContractVo extends Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事项类型
     */
    private String EventTypeName;

    /**
     * 附件列表
     */
    private List<FileInfo> attachmentList;

    /**
     * 历史合同列表
     */
    private List<Contract> historyContracts;

    /**
     * 可操作按钮
     */
    private Map<String,Boolean> permissionButton;

    /**
     * 合同金额字段
     */
    private String taxRateAmountString;



}
