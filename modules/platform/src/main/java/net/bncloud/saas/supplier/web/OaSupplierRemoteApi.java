package net.bncloud.saas.supplier.web;

import net.bncloud.common.api.R;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.service.OaSupplierRemoteApiService;
import net.bncloud.saas.supplier.service.SupplierService;
import net.bncloud.service.api.platform.supplier.dto.OaSupplierDTO;
import net.bncloud.service.api.platform.supplier.feign.OaSupplierFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/oa_supplier/")
public class OaSupplierRemoteApi implements OaSupplierFeignClient {

    @Autowired
    private OaSupplierRemoteApiService oaSupplierRemoteApiService;
    @Autowired
    private  SupplierService supplierService;

    @Override
    @PostMapping("/batchHandlePush")
    public R batchHandlePush(@RequestBody List<OaSupplierDTO> oaSupplierDTOS) {
        oaSupplierRemoteApiService.saveOaSupplierRemote(oaSupplierDTOS);
        return R.data(oaSupplierDTOS);
    }

}
