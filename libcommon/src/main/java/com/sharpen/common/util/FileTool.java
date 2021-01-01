package com.sharpen.common.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.sharpen.common.activity.BaseActivity;
import com.sharpen.common.consts.SignConst;
import com.sharpen.common.consts.SymbolConst;

//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.util.StrUtil;

import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.sharpen.common.util.BizData.FILE_PROTOCOL__CLASSPATH;
import static com.sharpen.common.util.BizData.USER_HOME;


public class FileTool {
//  private static Logger log = LoggerFactory.getLogger(FileTool.class);

  public static boolean testSwitch = false;
  public static Properties envProp = null;

  /**
   * 如果关闭了忽略，返回true
   *
   * @param switchName 开关名称，在CtrlParamConst中
   */
  public static boolean ignoreOff(String switchName) {
    return !switchCheck(switchName);
  }

  public static boolean switchCheck(String switchName) {
    Properties prop = FileTool.localProp();
    return StringUtils.equals(prop.getProperty(switchName), SignConst.Y);
  }

  public static String propVal(String key, String defVal) {
    String var = propVal(key);
    return StringUtils.isBlank(var) ? defVal : var;
  }

  public static String propVal(String key) {
    return localProp().getProperty(key);
  }


  public static Properties localProp() {
    if (envProp != null) {
      return envProp;
    }
    Properties propLocal = new Properties();
    if (testSwitch) {
      String switchStr = "loginIgnoreSwitch:Y;ignoreWSLog:Y;ignoreCallBackNotify:Y;"
          + "tempUseReqClient:Y";
      Map<String, String> switchMap = StrTool.str2map(switchStr, ";", ":");
      propLocal.putAll(switchMap);
      return propLocal;
    }
    try {
      String filePre = StringUtils.startsWith(System.getProperty("os.name").toLowerCase(), "win")
          ? "D:\\datum\\data\\conf\\" : "/datum/conf/";
      String propPath = filePre + "bcns.properties";
      propLocal = FileTool.file2prop(propPath);
      return propLocal == null ? new Properties() : propLocal;
    } catch (Exception e) {
      //log.info(e.getMessage(), e);
    }
    return propLocal;
  }

  /**
   * 格式化文件路径，正确的路径开头：classpath，file， http。否则，加上：file:
   *
   * @param filePath 需要格式化的文件路径
   * @author Justin
   */
  public static String formatPath(String filePath) {
    if (StringUtils.isNotBlank(filePath) && !filePath.startsWith("classpath")
        && !filePath.startsWith("file") && !filePath.startsWith("http")) {
      filePath = "file:" + filePath;
    }
    return filePath;
  }


  /**
   * 根据文件路径，获取文件输入流。支持协议： classpath, file, http, ftp
   *
   * @param filePath 文件路径。支持协议： classpath, file, http, ftp
   * @return 文件输入流
   * @author justin.li
   * @date 2017年7月29日
   */
  public static Reader getFileReader(String filePath) {
    return new InputStreamReader(getFileStream(filePath));
  }

  /**
   * 根据文件路径，获取文件输入流。支持协议： classpath, file, http, ftp
   *
   * @param filePath 文件路径。支持协议： classpath, file, http, ftp
   * @return 文件输入流
   * @author justin.li
   * @date 2017年7月29日
   */
  public static InputStream getFileStream(String filePath) {
    if (StringUtils.isBlank(filePath)) {
      return null;
    }
    if (!filePath.startsWith(SymbolConst.TILDE)) {
      filePath = formatPath(filePath);
    }
    InputStream fis = null;
    try {

      if (filePath.startsWith(SymbolConst.TILDE)) {
        filePath = USER_HOME + filePath.substring(1);
        fis = new FileInputStream(filePath);
      } else if (filePath.startsWith(FILE_PROTOCOL__CLASSPATH)) {
        // 参照 TomcatURLStreamHandlerFactory 解决 SslStoreProviderUrlStreamHandlerFactory 没有 classpath协议的问题
        String path = filePath.substring(FILE_PROTOCOL__CLASSPATH.length());
        //剥离classpath
        URL classpathUrl = Thread.currentThread().getContextClassLoader().getResource(path);
        if (classpathUrl == null) {
          classpathUrl = FileTool.class.getResource(path);
        }
        if (classpathUrl != null) {
          URLConnection uc = classpathUrl.openConnection();
          fis = uc.getInputStream();
        }
      }
      return fis;
    } catch (FileNotFoundException e) {
    } catch (Exception e) {
      //log.info("filePath" + filePath + e.getMessage(), e);
    }
    return null;
  }

