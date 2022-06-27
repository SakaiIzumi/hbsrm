ALTER TABLE t_order ADD off_state varchar(25) comment '关闭状态（ERP）';


-- 新增订单的关闭时间
ALTER TABLE t_order ADD closed_time datetime(0) comment '关闭时间';


ALTER TABLE t_delivery_note ADD `receipt_no` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '入库单号';


-- 供需平衡报表
CREATE TABLE `t_supply_demand_balance`
(
    `id`                                 bigint NOT NULL,
    `item_no`                            bigint                                                        DEFAULT NULL COMMENT '序号',
    `product_code`                       varchar(30) COLLATE utf8mb4_general_ci                        DEFAULT NULL COMMENT '产品编码',
    `product_name`                       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '产品名称',
    `merchant_code`                      varchar(30) COLLATE utf8mb4_general_ci                        DEFAULT NULL COMMENT '条码（商家编码）',
    `date`                               datetime                                                      DEFAULT NULL COMMENT '日期',
    `order_demand_quantity`              varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '订单需求数量',
    `plan_delivery_quantity`             varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '计划发货数量',
    `confirmed_undelivery_quantity`      varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '已确认未发货数量（当前日期的）',
    `confirmed_undelivery_quantity_show` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '已确认未发货数量(展示字段，展示的是3天前的数据)',
    `estimated_arrival_quantity`         varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '预计到货数量',
    `receipt_quantity`                   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '入库数量',
    `balance_quantity`                   varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '供需结余',
    `outstanding_orders_quantity`        varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '未完成订单',
    `is_deleted`                         bit(1)                                                        DEFAULT NULL COMMENT '逻辑删除：0未删除，1已删除',
    `created_by`                         bigint                                                        DEFAULT NULL COMMENT '创建人',
    `created_date`                       datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `last_modified_by`                   bigint                                                        DEFAULT NULL COMMENT '修改人',
    `last_modified_date`                 datetime                                                      DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='供需平衡';


-- 供需平衡报表明细视图
CREATE VIEW supply_demand_detail_view AS
(

-- 订单

(SELECT o.id                                       as id,
        opd.product_code                           as product_code,
        opd.product_name                           as product_name,
        opd.merchant_code                          as merchant_code,
        DATE_FORMAT(opd.delivery_time, '%Y-%m-%d') as date,
        '0'                                        as bill_type,
        o.order_no                                 as bill_code,
        o.order_status                             as bill_status,
        opd.purchase_order_code                    as purchase_order_code,
        o.supplier_code                            as supplier_code,
        o.supplier_name                            as supplier_name,
        ROUND(sum(opd.purchase_num))               as quantity,
        0                                          as remaining_quantity
 FROM `order`.t_order_product_details opd
          left JOIN `order`.t_order o on o.purchase_order_code = opd.purchase_order_code
 where opd.is_deleted = 0
   and o.order_status = 7
 GROUP BY opd.purchase_order_code, opd.product_code, date
 ORDER BY opd.product_code, date
)

UNION ALL

-- 送货计划
(SELECT p.id                                     as id,
        pd.product_code                          as product_code,
        pd.product_name                          as product_name,
        pd.merchant_code                         as merchant_code,
        DATE_FORMAT(t.delivery_date, '%Y-%m-%d') as date,
        '1'                                      as bill_type,
        p.plan_no                                as bill_code,
        p.plan_status                            as bill_status,
        pd.bill_no                               as purchase_order_code,
        pd.supplier_code                         as supplier_code,
        pd.supplier_name                         as supplier_name,
        ROUND(sum(t.delivery_quantity))          as quantity,
        ROUND(sum(t.remaining_quantity))         as remaining_quantity
 FROM delivery.t_delivery_plan_detail pd
          left join delivery.t_delivery_plan_detail_item t on t.delivery_plan_detail_id = pd.id
          left join delivery.t_delivery_plan p on p.id = pd.delivery_plan_id
          inner join `order`.t_order o on o.purchase_order_code = pd.bill_no
 where pd.is_deleted = 0
   and o.order_status = 7
 GROUP BY pd.bill_no, pd.product_code, date
 ORDER BY pd.product_code, date
)

UNION ALL

-- 送货单
(SELECT dn.id                                      as id,
        dd.product_code                            as product_code,
        dd.product_name                            as product_name,
        dd.bar_code                                as merchant_code,
        DATE_FORMAT(dn.estimated_time, '%Y-%m-%d') as date,
        '2'                                        as bill_type,
        dn.delivery_no                             as bill_code,
        dn.delivery_status_code                    as bill_status,
        dd.bill_no                                 as purchase_order_code,
        dn.supplier_code                           as supplier_code,
        dn.supplier_name                           as supplier_name,
        ROUND(sum(dd.real_delivery_quantity))      as quantity,
        0                                          as remaining_quantity
 FROM delivery.t_delivery_detail dd
          left join delivery.t_delivery_note dn on dd.delivery_id = dn.id
          inner join `order`.t_order o on o.purchase_order_code = dd.bill_no
 where dd.is_deleted = 0
   and o.order_status = 7
   and dn.delivery_status_code != '1'
 GROUP BY dd.bill_no, dd.product_code, date
 ORDER BY dd.product_code, date
)

UNION ALL

-- 入库单
(SELECT dd.id                                    as id,
        dd.product_code                          as product_code,
        dd.product_name                          as product_name,
        dd.bar_code                              as merchant_code,
        DATE_FORMAT(dn.signing_time, '%Y-%m-%d') as date,
        '3'                                      as bill_type,
        dn.f_number                              as bill_code,
        dn.erp_signing_status                    as bill_status,
        dd.bill_no                               as purchase_order_code,
        dn.supplier_code                         as supplier_code,
        dn.supplier_name                         as supplier_name,
        ROUND(sum(dd.receipt_quantity))          as quantity,
        0                                        as remaining_quantity
 FROM delivery.t_delivery_detail dd
          left join delivery.t_delivery_note dn on dd.delivery_id = dn.id
          inner join `order`.t_order o on o.purchase_order_code = dd.bill_no
 where dd.is_deleted = 0
   and o.order_status = 7
   and dn.erp_signing_status = 'signed'
 GROUP BY dd.bill_no, dd.product_code, date
 ORDER BY dd.product_code, date
)
)