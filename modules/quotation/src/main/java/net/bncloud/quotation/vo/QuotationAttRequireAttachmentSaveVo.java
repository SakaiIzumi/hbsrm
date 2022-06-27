package net.bncloud.quotation.vo;

import lombok.Data;
import lombok.ToString;
import net.bncloud.service.api.file.dto.FileInfoDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author lijiaju
 * @date 2022/3/11 9:39
 */
@Data
@ToString
public class QuotationAttRequireAttachmentSaveVo {
    /**
     * 询价单主键ID
     */
    @NotBlank(message = "询价单ID不能为空")
    private Long quotationId;
    /**
     * 需求附件ID
     */
    @NotBlank(message = "需求附件ID不能为空")
    private Long quotationAttRequireId;
    /**
     * 文件信息
     */
    private List<FileInfoDto> fileInfoDtos;
}
