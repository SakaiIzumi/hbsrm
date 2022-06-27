package net.bncloud.oem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.objects.annotations.Getter;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.oem.domain.entity.ReceivingAddress;
import net.bncloud.oem.domain.param.ReceivingAddressParam;
import net.bncloud.oem.domain.vo.ReceivingAddressVo;
import net.bncloud.oem.service.ReceivingAddressService;
import net.bncloud.oem.service.api.feign.OemReceivingAddressFeignClient;
import net.bncloud.oem.service.api.vo.OemReceivingAddressVo;
import net.bncloud.oem.wrapper.ReceivingAddressWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 采购地址 控制器
 *
 * @author Auto-generator
 * @since 2022-04-24
 */
@RestController
@RequestMapping("/zc/receiving/address")
public class ReceivingAddressController implements OemReceivingAddressFeignClient{

    @Autowired
    private ReceivingAddressService receivingAddressService;

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询", notes = "")
    public R<PageImpl<ReceivingAddressVo>> page(Pageable pageable, @RequestBody QueryParam<ReceivingAddressParam> param) {
        IPage<ReceivingAddressVo> pageVO=receivingAddressService.selectPage(PageUtils.toPage(pageable),param);
        return R.data(PageUtils.result(pageVO));
    }

    /**
     * 编辑的时候回显地址和供应商,相当于详情
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "分页查询", notes = "")
    public R<ReceivingAddressVo> getById(@PathVariable("id")Long id) {
        LambdaQueryWrapper<ReceivingAddress> eq = Condition.getQueryWrapper(new ReceivingAddress())
                .lambda()
                .eq(ReceivingAddress::getId, id);
        ReceivingAddress address = receivingAddressService.getOne(eq);
        ReceivingAddressVo receivingAddressVo = ReceivingAddressWrapper.build().entityVO(address);
        return R.data(receivingAddressVo);
    }

    /**
     * 编辑
     */
    @PostMapping("/update")
    @ApiOperation(value = "编辑更新", notes = "")
    public R update(@RequestBody ReceivingAddressParam param) {
        receivingAddressService.updateSupplier(param);
        return R.success();
    }

    /**
     * 批量编辑
     */
    @PostMapping("/batchUpdate")
    @ApiOperation(value = "批量编辑", notes = "")
    public R batchUpdate(@RequestBody List<ReceivingAddressParam> receivingAddressParamList) {
        receivingAddressService.batchUpdateSupplier(receivingAddressParamList);
        return R.success();
    }

    @Override
    public R<String> syncOemReceivingAddress(List<OemReceivingAddressVo> oemReceivingAddressVoList) {
        receivingAddressService.syncOemReceivingAddress( oemReceivingAddressVoList );
        return R.success();
    }
}
