package com.dastanapps.dastanlib.analytics;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by IQBAL-MEBELKART on 4/18/2016.
 */
public class DAppIndexing {
    private static final String TAG = DAppIndexing.class.getSimpleName();
    private GoogleApiClient mClient;
    private Uri mAppUrl;
    private String mTitle;
    private String mDescription;

    public DAppIndexing(@NonNull Context ctxt,@NonNull String mAppUrl, @NonNull String mTitle, @NonNull String mDescription) {
        init(ctxt,mAppUrl,mTitle, mDescription);
    }

    private void init(Context ctxt,String mAppUrl, String mTitle, String mDescription) {
        checkTrackerNull(ctxt);
        this.mAppUrl= Uri.parse(mAppUrl);
        this.mTitle = mTitle.toUpperCase();
        this.mDescription = mDescription.toUpperCase();
    }

    public DAppIndexing() {

    }

    public DAppIndexing getInstance(Context ctxt,@NonNull String mAppUrl, @NonNull String mTitle, @NonNull String mDescription) {
        init(ctxt,mAppUrl,mTitle, mDescription);
        return this;
    }

    private void checkTrackerNull(Context ctxt) {
        if (mClient == null) {
            mClient = new GoogleApiClient.Builder(ctxt).addApi(AppIndex.API).build();
        }
    }

    /**
     * To Start App Indexing
     */
    public void onStart() {
        mClient.connect();
        PendingResult<Status> result = AppIndex.AppIndexApi.start(mClient, getAction());
        result.setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if (status.isSuccess()) {
                    Log.d(TAG, "App Indexing API: Recorded  "
                            + /*recipe.getTitle() +*/ " view successfully.");
                } else {
                    Log.e(TAG, "App Indexing API: There was an error recording the recipe view."
                            + status.toString());
                }
            }
        });
    }

    /**
     * To Stop App Indexing
     * Call this method before super.onStart();
     */
    public void onStop() {
        PendingResult<Status> result = AppIndex.AppIndexApi.end(mClient, getAction());
        result.setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if (status.isSuccess()) {
                    Log.d(TAG, "App Indexing API: Recorded recipe "
                            + /*recipe.getTitle() +*/ " view end successfully.");
                } else {
                    Log.e(TAG, "App Indexing API: There was an error recording the recipe view."
                            + status.toString());
                }
            }
        });
        mClient.disconnect();
    }

    private Action getAction() {
       final Uri mUrl = mAppUrl.buildUpon().appendEncodedPath(mTitle).build();
        Thing object = new Thing.Builder()
                .setName(mTitle)
                .setDescription(mDescription)
                .setUrl(mUrl)
                .build();

        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}
