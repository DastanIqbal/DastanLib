package com.dastanapps.dastanlib.utils

import android.content.Context
import android.graphics.Matrix
import android.text.InputFilter
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.TextView
import java.util.*

/**
 * Created by Iqbal Ahmed on 13-01-2016.
 */

object StringUtils {
    private val TAG = StringUtils::class.java.simpleName
    private val blockCharacterSet = "@#_+-()/\"\':;?`{}[]%<>.,~#^|$%&*!"

    var SPECIAL_CHAR_FILTERS: InputFilter = InputFilter { source, start, end, dest, dstart, dend ->
        if (source != null && blockCharacterSet.contains("" + source)) {
            ""
        } else null
    }

    var LETTERS_N_DIGITS_FILTERS: InputFilter = InputFilter { source, start, end, dest, dstart, dend ->
        for (i in start until end) {
            if (source[i] == '.' || source[i] == ' ') {
                return@InputFilter source[i] + ""
            } else if (!Character.isLetterOrDigit(source[i])) {
                return@InputFilter ""
            }
        }
        null
    }


    /**
     * @param string
     * @param len
     * @return true if valid
     */

    fun stringLengthValidation(string: String, len: Int): Boolean {
        return !TextUtils.isEmpty(string) && string.length == len
    }

    /**
     * @param string
     * @param len
     * @return true if valid
     */

    fun stringLengthShouldbeLessthan(string: String, len: Int): Boolean {
        //returns true if valid
        return !TextUtils.isEmpty(string) && string.length < len
    }


    /**
     * @param string
     * @param min
     * @param max
     * @return true if valid
     */

    fun stringLengthMinMax(string: String, min: Int, max: Int): Boolean {
        //returns true if valid
        return !TextUtils.isEmpty(string) && string.length >= min && string.length <= max
    }

    fun validateEmail(email: String): Boolean {
        val b: Boolean
        if (!TextUtils.isEmpty(email)) {
            b = android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim { it <= ' ' }).matches()
        } else
            b = false
        return b
    }

    fun removeCharFromLast(normal: String, removeChar: String): String {
        var normal = normal
        if (normal.endsWith(removeChar)) {
            normal = normal.substring(0, normal.lastIndexOf(removeChar))
        }
        return normal
    }

    fun showPassword(edt_pass: EditText) {
        edt_pass.transformationMethod = HideReturnsTransformationMethod.getInstance()
    }

    fun hidePassword(edt_pass: EditText) {
        edt_pass.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    fun improvedShowPassword(edt_pass: EditText, tv: TextView, isShown: Boolean): Boolean {
        var isShown = isShown
        isShown = true
        edt_pass.transformationMethod = HideReturnsTransformationMethod.getInstance()
        tv.text = "Hide"
        return isShown
    }

    fun improvedHidePassword(edt_pass: EditText, tv: TextView, isShown: Boolean): Boolean {
        var isShown = isShown
        isShown = false
        tv.text = "Show"
        edt_pass.transformationMethod = PasswordTransformationMethod.getInstance()
        return isShown
    }

    fun validatePhoneNumber(editText: EditText): Boolean {
        val number = editText.text.toString()
        if (stringLengthMinMax(number, 10, 11) && TextUtils.isDigitsOnly(number)) {
            val initChar = editText.text.toString()[0].toString()
            if (initChar == "7" || initChar == "8" || initChar == "9") {
                return stringLengthShouldbeLessthan(number, 11)
            } else if (initChar == "0") {
                val initChar2 = editText.text.toString()[1].toString()
                return if (initChar2 == "7" || initChar2 == "8" || initChar2 == "9") {
                    stringLengthShouldbeLessthan(number, 12)
                } else {
                    false
                }
            } else {
            }
            return false
        } else
            return false
    }

    fun stringFromXml(ctxt: Context, resId: Int): String {
        return ctxt.getText(resId).toString()
    }

    fun nameValidation(editText: EditText): Boolean {
        var textStr = editText.text.toString()
        textStr = textStr.replace(" ", "")
        textStr = textStr.replace(".", "")
        textStr = textStr.replace("-", "")
        return textStr.matches("[a - zA - Z]".toRegex())
    }

    fun validateCity(editText: EditText): Boolean {
        val cityName = editText.text.toString()
        return cityName.matches("^[a-zA-Z ]+$".toRegex())
    }

    fun validateIFSCCode(str: String): Boolean {
        return str.matches("^[a-zA-Z]{4}[0][0-9]{6}$".toRegex())
    }

    fun toArrayString(matrix: Matrix): String {
        val temp = FloatArray(9)
        matrix.getValues(temp)
        return Arrays.toString(temp).replace("[", "").replace("]", "")
    }

    fun toMatrix(matrixFloatArrayString: String): Matrix {
        val floatArray = FloatArray(9)
        val stringArray = matrixFloatArrayString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in stringArray.indices) {
            floatArray[i] = java.lang.Float.parseFloat(stringArray[i])
        }
        val matrix = Matrix()
        matrix.setValues(floatArray)
        return matrix
    }

}
