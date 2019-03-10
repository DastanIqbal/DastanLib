package com.dastanapps.dastanlib.network

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.UnsupportedEncodingException

class GsonRequest<T>
/**
 * Make a GET request and return a parsed object from JSON.
 *
 * @param url URL of the request to make
 * @param clazz Relevant class object, for Gson's reflection
 * @param headers Map of request headers
 */
(url: String, private val clazz: Class<T>, private val headers: Map<String, String>?,
 private val listener: Response.Listener<T>, errorListener: Response.ErrorListener) : Request<T>(Request.Method.GET, url, errorListener) {
    private val gson = Gson()

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        return headers ?: super.getHeaders()
    }

    override fun deliverResponse(response: T) {
        listener.onResponse(response)
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<T> {
        return try {
            val json = String(
                    response.data,
                    charset(HttpHeaderParser.parseCharset(response.headers)))
            Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }

    }
}