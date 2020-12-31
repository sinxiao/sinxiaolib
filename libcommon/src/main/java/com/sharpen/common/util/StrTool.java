package com.sharpen.common.util;

import android.util.Log;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharpen.common.consts.SignConst;
import com.sharpen.common.consts.SymbolConst;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.MDC;

import java.io.File;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;

/**
 * 字符串相关的工具
 */
public class StrTool {
  public static final String TAG = "StrTool";
  public static ObjectMapper jacksonMapper = new ObjectMapper();

  static {
    jacksonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    jacksonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  public static final char[] VIEW_CODE="QT23456789ABCDEFGHKMNPRSVWXY".toCharArray();

  public static void p(Object o) {
    System.out.print(o);
  }

  public static void p(String... cmds) {
    StringBuilder text = new StringBuilder("$ ");
    for (String cmd : cmds) {
      text.append(cmd).append(" ");
    }
    System.out.println(text.toString());
  }

  public static void pl(Object o) {
    System.out.println(o);
  }

  public static void pl() {
    System.out.println();
  }

  /**
   * 如果原值为空白，用默认值替换
   * @param orig 原值
   * @param defVal 默认值
   * @return
   */
  public static String blankDef(String orig, String defVal){
    return StringUtils.isBlank(orig)? defVal : orig;
  }

  /**
   * 将对象转成json字符串
   * @param obj
   * @return
   */
  public static String obj2json(Object obj){
    try {
      return jacksonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }catch (Exception e){
      Log.d("jacksonMapperFail {}", e.getMessage(), e);
    }
    return null;
  }

  /**
   * 将json字符串转成对象
   * @param json JSON格式字符串
   * @return
   */
  public static <T> T json2obj(String json, Class<T> cls){
    if(StringUtils.isBlank(json)){
      return null;
    }
    try {
      return jacksonMapper.readValue(json, cls);
    }catch (Exception e){
      Log.d("json2objClsFail {}", e.getMessage(), e);
    }
    return null;
  }

  /**
   * 将json字符串转成对象
   * @param json JSON格式字符串
   * @return
   */
  public static <T> List<T> json2list(String json, Class<T> cls){
    if(StringUtils.isBlank(json)){
      return null;
    }
    try {
      List<T> objs =  jacksonMapper.readValue(json, new TypeReference<ArrayList<T>>(){});
      return objs;
    }catch (Exception e){
      Log.d("json2objClsFail {}", e.getMessage(), e);
    }
    return null;
  }
  /**
   * 将json字符串转成对象
   * @param json JSON格式字符串
   * @return
   */
  public static JsonNode json2node(String json){
    if(StringUtils.isBlank(json)){
      return null;
    }
    try {
       JsonNode jsonObj = jacksonMapper.readTree(json);
       return jsonObj;
    }catch (Exception e){
      Log.d("json2objClsFail {}", e.getMessage(), e);
    }
    return null;
  }

  /**
   * 将json字符串转成对象
   * @param json JSON格式字符串
   * @return
   */
  public static <T> T json2obj(String json, TypeReference<T> type){
    if(StringUtils.isBlank(json)){
      return null;
    }
    try {
      return jacksonMapper.readValue(json, type);
    }catch (Exception e){
      Log.d("json2objTypeFail {}", e.getMessage(), e);
    }
    return null;
  }


  public static String pack2dir(String pack) {
    if (StringUtils.isBlank(pack)) {
      return "";
    }
    return pack.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
  }

  /**
   * 能显示在文件名中的时间
   *
   * @return
   */
  public static String fnt() {
    return DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT);
  }

  /**
   * 字符串的首字母转成大写，例filedName被转成FieldName
   *
   * @param originStr
   * @return
   */
  public static String upperFirst(String originStr) {
    if (StringUtils.isNotBlank(originStr)) {
      return originStr.substring(0, 1).toUpperCase() + originStr.substring(1);
    }
    return originStr;
  }


  /**
   * 是否包含空白字符串
   * @param strs
   * @return
   */
  public static boolean hasBlank(String ...strs){
    if(strs == null){
      return true;
    }
    for(String str : strs){
      if(StringUtils.isBlank(str)){
        return true;
      }
    }
    return false;
  }

