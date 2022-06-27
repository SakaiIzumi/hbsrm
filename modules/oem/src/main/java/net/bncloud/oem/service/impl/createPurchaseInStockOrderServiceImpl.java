package net.bncloud.oem.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.bis.service.api.feign.PurchaseInStockOrderFeignClient;
import net.bncloud.bis.service.api.vo.Number;
import net.bncloud.bis.service.api.vo.PurchaseInStockOrderCreateVo;
import net.bncloud.common.api.R;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.DateUtil;
import net.bncloud.oem.domain.entity.PurchaseOrder;
import net.bncloud.oem.domain.entity.PurchaseOrderMaterial;
import net.bncloud.oem.domain.entity.PurchaseOrderReceiving;
import net.bncloud.oem.domain.vo.PurchaseOrderMaterialVo;
import net.bncloud.oem.domain.vo.PurchaseOrderReceivingVo;
import net.bncloud.oem.domain.vo.PurchaseOrderVo;
import net.bncloud.oem.enums.BillTypeEnum;
import net.bncloud.oem.mapper.PurchaseOrderMapper;
import net.bncloud.oem.mapper.PurchaseOrderMaterialMapper;
import net.bncloud.oem.mapper.PurchaseOrderReceivingMapper;
import net.bncloud.oem.service.CreatePurchaseInStockOrderService;
import net.bncloud.quotation.service.api.dto.MaterialInfoDTO;
import net.bncloud.quotation.service.api.dto.MaterialInfoDetailDTO;
import net.bncloud.quotation.service.api.feign.MaterialFeignClient;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author liyh
 * @description 订单同步erp服务
 * @since 2022/4/26
 */
@Service
@Slf4j
public class createPurchaseInStockOrderServiceImpl extends CreateStockOrderServiceImpl  implements CreatePurchaseInStockOrderService {

    @Autowired
    private PurchaseInStockOrderFeignClient purchaseInStockOrderFeignClient;
    @Autowired
    private MaterialFeignClient materialFeignClient;
    @Resource
    private PurchaseOrderReceivingMapper purchaseOrderReceivingMapper;
    @Autowired
    private PurchaseOrderMaterialMapper purchaseOrderMaterialMapper;
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    /**
     *
     * @description 回写采购入库单(单条)
     */
    @Override
    public void createPurchaseInStockOrder(Long id) {
        log.info("=====开始回写erp(单条回写)=====");
        //收货明细
        PurchaseOrderReceiving purchaseOrderReceiving = purchaseOrderReceivingMapper.selectById(id);
        log.info("=====根据明细id查询明细为:{}", JSON.toJSONString(purchaseOrderReceiving));

        //物料
        LambdaQueryWrapper<PurchaseOrderMaterial> purchaseOrderMaterialQuery = Condition.getQueryWrapper(new PurchaseOrderMaterial())
                .lambda()
                .eq(PurchaseOrderMaterial::getId, purchaseOrderReceiving.getPurchaseOrderMaterialId());
        PurchaseOrderMaterial purchaseOrderMaterial = purchaseOrderMaterialMapper.selectOne(purchaseOrderMaterialQuery);
        log.info("=====根据收货明细查询的物料为:{}", JSON.toJSONString(purchaseOrderMaterial));

        //采购订单
        LambdaQueryWrapper<PurchaseOrder> purchaseOrderQuery = Condition.getQueryWrapper(new PurchaseOrder())
                .lambda()
                .eq(PurchaseOrder::getId, purchaseOrderMaterial.getPurchaseOrderId());
        PurchaseOrder purchaseOrder = purchaseOrderMapper.selectOne(purchaseOrderQuery);
        log.info("=====根据收货物料查询的采购订单为:{}", JSON.toJSONString(purchaseOrder));

        //构建vo对象
        List<PurchaseOrderReceivingVo> PurchaseOrderReceivingList=new ArrayList();
        PurchaseOrderReceivingVo purchaseOrderReceivingVo = BeanUtil.copy(purchaseOrderReceiving, PurchaseOrderReceivingVo.class);
        PurchaseOrderMaterialVo purchaseOrderMaterialVo = BeanUtil.copy(purchaseOrderMaterial, PurchaseOrderMaterialVo.class);
        PurchaseOrderVo purchaseOrderVo = BeanUtil.copy(purchaseOrder, PurchaseOrderVo.class);
        PurchaseOrderReceivingList.add(purchaseOrderReceivingVo);
        log.info("=====前置查询成功!开始构建回写对象,构建参数为:{}",JSON.toJSONString(PurchaseOrderReceivingList)+JSON.toJSONString(purchaseOrderMaterialVo)+JSON.toJSONString(purchaseOrderVo));
        PurchaseInStockOrderCreateVo purchaseInStockOrderCreateVo = buildVo(PurchaseOrderReceivingList, purchaseOrderMaterialVo, purchaseOrderVo);


        //调用方法
        R<String> purchaseInStockOrderData = purchaseInStockOrderFeignClient.createPurchaseInStockOrder(purchaseInStockOrderCreateVo);

        //返回参数同步srm数据库
        Asserts.isTrue(purchaseInStockOrderData.isSuccess(),"回写erp采购入库单失败,出现异常"+purchaseInStockOrderData.getMsg());
        Asserts.notNull(purchaseInStockOrderData.getData(),"回写erp采购入库单失败==========没有数据返回");
        returnsWrites( purchaseInStockOrderData.getData(),purchaseOrderReceivingVo,purchaseOrderVo);
        log.info("=====采购入库单创建成功!=====");
    }

