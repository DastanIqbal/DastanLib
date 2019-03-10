package com.dastanapps.dastanlib.network

import android.Manifest
import android.support.annotation.RequiresPermission
import android.util.Log.i
import com.dastanapps.dastanlib.DastanLibApp
import com.dastanapps.dastanlib.utils.NetworkUtils
import com.dastanapps.dastanlib.utils.R
import okhttp3.Headers
import okhttp3.MultipartBody
import org.json.JSONObject
import java.util.*

/**
 * Created by Dastan Iqbal on 10/7/2015.
 */

object DRest {

    private val TAG = DRest::class.java.simpleName

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Synchronized
    fun sendPostRequest(postUrl: String, postParams: HashMap<String, String>, reqId: Int, iRestRequest: IRestRequest) {
        if (NetworkUtils.isConnectingToInternet(DastanLibApp.INSTANCE)) {
            VolleyRequest.getPostJsonObject(postUrl, postParams, reqId, iRestRequest)
        } else {
            i(TAG, DastanLibApp.INSTANCE.getString(R.string.no_internet))
            iRestRequest.onError(reqId, "No internet Connection")
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Synchronized
    fun sendPostBodyRequest(postUrl: String, bodyPost: String, reqId: Int, iRestRequest: IRestRequest, headers: MutableMap<String, String>) {
        if (NetworkUtils.isConnectingToInternet(DastanLibApp.INSTANCE)) {
            VolleyRequest.getPostBodyJsonObject(postUrl, bodyPost, reqId, iRestRequest, headers)
        } else {
            i(TAG, "No internet connection")
            iRestRequest.onError(reqId, "No internet Connection")
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Synchronized
    fun sendPatchBodyRequest(postUrl: String, bodyPost: String, reqId: Int, iRestRequest: IRestRequest, headers: MutableMap<String, String>) {
        if (NetworkUtils.isConnectingToInternet(DastanLibApp.INSTANCE)) {
            VolleyRequest.getPatchBodyJsonObject(postUrl, bodyPost, reqId, iRestRequest, headers)
        } else {
            i(TAG, DastanLibApp.INSTANCE.getString(R.string.no_internet))
            iRestRequest.onError(reqId, DastanLibApp.INSTANCE.getString(R.string.no_internet))
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Synchronized
    fun sendJPostBodyRequest(postUrl: String, bodyPost: JSONObject, reqId: Int, iRestRequest: IRestRequest, headers: Map<String, String>) {
        if (NetworkUtils.isConnectingToInternet(DastanLibApp.INSTANCE)) {
            VolleyRequest.getJPostJsonObject(postUrl, bodyPost, reqId, iRestRequest, headers)
        } else {
            i(TAG, DastanLibApp.INSTANCE.getString(R.string.no_internet))
            iRestRequest.onError(reqId, DastanLibApp.INSTANCE.getString(R.string.no_internet))
        }
    }


    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Synchronized
    fun sendPutBodyRequest(postUrl: String, bodyPost: String, reqId: Int, iRestRequest: IRestRequest, headers: MutableMap<String, String>) {
        if (NetworkUtils.isConnectingToInternet(DastanLibApp.INSTANCE)) {
            VolleyRequest.getPutBodyJsonObject(postUrl, bodyPost, reqId, iRestRequest, headers)
        } else {
            i(TAG, "No internet connection")
            iRestRequest.onError(reqId, "No internet Connection")
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Synchronized
    fun sendPostMultiformRequest(postUrl: String, multipartBody: MultipartBody, reqId: Int, iRestRequest: IRestRequest, multiHeaders: Headers) {
        if (NetworkUtils.isConnectingToInternet(DastanLibApp.INSTANCE)) {
            OkHttpUtil.postMultipart(postUrl, multipartBody, reqId, iRestRequest, multiHeaders)
        } else {
            i(TAG, "No internet connection")
            iRestRequest.onError(reqId, "No internet Connection")
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Synchronized
    fun sendPutMultiformRequest(postUrl: String, multipartBody: MultipartBody, reqId: Int, iRestRequest: IRestRequest, headers: Headers) {
        if (NetworkUtils.isConnectingToInternet(DastanLibApp.INSTANCE)) {
            OkHttpUtil.updateMultipart(postUrl, multipartBody, reqId, iRestRequest, headers)
        } else {
            i(TAG, "No internet connection")
            iRestRequest.onError(reqId, "No internet Connection")
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Synchronized
    fun sendGetRequest(url: String, reqId: Int, iRestRequest: IRestRequest) {
        if (NetworkUtils.isConnectingToInternet(DastanLibApp.INSTANCE)) {
            VolleyRequest.getJsonObj(url, reqId, iRestRequest)
        } else {
            i(TAG, "No internet connection")
            iRestRequest.onError(reqId, "No internet Connection")
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Synchronized
    fun sendGetRequest(url: String, reqId: Int, iRestRequest: IRestRequest, headers: MutableMap<String, String>) {
        if (NetworkUtils.isConnectingToInternet(DastanLibApp.INSTANCE)) {
            VolleyRequest.getJsonArray(url, reqId, iRestRequest, headers)
        } else {
            i(TAG, "No internet connection")
            iRestRequest.onError(reqId, "No internet Connection")
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Synchronized
    fun sendGetObjRequest(url: String, reqId: Int, iRestRequest: IRestRequest, headers: MutableMap<String, String>) {
        if (NetworkUtils.isConnectingToInternet(DastanLibApp.INSTANCE)) {
            VolleyRequest.getJsonObj(url, reqId, iRestRequest, headers)
        } else {
            i(TAG, "No internet connection")
            iRestRequest.onError(reqId, "No internet Connection")
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    @Synchronized
    fun sendGetRequest(url: String, reqId: Int, requestobj: JSONObject, iRestRequest: IRestRequest) {
        if (NetworkUtils.isConnectingToInternet(DastanLibApp.INSTANCE)) {
            VolleyRequest.getJsonObj(url, reqId, requestobj, iRestRequest)
        } else {
            i(TAG, DastanLibApp.INSTANCE.getString(R.string.no_internet))
            iRestRequest.onError(reqId, DastanLibApp.INSTANCE.getString(R.string.no_internet))
        }
    }
}
