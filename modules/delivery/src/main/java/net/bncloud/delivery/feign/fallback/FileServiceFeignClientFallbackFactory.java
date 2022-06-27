package net.bncloud.delivery.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.delivery.entity.FileInfo;
import net.bncloud.delivery.feign.FileServiceFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName FileServiceFeignClientFallbackFactory
 * @Description: 文件中心服务异常处理
 * @Author Administrator
 * @Date 2021/4/7
 * @Version V1.0
 **/
@Component
public  class FileServiceFeignClientFallbackFactory implements FallbackFactory<FileServiceFeignClient> {
    @Override
    public FileServiceFeignClient create(Throwable throwable) {
        return new FileServiceFeignClient() {
            @Override
            public R<List<FileInfo>> list(List<Long> idList) {
                return R.fail("文件服务暂时不可用，获取文件信息失败，请稍后再试");
            }
        };
    }
}
