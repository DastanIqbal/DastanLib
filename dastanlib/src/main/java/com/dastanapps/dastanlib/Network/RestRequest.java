package com.dastanapps.dastanlib.Network;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;

import com.mebelkart.app.AppConfig;
import com.mebelkart.app.AppConstant;
import com.mebelkart.app.Social.ISocialResponse;
import com.mebelkart.app.Utils.MkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IQBAL-MEBELKART on 11/16/2015.
 */

/**
 * defines the requests to be sent to the server
 */
public class RestRequest {

    /**
     * @return jsonObject to be sent as a request to server
     */
    public static String getMenu() {
        JSONObject jsonObject = new JSONObject();
        return jsonObject.toString();
    }

    /**
     * @param isPager true if it is a pager
     * @return the json object to be sent as request to the server
     */
    public static String getFeatured(boolean isPager) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("isPager", "" + isPager);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * @return jsonObject to be sent as a request to server
     */
    public static String getDeals() {
        JSONObject jsonObject = new JSONObject();
        return jsonObject.toString();
    }

    /**
     * @return jsonObject to be sent as a request to server
     */
    public static String getCategories() {
        JSONObject jsonObject = new JSONObject();
        return jsonObject.toString();
    }

    public static String productDetails(String id_product, String pincode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_product", id_product);
            jsonObject.put("pincode", pincode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String checkMail(String email) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String paymentMethods(String id_cart) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_cart", id_cart);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getOrdersDetails(String orderId, String customerId, String secureKey) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_order", orderId);
            jsonObject.put("id_customer", customerId);
            jsonObject.put("secure_key", secureKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String deactivateAccount(String custId, String secureKey) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_customer", custId);
            jsonObject.put("secure_key", secureKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * @param state
     * @param fulname
     * @param shippingAddress
     * @param landmark
     * @param pincode
     * @param city
     * @param phoneno
     * @param secure_key
     * @param id_customer
     * @return the  jsonObject to be sent as a request to server
     */
    public static JSONObject newAddress(String state,
                                        String fulname, String shippingAddress, String landmark,
                                        String pincode, String city, String phoneno, String secure_key, String id_customer) {
        JSONObject newAddObject = new JSONObject();
        try {
            newAddObject.put("call_type", "new");
            newAddObject.put("state", state);
            //newAddObject.put("alias", alias);
            newAddObject.put("name", fulname);
            newAddObject.put("address1", shippingAddress);
            newAddObject.put("address2", landmark);
            newAddObject.put("postcode", pincode);
            newAddObject.put("city", city);
            newAddObject.put("phone_mobile", phoneno);
            newAddObject.put("secure_key", secure_key);
            newAddObject.put("id_customer", id_customer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newAddObject;
    }

    /**
     * @param address   the address to be added
     * @param addressId the ID of the address to be updated
     * @return jsonObject to be sent as a request to server
     */
    public static JSONObject updateAddress(JSONObject address, String addressId) {
        try {
            address.remove("call_type");
            address.put("call_type", "update");
            address.put("id_address", addressId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return address;
    }

    /**
     * det delete address request
     *
     * @param address   address to be deleted
     * @param addressId id of the address to be deleted
     * @return jsonObject to be sent as a request to server
     */
    public static JSONObject deleteAddress(JSONObject address, String addressId) {
        try {
            address.remove("call_type");
            address.put("call_type", "delete");
            address.put("id_address", addressId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return address;
    }

    /**
     * getAddress request
     *
     * @param custId    the customer_id of the user
     * @param secureKey the secure key of the user
     * @return jsonObject to be sent as a request to server
     */
    public static JSONObject getAddresses(String custId, String secureKey, String id_cart) {
        JSONObject getAddresses = new JSONObject();
        try {
            getAddresses.put("call_type", "get");
            getAddresses.put("id_customer", custId);
            getAddresses.put("secure_key", secureKey);
            getAddresses.put("id_cart", id_cart);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getAddresses;
    }

    /**
     * get new cart request
     *
     * @param prodId          id of the product to be added
     * @param prodIdAttribute product attribute id
     * @param custId          id_customer of the user
     * @param secureKey       the secure key of the user
     * @return jsonObject to be sent as a request to server
     */
    public static JSONObject newCart(String prodId, String prodIdAttribute, String custId, String secureKey) {
        JSONObject newAddCart = new JSONObject();
        try {
            newAddCart.put("call_type", "new");
            newAddCart.put("id_product", prodId);
            newAddCart.put("id_product_attribute", prodIdAttribute);
            newAddCart.put("id_customer", custId);
            newAddCart.put("secure_key", secureKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newAddCart;
    }

    /**
     * get add cart request
     *
     * @param cartInfo information of cart as json
     * @param cartId   id of the cart
     * @param operator operator
     * @return jsonObject to be sent as a request to server
     */
    public static JSONObject addCart(JSONObject cartInfo, String cartId, String operator) {
        try {
            cartInfo.remove("call_type");
            cartInfo.put("call_type", "add");
            cartInfo.put("id_cart", cartId);
            cartInfo.put("operator", operator);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cartInfo;
    }

    /**
     * get updateaddress request
     *
     * @param cartId            ud of the cart
     * @param addressIdDelivery id of the delivery address
     * @param custId            customer_id of the user
     * @return jsonObject to be sent as a request to server
     */
    public static JSONObject updateCartAddress(String cartId, String addressIdDelivery, String custId) {
        JSONObject updateCart = new JSONObject();
        try {
            updateCart.put("call_type", "updateCartAddress");
            updateCart.put("id_cart", cartId);
            updateCart.put("id_customer", custId);
            updateCart.put("id_address_delivery", addressIdDelivery);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return updateCart;
    }

    /**
     * delete cart request
     *
     * @param cartId          id of the cart
     * @param prodId          id of the product
     * @param prodIdAttribute id of the product atribute
     * @return jsonObject to be sent as a request to server
     */
    public static JSONObject deleteCart(String cartId, String prodId, String prodIdAttribute, String idDiscount) {
        JSONObject deleteCart = new JSONObject();
        try {
            deleteCart.put("call_type", "delete");
            deleteCart.put("id_cart", cartId);
            deleteCart.put("id_product", prodId);
            deleteCart.put("id_discount", idDiscount);
            deleteCart.put("id_product_attribute", prodIdAttribute);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deleteCart;
    }

    /**
     * get getAllcart request
     *
     * @param custId
     * @return jsonObject to be sent as a request to server
     */
    public static JSONObject getAllCart(String custId) {
        JSONObject getAllCart = new JSONObject();
        try {
            getAllCart.put("call_type", "getCart");
            getAllCart.put("id_customer", custId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getAllCart;
    }

    /**
     * get cart summary request
     *
     * @param cartId
     * @return jsonObject to be sent as a request to server
     */
    public static JSONObject getCartSummary(String cartId) {
        JSONObject getCart = new JSONObject();
        try {
            getCart.put("call_type", "summary");
            getCart.put("id_cart", cartId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getCart;
    }

    /**
     * get request for new Sign Up
     *
     * @param authType
     * @param email
     * @param password
     * @return jsonObject to be sent as a request to server
     */
    public static JSONObject newSignInUp(String authType, String email, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ISocialResponse.PROVIDER, authType);
            jsonObject.put("email", email);
            jsonObject.put("passwd", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /***
     * get social-json request
     *
     * @param
     * @return jsonObject to be sent as a request to server
     */
    public static String getSocailJson(Bundle b) {
        Set<String> stringSet = b.keySet();
        Iterator<String> iterator = stringSet.iterator();
        JSONObject jsonObject = new JSONObject();
        try {
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = b.getString(key);
                jsonObject.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /***
     * get payClick request
     *
     * @param cartId
     * @param paySelectedJson
     * @param payModule
     * @param attemptCount
     * @return jsonObject to be sent as a request to server
     */
    public static String getPayClick(String cartId, String paySelectedJson, String payModule, int attemptCount) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_cart", cartId);
            jsonObject.put("componentSelected", paySelectedJson);
            jsonObject.put("api_based", "8783");
            jsonObject.put("payModule", payModule);
            jsonObject.put("attempt_count", attemptCount + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /***
     * get request to check product availability in an area with given pincode
     *
     * @param prodId
     * @param pincode
     * @return request to be sent to server
     */
    public static String getPincode(String prodId, String pincode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_product", prodId);
            jsonObject.put("pincode", pincode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /***
     * request to get discount
     *
     * @param discoutCoupon
     * @param cartId
     * @param custId
     * @param secureKey
     * @return string to be sent as a request to server
     */
    public static String getDiscount(String discoutCoupon, String cartId, String custId, String secureKey) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("call_type", "apply");
            jsonObject.put("id_customer", custId);
            jsonObject.put("secure_key", secureKey);
            jsonObject.put("id_cart", cartId);
            jsonObject.put("discount_name", discoutCoupon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /***
     * request to delete discount
     *
     * @param discoutCouponId
     * @param cartId
     * @return string to be sent as a request to server
     */
    public static String deleteDiscount(String discoutCouponId, String cartId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("call_type", "delete");
            jsonObject.put("id_cart", cartId);
            jsonObject.put("discount_id", discoutCouponId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /***
     * get idea board request
     *
     * @param id_customer
     * @return jsonObject to be sent as a request to server
     */
    public static JSONObject getIdeaboard(String id_customer) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("call_type", "get");
            jsonObject.put("id_customer", id_customer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * get new ideaboard request
     *
     * @param jsonObj
     * @param name
     * @return string to be sent as a request to server
     */
    public static String newIdeaboard(JSONObject jsonObj, String name) {
        //use getIdeaboard
        jsonObj.remove("call_type");
        try {
            jsonObj.put("call_type", "new");
            jsonObj.put("name", name);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    /***
     * get request to get products in an idea board
     *
     * @param jsonObject
     * @param id_wishlist
     * @return string to be sent as a request to server
     */
    public static String getProdsIdeaboard(JSONObject jsonObject, String id_wishlist, String pincode) {
        //use getIdeaboard
        try {
            jsonObject.remove("call_type");
            jsonObject.put("call_type", "getprod");
            jsonObject.put("id_wishlist", id_wishlist);
            jsonObject.put("pincode", pincode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /***
     * request to delete ideaboard
     *
     * @param jsonObject
     * @param id_wishlist
     * @return string to be sent as a request to server
     */
    public static String deleteIdeaboard(JSONObject jsonObject, String id_wishlist) {
        //use getIdeaboard
        try {
            jsonObject.remove("call_type");
            jsonObject.put("call_type", "delete");
            jsonObject.put("id_wishlist", id_wishlist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * request to update ideaboard
     *
     * @param jsonObject
     * @param id_wishlist
     * @param name
     * @return string to be sent as a request to server
     */
    public static String updateIdeaboard(JSONObject jsonObject, String id_wishlist, String name) {
        //object from remove
        try {
            jsonObject.remove("call_type");
            jsonObject.put("call_type", "update");
            jsonObject.put("name", name);
            jsonObject.put("id_wishlist", id_wishlist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * request to remove product from an ideaboard
     *
     * @param id_wishlist
     * @param id_product
     * @param id_product_attribute
     * @param id_customer
     * @return jsonObject to be sent as a request to server
     */
    public static JSONObject removeProdIdeaboard(String id_wishlist, String id_product,
                                                 String id_product_attribute,
                                                 String id_customer) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("call_type", "remove");
            jsonObject.put("id_wishlist", id_wishlist);
            jsonObject.put("id_product", id_product);
            jsonObject.put("id_product_attribute", id_product_attribute);
            jsonObject.put("id_customer", id_customer);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * request to add an ideabaord
     *
     * @param jsonObject
     * @param quantity
     * @return string to be sent as a request to server
     */
    public static String addIdeaboard(JSONObject jsonObject,
                                      String quantity) {
        //use remove json obj
        try {
            jsonObject.remove("call_type");
            jsonObject.put("call_type", "add");
            jsonObject.put("quantity", quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * request to update products in ideaboard
     *
     * @param jsonObject
     * @param priority
     * @param qty
     * @return string to be sent as a request to server
     */
    public static String updateProdIdeaboard(JSONObject jsonObject, String priority, String qty) {
        //object from remove
        try {
            jsonObject.remove("call_type");
            jsonObject.put("call_type", "update");
            jsonObject.put("priority", priority);
            jsonObject.put("quantity", qty);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /***
     * request to get my order details
     *
     * @param customerId
     * @param secureKey
     * @return string to be sent as a request to server
     */
    public static String getMyOrderDetails(String customerId, String secureKey) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_customer", customerId);
            jsonObject.put("call_type", "getdetails");
            jsonObject.put("secure_key", secureKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * request of a search query
     *
     * @param searchQuery
     * @param nc
     * @param nf
     * @param na
     * @param np
     * @param orderway
     * @param orderby
     * @param id_cat
     * @return string to be sent as a request to server
     */
    public static String searchQuery(String searchQuery, String nc, String nf, String na, String np,
                                     String orderway, String orderby, String id_cat) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_category", id_cat);
            jsonObject.put("q", searchQuery);
            jsonObject.put("p", "1");
            jsonObject.put("n", "10");
            jsonObject.put("narrow_category", nc);
            jsonObject.put("narrow_features", nf);
            jsonObject.put("narrow_attributes", na);
            jsonObject.put("narrow_price", np);
            jsonObject.put("orderby", orderby);
            jsonObject.put("orderway", orderway);
            jsonObject.put("id_selected_city", AppConfig.SELECTED_CITY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /***
     * request to get product list
     *
     * @param cartId
     * @param pageNo
     * @param numOfProd
     * @return string to be sent as a request to server
     */
    public static String getProductList(String cartId, String pageNo, String numOfProd) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_category", cartId);
            jsonObject.put("p", pageNo);
            jsonObject.put("n", numOfProd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * request to change password
     *
     * @param custId
     * @param oldPassword
     * @param newPassword
     * @return string to be sent as a request to server
     */
    public static String changePassword(String custId, String oldPassword, String newPassword) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_customer", custId);
            jsonObject.put("oldpassword", oldPassword);
            jsonObject.put("newpassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * request to register for notify by email for a product
     *
     * @param productId
     * @param email
     * @param signup_date
     * @return string to be sent as a request to server
     */
    public static String notifyEmail(String productId, String email, String signup_date) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_product", productId);
            jsonObject.put("email", email);
            jsonObject.put("signup_date", signup_date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String contactMeForm(String id_product, String email, String phone, String name, String city) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_product", id_product);
            jsonObject.put("email", email);
            jsonObject.put("phone", phone);
            jsonObject.put("name", name);
            jsonObject.put("city", city);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /*****
     * request to update personal info
     *
     * @param id_customer
     * @param secure_key
     * @param fname
     * @param lname
     * @param gender
     * @param email
     * @return jsonObject to be sent as a request to server
     */
    public static JSONObject updatePersonalInfo(String id_customer, String secure_key, String fname,
                                                String lname, String gender, String email) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_customer", id_customer);
            jsonObject.put("secure_key", secure_key);
            jsonObject.put("fname", fname);
            jsonObject.put("lname", lname);
            jsonObject.put("gender", gender);
            //   jsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static String appConfigURL(Context ctxt) {
        String baseString = null;
        String iv = MkUtils.getIV(ctxt);
        try {
            byte[] data = iv.getBytes("UTF-8");
            baseString = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(baseString)) {
            baseString = String.format(AppConstant.APP_CONFIG_URL + "config/%s", baseString);
            return baseString;
        } else {
            return baseString;
        }
    }

    /**
     * Add request for Push Notification
     */
    public static String addPushId(String token) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("regId", token);
            jsonObject.put("platform", "Android");
            jsonObject.put("devInfo", getDevInfo());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private static String getDevInfo() {
        JSONObject jsonObject = new JSONObject();
        return jsonObject.toString();
    }

    /***
     *
     */
    public static class SearchBuilder implements Parcelable {
        private String id_category = "", query = "", pageno = "", numProd,
                narrow_category = "", narrow_features = "", narrow_attributes = "",
                narrow_price = "", orderby = "", orderway = "";

        public SearchBuilder() {

        }

        protected SearchBuilder(Parcel in) {
            id_category = in.readString();
            query = in.readString();
            pageno = in.readString();
            numProd = in.readString();
            narrow_category = in.readString();
            narrow_features = in.readString();
            narrow_attributes = in.readString();
            narrow_price = in.readString();
            orderby = in.readString();
            orderway = in.readString();
        }

        /***
         *
         */
        public static final Creator<SearchBuilder> CREATOR = new Creator<SearchBuilder>() {
            @Override
            public SearchBuilder createFromParcel(Parcel in) {
                return new SearchBuilder(in);
            }

            @Override
            public SearchBuilder[] newArray(int size) {
                return new SearchBuilder[size];
            }
        };

        public SearchBuilder setId_category(String id_category) {
            this.id_category = id_category;
            return this;
        }

        public SearchBuilder setQuery(String query) {
            try {
                this.query = URLEncoder.encode(query, "ASCII");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return this;
        }

        public SearchBuilder setPageno(String pageno) {
            this.pageno = pageno;
            return this;
        }

        public SearchBuilder setNumProd(String numProd) {
            this.numProd = numProd;
            return this;
        }

        public SearchBuilder setNarrow_category(String narrow_category) {
            this.narrow_category = narrow_category;
            return this;
        }

        public SearchBuilder setNarrow_features(String narrow_features) {
            this.narrow_features = narrow_features;
            return this;
        }

        public SearchBuilder setNarrow_attributes(String narrow_attributes) {
            this.narrow_attributes = narrow_attributes;
            return this;
        }

        public SearchBuilder setNarrow_price(String narrow_price) {
            this.narrow_price = narrow_price;
            return this;
        }

        public SearchBuilder setOrderby(String orderby) {
            this.orderby = orderby;
            return this;
        }

        public SearchBuilder setOrderway(String orderway) {
            this.orderway = orderway;
            return this;
        }

        public String build() {
            JSONObject jsonObject = new JSONObject();
            try {
                if (!TextUtils.isEmpty(id_category))
                    jsonObject.put("id_category", id_category);

                if (!TextUtils.isEmpty(query))
                    jsonObject.put("q", query);

                if (!TextUtils.isEmpty(pageno))
                    jsonObject.put("p", pageno);
                else
                    jsonObject.put("p", "1");

                if (!TextUtils.isEmpty(numProd))
                    jsonObject.put("n", numProd);
                else
                    jsonObject.put("n", AppConstant.NO_LIST_PRODUCT);

                if (!TextUtils.isEmpty(narrow_category))
                    jsonObject.put("narrow_category", narrow_category);

                if (!TextUtils.isEmpty(narrow_features))
                    jsonObject.put("narrow_features", narrow_features);

                if (!TextUtils.isEmpty(narrow_attributes))
                    jsonObject.put("narrow_attributes", narrow_attributes);

                if (!TextUtils.isEmpty(narrow_price))
                    jsonObject.put("narrow_price", narrow_price);

                if (!TextUtils.isEmpty(orderby))
                    jsonObject.put("orderby", orderby);
               /* else
                    jsonObject.put("orderby", "position");*/

                if (!TextUtils.isEmpty(orderway))
                    jsonObject.put("orderway", orderway);
               /* else
                    jsonObject.put("orderway", "desc");*/

                jsonObject.put("id_selected_city", AppConfig.SELECTED_CITY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id_category);
            dest.writeString(query);
            dest.writeString(pageno);
            dest.writeString(numProd);
            dest.writeString(narrow_category);
            dest.writeString(narrow_features);
            dest.writeString(narrow_attributes);
            dest.writeString(narrow_price);
            dest.writeString(orderby);
            dest.writeString(orderway);
        }
    }
}
