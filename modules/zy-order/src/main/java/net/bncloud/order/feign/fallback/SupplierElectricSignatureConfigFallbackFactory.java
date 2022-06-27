package net.bncloud.order.feign.fallback;

import feign.hystrix.FallbackFactory;
import net.bncloud.common.api.R;
import net.bncloud.order.constants.ElectricSignatureConfigType;
import net.bncloud.order.entity.SupplierElectricSignatureConfig;
import net.bncloud.order.feign.SupplierElectricSignatureConfigResourceFeignClient;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;


@Component
public class SupplierElectricSignatureConfigFallbackFactory implements FallbackFactory<SupplierElectricSignatureConfigResourceFeignClient> {

    @Override
    public SupplierElectricSignatureConfigResourceFeignClient create(Throwable throwable) {
      return new SupplierElectricSignatureConfigResourceFeignClient() {
          @Override
          public R<SupplierElectricSignatureConfig> getByCodeAndType(String code, ElectricSignatureConfigType type) {
              throwable.printStackTrace();
              return R.fail(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"获取合同网签数据公司开关接口调用失败！");
          }
      };
    }
}
