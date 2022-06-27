package net.bncloud.quotation.service.api.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.quotation.service.api.dto.MaterialInfoDTO;
import net.bncloud.quotation.service.api.dto.MaterialInfoDetailDTO;
import net.bncloud.quotation.service.api.feign.MaterialFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lijiaju
 * @date 2022/2/22 10:41
 */
@Component
@Slf4j
public class MaterialFeignClientFallbackFactory implements FallbackFactory<MaterialFeignClient> {
    @Override
    public MaterialFeignClient create(Throwable throwable) {
        return new MaterialFeignClient() {
            @Override
            public void syncMaterialDataSaveOrUpdate(List<MaterialInfoDTO> materialInfoDTOS) {
                log.error("feign error!",throwable);
            }

            @Override
            public R<MaterialInfoDetailDTO> getMaterialDetailByCode(String code) {
                log.error("远程获取物料明细异常",throwable);
                return R.fail("远程获取物料明细异常");
            }

            @Override
            public R<List<String>> selectMaterialCodeByGroupIds(List<Long> materialGroupIds) {
                return R.remoteFail();
            }
        };
    }
}
