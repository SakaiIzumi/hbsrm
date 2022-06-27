package net.bncloud.bis.controller;

import net.bncloud.bis.manager.ExperimentSupplierManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * desc: 测试controller
 *
 * @author Rao
 * @Date 2022/02/22
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private ExperimentSupplierManager experimentSupplierManager;

    @RequestMapping("test1")
    public void test1(){
        experimentSupplierManager.experimentSupplierSyncInStockOrder(null);
    }

    @RequestMapping("test2")
    public void test2(){
        experimentSupplierManager.experimentSupplierSyncPurchaseOrder(null);
    }

}
