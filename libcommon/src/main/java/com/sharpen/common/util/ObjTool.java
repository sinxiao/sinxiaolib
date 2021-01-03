package com.sharpen.common.util;

import android.util.Log;

import com.sharpen.common.consts.SignConst;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.hutool.core.map.MapUtil;

public class ObjTool {
  public static final String TAG = "ObjTool";

  private static Logger log = LoggerFactory.getLogger(ObjTool.class);

  public static String blankOrStrDefault(String eqStr, String condVal, String defVal) {
    if (StringUtils.isBlank(condVal) || StringUtils.equals(eqStr, condVal)) {
      return defVal;
    } else {
      return condVal;
    }
  }


  /**
   * condition为true返回orign, 否则返回val值
   *
   * @param condition
   * @param orig
   * @param val
   * @param <T>
   * @return
   */
  public static <T> T trueVal(Boolean condition, T orig, T val) {
    if (condition == null) {
      condition = false;
    }
    return Boolean.TRUE.equals(condition) ? orig : val;
  }

  /**
   * orig不为空,返回orign, 否则返回val值
   *
   * @param orig
   * @param val
   * @param <T>
   * @return
   */
  public static <T> T nullDefault(T orig, T val) {
    return orig != null ? orig : val;
  }


  public static boolean noNullNumber(Number number) {
    return !nullNumber(number);
  }

  public static boolean no0Any(Number ...numbers) {
    for(Number num : numbers){
      if(nullNumber(num)){
        return false;
      }

    }
    return true;
  }


  public static boolean nullNumber(Number number) {
    return number == null || number.intValue() == 0;
  }

  public static boolean nullObj(Object obj) {
    return obj == null;
  }

  public static boolean noNullObj(Object obj) {
    return obj != null;
  }

  /**
   * 对象属性值拷贝
   *
   * @param origObj     原对象，有值的对象
   * @param distObj     目标对象，需要被设置值的对象
   * @param propMapStrs 属性映射字符串，用英文逗号分隔
   * @param conv        属性的键值对是否要转换
   */
  public static void objCopy(Object origObj, Object distObj, String propMapStrs, Boolean conv) {
    if (origObj == null || distObj == null || StringUtils.isBlank(propMapStrs)) {
      return;
    }
    conv = conv == null ? false : conv;
    objCopy(origObj, distObj, propMapStrs.split(","), conv);
  }

  /**
   * 对象属性值拷贝
   *
   * @param origObj     原对象，有值的对象
   * @param distObj     目标对象，需要被设置值的对象
   * @param propMapStrs 属性映射字符串数组
   * @param conv        属性的键值对是否要转换
   */
  public static void objCopy(Object origObj, Object distObj, String[] propMapStrs, Boolean conv) {
    if (origObj == null || distObj == null || ArrayUtils.isEmpty(propMapStrs)) {
      return;
    }
    conv = conv == null ? false : conv;
    Map<String, String> propMap = CollectionTool.array2map(propMapStrs);
    if (conv) {
      propMap = MapUtils.invertMap(propMap);
    }
    objCopy(origObj, distObj, propMap);
  }

  /**
   * 对象属性值拷贝
   *
   * @param origObj 原对象，有值的对象
   * @param distObj 目标对象，需要被设置值的对象
   */
  public static void objCopy(Object origObj, Object distObj, Map<String, String> propMap) {
    if (origObj == null || distObj == null || MapUtils.isEmpty(propMap)) {
      return;
    }
    for (Entry<String, String> enty : propMap.entrySet()) {
      Method origMethod = getMethodByNameFromObj(origObj, enty.getKey());
      if (origMethod == null) {
        Log.d(TAG, "origMethod is inextance, method name=" + enty.getKey());
        continue;
      }
      try {
        Object origPropVal = origMethod.invoke(origObj);
        if (origPropVal != null) {
          Method distPropSetMethod = setMethodByNameFromObj(distObj, enty.getValue(),
              origPropVal.getClass());
          distPropSetMethod.invoke(distObj, origPropVal);
        }
      } catch (Exception e) {
        Log.d(TAG, "invoke val fail", e);
        return;
      }
    }
  }


  /**
   * 获取bean对象的get方法
   *
   * @param obj  对象
   * @param prop 对象的属性
   */
  @SuppressWarnings("rawtypes")
  public static Method getMethodByNameFromObj(Object obj, String prop) {
    if (obj == null || StringUtils.isBlank(prop)) {
      return null;
    }
    Class cla = obj.getClass();
    return getMethodByName(cla, prop);
  }