  /**
   * 文件转属性。支持协议： classpath, file, http, ftp
   *
   * @param filePath 文件路径。支持协议： classpath, file, http, ftp
   * @return 文件属性对象
   * @author justin.li
   * @date 2017年7月29日
   */
  public static Properties file2prop(String filePath) {
    Properties sysConf = new Properties();
    InputStream fis = getFileStream(filePath);
    if (fis == null) {
      return sysConf;
    }
    InputStreamReader isr = null;
    BufferedReader br = null;

    try {
      isr = new InputStreamReader(fis, StandardCharsets.UTF_8.name());
      br = new BufferedReader(isr);
      sysConf.load(br);
    } catch (Exception e) {
      //log.error("file2prop exception,filePath:{}", filePath, e);
    } finally {
      try {
        if (br != null) {
          br.close();
        }
        if (isr != null) {
          isr.close();
        }
        fis.close();
      } catch (Exception e) {
        //log.info("filePath" + filePath + e.getMessage(), e);
      }
    }

    return sysConf;
  }

  /**
   * 文件是否存在。支持协议： classpath, file, http, ftp
   *
   * @param filePath 文件路径， 例： file:/datum/data/conf2/config.properties
   *                 classpath:com/grabdata/config/conf.properties
   */
  public static boolean fileExist(String filePath) {
    InputStream fis = getFileStream(filePath);
    try {
      if (fis != null) {
        fis.close();
        return true;
      }
    } catch (Exception e) {
      //log.info("filePath" + filePath + e.getMessage(), e);
    }

    return false;
  }


  /**
   * 接收，保存图片
   *
   * @param originalName 源文件夹名，例：Chess_n.png
   */
  public static Properties saveImg(InputStream in, String originalName, String fullPath) {
    Properties prop = new Properties();
    ZipInputStream zis = null;
    BufferedInputStream bis = null;
    BufferedInputStream bis2 = null;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    try {
      if (in instanceof BufferedInputStream) {
        bis = (BufferedInputStream) in;
      } else {
        bis = new BufferedInputStream(in);
      }
      zis = new ZipInputStream(bis);
      ZipEntry zipEntry = null;
      while ((zipEntry = zis.getNextEntry()) != null) {
        if (!zipEntry.isDirectory() && StringUtils.equals(zipEntry.getName(), originalName)) {
          bis2 = new BufferedInputStream(zis);
          fos = new FileOutputStream(fullPath);
          bos = new BufferedOutputStream(fos);
          byte[] buff = new byte[4096];
          int len;
          while ((len = bis2.read(buff)) > 0) {
            bos.write(buff, 0, len);
          }
          //log.info(originalName + " save to:" + fullPath);
        }
      }

    } catch (Exception e) {
      //log.error(e.getMessage(), e);
    } finally {
      try {

        if (zis != null) {
          zis.close();
        }
        if (bis != null) {
          bis.close();
        }
        if (bos != null) {
          bos.close();
        }
        if (fos != null) {
          fos.close();
        }
        if (bis2 != null) {
          bis2.close();
        }
      } catch (Exception e) {
        //log.error(e.getMessage(), e);
      }
    }
    return prop;
  }



  public static String pack2Dir(String packPath) {
    if (StringUtils.isBlank(packPath)) {
      return packPath;
    }
    String separatorStr =
        StringUtils.equals(File.separator, "\\") ? File.separator + File.separator : File.separator;
    packPath = packPath.replaceAll("\\.", separatorStr);
    return packPath;
  }


  public static boolean createFile(String filePath) {
    String dir = filePath.substring(0, filePath.lastIndexOf(File.separator));
    File currDir = new File(dir);
    if (currDir.exists() == Boolean.FALSE) {
      boolean createDirStatus = createDir(dir);
      if (createDirStatus == Boolean.FALSE) {
        return createDirStatus;
      }
    }
    File file = new File(filePath);
    if (file.exists() == Boolean.FALSE) {
      try {
        return file.createNewFile();
      } catch (Exception e) {
        //log.info(e.getMessage(), e);
        return false;
      }
    }
    return true;
  }

