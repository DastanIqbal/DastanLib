package com.dastanapps.dastanlib.Network;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.URLUtil;
import com.dastanapps.dastanlib.R;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static android.util.Log.i;

/**
 * Created by DANISH on 10/7/2015.
 */
public class MkartRest {

    private static final String TAG = "MkartRest";
    public static final int APP_CONFIG = 1000;
    public static final int NAV_MENU = 1001;
    public static final int FEATURED = 1002;
    public static final int CATEGORIES = 1003;
    public static final int DEALS = 1004;
    public static final int PROD_DETAILS = 1005;
    public static final int PROD_LIST = 1006;
    public static final int MK_AUTH = 1007;
    public static final int MK_ADDRESS = 1008;
    public static final int MK_CART = 1009;
    public static final int MK_CART_DELETE = 1010;
    public static final int MK_CART_UPDATE = 1011;
    public static final int MK_ADDRESS_UPDATE = 1012;
    public static final int MK_ADDRESS_DELETE = 1013;

    public static final int MK_CHECKOUT_LOGIN = 1014;
    public static final int MK_CHECKOUT_PAYMENT = 1016;
    public static final int MK_CHECKOUT_PAYCLICK = 1017;
    public static final int MK_CHECKOUT_AUTH = 1020;

    public static final int MK_CHECK_EMAIL = 1019;
    public static final int MK_CHECK_PINCODE = 1018;
    public static final int MK_APPLY_DISCOUNT = 1021;
    public static final int MK_DELETE_DISCOUNT = 1022;

    public static final int MK_IDEABOARD_NEW = 1023;
    public static final int MK_IDEABOARD_ADD = 1024;
    public static final int MK_IDEABOARD_UPDATE = 1025;
    public static final int MK_IDEABOARD_REMOVE = 1026;
    public static final int MK_IDEABOARD_GET = 1027;
    public static final int MK_IDEABOARD_GETPROD = 1028;
    public static final int MK_IDEABOARD_DELETE = 1029;
    public static final int MK_SEARCH = 1030;
    public static final int MK_MYORDERS = 1031;
    public static final int MK_NOTIFYEMAIL = 1032;
    public static final int MK_CHANGEADDRESS = 1034;
    public static final int MK_FORGETPASSWORD = 1035;
    public static final int MK_UPDATEPERSONALINFO = 1036;
    public static final int MK_DEACTIVATE = 1037;
    public static final int MK_CHANGE_PASSWORD = 1038;
    public static final int MYORDERDETAILS = 1039;
    public static final int MK_MODULAR_KITCHEN_FORM = 1040;
    public static final int MK_PUSH = 1041;

    static Context mContext = MkartApp.getInstance();


    public static synchronized void sentRequest(String url, int reqId, IRestRequest iRestRequest) {
        if (NetworkUtils.isConnectingToInternet(MkartApp.getInstance())) {
            if (url.startsWith("null")) {
                Context context = MkartApp.getInstance();
                url = url.replace("null", "");
//                context.startActivity(new Intent(context , SplashA.class));
                String encrypted=SPUtils.readAppURL();
                if(!TextUtils.isEmpty(encrypted)){
                    String urlReq= MkUtils.decrypt(MkartApp.getInstance(),encrypted);
                    if(!TextUtils.isEmpty(urlReq)) {
                        PostURL postURL = new PostURL(urlReq+url);
                        VolleyRequest.getPostJsonObject(postURL.getPostURL(), postURL.getPostParams(), reqId, iRestRequest);
                    }else{
                        storeAppConfigResponse(url, reqId, iRestRequest);
                    }
                    i(TAG, urlReq+"");
                }else {
                    storeAppConfigResponse(url, reqId, iRestRequest);
                }
            } else {
                PostURL postURL = new PostURL(url);
                VolleyRequest.getPostJsonObject(postURL.getPostURL(), postURL.getPostParams(), reqId, iRestRequest);
                i(TAG, url);
            }
            // VolleyRequest.getPostJsonObject(postURL.getPostURL(), postURL.getPostParams(), reqId, iRestRequest);
        } else {
            i(TAG, "No internet connection");
            showToast(MkartApp.getInstance(), stringFromXml(MkartApp.getInstance(), R.string.no_internet));
            iRestRequest.onError("No internet Connection");
//            ViewUtils.hideProgressDialog();
        }
    }

