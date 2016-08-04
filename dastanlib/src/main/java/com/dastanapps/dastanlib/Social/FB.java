package com.dastanapps.dastanlib.Social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mebelkart.app.Log.Logger;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * *@author : Dastan Iqbal
 *
 * @email : iqbal.ahmed@mebelkart.com
 */
public class FB {
    private final Activity activity;
    private Context ctxt;
    private CallbackManager callbackManager;
    private ISocialResponse iSocialResponse;

    public FB(Object ctxt) {
        this.ctxt = (Context) ctxt;
        this.activity = (Activity) ctxt;
        this.iSocialResponse = (ISocialResponse) ctxt;

        FacebookSdk.sdkInitialize(this.ctxt);
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, fbCallback);
        checkKeyHash();
    }

    public void signIn() {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "user_friends", "email"));
    }

    public void signOut() {
        LoginManager.getInstance().logOut();
        disconnectFromFacebook();
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                LoginManager.getInstance().logOut();
            }
        }).executeAsync();
    }

//    private void callFacebookLogout() {
//            Session session = Session.getActiveSession();
//            if (session != null) {
//
//                if (!session.isClosed()) {
//                    session.closeAndClearTokenInformation();
//                    //clear your preferences if saved
//                }
//            } else {
//
//                session = new Session(ctxt);
//                Session.setActiveSession(session);
//
//                session.closeAndClearTokenInformation();
//                //clear your preferences if saved
//
//        }
//    }

    public FacebookCallback<LoginResult> fbCallback = new FacebookCallback<LoginResult>() {

        private ProfileTracker mProfileTracker;

        @Override
        public void onSuccess(LoginResult loginResult) {
            if (Profile.getCurrentProfile() == null) {
                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        Logger.v("facebook - profile", profile2.getFirstName());
                        mProfileTracker.stopTracking();
                    }
                };
                mProfileTracker.startTracking();
            }
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            if (object == null) {
                                iSocialResponse.error("Something went wrong.Please try again!");
                                return;
                            }
                            String email = object.optString("email");
                            String uid = object.optString("id");
                            String gender = object.optString("gender");
                            Profile profile = Profile.getCurrentProfile();
                            Bundle b = new Bundle();
                            b.putString(ISocialResponse.PROVIDER, "FB");
                            b.putString(ISocialResponse.ID, uid);
                            b.putString(ISocialResponse.EMAIL_ID, email);
                            b.putString(ISocialResponse.ACCESS_TOKEN, AccessToken.getCurrentAccessToken().getToken());
                            if (profile != null) {
                                b.putString(ISocialResponse.NAME, profile.getName());
                                b.putString(ISocialResponse.FIRST_NAME, profile.getFirstName());
                                b.putString(ISocialResponse.LAST_NAME, profile.getLastName());
                                b.putString(ISocialResponse.LINK, profile.getLinkUri().toString());
                            }
                            b.putString(ISocialResponse.GENDER, gender);
                            b.putString(ISocialResponse.PROFILE_PIC, "https://graph.facebook.com/" + uid + "/picture");
                            iSocialResponse.socialResponse(b);

                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,email,name,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            iSocialResponse.error("Cancelled");
        }

        @Override
        public void onError(FacebookException exception) {
            iSocialResponse.error(exception.getMessage());
        }
    };


    public void checkKeyHash() {
        try {
            PackageInfo info = ctxt.getPackageManager().getPackageInfo(
                    "com.facebook.samples.hellofacebook",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        ;
    }
}
