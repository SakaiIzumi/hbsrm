package net.bncloud.delivery.feign;

import net.bncloud.common.api.R;
import net.bncloud.delivery.entity.FileInfo;
import net.bncloud.delivery.feign.fallback.FileServiceFeignClientFallbackFactory;
import net.bncloud.security.client.AuthorizedFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ClassName FileServiceFeignClient
 * @Description: FileServiceFeignClient
 * @Author Administrator
 * @Date 2021/3/19
 * @Version V1.0
 **/

@AuthorizedFeignClient(name = "file-center", contextId = "fileCenterClient" ,fallbackFactory = FileServiceFeignClientFallbackFactory.class, decode404 = true)
public interface FileServiceFeignClient {


    @PostMapping("/files/list")
    R<List<FileInfo>> list(@RequestBody List<Long> idList);
}
