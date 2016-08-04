package com.dastanapps.dastanlib.Network;

import com.mebelkart.app.AppConfig;
import com.mebelkart.app.Beans.AppConfigB;
import com.mebelkart.app.BuildConfig;
import com.mebelkart.app.MkartApp;
import com.mebelkart.app.Utils.MkUtils;
import com.mebelkart.app.Utils.SPUtils;

/**
 * Created by Iqbal Ahmed on 10/7/2015.
 */
public class URLConstant {
    //This file move to ndk

    //public static final String SENDER_ID="1086970221307";
    public static final String CUSTOMER_DETAILS = "Customer_Details";
    public static final String ID_CUSTOMER = "id_customer";
    public static final String ID_EMAIL = "email";
    public static final String SECURE_KEY = "secure_key";
    public static final String CART_CACHE = "TEST4";
    public static final String Cart = "Cart";
    public static final String APPID = "1579878282261910";

    //public static final String APP_URL = AppConfig.LOCAL_URL;
    public static String APP_URL = AppConfig.BASE_URL;
    public static String BASE_PATH = AppConfig.BASE_PATH;

    public static final String GET_MENU = APP_URL + BASE_PATH + "getMenu&json=%s";
    public static final String GET_FEATURED = APP_URL + BASE_PATH + "getFeatured&json=%s";
    public static final String GET_CATEGORIES = APP_URL + BASE_PATH + "getCategories&json=%s";
    public static final String GET_DEALS = APP_URL + BASE_PATH + "getDeals&json=%s";
    public static final String GET_PROD_DETAILS = APP_URL + BASE_PATH + "getProductDetails&json=%s";
    public static final String GET_PROD_LIST = APP_URL + BASE_PATH + "getProductList&json=%s";
    public static final String MK_AUTH = APP_URL + BASE_PATH + "auth&json=%s";
    public static final String MK_CHECKOUT_AUTH = APP_URL + BASE_PATH + "checkoutAuth&json=%s";
    public static final String MK_CART = APP_URL + BASE_PATH + "updateCart&json=%s";
    public static final String MK_ADDRESS = APP_URL + BASE_PATH + "updateAddress&json=%s";
    public static final String MK_CHECK_EMAIL = APP_URL + BASE_PATH + "checkEmail&json=%s";
    public static final String MK_PAYMENT_METHOD = APP_URL + BASE_PATH + "paymentMethods&json=%s";
    public static final String MK_PAYMENT_CLICK = APP_URL + BASE_PATH + "payClick&json=%s";
    public static final String MK_PINCODE = APP_URL + BASE_PATH + "checkPincode&json=%s";
    public static final String MK_DISCOUNT = APP_URL + BASE_PATH + "applyDiscount&json=%s";
    public static final String MK_IDEABOARD = APP_URL + BASE_PATH + "updateIdeaboard&json=%s";
    public static final String MK_SEARCH = APP_URL + BASE_PATH + "getSearchResult&json=%s";
    public static final String MK_MYORDERS = APP_URL + BASE_PATH + "getMyOrders&json=%s";
    public static final String MK_NOTIFYEMAIL = APP_URL + BASE_PATH + "notifyEmail&json=%s";
    public static final String MK_MYORDEER_DETAILS = APP_URL + BASE_PATH + "OrderDetails&json=%s";
    public static final String MK_CHANGE_PASSWORD = APP_URL + BASE_PATH + "changePassword&json=%s";
    public static final String MK_FORGET_PASSWORD = APP_URL + BASE_PATH + "forgetPassword&json=%s";

    public static final String MK_UPDATEPERSONALINFO = APP_URL + BASE_PATH + "updateProfileInfo&json=%s";
    public static final String MK_DEACTIVATEACCOUNT = APP_URL + BASE_PATH + "deactivateAccount&json=%s";
    public static final String MODULAR_KITCHEN_FORM = APP_URL + BASE_PATH + "modularKitchenForm&json=%s";
    //public static final String CONTACT_US = APP_URL+"contact-us.php";
    public static final String MK_PUSH = APP_URL + "push/addPush.php?req=%s&json=%s";


    //Deep Linking
    public static final String MK_DL_CATEGORY = "android-app://"+ BuildConfig.APPLICATION_ID+"/http/mebelkart.com/category/";
    public static final String MK_DL_PRODUCT = "android-app://"+ BuildConfig.APPLICATION_ID+"/http/mebelkart.com/product/";

    public static void init(AppConfigB appConfigB) {
        APP_URL = appConfigB.getBASE_URL();
        BASE_PATH = appConfigB.getBASE_PATH();
        SPUtils.writeAppURL(MkUtils.encrypt(MkartApp.getInstance(), APP_URL + BASE_PATH));
    }
}