  /**
   * 是否全部不为空白，即不含空白字符串
   * @param strs
   * @return
   */
  public static Boolean noBlank(String ...strs){
    if(ArrayUtils.isEmpty(strs)){
      return true;
    }
    for(String str : strs ){
      if(StringUtils.isBlank(str)){
        return false;
      }
    }
    return true;
  }

  /**
   * 获取第一个不为空白的字符串
   * @param strs
   * @return
   */
  public static String valNoBlank(String ...strs){
    if(ArrayUtils.isEmpty(strs)){
      return null;
    }
    for(String str : strs ){
      if(StringUtils.isNotBlank(str)){
        return str;
      }
    }
    return null;
  }

  /**
   * 是否包含空白字符串
   * @param strs
   * @return
   */
  public static boolean inAny(String judge, String ...strs){
    if(strs == null){
      return false;
    }
    for(String str : strs){
      if(StringUtils.equals(judge, str)){
        return true;
      }
    }
    return false;
  }

  /**
   * 值转换。如果没有匹配的值，返回默认值，否则返回
   * @param matchVal
   * @param defVal
   * @param candidate
   * @return
   */
  public static String valConvert(String matchVal, String defVal, String ...candidate){
    if(candidate == null || candidate.length<2){
      return defVal;
    }
    for(int i=0, leng = candidate.length-1; i<leng; i+=2){
      if(StringUtils.equals(matchVal,candidate[i])){
        return candidate[i+1];
      }
    }
    return defVal;
  }

  /**
   * 表的属性转成变量名
   *
   * @param dbStr
   * @return
   */
  public static String db2var(String dbStr) {
    if (StringUtils.isBlank(dbStr) || !StringUtils.contains(dbStr, SymbolConst.UNDERSCORE)) {
      return dbStr;
    }
    StringBuilder sb = new StringBuilder();
    for (String str : dbStr.split(SymbolConst.UNDERSCORE)) {
      sb.append(StringUtils.isBlank(sb) ? str : upperFirst(str));
    }
    return sb.toString();
  }

  public static String db2cls(String dbStr) {
    return upperFirst(db2var(dbStr));
  }


  /**
   * 删除开始的65279字符
   */
  public static String removeStart65279(String str) {
    char cha = 65279;
    while (str.startsWith(cha + "")) {
      if (str.length() == 1) {
        return "";
      }
      str = str.substring(1);
    }
    return str;
  }

  public static StringWriter ve2sw(VelocityEngine ve, String vePath, VelocityContext context) {
    StringWriter writer = new StringWriter();
    Template template = ve.getTemplate(vePath);
    template.merge(context, writer);
    return writer;
  }

  // static VelocityEngine veDefault = new VelocityEngine();

  /**
   * 获取默认VelocityEngine对象
   * ResourceLoader参考: https://www.iteye.com/blog/zhouzba-2093650
   */
  public static VelocityEngine defaultVe() {
    return defaultVe(null);
  }

