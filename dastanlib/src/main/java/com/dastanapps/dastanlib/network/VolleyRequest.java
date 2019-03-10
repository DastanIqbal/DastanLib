package com.dastanapps.dastanlib.network;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dastanapps.dastanlib.DastanApp;
import com.dastanapps.dastanlib.log.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dastan Iqbal on 10/7/2015.
 */
public class VolleyRequest {
    private static final String TAG = VolleyRequest.class.getSimpleName();
    private static RequestQueue mRequestQueue;
    private static int TIMEOUT = 60 * 1000;

    public static void getJsonArray(String url, final int reqId, JSONArray requestJson, final IRestRequest iRestRequest/*,
                                    final Map<String, String> headers*/) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, requestJson,
                response -> {
                    Logger.d(TAG, response.toString());
                    iRestRequest.onResponse(reqId, response.toString());
                },
                error -> {
                    Logger.d(TAG, error.toString());
                    iRestRequest.onError(reqId, error.toString());
                }) {

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                headers.remove("Content-Type");
//                return headers;
//            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, //timeout second
                0,          //Retry
                0));        //Backoff multiplier
        //time=(time+(timeout*multi)
        addToRequestQueue(jsonArrayRequest, url);
    }

    public static void getString(String url, final int reqId, final String requestJson, final IRestRequest restReq,
                                 final Map<String, String> headers) {
        Logger.i(TAG, url);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url, response -> {
//                        Log.i("address priyam response",response);
//                        RestResponse.parseDisplayAdd(response);
            Logger.d(TAG, response.toString());
            restReq.onResponse(reqId, response.toString());
        },
                error -> {
//                        Log.i("address priyam response",error.toString());
                    Logger.d(TAG, error.toString());
                    restReq.onError(reqId, error.toString());
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return requestJson.getBytes();
            }

        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(90 * 1000, //timeout second
                0,          //Retry don't change this value it will affect on cart page
                0));        //Backoff multiplier
        //time=(time+(timeout*multi)
        addToRequestQueue(jsonObjectRequest, url);
    }

    public static void getString(String url, final int reqId, final IRestRequest restReq) {
        Logger.i(TAG, url);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                        Log.i("address priyam response",response);
//                        RestResponse.parseDisplayAdd(response);
                Logger.d(TAG, response.toString());
                restReq.onResponse(reqId, response.toString());
            }
        },
                error -> {
//                        Log.i("address priyam response",error.toString());
                    Logger.d(TAG, error.toString());
                    restReq.onError(reqId, error.toString());
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(90 * 1000, //timeout second
                0,          //Retry don't change this value it will affect on cart page
                0));        //Backoff multiplier
        //time=(time+(timeout*multi)
        addToRequestQueue(jsonObjectRequest, url);
    }

    public static void getPostJsonObject(String url, final HashMap<String, String> postParams, final int reqId, final IRestRequest restReq) {
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                restReq.onResponse(reqId, response.toString());
            }

        }, error -> {
            if (error != null && error.networkResponse != null && error.networkResponse.statusCode == 500)
                restReq.onError(reqId, "500");
            else
                restReq.onError(reqId, error.getMessage());
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return postParams;
            }

        };


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, //timeout second
                0,          //Retry
                0));        //Backoff multiplier
        //time=(time+(timeout*multi)
        addToRequestQueue(jsonObjectRequest, url);
    }

    public static void getPostBodyJsonObject(String url, final String bodyPost, final int reqId, final IRestRequest restReq, final Map<String, String> headers) {
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                restReq.onResponse(reqId, response.toString());
            }

        }, error -> {
            if (error != null && error.networkResponse != null && error.networkResponse.statusCode == 500)
                restReq.onError(reqId, "500");
            else
                restReq.onError(reqId, error.getMessage());
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.remove("Content-Type");
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return bodyPost.getBytes();
            }
        };


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, //timeout second
                0,          //Retry
                0));        //Backoff multiplier
        //time=(time+(timeout*multi)
        addToRequestQueue(jsonObjectRequest, url);
    }

    public static void getPatchBodyJsonObject(String url, final String bodyPost, final int reqId, final IRestRequest restReq, final Map<String, String> headers) {
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.PATCH, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                restReq.onResponse(reqId, response.toString());
            }

        }, error -> {
            if (error != null && error.networkResponse != null && error.networkResponse.statusCode == 500)
                restReq.onError(reqId, "500");
            else
                restReq.onError(reqId, error.getMessage());
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.remove("Content-Type");
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return bodyPost.getBytes();
            }
        };


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, //timeout second
                0,          //Retry
                0));        //Backoff multiplier
        //time=(time+(timeout*multi)
        addToRequestQueue(jsonObjectRequest, url);
    }

    public static void getPutBodyJsonObject(String url, final String bodyPost, final int reqId, final IRestRequest restReq, final Map<String, String> headers) {
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                restReq.onResponse(reqId, response.toString());
            }

        }, error -> {
            if (error != null && error.networkResponse != null && error.networkResponse.statusCode == 500)
                restReq.onError(reqId, "500");
            else
                restReq.onError(reqId, error.getMessage());
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.remove("Content-Type");
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return bodyPost.getBytes();
            }
        };


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, //timeout second
                0,          //Retry
                0));        //Backoff multiplier
        //time=(time+(timeout*multi)
        addToRequestQueue(jsonObjectRequest, url);
    }

    public static void getJsonArray(String url, final int reqId, final IRestRequest iRestRequest, final Map<String, String> headers) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, "",
                response -> {
                    Logger.d(TAG, response.toString());
                    iRestRequest.onResponse(reqId, response.toString());
                },
                error -> {
                    Logger.d(TAG, error.toString());
                    iRestRequest.onError(reqId, error.toString());
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.remove("Content-Type");
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, //timeout second
                0,          //Retry
                0));        //Backoff multiplier
        //time=(time+(timeout*multi)
        addToRequestQueue(jsonArrayRequest, url);
    }

    public static void getJsonObj(String url, final int reqId, final IRestRequest restReq, final Map<String, String> headers) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, "",
                response -> {
                    Logger.d(TAG, response.toString());
                    restReq.onResponse(reqId, response.toString());
                },
                error -> {
                    Logger.d(TAG, error.toString());
                    restReq.onError(reqId, error.toString());
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.remove("Content-Type");
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15 * 1000, //timeout second
                0,          //Retry
                0));        //Backoff multiplier
        //time=(time+(timeout*multi)
        addToRequestQueue(jsonObjectRequest, url);
    }

    public static void getJsonObj(String url, final int reqId, JSONObject requestJson, final IRestRequest restReq) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, requestJson,
                response -> {
                    Logger.d(TAG, response.toString());
                    restReq.onResponse(reqId, response.toString());
                },
                error -> {
                    Logger.d(TAG, error.toString());
                    restReq.onError(reqId, error.toString());
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, //timeout second
                0,          //Retry
                0));        //Backoff multiplier
        //time=(time+(timeout*multi)
        addToRequestQueue(jsonObjectRequest, url);
    }

    public static void getJsonObj(String url, final int reqId, final IRestRequest restReq) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                response -> {
                    Logger.d(TAG, response.toString());
                    restReq.onResponse(reqId, response.toString());
                },
                error -> {
                    Logger.d(TAG, error.toString());
                    restReq.onError(reqId, error.toString());
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, //timeout second
                0,          //Retry
                0));        //Backoff multiplier
        //time=(time+(timeout*multi)
        addToRequestQueue(jsonObjectRequest, url);
    }


    public static void getJPostJsonObject(String url, final JSONObject bodyPost, final int reqId, final IRestRequest restReq, final Map<String, String> headers) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, bodyPost,
                response -> restReq.onResponse(reqId, response.toString()), error -> {
                    if (error != null && error.networkResponse != null && error.networkResponse.statusCode == 500)
                        restReq.onError(reqId, "500");
                    else
                        restReq.onError(reqId, error.getMessage());
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }

        };


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT, //timeout second
                0,          //Retry
                0));        //Backoff multiplier
        //time=(time+(timeout*multi)
        addToRequestQueue(jsonObjectRequest, url);
    }

    private static <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    private <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(DastanApp.getInstance());
        }

        return mRequestQueue;
    }

    private void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
