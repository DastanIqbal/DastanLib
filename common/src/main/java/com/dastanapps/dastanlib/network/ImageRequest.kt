package com.dastanapps.dastanlib.network

import android.content.Context
import android.graphics.Bitmap
import android.support.v4.util.LruCache
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.ImageLoader

/**
 * Created by Belal on 10/8/2015.
 */

class ImageRequest private constructor(private val context: Context) {
    private var requestQueue: RequestQueue? = null
    val imageLoader: ImageLoader


    init {
        this.requestQueue = getRequestQueue()

        imageLoader = ImageLoader(requestQueue,
                object : ImageLoader.ImageCache {
                    private val cache = LruCache<String, Bitmap>(200)

                    override fun getBitmap(url: String): Bitmap? {
                        return cache.get(url)
                    }

                    override fun putBitmap(url: String, bitmap: Bitmap) {
                        cache.put(url, bitmap)
                    }
                })
    }

    fun getRequestQueue(): RequestQueue {
        if (requestQueue == null) {
            val cache = DiskBasedCache(context.cacheDir, 10 * 1024 * 1024)
            val network = BasicNetwork(HurlStack())
            requestQueue = RequestQueue(cache, network)
            requestQueue!!.start()
        }
        return requestQueue as RequestQueue
    }

    companion object {

        private var customVolleyRequest: ImageRequest? = null

        @Synchronized
        fun getInstance(context: Context): ImageRequest {
            if (customVolleyRequest == null) {
                customVolleyRequest = ImageRequest(context)
            }
            return customVolleyRequest as ImageRequest
        }
    }

}