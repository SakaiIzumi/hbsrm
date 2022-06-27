ALTER TABLE ss_tenant_purchaser_supplier_ref ADD unique (sup_id, pur_id);
ALTER TABLE `t_order` ADD UNIQUE (`purchase_order_code`);