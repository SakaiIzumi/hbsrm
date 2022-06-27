package net.bncloud.contract.feign;

import net.bncloud.common.api.R;
import net.bncloud.contract.entity.FileInfo;
import org.springframework.cloud.openfeign.FeignClient;
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

@FeignClient("file-center")
public interface FileServiceFeignClient {


    @PostMapping("/files/list")
    R<List<FileInfo>> list(@RequestBody List<Long> idList);
}
