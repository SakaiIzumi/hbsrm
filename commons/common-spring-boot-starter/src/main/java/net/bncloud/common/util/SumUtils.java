package net.bncloud.common.util;

import java.math.BigDecimal;


public class SumUtils {

    /**
     *      * 两个Double数相除，并保留scale位小数
     *      * @param v1
     *      * @param v2
     *      * @param scale
     *      * @return Double
     *      
     */
    public static BigDecimal div(Double v1, Double v2, int scale) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
    }

	/**
	 * 乘法
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal v1, BigDecimal v2, int scale) {
		return v1.multiply(v2).setScale(scale,BigDecimal.ROUND_HALF_UP);
	}


	/**
	 *      * 两个Double数相加
	 *      * @param v1
	 *      * @param v2
	 *      * @param scale
	 *      * @return Double
	 *      
	 */
	public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
		return v1.add(v2);
	}

	/**
	 * 减法
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal v1, BigDecimal v2) {
		return v1.subtract(v2);
	}
	
	
	public static void main(String[] args) {
		
		BigDecimal orderConfirmPrice = new BigDecimal("50.00");
		BigDecimal orderConfirmPrice2 = new BigDecimal("70.00");
		BigDecimal subtract = SumUtils.subtract( orderConfirmPrice2,orderConfirmPrice);
		System.out.println(subtract);
		
		
	}



}
