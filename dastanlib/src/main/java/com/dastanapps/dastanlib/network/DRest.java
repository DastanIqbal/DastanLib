package com.dastanapps.dastanlib.network;

import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.R;
import com.dastanapps.dastanlib.utils.NetworkUtils;
import com.dastanapps.dastanlib.utils.StringUtils;
import com.dastanapps.dastanlib.utils.ViewUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MultipartBody;

import static android.util.Log.i;

/**
 * Created by Dastan Iqbal on 10/7/2015.
 */
public class DRest {

    private static final String TAG = DRest.class.getSimpleName();

    public static synchronized void sendPostRequest(String postUrl, HashMap<String, String> postParams, int reqId, IRestRequest iRestRequest) {
        if (NetworkUtils.isConnectingToInternet(DastanApp.getInstance())) {
            VolleyRequest.getPostJsonObject(postUrl, postParams, reqId, iRestRequest);
        } else {
            i(TAG, DastanApp.getInstance().getString(R.string.no_internet));
            ViewUtils.showToast(DastanApp.getInstance(), StringUtils.stringFromXml(DastanApp.getInstance(), R.string.no_internet));
            iRestRequest.onError(reqId, "No internet Connection");
        }
    }

    public static synchronized void sendPostBodyRequest(String postUrl, String bodyPost, int reqId, IRestRequest iRestRequest, Map<String, String> headers) {
        if (NetworkUtils.isConnectingToInternet(DastanApp.getInstance())) {
            VolleyRequest.getPostBodyJsonObject(postUrl, bodyPost, reqId, iRestRequest, headers);
        } else {
            i(TAG, "No internet connection");
            ViewUtils.showToast(DastanApp.getInstance(), StringUtils.stringFromXml(DastanApp.getInstance(), R.string.no_internet));
            iRestRequest.onError(reqId, "No internet Connection");
        }
    }

    public static synchronized void sendPatchBodyRequest(String postUrl, String bodyPost, int reqId, IRestRequest iRestRequest, Map<String, String> headers) {
        if (NetworkUtils.isConnectingToInternet(DastanApp.getInstance())) {
            VolleyRequest.getPatchBodyJsonObject(postUrl, bodyPost, reqId, iRestRequest, headers);
        } else {
            i(TAG, DastanApp.getInstance().getString(R.string.no_internet));
            ViewUtils.showToast(DastanApp.getInstance(), StringUtils.stringFromXml(DastanApp.getInstance(), R.string.no_internet));
            iRestRequest.onError(reqId, DastanApp.getInstance().getString(R.string.no_internet));
        }
    }

    public static synchronized void sendJPostBodyRequest(String postUrl, JSONObject bodyPost, int reqId, IRestRequest iRestRequest, Map<String, String> headers) {
        if (NetworkUtils.isConnectingToInternet(DastanApp.getInstance())) {
            VolleyRequest.getJPostJsonObject(postUrl, bodyPost, reqId, iRestRequest, headers);
        } else {
            i(TAG, DastanApp.getInstance().getString(R.string.no_internet));
            ViewUtils.showToast(DastanApp.getInstance(), StringUtils.stringFromXml(DastanApp.getInstance(), R.string.no_internet));
            iRestRequest.onError(reqId, DastanApp.getInstance().getString(R.string.no_internet));
        }
    }


    public static synchronized void sendPutBodyRequest(String postUrl, String bodyPost, int reqId, IRestRequest iRestRequest, Map<String, String> headers) {
        if (NetworkUtils.isConnectingToInternet(DastanApp.getInstance())) {
            VolleyRequest.getPutBodyJsonObject(postUrl, bodyPost, reqId, iRestRequest, headers);
        } else {
            i(TAG, "No internet connection");
            ViewUtils.showToast(DastanApp.getInstance(), StringUtils.stringFromXml(DastanApp.getInstance(), R.string.no_internet));
            iRestRequest.onError(reqId, "No internet Connection");
        }
    }

    public static synchronized void sendPostMultiformRequest(String postUrl, MultipartBody multipartBody, int reqId, IRestRequest iRestRequest, Headers multiHeaders) {
        if (NetworkUtils.isConnectingToInternet(DastanApp.getInstance())) {
            OkHttpUtil.postMultipart(postUrl, multipartBody, reqId, iRestRequest, multiHeaders);
        } else {
            i(TAG, "No internet connection");
            ViewUtils.showToast(DastanApp.getInstance(), StringUtils.stringFromXml(DastanApp.getInstance(), R.string.no_internet));
            iRestRequest.onError(reqId, "No internet Connection");
        }
    }

    public static synchronized void sendPutMultiformRequest(String postUrl, MultipartBody multipartBody, int reqId, IRestRequest iRestRequest, Headers headers) {
        if (NetworkUtils.isConnectingToInternet(DastanApp.getInstance())) {
            OkHttpUtil.updateMultipart(postUrl, multipartBody, reqId, iRestRequest, headers);
        } else {
            i(TAG, "No internet connection");
            ViewUtils.showToast(DastanApp.getInstance(), StringUtils.stringFromXml(DastanApp.getInstance(), R.string.no_internet));
            iRestRequest.onError(reqId, "No internet Connection");
        }
    }

    public static synchronized void sendGetRequest(String url, int reqId, IRestRequest iRestRequest) {
        if (NetworkUtils.isConnectingToInternet(DastanApp.getInstance())) {
            VolleyRequest.getJsonObj(url, reqId, iRestRequest);
        } else {
            i(TAG, "No internet connection");
            ViewUtils.showToast(DastanApp.getInstance(), StringUtils.stringFromXml(DastanApp.getInstance(), R.string.no_internet));
            iRestRequest.onError(reqId, "No internet Connection");
        }
    }

    public static synchronized void sendGetRequest(String url, int reqId, IRestRequest iRestRequest, Map<String, String> headers) {
        if (NetworkUtils.isConnectingToInternet(DastanApp.getInstance())) {
            VolleyRequest.getJsonArray(url, reqId, iRestRequest, headers);
        } else {
            i(TAG, "No internet connection");
            ViewUtils.showToast(DastanApp.getInstance(), StringUtils.stringFromXml(DastanApp.getInstance(), R.string.no_internet));
            iRestRequest.onError(reqId, "No internet Connection");
        }
    }

    public static synchronized void sendGetObjRequest(String url, int reqId, IRestRequest iRestRequest, Map<String, String> headers) {
        if (NetworkUtils.isConnectingToInternet(DastanApp.getInstance())) {
            VolleyRequest.getJsonObj(url, reqId, iRestRequest, headers);
        } else {
            i(TAG, "No internet connection");
            ViewUtils.showToast(DastanApp.getInstance(), StringUtils.stringFromXml(DastanApp.getInstance(), R.string.no_internet));
            iRestRequest.onError(reqId, "No internet Connection");
        }
    }

    public static synchronized void sendGetRequest(String url, int reqId, JSONObject requestobj, IRestRequest iRestRequest) {
        if (NetworkUtils.isConnectingToInternet(DastanApp.getInstance())) {
            VolleyRequest.getJsonObj(url, reqId, requestobj, iRestRequest);
        } else {
            i(TAG, DastanApp.getInstance().getString(R.string.no_internet));
            ViewUtils.showToast(DastanApp.getInstance(), StringUtils.stringFromXml(DastanApp.getInstance(), R.string.no_internet));
            iRestRequest.onError(reqId, DastanApp.getInstance().getString(R.string.no_internet));
        }
    }
}
