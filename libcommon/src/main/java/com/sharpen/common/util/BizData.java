package com.sharpen.common.util;

//import cn.hutool.extra.mail.MailAccount;
import com.sharpen.common.consts.SymbolConst;

import java.util.Map;

/**
 * 业务数据
 */
public class BizData {

  public static String WS_URL="http://s.51kxd.com:3513";
  public static String BDFP_HOST="http://s.51kxd.com:3513";
  public static String BCNS_HOST="http://dbt.51kxd.com:13111";
  public static String BACK_PROJ="bcns";

  public static  String PROJ_NAME = "bcnsBack";//changed，old is "marketRobot"
  public static final String FILE_PROTOCOL__CLASSPATH = "classpath:";
  public static final String USER_HOME = System.getProperty("user.home");
  public static final String OS = System.getProperty("os.name").toLowerCase();
  public static String defConf = "grc.properties";

  public static String WIN_CONF = "D:\\datum\\data\\conf\\";
  public static String MAC_CONF = "~/datum/conf/";

  // 中心币地址
  public static String CENTER_ISSUER = "marketRobot";


//  public static MailAccount defautlMailAccount = new MailAccount();

  public enum OSType {
    OS_TYPE_LINUX, OS_TYPE_WIN, OS_TYPE_SOLARIS, OS_TYPE_MAC, OS_TYPE_FREEBSD, OS_TYPE_OTHER
  }

  static private OSType getOSType() {
    String osName = System.getProperty("os.name");
    if (osName.startsWith("Windows")) {
      return OSType.OS_TYPE_WIN;
    } else if (osName.contains("SunOS") || osName.contains("Solaris")) {
      return OSType.OS_TYPE_SOLARIS;
    } else if (osName.contains("Mac")) {
      return OSType.OS_TYPE_MAC;
    } else if (osName.contains("FreeBSD")) {
      return OSType.OS_TYPE_FREEBSD;
    } else if (osName.startsWith("Linux")) {
      return OSType.OS_TYPE_LINUX;
    } else {
      // Some other form of Unix
      return OSType.OS_TYPE_OTHER;
    }
  }

  public static final String vePathPre = "vmconf/biz";
  public static final String DB_MYBATIS_TYPE_MAP = "varchar:VARCHAR;varchar2:VARCHAR;longtext:VARCHAR;" +
      "timestamp:TIMESTAMP;datatime:TIMESTAMP;" +
      "int:NUMERIC;integer:NUMERIC;bigint:NUMERIC";
  public static Map<String, String> dbMybatisTypeMap = StrTool.str2map(DB_MYBATIS_TYPE_MAP, SymbolConst.SEMICOLON, SymbolConst.COLON);


  public static final String DB_JAVA_TYPE_MAP = "varchar:String;varchar2:String;longtext:String;" +
      "timestamp:Date;datatime:Date;" +
      "int:Integer;integer:Integer;bigint:Long";
  public static Map<String, String> dbJavaTypeMap = StrTool.str2map(DB_JAVA_TYPE_MAP, SymbolConst.SEMICOLON, SymbolConst.COLON);


  public static String defaultVmWin = "E:\\datum\\code\\git\\mystudy\\sharpen3\\bcns.back\\bcns-back-biz\\bcns-back-biz-provider\\src\\main\\resources\\vmconf\\biz\\";
  public static String defaultVmLinux = "/Users/Justin/datum/code/company/self/study/sharpen3/bcns.back/bcns-back-biz/bcns-back-biz-provider/src/main/resources/vmconf/biz/";

  public static String getDefVm() {
    if (getOSType() == OSType.OS_TYPE_WIN) {
      return defaultVmWin;
    } else {
      return defaultVmLinux;
    }
  }

  public static String getCodeSaveBond() {
    return getOSType() == OSType.OS_TYPE_WIN ? "codeSaveWin" : "codeSaveLinux";
  }


}