  public static VelocityEngine veAbsolutePath() {
    Properties prop = new Properties();
    prop.put("resource.loader", "file");
    prop.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, "");
    prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
    VelocityEngine ve = StrTool.defaultVe(prop);
    return ve;
  }

  // static VelocityEngine veDefault = new VelocityEngine();

  /**
   * 获取默认VelocityEngine对象
   * ResourceLoader参考: https://www.iteye.com/blog/zhouzba-2093650
   */
  public static VelocityEngine defaultVe(Properties prop) {
    VelocityEngine veDefault = new VelocityEngine();
    veDefault.setProperty("input.encoding", StandardCharsets.UTF_8.name());
    Properties p = new Properties();
    p.put("file.resource.loader.class",
        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    p.put("directive.foreach.counter.name", "velocityCount");
    p.put("directive.foreach.counter.initial.value", 1);
    p.put("input.encoding", StandardCharsets.UTF_8.name());
    p.put("output.encoding", StandardCharsets.UTF_8.name());
    if (MapUtils.isNotEmpty(prop)) {
      p.putAll(prop);
    }

    veDefault.init(p);
    return veDefault;
  }

  public static String mask(String str) {
    return mask(str, 2, 6);
  }

  public static String mask(String str, Integer type, Integer hideLeng) {
    if (StringUtils.isBlank(str)) {
      return str;
    }
    int start = 0, end = str.length(), leng = str.length();
    if (leng > hideLeng) {
      if (type == SignConst.ONE.intValue()) {
        // 隐藏前面
        end = hideLeng;
      } else if (type == SignConst.TWO.intValue()) {
        // 隐藏中间
        start = (leng - hideLeng) / 2;
        end = start + hideLeng;
      } else if (type == SignConst.THREE.intValue()) {
        // 隐藏末尾
        start = leng - hideLeng;
      } else if (type == SignConst.FOUR.intValue()) {
        // 隐藏两边
        start = leng - hideLeng;
        return StrUtil.hide(StrUtil.hide(str, 0, (leng - hideLeng) / 2), (leng + hideLeng) / 2, end);
      }
    }
    //
    return StrUtil.hide(str, start, end);
  }


  public static AtomicInteger order = new AtomicInteger(9999999);

  public static String getId() {
    return System.currentTimeMillis() / 1000 + "" + order.addAndGet(1);
  }

  public static long getIdLong() {
    return Long.valueOf(getId());
  }

  public static int getIdInteger() {
    return Long.valueOf(getId()).intValue();
  }

  public static String getIdStr() {
    return Long.valueOf(getId()).toString();
  }

  // 将数字转成高可读性的28进制
  public static String getViewCodeStr(long num){
    // 获取原生的28进制
    String radixStr = Long.toString(num, 28);
    // 易读版的可视化28进制
    StringBuffer viewRadix = new StringBuffer();
    for(char str : radixStr.toCharArray()){
      char radix28 = VIEW_CODE[Integer.valueOf(str+"", 28)];
      viewRadix.append(radix28);
    }
    return viewRadix.toString();
  }

  /**
   * 获取栈字符串
   */
  public static String getStackStr() {
    StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
    return getStackStr(stacks);
  }

  /**
   * 获取栈字符串
   */
  public static String getStackStr(StackTraceElement[] stackElements) {
    StringBuilder sb = new StringBuilder();
    if (stackElements != null) {
      for (int i = 0; i < stackElements.length; i++) {
        sb.append("\t\t").append(stackElements[i].getClassName() + "\t");
        sb.append(stackElements[i].getFileName() + "\t");
        sb.append(stackElements[i].getLineNumber() + "\t");
        sb.append(stackElements[i].getMethodName() + "\n");
      }
    }
    return sb.toString();
  }

  /**
   * 获取异常的栈字符串
   *
   * @param e 异常
   */
  public static String getStackStr(Exception e) {
    if (e == null) {
      return null;
    }
    StackTraceElement[] stackElements = e.getStackTrace();
    return getStackStr(stackElements);
  }

  public static final DecimalFormat df = new DecimalFormat("#0.00000000");

  public static String decimalDown(String numStr, int qty){
    return new BigDecimal(numStr).setScale(qty, RoundingMode.DOWN).stripTrailingZeros().toPlainString();
  }

  /**
   * DecimalFormat.format()会导致四舍五入，用户转账全部资产的时候会出现余额不足 直接截取8位 ref: com.ngdes.api.wallet.service.common.util.StringUtil
   */
  public static String formatAsset(BigDecimal avail) {
    if (avail == null || avail.compareTo(new BigDecimal("0")) == 0) {
      return df.format(0);
    }

    //没有小数点，或者小数点后不足8位小数
    String availString = avail.toPlainString();
    if (availString.indexOf(".") == -1
        || availString.substring(availString.indexOf(".") + 1).length() < 8) {
      return df.format(avail);
    }

    return availString.substring(0, availString.lastIndexOf(".") + 9);
  }

  public static byte[] str2byte(String str) {
    if (str == null) {
      return null;
    }
    try {
      return str.getBytes(StandardCharsets.UTF_8.name());
    } catch (Exception e) {
      Log.d(TAG, "str2byteFail", e);
      return null;
    }
  }


  /**
   * 将字符串拆分，过滤掉空白，截掉前后的空白。去重
   *
   * @param origin 需要处理的源字符串
   */
  public static LinkedHashSet<String> str2set(String origin) {
    List<String> list = str2list(origin, null);
    LinkedHashSet<String> set = new LinkedHashSet<>(list);
    return set;
  }

  /**
   * 将字符串拆分，过滤掉空白，截掉前后的空白。去重
   *
   * @param origin 需要处理的源字符串
   * @param split  拆分的分隔字符串。如果为空，默认为英文逗号:","
   */
  public static Set<String> str2set(String origin, String split) {
    List<String> list = str2list(origin, split);
    Set<String> set = new LinkedHashSet<>(list);
    return set;
  }

  /**
   * 将字符串拆分，过滤掉空白，截掉前后的空白。去重
   *
   */
  public static <T> Set<T> anySet(T ...elems) {
    Set<T> targSet = new HashSet<>();
    if(ArrayUtils.isEmpty(elems)){
      return targSet;
    }
    for(T elem : elems){
      targSet.add(elem);
    }
    return targSet;
  }

  public static Set<Long> str2Long4set(Set<String> strs) {
    Set<Long> set = new HashSet<>();
    if (CollectionUtils.isEmpty(strs)) {
      return null;
    }
    for (String str : strs) {
      if (StringUtils.isBlank(str)) {
        continue;
      }
      if (str.matches("\\d+")) {
        set.add(Long.valueOf(str));
      }
    }
    return set;
  }


  /**
   * 将字符串拆分，过滤掉空白，截掉前后的空白
   *
   * @param origin 需要处理的源字符串
   */
  public static List<String> splitManyGene(String origin){
    return splitManyGene(origin, ",","\\\n","\\\t",";",":");
  }
  /**
   * 将字符串拆分，过滤掉空白，截掉前后的空白
   *
   * @param origin 需要处理的源字符串
   * @param splits 拆分的分隔字符串。如果为空，默认为英文逗号:","
   */
  public static List<String> splitManyGene(String origin, String ...splits) {
    List<String> list = new ArrayList<>();
    if(StringUtils.isBlank(origin)){
      return list;
    }
    if(ArrayUtils.isEmpty(splits)){
      splits = new String[]{SymbolConst.COMMA};
    }
    for(String split : splits){
      if(CollectionUtils.isEmpty(list)){
        list = str2list(origin, split);
      }else {
        List<String> temp = new ArrayList<>();
        for(String str : list){
          temp.addAll(str2list(str, split));
        }
        list = temp;
      }
    }
    return list;
  }

  /**
   * 将字符串拆分，过滤掉空白，截掉前后的空白
   *
   * @param origin 需要处理的源字符串
   * @param split  拆分的分隔字符串。如果为空，默认为英文逗号:","
   */
  public static List<String> str2list(String origin, String split) {
    List<String> list = new ArrayList<>();

    if (StringUtils.isBlank(origin)) {
      return list;
    }
    origin = origin.trim();
    if (StringUtils.isBlank(split)) {
      split = SymbolConst.COMMA;
    }
    String[] strs = origin.split(split);
    for (String str : strs) {
      if (StringUtils.isBlank(str)) {
        continue;
      }
      list.add(str.trim());
    }
    return list;
  }

  /**
   * 将字符串拆分，过滤掉空白，截掉前后的空白
   *
   * @param listParam 需要处理的源字符串
   */
  public static List<String> strlistTrim(List<String> listParam) {
    if (CollectionUtils.isEmpty(listParam)) {
      return null;
    }
    List<String> list = new ArrayList<>();
    for (String str : listParam) {
      if (StringUtils.isBlank(str)) {
        continue;
      }
      list.add(str.trim());
    }
    return list;
  }

  /**
   * 将字符串转换成map。
   *
   * @param str      需要转换的字符串。例：0,买;1,卖
   * @param rowSplit 用于拆分每条记录的字符串
   * @param colSplit 用于拆分key, value的字符串。只取前两个元素作为key, value。
   */
  public static Map<String, String> str2map(String str, String rowSplit, String colSplit) {
    return str2map(str, rowSplit, colSplit, true);
  }

  /**
   * 将字符串转换成map。
   *
   * @param str      需要转换的字符串。例：0,买;1,卖
   * @param rowSplit 用于拆分每条记录的字符串
   * @param colSplit 用于拆分key, value的字符串。只取前两个元素作为key, value。
   */
  public static Map<String, String> str2map(String str, String rowSplit, String colSplit, boolean origin) {
    Map<String, String> retnMap = new HashMap<>();
    if (StringUtils.isBlank(str)) {
      return retnMap;
    }
    String[] strs = str.split(rowSplit);
    for (String strRow : strs) {
      if (StringUtils.isBlank(strRow)) {
        continue;
      }
      strRow = strRow.trim();
      String[] strs2 = strRow.split(colSplit);
      if (strs2.length < 2) {
        Log.d("lack conf, strRow={}", strRow);
        continue;
      }
      if (StringUtils.isNotBlank(strs2[0])) {
        if (origin) {
          retnMap.put(strs2[0].trim(), strs2[1].trim());
        } else {
          retnMap.put(strs2[1].trim(), strs2[0].trim());
        }
      }
    }
    return retnMap;
  }

  /**
   * 将字符串转换成map。
   *
   * @param str      需要转换的字符串。例：0,买;1,卖
   * @param rowSplit 用于拆分每条记录的字符串
   * @param colSplit 用于拆分key, value的字符串。只取前两个元素作为key, value。
   */
  public static Map<String, String> str2map(Map<String, String> map, String str, String rowSplit, String colSplit,
                                            boolean origin) {
    if (map == null) {
      return str2map(str, rowSplit, colSplit, origin);
    }
    if (MapUtils.isEmpty(map)) {
      synchronized (map) {
        if (MapUtils.isEmpty(map)) {
          map.putAll(str2map(str, rowSplit, colSplit, origin));
        }
      }
    }
    return map;
  }

  /**
   * 将字符串转换成map。
   *
   * @param str      需要转换的字符串
   * @param rowSplit 用于拆分每条记录的字符串
   * @param colSplit 用于拆分key, value的字符串。只取前三个元素作为key, key, value。
   */
  public static Map<String, Map<String, String>> str2map2(String str, String rowSplit,
                                                          String colSplit) {
    Map<String, Map<String, String>> retnMap = new HashMap<>();
    String[] strs = str.split(rowSplit);
    for (String strRow : strs) {
      if (StringUtils.isBlank(strRow)) {
        continue;
      }
      strRow = strRow.trim();
      String[] strs2 = strRow.split(colSplit);
      Map<String, String> rowMap = retnMap.get(strs2[0].trim());
      if (rowMap == null) {
        rowMap = new HashMap();
        retnMap.put(strs2[0].trim(), rowMap);
      }
      if (StringUtils.isNotBlank(strs2[2])) {
        rowMap.put(strs2[1].trim(), strs2[2].trim());
      }
    }
    return retnMap;
  }


  /**
   * 将字符串转换成map。跟str2map2的区别是：最终的值是个List，用于处理2级key重复
   *
   * @param str      需要转换的字符串
   * @param rowSplit 用于拆分每条记录的字符串
   * @param colSplit 用于拆分key, value的字符串。只取前三个元素作为key, key, value。
   */
  public static Map<String, Map<String, List<String>>> str2mapList(String str, String rowSplit,
                                                                   String colSplit) {
    Map<String, Map<String, List<String>>> retnMap = new HashMap<>();
    String[] strs = str.split(rowSplit);
    for (String strRow : strs) {
      if (StringUtils.isBlank(strRow)) {
        continue;
      }
      strRow = strRow.trim();
      String[] strs2 = strRow.split(colSplit);
      Map<String, List<String>> rowMap = retnMap.get(strs2[0].trim());
      if (rowMap == null) {
        rowMap = new HashMap<>();
        retnMap.put(strs2[0].trim(), rowMap);
      }
      List<String> elemList = rowMap.get(strs2[1].trim());
      if (elemList == null) {
        elemList = new ArrayList<>();
        rowMap.put(strs2[1].trim(), elemList);
      }
      if (StringUtils.isNotBlank(strs2[2])) {
        elemList.add(strs2[2].trim());
      }
    }
    return retnMap;
  }

  public static <T> String map2urlPath(Map<String, T> map) {
    return map2urlPath(map, null);
  }

  public static <T> String mapStr2urlPath(Map<String, T> map, String ignoreKey) {
    return map2urlPath(map, new HashSet<>(Arrays.asList(ignoreKey)));
  }

  public static <T> String map2urlPath(Map<String, T> map, Set<String> ignoreKeys) {
    StringBuilder sb = new StringBuilder();
    TreeMap<String, T> treeMap = new TreeMap<>(map);
    int i = 0, leng = treeMap.size();
    for (Map.Entry<String, T> entry : treeMap.entrySet()) {
      ++i;
      if (ignoreKeys != null && ignoreKeys.contains(entry.getKey())) {
        continue;
      }
      sb.append(entry.getKey()).append(SymbolConst.EQUALS).append(entry.getValue());
      if (i != leng) {
        sb.append(SymbolConst.AMPERSAND);
      }
    }
    return sb.toString();
  }

  /**
   * 16进制转成long类型
   *
   * @author Justin
   */
  public static long hex2long(String hex) {
    if (StringUtils.isBlank(hex)) {
      return 0;
    }
    byte[] bys = parseHexStr2Byte(hex);
    long lng = 0;
    for (byte by : bys) {
      lng = (lng << 8) | (by & 0xff);
    }
    return lng;
  }


  /**
   * 计算MD5
   *
   * @return 小写的16进制的md5值
   */
  public static String md5(byte[] plaintextByte) {
    try {
      MessageDigest mdInst = MessageDigest.getInstance("MD5");
      // 使用指定的字节更新摘要
      mdInst.update(plaintextByte);
      // 获得密文
      byte[] md = mdInst.digest();
      String ciphertext = Hex.encodeHexString(md);
      return ciphertext;
    } catch (Exception e) {
      Log.d(TAG, e.getMessage(), e);
    }
    return null;
  }


  /**
   * 计算MD5
   */
  public static String md5(String str) {
    try {
      return md5(str.getBytes(StandardCharsets.UTF_8.name()));
    } catch (Exception e) {
      Log.d(TAG, e.getMessage(), e);
    }
    return null;

  }

  /**
   * 字符串转成摘要，再转成long类型
   *
   * @author Justin
   */
  public static long str2long(String str) {
    String md5Str = md5(str);
    int leng = md5Str.length();
    return hex2long(md5Str.substring(leng - 16, leng));
  }


  /**
   * 字符串用utf-8转成字节，再转成16进制
   */
  public static String str2utf8HexLow(String str) {
    if (str == null) {
      return null;
    }
    try {
      String hexStr = Hex.encodeHexString(str.getBytes(StandardCharsets.UTF_8));
      return hexStr;
    } catch (Exception e) {
      Log.d(TAG, e.getMessage(), e);
    }
    return null;
  }

  /**
   * 16进制字符串用转成字节，再用utf-8转成字符串，再转成
   *
   * @param hex 小写的16进制字符串
   */
  public static String hex2utf8Str(String hex) {
    if (hex == null) {
      return null;
    }
    try {
      String str = new String(Hex.decodeHex(hex.toCharArray()), StandardCharsets.UTF_8);
      return str;
    } catch (Exception e) {
      Log.d(TAG, e.getMessage(), e);
    }
    return null;
  }


  /**
   * 将16进制转换为二进制
   *
   * @param hexStr 16进制字符串
   */
  public static byte[] parseHexStr2Byte(String hexStr) {
    if (hexStr.length() < 1) {
      return null;
    }
    byte[] result = new byte[hexStr.length() / 2];
    for (int i = 0; i < hexStr.length() / 2; i++) {
      int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
      int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
          16);
      result[i] = (byte) (high * 16 + low);
    }
    return result;
  }


  /**
   * 获取json里面的值
   *
   * @param json json字符串
   * @param propStr 用英文逗号分隔的属性名，例："results,status"
   */
  public static String getJsonVal(String json, String propStr) {
    if (StringUtils.isBlank(propStr)) {
      return json;
    }
    String[] props = propStr.split(",");
    return getJsonVal(json, props);
  }

  public static String getJsonVal(String json, String[] props) {
    if (StringUtils.isBlank(json) || props == null || props.length == 0) {
      return json;
    }
    try {
      JsonNode jsonObj = jacksonMapper.readTree(json);
      return getJsonVal(jsonObj, props);
    }catch (Exception e){
      Log.d("readTreeFail {}", e.getMessage(), e);
    }
    return null;
  }


  public static String getJsonVal(JsonNode jsonObj, String propStr) {
    if (jsonObj == null || StringUtils.isBlank(propStr)) {
      return null;
    }
    String[] props = propStr.split(",");
    return getJsonVal(jsonObj, props);
  }

  public static String getJsonVal(JsonNode jsonObj, String[] props) {
    JsonNode jo = jsonObj;
    if (jo == null || props == null || props.length == 0) {
      return null;
    }
    JsonNode node = null;
    for (int i = 0, l = props.length; i < l; ++i) {
      String p = props[i];
      node = jo.get(p);
      if (node == null) {
        return null;
      }
      if (i == l - 1) {
        String str = node.asText();
        if(StringUtils.isBlank(str)){
          try {
            return jacksonMapper.writeValueAsString(node);
          }catch (Exception e){
            Log.d(TAG, "writeValueAsStringFail "+ e.getMessage(), e);
          }

        }
      }
      jo = node;
    }
    return node.asText();
  }

  public static Set<String> localIPAll() {
    Set<String> ips = new HashSet<>();

    try {
      InetAddress candidateAddress = null;
      // 遍历所有的网络接口
      for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
           ifaces.hasMoreElements(); ) {
        NetworkInterface iface = ifaces.nextElement();
        // 在所有的接口下再遍历IP
        for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses();
             inetAddrs.hasMoreElements(); ) {
          InetAddress inetAddr = inetAddrs.nextElement();
          if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
            if (inetAddr.isSiteLocalAddress()) {
              // 如果是site-local地址，就是它了
              ips.add(inetAddr.getHostAddress());
            } else if (candidateAddress == null) {
              // site-local类型的地址未被发现，先记录候选地址
              candidateAddress = inetAddr;
            }
          }
        }
      }
      if (candidateAddress != null) {
        ips.add(candidateAddress.getHostAddress());
      }
      // 如果没有发现 non-loopback地址.只能用最次选的方案
      InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
    } catch (Exception e) {
      Log.d(TAG, e.getMessage(), e);
    }

    return ips;
  }

  /**
   * 版本号比较
   *
   * @param v1
   * @param v2
   * @return 0代表相等，1代表左边大，-1代表右边大
   * Utils.compareVersion("1.0.358","1.0.358")=1
   */
  public static int compareVersion(String v1, String v2) {
    if (v1.equals(v2)) {
      return 0;
    }
    String[] version1Array = v1.split("[._]");
    String[] version2Array = v2.split("[._]");
    int index = 0;
    int minLen = Math.min(version1Array.length, version2Array.length);
    long diff = 0;

    while (index < minLen
            && (diff = Long.parseLong(version1Array[index])
            - Long.parseLong(version2Array[index])) == 0) {
      index++;
    }
    if (diff == 0) {
      for (int i = index; i < version1Array.length; i++) {
        if (Long.parseLong(version1Array[i]) > 0) {
          return 1;
        }
      }

      for (int i = index; i < version2Array.length; i++) {
        if (Long.parseLong(version2Array[i]) > 0) {
          return -1;
        }
      }
      return 0;
    } else {
      return diff > 0 ? 1 : -1;
    }
  }

  public static void main(String[] args) {
    pl("getViewCodeStr="+getViewCodeStr(28*28*28*28*28+28));

  }

  public String rsaDecrypt(String privateKey,String cipherBase64){
    RSA rsa=new RSA(privateKey,null);
    byte[] plainBytes=rsa.decryptFromBase64(cipherBase64, KeyType.PrivateKey);
    return new String(plainBytes);
  }

  public String rsaEncrypt(String publicKey,byte[] plainBytes){
    RSA rsa=new RSA(null,publicKey);
    byte[] plainText=rsa.encrypt(plainBytes, KeyType.PublicKey);
    return new String(plainText);
  }

}
