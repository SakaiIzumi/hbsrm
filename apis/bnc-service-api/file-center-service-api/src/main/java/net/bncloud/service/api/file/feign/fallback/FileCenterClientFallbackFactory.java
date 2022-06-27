package net.bncloud.service.api.file.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.service.api.file.dto.FileInfoDto;
import net.bncloud.service.api.file.feign.FileCenterFeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @ClassName FileCenterClientFallbackFactory
 * @Description: 文件中心服务异常处理
 * @Author Administrator
 * @Date 2021/3/28
 * @Version V1.0
 **/
@Component
@Slf4j
public class FileCenterClientFallbackFactory implements FallbackFactory<FileCenterFeignClient> {
    @Override
    public FileCenterFeignClient create(Throwable throwable) {
        return new FileCenterFeignClient() {
            @Override
            public R<List<FileInfoDto>> list(List<Long> idList) {
                return R.fail("文件服务暂时不可用，获取附件信息失败，请稍后再试");
            }

            @Override
            public R<List<FileInfoDto>> upload(MultipartFile[] files) {
                log.error("error:",throwable);
                return R.fail("文件服务暂时不可用，获取附件信息失败，请稍后再试");
            }
        };
    }
}
