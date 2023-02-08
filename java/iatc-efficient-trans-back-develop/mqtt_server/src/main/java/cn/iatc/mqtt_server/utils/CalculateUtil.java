package cn.iatc.mqtt_server.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CalculateUtil {

    public static BigDecimal add(String numA, String numB) {
        BigDecimal numABigD = new BigDecimal(numA);
        BigDecimal numBBigD = new BigDecimal(numB);
        return numABigD.add(numBBigD);
    }

    public static BigDecimal add(BigDecimal numA, BigDecimal numB) {
        return numA.add(numB);
    }

    /**
     * 减法
     * @param numA
     * @param numB
     * @return
     */
    public static BigDecimal subtract(String numA, String numB) {
        BigDecimal numABigD = new BigDecimal(numA);
        BigDecimal numBBigD = new BigDecimal(numB);
        return subtract(numABigD, numBBigD);
    }

    public static BigDecimal subtract(BigDecimal numA, BigDecimal numB) {
        return numA.subtract(numB);
    }
    /**
     * 乘法
     * @param numA
     * @param numB
     * @return
     */
    public static BigDecimal multiply(String numA, String numB) {
        BigDecimal numABigD = new BigDecimal(numA);
        BigDecimal numBBigD = new BigDecimal(numB);
        return multiply(numABigD, numBBigD);
    }

    public static BigDecimal multiply(BigDecimal numA, BigDecimal numB) {
        return numA.multiply(numB);
    }

    /**
     * n次方
     * @param value
     * @param n
     * @return
     */
    public static BigDecimal pow(String value,  int n) {
        BigDecimal valueBigD = new BigDecimal(value);
        return pow(valueBigD, n);
    }

    public static BigDecimal pow(BigDecimal value,  int n) {
        return value.pow(n);
    }

    /**
     * 除法
     * @param numA
     * @param numB
     * @param scale 结果精度
     * @return
     */
    public static BigDecimal divide(String numA, String numB, int scale) {
        BigDecimal numABigD = new BigDecimal(numA);
        BigDecimal numBBigD = new BigDecimal(numB);

        return divide(numABigD, numBBigD, scale);
    }

    public static BigDecimal divide(BigDecimal numA, BigDecimal numB, int scale) {
        return numA.divide(numB, scale, RoundingMode.HALF_UP);
    }

    /**
     * 绝对值计算
     * @param num
     * @return
     */
    public static BigDecimal abs(String num) {
        BigDecimal numBigD = new BigDecimal(num);
        return abs(num);
    }

    public static BigDecimal abs(BigDecimal num) {
        return num.abs();
    }
    /**
     * 标准差σ=sqrt(s^2)
     * 结果精度：scale
     * 牛顿迭代法求大数开方
     *
     * @param value 开方的值
     * @param scale 结果精度
     * @return
     */
    public static BigDecimal sqrt(String value, int scale){
        BigDecimal valueBigD = new BigDecimal(value);
        return sqrt(valueBigD, scale);
    }

    public static BigDecimal sqrt(BigDecimal value, int scale){
        BigDecimal num2 = BigDecimal.valueOf(2);
        int precision = 100;
        MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
        BigDecimal deviation = value;
        int cnt = 0;
        while (cnt < precision) {
            deviation = (deviation.add(value.divide(deviation, mc))).divide(num2, mc);
            cnt++;
        }
        deviation = deviation.setScale(scale, RoundingMode.HALF_UP);
        return deviation;
    }
}
