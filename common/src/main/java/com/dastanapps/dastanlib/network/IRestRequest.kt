package com.dastanapps.dastanlib.network

/**
 * Created by Iqbal Ahmed on 10/13/2015.
 */
interface IRestRequest {
    fun onResponse(reqID: Int, resp: String)
    fun onError(reqId: Int, error: String)
}
