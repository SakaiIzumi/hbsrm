package net.bncloud.delivery.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.StringUtil;
import net.bncloud.delivery.entity.DeliveryCountry;
import net.bncloud.delivery.param.DeliveryCountryParam;
import net.bncloud.delivery.service.DeliveryCountryService;
import net.bncloud.delivery.vo.DeliveryCountryVo;
import net.bncloud.delivery.wrapper.DeliveryCountryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 国家信息表 前端控制器
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@RestController
@RequestMapping("/country")
@Api(tags = "国家信息控制器")
public class DeliveryCountryController {

    
    @Autowired
    private DeliveryCountryService deliveryCountryService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入DeliveryCountry")
    public R<DeliveryCountry> getById(@PathVariable(value = "id") Long id){
        return R.data(deliveryCountryService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入DeliveryCountry")
    public R save(@RequestBody DeliveryCountry deliveryCountry){
        deliveryCountryService.save(deliveryCountry);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id){
        deliveryCountryService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入deliveryCountry")
    public R updateById(@RequestBody DeliveryCountry deliveryCountry){
        deliveryCountryService.updateById(deliveryCountry);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入deliveryCountry")
    public R list(@RequestBody DeliveryCountry deliveryCountry ){
        LambdaQueryWrapper<DeliveryCountry> queryWrapper = Wrappers.lambdaQuery();
        String countryName = deliveryCountry.getCountryName();
        queryWrapper.like(StringUtil.isNotBlank(countryName), DeliveryCountry::getCountryName,countryName);
        List<DeliveryCountry> list = deliveryCountryService.list(queryWrapper);

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询列表", notes = "传入DeliveryCountryParam")
    public R page(Pageable pageable, @RequestBody QueryParam<DeliveryCountryParam> queryParam){
        IPage<DeliveryCountry> page = deliveryCountryService.selectPage(PageUtils.toPage(pageable), queryParam);
        IPage<DeliveryCountryVo> deliveryCountryVoIPage = DeliveryCountryWrapper.build().pageVO(page);

		return R.data(PageUtils.result(deliveryCountryVoIPage));
    }







}
