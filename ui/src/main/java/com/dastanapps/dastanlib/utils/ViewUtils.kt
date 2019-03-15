package com.dastanapps.dastanlib.utils

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.support.v7.view.ContextThemeWrapper
import android.support.v7.widget.AppCompatEditText
import android.text.TextUtils
import android.view.*
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.dastanapps.dastanlib.DastanLibApp
import com.dastanapps.dastanlib.ui.R
import com.dastanapps.dastanlib.utils.StringUtils.stringLengthShouldbeLessthan

/**
 * *@author : Dastan Iqbal
 *
 * @email : ask2iqbal@gmail.com
 */

object ViewUtils {

    private val TAG = ViewUtils::class.java.simpleName
    private var progressDialog: ProgressDialog? = null
    private val progressDialogNotCancellable: ProgressDialog? = null
    private var snack: Snackbar? = null

    private var mToast: Toast? = null

    val isProgressDialog: Boolean
        get() = if (progressDialog != null) progressDialog!!.isShowing else false

    /**
     * @param ctxt
     */

    fun showProgressDialog(ctxt: Context?) {
        if (ctxt != null && (progressDialog == null || !progressDialog!!.isShowing)) {
            progressDialog = ProgressDialog(ctxt)
            // progressDialog.setCancelable(false);
            progressDialog!!.setMessage("Loading")
            progressDialog!!.setIndeterminateDrawable(getDrawable(ctxt as Activity, R.drawable.progressbar_circle))
            progressDialog!!.show()
            progressDialog!!.setCanceledOnTouchOutside(true)

            progressDialog!!.setOnDismissListener {
                hideProgressDialog()
                //                    progressDialog.dismiss();
            }

            progressDialog!!.setOnCancelListener { progressDialog!!.dismiss() }
        }
    }

    fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    fun showProgressDialogNotCancellable(ctxt: Context?) {
        if (ctxt != null && (progressDialog == null || !progressDialog!!.isShowing)) {
            progressDialog = ProgressDialog(ctxt)
            // progressDialog.setCancelable(false);
            progressDialog!!.setMessage("Loading")
            progressDialog!!.setIndeterminateDrawable(getDrawable(ctxt as Activity, R.drawable.progressbar_circle))
            progressDialog!!.show()
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.setCancelable(false)

            progressDialog!!.setOnDismissListener {
                hideProgressDialog()
                //                    progressDialog.dismiss();
            }

            progressDialog!!.setOnCancelListener { progressDialog!!.dismiss() }
        }
    }

