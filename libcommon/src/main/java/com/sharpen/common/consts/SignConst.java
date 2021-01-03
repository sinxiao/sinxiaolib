package com.sharpen.common.consts;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 跟业务无关的标识常量
 *
 * @author Justin
 */
public class SignConst {

  public static String SN = null;
  public static Set<String> IGNORE_METHOD = new HashSet<>(Arrays.asList("OPTIONS"));
  public static final Integer[] EXPAND10 = {1, 10,100,1000,10000,100000, 1000000,1000000};

  public static final String STR_ZERO = "0";
  public static final String STR_ONE = "1";
  public static final String STR_TWO = "2";
  public static final String STR_THREE = "3";
  public static final String STR_FOUR = "4";
  public static final String STR_FIVE = "5";

  // 常用百分比
  public static final String PER25 = "0.25";
  public static final String PER50 = "0.5";
  public static final String PER75 = "0.75";
  public static final String PER100 = "1";


  public static final Integer INT200 = 1000;
  public static final Integer INT_THOUSAND = 1000;
  public static final Long LONG_MILLI = 1000000L;
  public static final BigDecimal BD_ONE_THOUSAND = new BigDecimal(1/1000);
  public static final BigDecimal BD_TWELVE_MILLI = new BigDecimal(12/1000000);
  public static final BigDecimal BD_THOUSAND = new BigDecimal(1000);
  public static final BigDecimal BD_TWELVE = new BigDecimal(12);
  public static final BigDecimal BD_TEN_THOUSAND = new BigDecimal(10000);
  public static final Integer INT_MILLI = 1000000;
  public static final String STR_MILLI = "1000000";
  public static final BigDecimal BD_MILLI = new BigDecimal(1000000);

  public static final Long L0 = 0L;
  public static final Long L1 = 1L;
  public static final Long L200 = 200L;
  public static final Long L10000 = 10000L;

  // 瑞波基准时间，单位：秒。参考： https://radarlab.org/dev-cn/radard-apis.html
  public static final Long RIPPLE_BASE_SECOND = 946684800L;


  public static final String ORDERBY_ASC = "asc";
  public static final String ORDERBY_VIEW_ASC = "ascending";
  public static final String ORDERBY_DESC = "desc";
  public static final String ORDERBY_VIEW_DESC = "desc";

  public static final String LOGIN_TOKEN_ERROR_PED_PREFIX = "TOKEN_ERROR_PWD_";

  public static final String CURRENT_LOGIN_CLIENT_IOS = "IOS";
  public static final String CURRENT_LOGIN_CLIENT_ANDROID = "ANDROID";
  public static final String CURRENT_LOGIN_CLIENT_WEB = "WEB";

  public static final String CONTENTTYPE_JSON = "application/json; charset=utf-8";


  public static final String SESSIONID= "SESSIONID";
  /**
   * redis 随机码KEY前缀
   */
  public static final String SESSION_RANDOM = "RANDOM_";
  /**
   * redis 谷歌验证码Key的前缀
   */
  public static final String GOOGLE_CODE_KEY_PREFIX = "GOOGLE_CODE_KEY_";
  /**
   * 验证码key过期时间
   */
  public static final Long VALIDATE_CODE_EXPIRE = 10 * 60 * 1000L;

  public static final String LOGIN_USER_IP_TOKEN_PREFIX = "LOGIN_TOKEN_CURRENT_";

  public static final String STRING_NULL = "";

  public static final String LOGIN_PWD_ERROR_COUNT_MSG_ZH = "您的账户或密码错误，您还可以尝试登录%d次";
  public static final String LOGIN_PWD_ERROR_COUNT_MSG_EN = "Incorrect password. You can try %d times";

  /**
   * 验证类型:短信,图片,邮箱,谷歌
   */
  public static final String CODE_TYPE_SMS = "SMS";
  public static final String CODE_TYPE_IMAGE = "IMAGE";
  public static final String CODE_TYPE_EMAIL = "EMAIL";

