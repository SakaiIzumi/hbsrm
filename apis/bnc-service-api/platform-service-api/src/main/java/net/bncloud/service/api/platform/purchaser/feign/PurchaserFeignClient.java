package net.bncloud.service.api.platform.purchaser.feign;

import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.feign.fallback.PurchaserFeignClientFallbackFactory;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.service.api.platform.purchaser.vo.PurchaserVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@AuthorizedFeignClient(name = "platform", contextId = "purchaserClient", fallbackFactory = PurchaserFeignClientFallbackFactory.class)
public interface PurchaserFeignClient {

    @PostMapping("/purchaser/remote/info")
    R<OrgIdDTO> info(@RequestBody OrgIdQuery query);

    @PostMapping("/purchaser/remote/infos")
    R<List<OrgIdDTO>> infos(@RequestBody List<OrgIdQuery> orgInfoQueries);

    @PostMapping("/purchaser/remote/findCustomerCode")
    R<String> findCustomerCode(@RequestBody String customerName);

    @GetMapping("/purchaser/remote/queryAllPurchaser")
    R<List<String>> queryAllPurchaser();

    /**
     * 查询采购方信息 with code
     * @param purchaserCodeColl
     * @return
     */
    @PostMapping("/purchaser/remote/getPurchaserListByCode")
    R<List<PurchaserVo>> getPurchaserListByCode(@RequestBody Collection<?> purchaserCodeColl );

    /**
     * 获取所有采购方信息
     * @param ordId
     * @return
     */
    @PostMapping("/purchaser/remote/getAllPurchaserByOrgId")
    R<List<PurchaserVo>> getAllPurchaserByOrgId(@RequestParam Long ordId);

    @PostMapping("/purchaser/remote/queryAllPurchaserInfo")
    R<List<PurchaserVo>> queryAllPurchaserInfo();

}
