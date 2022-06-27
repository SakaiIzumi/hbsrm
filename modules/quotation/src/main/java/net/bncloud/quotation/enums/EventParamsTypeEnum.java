package net.bncloud.quotation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventParamsTypeEnum {


    winner_event("winner_event","发送中标事件"),
    loser_event("loser_event","发送未中标事件"),
    winner_event_sms("winner_sms_event","发送中标事件短信"),
    loser_event_sms("loser_sms_event","发送未中标事件短信"),
    quotation_supplier_event("quotation_supplier_event","供应商应标事件"),
    quotation_supplier_event_sms("quotation_supplier_event_sms","供应商应标短信事件"),
    quotation_Restate_event("quotation_Restate_event","询价单重报站内信"),
    quotation_Restate_event_sms("quotation_Restate_event_sms","询价单重报短信事件"),
    ;

    private String code;

    private String name;
}
