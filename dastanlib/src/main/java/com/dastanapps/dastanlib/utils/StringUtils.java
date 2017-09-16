package com.dastanapps.dastanlib.utils;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by MEBELKART on 13-01-2016.
 */
public class StringUtils {
    private static final String TAG = StringUtils.class.getSimpleName();
    private static String blockCharacterSet = "@#_+-()/\"\':;?`{}[]%<>.,~#^|$%&*!";

    public static InputFilter SPECIAL_CHAR_FILTERS = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    public static InputFilter LETTERS_N_DIGITS_FILTERS = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (source.charAt(i) == '.' || source.charAt(i) == ' ') {
                    return source.charAt(i) + "";
                } else if (!Character.isLetterOrDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };


    public static boolean stringLengthValidation(String string, int len) {
        //returns true if valid
        if ((!TextUtils.isEmpty(string)) && string.length() == len) {
            return true;
        } else
            return false;
    }

    public static boolean stringLengthShouldbeLessthan(String string, int len) {
        //returns true if valid
        if ((!TextUtils.isEmpty(string)) && string.length() < len) {
            return true;
        } else
            return false;
    }

    public static boolean stringLengthMinMax(String string, int min, int max) {
        //returns true if valid
        if ((!TextUtils.isEmpty(string)) && string.length() >= min && string.length() <= max) {
            return true;
        } else
            return false;
    }

    public static boolean stringNumericShouldbeLessthan(String string, int min, int max) {
        //returns true if valid
//        Integer.parseInt(edtConsultFee.getText().toString()) < 0 ||
//                Integer.parseInt(edtConsultFee.getText().toString()) > 2000
        if ((!TextUtils.isEmpty(string)) && string.length() != 0 && TextUtils.isDigitsOnly(string)) {
            int value = Integer.parseInt(string);
            if (value >= min && value <= max) {
                return true;
            }
            return false;
        } else
            return false;
    }

    public static boolean validateEmail(String email) {
        boolean b;
        if (!TextUtils.isEmpty(email)) {
            b = android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
        } else
            b = false;
        return b;
    }

    public static String removeCharFromLast(String normal, String removeChar) {
        if (normal.endsWith(removeChar)) {
            normal = normal.substring(0, normal.lastIndexOf(removeChar));
        }
        return normal;
    }

    public static void showPassword(EditText edt_pass) {
        edt_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }

    public static void hidePassword(EditText edt_pass) {
        edt_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public static boolean improvedShowPassword(EditText edt_pass, TextView tv, boolean isShown) {
        isShown = true;
        edt_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        tv.setText("Hide");
        return isShown;
    }

    public static boolean improvedHidePassword(EditText edt_pass, TextView tv, boolean isShown) {
        isShown = false;
        tv.setText("Show");
        edt_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        return isShown;
    }

    /**
     * @param editText
     * @param textInputLayout
     * @param stringName
     * @return true is the fields are entered
     */
    public static boolean isFormValid(EditText editText, TextInputLayout textInputLayout, String stringName) {
        String noBlank = editText.getText().toString();
        noBlank = noBlank.replace(" ", "");

        if (TextUtils.isEmpty(noBlank)) {
            textInputLayout.setError("Please Enter " + stringName);
        } else if (!stringLengthShouldbeLessthan(noBlank, 256)) {
            textInputLayout.setError("Input text should be less than 256 character");
        } else {
            textInputLayout.setError(null);
            return true;
        }
        return false;
    }

    public static boolean validatePhoneNumber(EditText editText) {
        String number = editText.getText().toString();
        if (stringLengthMinMax(number, 10, 11) && TextUtils.isDigitsOnly(number)) {
            String initChar = String.valueOf(editText.getText().toString().charAt(0));
            if (initChar.equals("7") || initChar.equals("8") || initChar.equals("9")) {
                return stringLengthShouldbeLessthan(number, 11);
            } else if (initChar.equals("0")) {
                String initChar2 = String.valueOf(editText.getText().toString().charAt(1));
                if (initChar2.equals("7") || initChar2.equals("8") || initChar2.equals("9")) {
                    return stringLengthShouldbeLessthan(number, 12);
                } else {
                    return false;
                }
            } else {
            }
            return false;
        } else
            return false;
    }

    public static String stringFromXml(Context ctxt, int resId) {
        return String.valueOf(ctxt.getText(resId));
    }

    public static boolean nameValidation(EditText editText) {
        String textStr = editText.getText().toString();
        textStr = textStr.replace(" ", "");
        textStr = textStr.replace(".", "");
        textStr = textStr.replace("-", "");
        if (textStr.matches("[a - zA - Z]"))
            return true;
        else
            return false;
    }

    public static boolean validateCity(EditText editText) {
        String cityName = editText.getText().toString();
        return (cityName.matches("^[a-zA-Z ]+$"));
    }

    public static boolean validateIFSCCode(String str) {
        return (str.matches("^[a-zA-Z]{4}[0][0-9]{6}$"));
    }

}