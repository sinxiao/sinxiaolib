package com.xu.sinxiao.common;

/**
 * https://jitpack.io/docs/ANDROID/
 */

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Utils {
    private static Gson gson = new Gson();
    private static String ALICE_AES = Configer.getInstance().getPackageName() + "aes_alice";
    private static String hexStr = "0123456789ABCDEF";
    private static String[] binaryArray =
            {"0000", "0001", "0010", "0011",
                    "0100", "0101", "0110", "0111",
                    "1000", "1001", "1010", "1011",
                    "1100", "1101", "1110", "1111"};

    public static void showLog(String info) {
        Log.d("Utils", " ===== " + info);
    }

    public static void showError(String error) {
        Log.e("Utils", " ===== " + error);
    }

    public static void saveStringToSpf(Context context, String key, String value) {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(key, "key is null");
        Objects.requireNonNull(value, "value is null");
        context.getSharedPreferences("data", 0).edit().putString(key, value).commit();
    }

    public static String getStringFromSpf(Context context, String key) {
        Objects.requireNonNull(context, "context is null");
        Objects.requireNonNull(key, "key is null");
        return context.getSharedPreferences("data", 0).getString(key, "");
    }

    /**
     * @param hexString
     * @return 将十六进制转换为字节数组
     */
    public static byte[] hexString2Bytes(String hexString) {
        //hexString的长度对2取整，作为bytes的长度
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        byte high = 0;//字节高四位
        byte low = 0;//字节低四位

        for (int i = 0; i < len; i++) {
            //右移四位得到高位
            high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
            low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
            bytes[i] = (byte) (high | low);//高地位做或运算
        }
        return bytes;
    }

    /**
     * @param bytes
     * @return 将二进制转换为十六进制字符输出
     */
    public static String bytes2HexString(byte[] bytes) {
        String result = "";
        String hex = "";
        for (int i = 0; i < bytes.length; i++) {
            //字节高4位
            hex = String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4));
            //字节低4位
            hex += String.valueOf(hexStr.charAt(bytes[i] & 0x0F));
            result += hex + " ";
        }
        return result;
    }

    /**
     * 转换为二进制字符串
     */
    public static String bytes2BinaryStr(byte[] bArray) {
        String outStr = "";
        int pos = 0;
        for (byte b : bArray) {
            //高四位
            pos = (b & 0xF0) >> 4;
            outStr += binaryArray[pos];
            //低四位
            pos = b & 0x0F;
            outStr += binaryArray[pos];
        }
        return outStr;
    }

    public static String encryptAESLocal(String value) {
        try {
            CryptUtils.initKeyStore();
            SecretKey secretKey = CryptUtils.getSecretKey(ALICE_AES);
            if (secretKey == null) {
                CryptUtils.createSecretKey(ALICE_AES);
                secretKey = CryptUtils.getSecretKey(ALICE_AES);
            }
            if (secretKey != null) {
                return bytes2HexString(encryptAESNow(value, secretKey.getEncoded()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dencryptAESLocal(String data) {
        try {
            CryptUtils.initKeyStore();
            SecretKey secretKey = CryptUtils.getSecretKey(ALICE_AES);
            if (secretKey == null) {
                CryptUtils.createSecretKey(ALICE_AES);
                secretKey = CryptUtils.getSecretKey(ALICE_AES);
            }
            if (secretKey != null) {
                return bytes2HexString(dencryptAESNow(hexString2Bytes(data), secretKey.getEncoded()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encryptAES(String data, String password) {
        try {
            return bytes2HexString(encryptAESNow(data, password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dencryptAES(String value, String password) {
        try {
            return bytes2HexString(dencryptAESNow(hexString2Bytes(value), password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dencryptAES(String value, byte[] password) {
        try {
            return bytes2HexString(dencryptAESNow(hexString2Bytes(value), password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 这里的password的长度，必须为128或192或256bits.也就是16或24或32byte。否则会报出如下错误：
     * com.android.org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$1: Key length not 128/192/256 bits.
     *
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    private static byte[] encryptAESNow(String content, String password) throws Exception {
        // 创建AES秘钥
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES/CBC/PKCS5PADDING");
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化加密器
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 加密
        return cipher.doFinal(content.getBytes("UTF-8"));
    }

    /**
     * 这里的password的长度，必须为128或192或256bits.也就是16或24或32byte。否则会报出如下错误：
     * com.android.org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$1: Key length not 128/192/256 bits.
     *
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    private static byte[] encryptAESNow(String content, byte[] password) throws Exception {
        // 创建AES秘钥
        SecretKeySpec key = new SecretKeySpec(password, "AES/CBC/PKCS5PADDING");
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化加密器
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 加密
        return cipher.doFinal(content.getBytes("UTF-8"));
    }

    /**
     * 这里的password的长度，必须为128或192或256bits.也就是16或24或32byte。否则会报出如下错误：
     * com.android.org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$1: Key length not 128/192/256 bits.
     *
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    private static byte[] dencryptAESNow(byte[] content, String password) throws Exception {
        // 创建AES秘钥
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES/CBC/PKCS5PADDING");
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化解密器
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 解密
        return cipher.doFinal(content);
    }

    /**
     * 这里的password的长度，必须为128或192或256bits.也就是16或24或32byte。否则会报出如下错误：
     * com.android.org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher$1: Key length not 128/192/256 bits.
     *
     * @param content
     * @param password
     * @return
     * @throws Exception
     */
    private static byte[] dencryptAESNow(byte[] content, byte[] password) throws Exception {
        // 创建AES秘钥
        SecretKeySpec key = new SecretKeySpec(password, "AES/CBC/PKCS5PADDING");
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化解密器
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 解密
        return cipher.doFinal(content);
    }

    public void saveSpfWithEncrypt(Context context, String key, String vlaue) {
        saveStringToSpf(context, key, encryptAESLocal(vlaue));
    }

    public String getSpfWithEncrypt(Context context, String key) {
        String data = getStringFromSpf(context, key);
        if (TextUtils.isEmpty(data)) {
            return "";
        } else {
            return dencryptAESLocal(data);
        }
    }

    public static String toJSON(Object object) {
        return gson.toJson(object);
    }

    public static <T> T parseJSON(String json, Class<T> t) {
        return gson.fromJson(json, t);
    }

}
