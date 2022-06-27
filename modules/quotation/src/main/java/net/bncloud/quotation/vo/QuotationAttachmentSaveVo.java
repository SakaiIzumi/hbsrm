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
public class QuotationAttachmentSaveVo {
    /**
     * 业务表单ID
     */
    @NotBlank(message = "业务表单ID不能为空")
    private Long businessFormId;
    /**
     * *业务类型，quotation_base 询价单,quotation_pricing 定价单
     */
    @NotBlank(message = "业务类型不为空")
    private String businessType;
    /**
     * 文件信息
     */
    private List<FileInfoDto> fileInfoDtos;
}
