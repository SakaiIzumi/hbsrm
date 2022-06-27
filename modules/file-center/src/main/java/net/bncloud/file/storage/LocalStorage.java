package net.bncloud.file.storage;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.file.constant.StorageType;
import net.bncloud.file.domain.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class LocalStorage extends AbstractFileStorage {

    private final String dir;

    private final String domain;

    public LocalStorage(String dir,String domain) {
        this.dir = dir;
        this.domain = domain;
    }

    @Override
    public boolean support(StorageType type) {
        return StorageType.LOCAL == type;
    }

    @Override
    public StorageInfo store(InputStream in, StorageInfo info) throws Exception {

        Files.copy(in, Paths.get(dir, info.getFilename()));
        info.setPath(info.getFilename());
        info.setStorageType(StorageType.LOCAL);
        String path = Paths.get(dir, info.getFilename()).toString()
                .replaceAll("\\\\","/");
        info.setUrl(domain+path);
        return info;
    }

    @Override
    public File load(FileInfo info) throws Exception {
        Path path = Paths.get(dir, info.getPath());
        return path.toFile();
    }

    @Override
    public void loadToStream(FileInfo info, OutputStream out) throws Exception {
        Path path = Paths.get(dir, info.getPath());
        Files.copy(path, out);
    }

    @Override
    public void delete(FileInfo info) throws IOException {
        log.info(info.toString());
        Path path = Paths.get(dir,info.getPath());
        Files.delete(path);
    }
}
