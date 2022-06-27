package net.bncloud.service.api.platform.config.enums;

import lombok.Getter;

/**
 * desc: key 枚举
 * 有新的请往里添加
 *
 * @author Rao
 * @Date 2022/03/11
 **/
@Getter
public enum CfgParamKeyEnum {

    DELIVERY_AUTO_SEND("delivery:auto_send","是否自动发送送货计划"),
    DELIVERY_EXCESS_QUANTITY("delivery:excess_quantity","实际送货数量超出计划送货数量"),
    MRP_DEFAULT_WORKDAY("mrp:default_workday","mrp的默认工作日"),
    MRP_AUTO_HOLIDAY("mrp:auto_holiday","mrp是否订阅节假日"),
    MRP_SUPPLIER_DEFAULT_WORKDAY("mrp:supplier_default_workday","是否设置供应方默认工作日"),

    MRP_DELIVERY_AUTO_SEND("delivery:plan_scheduling_auto_send","计划排程送货计划是否自动发送"),
    DELIVERY_DELIVERY_COLLABORATION_METHOD("delivery:delivery_collaboration_method","送货协同方式"),
    DELIVERY_AUTOMATICALLY_SYNCHRONIZE_MRP_DEMAND_PLANS("delivery:automatically_synchronize_mrp_demand_plans","查询是否自动同步MRP需求计划的配置"),
    DELIVERY_SUPPLIER_DISCREPANCY_REPLY("delivery:supplier_discrepancy_reply","供应商是否可以对送货计划进行差异答复"),
    DELIVERY_IS_SUPPORT_MANUAL_ADDITION("delivery:is_support_manual_addition","计划排程是否支持手工新增"),


    ;
    private final String code;
    private final String desc;

    CfgParamKeyEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
