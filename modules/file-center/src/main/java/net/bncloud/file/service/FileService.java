package net.bncloud.file.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.StringUtil;
import net.bncloud.file.domain.Download;
import net.bncloud.file.domain.FileInfo;
import net.bncloud.file.domain.vo.User;
import net.bncloud.file.enumns.FileResultCode;
import net.bncloud.file.repository.DownloadRepository;
import net.bncloud.file.repository.FileRepository;
import net.bncloud.file.storage.FileStorageManager;
import net.bncloud.file.storage.StorageInfo;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final FileStorageManager fileStorageManager;
    private final DownloadRepository downloadRepository;

    public FileService(FileRepository fileRepository,
                       FileStorageManager fileStorageManager,
                       DownloadRepository downloadRepository) {
        this.fileRepository = fileRepository;
        this.fileStorageManager = fileStorageManager;
        this.downloadRepository = downloadRepository;
    }

    public List<FileInfo> upload(MultipartFile[] files) throws Exception {
        List<FileInfo> collect = Stream.of(files).map(this::upload).filter(Objects::nonNull).collect(Collectors.toList());
        return collect;
    }
    public FileInfo upload(MultipartFile file){
        StorageInfo s = new StorageInfo();
        s.setOriginalFilename(file.getOriginalFilename());
        s.setContentType(file.getContentType());

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID().toString().replace("-", "");
        if (StringUtils.isNotBlank(extension)) {
            filename = filename + "." + extension;
        }
        s.setFilename(filename);
        try {
            StorageInfo storageInfo = fileStorageManager.store(file.getInputStream(), s);
            log.info("storageInfo is {}", JSON.toJSONString(storageInfo));

            FileInfo f = new FileInfo();
            f.setFilename(storageInfo.getFilename());
            f.setOriginalFilename(file.getOriginalFilename());
            f.setContentType(file.getContentType());
            f.setSize(file.getSize());
            f.setStorageType(storageInfo.getStorageType());
            f.setPath(storageInfo.getPath());
            f.setUrl(storageInfo.getUrl());
            f.setExtension(extension);
            f.setStorageType(storageInfo.getStorageType());
            return fileRepository.save(f);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过文件ID集合查询文件列表
     * @param idList id list
     * @return 文件列表
     */
    public List<FileInfo> list(List<Long> idList) {
        return fileRepository.findAllById(idList);
    }

    public void download(String id, HttpServletResponse httpServletResponse) throws Exception {
        Optional<FileInfo> fileInfo = fileRepository.findById(Long.valueOf(id));
        if (!fileInfo.isPresent()) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        FileInfo f = fileInfo.get();
        Download download = new Download();
        download.setDownloadBy(SecurityUtils.getLoginInfo().map(loginInfo -> {
            return User.of(loginInfo.getId(), loginInfo.getName());
        }).orElse(User.of(-1L, "匿名")));
        download.setFileInfo(f);
        downloadRepository.save(download);

        String contentType = f.getContentType();
        if(StringUtil.isNotBlank(contentType) && contentType.contains("image")){
            httpServletResponse.setHeader("Content-Disposition", "inline; filename=\"" +
                    new String(f.getOriginalFilename().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + "\"");
            httpServletResponse.setContentType("image/jpeg;charset=UTF-8");
        }if(StringUtil.isNotBlank(contentType) && contentType.contains("pdf")){
            String fileName = URLEncoder.encode(f.getOriginalFilename(), "UTF-8").replaceAll("\\+", "%20");
            httpServletResponse.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName );
            httpServletResponse.setContentType("application/pdf;charset=utf-8");
        }else{
            String fileName = URLEncoder.encode(f.getOriginalFilename(), "UTF-8").replaceAll("\\+", "%20");
            httpServletResponse.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName );
            httpServletResponse.setContentType("application/octet-stream;charset=UTF-8");
        }

        httpServletResponse.addHeader("Content-Length", String.valueOf(f.getSize()));

        fileStorageManager.loadToStream(f, httpServletResponse.getOutputStream());
    }

    /**
     * 根据id删除文件
     * @param id 文件id
     * @param httpServletResponse
     */
    @Transactional
    public void delete(String id, HttpServletResponse httpServletResponse) throws IOException {
        Optional<FileInfo> fileInfo = fileRepository.findById(Long.valueOf(id));
        if (!fileInfo.isPresent()) {
            throw new BizException(FileResultCode.FILE_NOT_EXIST);
        }
        FileInfo f = fileInfo.get();
        downloadRepository.deleteByFileInfo(f);
        fileRepository.deleteById(f.getId());
        fileStorageManager.delete(f);
    }
}
