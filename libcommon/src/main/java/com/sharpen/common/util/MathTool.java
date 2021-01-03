package com.sharpen.common.util;

import com.sharpen.common.consts.SignConst;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

import static com.sharpen.common.consts.SignConst.STR_ZERO;
import static com.sharpen.common.consts.SymbolConst.BLANK;
import static com.sharpen.common.consts.SymbolConst.BLANK_SPACE;
import static java.math.BigDecimal.ROUND_DOWN;

/**
 * 数学工具
 * @author Justin
 */
public class MathTool {

    /**
     * 乘于100万并转成整数
     * @param num 数字
     * @return
     */
    public static long multiplyMilliLong(String num) {
        if(StringUtils.isBlank(num)){
            return 0L;
        }
        return new BigDecimal(num).multiply(SignConst.BD_MILLI).longValue();
    }
    /**
     * 乘于100万并转成整数
     * @param num 数字
     * @return
     */
    public static String divideMilliStr(Long num) {
        if(num==null || num==0){
            return STR_ZERO;
        }
        return new BigDecimal(num).divide(SignConst.BD_MILLI, 6, ROUND_DOWN).stripTrailingZeros().toPlainString();
    }
    /**
     * 向下保留小数位为4
     * @param num 数字
     * @return
     */
    public static String downDecimal4(String num) {
        if(StringUtils.isBlank(num)){
            return STR_ZERO;
        }
        return new BigDecimal(num).setScale(4, ROUND_DOWN).stripTrailingZeros().toPlainString();
    }
    /**
     * 向下保留小数位
     * @param num 数字
     * @return
     */
    public static String downDecimal6(String num) {
        return new BigDecimal(num).setScale(6, ROUND_DOWN).stripTrailingZeros().toPlainString();
    }
    /**
     * 向下保留小数位
     * @param num 数字
     * @return
     */
    public static String downDecimal(String num, int decimal) {
        return new BigDecimal(num).setScale(decimal, ROUND_DOWN).stripTrailingZeros().toPlainString();
    }


    /**
     * 求对数值
     * @param base 底数
     * @param real 真数
     * @return
     */
    public static double log(double base, double real){
        return Math.log(real)/Math.log(base);
    }

    /**
     * 求绝对值
     * @param num 数字
     * @return
     */
    public static String abs(String num) {
        return new BigDecimal(num).abs().stripTrailingZeros().toPlainString();
    }

    /**
     * 求完全n叉数树的层数,从1开始
     * @param base 树的分支数
     * @param ordinal 节点编号,从1开始
     * @return
     */
    public static int treeLayer(Integer base, Long ordinal){
        if(base==0 || ordinal==null || ordinal==0){
            return 1;
        }
        int val = (int)(Math.log(ordinal.doubleValue())/Math.log(base.doubleValue()));
        return val+1;
    }


    public static void main(String[] args) {
        for(Long i=1L, size = 20L; i< size; ++i) {
            StrTool.pl(i+", layer="+treeLayer(2, i));
        }
    }

    // 加法

    public static String strAddDown4(String aa, String bb){
        return strAdd(aa, bb, 4, ROUND_DOWN);
    }
    public static String strAddDown6(String aa, String bb){
        return strAdd(aa, bb, 6, ROUND_DOWN);
    }

    public static String strAdd(String aa, String bb){
        return strAdd(aa, bb, 6, null);
    }

    public static String strAdd(String aa, String bb, int scale, Integer roundingMode){
        BigDecimal bd = new BigDecimal(aa).add(new BigDecimal(bb));
        if(roundingMode!=null) {
            bd.setScale(scale, roundingMode);
        }
        return bd.stripTrailingZeros().toPlainString();
    }

    // 减法

    public static String strSubtractDown6(String aa, String bb){
        return strSubtract(aa, bb, 6, ROUND_DOWN);
    }

    public static String strSubtract(String aa, String bb){
        return strSubtract(aa, bb, 6, null);
    }

    public static String strSubtract(String aa, String bb, int scale, Integer roundingMode){
        BigDecimal bd = new BigDecimal(aa).subtract(new BigDecimal(bb));
        if(roundingMode!=null) {
            bd.setScale(scale, roundingMode);
        }
        return bd.stripTrailingZeros().toPlainString();
    }

    // 乘法

    public static String strMultiplyManyDown4(String aa, String ...bbs){
        return strMultiplyManyDown(4, aa, bbs);
    }
    public static String strMultiplyManyDown(int scale, String aa, String ...bbs){
        if(StringUtils.isBlank(aa)){
            aa = SignConst.STR_ONE;
        }
        BigDecimal bda = new BigDecimal(aa);
        for(String bb : bbs){
            if(StringUtils.isBlank(bb)){
                continue;
            }
            bda = bda.multiply(new BigDecimal(bb)).setScale(scale, ROUND_DOWN);
        }
        return bda.setScale(scale, ROUND_DOWN).stripTrailingZeros().toPlainString();
    }
    public static String strMultiplyDown4(String aa, String bb){
        return strMultiply(aa, bb, 4, ROUND_DOWN);
    }
    public static String strMultiplyDown6(String aa, String bb){
        return strMultiply(aa, bb, 6, ROUND_DOWN);
    }

    //判断时候是正的数值 (包括浮点字符串,不能是科学计数法)
    public static boolean isNumber(String str){
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }
    public static String strMultiply(String aa, String bb){
        return strMultiply(aa, bb, 6, null);
    }

    public static String strMultiply(String aa, String bb, int scale, Integer roundingMode){
        BigDecimal bd = new BigDecimal(aa).multiply(new BigDecimal(bb));
        if(roundingMode!=null) {
            bd.setScale(scale, roundingMode);
        }
        return bd.stripTrailingZeros().toPlainString();
    }




    // 除法

    public static String longDivideDown6(long aa, long bb){
        return strDivide(aa+"", bb+"", 6, ROUND_DOWN);
    }
    public static String intDivideDown6(int aa, int bb){
        return strDivide(aa+"", bb+"", 6, ROUND_DOWN);
    }
    public static String strDivideDown4(String aa, String bb){
        if(StrTool.inAny(bb, BLANK, BLANK_SPACE, STR_ZERO)){
            return STR_ZERO;
        }
        try{
            return strDivide(aa, bb, 4, ROUND_DOWN);
        }catch (Exception ex){
            return "";
        }
    }
    public static String strDivideDown6(String aa, String bb){
        return strDivide(aa, bb, 6, ROUND_DOWN);
    }

    public static String strDivideDef(String aa, String bb){
        return strDivide(aa, bb, 6, null);
    }

    public static String strDivide(String aa, String bb, int scale, Integer roundingMode){
        if(StrTool.inAny(bb, BLANK, BLANK_SPACE, STR_ZERO)){
            return STR_ZERO;
        }
        roundingMode = roundingMode == null ? BigDecimal.ROUND_DOWN : roundingMode;
        BigDecimal bd = new BigDecimal(aa).divide(new BigDecimal(bb), scale, roundingMode);
        return bd.stripTrailingZeros().toPlainString();
    }

    // 比较
    public static int compare(String aa, String bb){
        return new BigDecimal(aa).compareTo(new BigDecimal(bb));
    }
    public static int strCompare(String aa, String bb) {
        return new BigDecimal(aa).compareTo(new BigDecimal(bb));
    }


}
