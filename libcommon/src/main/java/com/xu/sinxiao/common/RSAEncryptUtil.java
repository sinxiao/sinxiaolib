package com.xu.sinxiao.common;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

/**
 * https://www.jianshu.com/p/06775ddf435f
 */
public class RSAEncryptUtil {
    private static final String TAG = "EncryptUtil";
    private static RSAEncryptUtil encryptUtilInstance = new RSAEncryptUtil();

    private KeyStore keyStore;

    private Context context;
    // 单位年
    private final int maxExpiredTime = 1000;

    private String x500PrincipalName = "CN=MyKey, O=Android Authority";

    // RSA有加密字符长度限制，所以需要分段加密
    private int rsaEncryptBlock = 244;
    private int rsaDecryptBlock = 256;

    private RSAEncryptUtil() {
    }

    public static RSAEncryptUtil getInstance() {
        return encryptUtilInstance;
    }

    public void init(Context context, String x500PrincipalName) {
        this.context = context;
        if (!TextUtils.isEmpty(x500PrincipalName)) {
            this.x500PrincipalName = x500PrincipalName;
        }

    }

    public void initKeyStore(String alias) {
        synchronized (RSAEncryptUtil.class) {
            try {
                if (null == keyStore) {
                    keyStore = KeyStore.getInstance("AndroidKeyStore");
                    keyStore.load(null);
                }
                createNewKeys(alias);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createNewKeys(String alias) {
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        try {
            if (keyStore.containsAlias(alias)) {
                return;
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // Create new key
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, maxExpiredTime);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                        .setAlias(alias)
                        .setSubject(new X500Principal(x500PrincipalName))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                generator.initialize(spec);
                generator.generateKeyPair();
            } else {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
                keyPairGenerator.initialize(
                        new KeyGenParameterSpec.Builder(
                                alias,
                                KeyProperties.PURPOSE_DECRYPT)
                                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                                .setUserAuthenticationRequired(false)
                                .build());
                keyPairGenerator.generateKeyPair();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearKeystor(String alias) {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            keyStore.deleteEntry(alias);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密方法
     *
     * @param needEncryptWord 　需要加密的字符串
     * @param alias           　加密秘钥
     * @return
     */
    public String encryptString(String needEncryptWord, String alias) {
        if (TextUtils.isEmpty(needEncryptWord) || TextUtils.isEmpty(alias)) {
            return "";
        }
        String encryptStr = "";
        synchronized (RSAEncryptUtil.class) {
            initKeyStore(alias);
            try {
                PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();
                Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int offSet = 0;
                int inputLen = needEncryptWord.length();
                byte[] inputData = needEncryptWord.getBytes();
                for (int i = 0; inputLen - offSet > 0; offSet = i * rsaEncryptBlock) {
                    byte[] cache;
                    if (inputLen - offSet > rsaEncryptBlock) {
                        cache = inCipher.doFinal(inputData, offSet, rsaEncryptBlock);
                    } else {
                        cache = inCipher.doFinal(inputData, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    ++i;
                }
                byte[] encryptedData = out.toByteArray();
                out.close();

                encryptStr = Base64.encodeToString(encryptedData, Base64.URL_SAFE);
            } catch (Exception e) {
                e.printStackTrace();
//                LogUtil.e(TAG, "in encryptString error:" + e.getMessage());
            }
        }
        return encryptStr;
    }


    /**
     * 解密方法
     *
     * @param needDecryptWord 需要解密的字符串
     * @param alias           key的别称
     * @return
     */
    public String decryptString(String needDecryptWord, String alias) {
        if (TextUtils.isEmpty(needDecryptWord) || TextUtils.isEmpty(alias)) {
            return "";
        }
        String decryptStr = "";
        synchronized (RSAEncryptUtil.class) {
            initKeyStore(alias);
            try {
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, null);
                Cipher outCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                outCipher.init(Cipher.DECRYPT_MODE, privateKey);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int offSet = 0;
                byte[] encryptedData = Base64.decode(needDecryptWord, Base64.URL_SAFE);
                int inputLen = encryptedData.length;
                for (int i = 0; inputLen - offSet > 0; offSet = i * rsaDecryptBlock) {
                    byte[] cache;
                    if (inputLen - offSet > rsaDecryptBlock) {
                        cache = outCipher.doFinal(encryptedData, offSet, rsaDecryptBlock);
                    } else {
                        cache = outCipher.doFinal(encryptedData, offSet, inputLen - offSet);
                    }
                    out.write(cache, 0, cache.length);
                    ++i;
                }
                byte[] decryptedData = out.toByteArray();
                out.close();

                decryptStr = new String(decryptedData, 0, decryptedData.length, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
//                LogUtil.e(TAG, "in decryptString error:" + e.getLocalizedMessage());
            }
        }
        return decryptStr;
    }
}
