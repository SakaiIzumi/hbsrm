# 金蝶ERP对接SDK
1、封装配置信息，自定义配置类登录
2、封装查询转换
3、接口示例

```text
1、QueryParam 使用
 参考 PurchaseOrderVo.class  字段需要使用 @JSONField (因为我使用的是 fastjson来做解析)
 查询  FEntryID 需要 使用 对象_FEntryID  ,eg:  FPOOrderEntry_FEntryID
 查询的字段有 属于Number类下的 则 需要使用 字段.fnumber  fnumber 不关心大小写
```
```java

        Class<PurchaseOrderVo> purchaseOrderVoClass = PurchaseOrderVo.class;
        List<String> fieldKeyList = FieldKeyAnoUtils.getFieldKeyList( purchaseOrderVoClass);

        QueryCondition queryCondition = QueryCondition.build( SupplierChainConstants.PUR_PURCHASEORDER)
//                .select( fieldKeyList )
                .select("FChangeFlag")
                // 查询大于上次的数据
//                .gt( "FModifyDate", new Date())
                // 单据状态 已审核 需要其他状态枚举找产品
                .eq( "FDocumentStatus","C" )
//                 关闭状态 未关闭 需要其他状态枚举找产品
                .eq("FCloseStatus","A")
//                .eq("FBillNo","CGDD003405")
                .page(1, 10000);

        System.out.println(JSON.toJSONString( queryCondition.queryParam() ));

        List<PurchaseOrderVo> purchaseOrderVoList = k3cloudRemoteService.documentQueryWithClass(queryCondition.queryParam(), purchaseOrderVoClass );

        List<String> collect = purchaseOrderVoList.stream().map(PurchaseOrderVo::getFChangeFlag).distinct().collect(Collectors.toList());
        System.out.println(JSON.toJSONString( collect ));

        System.out.println( purchaseOrderVoList.size());
        long count = purchaseOrderVoList.stream().map(PurchaseOrderVo::getFBillNo).distinct().count();
        Map<String, List<PurchaseOrderVo>> collect = purchaseOrderVoList.stream().collect( Collectors.groupingBy( PurchaseOrderVo::getFBillNo ));
        System.out.println(collect.size());

        System.out.println( JSON.toJSONString( purchaseOrderVoList ));

```

```text
2、SaveParam 使用
保存或更新对象的字段需要是要符合要求,要么加注解,要么字段要一样
应该 fastjson的 JSONField or gson 的 @SerializedName("") 
由于 金蝶使用的是 gson ,而我封装的json解析使用的是 fastjson,ememem(算了不移除了,就这样用吧)
也就是 保存 使用的 字段注解应该是 @SerializedName  
```
```java
        PurchaseReceiveBillCreateOrder purchaseReceiveBillCreateOrder = new PurchaseReceiveBillCreateOrder();
        purchaseReceiveBillCreateOrder.setFBillTypeId(new Number("SLD01_SYS"));
        purchaseReceiveBillCreateOrder.setFMsWlfl(new Number("001"));
        purchaseReceiveBillCreateOrder.setFPurOrgId(new Number("100"));
        purchaseReceiveBillCreateOrder.setFSupplierId( new Number("00013") );
        purchaseReceiveBillCreateOrder.setFOwnerIdHead( new Number("00013") );

        List< PurchaseReceiveBillCreateOrder.FDetailEntity> detailEntityList = new ArrayList<>();
        PurchaseReceiveBillCreateOrder.FDetailEntity detailEntity = new PurchaseReceiveBillCreateOrder.FDetailEntity();
        detailEntity.setFUnitId( new Number("Pcs") );
        detailEntity.setFPriceUnitId( new Number("Pcs"));
        detailEntity.setFStockUnitId( new Number("Pcs"));
        detailEntity.setFActReceiveQty( 100);
        detailEntity.setFPriceBaseQty(100);
        detailEntity.setFsupdelqty( 100);
        detailEntity.setFActlandQty(100);
        detailEntity.setFStockQty(100);
        detailEntity.setFMaterialId( new Number("010006"));
        detailEntity.setFStockId(new Number("0002"));
        detailEntityList.add( detailEntity);

        PurchaseReceiveBillCreateOrder.FDetailEntity detailEntity1 = new PurchaseReceiveBillCreateOrder.FDetailEntity();
        detailEntity1.setFUnitId( new Number("Pcs"));
        detailEntity1.setFPriceUnitId( new Number("Pcs"));
        detailEntity1.setFStockUnitId( new Number("Pcs"));
        detailEntity1.setFActReceiveQty( 100);
        detailEntity1.setFPriceBaseQty(100);
        detailEntity1.setFsupdelqty( 100);
        detailEntity1.setFActlandQty(100);
        detailEntity1.setFStockQty(100);
        detailEntity1.setFMaterialId( new Number("1AAAAA-01"));
        detailEntity1.setFStockId(new Number("0002"));
        detailEntityList.add( detailEntity1);

        purchaseReceiveBillCreateOrder.setFDetailEntity( detailEntityList);

        SaveCondition<PurchaseReceiveBillCreateOrder> saveCondition = SaveCondition.build(purchaseReceiveBillCreateOrder)
                .needReturnField(Arrays.asList("FID", "FDetailEntity.FEntryID","FDetailEntity.FMaterialId"));

        SaveParam<PurchaseReceiveBillCreateOrder> saveParam = saveCondition.saveParam();
        System.out.println(new Gson().toJson(saveParam));

        PurchaseReceiveBillCreateOrder purchaseReceiveBillCreateOrderInfo = k3cloudRemoteService.saveOrUpdateWithReturnObj( SupplierChainConstants.PUR_RECEIVE_BILL, saveParam ,PurchaseReceiveBillCreateOrder.class);
        System.out.println( JSON.toJSONString( purchaseReceiveBillCreateOrderInfo ));

```
