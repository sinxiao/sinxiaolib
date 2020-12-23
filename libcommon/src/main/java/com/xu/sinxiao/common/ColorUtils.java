package com.xu.sinxiao.common;

import androidx.annotation.ColorInt;

public class ColorUtils {
    @ColorInt
    public static final int TRANSPARENT = 0;
    @ColorInt
    public static final int WHITE10 = 452984831;
    @ColorInt
    public static final int WHITE20 = 872415231;
    @ColorInt
    public static final int WHITE30 = 1308622847;
    @ColorInt
    public static final int WHITE40 = 1728053247;
    @ColorInt
    public static final int WHITE50 = -2130706433;
    @ColorInt
    public static final int WHITE60 = -1711276033;
    @ColorInt
    public static final int WHITE70 = -1275068417;
    @ColorInt
    public static final int WHITE80 = -855638017;
    @ColorInt
    public static final int WHITE90 = 1191182335;
    @ColorInt
    public static final int WHITE = -1;
    @ColorInt
    public static final int BLACK10 = 436207616;
    @ColorInt
    public static final int BLACK20 = 855638016;
    @ColorInt
    public static final int BLACK30 = 1291845632;
    @ColorInt
    public static final int BLACK40 = 1711276032;
    @ColorInt
    public static final int BLACK50 = -2147483648;
    @ColorInt
    public static final int BLACK60 = -1728053248;
    @ColorInt
    public static final int BLACK70 = -1291845632;
    @ColorInt
    public static final int BLACK80 = -872415232;
    @ColorInt
    public static final int BLACK90 = 1174405120;
    @ColorInt
    public static final int BLACK = -16777216;

    @ColorInt
    public static int alphaColor(float alpha, @ColorInt int color) {
        return Math.round(alpha * (float) (color >>> 24)) << 24 | color & 16777215;
    }
}
