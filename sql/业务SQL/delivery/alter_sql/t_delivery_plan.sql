
-- 计划表
-- 1期 送货计划新增字段
ALTER TABLE t_delivery_plan ADD bill_no VARCHAR(255);
-- 修改采购方编码和名称的字段
alter table t_delivery_plan change customer_code purchase_code VARCHAR(255);
alter table t_delivery_plan change customer_name purchase_name VARCHAR(255);
-- 新增供应商名称
ALTER TABLE t_delivery_plan ADD supplier_name VARCHAR(255);
-- 订单类型
ALTER TABLE t_delivery_plan ADD order_type VARCHAR(255);



-- 计划明细表
-- 采购单号
ALTER TABLE t_delivery_plan_detail ADD bill_no VARCHAR(255);
-- 商家编码
ALTER TABLE t_delivery_plan_detail ADD merchant_code VARCHAR(255);
-- 产品单价
ALTER TABLE t_delivery_plan_detail ADD product_unit_price decimal(10,2);
-- 含税单价
ALTER TABLE t_delivery_plan_detail ADD tax_unit_price decimal(10,2);



-- 计划项次表
-- 入库仓
ALTER TABLE t_delivery_plan_detail ADD warehousing VARCHAR(255);
-- 送货地址
ALTER TABLE t_delivery_plan_detail ADD delivery_address VARCHAR(255);
-- 剩余数量
ALTER TABLE t_delivery_plan_detail ADD remaining_quantity VARCHAR(255);

-- 送货单
-- 入库单号
ALTER TABLE t_delivery_note ADD receipt_no VARCHAR(25);
