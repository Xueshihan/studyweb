package cn.iatc.mqtt_server.algorithm;

import cn.iatc.mqtt_server.utils.CalculateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

// 变压器算法
public class TransAlgorithm {

    /**
     * 计算高压电压
     * @param lowCurrent 低压电流
     * @param lowVoltage 低压电压
     * @param powerFactor 功率因数
     * @param k 变压器额定变比
     * @param uk 变压器短路阻抗电压百分数
     * @param u1n 变压器高压侧额定电压 目前是10kv
     * @param sn 变压器额定容量
     * @param scale 精度
     * @return
     */
    public static String calHighVoltage(String lowCurrent, String lowVoltage, String powerFactor, String k, String uk, String u1n, String sn, int scale) {
        BigDecimal a = CalculateUtil.multiply(lowVoltage, k);
        BigDecimal u1nPow = CalculateUtil.pow(u1n, 2);
        BigDecimal u1nPowI2 = CalculateUtil.multiply(u1nPow.toString(), lowCurrent);
        BigDecimal uiUpper = CalculateUtil.multiply(uk, u1nPowI2.toString());
        BigDecimal snDown = CalculateUtil.multiply("100", sn);
        BigDecimal b = CalculateUtil.divide(uiUpper, snDown, scale);

        BigDecimal a2 = CalculateUtil.pow(a, 2);
        BigDecimal b2 = CalculateUtil.pow(b, 2);
        BigDecimal ab = CalculateUtil.multiply(a, b);
        BigDecimal twoPowerFactor = CalculateUtil.multiply("2", powerFactor);
        BigDecimal twoAB = CalculateUtil.multiply(ab, twoPowerFactor);
        BigDecimal result = CalculateUtil.add(a2, CalculateUtil.add(b2, twoAB));
        BigDecimal resultSqrt = CalculateUtil.sqrt(result, scale);
        return resultSqrt.setScale(scale, RoundingMode.HALF_UP).toString();
    }

    /**
     * 计算无功功率
     * @param highCurrent 高压电流
     * @param highVoltage 高压电压
     * @param i0 变压器空载电流百分数
     * @param uk 变压器短路阻抗电压百分数
     * @param sn 变压器额定容量
     * @param scale 精度
     * @return
     */
    public static String calLossPower(String highCurrent, String highVoltage, String i0, String uk, String sn, int scale) {
        BigDecimal ui = CalculateUtil.multiply(highCurrent, highVoltage);
        System.out.println("ui:" + ui);
        BigDecimal i0Divide = CalculateUtil.divide(i0, "100", scale);
        System.out.println("i0Divide:" + i0Divide);
        BigDecimal ukDivide = CalculateUtil.divide(uk, "100", scale);
        System.out.println("ukDivide:" + ukDivide);
        BigDecimal snUI = CalculateUtil.divide(sn, ui.toString(), scale);
        System.out.println("snUI:" + snUI);
        BigDecimal result = CalculateUtil.multiply(ui, CalculateUtil.add(i0Divide, CalculateUtil.multiply(ukDivide, CalculateUtil.pow(snUI, 2))));
        return result.setScale(scale, RoundingMode.HALF_UP).toString();
    }

    /**
     * 计算高压侧有功功率
     * @param highCurrent 高压电流
     * @param highVoltage 高压电压
     * @param lowCurrent 低压电流
     * @param lowVoltage 低压电压
     * @param powerFactor 功率因数
     * @param lossPower 无功损耗
     * @param scale 精度
     * @return
     */
    public static String calHighActivePower(String highCurrent, String highVoltage, String lowCurrent, String lowVoltage, String powerFactor, String lossPower, int scale) {
        BigDecimal highUI = CalculateUtil.multiply(highCurrent, highVoltage);
        BigDecimal lowUI = CalculateUtil.multiply(lowCurrent, lowVoltage);
        BigDecimal cos2 = CalculateUtil.pow(powerFactor, 2);
        BigDecimal sin2 = CalculateUtil.subtract("1", cos2.toString());
        BigDecimal sin = CalculateUtil.sqrt(sin2, scale);

        BigDecimal highUI2 = CalculateUtil.pow(highUI, 2);
        BigDecimal lossPowerBig = new BigDecimal(lossPower);
        BigDecimal lowUI2 = CalculateUtil.pow(CalculateUtil.add(lossPowerBig, CalculateUtil.multiply(lowUI, sin)), 2);
        BigDecimal dValue = CalculateUtil.subtract(highUI2, lowUI2);
        BigDecimal dValueAbs = CalculateUtil.abs(dValue);
        BigDecimal result = CalculateUtil.sqrt(dValueAbs, scale);
        return result.setScale(scale, RoundingMode.HALF_UP).toString();
    }

    /**
     * 计算低压有功功率
     * @param lowCurrent 低压电流
     * @param lowVoltage 低压电压
     * @param powerFactor 功率因数
     * @return
     */
    public static String calLowActivePower(String lowCurrent, String lowVoltage, String powerFactor) {
        BigDecimal ui = CalculateUtil.multiply(lowCurrent, lowVoltage);
        BigDecimal result = CalculateUtil.multiply(ui.toString(), powerFactor);
        return result.toString();
    }

    public static void main(String[] args) {
        String aa = "2";
        String bb = "6";
        BigDecimal bigDecimal1 = new BigDecimal(aa);
        BigDecimal bigDecimal2 = new BigDecimal(bb);

//        System.out.println(bigDecimal1.divide(bigDecimal2, 2, RoundingMode.HALF_UP));
//        // 平方
//        BigDecimal a1 = bigDecimal1.pow(2);
//        System.out.println(a1);
//        BigDecimal a2 = CalculateUtil.sqrt(a1, 10);
        System.out.println(calLossPower("1", "2", "0.5", "12.9", "7", 6));




    }
}
