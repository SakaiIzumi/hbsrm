package net.bncloud.quotation.vo;


import net.bncloud.quotation.entity.QuotationAttachment;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * <p>
 * 附件信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
public class QuotationAttachmentVo extends QuotationAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    private Integer itemNo;

    /**
     * 文件名字
     */
    private String originalFilename;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 文件名字
     */
    private String filename;

    /**
     * 文件url
     */
    private String url;

}
