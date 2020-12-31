package com.sharpen.common.util;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_DOWN;

/**
 * 数学工具
 * @author Justin
 */
public class MathTool {

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

  public static String strMultiplyDown6(String aa, String bb){
    return strMultiply(aa, bb, 6, ROUND_DOWN);
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
  public static String strDivideDown6(String aa, String bb){
    return strDivide(aa, bb, 6, ROUND_DOWN);
  }

  public static String strDivideDef(String aa, String bb){
    return strDivide(aa, bb, 6, null);
  }

  public static String strDivide(String aa, String bb, int scale, Integer roundingMode){
    BigDecimal bd = new BigDecimal(aa).divide(new BigDecimal(bb));
    if(roundingMode!=null) {
      bd.setScale(scale, roundingMode);
    }
    return bd.stripTrailingZeros().toPlainString();
  }

  // 比较
  public static int compare(String aa, String bb){
    return new BigDecimal(aa).compareTo(new BigDecimal(bb));
  }


}
