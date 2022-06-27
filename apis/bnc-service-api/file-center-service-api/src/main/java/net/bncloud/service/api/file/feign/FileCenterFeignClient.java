package net.bncloud.service.api.file.feign;


import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.file.dto.FileInfoDto;
import net.bncloud.service.api.file.feign.fallback.FileCenterClientFallbackFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName FileServiceFeignClient
 * @Description: FileServiceFeignClient
 * @Author Administrator
 * @Date 2021/3/19
 * @Version V1.0
 **/

@AuthorizedFeignClient(name ="file-center",contextId = "fileCenterClient", fallbackFactory = FileCenterClientFallbackFactory.class, decode404 = true)
public interface FileCenterFeignClient {


    @PostMapping("/files/list")
    R<List<FileInfoDto>> list(@RequestBody List<Long> idList);

    @PostMapping(value="/files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<List<FileInfoDto>> upload(@RequestPart("file") MultipartFile[] files);
}
