package com.dastanapps.dastanlib.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Dastan Iqbal on 11/19/2016.
 * author Iqbal Ahmed
 * emailId: ask2iqbal@gmail.com
 */

public class OkHttpUtil {
    public static void postMultipart(String url, RequestBody requestBody, final int reqId, final IRestRequest iRestRequest, Headers multiHeaders) {
        Request request = new Request.Builder()
                .url(url)
                .headers(multiHeaders)
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                iRestRequest.onError(reqId, e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                iRestRequest.onResponse(reqId, response.body().string());
            }
        });
    }

    public static void updateMultipart(String url, RequestBody requestBody, final int reqId, final IRestRequest iRestRequest, Headers headers) {
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .put(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                iRestRequest.onError(reqId, e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                iRestRequest.onResponse(reqId, response.body().string());
            }
        });
    }
}
