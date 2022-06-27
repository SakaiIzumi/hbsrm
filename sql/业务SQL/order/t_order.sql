-- 新增消息阅读时间
ALTER TABLE t_order ADD msg_read_time datetime(0);
-- 订单类型
ALTER TABLE t_order ADD order_type varchar(255);

-- 采购订单添加唯一索引
create unique index purchase_order_code on t_order(purchase_department)

-- 新增商家编码（物料条码）
ALTER TABLE t_order_product_details ADD merchant_code varchar(255);