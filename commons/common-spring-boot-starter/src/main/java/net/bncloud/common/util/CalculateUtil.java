package net.bncloud.common.util;

import java.math.BigDecimal;

/**
 * @author Toby
 */
public class CalculateUtil {

    public static BigDecimal convertNullToZero(BigDecimal val){
        return val==null ? BigDecimal.ZERO : val;
    }

    public static BigDecimal add(BigDecimal val1,BigDecimal val2){
        return convertNullToZero(val1).add(convertNullToZero(val2));
    }

    public static BigDecimal subtract(BigDecimal val1,BigDecimal val2){
        return convertNullToZero(val1).subtract(convertNullToZero(val2));
    }

    public static BigDecimal add(BigDecimal... val){
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal item : val) {
            sum = sum.add(CalculateUtil.convertNullToZero(item));
        }
        return sum;

    }

    public static BigDecimal subtract(BigDecimal... val){
        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal item : val) {
            result = result.subtract(CalculateUtil.convertNullToZero(item));
        }
        return result;

    }


}
