package net.bncloud.quotation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName QuotationStatusEnum
 * @Description: 询报价状态枚举类
 * @Author Administrator
 * @Date 2021/2/17
 * @Version V1.0
 **/
@AllArgsConstructor
@Getter
public enum QuotationStatusEnum {
    /*询报价状态：
    draft 草稿，
    quotation 报价中，
    bid_opening 待开标，
    comparison 比价中，
    failure_bid 流标，
    have_pricing 已定价，
    obsolete 已作废，
    fresh 新的轮次，
    no_quotation 未报价
    */
    DRAFT("draft","草稿"),
    QUOTATION("quotation","报价中"),
    BID_OPENING("bid_opening","待开标"),
    COMPARISON("comparison","比价中"),
    FAILURE_BID("failure_bid","流标"),
    HAVE_PRICING("have_pricing","已定价"),
    OBSOLETE("obsolete","已作废"),
    FRESH("fresh","新的轮次"),
    NO_QUOTATION("no_quotation","未报价"),
    ;

    private String code;

    private String name;

    /**
     * 根据code获取去value
     * @param code
     * @return
     */
    public static String getNameByCode(String code){
        for(QuotationStatusEnum quotationStatusEnum : QuotationStatusEnum.values()){
            if(code.equals(quotationStatusEnum.getCode())){
                return quotationStatusEnum.getName();
            }
        }
        return  null;
    }

    /**
     * 通过code取枚举
     *
     * @param code
     * @return
     */
    public static QuotationStatusEnum getTypeByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (QuotationStatusEnum enums : QuotationStatusEnum.values()) {
            if (StringUtils.equals(enums.getCode(), code)) {
                return enums;
            }
        }
        return null;
    }
}