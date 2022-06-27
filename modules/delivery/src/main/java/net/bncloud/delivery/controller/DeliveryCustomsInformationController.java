package net.bncloud.delivery.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.delivery.entity.DeliveryCustomsInformation;
import net.bncloud.delivery.param.DeliveryCustomsInformationParam;
import net.bncloud.delivery.service.DeliveryCustomsInformationService;
import net.bncloud.delivery.vo.DeliveryCustomsInformationVo;
import net.bncloud.delivery.wrapper.DeliveryCustomsInformationWrapper;
import net.bncloud.support.Condition;
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

import java.util.List;


/**
 * <p>
 * 报关资料信息表 前端控制器
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@RestController
@RequestMapping("/customs-information")
@Api(tags = "报关资料信息控制器")
public class DeliveryCustomsInformationController {

    
    @Autowired
    private DeliveryCustomsInformationService deliveryCustomsInformationService;

    
    /**
    * 通过id查询
    */
    @GetMapping("/getByDeliveryId/{deliveryId}")
    @ApiOperation(value = "送货单ID", notes = "传入DeliveryNote")
    public R<DeliveryCustomsInformation> getByDeliveryId(@PathVariable(value = "deliveryId") Long deliveryId){
        return R.data(deliveryCustomsInformationService.getByDeliveryId(deliveryId));
    }


    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入DeliveryCustomsInformation")
    public R<DeliveryCustomsInformation> getById(@PathVariable(value = "id") Long id){
        return R.data(deliveryCustomsInformationService.getById(id));
    }

    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入DeliveryCustomsInformation")
    public R save(@RequestBody DeliveryCustomsInformation deliveryCustomsInformation){
        deliveryCustomsInformationService.save(deliveryCustomsInformation);
        return R.success();
    }

    /**
    * 通过id删除
    */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id){
        deliveryCustomsInformationService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
    * 修改
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改", notes = "传入deliveryCustomsInformation")
    public R updateById(@RequestBody DeliveryCustomsInformation deliveryCustomsInformation){
        deliveryCustomsInformationService.updateById(deliveryCustomsInformation);
        return R.success();
    }


    /**
    * 查询列表
    */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入deliveryCustomsInformation")
    public R list(@RequestBody DeliveryCustomsInformation deliveryCustomsInformation ){
        List<DeliveryCustomsInformation> list = deliveryCustomsInformationService.list(Condition.getQueryWrapper(deliveryCustomsInformation));

        return R.data(list);
    }

    /**
    * 分页查询
    */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询列表", notes = "传入DeliveryCustomsInformationParam")
    public R page(Pageable pageable, @RequestBody QueryParam<DeliveryCustomsInformationParam> queryParam){
        final DeliveryCustomsInformationParam param = queryParam.getParam();

        final IPage<DeliveryCustomsInformation> page = deliveryCustomsInformationService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
		IPage<DeliveryCustomsInformationVo> deliveryCustomsInformationVoIPage = DeliveryCustomsInformationWrapper.build().pageVO(page);

		return R.data(PageUtils.result(deliveryCustomsInformationVoIPage));
    }





}
