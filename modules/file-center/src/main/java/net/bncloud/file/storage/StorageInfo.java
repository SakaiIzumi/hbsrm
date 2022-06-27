package net.bncloud.file.storage;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.file.constant.StorageType;

@Getter
@Setter
public class StorageInfo {
    private StorageType storageType;
    private String originalFilename;
    private String filename;
    private String path;
    private String url;
    private String contentType;

}
