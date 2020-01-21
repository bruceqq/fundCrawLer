package com.jxnu.finance.utils;

import java.math.BigDecimal;

/**
 * @author shoumiao_yao
 * @date 2016-10-13
 */
public class NumberUtil {

    public static boolean maxRatio(Float netWorth, Float maxNetWorth) {
        if (maxNetWorth == null) return false;
        Float ratio = (maxNetWorth - netWorth) / maxNetWorth;
        if (ratio > 0.12) return true;
        return false;
    }

    public static boolean minRatio(Float netWorth, Float minNetWorth) {
        if (minNetWorth == null) return false;
        Float ratio = (netWorth - minNetWorth) / minNetWorth;
        if (ratio > 0.12) return true;
        return false;
    }

    public static Double calculate(BigDecimal bigDecimal) {
        return bigDecimal.divide(new BigDecimal(100000000), 2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    /**
     * 除
     *
     * @param molecule
     * @param denominator
     * @return
     */
    public static Double divide(Double molecule, Double denominator) {
        BigDecimal moleculeDecimal = new BigDecimal(molecule);
        BigDecimal denoMinatorDecimal = new BigDecimal(denominator);
        return moleculeDecimal.divide(denoMinatorDecimal, 2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    /**
     * 乘
     *
     * @param molecule
     * @param denominator
     * @return
     */
    public static Double multiply(Double molecule, Double denominator) {
        BigDecimal moleculeDecimal = new BigDecimal(molecule);
        BigDecimal denoMinatorDecimal = new BigDecimal(denominator);
        return moleculeDecimal.multiply(denoMinatorDecimal).doubleValue();
    }
}