  public static boolean createDir(String dirPath) {
    Stack<String> stack = new Stack<String>();

    String currDirStr = dirPath;
    File currDir = new File(dirPath);

    while (!currDir.exists()) {
      // 去掉末尾的\
      while (currDirStr.endsWith(File.separator)) {
        currDirStr = currDirStr.substring(0, currDirStr.length() - 1);
      }
      String belongDir = currDirStr.lastIndexOf(File.separator) < 0 ? currDirStr
          : currDirStr.substring(0, currDirStr.lastIndexOf(File.separator));
      String endDirStr = currDirStr
          .substring(currDirStr.lastIndexOf(File.separator) + 1);
      stack.push(endDirStr);
      currDir = new File(belongDir);
      currDirStr = belongDir;
    }

    while (!stack.isEmpty()) {
      currDirStr = currDirStr + File.separator + stack.pop();
      currDir = new File(currDirStr);
      try {
        boolean create = currDir.mkdir();
        if (!create) {
          System.out.println("create dir fail:" + currDirStr);
        }
      } catch (Exception e) {
        //log.info(e.getMessage(), e);
      }
    }
    return true;
  }


  /**
   * 按UTF-8格式读取文件为一个内存字符串,保持文件原有的换行格式
   *
   * @param filePath 文件路径
   * @return 文件内容的字符串
   */
  public static String file2String(String filePath) {
    String str = file2String(filePath, StandardCharsets.UTF_8.name());
    return str;
  }

  /**
   * 读取文件为一个内存字符串,保持文件原有的换行格式
   *
   * @param filePath 文件路径
   * @param charset  文件字符集编码
   * @return 文件内容的字符串
   */
  public static String file2String(String filePath, String charset) {
    File fileObj = new File(filePath);
    if (fileObj.exists() == Boolean.FALSE) {
      return null;
    }
    String str = file2String(fileObj, charset);
    return str;
  }


