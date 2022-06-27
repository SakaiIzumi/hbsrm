package net.bncloud.file.storage;

import net.bncloud.file.domain.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FileStorage {
    StorageInfo store(InputStream in, StorageInfo info) throws Exception;

    File load(FileInfo info) throws Exception;

    void loadToStream(FileInfo info, OutputStream out) throws Exception;

    void delete(FileInfo f) throws IOException;
}
