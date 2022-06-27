package net.bncloud.contract.param;


import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.contract.entity.Attachment;
import net.bncloud.contract.entity.Contract;
import net.bncloud.contract.entity.FileInfo;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;


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
public class ContractSaveParam extends Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 附件列表
     */
    @Valid
    private List<FileInfo> attachmentList;


}
