package net.bncloud.contract.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.contract.entity.FileInfo;
import net.bncloud.contract.feign.FileCenterFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName FileCenterClientFallbackFactory
 * @Description: 文件中心服务异常处理
 * @Author Administrator
 * @Date 2021/3/28
 * @Version V1.0
 **/
@Component
public class FileCenterClientFallbackFactory implements FallbackFactory<FileCenterFeignClient> {
    @Override
    public FileCenterFeignClient create(Throwable throwable) {
        return new FileCenterFeignClient() {
            @Override
            public R<List<FileInfo>> list(List<Long> idList) {
                return R.fail("文件服务暂时不可用，获取附件信息失败，请稍后再试");
            }
        };
    }
}
