package net.bncloud.service.api.platform.purchaser.feign.fallback;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.PurchaserFeignClient;

import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.service.api.platform.purchaser.vo.PurchaserVo;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class PurchaserFeignClientFallbackFactory implements FallbackFactory<PurchaserFeignClient> {
    @Override
    public PurchaserFeignClient create(Throwable cause) {
        log.error("PurchaserFeignClient remote error!",cause);
        String msg = "服务调用失败，请稍后重试！";
        return new PurchaserFeignClient() {
            @Override
            public R<OrgIdDTO> info(OrgIdQuery query) {
                return R.fail(msg);
            }

            @Override
            public R<List<OrgIdDTO>> infos(List<OrgIdQuery> orgInfoQueries) {
                return R.fail(msg);
            }

            @Override
            public R<String> findCustomerCode(String customerName) {
                return R.fail(msg);
            }

            @Override
            public R<List<String>> queryAllPurchaser() {
                return R.fail(msg);
            }

            @Override
            public R<List<PurchaserVo>> getPurchaserListByCode(Collection<?> purchaserCodeColl) {
                return R.remoteFail();
            }

            @Override
            public R<List<PurchaserVo>> getAllPurchaserByOrgId(Long ordId) {
                return R.remoteFail();
            }

            @Override
            public R<List<PurchaserVo>> queryAllPurchaserInfo() {
                return R.remoteFail();
            }
        };
    }
}