  /**
   * 短信,邮箱以及验证码的redis的key前缀
   */
  public static final String SMS_CODE_PREFIX = "SMS_CODE_";
  public static final String IMAGE_CODE_PREFIX = "IMAGE_CODE_";
  public static final String EMAIL_CODE_PREFIX = "EMAIL_CODE_";

  /**
   * 更新手机号不可提币时长
   */
  public static final long UPDATE_PHONE_FORBID_COIN_TIME = 24 * 60 * 60 * 1000;
  public static final String UPDATE_PHONE_FORBID_COIN_PREFIX = "UPDATE_PHONE_FORBID_COIN_";

  /**
   * 更新googlekey不可提币时长
   */
  public static final long UPDATE_GOOGLE_KEY_FORBID_COIN_TIME = 24 * 60 * 60 * 1000;
  public static final String UPDATE_GOOGLE_KEY_FORBID_COIN_PREFIX = "UPDATE_GOOGLE_KEY_FORBID_COIN_";

  // 时间格式

  /**
   * 年年年年月月日日时时，例： 2020121430，2020121400
   */
  public static final String TIME_FORMAT_SHORT_HOUR = "yyyyMMddHH";
  /**
   * 年年年年月月，例： 202012，202013
   */
  public static final String TIME_FORMAT_SHORT_MONTH = "yyyyMM";
  /**
   * 年年年年，例： 2019，2020
   */
  public static final String TIME_FORMAT_SHORT_YEAR = "yyyy";



  public static Integer ZERO = 0;
  public static Integer ONE = 1;

  public static Integer TWO = 2;
  public static Integer THREE = 3;
  public static Integer FOUR = 4;
  public static Integer FIVE = 5;
  public static Integer SIX = 6;

  public static Double D_ZERO = 0D;

  public static final Integer YES = 0;
  public static final Integer NO = 1;
  public static final Integer IS_BIND_SUCCESS = 0;

  public static final String ENCODE_UTF8 = StandardCharsets.UTF_8.name();

  public static final String Y = "Y";
  public static final String N = "N";

  public static final String CALL_STATUS_WAIT = "waiting";
  public static final String CALL_STATUS_SUCCESS = "success";
  public static final String STR_SUCCESS = "success";
  public static final String CALL_STATUS_FAIL = "fail";

  // 表示回调请求协议的常量,用于拼接回调URL
  public static final String PROTOCL_HTTP = "http://";
  public static final String PROTOCL_HTTPS = "https://";

  // 常用字符串
  public static final String NEWLINE = "\n";

  // 请求流水号
  public static final String MSG = "msg";
  // 请求流水号
  public static final String REQUEST_SEQUENCE = "requestId";
  // 服务流水号
  public static final String SERVICE_SEQUENCE = "serveSeq";
  // 跟踪序号
  public static final String TRACE_NO = "traceNo";
  // 当前登录用户IP
  public static final String CURRENT_LOGIN_IP = "CURRENT_LOGIN_IP";

  public static final String REQUESTMAPPING = "RequestMapping";
  public static final String RESOURCEPAGEANNOTION = "ResourcePageAnnotion";
  public static final String RESOURCEANNOTION = "ResourceAnnotion";
  public static final String ANNOTIONMAPMETHODKEY = "method"; // 方法上的注解
  public static final String ANNOTIONMAPCLASSKEY = "class"; // 类上的注解

  public static final String STATUS_NORMAL = "normal";
  public static final String STATUS_FORBIDDEN = "forbidden";


  public static final String BUY = "buy";
  public static final String SELL = "sell";


  public static final String CLASSIFY_USERTYPE = "userType";
  public static final String USERTYPE_ADMIN = "admin";
  public static final String USERTYPE_MANAGER = "manager";
  public static final String USERTYPE_MEMBER = "member";
  public static final String MINUS_SIGN = "-";


}
