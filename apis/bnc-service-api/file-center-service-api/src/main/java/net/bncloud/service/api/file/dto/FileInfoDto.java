package net.bncloud.service.api.file.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="文件对象", description="")
public class FileInfoDto {

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

    private StorageType storageType;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public void setStorageType(StorageType storageType) {
        this.storageType = storageType;
    }

    public Boolean getImg() {
        return isImg;
    }

    public void setImg(Boolean img) {
        isImg = img;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
