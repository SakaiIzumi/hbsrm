package net.bncloud.delivery.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum MrpOperateType {

    EDIT("edit","编辑"),
    DELETE("delete","删除"),
    ON_OFF("modify","修改");

    private String code;

    private String name;
}