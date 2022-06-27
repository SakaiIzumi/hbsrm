package net.bncloud.service.api.contract.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class FileInfoDTO implements Serializable {

    private static final long serialVersionUID = -7306945054166828226L;

    private Long id;
    /**
     * 原始文件名
     */
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
    private String url;
    /**
     * 请求id
     */
    private Integer requestId;

}
