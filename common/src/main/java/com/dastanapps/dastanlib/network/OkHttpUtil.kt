package com.dastanapps.dastanlib.network

import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Created by Dastan Iqbal on 11/19/2016.
 * author Iqbal Ahmed
 * emailId: ask2iqbal@gmail.com
 */

object OkHttpUtil {
    fun postMultipart(url: String, requestBody: RequestBody, reqId: Int, iRestRequest: IRestRequest, multiHeaders: Headers) {
        val request = Request.Builder()
                .url(url)
                .headers(multiHeaders)
                .post(requestBody)
                .build()

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout((30 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                iRestRequest.onError(reqId, e.message!!)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { iRestRequest.onResponse(reqId, it) }
            }
        })
    }

    fun updateMultipart(url: String, requestBody: RequestBody, reqId: Int, iRestRequest: IRestRequest, headers: Headers) {
        val request = Request.Builder()
                .url(url)
                .headers(headers)
                .put(requestBody)
                .build()

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout((30 * 1000).toLong(), TimeUnit.MILLISECONDS)
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                iRestRequest.onError(reqId, e.message!!)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { iRestRequest.onResponse(reqId, it) }
            }
        })
    }
}
