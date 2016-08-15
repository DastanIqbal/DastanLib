package com.dastanapps.dastanlib.network;

/**
 * Created by Iqbal Ahmed on 10/13/2015.
 */
public interface IRestRequest {
    void onResponse(int reqID, String resp);
    void onError(String error);
}
