package net.bncloud.service.api.platform.supplier.feign;

import net.bncloud.common.api.R;
import net.bncloud.security.client.AuthorizedFeignClient;
import net.bncloud.service.api.platform.supplier.dto.FinancialInfoOfSupplierDTO;
import net.bncloud.service.api.platform.supplier.dto.SupplierDTO;
import net.bncloud.service.api.platform.supplier.dto.SuppliersDTO;
import net.bncloud.service.api.platform.supplier.feign.fallback.SupplierFeignClientFallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@AuthorizedFeignClient(name = "platform", contextId = "supplierClient", fallbackFactory = SupplierFeignClientFallback.class, decode404 = true)
public interface SupplierFeignClient {

    @PostMapping("/supplier/findSupplierByCode")
    R<List<SupplierDTO>> findSupplierByCode(@RequestBody List<SupplierDTO> oaSupplierDTOS);

    @PostMapping("/supplier/findOneSupplierByCode")
    R<SupplierDTO> findOneSupplierByCode(@RequestBody SupplierDTO oaSupplierDTOS);

    @PostMapping("/supplier/findSupplierByName")
    R<String> findSupplierByName(@RequestBody String supplierName);

    /**
     * 查询供应商的对账信息
     * @return 应商的对账信息
     */
    @PostMapping("/supplier/queryFinancialInfoOfSupplier")
    R<FinancialInfoOfSupplierDTO> queryFinancialInfoOfSupplier(@RequestBody SupplierDTO supplierDTO);

    /**
     * 通过供应商id获取供应商的信息
     * @param id
     * @return
     */
    @PostMapping("/supplier/querySupplierInformation/{id}")
    R<SuppliersDTO> querySupplierInformation(@PathVariable("id") Long id);

    /**
     * 获取所有供应商信息接口
     * @return
     */
    @GetMapping("/supplier/getSupplierInfoAll")
    R<List<SuppliersDTO>> getSupplierInfoAll();
}
