-- 新增商家编码（物料条码）
ALTER TABLE t_order_product_details ADD merchant_code varchar(255);
-- 添加订单明细的入库数量字段
ALTER TABLE t_order_product_details ADD inventory_quantity decimal(20,2) DEFAULT '0.0' COMMENT '入库数量';
-- 订单明细的来源ID
ALTER TABLE t_order_product_details ADD `source_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '来源系统ID';
-- 订单的来源ID
ALTER TABLE t_order ADD `source_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '来源系统ID';
-- 设置sourceId的值
update t_order_product_details set source_id=item_code where source_id is null


-- add 添加送货计划的采购日期
ALTER TABLE t_delivery_plan ADD `purchase_time` datetime DEFAULT NULL COMMENT '采购日期';
-- 跨库更新，需要考虑账号权限的问题。
update delivery.t_delivery_plan dp,order.t_order o set dp.`purchase_time` = o.`purchase_time` where o.purchase_order_code = dp.bill_no;


ALTER TABLE t_delivery_note ADD have_attachment int(8) COMMENT '送货明细是否有附件：0表示改送货单下的明细都没有附件，1表示部分有，部分没有，2表示都有附件';
-- TODO bis需要切换成正式的数据库
-- TODO 数据库中修改菜单显示