    public static void storeAppConfigResponse(final String url, int reqId, final IRestRequest iRestRequest) {
        String appConfig = SPUtils.readAppConfig();
        if (!TextUtils.isEmpty(appConfig)) {
            writeToAppConfig(appConfig, url, reqId, iRestRequest);
            Logger.d(TAG, "appConfig is already stored in SP");
        } else {
            String mkAppConfigUrl = RestRequest.appConfigURL(mContext);
            if (!TextUtils.isEmpty(mkAppConfigUrl)) {
                MkartRest.appConfigRequest(mkAppConfigUrl, MkartRest.APP_CONFIG, new IRestRequest() {
                    @Override
                    public void onResponse(int reqID, String resp) {
                        BaseB baseB = checkStatus(resp);
                        if (baseB != null) {
                            if (baseB.getStatus().equals("200")) {
                                try {
                                    JSONObject jsonObject = new JSONObject(resp);
                                    String data = jsonObject.getString("data");
                                    SPUtils.writeAppConfig(data);
                                    writeToAppConfig(data, null, 0, iRestRequest);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                i(TAG, baseB.getMsg());
                                showToast(MkartApp.getInstance(), baseB.getMsg());
                            }
                        } else {
                            showToast(mContext, stringFromXml(mContext, invalid_response));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        //showToast(mContext, stringFromXml(mContext, invalid_response));
                        iRestRequest.onError(mContext.getResources().getString(R.string.invalid_response));
                    }
                });
            } else {
                //showToast(MkartApp.getInstance(), MkartApp.getInstance().getResources().getString(invalid_response));
                iRestRequest.onError(mContext.getResources().getString(R.string.invalid_response));
            }
        }
    }

    private static void writeToAppConfig(String encrypted, String url, int reqId, IRestRequest iRestRequest) {
        Boolean isSuccess = MkUtils.initializeAppConfig(MkartApp.getInstance(), encrypted);
        if (isSuccess == null) {
            SPUtils.writeAppConfig("");
            storeAppConfigResponse(url, reqId, iRestRequest);
            return;
        }
        if (isSuccess) {
            if (!TextUtils.isEmpty(url) && reqId != 0) {
                sentRequest(AppConfig.BASE_URL + AppConfig.BASE_PATH + url, reqId, iRestRequest);
            }
        } else {
            //showToast(mContext, mContext.getResources().getString(R.string.invalid_response));
            iRestRequest.onResponse(reqId, mContext.getResources().getString(R.string.invalid_response));
        }
    }


    public static void appConfigRequest(String url, int reqId, IRestRequest iRestRequest) {
        if (NetworkUtils.isConnectingToInternet(MkartApp.getInstance())) {
            VolleyRequest.getString(url, reqId, iRestRequest);
            i(TAG, url);
        } else {
            i(TAG, "No internet connection");
            showToast(MkartApp.getInstance(), stringFromXml(MkartApp.getInstance(), R.string.no_internet));
            iRestRequest.onError("No internet Connection");
        }
    }

    public static String filterUrl(String url) {
        return url.replaceAll("[{]", "%7B").replaceAll("[}]", "%7D");
    }

    public static class PostURL {
        private String postURL;
        private HashMap<String, String> postParams;

        public PostURL(String url) {
            if (URLUtil.isValidUrl(url)) {
                String splitArray[] = url.split(Pattern.quote("?req="));
                if (splitArray.length == 2) {
                    postURL = splitArray[0];
//                    String getjson[] = splitArray[1].split("[&]");
                    String getjson[] = splitArray[1].split("&json=");
                    if (getjson.length == 2) {
                        postParams = new HashMap<>();
                        postParams.put("req", getjson[0]);
//                        postParams.put("json", getjson[1].split(Pattern.quote("json="))[1]);
                        postParams.put("json", getjson[1]);
                    } else {
                        i(TAG, url);
                        showToast(MkartApp.getInstance(), "Something went Wrong");
                    }
                } else {
                    i(TAG, url);
                    showToast(MkartApp.getInstance(), "Something went Wrong");
                }

                /*Uri uri = Uri.parse(url);
                String protocol = uri.getScheme();
                String server = uri.getAuthority();
                String path = uri.getPath();
                postURL = protocol + "://" + server + path;
                postParams = new HashMap<>();
                String reqValue = uri.getQueryParameter("req");
                String json = uri.getQueryParameter("json");
                postParams.put("req", reqValue);
                postParams.put("json", json);*/
            }
        }

        public String getPostURL() {
            return postURL;
        }

        public HashMap<String, String> getPostParams() {
            return postParams;
        }

        private String urlEncodeUTF8(String s) {
            try {
                return URLEncoder.encode(s, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new UnsupportedOperationException(e);
            }
        }

        public String getPostURLParams() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<?, ?> entry : postParams.entrySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(String.format("%s=%s",
                        urlEncodeUTF8(entry.getKey().toString()),
                        urlEncodeUTF8(entry.getValue().toString())
                ));
            }
            return sb.toString();
        }

    }
}
