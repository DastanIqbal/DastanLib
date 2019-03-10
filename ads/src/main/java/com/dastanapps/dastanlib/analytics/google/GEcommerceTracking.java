/*
package com.dastanapps.dastanlib.analytics.google;

import android.content.Context;

import com.dastanapps.dastanlib.BuildConfig;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.analytics.ecommerce.Promotion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

*/
/**
 * Created by IQBAL-MEBELKART on 6/8/2016.
 *//*

public class GEcommerceTracking {
    private static final String TAG = GEcommerceTracking.class.getSimpleName();
    private static Tracker mTracker;

    public static void checkTrackerNull(Context ctxt,int trackerId) {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(ctxt);
            if (!BuildConfig.DEBUG) {
                analytics.setDryRun(true);
                analytics.setAppOptOut(true); //To disable analytics across the app
            }
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(trackerId);
        }
    }

    public static void measureImpression(JSONObject impressionJson, String screenName) {
        Product product = new Product();
        try {
            JSONObject jsonObject = impressionJson;
            product.setId(jsonObject.getString("id"));
            product.setName(jsonObject.getString("name"));
            product.setCategory(jsonObject.getString("category"));
            product.setBrand(jsonObject.getString("brand"));
            product.setVariant(jsonObject.getString("variant"));
            product.setPosition(jsonObject.getInt("position"));
            product.setCustomDimension(1, jsonObject.getString("customDimValue"));
            HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                    .addImpression(product, screenName);

            mTracker.setScreenName(screenName);
            mTracker.send(builder.build());
        } catch (JSONException e) {
            //e.printStackTrace();
        }
    }

    public static void measureAction(JSONObject actionJson, String actionName) {
        Product product = new Product();
        try {
            JSONObject jsonObject = actionJson;
            product.setId(jsonObject.getString("id"));
            product.setName(jsonObject.getString("name"));
            product.setCategory(jsonObject.getString("category"));
            product.setBrand(jsonObject.getString("brand"));
            product.setVariant(jsonObject.getString("variant"));
            product.setPosition(jsonObject.getInt("position"));
            product.setCustomDimension(1, jsonObject.getString("customDimValue"));
        } catch (JSONException e) {
            //e.printStackTrace();
        }

        ProductAction productAction = new ProductAction(ProductAction.ACTION_CLICK)
                .setProductActionList(actionName);
        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                .addProduct(product)
                .setProductAction(productAction);

        mTracker.setScreenName(actionName);
        mTracker.send(builder.build());
    }

    public static void measureImpressionAndAction(JSONObject impressionNActionJson) {

        ProductAction productAction = new ProductAction(ProductAction.ACTION_DETAIL);
        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder();

        try {
            // The product being viewed.
            Product viewedProduct = new Product();
            JSONObject jsonObject = impressionNActionJson;
            viewedProduct.setId(jsonObject.getString("id"));
            viewedProduct.setName(jsonObject.getString("name"));
            viewedProduct.setCategory(jsonObject.getString("category"));
            viewedProduct.setBrand(jsonObject.getString("brand"));
            viewedProduct.setVariant(jsonObject.getString("variant"));
            viewedProduct.setPosition(jsonObject.getInt("position"));
            viewedProduct.setCustomDimension(1, jsonObject.getString("customDimValue"));

            builder.addProduct(viewedProduct);
            builder.setProductAction(productAction);

            JSONArray relatedProdArray=jsonObject.getJSONArray("relatedProducts");
            for(int i=0;i<relatedProdArray.length();i++){
                JSONObject relatedObject = relatedProdArray.getJSONObject(i);
                // The product from a related products section.
                Product relatedProduct = new Product();
                relatedProduct.setId(relatedObject.getString("id"));
                relatedProduct.setName(relatedObject.getString("name"));
                relatedProduct.setCategory(relatedObject.getString("category"));
                relatedProduct.setBrand(relatedObject.getString("brand"));
                relatedProduct.setVariant(relatedObject.getString("variant"));
                relatedProduct.setPosition(relatedObject.getInt("position"));
                relatedProduct.setCustomDimension(1, relatedObject.getString("customDimValue"));

                builder.addImpression(relatedProduct, "Related Products");
            }
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        mTracker.setScreenName("product");
        mTracker.send(builder.build());
    }

    public static void measureTransaction(String transactionJson) {
        Product product = new Product()
                .setId("P12345")
                .setName("Android Warhol T-Shirt")
                .setCategory("Apparel/T-Shirts")
                .setBrand("Google")
                .setVariant("black")
                .setPrice(29.20)
                .setCouponCode("APPARELSALE")
                .setQuantity(1);
        ProductAction productAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                .setTransactionId("T12345")
                .setTransactionAffiliation("Google Store - Online")
                .setTransactionRevenue(37.39)
                .setTransactionTax(2.85)
                .setTransactionShipping(5.34)
                .setTransactionCouponCode("SUMMER2013");
        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                .addProduct(product)
                .setProductAction(productAction);

        mTracker.setScreenName("transaction");
        mTracker.send(builder.build());
    }

    public static void measureCheckoutStep() {
        Product product = new Product()
                .setId("P12345")
                .setName("Android Warhol T-Shirt")
                .setCategory("Apparel/T-Shirts")
                .setBrand("Google")
                .setVariant("black")
                .setPrice(29.20)
                .setQuantity(1);
        // Add the step number and additional info about the checkout to the action.
        ProductAction productAction = new ProductAction(ProductAction.ACTION_CHECKOUT)
                .setCheckoutStep(1)
                .setCheckoutOptions("Visa");
        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                .addProduct(product)
                .setProductAction(productAction);

        mTracker.setScreenName("checkoutStep1");
        mTracker.send(builder.build());
    }

    public static void measureCheckoutOption() {
        // (On "Next" button click.)
        ProductAction productAction = new ProductAction(ProductAction.ACTION_CHECKOUT_OPTIONS)
                .setCheckoutStep(1)
                .setCheckoutOptions("FedEx");
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setProductAction(productAction)
                .setCategory("Checkout")
                .setAction("Option");

        mTracker.send(builder.build());

        // Advance to next page.
    }

    public static void measureRefundTransaction() {
        // Refund an entire transaction.
        ProductAction productAction = new ProductAction(ProductAction.ACTION_REFUND)
                .setTransactionId("T12345");  // Transaction ID is only required field for a full refund.
        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                .setProductAction(productAction);

        mTracker.setScreenName("refund");
        mTracker.send(builder.build());
    }

    public static void measureRefundProduct() {
        // Refund a single product.
        Product product = new Product()
                .setId("P12345")  // Product ID is required for partial refund.
                .setQuantity(1);  // Quanity is required for partial refund.
        ProductAction productAction = new ProductAction(ProductAction.ACTION_REFUND)
                .setTransactionId("T12345");  // Transaction ID is required for partial refund.
        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                .addProduct(product)
                .setProductAction(productAction);

        mTracker.setScreenName("refundProduct");
        mTracker.send(builder.build());
    }

    public static void measureRefundNonInteraction() {
        // Refund an entire transaction.
        ProductAction productAction = new ProductAction(ProductAction.ACTION_REFUND)
                .setTransactionId("T12345");
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setProductAction(productAction)
                .setNonInteraction(true)
                .setCategory("Ecommerce")
                .setAction("Refund");

        mTracker.send(builder.build());
    }

    public static void measurePromotionImpression() {
        Promotion promotion = new Promotion()
                .setId("PROMO_1234")
                .setName("Summer Sale")
                .setCreative("summer_banner2")
                .setPosition("banner_slot1");
        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                .addPromotion(promotion);

        mTracker.setScreenName("promotions");
        mTracker.send(builder.build());
    }

    public static void measurePromotionClick() {
        Promotion promotion = new Promotion()
                .setId("PROMO_1234")
                .setName("Summer Sale")
                .setCreative("summer_banner2")
                .setPosition("banner_slot1");
        ProductAction promoClickAction = new ProductAction(Promotion.ACTION_CLICK);
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .addPromotion(promotion)
                .setProductAction(promoClickAction)
                .setCategory("Internal Promotions")
                .setAction("click")
                .setLabel("Summer Sale");

        mTracker.send(builder.build());
    }
}
*/
