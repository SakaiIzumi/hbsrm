package net.bncloud.enums;



import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @ClassName DeliveryOperateType
 * @Description: 发货单操作类型枚举
 * @Author liulu
 * @Date 2021/3/23
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
public enum EventCode {

    /*费用单*/
    cost_bill_send("cost_bill:send","费用单发送事件"),
    cost_bill_invalid("cost_bill:invalid","费用单作废事件"),
    cost_bill_confirm("cost_bill:confirm","费用单确认事件"),
    cost_bill_withdraw("cost_bill:withdraw","费用单撤回事件"),
    cost_bill_remind("cost_bill:remind","费用单提醒事件"),
    cost_bill_return("cost_bill:return","费用单退回事件"),

    /*智采合同*/
    contract_invalid("contract:invalid","合同作废事件"),
    contract_remind("contract:remind","合同提醒事件"),
    contract_send("contract:send","合同发送事件"),
    contract_withdraw("contract:withdraw","合同撤回事件"),
    /*智易合同
    * */
    contract_confirm("contract:confirm","合同确认事件"),
    contract_reject("contract:reject","合同拒绝事件"),

    /**
     *智采收货
     */
    delivery_apply_all_agree("delivery_apply:all_agree","送货申请全部同意事件"),
    delivery_apply_delete("delivery_apply:delete","送货申请删除事件"),
    delivery_apply_edit("delivery_apply:edit","送货申请编辑事件"),
    delivery_apply_invalid("delivery_apply:invalid","送货申请作废事件"),
    delivery_apply_issue("delivery_apply:issue","送货申请发布事件"),
    delivery_apply_new("delivery_apply:new","送货申请新建事件"),
    delivery_apply_part_agree("delivery_apply:part_agree","送货申请部分同意事件"),
    delivery_apply_remind("delivery_apply:remind","送货提醒事件"),
    delivery_apply_returned("delivery_apply:returned","送货申请送货申请已退回事件"),
    delivery_apply_save("delivery_apply:save","送货申请保存事件"),
    delivery_apply_withdraw("delivery_apply:withdraw","送货申请撤回事件"),
    delivery_note_confirm_receipt("delivery_note:confirm_receipt","送货通知单确认签收事件"),
    delivery_note_delete("delivery_note:delete","送货通知单删除事件"),
    delivery_note_delivered("delivery_note:delivered","货物已发事件"),
    delivery_note_edit("delivery_note:edit","送货通知单编辑事件"),
    delivery_note_good_delivered("delivery_note:good_delivered","送货通知单货物送达事件"),
    delivery_note_invalid("delivery_note:invalid","送货通知单作废事件"),
    delivery_note_issue("delivery_note:issue","送货通知单发布事件"),
    delivery_note_new("delivery_note:new","送货通知单新建事件"),
    delivery_note_returned("delivery_note:returned","送货通知单送货已退回事件"),
    delivery_note_save("delivery_note:save","送货通知单保存事件"),
    delivery_note_withdraw("delivery_note:withdraw","送货通知单撤回事件"),
    delivery_note_withdraw_receipt("delivery_note:withdraw_receipt","送货通知单撤回签收事件"),

    /**
     * 送货计划
     */
    delivery_plan_send("delivery_plan:send","送货计划发送事件"),
    delivery_plan_remind("delivery_plan:remind","送货计划提醒事件"),
    delivery_plan_confirm("delivery_plan:confirm","送货计划确认事件"),


    /**
     * 计划排程明细
     */
    plan_scheduling_detail_send("plan_scheduling_detail:send","计划排程明细发布事件"),
    plan_scheduling_detail_remind("plan_scheduling_detail:remind","计划排程明细提醒事件"),
    //代办 -采购方差异确认
    plan_scheduling_detail_difference_confirm("plan_scheduling_detail:difference_confirm","计划排程明细差异确认事件"),
    //代办  -供应商确认
    plan_scheduling_detail_confirm("plan_scheduling_detail:confirm","计划排程明细确认事件"),

    /**
     * 智易送货确认
     */
    delivery_note_confirm("delivery_note:confirm","送货单确认发出事件"),



