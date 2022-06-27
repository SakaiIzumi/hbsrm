package net.bncloud.contract.vo.event;


import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.common.util.DateUtil;
import net.bncloud.contract.entity.Contract;
import net.bncloud.contract.entity.FileInfo;

import java.io.Serializable;
import java.util.Date;
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
public class ContractEvent extends Contract implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private Long  contractId;

    private Long businessId;

    private String addTime = DateUtil.formatDateTime(new Date());


}
