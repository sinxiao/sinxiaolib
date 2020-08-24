package com.xu.sinxiao.common;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.UUID;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * https://blog.csdn.net/tscyds/article/details/72861436
 */
public class CryptUtils {
    private static KeyStore keyStore;
    private static final String TAG = "CrpytUtils";

    public static void initKeyStore() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建AES的秘钥
     *
     * @param alias
     */
    public synchronized static void createSecretKey(String alias) {
        if (keyStore == null) {
            initKeyStore();
        }
        if (hasAlias(alias)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {

                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                        "AndroidKeyStore");
                try {
                    //AES算法用于加密与解密，具体参考KeyGenParameterSpec类注释
                    keyGenerator.init(new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build());

//                    keyGenerator.initialize(
////                            new KeyGenParameterSpec.Builder(alias,
////                                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
////                                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
////                                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
////                                    .build());
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
//                SecretKey key = keyGenerator.generateKey();
//                byte[] data = key.getEncoded();
//                Log.d(TAG, "SecretKey:" + key);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 对称加密 ，获取key
     *
     * @param alias
     * @return
     */
    public static SecretKey getSecretKey(String alias) {
        if (keyStore == null) {
            initKeyStore();
        }
        try {
            SecretKey secretKey = (SecretKey) keyStore.getKey(alias, null);
            Log.d(TAG, "SecretKey:" + secretKey);
            if (secretKey == null) {
                createSecretKey(alias);
                secretKey = (SecretKey) keyStore.getKey(alias, null);
            }
            Log.d(TAG, "SecretKey:" + secretKey);
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String aesKey = null;

    public static String encryptNow(String data, String alice) {
        genTheAESKey(alice);
        return Utils.encryptAES(data, aesKey);
    }

    private static void genTheAESKey(String alice) {

        if (TextUtils.isEmpty(aesKey)) {
            String encypt = Utils.getStringFromSpf(Configer.getInstance().getContext(), alice);
            Utils.showError("encypt is  >>> " + encypt);
            if (!TextUtils.isEmpty(encypt)) {
                aesKey = RSAEncryptUtil.getInstance().decryptString(encypt, Utils.ALICE + "RSA");
            } else {
                aesKey =
                        UUID.randomUUID().toString();
                aesKey = aesKey.substring(0, 24);
                Utils.saveStringToSpf(Configer.getInstance().getContext(), alice, RSAEncryptUtil.getInstance().encryptString(aesKey, Utils.ALICE + "RSA"));
            }
        }
        Utils.showError("aesKey is  >>> " + aesKey);
    }

    public static String decryptNow(String data, String alice) {
        genTheAESKey(alice);
        Utils.showError("data is  >>> " + data);
        Utils.showError("alice is  >>> " + alice);
        return Utils.dencryptAES(data, aesKey);

//        SecretKey key = getSecretKey(alice);
//        Cipher cipher = null;
//        try {
//            cipher = Cipher.getInstance("AES/GCM/NoPadding");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        }
//        try {
//            cipher.init(Cipher.DECRYPT_MODE, key);
//            try {
//                byte[] value = cipher.doFinal(Utils.hexString2Bytes(data));
//                return new String(value);
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//            }
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        }
//        return "";
    }


    private static boolean hasAlias(String alias) {
        if (keyStore == null) {
            initKeyStore();
        }
        try {
            if (keyStore != null && keyStore.containsAlias(alias)) {
                byte[] data = getSecretKey(alias).getEncoded();
                if (data != null && data.length > 0) {
                    return true;
                }
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 非对称加密算法密匙对的生成与获取
     *
     * @param alias
     */
    public static void createKeyPair(String alias) {
        if (hasAlias(alias)) {
            return;
        }
        KeyPairGenerator keyPairGenerator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
                //RSA算法用于签名与校验，具体参考KeyGenParameterSpec类注释
                keyPairGenerator.initialize(new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN |
                        KeyProperties.PURPOSE_VERIFY).setDigests(KeyProperties.DIGEST_SHA256, KeyProperties
                        .DIGEST_SHA512).build());
                KeyPair keyPair = keyPairGenerator.generateKeyPair();
//                Log.d(TAG, "createKeyPair>>PrivateKey:" + keyPair.getPrivate() + ",PublicKey:" + keyPair.getPublic());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 非对称加密
     *
     * @param alias
     * @return
     */
    public static KeyPair getTargetKeyPair(String alias) {
        try {
            KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            PublicKey publicKey = entry.getCertificate().getPublicKey();
            PrivateKey privateKey = entry.getPrivateKey();
//            Log.d(TAG, "getTargetKeyPair>>privateKey:" + privateKey + ",publicKey:" + publicKey);
            return new KeyPair(publicKey, privateKey);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        }
        return null;
    }

}
