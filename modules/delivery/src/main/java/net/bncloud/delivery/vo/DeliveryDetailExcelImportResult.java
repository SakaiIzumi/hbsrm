package net.bncloud.delivery.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * desc: 送货明细excel导入结果
 *
 * @author Rao
 * @Date 2022/03/12
 **/
@Accessors(chain = true)
@Data
public class DeliveryDetailExcelImportResult implements Serializable {
    private static final long serialVersionUID = -1644061224452917453L;

    public static final String UPLOADING = "上传中";
    public static final String SUCCESS = "上传成功";
    public static final String FAIL = "上传失败";

    /**
     * 送货单ID
     */
    private Long deliveryNoteId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小(字节)
     */
    private Long size;

    /**
     * 上传中/上传成功/上传失败
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}
