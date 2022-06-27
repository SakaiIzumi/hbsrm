package net.bncloud.saas.supplier.web;

import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.domain.SupplierAccount;
import net.bncloud.saas.supplier.domain.SupplierExt;
import net.bncloud.saas.supplier.service.OaSupplierRemoteApiService;
import net.bncloud.saas.supplier.service.SupplierService;
import net.bncloud.saas.supplier.service.dto.SupplierArchiveDTO;
import net.bncloud.saas.supplier.service.query.SupplierQuery;
import net.bncloud.service.api.platform.supplier.dto.FinancialInfoOfSupplierDTO;
import net.bncloud.service.api.platform.supplier.dto.OaSupplierDTO;
import net.bncloud.service.api.platform.supplier.dto.SupplierDTO;
import net.bncloud.service.api.platform.supplier.dto.SuppliersDTO;
import net.bncloud.service.api.platform.supplier.feign.OaSupplierFeignClient;
import net.bncloud.service.api.platform.supplier.feign.SupplierFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * 平台服务模块-供应商远程接口文档
 */
@RestController
@RequestMapping("/supplier/")
public class SupplierRemoteApi implements SupplierFeignClient {

    @Autowired
    private OaSupplierRemoteApiService oaSupplierRemoteApiService;
    @Autowired
    private  SupplierService supplierService;


    @Override
    @PostMapping("/findSupplierByCode")
    public R<List<SupplierDTO>> findSupplierByCode(List<SupplierDTO> SupplierDTO) {

        for (SupplierDTO oaSupplierDTO : SupplierDTO) {
            Supplier supplier = supplierService.getByCode(oaSupplierDTO.getCode());
            if (supplier != null) {
                oaSupplierDTO.setRelevanceStatus(supplier.getRelevanceStatus());
            }

        }
        return R.data(SupplierDTO);
    }


    @Override
    @PostMapping("/findOneSupplierByCode")
    public R<SupplierDTO> findOneSupplierByCode(SupplierDTO SupplierDTO) {
        Supplier supplier = supplierService.getByCode(SupplierDTO.getCode());
        SupplierDTO supplierDTO = BeanUtil.copy(supplier, SupplierDTO.getClass());
        return R.data(supplierDTO);
    }

    @Override
    @PostMapping("/findSupplierByName")
    public R<String> findSupplierByName(String supplierName) {
        SupplierQuery supplierQuery = new SupplierQuery();
        supplierQuery.setSupplierName(supplierName);
        QueryParam<SupplierQuery> queryParam = new QueryParam<>();
        queryParam.setParam(supplierQuery);

        //构建Pageable对象
        int page = 0;
        int pageSize = 100;
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");

        Page<SupplierArchiveDTO> supplierArchiveDTOS = supplierService.pageQuery(queryParam, pageRequest);
        String code = supplierArchiveDTOS.getContent().get(0).getCode();
        return R.data(code);
    }

    /**
     * 获取供应商的对账信息
     * @param supplierDTO 供应商的编码
     * @return 供应商的对账信息
     */
    @PostMapping("/queryFinancialInfoOfSupplier")
    @ApiOperation(value = "获取供应商的对账信息接口", notes = "")
    public R<FinancialInfoOfSupplierDTO> queryFinancialInfoOfSupplier(@RequestBody SupplierDTO supplierDTO){
        final SupplierExt supplierExt =supplierService.queryFinancialInfoOfSupplier(supplierDTO.getCode());
        FinancialInfoOfSupplierDTO info = BeanUtil.copy(supplierExt,FinancialInfoOfSupplierDTO.class);
        return R.data(info);
    }

    /**
     * 获取供应商的银行账户信息列表
     * @param supplier 供应商的信息
     * @return 供应商的银行账户信息列表
     */
    @PostMapping("/querySupplierAccountInfo")
    @ApiOperation(value = "获取供应商的银行信息接口", notes = "")
    public R<List<SupplierAccount>> querySupplierAccountInfo(@RequestBody Supplier supplier){
       final List<SupplierAccount> accounts=supplierService.querySupplierAccountInfo(supplier.getCode());
       return R.data(accounts);
    }

    /**
     * 通过供应商id获取供应商的信息
     * @param id 供应商id
     * @return
     */
    @PostMapping("/querySupplierInformation/{id}")
    @ApiOperation(value = "获取供应商信息接口", notes = "")
    public R<SuppliersDTO> querySupplierInformation(@PathVariable("id") Long id){
        SuppliersDTO supplier = supplierService.querySupplierInformation(id);
        return R.data(supplier);
    }

    /**
     * 获取所有供应商信息接口
     * @return
     */
    @GetMapping("/getSupplierInfoAll")
    @ApiOperation(value = "获取所有供应商信息接口", notes = "")
    public R<List<SuppliersDTO>> getSupplierInfoAll() {
        List<SuppliersDTO> list=supplierService.getSupplierInfoAll();
        return R.data(list);
    }


}
