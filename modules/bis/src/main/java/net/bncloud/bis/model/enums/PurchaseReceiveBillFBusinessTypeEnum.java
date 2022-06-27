package net.bncloud.bis.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * desc: 业务类型
 *
 * @author Rao
 * @Date 2022/03/01
 **/
@AllArgsConstructor
@Getter
public enum PurchaseReceiveBillFBusinessTypeEnum {


    /**
     * 标准收料单
     */
    standard("SLD01_SYS","CG"),
    /**
     * 委外收料单
     */
    outsource("SLD03_SYS","WW"),
    ;

    /**
     * 收料单单据类型
     */
    private final String rc_bill_type;
    /**
     * 收料通知单的业务类型
     */
    private final String rc_business_type;

    /**
     * bill type 转 business type
     */
    public final static Map<String,String> billType2BusinessTypeMap = Stream.of( values()).collect(Collectors.toMap(PurchaseReceiveBillFBusinessTypeEnum::getRc_bill_type,PurchaseReceiveBillFBusinessTypeEnum::getRc_business_type) );

}