    /**
     *
     * @description 回写srm数据库
     */
    private void returnsWrites(String fid,PurchaseOrderReceivingVo purchaseOrderReceiving,PurchaseOrderVo purchaseOrder){
        log.info("创建入库单成功,开始回写srm数据库");

        Asserts.notNull(fid,"fid为空");
        //String fid = data.getFid();
//        purchaseOrder.setFid(fid);
//        purchaseOrderMapper.updateById(purchaseOrder);

        //Asserts.notNull(data.getFInStockEntryList(),"返回的明细列表为空");
        //List<PurchaseInStockOrderCreateVo.FInStockEntry> fInStockEntryList = data.getFInStockEntryList();

        log.info("======开始回写srm明细=====");
        //for (PurchaseInStockOrderCreateVo.FInStockEntry fInStockEntry : fInStockEntryList) {
            //log.info("本次回写明细为:{}",JSON.toJSONString(fInStockEntry));

            //Asserts.notNull(fInStockEntry.getFEntryId(),"明细中的entryId为空");
            //String fEntryId = fInStockEntry.getFEntryId();
            purchaseOrderReceiving.setFEntryId(fid);
            purchaseOrderReceivingMapper.updateById(purchaseOrderReceiving);
        //}
    }

    /**
     *
     * @description 批量回写srm数据库
     * 默认美尚那边的erp是按顺序回写的,如果有问题,问产品和提bug给美尚
     */
    private void batchReturnsWrites(List<String> ids,List<PurchaseOrderReceivingVo> purchaseOrderReceivingVoList){
        //List<PurchaseOrderReceiving> purchaseOrderReceivingList = new ArrayList<>();
        for (int i = 0; i < purchaseOrderReceivingVoList.size(); i++) {
            PurchaseOrderReceiving receive = BeanUtil.copy(purchaseOrderReceivingVoList.get(i), PurchaseOrderReceiving.class);
            receive.setFEntryId(ids.get(i));
            purchaseOrderReceivingMapper.updateById(receive);
        }
        //List<PurchaseOrder> purchaseOrderList=new ArrayList();

        /*for (int i =0;i<purchaseOrderVoList.size();i++) {
            PurchaseInStockOrderCreateVo purchaseInStockOrderCreateVo = data.get(i);
            //设置采购订单回写的fid
            PurchaseOrder purchaseOrder = new PurchaseOrder();
            purchaseOrder.setId(purchaseOrderVoList.get(i).getId());
            purchaseOrder.setFid(purchaseInStockOrderCreateVo.getFid());
            purchaseOrderList.add(purchaseOrder);
            //明细id
            List<PurchaseInStockOrderCreateVo.FInStockEntry> fInStockEntryList = purchaseInStockOrderCreateVo.getFInStockEntryList();
            List<PurchaseOrderReceivingVo> receivingChildrenList = purchaseOrderVoList.get(i).getReceivingChildren();
            for (int j =0;j<receivingChildrenList.size();j++) {
                receivingChildrenList.get(j).setFEntryId(fInStockEntryList.get(j).getFEntryId());
                PurchaseOrderReceiving purchaseOrderReceiving = new PurchaseOrderReceiving();
                purchaseOrderReceiving.setId(receivingChildrenList.get(j).getId());
                purchaseOrderReceiving.setFEntryId(fInStockEntryList.get(j).getFEntryId());
                purchaseOrderReceivingList.add(purchaseOrderReceiving);
            }
        }*/

        //批量更新订单fid
        /*try (SqlSession batchSqlSession = sqlSessionBatchOrder()) {

            for (PurchaseOrder purchaseOrder : purchaseOrderList) {
                MapperMethod.ParamMap<PurchaseOrder> param = new MapperMethod.ParamMap<>();
                param.put(Constants.ENTITY, purchaseOrder);
                batchSqlSession.update(sqlStatement(SqlMethod.UPDATE_BY_ID), param);
            }

            batchSqlSession.flushStatements();
        }*/

        //批量更新明细fid
        /*try (SqlSession batchSqlSession = sqlSessionBatchReceiving()) {

            for (PurchaseOrderReceiving purchaseOrder : purchaseOrderReceivingList) {
                MapperMethod.ParamMap<PurchaseOrderReceiving> param = new MapperMethod.ParamMap<>();
                param.put(Constants.ENTITY, purchaseOrder);
                batchSqlSession.update(sqlStatementReceiving(SqlMethod.UPDATE_BY_ID), param);
            }
            batchSqlSession.flushStatements();
        }*/
    }