    /**
     * 智采订单
     */
    zc_confirmChangeOrder("zc:confirmChangeOrder","智采确定变更事件(确认变更订单)"),
    zc_confirmDifferenceOrder("zc:confirmDifferenceOrder","智采确定差异事件(差异确认订单)"),
    zc_confirmMsgOrder("zc:confirmMsgOrder","智采确认事件(订单已确认)"),
    zc_sendCommunicateOrder("zc:sendCommunicateOrder","智采答交回复事件"),
    zc_sendDifferenceOrder("zc:sendDifferenceOrder","智采发送变更事件"),
    zc_SendOrder("zc:SendOrder","智采发送事件(订单已提醒)"),
    zc_sendRemindMsgOrder("zc:sendRemindMsgOrder","智采提醒事件(订单已提醒)"),
    zc_startOrder("zc:startOrder","智采取消挂起事件"),
    zc_stopOrder("zc:stopOrder","智采挂起事件"),
    /**
     * 智易订单
     */
    zy_goBackOrder("zy:goBackOrder","订单退回（订单已退回）"),
    zy_confirmChangeOrder("zy:confirmChangeOrder","智采确定变更事件(订单已确认变更)"),
    zy_confirmDifferenceOrder("zy:confirmDifferenceOrder","智采确定差异事件"),
    zy_confirmMsgOrder("zy:confirmMsgOrder","智采确认事件(订单已确认)"),
    zy_sendCommunicateOrder("zy:sendCommunicateOrder","智采答交回复事件"),
//    zy_sendRemindMsgOrder("zc:stopOrder"," 智采提醒事件(订单已提醒)"),
    zy_sendRemindMsgOrder("zy:sendRemindMsgOrder"," 智采提醒事件(订单已提醒)"),

    /**对账单，采购工作台*/
    statement_purchase_send("statement_purchase:send","对账单，采购方发送事件"),
    statement_purchase_withdraw("statement_purchase:withdraw","对账单，采购方撤回事件"),
    statement_purchase_invalid("statement_purchase:invalid","对账单，采购方作废事件"),
    statement_purchase_remind("statement_purchase:remind","对账单，采购方提醒事件"),
    statement_purchase_confirm("statement_purchase:confirm","对账单，采购方确认事件"),
    statement_purchase_reject("statement_purchase:reject","对账单，采购方退回事件"),

    /**对账单，销售工作台*/
    statement_supplier_send("statement_supplier:send","对账单，供应商发送事件"),
    statement_supplier_withdraw("statement_supplier:withdraw","对账单，供应商撤回事件"),
    statement_supplier_invalid("statement_supplier:invalid","对账单，供应商作废事件"),
    statement_supplier_remind("statement_supplier:remind","对账单，供应商提醒事件"),
    statement_supplier_confirm("statement_supplier:confirm","对账单，供应商确认事件"),
    statement_supplier_reject("statement_supplier:reject","对账单，供应商退回事件"),



    /**智采-询价单*/
    quotation_supplier_winner("quotation_winner:pricing","定价单成功通知供应商中标事件"),
    quotation_supplier_loser("quotation_loser:pricing","定价单失败通知供应商中标事件"),
    quotation_winner_sms("quotation_winner_sms:pricing","询价单定价成功短信提醒供应商"),
    quotation_loser_sms("quotation_loser_sms:pricing","询价单定价失败短信提醒供应商"),
    quotation_supplier_restate("quotation_restate:pricing","定价单重新报价通知供应商"),
    quotation_restate_sms("quotation_restate_sms:pricing","询价单重新报价短信提醒供应商"),
    quotation_supplier_early_warning("quotation_supplier:early_warning","询价单快结束对供应商预警事件"),
    quotation_supplier_sms_early_warning("quotation_supplier_sms:early_warning","询价单快结束对供应商短信预警"),
    quotation_supplier_notice_bid("quotation_supplier:notice_bid","通知供应商应标事件"),
    quotation_supplier_sms_notice_bid("quotation_supplier_sms:notice_bid","通知供应商应标短信事件"),
    quotation_supplier_quoted_price("quotation_supplier:quoted_price","供应商报价事件"),
    quotation_supplier_sms_quoted_price("quotation_supplier_sms:quoted_price","供应商报价短信事件"),
    quotation_supplier_response_notice("quotation_supplier:response_notice","供应商应标通知"),
    quotation_info_publish("quotation_info:publish","询价单发布通知"),
    quotation_supplier_reject("quotation_supplier:reject","供应商应标拒绝")
    ;






    private String code;

    private String name;
}
