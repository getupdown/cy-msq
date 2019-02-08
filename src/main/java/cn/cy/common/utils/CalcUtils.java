package cn.cy.common.utils;

public class CalcUtils {

    /**
     * 向上调整至倍数
     *
     * @param pre
     * @param divisor
     *
     * @return
     */
    public static long enlargeToNextMultiple(long pre, long divisor) {
        return pre + (divisor - pre % divisor);
    }

    /**
     * 向下调整至倍数
     *
     * @param pre
     * @param divisor
     *
     * @return
     */
    public static long reduceToPreMultiple(long pre, long divisor) {
        return pre - pre % divisor;
    }
}
