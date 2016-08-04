package com.dastanapps.dastanlib.Utils;

import android.content.Context;
import android.text.TextUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Rakshith on 16/3/16.
 */
public class AesHelper {

    public static byte[] encrypt(String plainText, String encryptionKey, String IV) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public static String decrypt(byte[] cipherText, String encryptionKey, String IV) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        return new String(cipher.doFinal(cipherText),"UTF-8");
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }

    private final static String HEX = "0123456789ABCDEF";
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }

    public static String getIV(Context ctxt) {
        String iv = CommonUtils.getAccountName(ctxt);
//        iv = CommonUtils.getAccountName(SplashA.this) != null ? CommonUtils.getAccountName(SplashA.this) : "dummy@mekart.com";
        if (TextUtils.isEmpty(iv)) {
            iv = "dummy@mekart.com";
        } else {
            int ivLength = iv.length();
            int stringLen = ivLength % 16;
            StringBuilder stringBuilder = new StringBuilder();
            if (ivLength < 16) {
                for (int i = stringLen; i < 16; i++) {
                    stringBuilder.append(" ");
                }
                iv = iv + stringBuilder.toString();
            } else {
                iv = iv.substring(0, 16);
            }
        }

        if (iv.length() == 16)
            return iv;
        else return "dummy@mekart.com";
    }
}

