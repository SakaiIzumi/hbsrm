package net.bncloud.oem.domain.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ddh
 * @description
 * @since 2022/4/24
 */
@Data
@Accessors(chain = true)
public class FileInfo implements Serializable {
    private static final long serialVersionUID = 1L;

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
}