  /**
   * 获取bean对象的get方法
   *
   * @param cla  对象的类
   * @param prop 对象的属性
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static Method getMethodByName(Class cla, String prop) {
    String methodName = "get" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
    Method method = null;
    try {
      method = cla.getMethod(methodName);
    } catch (Exception e) {
      try {
        method = cla.getMethod(prop);
      } catch (Exception e2) {
        Log.d(TAG, cla.getName() + " inexistence methodName " + prop + "," + methodName);
      }
    }
    return method;
  }


  /**
   * 获取bean对象的set方法
   *
   * @param obj    对象
   * @param prop   对象的属性
   * @param valCla 对象的属性的类
   */
  @SuppressWarnings("rawtypes")
  public static Method setMethodByNameFromObj(Object obj, String prop, Class valCla) {
    if (obj == null || StringUtils.isBlank(prop)) {
      return null;
    }
    Class cla = obj.getClass();
    return setMethodByName(cla, prop, valCla);
  }

  // 收集bit位的值
  public static List<Long> splitBit(long src) {
    List<Long> bi = new ArrayList<Long>();
    while (src > 0) {
      long v = -src & src;
      bi.add(v);
      src -= v;
    }
    return bi;
  }

  /**
   * 获取bean对象的set方法
   *
   * @param cla    对象的类
   * @param prop   对象的属性
   * @param valCla 对象的属性的类
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static Method setMethodByName(Class cla, String prop, Class valCla) {
    String methodName = "set" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
    Method method = null;
    try {
      method = cla.getMethod(methodName, valCla);
    } catch (Exception e) {
      try {
        method = cla.getMethod(prop, valCla);
      } catch (Exception e2) {
        Log.d(TAG, cla.getName() + " inexistence methodName " + prop + "," + methodName + ",valCla="
            + valCla);
      }
    }
    return method;
  }


  public static BigDecimal str2moneyDb(String str, int decimal, RoundingMode roundingMode) {
    if (StringUtils.isBlank(str)) {
      return new BigDecimal(0);
    }
    BigDecimal bd = new BigDecimal(str);
    bd.setScale(decimal, roundingMode);
    return bd.stripTrailingZeros();
  }

  public static String intMoney2str(Integer money, int divide) {
    if (money == null) {
      return SignConst.STR_ZERO;
    }

    return new BigDecimal(money).divide(new BigDecimal(divide)).stripTrailingZeros().toPlainString();
  }

  public static Long str2long(String str) {
    if (StringUtils.isBlank(str)) {
      return null;
    }
    return Long.valueOf(str);
  }

  public static Integer str2int(String str) {
    if (StringUtils.isBlank(str)) {
      return null;
    }
    return Integer.valueOf(str);
  }

  public static Date str2date(String str) {
    if (StringUtils.isBlank(str)) {
      return null;
    }
    return DateUtil.parse(str);
  }

  public static String long2str(Long obj) {
    return obj == null ? "" : obj.toString();
  }

  public static String int2str(Integer obj) {
    return obj == null ? "" : obj.toString();
  }

  public static String date2str(Date obj) {
    return obj == null ? "" : DateUtil.formatDateTime(obj);
  }

  public static <T> Set<T> objs2set(T... obj) {
    return new HashSet<>(Arrays.asList(obj));
  }

  public static boolean noEqualsNumber(Number obj1, Number obj2) {
    return !equalsNumber(obj1, obj2);
  }
  public static boolean equalsNumber(Number obj1, Number obj2) {
    if (obj1 == null && obj2 == null) {
      return true;
    }
    if (obj1 == null || obj2 == null) {
      return false;
    }
    return obj1.longValue() == obj2.longValue();
  }


  public Map<String, Object> map2map(Map<String, Object> valMap, Map<String, String> propMap) {
    if (MapUtil.isEmpty(valMap) || MapUtil.isEmpty(propMap)) {
      return valMap;
    }
    Map<String, Object> newMap = new HashMap<>();
    for (Entry<String, String> prop : propMap.entrySet()) {
      Object val = valMap.get(prop.getKey());
      if (val != null) {
        newMap.put(prop.getValue(), val);
      }
    }
    return newMap;
  }

  public static void closeStream(Reader reader, Writer writer, InputStream is, OutputStream os){
    try{
      if(reader!=null){
        reader.close();
      }
      if(writer!=null){
        writer.close();
      }
      if(is!=null){
        is.close();
      }
      if(os!=null){
        os.close();
      }

    }catch (Exception e){
      e.printStackTrace();
    }

  }
}
