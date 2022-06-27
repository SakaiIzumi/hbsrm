package net.bncloud.service.api.platform.supplier.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.service.api.platform.supplier.dto.FinancialInfoOfSupplierDTO;
import net.bncloud.service.api.platform.supplier.dto.SupplierDTO;
import net.bncloud.service.api.platform.supplier.dto.SuppliersDTO;
import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SupplierFeignClientFallback implements FallbackFactory<SupplierFeignClient> {
    @Override
    public SupplierFeignClient create(Throwable throwable) {
        return new SupplierFeignClient() {
            @Override
            public R<List<SupplierDTO>> findSupplierByCode(List<SupplierDTO> oaSupplierDTOS) {
                log.error("平台服务,异常",throwable);
                return R.fail("平台服务暂时不可用，请稍后再试");
            }

            @Override
            public R<SupplierDTO> findOneSupplierByCode(SupplierDTO oaSupplierDTOS) {
                log.error("平台服务,异常",throwable);
                return R.fail("平台服务暂时不可用，请稍后再试");
            }

            @Override
            public R<String> findSupplierByName(String supplierName) {
                log.error("平台服务,异常",throwable);
                return R.fail("平台服务暂时不可用，请稍后再试");
            }

            @Override
            public R<FinancialInfoOfSupplierDTO> queryFinancialInfoOfSupplier(SupplierDTO supplierDTO) {
                log.error("平台服务,异常",throwable);
                return R.fail("平台服务暂时不可用，获取供应商信息失败，请稍后再试");
            }

            @Override
            public R<SuppliersDTO> querySupplierInformation(Long id) {
                log.error("平台服务,异常",throwable);
                return R.fail("平台服务暂时不可用，获取供应商信息失败");
            }

            @Override
            public R<List<SuppliersDTO>> getSupplierInfoAll() {
                log.error("平台服务,异常",throwable);
                return R.fail("平台服务暂时不可用，获取所有供应商信息失败");
            }
        };
    }
}
