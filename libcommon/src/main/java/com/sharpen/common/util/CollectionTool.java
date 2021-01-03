package com.sharpen.common.util;

import com.sharpen.common.consts.SymbolConst;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CollectionTool {

    private static Logger log = LoggerFactory.getLogger(CollectionTool.class);

    public static <T> T multiMapGet(int modelInt, List<ConcurrentMap<String, T>> list, String key) {
        if (list == null) {
            log.info("multiMapGet list must init, for sync");
            return null;
        }

        modelInt = modelInt < 1 ? 1 : modelInt;
        long modelNum = Math.abs(StrTool.str2long(key)) % modelInt;
        if (list.size() < modelInt) {
            return null;
        }
        ConcurrentMap<String, T> map = list.get(new Long(modelNum).intValue());
        T obj = map.get(key);
        return obj;
    }

    public static <T> T multiMapRemove(int modelInt, List<ConcurrentMap<String, T>> list,
                                       String key) {
        if (list == null) {
            log.info("multiMapGet list must init, for sync");
            return null;
        }

        modelInt = modelInt < 1 ? 1 : modelInt;
        long modelNum = Math.abs(StrTool.str2long(key)) % modelInt;
        if (list.size() < modelInt) {
            return null;
        }
        ConcurrentMap<String, T> map = list.get(new Long(modelNum).intValue());
        T obj = map.remove(key);
        return obj;
    }


    public static <T> T multiMapPut(int modelInt, List<ConcurrentMap<String, T>> list, String key,
                                    T val) {
        if (list == null) {
            log.info("multiMapGet list must init, for sync");
            return null;
        }

        long modelNum = Math.abs(StrTool.str2long(key)) % modelInt;
        if (list.size() < modelInt) {
            synchronized (list) {
                if (list.size() < modelInt) {
                    for (int i = 0, leng = modelInt - list.size(); i < leng; ++i) {
                        list.add(new ConcurrentHashMap<String, T>());
                    }
                }
            }
        }
        try {
            ConcurrentMap<String, T> map = list.get(new Long(modelNum).intValue());
            map.put(key, val);
            return val;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }


    public static <K, T> T multiMapBitGet(int bitLeng, List<ConcurrentMap<K, T>> list, K key) {
        if (list == null || key == null) {
            log.info("null is null or multiMapGet list must init, for sync");
            return null;
        }

        bitLeng = bitLeng > 10 ? 10 : bitLeng;
        long keyInt = key instanceof Long ? (Long) key : key instanceof Integer ? (Integer) key :
                key.toString().matches("-?[0-9]+") ? Long.valueOf(key.toString())
                        : StrTool.str2long(key.toString());
        keyInt = Math.abs(keyInt);
        int modelInt = 1 << bitLeng;
        long modelNum = (modelInt - 1) & keyInt;
        if (list.size() < modelInt) {
            return null;
        }
        ConcurrentMap<K, T> map = list.get(new Long(modelNum).intValue());
        T obj = map.get(key);
        return obj;
    }

    public static <K, T> T multiMapBitRemove(int bitLeng, List<ConcurrentMap<K, T>> list, K key) {
        if (list == null || key == null) {
            log.info("null is null or multiMapGet list must init, for sync");
            return null;
        }

        bitLeng = bitLeng > 10 ? 10 : bitLeng;
        long keyInt = key instanceof Long ? (Long) key : key instanceof Integer ? (Integer) key :
                key.toString().matches("-?[0-9]+") ? Long.valueOf(key.toString())
                        : StrTool.str2long(key.toString());
        keyInt = Math.abs(keyInt);
        int modelInt = 1 << bitLeng;
        long modelNum = (modelInt - 1) & keyInt;
        if (list.size() < modelInt) {
            return null;
        }
        ConcurrentMap<K, T> map = list.get(new Long(modelNum).intValue());
        T obj = map.remove(key);
        return obj;
    }

    /**
     * 维护一个多ConcurrentMap的list的放值。取指定bitLeng的bit跟key相与，作为list的索引，然后根据索引取对应的map来操作
     *
     * @param bitLeng 位的长度。目前最大不能超过10bit即小于1024
     * @param key     键
     */
    public static <K, T> T multiMapBitPut(int bitLeng, List<ConcurrentMap<K, T>> list, K key, T val) {
        if (list == null || key == null) {
            log.info("null is null or multiMapGet list must init, for sync");
            return null;
        }

        bitLeng = bitLeng > 10 ? 10 : bitLeng;
        long keyInt = key instanceof Long ? (Long) key : key instanceof Integer ? (Integer) key :
                key.toString().matches("-?[0-9]+") ? Long.valueOf(key.toString())
                        : StrTool.str2long(key.toString());
        keyInt = Math.abs(keyInt);
        int modelInt = 1 << bitLeng;
        long modelNum = (modelInt - 1) & keyInt;
        if (list.size() < modelInt) {
            synchronized (list) {
                if (list.size() < modelInt) {
                    for (int i = 0, leng = modelInt - list.size(); i < leng; ++i) {
                        list.add(new ConcurrentHashMap<K, T>());
                    }
                }
            }
        }
        try {
            ConcurrentMap<K, T> map = list.get(new Long(modelNum).intValue());
            map.put(key, val);
            return val;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    /**
     * 获取map的第一个键值对
     */
    public static <K, V> Entry<K, V> getHead(Map<K, V> map) {
        if (map == null) {
            return null;
        }
        return map.entrySet().iterator().next();
    }

    /**
     * 获取map的第一个元素
     */
    public static <K, V> V getHeadVal(Map<K, V> map) {
        if (map == null) {
            return null;
        }
        return map.entrySet().iterator().next().getValue();
    }

    /**
     * 获取map的最后一个键值对
     */
    public static <K, V> Entry<K, V> getTail(Map<K, V> map) {
        if (map == null) {
            return null;
        }
        Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
        Entry<K, V> tail = null;
        while (iterator.hasNext()) {
            tail = iterator.next();
        }
        return tail;
    }

    /**
     * 获取map的最后一个值
     */
    public static <K, V> V getTailVal(Map<K, V> map) {
        if (map == null) {
            return null;
        }
        Iterator<Entry<K, V>> iterator = map.entrySet().iterator();
        Entry<K, V> tail = null;
        while (iterator.hasNext()) {
            tail = iterator.next();
        }
        return tail.getValue();
    }

    public static <T> LinkedHashSet<T> noEmptySet(LinkedHashSet<T> ...sets){
        for(LinkedHashSet<T> set : sets){
            if(CollectionUtils.isNotEmpty(set)){
                return set;
            }
        }
        return null;
    }

    /**
     * 将List转成值为List的Map
     *
     * @param objs 需要抽取属性值的对象列表
     * @param prop 属性名称或者方法名称
     * @return 属性值列表的映射
     */
    public static <T> Map<Object, List<T>> list2mapList(List<T> objs, String prop) {
        Map<Object, List<T>> vals = new LinkedHashMap<Object, List<T>>();
        if (CollectionUtils.isEmpty(objs)) {
            return null;
        }
        // 属性方法
        Method method = null;
        for (T obj : objs) {
            if (obj == null) {
                continue;
            }
            if (method == null) {
                method = ObjTool.getMethodByNameFromObj(obj, prop);
                if (method == null) {
                    return vals;
                }
            }
            try {
                Object retnObj = method.invoke(obj);
                if (retnObj != null) {
                    List<T> list = vals.get(retnObj);
                    if (list == null) {
                        list = new ArrayList<>();
                        vals.put(retnObj, list);
                    }
                    list.add(obj);
                }
            } catch (Exception e) {
                log.info(
                        method.getDeclaringClass().getName() + " invoke fail methodName=" + method.getName());
                return vals;
            }
        }
        return vals;
    }

    /**
     * 将List转成Map
     *
     * @param objs 需要抽取属性值的对象列表
     * @param prop 属性名称或者方法名称
     * @return 属性值集合
     */
    public static <T> Map<Object, T> list2map(List<T> objs, String prop) {
        Map<Object, T> vals = new LinkedHashMap<Object, T>();
        if (CollectionUtils.isEmpty(objs)) {
            return vals;
        }
        // 属性方法
        Method method = null;
        for (T obj : objs) {
            if (obj == null) {
                continue;
            }
            if (method == null) {
                method = ObjTool.getMethodByNameFromObj(obj, prop);
                if (method == null) {
                    return vals;
                }
            }
            try {
                Object retnObj = method.invoke(obj);
                if (retnObj != null) {
                    vals.put(retnObj, obj);
                }
            } catch (Exception e) {
                log.info(
                        method.getDeclaringClass().getName() + " invoke fail methodName=" + method.getName());
                return vals;
            }
        }
        return vals;
    }

    /**
     * 将List转成Map
     *
     * @param objs
     *            需要抽取属性值的对象列表
     * @param propKey
     *            键的属性名称或者方法名称
     * @param propVal
     *            值的属性名称或者方法名称
     * @return 属性值集合
     */
    public static <T, K, V> Map<K, V> list2mapProp(List<T> objs, String propKey, String propVal, Class<K> kc,
                                                   Class<V> vc) {
        Map<K, V> targMap = new HashMap<>();
        Map<Object, Object> map = list2mapProp(objs, propKey, propVal);
        if (MapUtils.isNotEmpty(map)) {
            for (Entry<Object, Object> entry : map.entrySet()) {
                Object k = entry.getKey(), v = entry.getValue();
                if (k == null || v == null) {
                    continue;
                }
                if (kc.isAssignableFrom(k.getClass()) && vc.isAssignableFrom(v.getClass())) {
                    targMap.put((K)k, (V)v);
                }
            }
        }
        return targMap;
    }


    /**
     * 将List转成Map
     *
     * @param objs    需要抽取属性值的对象列表
     * @param propKey 键的属性名称或者方法名称
     * @param propVal 值的属性名称或者方法名称
     * @return 属性值集合
     */
    public static <T> Map<Object, Object> list2mapProp(List<T> objs, String propKey, String propVal) {
        Map<Object, Object> vals = new LinkedHashMap<>();
        if (CollectionUtils.isEmpty(objs)) {
            return null;
        }
        // 属性方法
        Method methodKey = null;
        Method methodVal = null;
        for (T obj : objs) {
            if (obj == null) {
                continue;
            }
            if (methodKey == null) {
                methodKey = ObjTool.getMethodByNameFromObj(obj, propKey);
                if (methodKey == null) {
                    return vals;
                }
            }
            if (methodVal == null) {
                methodVal = ObjTool.getMethodByNameFromObj(obj, propVal);
                if (methodVal == null) {
                    return vals;
                }
            }
            try {
                Object keyObj = methodKey.invoke(obj);
                Object valObj = methodVal.invoke(obj);
                if (keyObj != null && valObj != null) {
                    vals.put(keyObj, valObj);
                }
            } catch (Exception e) {
                log.info(methodKey.getDeclaringClass().getName() + " invoke fail methodName=" + methodKey
                        .getName());
                return vals;
            }
        }
        return vals;
    }

    /**
     * 将List转成Map
     *
     * @param objs 需要抽取属性值的对象列表
     * @param prop 属性名称或者方法名称
     * @param cla  属性的数据类型
     * @return 属性值集合
     */
    @SuppressWarnings("unchecked")
    public static <K, T> Map<K, T> list2map(List<T> objs, String prop, Class<K> cla) {
        Map<K, T> vals = new LinkedHashMap<K, T>();
        if (CollectionUtils.isEmpty(objs)) {
            return null;
        }
        // 属性方法
        Method method = null;
        for (T obj : objs) {
            if (obj == null) {
                continue;
            }
            if (method == null) {
                method = ObjTool.getMethodByNameFromObj(obj, prop);
                if (method == null) {
                    return vals;
                }
            }
            try {
                Object retnObj = method.invoke(obj);
                if (retnObj != null) {
                    vals.put((K) retnObj, obj);
                }
            } catch (Exception e) {
                log.info(
                        method.getDeclaringClass().getName() + " invoke fail methodName=" + method.getName());
                return vals;
            }
        }
        return vals;
    }


    /**
     * 将数组拆分成Map。 自然序列的奇数为key, 偶数为value 例：{"cnyPrice", "10.2","devOrderId","15292090210000000","devOrderSubject","gussing","payType","PYC","totalamount","2"}
     * 返回cnyPrice为key,10.2为value
     *
     * @param origin 需要处理的源数组
     */
    public static <T> Map<T, T> array2map(T[] origin) {
        if (origin == null || origin.length < 2) {
            return null;
        }
        Map<T, T> retnMap = new LinkedHashMap<>();
        for (int i = 0, leng = origin.length - 1; i < leng; i += 2) {
            retnMap.put(origin[i], origin[i + 1]);
        }
        return retnMap;
    }


    /**
     * 抽取对象列表的属性值
     *
     * @param objs 需要抽取属性值的对象列表
     * @param prop 属性名称或者方法名称
     * @param cl   属性的数据类型
     * @return 属性值集合
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> List<T> abstract2list(List objs, String prop, Class<T> cl) {
        return new ArrayList<>(abstrict(objs, prop, cl));
    }

    /**
     * 抽取对象列表的属性值
     *
     * @param objs 需要抽取属性值的对象列表
     * @param prop 属性名称或者方法名称
     * @return 属性值集合
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Set<String> abstrictStrTrim(List objs, String prop) {
        Set<String> set = abstrict(objs, prop, String.class);
        Set<String> setTrim = new HashSet<>(set.size());
        for (String str : set) {
            if (StringUtils.isBlank(str)) {
                continue;
            }
            setTrim.add(str.trim());
        }
        return setTrim;
    }

    /**
     * 抽取对象列表的属性值
     *
     * @param objs 需要抽取属性值的对象列表
     * @param prop 属性名称或者方法名称
     * @param cl   属性的数据类型
     * @return 属性值集合
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> Set<T> abstrict(List objs, String prop, Class<T> cl) {
        Set<T> vals = new LinkedHashSet<T>();
        if (CollectionUtils.isEmpty(objs) || StringUtils.isBlank(prop)) {
            return vals;
        }

        // 属性方法
        Method method = null;
        for (Object obj : objs) {
            if (obj == null) {
                continue;
            }
            if (method == null) {
                method = ObjTool.getMethodByNameFromObj(obj, prop);
                if (method == null) {
                    return vals;
                }
            }
            try {
                Object retnObj = method.invoke(obj);
                if (retnObj != null) {
                    vals.add((T) retnObj);
                }
            } catch (Exception e) {
                log.info(
                        method.getDeclaringClass().getName() + " invoke fail methodName=" + method.getName());
                return vals;
            }
        }

        return vals;
    }


    /**
     * 批量抽取对象列表的属性
     *
     * @param objs    需要抽取属性值的对象列表
     * @param propMap 键为属性名称，值为抽取的结果需要存在的集合
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void abstrictBatch(List objs, Map<String, Set> propMap) {
        if (CollectionUtils.isEmpty(objs) || MapUtils.isEmpty(propMap)) {
            return;
        }

        Map<String, Method> propMethod = new LinkedHashMap<String, Method>();

        Method method = null;
        for (Object obj : objs) {
            if (obj == null) {
                continue;
            }
            for (Entry<String, Set> enty : propMap.entrySet()) {
                String key = enty.getKey();
                Set val = enty.getValue();
                if (key == null) {
                    continue;
                }
                method = propMethod.get(key);
                if (method == null) {
                    method = ObjTool.getMethodByNameFromObj(obj, key);
                    if (method == null) {
                        return;
                    }
                }
                try {
                    Object retnObj = method.invoke(obj);
                    if (retnObj != null) {
                        val.add(retnObj);
                    }
                } catch (Exception e) {
                    log.info(
                            method.getDeclaringClass().getName() + " invoke fail methodName=" + method.getName());
                    return;
                }
            }
        }

        return;
    }

    /**
     * 从一批对象中选取指定的属性的值在propVals中的对象
     */
    public static <K, T> List<T> listChoose(Collection<T> objs, String prop, Collection<K> propVals) {
        List<T> vals = new ArrayList<T>();
        if (CollectionUtils.isEmpty(objs) || CollectionUtils.isEmpty(propVals) || StringUtils
                .isBlank(prop)) {
            return vals;
        }
        Map<Object, T> objPropMap = list2map(new ArrayList<>(objs), prop);
        // 属性方法
        Method method = null;
        for (K propVal : propVals) {
            if (propVal == null) {
                continue;
            }
            T obj = objPropMap.get(propVal);

            if (method == null) {
                method = ObjTool.getMethodByNameFromObj(obj, prop);
                if (method == null) {
                    return vals;
                }
            }
            try {
                Object retnObj = method.invoke(obj);
                if (retnObj != null) {
                    if (propVal == retnObj) {
                        vals.add(obj);
                    } else if (propVal instanceof String && StringUtils
                            .equals(propVal.toString(), retnObj.toString())) {
                        vals.add(obj);
                    } else if (propVal instanceof Integer && retnObj instanceof Integer) {
                        if (((Integer) propVal).intValue() == (Integer) retnObj) {
                            vals.add(obj);
                        }
                    } else if (propVal instanceof Long && retnObj instanceof Long) {
                        if (((Long) propVal).longValue() == (Long) retnObj) {
                            vals.add(obj);
                        }
                    } else if (propVal.equals(retnObj)) {
                        vals.add(obj);
                    }
                }
            } catch (Exception e) {
                log.info(
                        method.getDeclaringClass().getName() + " invoke fail methodName=" + method.getName());
                return vals;
            }
        }

        return vals;
    }


    /**
     * 抽取map中的对象，然后放到一个数组列表里面。用于2层结构
     */
    public static <T> List<T> abstrictMapObj2(Map<String, Map<String, T>> map) {
        if (MapUtils.isEmpty(map)) {
            return null;
        }
        List<T> objs = new ArrayList<>();
        for (Entry<String, Map<String, T>> enty1 : map.entrySet()) {
            objs.addAll(enty1.getValue().values());
        }
        return objs;
    }

    public static String colt2str(Collection colt) {
        if (CollectionUtils.isEmpty(colt)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Object obj : colt) {
            sb.append(obj.toString()).append(SymbolConst.COMMA);
        }
        return sb.toString();

    }


    /**
     * 将List转成3层Map
     *
     * @param objs  需要抽取属性值的对象列表
     * @param prop1 属性名称或者方法名称。只支持String类型
     * @param prop2 属性名称或者方法名称。只支持String类型
     * @param prop3 属性名称或者方法名称。只支持String类型
     * @return 属性值集合
     */
    public static <T> Map<Object, Map<Object, Map<Object, T>>> list2map3(List<T> objs, String prop1,
                                                                         String prop2, String prop3) {
        Map<Object, Map<Object, Map<Object, T>>> vals = new LinkedHashMap<>();
        Map<String, Method> methodMap = new HashMap<>(3);
        if (CollectionUtils.isEmpty(objs)) {
            return null;
        }
        // 属性方法
        Method method1 = null;
        Method method2 = null;
        Method method3 = null;
        for (T obj : objs) {
            if (obj == null) {
                continue;
            }
            method1 = methodMap.get(prop1);
            if (method1 == null) {
                method1 = ObjTool.getMethodByNameFromObj(obj, prop1);
                if (method1 == null) {
                    return vals;
                }
                methodMap.put(prop1, method1);
            }
            method2 = methodMap.get(prop2);
            if (method2 == null) {
                method2 = ObjTool.getMethodByNameFromObj(obj, prop2);
                if (method2 == null) {
                    return vals;
                }
                methodMap.put(prop2, method2);
            }
            method3 = methodMap.get(prop3);
            if (method3 == null) {
                method3 = ObjTool.getMethodByNameFromObj(obj, prop3);
                if (method3 == null) {
                    return vals;
                }
                methodMap.put(prop3, method3);
            }
            try {
                Object obj1 = method1.invoke(obj);
                Object obj2 = method2.invoke(obj);
                Object obj3 = method3.invoke(obj);
                if (obj1 != null && obj2 != null && obj3 != null) {
                    Map<Object, Map<Object, T>> map2 = vals.get(obj1);
                    if (map2 == null) {
                        map2 = new HashMap<>();
                        vals.put(obj1, map2);
                    }
                    Map<Object, T> map3 = map2.get(obj2);
                    if (map3 == null) {
                        map3 = new HashMap<>();
                        map2.put(obj2, map3);
                    }
                    map3.put(obj3, obj);
                }
            } catch (Exception e) {
                log.info("prop1=" + prop1 + ", prop2=" + prop1 + ", prop3=" + prop3 + e.getMessage(), e);
                return vals;
            }
        }
        return vals;
    }

    public static List<Object> listStr2obj(List<String> strs) {
        List<Object> objs = new ArrayList<>();
        if (strs == null) {
            return objs;
        }
        for (String str : strs) {
            objs.add(str);
        }
        return objs;
    }
}
