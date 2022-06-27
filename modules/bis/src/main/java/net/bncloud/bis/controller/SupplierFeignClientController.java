package net.bncloud.bis.controller;

import net.bncloud.bis.manager.SupplierManager;
import net.bncloud.bis.service.api.feign.SupplierFeignClient;
import net.bncloud.common.api.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * desc: 供应商
 *
 * @author Rao
 * @Date 2022/01/20
 **/
@RequestMapping
@RestController
public class SupplierFeignClientController implements SupplierFeignClient {

    @Resource
    private SupplierManager supplierManager;

    /**
     * 同步数据
     * @param ids
     * @return
     */
    @Override
    public R<Object> syncSupplierInfo(List<Integer> ids) {
        supplierManager.syncSupplierInfo( ids);
        return R.success();
    }

    @Override
    public R<String> syncSupplierInfoWithTable(List<Integer> ids) {
        return supplierManager.syncOaTableData(ids);
    }


}
