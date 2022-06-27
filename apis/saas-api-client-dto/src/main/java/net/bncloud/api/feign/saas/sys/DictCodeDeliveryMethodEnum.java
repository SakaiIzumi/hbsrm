package net.bncloud.api.feign.saas.sys;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ClassName DictCodeEnum
 * @Description: 字典编码
 * @Author Administrator
 * @Date 2021/4/30
 * @Version V1.0
 **/
@Getter
public enum DictCodeDeliveryMethodEnum {

    SUPPLIER_DELIVERY("1","供应商送货"),
    EXPRESS("2","快递"),
    PROVIDED_BY_PURCHASER("3","采购方自提");

    private String code;
    private String msg;

    DictCodeDeliveryMethodEnum(String code ,String msg){
        this.code=code ;
        this.msg=msg ;
    }





}
