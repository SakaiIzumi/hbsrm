package net.bncloud.saas.purchaser.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.purchaser.service.PurchaserQueryService;
import net.bncloud.saas.purchaser.service.PurchaserService;
import net.bncloud.saas.purchaser.service.command.*;
import net.bncloud.saas.purchaser.service.query.PurchaserQuery;
import net.bncloud.saas.purchaser.service.query.PurchaserSmallQuery;
import net.bncloud.saas.supplier.service.command.BindPurchaserCommand;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Api(tags = "采购方")
@RestController
@RequestMapping("/purchaser")
@AllArgsConstructor
public class PurchaserResource {


    private final PurchaserService purchaserService;
    private final PurchaserQueryService purchaserQueryService;

    @ApiOperation(value = "创建采购方")
    @PostMapping("/add")
    public R create(@RequestBody CreatePurchaserCommand command) {
        purchaserService.create(command);
        return R.success();
    }

    @ApiOperation(value = "更新采购方信息")
    @PostMapping("/update")
    public R update(@RequestBody UpdatePurchaserCommand command) {
        purchaserService.update(command);
        return R.success();
    }

    @ApiOperation(value = "采购方信息回显")
    @GetMapping("/edit/{id}")
    public R edit(@PathVariable Long id) {
        return R.data(purchaserQueryService.edit(id));
    }

    /**
     * 分页查询
     *
     * @param queryParam
     * @param pageable
     * @return
     */
    @ApiOperation(value = "分页查询采购方信息")
    @PostMapping("/purchasers/page")
    public R page(@RequestBody QueryParam<PurchaserQuery> queryParam, Pageable pageable) {
        return R.data(purchaserService.page(queryParam, pageable));
    }

    /**
     * 下拉接口 此处提供
     *
     * @param queryParam
     * @param pageable
     * @return
     */
    @ApiOperation(value = "下拉分页查询采购方信息")
    @PostMapping("/purchasers/pageQuery")
    public R pageQuery(@RequestBody QueryParam<PurchaserSmallQuery> queryParam, Pageable pageable) {

        PurchaserSmallQuery query = queryParam.getParam();
//        if(query==null){
//            query=new PurchaserSmallQuery();
//        }
        return R.data(purchaserService.pageQuery(query, pageable));
    }

    /**
     * 下拉接口V2 此处提供
     *
     * @param queryParam
     * @param pageable
     * @return
     */
    @ApiOperation(value = "下拉分页查询采购方信息")
    @PostMapping("/purchasers/pageQuerySecond")
    public R pageQueryV2(@RequestBody PurchaserSmallQuery queryParam, Pageable pageable) {

        PurchaserSmallQuery query = queryParam;
//        if(query==null){
//            query=new PurchaserSmallQuery();
//        }
        return R.data(purchaserService.pageQuery(query, pageable));
    }


    @ApiOperation(value = "获取采购方关联供应商信息")
    @GetMapping("/relateSuppliers")
    public R relateSuppliers(@RequestParam Long purchaserId) {
        return R.data(purchaserService.relateSuppliers(purchaserId));
    }


    @ApiOperation(value = "绑定供应商")
    @PostMapping("/bindSupplier")
    public R<Void> bindSupplierCommand(@Validated @RequestBody BindSupplierCommand command) {
        purchaserService.bindSupplierCommand(command);
        return R.success();
    }

    @ApiOperation(value = "取消绑定供应商")
    @PostMapping("/cancelBindSupplier")
    public R<Void> cancelBindSupplierCommand(@Validated @RequestBody CancelBindSupplierCommand command) {
        purchaserService.cancelBindSupplierCommand(command);
        return R.success();
    }


    @ApiOperation(value = "绑定所有供应商")
    @PostMapping("/bindAllToPurchaser")
    public R<Void> bindAllToPurchaserCommand(@Validated @RequestBody BindAllSupplierCommand command) {
        purchaserService.bindAllToPurchaserCommand(command);
        return R.success();
    }

    @ApiOperation(value = "取消绑定所有供应商")
    @PostMapping("/cancelBindAllToPurchaser")
    public R<Void> cancelBindAllToPurchaserCommand(@Validated @RequestBody CancelBindAllSupplierCommand command) {
        purchaserService.cancelBindAllToPurchaserCommand(command);
        return R.success();
    }
}
