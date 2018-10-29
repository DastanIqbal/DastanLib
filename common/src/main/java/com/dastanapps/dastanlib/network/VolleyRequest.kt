package com.dastanapps.dastanlib.network

import android.text.TextUtils
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dastanapps.dastanlib.DastanLibApp
import com.dastanapps.dastanlib.log.Logger
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * Created by Dastan Iqbal on 10/7/2015.
 */
class VolleyRequest {

    private fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        requestQueue.add(req)
    }

    private fun cancelPendingRequests(tag: Any) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

    companion object {
        private val TAG = VolleyRequest::class.java.simpleName
        private var mRequestQueue: RequestQueue? = null
        private val TIMEOUT = 60 * 1000

        fun getJsonArray(url: String, reqId: Int, requestJson: JSONArray, iRestRequest: IRestRequest/*,
                                    final Map<String, String> headers*/) {
            val jsonArrayRequest = object : JsonArrayRequest(Request.Method.GET, url, requestJson,
                    { response ->
                        Logger.d(TAG, response.toString())
                        iRestRequest.onResponse(reqId, response.toString())
                    },
                    { error ->
                        Logger.d(TAG, error.toString())
                        iRestRequest.onError(reqId, error.toString())
                    }) {

                //            @Override
                //            public Map<String, String> getHeaders() throws AuthFailureError {
                //                headers.remove("Content-Type");
                //                return headers;
                //            }
            }
            jsonArrayRequest.retryPolicy = DefaultRetryPolicy(TIMEOUT, //timeout second
                    0, //Retry
                    0f)        //Backoff multiplier
            //time=(time+(timeout*multi)
            addToRequestQueue(jsonArrayRequest, url)
        }

        fun getString(url: String, reqId: Int, requestJson: String, restReq: IRestRequest,
                      headers: Map<String, String>) {
            Logger.i(TAG, url)
            val jsonObjectRequest = object : StringRequest(Request.Method.GET, url, { response ->
                //                        Log.i("address priyam response",response);
                //                        RestResponse.parseDisplayAdd(response);
                Logger.d(TAG, response.toString())
                restReq.onResponse(reqId, response.toString())
            },
                    { error ->
                        //                        Log.i("address priyam response",error.toString());
                        Logger.d(TAG, error.toString())
                        restReq.onError(reqId, error.toString())
                    }) {

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    return headers
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    return requestJson.toByteArray()
                }

            }

            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(90 * 1000, //timeout second
                    0, //Retry don't change this value it will affect on cart page
                    0f)        //Backoff multiplier
            //time=(time+(timeout*multi)
            addToRequestQueue(jsonObjectRequest, url)
        }

        fun getString(url: String, reqId: Int, restReq: IRestRequest) {
            Logger.i(TAG, url)
            val jsonObjectRequest = StringRequest(Request.Method.GET, url, Response.Listener { response ->
                //                        Log.i("address priyam response",response);
                //                        RestResponse.parseDisplayAdd(response);
                Logger.d(TAG, response.toString())
                restReq.onResponse(reqId, response.toString())
            }, Response.ErrorListener { error ->
                //                        Log.i("address priyam response",error.toString());
                Logger.d(TAG, error.toString())
                restReq.onError(reqId, error.toString())

            })
            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(90 * 1000, //timeout second
                    0, //Retry don't change this value it will affect on cart page
                    0f)        //Backoff multiplier
            //time=(time+(timeout*multi)
            addToRequestQueue(jsonObjectRequest, url)
        }

        fun getPostJsonObject(url: String, postParams: HashMap<String, String>, reqId: Int, restReq: IRestRequest) {
            val jsonObjectRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { response -> restReq.onResponse(reqId, response.toString()) }, Response.ErrorListener { error ->
                if (error?.networkResponse != null && error.networkResponse.statusCode == 500)
                    restReq.onError(reqId, "500")
                else
                    restReq.onError(reqId, error.message!!)
            }) {

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    return headers
                }

                override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                    return super.parseNetworkResponse(response)
                }

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    return postParams
                }

            }


            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(TIMEOUT, //timeout second
                    0, //Retry
                    0f)        //Backoff multiplier
            //time=(time+(timeout*multi)
            addToRequestQueue(jsonObjectRequest, url)
        }

        fun getPostBodyJsonObject(url: String, bodyPost: String, reqId: Int, restReq: IRestRequest, headers: MutableMap<String, String>) {
            val jsonObjectRequest = object : StringRequest(Request.Method.POST, url,
                    Response.Listener { response -> restReq.onResponse(reqId, response.toString()) },
                    Response.ErrorListener { error ->
                        if (error?.networkResponse != null && error.networkResponse.statusCode == 500)
                            restReq.onError(reqId, "500")
                        else
                            restReq.onError(reqId, error.message!!)
                    }) {

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    headers.remove("Content-Type")
                    return headers
                }

                override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                    return super.parseNetworkResponse(response)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    return super.getParams()
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    return bodyPost.toByteArray()
                }
            }


            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(TIMEOUT, //timeout second
                    0, //Retry
                    0f)        //Backoff multiplier
            //time=(time+(timeout*multi)
            addToRequestQueue(jsonObjectRequest, url)
        }

        fun getPatchBodyJsonObject(url: String, bodyPost: String, reqId: Int, restReq: IRestRequest, headers: MutableMap<String, String>) {
            val jsonObjectRequest = object : StringRequest(Request.Method.PATCH, url,
                    Response.Listener { response -> restReq.onResponse(reqId, response.toString()) },
                    Response.ErrorListener { error ->
                        if (error?.networkResponse != null && error.networkResponse.statusCode == 500)
                            restReq.onError(reqId, "500")
                        else
                            restReq.onError(reqId, error?.message!!)
                    }) {

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    headers.remove("Content-Type")
                    return headers
                }

                override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                    return super.parseNetworkResponse(response)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    return super.getParams()
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    return bodyPost.toByteArray()
                }
            }


            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(TIMEOUT, //timeout second
                    0, //Retry
                    0f)        //Backoff multiplier
            //time=(time+(timeout*multi)
            addToRequestQueue(jsonObjectRequest, url)
        }

        fun getPutBodyJsonObject(url: String, bodyPost: String, reqId: Int, restReq: IRestRequest, headers: MutableMap<String, String>) {
            val jsonObjectRequest = object : StringRequest(Request.Method.PUT, url,
                    Response.Listener { response -> restReq.onResponse(reqId, response.toString()) },
                    Response.ErrorListener { error ->
                        if (error?.networkResponse != null && error.networkResponse.statusCode == 500)
                            restReq.onError(reqId, "500")
                        else
                            restReq.onError(reqId, error.message!!)
                    }) {

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    headers.remove("Content-Type")
                    return headers
                }

                override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                    return super.parseNetworkResponse(response)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    return super.getParams()
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    return bodyPost.toByteArray()
                }
            }


            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(30 * 1000, //timeout second
                    0, //Retry
                    0f)        //Backoff multiplier
            //time=(time+(timeout*multi)
            addToRequestQueue(jsonObjectRequest, url)
        }

        fun getJsonArray(url: String, reqId: Int, iRestRequest: IRestRequest, headers: MutableMap<String, String>) {
            val jsonArrayRequest = object : JsonArrayRequest(Request.Method.GET, url, "",
                    { response ->
                        Logger.d(TAG, response.toString())
                        iRestRequest.onResponse(reqId, response.toString())
                    },
                    { error ->
                        Logger.d(TAG, error.toString())
                        iRestRequest.onError(reqId, error.toString())
                    }) {

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    headers.remove("Content-Type")
                    return headers
                }
            }
            jsonArrayRequest.retryPolicy = DefaultRetryPolicy(15 * 1000, //timeout second
                    0, //Retry
                    0f)        //Backoff multiplier
            //time=(time+(timeout*multi)
            addToRequestQueue(jsonArrayRequest, url)
        }

        fun getJsonObj(url: String, reqId: Int, restReq: IRestRequest, headers: MutableMap<String, String>) {
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, "",
                    { response ->
                        Logger.d(TAG, response.toString())
                        restReq.onResponse(reqId, response.toString())
                    },
                    { error ->
                        Logger.d(TAG, error.toString())
                        restReq.onError(reqId, error.toString())
                    }) {

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    headers.remove("Content-Type")
                    return headers
                }
            }

            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(15 * 1000, //timeout second
                    0, //Retry
                    0f)        //Backoff multiplier
            //time=(time+(timeout*multi)
            addToRequestQueue(jsonObjectRequest, url)
        }

        fun getJsonObj(url: String, reqId: Int, requestJson: JSONObject, restReq: IRestRequest) {
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, requestJson,
                    { response ->
                        Logger.d(TAG, response.toString())
                        restReq.onResponse(reqId, response.toString())
                    },
                    { error ->
                        Logger.d(TAG, error.toString())
                        restReq.onError(reqId, error.toString())
                    })
            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(TIMEOUT, //timeout second
                    0, //Retry
                    0f)        //Backoff multiplier
            //time=(time+(timeout*multi)
            addToRequestQueue(jsonObjectRequest, url)
        }

        fun getJsonObj(url: String, reqId: Int, restReq: IRestRequest) {
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url,
                    { response ->
                        Logger.d(TAG, response.toString())
                        restReq.onResponse(reqId, response.toString())
                    },
                    { error ->
                        Logger.d(TAG, error.toString())
                        restReq.onError(reqId, error.toString())
                    })
            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(TIMEOUT, //timeout second
                    0, //Retry
                    0f)        //Backoff multiplier
            //time=(time+(timeout*multi)
            addToRequestQueue(jsonObjectRequest, url)
        }


        fun getJPostJsonObject(url: String, bodyPost: JSONObject, reqId: Int, restReq: IRestRequest, headers: Map<String, String>) {
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, bodyPost,
                    { response -> restReq.onResponse(reqId, response.toString()) }, { error ->
                if (error?.networkResponse != null && error.networkResponse.statusCode == 500)
                    restReq.onError(reqId, "500")
                else
                    restReq.onError(reqId, error?.message!!)
            }) {

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    return headers
                }

            }


            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(TIMEOUT, //timeout second
                    0, //Retry
                    0f)        //Backoff multiplier
            //time=(time+(timeout*multi)
            addToRequestQueue(jsonObjectRequest, url)
        }

        private fun <T> addToRequestQueue(req: Request<T>, tag: String) {
            // set the default tag if tag is empty
            req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
            requestQueue.add(req)
        }

        val requestQueue: RequestQueue = Volley.newRequestQueue(DastanLibApp.INSTANCE)
    }
}
