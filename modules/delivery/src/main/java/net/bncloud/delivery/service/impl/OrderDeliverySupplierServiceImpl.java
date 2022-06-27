package net.bncloud.delivery.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.delivery.entity.OrderDeliverySupplier;
import net.bncloud.delivery.mapper.OrderDeliverySupplierMapper;
import net.bncloud.delivery.param.OrderDeliverySupplierParam;
import net.bncloud.delivery.service.OrderDeliverySupplierService;
import net.bncloud.delivery.vo.OrderDeliverySupplierVo;
import net.bncloud.delivery.vo.SupplierItem;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author ddh
 * @description
 * @since 2022/5/23
 */
@Slf4j
@Service
public class OrderDeliverySupplierServiceImpl extends BaseServiceImpl<OrderDeliverySupplierMapper, OrderDeliverySupplier> implements OrderDeliverySupplierService {

    private final OrderDeliverySupplierMapper supplierMapper;
    public OrderDeliverySupplierServiceImpl(OrderDeliverySupplierMapper supplierMapper) {
        this.supplierMapper = supplierMapper;
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)){
            deleteLogic(ids);
        }
    }

    /**
     * 保存供应商(批量保存同一种物料类型的供应商)
     * 去重，同一种物料类型不能保存相同的两个供应商
     */
    @Override
    @Transactional
    public void saveSuppliers(OrderDeliverySupplierParam param) {
        if (CollectionUtil.isEmpty(param.getSupplierList())) {
            throw new ApiException(500,"请选择供应商！");
        }
        if (CollectionUtil.isEmpty(param.getMaterialList())) {
            throw new ApiException(500,"请选择物料！");
        }

        long length = param.getSupplierList().stream().map(OrderDeliverySupplier::getSupplierId).count();
        int disLength = (int) param.getSupplierList().stream().map(OrderDeliverySupplier::getSupplierId).distinct().count();
        if (length!=disLength){
            log.info("选择的供应商存在重复的供应商");
            throw new RuntimeException("您重复选择了同一个供应商！");
        }
        /*suppliers.forEach(supplier->{
                OrderDeliverySupplier orderDeliverySupplier = getOne(Wrappers.<OrderDeliverySupplier>lambdaQuery()
                        .eq(OrderDeliverySupplier::getSupplierId, supplier.getSupplierId())
                        .eq(OrderDeliverySupplier::getMaterialType, supplier.getMaterialType()));
                if (ObjectUtil.isNotEmpty(orderDeliverySupplier)) {
                    log.info("选择了一个已经存在的供应商");
                    throw new RuntimeException("您选择了一个已经存在的供应商！");
                }
                //supplier.setStatus(true);
                save(supplier);
            });*/
        List<OrderDeliverySupplier> supplierList = param.getSupplierList();
        List<OrderDeliverySupplier> materialList = param.getMaterialList();

        List<OrderDeliverySupplier> saveList =new ArrayList<>();
        for (OrderDeliverySupplier orderDeliverySupplier : supplierList) {
            for (OrderDeliverySupplier deliverySupplier : materialList) {

                OrderDeliverySupplier orderDeliverySupplierForSave = new OrderDeliverySupplier();

                //保存供应商信息
                orderDeliverySupplierForSave.setSupplierId(orderDeliverySupplier.getSupplierId());
                orderDeliverySupplierForSave.setSupplierName(orderDeliverySupplier.getSupplierName());
                orderDeliverySupplierForSave.setSupplierCode(orderDeliverySupplier.getSupplierCode());
                //orderDeliverySupplierForSave.setSupplierType(orderDeliverySupplier.getSupplierType());

                //保存物料
                orderDeliverySupplierForSave.setMaterialId(deliverySupplier.getMaterialId());
                orderDeliverySupplierForSave.setMaterialErpId(deliverySupplier.getMaterialErpId());
                orderDeliverySupplierForSave.setMaterialErpParentId(deliverySupplier.getMaterialErpParentId());
                orderDeliverySupplierForSave.setMaterialErpNumber(deliverySupplier.getMaterialErpNumber());
                orderDeliverySupplierForSave.setMaterialErpName(deliverySupplier.getMaterialErpName());
                orderDeliverySupplierForSave.setParentId(deliverySupplier.getParentId());
                orderDeliverySupplierForSave.setParentErpId(deliverySupplier.getParentErpId());
                orderDeliverySupplierForSave.setParentErpNumber(deliverySupplier.getParentErpNumber());
                orderDeliverySupplierForSave.setParentName(deliverySupplier.getParentName());

                saveList.add(orderDeliverySupplierForSave);
            }
        }
        this.saveBatch(saveList);
    }

    @Override
    public PageImpl<OrderDeliverySupplierVo> selectPage(IPage<OrderDeliverySupplierVo> page, QueryParam<OrderDeliverySupplierParam> queryParam) {
        /*List<OrderDeliverySupplierVo> orderDeliverySupplierVos = supplierMapper.selectPageList(page, queryParam);
        //设置供应商tag和type
        Optional.ofNullable(orderDeliverySupplierVos).ifPresent(supplierList->{
            supplierList.forEach(supplier->{
                //供应商标签
                List<SupplierItem> tagItems = supplierMapper.selectTagItemListBySupplierId(supplier.getSupplierId());
                supplier.setTagItems(tagItems);
                //供应商类型
                List<SupplierItem> typeItems = supplierMapper.selectTypeItemListBySupplierId(supplier.getSupplierId());
                supplier.setTypeItems(typeItems);
            });
        });
        //高查的供应商类型过滤
        if(StrUtil.isNotBlank(queryParam.getParam().getTypes())){
            String types = queryParam.getParam().getTypes();
            //List<String> typeList = Arrays.asList(split);

            List<OrderDeliverySupplierVo> orderDeliverySupplierVoReturnList = new ArrayList<>();
            for (OrderDeliverySupplierVo orderDeliverySupplierVo : orderDeliverySupplierVos) {

                List<SupplierItem> typeItems = orderDeliverySupplierVo.getTypeItems();
                for (SupplierItem typeItem : typeItems) {
                    if(typeItem.getItem().indexOf(types)>-1){
                        orderDeliverySupplierVoReturnList.add(orderDeliverySupplierVo);
                    }
                }

            }
            return PageUtils.result(page.setRecords(orderDeliverySupplierVoReturnList));

        }
        return PageUtils.result(page.setRecords(orderDeliverySupplierVos));*/
        /**
         * 时间紧急  手动分页  low一点就low一点先吧
         * */
        //高查的供应商类型过滤
        if(StrUtil.isNotBlank(queryParam.getParam().getTypes())){
            //查询所有
            List<OrderDeliverySupplierVo> orderDeliverySupplierVos = supplierMapper.selectNoPageList( queryParam);
            //设置供应商tag和type
            Optional.ofNullable(orderDeliverySupplierVos).ifPresent(supplierList->{
                supplierList.forEach(supplier->{
                    //供应商标签
                    List<SupplierItem> tagItems = supplierMapper.selectTagItemListBySupplierId(supplier.getSupplierId());
                    supplier.setTagItems(tagItems);
                    //供应商类型
                    List<SupplierItem> typeItems = supplierMapper.selectTypeItemListBySupplierId(supplier.getSupplierId());
                    supplier.setTypeItems(typeItems);
                });
            });



            String types = queryParam.getParam().getTypes();
            //List<String> typeList = Arrays.asList(split);

            List<OrderDeliverySupplierVo> orderDeliverySupplierVoReturnList = new ArrayList<>();
            for (OrderDeliverySupplierVo orderDeliverySupplierVo : orderDeliverySupplierVos) {

                List<SupplierItem> typeItems = orderDeliverySupplierVo.getTypeItems();
                for (SupplierItem typeItem : typeItems) {
                    if(typeItem.getItem().indexOf(types)>-1){
                        orderDeliverySupplierVoReturnList.add(orderDeliverySupplierVo);
                    }
                }

            }
            long current = page.getCurrent();
            long size = page.getSize();
            long count = orderDeliverySupplierVoReturnList.stream().count();
            page.setTotal(count);
            long pages = count / size;
            if (count % size!= 0) {
                pages++;
            }
            page.setPages(pages);

            //手动分页
            long endForLong = current * size - 1;
            long startForLong = (current * size - size)<0?0:current * size - size;
            int end=(int)endForLong;
            int start=(int)startForLong;
            List<OrderDeliverySupplierVo> orderDeliverySupplierVoFilterReturnList = new ArrayList<>();
            int listSize = orderDeliverySupplierVoReturnList.size();
            for (int i = start; i <= end; i++) {
                if(i<listSize){
                    orderDeliverySupplierVoFilterReturnList.add(orderDeliverySupplierVoReturnList.get(i));

                }
            }

            return PageUtils.result(page.setRecords(orderDeliverySupplierVoFilterReturnList));

        }else{
            //查询分页
            List<OrderDeliverySupplierVo> orderDeliverySupplierVos = supplierMapper.selectPageList(page, queryParam);
            //设置供应商tag和type
            Optional.ofNullable(orderDeliverySupplierVos).ifPresent(supplierList->{
                supplierList.forEach(supplier->{
                    //供应商标签
                    List<SupplierItem> tagItems = supplierMapper.selectTagItemListBySupplierId(supplier.getSupplierId());
                    supplier.setTagItems(tagItems);
                    //供应商类型
                    List<SupplierItem> typeItems = supplierMapper.selectTypeItemListBySupplierId(supplier.getSupplierId());
                    supplier.setTypeItems(typeItems);
                });
            });
            return PageUtils.result(page.setRecords(orderDeliverySupplierVos));
        }

    }

    @Override
    public Boolean isOrderDeliverySupplier() {
        AtomicReference<Boolean> flag= new AtomicReference<>(false);
        SecurityUtils.getCurrentSupplier().ifPresent(supplier -> {
            List<OrderDeliverySupplier> suppliers = this.list(Wrappers.<OrderDeliverySupplier>lambdaQuery()
                    .eq(OrderDeliverySupplier::getSupplierId, supplier.getSupplierId()));
                    //.eq(OrderDeliverySupplier::getStatus, true));
            if (CollectionUtil.isNotEmpty(suppliers)){
                flag.set(true);
            }
        });
        return flag.get();
    }
}
