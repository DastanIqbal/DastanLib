package com.dastanapps.dastanlib.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mebelkart.app.MkartApp;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by Iqbal Ahmed on 10/10/2015.
 */
public class SPUtils {
    private static final int MAX_ENTRIES = 10;
    private static SharedPreferences sharedPreferences;

    private static SharedPreferences getSP(Context ctxt) {
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctxt);
        return sharedPreferences;
    }

    public static String readMenu(Context ctxt) {
        return getSP(ctxt).getString("MENUS", "");
    }

    public static void writeMenu(Context ctxt, String menuList) {
        getSP(ctxt).edit().putString("MENUS", menuList).commit();
    }

    public static void writeAuthInfo(Context ctxt, String authInfo) {
        getSP(ctxt).edit().putString("AUTH_INFO", authInfo).commit();
    }

    public static String readAuthInfo(Context ctxt) {
        return getSP(ctxt).getString("AUTH_INFO", "");
    }

    public static void writeProfilPic(Context ctxt, String picUrl) {
        getSP(ctxt).edit().putString("PIC_URL", picUrl).commit();
    }

    public static String readProfilPic(Context ctxt) {
        return getSP(ctxt).getString("PIC_URL", "");
    }

    public static void writeAddress(Context ctxt, String json) {
        getSP(ctxt).edit().putString("ADDRESS", json).commit();
    }

    public static String readAddress(Context ctxt) {
        return getSP(ctxt).getString("ADDRESS", "");
    }

    public static void writeCartQuantity(int cartQuantity) {
        getSP(MkartApp.getInstance()).edit().putInt("CART_QUANTITY", cartQuantity).commit();
    }

    public static int readCartQuantity() {
        return getSP(MkartApp.getInstance()).getInt("CART_QUANTITY", 0);
    }

    public static String readWalkthrough() {
        return getSP(MkartApp.getInstance()).getString("WALKTHROUGH", "");
    }

    public static void writeWalkthrough() {
        getSP(MkartApp.getInstance()).edit().putString("WALKTHROUGH", "read").commit();
    }

    public static void writeId_defaultAdd(String id_default_add) {
        getSP(MkartApp.getInstance()).edit().putString("DEFAULT_ADD", id_default_add).commit();
    }

    public static String readId_defaultAdd() {
        return getSP(MkartApp.getInstance()).getString("DEFAULT_ADD", "");
    }

    public static int readPayAttemptCount() {
        return getSP(MkartApp.getInstance()).getInt("ATTEMPT_COUNT", 0);
    }

    public static void writePayAttemptCount(int attempt_count) {
        getSP(MkartApp.getInstance()).edit().putInt("ATTEMPT_COUNT", attempt_count).commit();
    }

    public static void writeTotatPrice(String totalPrice) {
        getSP(MkartApp.getInstance()).edit().putString("TOTAL_PRICE", totalPrice).commit();
    }

    public static String readTotatPrice() {
        return getSP(MkartApp.getInstance()).getString("TOTAL_PRICE", "");
    }


    public static void writePinCode(String pincode) {
        getSP(MkartApp.getInstance()).edit().putString("PINCODE", pincode).commit();
    }

    public static String readPinCode() {
        return getSP(MkartApp.getInstance()).getString("PINCODE", "");
    }

    public static void writePriceMax(int priceMax) {
        getSP(MkartApp.getInstance()).edit().putInt("PriceMax", priceMax).commit();
    }

    public static void writePriceMin(int priceMin) {
        getSP(MkartApp.getInstance()).edit().putInt("PriceMin", priceMin).commit();
    }

    public static int readPriceMax() {
        return getSP(MkartApp.getInstance()).getInt("PriceMax", 0);
    }

    public static int readPriceMin() {
        return getSP(MkartApp.getInstance()).getInt("PriceMin", 0);
    }

    /**
     * method is used to get lru set
     * it will automatically removes the older entries in the set
     * it will hold max number of elements in a set based on LRU.
     *
     * @param maxEntries max number of entries for the set
     * @return
     */
    public static Set<String> getLRUSet(final int maxEntries) {
        Set<String> set = Collections.newSetFromMap(new LinkedHashMap<String, Boolean>() {
            @Override
            protected boolean removeEldestEntry(Entry<String, Boolean> eldest) {
                return size() > maxEntries;
            }
        });
        return set;
    }

    /**
     * method is used to get hot search history which is stored earlier.
     *
     * @param ctx context value
     * @return
     */
    public static Set<String> getHotSearch(Context ctx) {
        Set<String> set = getLRUSet(MAX_ENTRIES);
        set.addAll(getSP(ctx).getStringSet("HOT_SEARCH", set));
        return set;
    }

    /**
     * is used to store the hot search in a set
     *
     * @param ctx context value.
     * @param set LRU set to get expected results.
     */
    public static void setHotSearch(Context ctx, Set<String> set) {
        getSP(ctx).edit().putStringSet("HOT_SEARCH", set).apply();
    }

    public static void writeAppConfig(String value) {
        getSP(MkartApp.getInstance()).edit().putString("APP_CONFIG", value).commit();
    }

    public static String readAppConfig() {
        return getSP(MkartApp.getInstance()).getString("APP_CONFIG", "");
    }

    public static void writeAppURL(String value) {
        getSP(MkartApp.getInstance()).edit().putString("APP_URL", value).commit();
    }

    public static String readAppURL() {
        return getSP(MkartApp.getInstance()).getString("APP_URL", "");
    }


    public static void writeGCMToken(boolean b) {
        getSP(MkartApp.getInstance()).edit().putBoolean("GCM_TOKEN", b).commit();
    }

    public static boolean readGCMToken() {
        return getSP(MkartApp.getInstance()).getBoolean("GCM_TOKEN", false);
    }
}