    /**
     *  批量创建采购入库单
     *
     */
    @Override
    public void batchCreateStockOrder(List<PurchaseInStockOrderCreateVo> purchaseInStockOrderCreateVo,List<PurchaseOrderReceivingVo> purchaseOrderReceivingVoList) {
        R<List<String>> listR = purchaseInStockOrderFeignClient.batchCreatePurchaseInStockOrder(purchaseInStockOrderCreateVo);
        //返回参数同步srm数据库
        Asserts.isTrue(listR.isSuccess(),"回写erp采购入库单失败,出现异常");
        Asserts.notNull(listR.getData(),"回写erp采购入库单失败==========没有数据返回");
        //保存erp回传的id
        //批量回写srm数据库
        //默认美尚那边的erp是按顺序回写的,如果有问题,问产品和提bug给美尚
        List<String> data = listR.getData();
        batchReturnsWrites( data,purchaseOrderReceivingVoList);
    }

    /**
     *  批量创建采购入库单
     *
     */
    @Override
    public R<PurchaseInStockOrderCreateVo> batchCreateStockOrderV2(PurchaseInStockOrderCreateVo purchaseInStockOrderCreateVo,PurchaseOrderReceivingVo purchaseOrderReceiving,PurchaseOrderVo purchaseOrder) {
        R<String> purchaseInStockOrderData = purchaseInStockOrderFeignClient.createPurchaseInStockOrder(purchaseInStockOrderCreateVo);
        //返回参数同步srm数据库
        Asserts.isTrue(purchaseInStockOrderData.isSuccess(),"回写erp采购入库单失败,出现异常");
        Asserts.notNull(purchaseInStockOrderData.getData(),"回写erp采购入库单失败==========没有数据返回");
        //保存erp回传的id
        //returnsWrites( purchaseInStockOrderData.getData(),purchaseOrderReceiving,purchaseOrder);

        return null;
    }

