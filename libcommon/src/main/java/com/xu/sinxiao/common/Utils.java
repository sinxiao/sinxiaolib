package com.xu.sinxiao.common;

/**
 * https://jitpack.io/docs/ANDROID/
 */

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Objects;

public class Utils {
    private static Gson gson = new Gson();
    public static String ALICE = Configer.getInstance().getPackageName() + "key_alice";
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

    private static byte charToByte(char c) {
        String chars = "0123456789ABCDEF";
        byte b = (byte) chars.indexOf(c);
        return b;
    }

    /**
     * @param hexStr
     * @return 将十六进制转换为字节数组
     */
    public static byte[] hexString2Bytes(String hexStr) {
        if (TextUtils.isEmpty(hexStr) || hexStr.length() == 0) {
            return null;
        }
        if (hexStr.length() % 2 == 1) {
            hexStr = "0" + hexStr;
        }
        int len = hexStr.length() / 2;
        byte[] result = new byte[len];
        char[] chars = hexStr.toCharArray();
        for (int i = 0; i < len; i++) {
            result[i] = (byte) (charToByte(chars[i]) << 4 | charToByte(chars[i + 1]));
        }
        return result;
    }

    /**
     * @param bytes
     * @return 将二进制转换为十六进制字符输出
     */
    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        String strInt = "";
        for (int i = 0; i < bytes.length; i++) {
            strInt = Integer.toHexString(bytes[i] & 0xFF);
            if (strInt.length() < 2) {
                sb.append(0);
            }
            sb.append(strInt.toUpperCase());
        }
        return sb.toString();
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

    public static String encryptRSALocal(String value) {
        return RSAEncryptUtil.getInstance().encryptString(value, ALICE + "RSA");
    }

    public static String dencryptRSALocal(String data) {
        return RSAEncryptUtil.getInstance().decryptString(data, ALICE + "RSA");
    }

    public static String encryptAESLocal(String value) {
        return CryptUtils.encryptNow(value, ALICE + "AES");
    }

    public static String dencryptAESLocal(String data) {
        return CryptUtils.decryptNow(data, ALICE + "AES");
    }

    public static String encryptAES(String data, String password) {
        try {
            return Base64.encodeToString(AESUtil.encryptByte2Byte(data.getBytes(), password), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dencryptAES(String value, String password) {
        try {
            return new String(AESUtil.decryptByte2Byte(Base64.decode(value, Base64.DEFAULT), password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void saveSpfWithEncrypt(Context context, String key, String vlaue) {
        saveStringToSpf(context, key, encryptAESLocal(vlaue));
    }

    public static String getSpfWithEncrypt(Context context, String key) {
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