  public static String assetsFile2str(Context ctx, String path) {
    try {
      InputStream is = ctx.getResources().getAssets().open(path);
      return stream2str(is, StandardCharsets.UTF_8.name());
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }
  public static String stream2str(InputStream is, String charset) {
    StringBuffer sb = new StringBuffer();

    try{
      InputStreamReader isReader = new InputStreamReader(is , charset);
      BufferedReader bReader = new BufferedReader (isReader);
      String mimeTypeLine = null ;
      while((mimeTypeLine = bReader.readLine())!=null) {
//        sb.append(mimeTypeLine).append(StrUtil.CRLF);
//        sb.append(mimeTypeLine).append(StrUtil.CRLF);
      }
    }catch (Exception e){
      e.printStackTrace();
    }
    return sb.toString();
  }

  /**
   * 读取文件为一个内存字符串,保持文件原有的换行格式
   *
   * @param file    文件对象
   * @param charset 文件字符集编码
   * @return 文件内容的字符串
   */
  public static String file2String(File file, String charset) {
    StringBuffer sb = new StringBuffer();
    LineNumberReader reader = null;
    try {
      reader = new LineNumberReader(new InputStreamReader(
          new FileInputStream(file), charset));
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line).append(System.getProperty("line.separator"));
      }
    } catch (UnsupportedEncodingException e) {
      //log.error("读取文件为一个内存字符串失败，失败原因是使用了不支持的字符编码" + charset, e);
      return null;
    } catch (FileNotFoundException e) {
      //log.error("读取文件为一个内存字符串失败，失败原因所给的文件" + file + "不存在！", e);
      return null;
    } catch (IOException e) {
      //log.error("读取文件为一个内存字符串失败，失败原因是读取文件异常！", e);
      return null;
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (Exception e) {
        //log.error("关闭文件异常！", e);
      }
    }
    return sb.toString();
  }


  /**
   * 读取文件到字节数组。最大只支持512M，超过的情况下返回null
   *
   * @param file 文件对象
   * @return 文件内容的字节数组
   */
  public static boolean byte2file(File file, byte[] fileByte) {
    if (file == null || fileByte == null) {
      return false;
    }
    if (file.exists() == Boolean.FALSE) {
      boolean createFileStatus = createFile(file.getAbsolutePath());
      if (createFileStatus == Boolean.FALSE) {
        return createFileStatus;
      }
    }
    OutputStream bos = null;
    try {
      bos = new FileOutputStream(file);
      bos.write(fileByte);
      return true;
    } catch (Exception e) {
      //log.error("read file fail", e);
    } finally {
      try {
        if (bos != null) {
          bos.close();
        }
      } catch (Exception e) {
        //log.error("file close fail", e);
      }
    }
    return false;
  }

  /**
   * 将内存字符串按照utf-8格式写入到磁盘的文件中
   *
   * @param str      要写入的字符串
   * @param filePath 文件路径
   * @return 文件内容的字符串
   */
  public static void str2file(String str, String filePath) {
    str2file(str, new File(filePath), StandardCharsets.UTF_8.name());
  }

  /**
   * 将内存字符串写入到磁盘的文件中
   *
   * @param str     要写入的字符串
   * @param file    文件对象
   * @param charset 文件字符集编码
   * @return 文件内容的字符串
   */
  public static void str2file(String str, File file, String charset) {
    BufferedWriter writer = null;
    Writer writerStream = null;
    OutputStream out = null;
    try {
      if (!file.exists()) {
        createDir(file.getParent());
        if (!file.exists()) {
          file.createNewFile();
        }
      }
      out = new FileOutputStream(file);
      writerStream = new OutputStreamWriter(out, charset);
      writer = new BufferedWriter(writerStream);
      writer.write(str);
    } catch (UnsupportedEncodingException e) {
      //log.error("读取文件为一个内存字符串失败，失败原因是使用了不支持的字符编码" + charset, e);
    } catch (FileNotFoundException e) {
      //log.error("读取文件为一个内存字符串失败，失败原因所给的文件" + file + "不存在！", e);
    } catch (IOException e) {
      //log.error("读取文件为一个内存字符串失败，失败原因是读取文件异常！", e);
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
        if (writerStream != null) {
          writerStream.close();
        }
        if (out != null) {
          out.close();
        }
      } catch (Exception e) {
        //log.error("关闭文件异常！", e);
      }
    }
  }


  /**
   * 将文件夹中的子文件夹批量压缩成zip文件
   *
   * @param sourceFolderPath 文件或文件夹路径
   * @param zipFolder        生成的zip文件存在路径（包括文件名）
   */
  public static void createFolder2zip(String sourceFolderPath, String zipFolder) {
    if (StringUtils.isBlank(sourceFolderPath) || StringUtils.isBlank(zipFolder)) {
      return;
    }
    File folder = new File(sourceFolderPath);
    if (folder == null || !folder.exists() || !folder.isDirectory()) {
      //log.info(zipFolder + " unreal");
      return;
    }
    zipFolder = zipFolder.trim();
    zipFolder = zipFolder.endsWith(File.separator) ? zipFolder : zipFolder + File.separator;

    File aimFolder = new File(zipFolder);
    if (!aimFolder.exists()) {
      createDir(aimFolder.getAbsolutePath());
    }

    for (File subFolder : folder.listFiles()) {
      if (!subFolder.isDirectory()) {
        continue;
      }
      String aimFileName = zipFolder + subFolder.getName() + ".zip";
      //log.info(DateUtil.now() + " start zip subFolder=" + subFolder.getAbsolutePath() + " aimFileName=" + aimFileName);
      createZip(subFolder.getAbsolutePath(), aimFileName);
      //log.info(DateUtil.now() + " end zip subFolder=" + subFolder.getAbsolutePath());
    }
  }

  /**
   * 创建ZIP文件
   *
   * @param sourcePath 文件或文件夹路径
   * @param zipPath    生成的zip文件存在路径（包括文件名）
   */
  public static void createZip(String sourcePath, String zipPath) {
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    ZipOutputStream zos = null;
    try {
      fos = new FileOutputStream(zipPath);
      bos = new BufferedOutputStream(fos);
      zos = new ZipOutputStream(bos);
      writeZip(new File(sourcePath), "", zos);
    } catch (FileNotFoundException e) {
      StrTool.p("创建ZIP文件失败");
    } finally {
      try {
        if (zos != null) {
          zos.close();
        }
        if (bos != null) {
          bos.flush();
          bos.close();
        }
        if (fos != null) {
          fos.close();
        }
      } catch (IOException e) {
        //log.error("创建ZIP文件失败", e);
      }

    }
  }

  private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
    if (!file.exists()) {
      return;
    }
    if (file.isDirectory()) {//处理文件夹
      parentPath += file.getName() + File.separator;
      File[] files = file.listFiles();
      for (File f : files) {
        writeZip(f, parentPath, zos);
      }
    } else {
      FileInputStream fis = null;
      BufferedInputStream bis = null;
      try {
        fis = new FileInputStream(file);
        bis = new BufferedInputStream(fis);
        ZipEntry ze = new ZipEntry(parentPath + file.getName());
        zos.putNextEntry(ze);
        byte[] content = new byte[1024];
        int len;
        while ((len = bis.read(content)) != -1) {
          zos.write(content, 0, len);
          zos.flush();
        }
      } catch (FileNotFoundException e) {
        //log.error("创建ZIP文件失败", e);
      } catch (IOException e) {
        //log.error("创建ZIP文件失败", e);
      } finally {
        try {
          if (bis != null) {
            bis.close();
          }
          if (fis != null) {
            fis.close();
          }
        } catch (IOException e) {
          //log.error("创建ZIP文件失败", e);
        }
      }
    }
  }
}
