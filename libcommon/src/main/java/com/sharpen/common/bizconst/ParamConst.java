package com.sharpen.common.bizconst;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 参数相关的常量
 */
public class ParamConst {

    public static final String T_BASECOIN = "baseCoin";
    // 创世币
    public static final String B_ROOTCOIN= "rootCoin";
    // 计价币，多个用英文逗号分隔
    public static final String B_PRICECOIN = "priceCoin";
    // 币标识对.只能是3个英文大写字母，例： RUX/UST,QQQ/UST,QQQ/RUX
    public static final String B_COINNAMEPAIR = "coinNamePair";

}
