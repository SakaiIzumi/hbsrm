package net.bncloud.bis.manager;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.model.enums.PurchaseReceiveBillFBusinessTypeEnum;
import net.bncloud.bis.model.erp.PurchaseReceiveBillCreateOrder;
import net.bncloud.bis.service.api.dto.PurchaseReceiveBillCreateOrderDto;
import net.bncloud.bis.service.api.vo.PurchaseReceiveBillCallCreateVo;
import net.bncloud.bis.service.api.vo.PurchaseReceiveBillEntryCallCreateVo;
import net.bncloud.common.api.R;
import net.bncloud.msk3cloud.constant.formid.SupplierChainConstants;
import net.bncloud.msk3cloud.core.condition.SaveCondition;
import net.bncloud.msk3cloud.core.params.K3CloudSaveParam;
import net.bncloud.msk3cloud.kingdee.K3cloudRemoteService;
import net.bncloud.msk3cloud.kingdee.entity.common.Number;
import net.bncloud.msk3cloud.util.FieldKeyAnoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * desc: 收料通知单
 *
 * @author Rao
 * @Date 2022/01/21
 **/
@Component
@Slf4j
public class PurchaseReceiveBillOrderManager {

    @Autowired
    private K3cloudRemoteService k3cloudRemoteService;

    /**
     * 创建收料通知单
     * @param purchaseReceiveBillCreateOrderDto
     * @return
     */
    public R<PurchaseReceiveBillCallCreateVo> createReceiveBillOrder(PurchaseReceiveBillCreateOrderDto purchaseReceiveBillCreateOrderDto){

        PurchaseReceiveBillCreateOrder purchaseReceiveBillCreateOrder = new PurchaseReceiveBillCreateOrder();
        purchaseReceiveBillCreateOrder.setFId( purchaseReceiveBillCreateOrderDto.getFid() );
        purchaseReceiveBillCreateOrder.setFBillTypeId( new Number( purchaseReceiveBillCreateOrderDto.getFBillTypeId().getFNumber() ) );
        purchaseReceiveBillCreateOrder.setFDate( purchaseReceiveBillCreateOrderDto.getFDate() );
        purchaseReceiveBillCreateOrder.setFPurOrgId( new Number( purchaseReceiveBillCreateOrderDto.getFPurOrgId().getFNumber() ) );
        purchaseReceiveBillCreateOrder.setFSupplierId( new Number( purchaseReceiveBillCreateOrderDto.getFSupplierId().getFNumber() ) );
        purchaseReceiveBillCreateOrder.setFOwnerIdHead( new Number( purchaseReceiveBillCreateOrderDto.getFOwnerIdHead().getFNumber() ) );
        purchaseReceiveBillCreateOrder.setFMsWlfl( new Number(purchaseReceiveBillCreateOrderDto.getFMsWlfl().getFNumber()) );
        purchaseReceiveBillCreateOrder.setFBusinessType( PurchaseReceiveBillFBusinessTypeEnum.billType2BusinessTypeMap.getOrDefault( purchaseReceiveBillCreateOrder.getFBillTypeId().getFNumber(), PurchaseReceiveBillFBusinessTypeEnum.standard.getRc_business_type() ) );
        purchaseReceiveBillCreateOrder.setFStockOrgId( new Number( purchaseReceiveBillCreateOrderDto.getFStockOrgId().getFNumber() ));
        purchaseReceiveBillCreateOrder.setFDemandOrgId( new Number( purchaseReceiveBillCreateOrderDto.getFDemandOrgId().getFNumber() ) );

        // 处理业务类型相关的字段
        List<PurchaseReceiveBillCreateOrder.FDetailEntity> fDetailEntityList = purchaseReceiveBillCreateOrderDto.getFDetailEntity().stream().map(detailEntityDto -> {

            PurchaseReceiveBillCreateOrder.FDetailEntity detailEntity = new PurchaseReceiveBillCreateOrder.FDetailEntity();
            detailEntity.setFEntryId( detailEntityDto.getFEntryId() );
            detailEntity.setFMaterialId(new Number(detailEntityDto.getFMaterialId().getFNumber()));
            detailEntity.setFUnitId(new Number(detailEntityDto.getFUnitId().getFNumber()));
            detailEntity.setFActReceiveQty(detailEntityDto.getFActReceiveQty());
            detailEntity.setFPreDeliveryDate(detailEntityDto.getFPreDeliveryDate());
            detailEntity.setFsupdelqty(detailEntityDto.getFsupdelqty());
            detailEntity.setFPriceUnitId(new Number(detailEntityDto.getFPriceUnitId().getFNumber()));
            detailEntity.setFStockId(new Number(detailEntityDto.getFStockId().getFNumber()));

            // 业务类型相关字段
            detailEntity.setFSrcBillNo(detailEntityDto.getFSrcBillNo());
            detailEntity.setFSrcFormId(detailEntityDto.getFSrcFormId());
            detailEntity.setFOrderBillNo(detailEntityDto.getFSrcBillNo());
            // 理论只有一条 所以
            List<PurchaseReceiveBillCreateOrder.FDetailEntityLink> fDetailEntityLinkList = detailEntityDto.getFDetailEntityLinkList().stream().map(detailEntityLinkDto -> {

                detailEntity.setFSrcId(detailEntityLinkDto.getFDetailEntity_Link_FSBillId());
                detailEntity.setFSrcEntryId(detailEntityLinkDto.getFDetailEntity_Link_FSId());
                detailEntity.setFPOORDERENTRYID(detailEntityLinkDto.getFDetailEntity_Link_FSId());

                PurchaseReceiveBillCreateOrder.FDetailEntityLink fDetailEntityLink = new PurchaseReceiveBillCreateOrder.FDetailEntityLink();
                fDetailEntityLink.setFDetailEntity_Link_FSBillId(detailEntityLinkDto.getFDetailEntity_Link_FSBillId());
                fDetailEntityLink.setFDetailEntity_Link_FSId(detailEntityLinkDto.getFDetailEntity_Link_FSId());
                fDetailEntityLink.setFDetailEntity_Link_FBaseUnitQtyOld(detailEntityLinkDto.getFDetailEntity_Link_FBaseUnitQtyOld());
                fDetailEntityLink.setFDetailEntity_Link_FBaseUnitQty(detailEntityLinkDto.getFDetailEntity_Link_FBaseUnitQty());
                return fDetailEntityLink;

            }).collect(Collectors.toList());

            detailEntity.setFDetailEntityLinkList(fDetailEntityLinkList);

            detailEntity.setFPriceBaseQty(detailEntityDto.getFPriceBaseQty());
            detailEntity.setFStockUnitId(new Number(detailEntityDto.getFStockUnitId().getFNumber()));
            detailEntity.setFStockQty(detailEntityDto.getFStockQty());
            detailEntity.setFActlandQty(detailEntityDto.getFActlandQty());
            detailEntity.setFPrice(detailEntityDto.getFPrice());
            detailEntity.setFTaxPrice(detailEntityDto.getFTaxPrice());

            // 处理仓位编码
            if( detailEntityDto.getStockLocId() != null ){
                if( detailEntityDto.getStockLocId().getFstocklocidFf100002() != null){
                    PurchaseReceiveBillCreateOrder.FStockLocId fStockLocId = new PurchaseReceiveBillCreateOrder.FStockLocId();
                    fStockLocId.setFstocklocidFf100002( new Number( detailEntityDto.getStockLocId().getFstocklocidFf100002().getFNumber() ));
                    detailEntity.setStockLocId( fStockLocId );
                }

            }

            return detailEntity;
        }).collect(Collectors.toList());

        purchaseReceiveBillCreateOrder.setFDetailEntity( fDetailEntityList );

        K3CloudSaveParam<PurchaseReceiveBillCreateOrder> k3CloudSaveParam = SaveCondition.build(purchaseReceiveBillCreateOrder)
                .needReturnField(FieldKeyAnoUtils.getFieldKeyList(PurchaseReceiveBillCreateOrder.class))
                .needReturnField(FieldKeyAnoUtils.getFieldKeyList(PurchaseReceiveBillCreateOrder.FDetailEntity.class))
                .setAutoSubmitAndAudit(true)
                .k3CloudSaveParam();
        try {
            PurchaseReceiveBillCreateOrder receiveBillCreateOrder = k3cloudRemoteService.saveOrUpdateWithReturnObj(SupplierChainConstants.PUR_RECEIVE_BILL, k3CloudSaveParam, PurchaseReceiveBillCreateOrder.class);
            PurchaseReceiveBillCallCreateVo purchaseReceiveBillCallCreateVo = new PurchaseReceiveBillCallCreateVo();
            purchaseReceiveBillCallCreateVo.setFId( receiveBillCreateOrder.getFId() );
            purchaseReceiveBillCallCreateVo.setFNumber( receiveBillCreateOrder.getFBillNo() );

            List<PurchaseReceiveBillEntryCallCreateVo> purchaseReceiveBillEntryCallCreateVoList = new ArrayList<>();
            List<PurchaseReceiveBillCreateOrderDto.FDetailEntity> createDetailEntityList = purchaseReceiveBillCreateOrderDto.getFDetailEntity();
            List<PurchaseReceiveBillCreateOrder.FDetailEntity> returnDetailEntityList = receiveBillCreateOrder.getFDetailEntity();
            for (int i = 0; i < createDetailEntityList.size(); i++) {
                PurchaseReceiveBillEntryCallCreateVo purchaseReceiveBillEntryCallCreateVo = new PurchaseReceiveBillEntryCallCreateVo();

                PurchaseReceiveBillCreateOrderDto.FDetailEntity fDetailEntity = createDetailEntityList.get(i);
                PurchaseReceiveBillCreateOrder.FDetailEntity detailEntity = returnDetailEntityList.get(i);

                purchaseReceiveBillEntryCallCreateVo.setSrmId( fDetailEntity.getSrmId() );
                purchaseReceiveBillEntryCallCreateVo.setErpId( detailEntity.getFEntryId() );

                purchaseReceiveBillEntryCallCreateVoList.add(  purchaseReceiveBillEntryCallCreateVo);
            }
            purchaseReceiveBillCallCreateVo.setPurchaseReceiveBillEntryCallCreateVoList( purchaseReceiveBillEntryCallCreateVoList );
            return R.data(purchaseReceiveBillCallCreateVo);

        } catch (Exception exception) {
            log.error("[PurchaseReceiveBillOrder] createReceiveBillOrder error!",exception);
            return R.fail(exception.getMessage());
        }

    }

}