    fun hideProgressDialogNotCancellable() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }


    fun showToast(ctxt: Context, msg: String) {
        if (mToast != null)
            mToast!!.cancel()
        mToast = Toast.makeText(DastanLibApp.INSTANCE, msg, Toast.LENGTH_LONG)
        mToast!!.show()
    }

    fun showSnack(msg: String, actionStr: String, snackView: View, listener: View.OnClickListener) {
        //with action listener
        if (snack != null)
            snack!!.dismiss()
        snack = Snackbar.make(snackView, msg, Snackbar.LENGTH_LONG)
        val view = snack!!.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snack!!.setAction(actionStr, listener)
        snack!!.duration = Snackbar.LENGTH_LONG
        snack!!.show()
    }

    fun showSnack(msg: String, actionStr: String, snackView: View, listener: View.OnClickListener, tagValue: String) {
        //with action listener
        if (snack != null)
            snack!!.dismiss()
        snack = Snackbar.make(snackView, msg, Snackbar.LENGTH_LONG)
        val view = snack!!.view as Snackbar.SnackbarLayout
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        view.findViewById<View>(R.id.snackbar_action).setTag(R.id.handleMultipleTap, tagValue)
        snack!!.setAction(actionStr, listener)
        snack!!.duration = Snackbar.LENGTH_LONG
        snack!!.show()
    }

    fun showSnackDown(msg: String, actionStr: String, snackView: View, listener: View.OnClickListener) {
        //with action listener
        val snack = Snackbar.make(snackView, msg, Snackbar.LENGTH_LONG)
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        view.layoutParams = params
        snack.setAction(actionStr, listener)
        snack.duration = Snackbar.LENGTH_INDEFINITE
        snack.show()
    }

    fun showSnack(msg: String, snackView: View, gravity: Int) {
        val snack = Snackbar.make(snackView, msg, Snackbar.LENGTH_LONG)
        val view = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = gravity
        view.layoutParams = params
        snack.show()
    }

    fun showSnack(msg: String, snackView: View) {
        Snackbar.make(snackView, msg, Snackbar.LENGTH_LONG).show()
    }

    fun inflateLayout(ctxt: Context, resId: Int): View {
        return LayoutInflater.from(ctxt).inflate(resId, null)
    }

    fun inflateLayout(ctxt: Context, resId: Int, parent: ViewGroup): View {
        return LayoutInflater.from(ctxt).inflate(resId, parent, false)
    }

    fun getDDialog(ctxt: Context, reslayout: Int): Dialog {
        val d = Dialog(ctxt)
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        d.setContentView(reslayout)
        d.setCanceledOnTouchOutside(false)
        return d
    }

    fun getDDialogOK(ctxt: Context, title: String, msg: String,
                     positiveText: String, negativeText: String, okInterface: DialogInterface.OnClickListener,
                     cancelInterface: DialogInterface.OnClickListener): Dialog {
        val builder = AlertDialog.Builder(ctxt)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton(positiveText, okInterface)
        builder.setNegativeButton(negativeText, cancelInterface)
        return builder.create()
    }

    fun getDDialogOK(ctxt: Context, title: String, msg: String,
                     positiveText: String, okInterface: DialogInterface.OnClickListener, isCancelable: Boolean): AlertDialog {
        val builder = AlertDialog.Builder(ctxt)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setCancelable(isCancelable)
        builder.setPositiveButton(positiveText, okInterface)

        return builder.create()
    }

    fun getCustomDDialogOK(ctxt: Context, title: String, msg: String,
                           positiveText: String, okInterface: DialogInterface.OnClickListener, isCancelable: Boolean): AlertDialog {
        val builder = AlertDialog.Builder(ContextThemeWrapper(ctxt, R.style.AlertDialogCustom))
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setCancelable(isCancelable)
        builder.setPositiveButton(positiveText, okInterface)
        return builder.create()
    }

    /**
     * used to support all versions of OS
     *
     * @param ctxt
     * @param res
     * @return
     */

    fun getDrawable(ctxt: Activity, res: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ctxt.resources.getDrawable(res, ctxt.theme)
        } else {
            ctxt.resources.getDrawable(res)
        }
    }

    fun changeEditTextBottomColor(tvText: AppCompatEditText, color: Int) {
        val drawable = tvText.background // get current EditText drawable
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP) // change the drawable color

        if (Build.VERSION.SDK_INT > 16) {
            tvText.background = drawable // set the new drawable to EditText
        } else {
            tvText.setBackgroundDrawable(drawable) // use setBackgroundDrawable because setBackground required API 16
        }
    }

    fun setFontTextViews(ctxt: Context, font: Int, vararg tv: TextView) {
        val tf = Typeface.createFromAsset(ctxt.assets,
                ctxt.resources.getString(font))
        for (tview in tv) {
            tview.typeface = tf
        }
    }

    fun getDimensFromRes(ctxt: Context, dimenId: Int): Int {
        return (ctxt.resources.getDimension(dimenId) / ctxt.resources.displayMetrics.density).toInt()
    }

    /**
     * @param editText
     * @param textInputLayout
     * @param stringName
     * @return true is the fields are entered
     */

    fun isFormValid(editText: EditText, textInputLayout: TextInputLayout, stringName: String): Boolean {
        var noBlank = editText.text.toString()
        noBlank = noBlank.replace(" ", "")

        if (TextUtils.isEmpty(noBlank)) {
            textInputLayout.error = "Please Enter $stringName"
        } else if (!stringLengthShouldbeLessthan(noBlank, 256)) {
            textInputLayout.error = "Input text should be less than 256 character"
        } else {
            textInputLayout.error = null
            return true
        }
        return false
    }
}

