package net.bncloud.api.feign.saas.sys;

import lombok.Getter;

/**
 * @ClassName DictCodeEnum
 * @Description: 字典编码
 * @Author Administrator
 * @Date 2021/4/30
 * @Version V1.0
 **/
@Getter
public enum DictCodeTransportMethodEnum {

    CAR("1","汽车"),
    RAILWAY("2","铁路"),
    PLANE("3","空运"),
    SHIP("4","海运");

    private String code;
    private String msg;

    DictCodeTransportMethodEnum(String code , String msg){
        this.code=code ;
        this.msg=msg ;
    }





}
