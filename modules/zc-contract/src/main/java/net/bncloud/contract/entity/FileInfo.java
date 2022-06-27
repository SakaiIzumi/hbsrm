package net.bncloud.contract.entity;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class FileInfo {

    private static final long serialVersionUID = -7306945054166828222L;

    @NotNull(message = "文件ID不能为空")
    private Long id;
    /**
     * 原始文件名
     */
    @NotBlank(message = "文件名称不能为空")
    private String originalFilename;

    /**
     * 存储的文件名（包含扩展名）
     */
    private String filename;

    /**
     * 文件扩展名
     */
    private String extension;


    /**
     * 是否图片
     */
    private Boolean isImg;
    /**
     * 上传文件类型
     */
    private String contentType;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 冗余字段
     */
    private String path;
    /**
     * 访问路径
     */
    @NotBlank(message = "文件地址不能为空")
    private String url;
    /**
     * 请求id
     */
    @NotBlank(message = "请求id")
    private Integer requestId;

}
