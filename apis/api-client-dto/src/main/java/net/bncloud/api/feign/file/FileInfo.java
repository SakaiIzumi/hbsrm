package net.bncloud.api.feign.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Toby
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo implements Serializable {

    private static final long serialVersionUID = -7306945054166828222L;

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
