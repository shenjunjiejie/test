package com.ucash_test.lirongyunindialoan.internet;

import android.util.Base64;
import android.util.Log;

import com.ucash_test.lirongyunindialoan.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

public class RSA {

    public static String getPUBLICKEY(){
            return PUBLICKEY_TEST3;

    }

    //测试密钥3
    private static String PUBLICKEY_TEST3 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCAdYSHdtSKQsIqBse01CdAtRe\n"+
            "ypeB2YfhL3RVWJ5s15p/SBvKNg2PtfcQNmt+RfUDosu8ZIZGhds0XxFW253PBtzG\n"+
            "gTcuNYR3WxQiHZqhD9eqnU/4lczpltlfLSLHawmPDBed/uX7aTD/avnmglIZVVAt\n"+
            "66g0P7YqdZN0JKYBewIDAQAB";

    //测试秘钥2
    private static String PUBLICKEY_TEST2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6LEYmqrxfWzKrDpspJor08ctn\n" +
            "PdJNiL+HveEH2SnovvQAW1njLqCO+379vGcaAL+ieV7Zep3AroL0iC8bJUiOVp3l\n" +
            "bNxadaSLp5mSs70L+i2fyArOk3e9QZ5dPclhvJhZ/r/qcCjWT5aDfFzYM5xxL9h+\n" +
            "IDtuMznlEujvHVHKjwIDAQAB\n";

    //测试秘钥
    public static String PUBLICKEY_TEST = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrHbsYZ4tYlcQ0GJoLj9bMqvBh\n" +
            "12s/rdeoKBbaZj63y/KkRLail3hb86SDD1V0t2jTjRqw03NejKil+Z4xesxm48iJ\n" +
            "ts854w37o3Qp2es/VJktzBVi/fAajc7TzS6z6W9HwC7FSWnzn6QawkntZZnaRChh\n" +
            "8otL57r+Px+s0UQ4uQIDAQAB";

    //正式密钥
    private static String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8Re9tlPYs6oh/g2aFgMJ7Zavk\n" +
            "Ux2Lcf1jvcgLAwzGg/B6tnnptD43yC7l7AARgZy6CgNJs/OyTW9eKqL9J8ZTDaQo\n" +
            "6etKbRbck+6790/K8rPyUg0Jhe+N20F3WLSBianaZ5ifq+DK0VF2XeZwuEOG3W8d\n" +
            "Ba6S3a9X+fdoRZ01XQIDAQAB";

    private static String RSA = "RSA";


    /**
     * 用公钥加密 <br>
     * 每次加密的字节数，不能超过密钥的长度值减去11
     *
     * @param data      需加密数据的byte数据
     * @param publicKey 公钥
     * @return 加密后的byte型数据
     */
    public static byte[] encryptData(byte[] data, PublicKey publicKey) {
        try {

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 传入编码数据并返回编码结果
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 从字符串中加载公钥

     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从字符串中加载私钥<br>
     * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
     *
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr, Base64.DEFAULT);
            // X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }


    /**
     * 用公钥对字符串进行加密
     * @param data 原文
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey)
            throws Exception {
        // 得到公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory kf = KeyFactory.getInstance(RSA);
        PublicKey keyPublic = kf.generatePublic(keySpec);
        // 加密数据
        Cipher cp = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cp.init(Cipher.ENCRYPT_MODE, keyPublic);
        return cp.doFinal(data);
    }


    public static final int DEFAULT_KEY_SIZE = 1024;//秘钥默认长度
    public static final byte[] DEFAULT_SPLIT = "#PART#".getBytes(); // 当要加密的内容超过bufferSize，则采用partSplit进行分块加密
    public static final int DEFAULT_BUFFERSIZE = (DEFAULT_KEY_SIZE / 8) - 11;// 当前秘钥支持加密的最大字节数

    /**
     * 用公钥对字符串进行分段加密
     *
     * @param data      要加密的原始数据
     * @param publicKey 公钥
     */
    public static byte[] encryptByPublicKeyForSpilt(byte[] data, byte[] publicKey) throws Exception {
        int dataLen = data.length;
        if (dataLen <= DEFAULT_BUFFERSIZE) {
            return encryptByPublicKey(data, publicKey);
        }
        List<Byte> allBytes = new ArrayList<Byte>(2048);
        int bufIndex = 0;
        int subDataLoop = 0;
        byte[] buf = new byte[DEFAULT_BUFFERSIZE];
        for (int i = 0; i < dataLen; i++) {
            buf[bufIndex] = data[i];
            if (++bufIndex == DEFAULT_BUFFERSIZE || i == dataLen - 1) {
                subDataLoop++;
                if (subDataLoop != 1) {
                    for (byte b : DEFAULT_SPLIT) {
                        allBytes.add(b);
                    }
                }
                byte[] encryptBytes = encryptByPublicKey(buf, publicKey);
                for (byte b : encryptBytes) {
                    allBytes.add(b);
                }
                bufIndex = 0;
                if (i == dataLen - 1) {
                    buf = null;
                } else {
                    buf = new byte[Math.min(DEFAULT_BUFFERSIZE, dataLen - i - 1)];
                }
            }
        }
        byte[] bytes = new byte[allBytes.size()];
        {
            int i = 0;
            for (Byte b : allBytes) {
                bytes[i++] = b.byteValue();
            }
        }
        return bytes;
    }


    /**
     * 加密
     * @param data 原数据
     * @param publicKey 秘钥
     * @return 加密后的数
     * @throws Exception 异常
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey, Base64.DEFAULT);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > DEFAULT_BUFFERSIZE) {
                cache = cipher.doFinal(data, offSet, DEFAULT_BUFFERSIZE);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * DEFAULT_BUFFERSIZE;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }


}