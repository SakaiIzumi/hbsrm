package net.bncloud.delivery.remote;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.common.api.R;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.delivery.entity.OrderDeliverySupplier;
import net.bncloud.delivery.service.DeliveryNoteService;
import net.bncloud.delivery.service.OrderDeliverySupplierService;
import net.bncloud.delivery.service.impl.DeliveryPlanManager;
import net.bncloud.quotation.service.api.dto.MaterialInfoDetailDTO;
import net.bncloud.quotation.service.api.feign.MaterialFeignClient;
import net.bncloud.service.api.delivery.dto.DeliveryDetailUpdateDTO;
import net.bncloud.service.api.delivery.dto.DeliveryMaterialNoticeDTO;
import net.bncloud.service.api.delivery.dto.DeliveryPlanDTO;
import net.bncloud.service.api.delivery.dto.SyncOrgIdParams;
import net.bncloud.service.api.delivery.feign.DeliveryPlanFeignClient;
import net.bncloud.service.api.delivery.feign.OrderDeliverySupplierFeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 订单模块调用的排程供应商controller
 */
@RestController
public class OrderDeliverySupplierRemoteController implements OrderDeliverySupplierFeignClient {

    @Resource
    private OrderDeliverySupplierService orderDeliverySupplierService;

    @Resource
    private MaterialFeignClient materialFeignClient;


    /**
     * 根据当前排程供应商查询拥有的物料编码
     */
    @Override
    public R<List<String>> selectOrderDeliverySupplierProductCode(@RequestBody Long supplierId) {
        List<OrderDeliverySupplier> orderDeliverySupplierList = orderDeliverySupplierService.list(Wrappers
                .<OrderDeliverySupplier>lambdaQuery()
                //.select(OrderDeliverySupplier::getMaterialErpId)
                .eq(OrderDeliverySupplier::getSupplierId, supplierId));

        //为空就是没有关联物料 返回空
        if(CollectionUtil.isEmpty(orderDeliverySupplierList)){
            return R.data(new ArrayList<>());
        }

        //获取所拥有的物料分组erpid
        List<Long> materialErpIds = orderDeliverySupplierList.stream().map(item -> item.getMaterialErpId()).distinct().collect(Collectors.toList());

        R<List<String>> materialCodeR = materialFeignClient.selectMaterialCodeByGroupIds(materialErpIds);
        Asserts.isTrue(materialCodeR.isSuccess(),"远程获取排程供应商所属物料失败");
        List<String> materialCodeList = materialCodeR.getData();
        return R.data(materialCodeList);
    }




}
