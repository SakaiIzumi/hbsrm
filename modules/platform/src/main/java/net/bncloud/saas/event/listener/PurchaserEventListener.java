package net.bncloud.saas.event.listener;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.saas.event.*;
import net.bncloud.saas.purchaser.domain.Purchaser;
import net.bncloud.saas.purchaser.service.PurchaserQueryService;
import net.bncloud.saas.purchaser.service.PurchaserService;
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.service.SupplierService;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.service.OrganizationService;
import net.bncloud.serivce.api.order.feign.OrderSych;
import net.bncloud.serivce.api.order.feign.ZcOrderServiceFeignClient;
import net.bncloud.service.api.delivery.dto.SyncOrgIdParams;
import net.bncloud.service.api.delivery.feign.DeliveryPlanFeignClient;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class PurchaserEventListener {
    private final PurchaserService purchaserService;
    private final PurchaserQueryService purchaserQueryService;
    private final SupplierService supplierService;
    private final OrganizationService organizationService;
    private final DeliveryPlanFeignClient deliveryPlanFeignClient;
    private final ZcOrderServiceFeignClient zcOrderServiceFeignClient;

    @EventListener(PurchaserCreatedEvent.class)
    public void listenerPurchaserCreatedEvent(PurchaserCreatedEvent purchaserCreatedEvent) {
        CreatePurchaser createPurchaser = purchaserCreatedEvent.getCreatePurchaser();
        Long orgId = createPurchaser.getOrgId();
        Organization organization = organizationService.findById(orgId).orElseThrow(() -> new ApiException(404, "无法找到对应实体"));
        Purchaser purchaser = Purchaser
                .builder()
                .code(createPurchaser.getCode())
                .name(createPurchaser.getName())
                .artificialPerson(createPurchaser.getArtificialPerson())
                .enabled(createPurchaser.getEnabled())
                .description(createPurchaser.getDescription())
                .organization(organization)
                .build();
        purchaserService.save(purchaser);

    }


    @EventListener(PurchaserUpdatedEvent.class)
    public void listenerPurchaserUpdateEvent(PurchaserUpdatedEvent purchaserUpdatedEvent) {
        UpdatePurchaser updatePurchaser = purchaserUpdatedEvent.getUpdatePurchaser();
        Long id = updatePurchaser.getId();
        String code = updatePurchaser.getCode();
        Purchaser purchaser = purchaserQueryService.getOne(id);
        Purchaser purchaserGetByCode = purchaserQueryService.findByCode(code);
        if (purchaserGetByCode != null && !purchaserGetByCode.getId().equals(id)) {
            throw new IllegalArgumentException("采购方编码已被使用");
        }
        Organization organization = purchaser.getOrganization();
        purchaser.setCode(code);
        purchaser.setName(updatePurchaser.getName());
        purchaser.setArtificialPerson(updatePurchaser.getArtificialPerson());
        purchaser.setDescription(updatePurchaser.getDescription());
        purchaser.setEnabled(updatePurchaser.getEnabled());
        purchaserService.save(purchaser);
    }

    @EventListener(PurchaserBoundEvent.class)
    public void purchaserBoundEvent(PurchaserBoundEvent event) {
        BindPurchaser bindPurchaser = event.getBindPurchaser();
        List<Long> supplierIds = bindPurchaser.getSupplierIds();
        Long orgId = bindPurchaser.getOrgId();
        Long id = bindPurchaser.getId();

        Purchaser purchaser = purchaserService.getOne(id);
        Organization organization = organizationService.findById(orgId).orElseThrow(() -> new ApiException(404, "无法找到对应实体"));
        if (CollectionUtil.isNotEmpty(supplierIds)) {
            List<Supplier> suppliers = supplierService.findAllById(supplierIds);
            suppliers.forEach(supplier -> supplier.setOrganizations(Lists.newArrayList(organization)));
            supplierService.saveAll(suppliers);
            purchaser.getSuppliers().addAll(suppliers);
            purchaserService.save(purchaser);
            // 远程调用接口
            SyncOrgIdParams syncOrgIdParams = SyncOrgIdParams.of(purchaser.getCode(), orgId, suppliers.stream().map(Supplier::getCode).collect(Collectors.toList()));
            syncOrgIdParams.setPurchaseName( purchaser.getName() );

            Map<String, String> codeNameMap = suppliers.stream().collect(Collectors.toMap(Supplier::getCode, Supplier::getName));
            syncOrgIdParams.setSupplierCodeNameMap( codeNameMap );

            updateOrgId( syncOrgIdParams );
        }

    }

    @EventListener(PurchaserCancelBoundEvent.class)
    public void purchaserCancelBoundEvent(PurchaserCancelBoundEvent event) {
        BindPurchaser bindPurchaser = event.getBindPurchaser();
        List<Long> supplierIds = bindPurchaser.getSupplierIds();
        Long orgId = bindPurchaser.getOrgId();
        Long id = bindPurchaser.getId();

        Purchaser purchaser = purchaserService.findById(id);
        Organization organization = organizationService.findById(orgId).orElseThrow(() -> new ApiException(404, "无法找到对应实体"));
        //产品又改需求!!!!!!!!!!
        if (CollectionUtil.isNotEmpty(supplierIds)) {
            purchaserService.cancelBound(purchaser.getId(), supplierIds);
        }
    }


    @EventListener(PurchaserAllBoundEvent.class)
    public void purchaserAllBoundEvent(PurchaserAllBoundEvent event) {
        AllBindPurchaser allBindPurchaser = event.getAllBindPurchaser();
        Long orgId = allBindPurchaser.getOrgId();
        Long id = allBindPurchaser.getId();
        Purchaser purchaser = purchaserService.getOne(id);
        Organization organization = organizationService.findById(orgId).orElseThrow(() -> new ApiException(404, "无法找到对应实体"));
        List<Supplier> suppliers = supplierService.findAll();
        if (CollectionUtil.isNotEmpty(suppliers)) {
            purchaserService.cancelBound(purchaser.getId(), suppliers.stream().map(Supplier::getId).collect(Collectors.toList()));
            suppliers.forEach(supplier ->supplier.setOrganizations(Lists.newArrayList(organization)));
            purchaser.setSuppliers(suppliers);
            purchaserService.save(purchaser);

            // 远程调用接口
            SyncOrgIdParams syncOrgIdParams = SyncOrgIdParams.of(purchaser.getCode(), orgId, suppliers.stream().map(Supplier::getCode).collect(Collectors.toList()));
            syncOrgIdParams.setPurchaseName( purchaser.getName() );

            Map<String, String> codeNameMap = suppliers.stream().collect(Collectors.toMap(Supplier::getCode, Supplier::getName));
            syncOrgIdParams.setSupplierCodeNameMap( codeNameMap );

            updateOrgId( syncOrgIdParams );
        }
    }


    /**
     * 远程调用
     *
     * @param syncOrgIdParams
     */
    public void updateOrgId(SyncOrgIdParams syncOrgIdParams) {

        log.info("updateOrgId: params:{}", JSON.toJSONString(syncOrgIdParams) );

        try {
            R<Object> objectR = deliveryPlanFeignClient.updateDeliveryPlanOrgId(syncOrgIdParams);
            if (! objectR.isSuccess()) {
                log.error("远程调用Delivery服务失败！更新组织id：{}",objectR.getMsg());
            }
        } catch (Exception e) {
            log.error("远程服务delivery 调用失败",e);
        }

        try {
            OrderSych orderSych = new OrderSych();
            orderSych.setOrgId( syncOrgIdParams.getOrgId() );
            orderSych.setPurchaseCode( syncOrgIdParams.getPurchaseCode() );
            orderSych.setPurchaseName( syncOrgIdParams.getPurchaseName() );
            orderSych.setSupplierCode( syncOrgIdParams.getSupplierCodeList() );
            orderSych.setSupplierCodeNameMap( syncOrgIdParams.getSupplierCodeNameMap() );
            R r = zcOrderServiceFeignClient.syncOrder(orderSych);
            if (! r.isSuccess()) {
                log.error("远程调用订单服务失败！更新组织id：{}",r.getMsg());
            }
        }catch (Exception ex){
            log.error("远程调用订单服务失败！",ex);
        }

    }



}