    /**
     * @description 构建vo对象
     */
    public PurchaseInStockOrderCreateVo buildVo(List<PurchaseOrderReceivingVo> PurchaseOrderReceivingList, PurchaseOrderMaterialVo purchaseOrderMaterial, PurchaseOrderVo purchaseOrder ){
        //构建vo对象
        PurchaseInStockOrderCreateVo purchaseInStockOrderCreateVo = new PurchaseInStockOrderCreateVo();

        //采购单号(美尚同步) 同步过来的单据编号 //todo********
        Asserts.notNull(purchaseOrder.getPurchaseOrderCode(),"采购单号(美尚同步) 为空");
        String orderType = purchaseOrder.getOrderType();
        String changeOrderType=StrUtil.equals(orderType, BillTypeEnum.STANDARD.getCode())?BillTypeEnum.STANDARD.getChangeCode():BillTypeEnum.OUTSOURCING.getChangeCode();

        purchaseInStockOrderCreateVo.setFBillTypeId(new Number(changeOrderType));

        //明细填写的收货时间//todo******** 多条明细写哪一个时间
        Asserts.notNull(purchaseOrder.getConfirmDate(),"明细填写的收货时间 为空");
        purchaseInStockOrderCreateVo.setFDate( purchaseOrder.getConfirmDate().toLocalDate().toString()  );

        //收料组织 = oem供应商编码//todo********
        Asserts.notNull(purchaseOrder.getOemSupplierCode(),"收料组织 = oem供应商编码 为空");
        purchaseInStockOrderCreateVo.setFStockOrgId(new Number(purchaseOrder.getPurchaseCode()));

        //采购组织 = 采购方编码//todo********
        Asserts.notNull(purchaseOrder.getPurchaseCode(),"采购方编码 为空");
        purchaseInStockOrderCreateVo.setFPurchaseOrgId(new Number(purchaseOrder.getPurchaseCode()));

        //远程调用物料接口
        Asserts.notNull(purchaseOrderMaterial.getMaterialCode(),"物料编码 为空");
        R<MaterialInfoDetailDTO> materialDetailByCode = materialFeignClient.getMaterialDetailByCode(purchaseOrderMaterial.getMaterialCode());
        Asserts.isTrue(materialDetailByCode.isSuccess(),"远程调用物料明细出现异常");
        Asserts.notNull(materialDetailByCode.getData(),"远程调用物料明细为空");
        MaterialInfoDetailDTO materialInfoDTO = materialDetailByCode.getData();

        //设置物料分类 todo******** 是genreCode还是name
        Asserts.notNull(materialInfoDTO.getGenreCode(),"物料分类为空");
        purchaseInStockOrderCreateVo.setFMsWlfl(new Number(materialInfoDTO.getGenreCode()));
        //purchaseInStockOrderCreateVo.setF_MS_WLFL(materialInfoDTO.getGenreCode());//todo******** 是genreCode还是name


        //物料明细
        List<PurchaseInStockOrderCreateVo.FInStockEntry> fInStockEntryList = new ArrayList<>();

        for (PurchaseOrderReceivingVo purchaseOrderReceivingVo : PurchaseOrderReceivingList) {
            PurchaseInStockOrderCreateVo.FInStockEntry fInStockEntry = new PurchaseInStockOrderCreateVo.FInStockEntry();

            //物料编码
            fInStockEntry.setFMaterialId(new Number(purchaseOrderMaterial.getMaterialCode()));

            //库存单位   //todo********
            Asserts.notNull(materialInfoDTO.getUnit(),"库存/计价/采购单位 为空");
            fInStockEntry.setFUnitId(new Number(materialInfoDTO.getUnit()));
            //计价单位//todo********
            fInStockEntry.setFPriceUnitId(new Number(materialInfoDTO.getUnit()));
            //采购单位//todo********
            fInStockEntry.setFRemainInStockUnitId(new Number(materialInfoDTO.getUnit()));


            //货主=采购编码//todo********
            Asserts.notNull(purchaseOrder.getPurchaseCode(),"货主=采购编码 为空");
            fInStockEntry.setFownerid(new Number(purchaseOrder.getPurchaseCode()));

            //货主类型 = 采购编码//todo********
            fInStockEntry.setFownertypeid(purchaseOrder.getPurchaseCode());

            //货主类型写死  测试回写//todo********
            fInStockEntry.setFownertypeid("BD_OwnerOrg");

            //供应商订单号 = 采购单号(美尚同步)//todo********
            Asserts.notNull(purchaseOrder.getPurchaseOrderCode(),"供应商订单号 = 采购单号(美尚同步) 为空");
            fInStockEntry.setFQhSupplyno(purchaseOrder.getPurchaseOrderCode());

            //实收数量参数缺失！=本批次收货数量//todo********
            Asserts.notNull(purchaseOrderReceivingVo.getReceiveQuantity(),"实收数量参数缺失！=本批次收货数量 为空");
            fInStockEntry.setFRealQty(purchaseOrderReceivingVo.getReceiveQuantity().toString());

            //入库仓/仓库  todo
            Asserts.notNull(purchaseOrderMaterial.getWarehouse(),"入库仓/仓库 为空");
            fInStockEntry.setFStockID(new Number(purchaseOrderMaterial.getWarehouse()));

            //批号== 生产批次号 还是 收货批次号??//todo********
            //fInStockEntry.setFLot(new Number(purchaseOrderReceivingVo.getManufactureBatchNo()));

            //供应商编码
            Asserts.notNull(purchaseOrder.getSupplierCode(),"供应商编码 为空");
            fInStockEntry.setFSupplierId(new Number(purchaseOrder.getSupplierCode()));
            //fInStockEntryList.add(fInStockEntry);

            //含税单价
            Asserts.notNull(purchaseOrderMaterial.getTaxPrice(),"单价 为空");

            fInStockEntry.setFTaxPrice(new BigDecimal(purchaseOrderMaterial.getTaxPrice()));
            fInStockEntryList.add(fInStockEntry);
        }


        purchaseInStockOrderCreateVo.setFInStockEntryList(fInStockEntryList);

        log.info("构建回写vo对象成功,vo值为:{}",JSON.toJSONString(purchaseInStockOrderCreateVo));
        return purchaseInStockOrderCreateVo;
    }

}
