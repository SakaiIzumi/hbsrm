package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/1/25
 */
@Getter
@AllArgsConstructor
public enum DeliveryCfgParam {

    DELIVERY_AUTO_SEND("delivery:auto_send","是否自动发送送货计划"),
    DELIVERY_EXCESS_QUANTITY("delivery:excess_quantity","是否自动发送送货计划"),
    MRP_DEFAULT_WORKDAY("mrp:default_workday","mrp默认工作日"),
    MRP_AUTO_HOLIDAY("mrp:auto_holiday","mrp是否开启订阅法定节假日"),
    DELIVERY_ENABLE_PLAN_SCHEDULING("delivery:delivery_collaboration_method","送货协同方式"),
    DELIVERY_IS_SUPPORT_MANUAL_ADDITION("delivery:is_support_manual_addition","计划排程是否支持手工新增"),
    DELIVERY_PLAN_SCHEDULING_AUTO_SEND("delivery:plan_scheduling_auto_send","计划排程自动发送"),
    DELIVERY_SUPPLIER_DISCREPANCY_REPLY("delivery:supplier_discrepancy_reply","供应商是否可以对送货计划进行差异答复"),
    DELIVERY_AUTOMATICALLY_SYNCHRONIZE_MRP_DEMAND_PLANS("delivery:automatically_synchronize_mrp_demand_plans","是否自动同步MRP需求计划"),
    ;
    private final String code;
    private final String description;
}
