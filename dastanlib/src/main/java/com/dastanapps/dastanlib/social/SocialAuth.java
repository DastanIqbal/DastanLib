package com.dastanapps.dastanlib.social;//package com.mebelkart.app.Social;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import com.mebelkart.app.Log.Logger;
//import com.mebelkart.app.R;
//
//import org.brickred.socialauth.android.DialogListener;
//import org.brickred.socialauth.android.SocialAuthAdapter;
//import org.brickred.socialauth.android.SocialAuthError;
//
///**
// * Created by IQBAL-MEBELKART on 10/31/2015.
// */
//public class SocialAuth {
//
//    public final static String TAG=SocialAuth.class.getSimpleName();
//    public ISocialResponse iSocialResponse;
//    private SocialAuthAdapter adapter;
//    private Context ctxt;
//    /**
//     * Listens Response from Library
//     *
//     */
//
//    private final class ResponseListener implements DialogListener {
//        @Override
//        public void onComplete(Bundle values) {
//            // Variable to receive message status
//            Logger.d(TAG, "Authentication Successful");
//
//            // Get name of provider after authentication
//            final String providerName = values.getString(SocialAuthAdapter.PROVIDER);
//            final String accessGrant=values.getString(SocialAuthAdapter.ACCESS_GRANT);
//
//            Logger.d(TAG, "Provider Name = " + providerName);
//            Bundle bundle=new Bundle();
//            bundle.putString(ISocialResponse.PROVIDER,providerName);
//            bundle.putString(ISocialResponse.ACCESS_GRANT,accessGrant);
//            iSocialResponse.socialResponse(bundle);
//        }
//
//        @Override
//        public void onError(SocialAuthError error) {
//            error.printStackTrace();
//            iSocialResponse.error(error.getMessage());
//            Logger.d(TAG, error.getMessage());
//        }
//
//        @Override
//        public void onCancel() {
//            Logger.d(TAG, "Authentication Cancelled");
//            iSocialResponse.error("Authentication Cancelled");
//        }
//
//        @Override
//        public void onBack() {
//            Logger.d(TAG, "Dialog Closed by pressing Back Key");
//
//        }
//    }
//
//    public SocialAuth(Context ctxt,ISocialResponse iSocialResponse){
//        this.ctxt=ctxt;
//        this.iSocialResponse=iSocialResponse;
//        adapter = new SocialAuthAdapter(new ResponseListener());
//        adapter.addProvider(SocialAuthAdapter.Provider.FACEBOOK, R.drawable.facebook);
//    }
//
//    public void enableFacebook(){
//        adapter.authorize(ctxt, SocialAuthAdapter.Provider.FACEBOOK);
//    }
//}