----------------------------------
-- 一、修改和添加表字段sql
----------------------------------

ALTER TABLE `bncloud_oem`.`t_oem_purchase_order_material`
ADD COLUMN `warehouse` varchar(255) NULL COMMENT '入库仓(仓库)' AFTER `is_deleted`;

ALTER TABLE `bncloud_oem`.`t_oem_purchase_order`
ADD COLUMN `order_type` varchar(255) NULL COMMENT 'erp同步过来的单据类型' AFTER `take_over_status`;

ALTER TABLE `bncloud_oem`.`t_oem_purchase_order_material`
ADD COLUMN `tax_price` varchar(100) NULL COMMENT '含税单价' AFTER `warehouse`;
