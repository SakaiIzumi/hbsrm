package net.bncloud.file.config;

import net.bncloud.file.storage.FileStorageManager;
import net.bncloud.file.storage.FileStorageProvider;
import net.bncloud.file.storage.LocalStorage;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(FileCenterProperties.class)
public class FileCenterConfiguration {

    private final FileCenterProperties fileCenterProperties;

    public FileCenterConfiguration(FileCenterProperties fileCenterProperties) {
        this.fileCenterProperties = fileCenterProperties;
    }

    @Bean
    public FileStorageManager fileStorageManager(ObjectProvider<FileStorageProvider> fileStorageObjectProvider) {
        List<FileStorageProvider> storages = fileStorageObjectProvider.stream().collect(Collectors.toList());
        return new FileStorageManager(storages, fileCenterProperties.getStorageType());
    }

    @Bean
    public LocalStorage localStorage() {
        return new LocalStorage(fileCenterProperties.getLocal().getDir(),
                fileCenterProperties.getLocal().getDomain());
    }
}
