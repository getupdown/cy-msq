package cn.cy.common.utils;

public class CalcUtils {

    /**
     * 向上调整至倍数,若已经是倍数,还会再向上调整
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
     * 向下调整至倍数,若已经是倍数则不再向下调整
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
