package net.bncloud.saas.purchaser.web;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import net.bncloud.common.api.R;
import net.bncloud.saas.purchaser.service.PurchaserQueryService;
import net.bncloud.saas.purchaser.service.PurchaserService;
import net.bncloud.saas.purchaser.service.dto.PurchaserDTO;
import net.bncloud.saas.purchaser.service.query.PurchaserSmallQuery;
import net.bncloud.service.api.platform.purchaser.dto.OrgIdDTO;
import net.bncloud.service.api.platform.purchaser.query.OrgIdQuery;
import net.bncloud.service.api.platform.purchaser.vo.PurchaserVo;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Api(value = "采购方feign接口服务")
@RestController
@RequestMapping("/purchaser/remote")
@AllArgsConstructor
public class PurchaserRemoteApi  {

    private final PurchaserQueryService purchaserQueryService;
    private final PurchaserService purchaserService;

    @PostMapping("/info")
    public R<OrgIdDTO> info(@RequestBody OrgIdQuery query) {
        return R.data(purchaserQueryService.info(query));
    }

    @PostMapping("/infos")
    public R<List<OrgIdDTO>> infos(@RequestBody List<OrgIdQuery> orgInfoQueries) {

        return R.data(purchaserQueryService.infos(orgInfoQueries));
    }

    @PostMapping("/findCustomerCode")
    public R<String> findCustomerCode(String customerName) {
        PurchaserSmallQuery query = new PurchaserSmallQuery();
        query.setName(customerName);
        Page page = purchaserService.pageQuery(query, null);
        PurchaserDTO dto = (PurchaserDTO)page.getContent().get(0);

        return R.data(dto.getCode());
    }


    @GetMapping("/queryAllPurchaser")
    public R<List<String>> queryAllPurchaser() {
        return R.data(purchaserService.getAllPurchaserCode());
    }

    @PostMapping("/getPurchaserListByCode")
    public R<List<PurchaserVo>> getPurchaserListByCode(Collection<?> purchaserCodeColl) {
        return R.data( purchaserQueryService.listByCode(purchaserCodeColl));
    }

    @PostMapping("getAllPurchaserByOrgId")
    public R<List<PurchaserVo>> getAllPurchaserByOrgId(Long ordId) {
        return R.data( purchaserQueryService.getAllPurchaserByOrgId( ordId) );
    }

    @PostMapping("/queryAllPurchaserInfo")
    public R<List<PurchaserVo>> queryAllPurchaserInfo() {
        return R.data( purchaserQueryService.getAllPurchaserInfo());
    }


}
