package com.dastanapps.dastanlib.social;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.dastanapps.dastanlib.log.Logger;
import com.dastanapps.dastanlib.utils.ViewUtils;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;

/**
 * Created by IQBAL-MEBELKART on 10/31/2015.
 */
public class G implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 1;

    /* RequestCode for resolutions to get GET_ACCOUNTS permission on M */
    private static final int RC_PERM_GET_ACCOUNTS = 2;

    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";

    private static final String TAG = G.class.getSimpleName();
    GoogleApiClient mGoogleApiClient;
    private Context ctxt;
    private ISocialResponse iSocialResponse;

    // [START resolution_variables]
    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;
    private Activity actvity;

    public G(Object ref) {
        this.ctxt = (Context) ref;
        this.actvity = (Activity) ref;
        this.iSocialResponse = (ISocialResponse) ref;
    }

    public void init() {

        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(ctxt)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .build();
    }

    public void savedInstances(Bundle savedInstanceState) {
        // Restore from saved instance state
        // [START restore_saved_instance_state]
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }
        // [END restore_saved_instance_state]

    }

    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Logger.d(TAG, "onConnected:" + bundle);
       /* if (mShouldResolve)*/
        // Show the signed-in UI
        updateUI(true);

        mShouldResolve = false;

    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost. The GoogleApiClient will automatically
        // attempt to re-connect. Any UI elements that depend on connection to Google APIs should
        // be hidden or disabled until onConnected is called again.
        Logger.w(TAG, "onConnectionSuspended:" + i);
        iSocialResponse.stopLoading();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Logger.d(TAG, "onConnectionFailed:" + connectionResult);

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                iSocialResponse.stopLoading();
                try {
                    connectionResult.startResolutionForResult(actvity, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Logger.e(TAG, "Could not resolve ConnectionResult.");
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                iSocialResponse.stopLoading();
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
            updateUI(false);
        }
    }

    public void onStart() {
        mGoogleApiClient.connect();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
    }

    public void signInG() {
        mShouldResolve = true;
        mGoogleApiClient.connect();
        Logger.i(TAG, "Signing In");
    }

    public void signOutG() {
        // Clear the default account so that GoogleApiClient will not automatically
        // connect in the future.
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }

       /* if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.GET_ACCOUNTS},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }*/
        updateUI(false);
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            if (currentPerson != null) {
                // Show users' email address (which requires GET_ACCOUNTS permission)
                checkAccountsPermission();

            } else {
                // If getCurrentPerson returns null there is generally some error with the
                // configuration of the application (invalid Client ID, Plus API not enabled, etc).
                Logger.w(TAG, "Null Person");
                iSocialResponse.stopLoading();
            }
        } else {
            // Show signed-out message and clear email field
            Logger.w(TAG, "Signed Out");
            iSocialResponse.stopLoading();
        }
    }

    /**
     * Revoking access from google
     */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                        }

                    });
        }
    }

    /**
     * Check if we have the GET_ACCOUNTS permission and request it if we do not.
     *
     * @return true if we have the permission, false if we do not.
     */
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private void checkAccountsPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasWriteContactsPermission = actvity.checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
               /* if (!actvity.shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {
                    ViewUtils.getMKDialogOK(ctxt, "Permission", "You need to allow access to GPlus Login", new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            actvity.requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS},
                                    REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    return;
                }*/
                actvity.requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            } else {
                requestToken();
            }
        } else {
            requestToken();
        }
    }

    private void showErrorDialog(ConnectionResult connectionResult) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(ctxt);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(actvity, resultCode, RC_SIGN_IN,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                mShouldResolve = false;
                                updateUI(false);
                            }
                        }).show();
            } else {
                Logger.w(TAG, "Google Play Services Error:" + connectionResult);
                String errorString = apiAvailability.getErrorString(resultCode);
                Toast.makeText(ctxt, errorString, Toast.LENGTH_SHORT).show();

                mShouldResolve = false;
                updateUI(false);
            }
        }
    }

    public void disConnect() {
        // Revoke all granted permissions and clear the default account.  The user will have
        // to pass the consent screen to sign in again.
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }

        updateUI(false);
    }

    public void onSaveInstance(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }

    public String getGender(int gender) {
        switch (gender) {
            case Person.Gender.MALE:
                return "male";
            case Person.Gender.FEMALE:
                return "female";
            default:
                return "other";
        }
    }

    public void requestToken() {
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        //show signied user name
        String uid = currentPerson.getId();
        String name = currentPerson.getDisplayName();
        Logger.i(TAG, name);

        final Bundle b = new Bundle();
        b.putString(ISocialResponse.PROVIDER, "G");
        b.putString(ISocialResponse.ID, uid);
        b.putString(ISocialResponse.NAME, name);
        String currentAccount = Plus.AccountApi.getAccountName(mGoogleApiClient);
        b.putString(ISocialResponse.EMAIL_ID, currentAccount);
        b.putString(ISocialResponse.PROFILE_PIC, currentPerson.getImage().getUrl());
        b.putString(ISocialResponse.FIRST_NAME, currentPerson.getName().getGivenName());
        b.putString(ISocialResponse.LAST_NAME, currentPerson.getName().getFamilyName());
        b.putString(ISocialResponse.LINK, currentPerson.getUrl());
        b.putString(ISocialResponse.GENDER, getGender(currentPerson.getGender()));

        Logger.i(TAG, currentAccount);

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token = null;
                final String SCOPES = "https://www.googleapis.com/auth/plus.login ";
                try {
                    token = GoogleAuthUtil.getToken(
                            ctxt,
                            Plus.AccountApi.getAccountName(mGoogleApiClient),
                            "oauth2:" + SCOPES);
                    if (token == null) {
                        revokeGplusAccess();
                        signOutG();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }
                return token;

            }

            @Override
            protected void onPostExecute(String token) {
                Log.i(TAG, "Access token retrieved:" + token);
                b.putString(ISocialResponse.ACCESS_TOKEN, token);
                iSocialResponse.socialResponse(b);
            }
        };
        task.execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != actvity.RESULT_OK) {
                mShouldResolve = false;
            }
            ViewUtils.showProgressDialog(ctxt);
            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }
}

