package com.dastanapps.dastanlib.utils

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import android.text.TextUtils
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Iqbal Ahmed on 16/3/16.
 */
object AesHelper {

    private val HEX = "0123456789ABCDEF"

    @Throws(Exception::class)
    fun encrypt(plainText: String, encryptionKey: String, IV: String): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val key = SecretKeySpec(encryptionKey.toByteArray(Charset.defaultCharset()), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(IV.toByteArray(Charset.defaultCharset())))
        return cipher.doFinal(plainText.toByteArray(Charset.defaultCharset()))
    }

    @Throws(Exception::class)
    fun decrypt(cipherText: ByteArray, encryptionKey: String, IV: String): String {
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val key = SecretKeySpec(encryptionKey.toByteArray(Charset.defaultCharset()), "AES")
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(IV.toByteArray(Charset.defaultCharset())))
        return String(cipher.doFinal(cipherText), Charset.defaultCharset())
    }

    //TODO: fix toHex
    fun toHex(buf: ByteArray?): String {
        if (buf == null)
            return ""
        val result = StringBuffer(2 * buf.size)
        for (i in buf.indices) {
            appendHex(result, buf[i].toInt()) //FIXME it should be byte
        }
        return result.toString()
    }

    fun toByte(hexString: String): ByteArray {
        val len = hexString.length / 2
        val result = ByteArray(len)
        for (i in 0 until len)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).toByte()
        return result
    }

    private fun appendHex(sb: StringBuffer, b: Int) {
        sb.append(HEX[b.shr(4) and 0x0f]).append(HEX[(b and 0x0f)])
    }

    @RequiresPermission(Manifest.permission.GET_ACCOUNTS)
    fun getIV(ctxt: Context): String {
        var iv = CommonUtils.getAccountName(ctxt)
        //        iv = CommonUtils.getAccountName(SplashA.this) != null ? CommonUtils.getAccountName(SplashA.this) : "dummy@mekart.com";
        iv = if (TextUtils.isEmpty(iv)) {
            "dummy@dastanapps.com"
        } else {
            val ivLength = iv!!.length
            val stringLen = ivLength % 16
            val stringBuilder = StringBuilder()
            if (ivLength < 16) {
                for (i in stringLen..15) {
                    stringBuilder.append(" ")
                }
                iv + stringBuilder.toString()
            } else {
                iv.substring(0, 16)
            }
        }

        return if (iv.length == 16)
            iv
        else
            "dummy@dastanapps.com"
    }
}

