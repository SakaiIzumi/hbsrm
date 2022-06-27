package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * desc: 送货单单据类型转换枚举
 *    采购订单的单据类型 -->  收料通知单的单据类型
 *
 * @author Rao
 * @Date 2022/02/18
 **/
@AllArgsConstructor
@Getter
public enum DeliveryBillTypeConvertEnum {


    /**
     * 标准收料单
     */
    standard("CGDD01_SYS","SLD01_SYS"),
    /**
     * 委外收料单
     */
    outsource("CGDD01_SYS","SLD03_SYS"),
    ;

    /**
     * 采购订单单据类型
     */
    private final String po_bill_type;
    /**
     * 收料单单据类型
     */
    private final String rc_bill_type;

    public final static Map<String,String> DELIVERY_BILL_TYPE_CONVERT_ENUM_MAP = Stream.of( values() ).collect(Collectors.toMap(DeliveryBillTypeConvertEnum::getPo_bill_type,DeliveryBillTypeConvertEnum::getRc_bill_type));


}
