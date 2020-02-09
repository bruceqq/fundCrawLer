package com.jxnu.finance.utils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yaphyao
 * @version 2017/11/7
 * @see com.jxnu.finance.utils
 */
public class CalculateUtil {

    /**
     * 乘
     *
     * @param amount
     * @param share
     * @return
     */
    public static Float multiply(Float amount, Float share) {
        BigDecimal amountBig = new BigDecimal(String.valueOf(amount));
        BigDecimal shareBig = new BigDecimal(String.valueOf(share));
        return amountBig.multiply(shareBig).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 除
     *
     * @param molecule
     * @param denominator
     * @return
     */
    public static Float divide(Float molecule, Float denominator, Integer num) {
        BigDecimal moleculeBig = new BigDecimal(String.valueOf(molecule));
        BigDecimal denominatorBig = new BigDecimal(String.valueOf(denominator));
        return moleculeBig.divide(denominatorBig, num, BigDecimal.ROUND_HALF_EVEN).floatValue();
    }

    public static Double divide(Double molecule, Double denominator, Integer num) {
        BigDecimal moleculeBig = new BigDecimal(String.valueOf(molecule));
        BigDecimal denominatorBig = new BigDecimal(String.valueOf(denominator));
        return moleculeBig.divide(denominatorBig, num, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    /**
     * 加
     *
     * @param amounts
     * @return
     */
    public static Float plus(List<Float> amounts) {
        float sum = 0f;
        if (amounts == null || amounts.isEmpty()) return sum;
        for (Float amount : amounts) {
            sum += amount;
        }
        BigDecimal sumBig = new BigDecimal(sum);
        return sumBig.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }


    public static void main(String[] args) {
        System.out.println(multiply(10.0f, 1 - 0.0015f));
        System.out.println(divide(12.34f, 3.44f, 2));
    }


}
