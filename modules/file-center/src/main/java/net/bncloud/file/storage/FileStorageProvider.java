package net.bncloud.file.storage;

import net.bncloud.file.constant.StorageType;

public interface FileStorageProvider extends FileStorage {
    boolean support(StorageType type);
}
