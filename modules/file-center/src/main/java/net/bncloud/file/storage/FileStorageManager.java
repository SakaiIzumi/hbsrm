package net.bncloud.file.storage;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.file.constant.StorageType;
import net.bncloud.file.domain.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

@Slf4j
public class FileStorageManager implements FileStorage {

    private StorageType storageType = StorageType.LOCAL;

    private List<FileStorageProvider> fileStorages = Collections.emptyList();
    public FileStorageManager(List<FileStorageProvider> fileStorages, StorageType storageType) {
        this.fileStorages = fileStorages;
        this.storageType = storageType;
        checkState();
    }

    private void checkState() {
        if (fileStorages.isEmpty()) {
            //fixme
            /*throw new IllegalArgumentException(
                    "A list of FileStorage is required");*/
        } else if (fileStorages.contains(null)) {
            throw new IllegalArgumentException(
                    "storage list cannot contain null values");
        }
    }

    @Override
    public StorageInfo store(InputStream in, StorageInfo info) throws Exception {
        for (FileStorageProvider fileStorage : fileStorages) {
            if (fileStorage.support(storageType)) {
                return fileStorage.store(in, info);
            }
        }
        return null;
    }

    @Override
    public File load(FileInfo info) throws Exception {
        StorageType storageType = info.getStorageType();
        for (FileStorageProvider fileStorage : fileStorages) {
            if (fileStorage.support(storageType)) {
                return fileStorage.load(info);
            }
        }
        return null;
    }

    @Override
    public void loadToStream(FileInfo info, OutputStream out) throws Exception {
        StorageType storageType = info.getStorageType();
        for (FileStorageProvider fileStorage : fileStorages) {
            if (fileStorage.support(storageType)) {
                fileStorage.loadToStream(info, out);
                return;
            }
        }
    }

    @Override
    public void delete(FileInfo info) throws IOException {
        StorageType storageType = info.getStorageType();
        for (FileStorageProvider fileStorage : fileStorages) {
            if (fileStorage.support(storageType)) {
                fileStorage.delete(info);
                return;
            }
        }
    }
}
