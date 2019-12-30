package com.yangj.dahemodule.util;

import android.text.TextUtils;
import android.util.Base64;

import com.arialyy.aria.exception.ParamException;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Author:xch
 * Date:2019/9/3
 * Description:
 */
public class AesUtils {

    private static final String ENCODING = "UTF-8";

    private static final String PASSWORD = "thanks,tepia.com"; // 加密秘钥


    /*
     * 加密
     */
    public static String encrypt(String cleartext) {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        try {
            byte[] result = encrypt(PASSWORD, cleartext);
            return Base64.encodeToString(result, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] encrypt(String password, String clear) throws Exception {
        // 创建AES秘钥
        SecretKeySpec secretKeySpec = new SecretKeySpec(password.getBytes("UTF-8"), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(password.getBytes("UTF-8"));
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 初始化加密器
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);
        // 加密
        return cipher.doFinal(clear.getBytes("UTF-8"));
    }

    private byte[] decrypt(byte[] content, String password) throws Exception {
        // 创建AES秘钥
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES/CBC/PKCS5PADDING");
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化解密器
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 解密
        return cipher.doFinal(content);
    }
}
