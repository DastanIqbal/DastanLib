package com.dastanapps.dastanlib.network;

import android.content.Context;

import com.dastanapps.dastanlib.R;
import com.dastanapps.dastanlib.utils.NetworkUtils;
import com.dastanapps.dastanlib.utils.StringUtils;
import com.dastanapps.dastanlib.utils.ViewUtils;

import java.util.HashMap;

import static android.util.Log.i;

/**
 * Created by DANISH on 10/7/2015.
 */
public class MkartRest {

    private static final String TAG = "MkartRest";

    public static synchronized void sentRequest(Context ctxt, String postUrl, HashMap<String, String> postParams, int reqId, IRestRequest iRestRequest) {
        if (NetworkUtils.isConnectingToInternet(ctxt)) {
            VolleyRequest.getPostJsonObject(postUrl, postParams, reqId, iRestRequest);
        } else {
            i(TAG, "No internet connection");
            ViewUtils.showToast(ctxt, StringUtils.stringFromXml(ctxt, R.string.no_internet));
            iRestRequest.onError("No internet Connection");
        }
    }

}
