package com.dastanapps.dastanlib.Network;

import android.text.TextUtils;

import com.mebelkart.app.AppConfig;
import com.mebelkart.app.Base.BaseB;
import com.mebelkart.app.Beans.AddressB;
import com.mebelkart.app.Beans.AppConfigB;
import com.mebelkart.app.Beans.AttributeGrpAttriB;
import com.mebelkart.app.Beans.AttributeGrpB;
import com.mebelkart.app.Beans.AttributeMappingB;
import com.mebelkart.app.Beans.AuthInfoB;
import com.mebelkart.app.Beans.CartB;
import com.mebelkart.app.Beans.CartDiscountB;
import com.mebelkart.app.Beans.CartProductB;
import com.mebelkart.app.Beans.CategoriesB;
import com.mebelkart.app.Beans.CatgeoriesListB;
import com.mebelkart.app.Beans.CheckPincodeB;
import com.mebelkart.app.Beans.CheckoutAddressCheck;
import com.mebelkart.app.Beans.ChildrenItemB;
import com.mebelkart.app.Beans.DealsB;
import com.mebelkart.app.Beans.DealsChildrenB;
import com.mebelkart.app.Beans.DealsListB;
import com.mebelkart.app.Beans.DiscountB;
import com.mebelkart.app.Beans.EMIPaymentMethodComp;
import com.mebelkart.app.Beans.FeaturedB;
import com.mebelkart.app.Beans.FeaturedChildrenB;
import com.mebelkart.app.Beans.IdeaboardB;
import com.mebelkart.app.Beans.IdeaboardItemB;
import com.mebelkart.app.Beans.MenuB;
import com.mebelkart.app.Beans.MyOrderDetailsB;
import com.mebelkart.app.Beans.OrderMainB;
import com.mebelkart.app.Beans.OrdersB;
import com.mebelkart.app.Beans.OrdersProdFeaturesB;
import com.mebelkart.app.Beans.OrdersProductB;
import com.mebelkart.app.Beans.PayClickB;
import com.mebelkart.app.Beans.PaymentMethodB;
import com.mebelkart.app.Beans.PaymentMethodCompB;
import com.mebelkart.app.Beans.PaymentMethodStatus;
import com.mebelkart.app.Beans.ProdReviewsB;
import com.mebelkart.app.Beans.ProductDetailsB;
import com.mebelkart.app.Beans.ProductImageB;
import com.mebelkart.app.Beans.ProductListB;
import com.mebelkart.app.Beans.ProductListItemB;
import com.mebelkart.app.Beans.PushB;
import com.mebelkart.app.Beans.RateB;
import com.mebelkart.app.Beans.SearchB;
import com.mebelkart.app.Beans.SearchChildB;
import com.mebelkart.app.Beans.SearchFiltersB;
import com.mebelkart.app.Beans.SearchResultB;
import com.mebelkart.app.Log.Logger;
import com.mebelkart.app.Payment.Beans.PayInfoB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RestResponse {

    private static final String TAG = RestResponse.class.getSimpleName();

    /****
     * common method to call at every onResponse
     *
     * @param json
     * @return the status and message sent from server
     */
    public static BaseB checkStatus(String json) {
        BaseB baseB = null;
        try {
            if (!TextUtils.isEmpty(json)) {
                JSONObject statusObject = new JSONObject(json);
                baseB = new BaseB();
                if (statusObject.getString("status").equals("200")) {
                    baseB.setStatus(statusObject.getString("status"));
                    if (statusObject.has("msg")) {
                        baseB.setMsg(statusObject.getString("msg"));
                    }
                } else {
                    baseB.setStatus(statusObject.getString("status"));
                    baseB.setMsg(statusObject.getString("msg"));
                }
            } else {
                return null;
            }
        } catch (JSONException e) {
            return null;
        }

        return baseB;
    }

    /***
     * @param jsonStr
     * @return
     */
    public static ArrayList<MenuB> parseMenu(String jsonStr) {
        try {
            JSONObject menuObj = new JSONObject(jsonStr);
            JSONObject catgTree = menuObj.getJSONObject("blockCategTree");
            JSONArray jsonArray = catgTree.getJSONArray("children");
            return prepareMenuList(jsonArray.toString(), catgTree.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * @param json
     * @param name
     * @return
     */
    public static ArrayList<MenuB> prepareMenuList(String json, String name) {
        ArrayList<MenuB> menuList = new ArrayList<>();
        try {
            JSONArray menuArray = new JSONArray(json);
            //For Menu Header
            MenuB headerMenuB = new MenuB();
            headerMenuB.setName(name);
            menuList.add(headerMenuB);

            for (int i = 0; i < menuArray.length(); i++) {
                JSONObject catObj = menuArray.getJSONObject(i);

                MenuB menuB = new MenuB();
                menuB.setId(catObj.getString("id"));
                menuB.setLink_rewrite(catObj.getString("link_rewrite"));
                menuB.setName(catObj.getString("name"));
                menuB.setChildren(catObj.getJSONArray("children").toString());

                menuList.add(menuB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return menuList;
    }

    /***
     * get parsed featuredlist
     *
     * @param json response json
     * @return featured products
     */
    public static FeaturedB parseFeatured(String json) {
        FeaturedB featuredB = null;
        ArrayList<FeaturedChildrenB> featuredList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String total_pages = jsonObject.getString("total_pages");
            String page_no = jsonObject.getString("page_no");
            JSONArray featuredArray = jsonObject.getJSONArray("featured");
            // if (featuredArray.length() != 0) {
            featuredB = new FeaturedB();
            featuredB.setStatus(status);
            featuredB.setTotal_pages(total_pages);
            featuredB.setPage_no(page_no);

            for (int i = 0; i < featuredArray.length(); i++) {
                JSONObject jobj = featuredArray.getJSONObject(i);
                String type = jobj.getString("type");
                if (type.equals("pager")) {
                    FeaturedChildrenB pagerB = new FeaturedChildrenB();
                    pagerB.setType(type);
                    pagerB.setChildren(jobj.getJSONArray("children").toString());
                    featuredList.add(pagerB);
                } else if (type.equals("deals")) {
                    FeaturedChildrenB dealsB = new FeaturedChildrenB();
                    dealsB.setType(type);
                    dealsB.setTitle(jobj.getString("title"));
                    dealsB.setChildren(jobj.getJSONArray("children").toString());
                    featuredList.add(dealsB);
                } else if (type.equals("discount")) {
                    FeaturedChildrenB discountB = new FeaturedChildrenB();
                    discountB.setType(type);
                    discountB.setTitle(jobj.getString("title"));
                    discountB.setChildren(jobj.getJSONArray("children").toString());
                    featuredList.add(discountB);
                } else if (type.equals("categories")) {
                    FeaturedChildrenB catB = new FeaturedChildrenB();
                    catB.setType(type);
                    catB.setCat_id(jobj.getString("category_id"));
                    catB.setCat_name(jobj.getString("category_name"));
                    catB.setStart_price(jobj.getString("start_price"));
                    catB.setChildren(jobj.getJSONArray("children").toString());
                    featuredList.add(catB);
                } else if (type.equals("brands")) {
                    FeaturedChildrenB brandB = new FeaturedChildrenB();
                    brandB.setType(type);
                    brandB.setBrand_id(jobj.getString("brand_id"));
                    brandB.setCat_name(jobj.getString("category_name"));
                    brandB.setStart_price(jobj.getString("start_price"));
                    brandB.setChildren(jobj.getJSONArray("children").toString());
                    featuredList.add(brandB);
                }
            }
            featuredB.setFeaturedChildrenList(featuredList);
            //  }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return featuredB;
    }

    /***
     * get item details
     *
     * @param json response json
     * @return item details as a bean
     */
    public static ChildrenItemB parseChildren(String json) {
        try {
            ChildrenItemB childrenItemB = new ChildrenItemB();
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.has("type")) {
                String type = jsonObject.getString("type");
                childrenItemB.setType(type);
            }

            if (jsonObject.has("product_id")) {
                String product_id = jsonObject.getString("product_id");
                childrenItemB.setProduct_id(product_id);
            }

            if (jsonObject.has("category_id")) {
                String category_id = jsonObject.getString("category_id");
                childrenItemB.setCategory_id(category_id);
            }

            if (jsonObject.has("brand_id")) {
                String brand_id = jsonObject.getString("brand_id");
                childrenItemB.setBrand_id(brand_id);
            }
            if (jsonObject.has("category_name")) {
                String category_name = jsonObject.getString("category_name");
                childrenItemB.setCategory_name(category_name);
            }
            if (jsonObject.has("imag_url")) {
                String imag_url = jsonObject.getString("imag_url");
                childrenItemB.setImg_url(imag_url);
            }

            if (jsonObject.has("title")) {
                String title = jsonObject.getString("title");
                childrenItemB.setTitle(title);
            }

            if (jsonObject.has("desc")) {
                String desc = jsonObject.getString("desc");
                childrenItemB.setDesc(desc);
            }

            if (jsonObject.has("offer_text")) {
                String offer_text = jsonObject.getString("offer_text");
                childrenItemB.setOffer_text(offer_text);
            }

            if (jsonObject.has("mkt_price")) {
                String mkt_price = jsonObject.getString("mkt_price");
                childrenItemB.setMkt_price(mkt_price);
            }

            if (jsonObject.has("our_price")) {
                String our_price = jsonObject.getString("our_price");
                childrenItemB.setOur_price(our_price);
            }

            if (jsonObject.has("is_sold_out")) {
                String is_sold_out = jsonObject.getString("is_sold_out");
                childrenItemB.setIs_sold_out(is_sold_out);
            }

            return childrenItemB;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * get list of category
     *
     * @param json response
     * @return list of categories as a bean
     */
    public static CatgeoriesListB parseCategoriesList(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String total_pages = jsonObject.getString("total_pages");
            String page_no = jsonObject.getString("page_no");
            String categories = jsonObject.getString("categories");
            CatgeoriesListB catgeoriesListB = new CatgeoriesListB();
            catgeoriesListB.setStatus(status);
            catgeoriesListB.setCategories(categories);
            catgeoriesListB.setTotal_pages(total_pages);
            catgeoriesListB.setPage_no(page_no);
            catgeoriesListB.setCategories(categories);
            return catgeoriesListB;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * get list of categories
     *
     * @param json response json
     * @return listof categories as a list of bean
     */
    public static ArrayList<CategoriesB> parseCategories(String json) {
        ArrayList<CategoriesB> catList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String cat_id = jsonObj.getString("category_id");
                String brand_id = jsonObj.getString("brand_id");
                String category_name = jsonObj.getString("category_name");
                String imag_url = jsonObj.getString("imag_url");
                String offer_text = jsonObj.getString("offer_text");
                String title = jsonObj.getString("title");

                CategoriesB categoriesB = new CategoriesB();
                categoriesB.setCategory_id(cat_id);
                categoriesB.setBrand_id(brand_id);
                categoriesB.setCategory_name(category_name);
                categoriesB.setImage_url(imag_url);
                categoriesB.setOffer_text(offer_text);
                categoriesB.setTitle(title);

                catList.add(categoriesB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return catList;
    }

    /***
     * parse the deals
     *
     * @param json response json
     * @return list of deals as  bean
     */
    public static DealsListB parseDealsList(String json) {
        DealsListB dealsListB = null;
        ArrayList<DealsChildrenB> dealsListsB = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String total_pages = jsonObject.getString("total_pages");
            String page_no = jsonObject.getString("page_no");
            JSONArray dealsArray = jsonObject.getJSONArray("deals");
            // if (dealsArray.length() != 0) {
            dealsListB = new DealsListB();
            dealsListB.setStatus(status);
            dealsListB.setTotal_pages(total_pages);
            dealsListB.setPage_no(page_no);

            for (int i = 0; i < dealsArray.length(); i++) {
                JSONObject jobj = dealsArray.getJSONObject(i);
                String type = jobj.getString("type");
                if (type.equals("deals")) {
                    String category_id = jobj.getString("category_id");
                    String category_name = jobj.getString("category_name");
                    String children = jobj.getString("children");

                    DealsChildrenB dealsChildrenB = new DealsChildrenB();
                    dealsChildrenB.setType(type);
                    dealsChildrenB.setCat_id(category_id);
                    dealsChildrenB.setCat_name(category_name);
                    dealsChildrenB.setChildren(children);

                    dealsListsB.add(dealsChildrenB);
                }
            }
            dealsListB.setDealsChildrenList(dealsListsB);
            //  }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dealsListB;
    }

    /***
     * get parsed deal
     *
     * @param deals response string to be parsed
     * @return get the details of a deal in bean
     */
    public static DealsB parseDeals(String deals) {
        try {
            JSONObject jsonObj = new JSONObject(deals);
            String type = jsonObj.getString("type");
            String product_id = jsonObj.getString("product_id");
            //String brand_id = jsonObj.getString("brand_id");
            String imag_url = jsonObj.getString("imag_url");
            //String offer_text = jsonObj.getString("offer_text");
            String title = jsonObj.getString("title");
            String is_sold_out = jsonObj.getString("is_sold_out");
            String mkt_price = jsonObj.getString("mkt_price");
            String our_price = jsonObj.getString("our_price");
            String flash_end_date = jsonObj.getString("flash_sale_ends_in");

            DealsB dealsB = new DealsB();
            dealsB.setType(type);
            dealsB.setProduct_id(product_id);
            //dealsB.setBrand_id(brand_id);
            dealsB.setImg_url(imag_url);/**/
            // dealsB.setOffer_text(offer_text);
            dealsB.setTitle(title);
            dealsB.setIs_sold_out(is_sold_out);
            dealsB.setMkt_price(mkt_price);
            dealsB.setOur_price(our_price);
            dealsB.setFlash_sales_end_in(flash_end_date);
//
            return dealsB;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get the details of the product
     *
     * @param json the reponse string
     * @return details of the product as a bean
     */
    public static ProductDetailsB parseProductDetails(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            if (!status.equalsIgnoreCase("200")) {
                ProductDetailsB productDetailsB = new ProductDetailsB();
                productDetailsB.setStatus(status);
                return productDetailsB;
            } else {

                String category_id = jsonObject.getString("category_id");
                String category_name = jsonObject.getString("category_name");
                String product_id = jsonObject.getString("product_id");
                String product_name = jsonObject.getString("product_name");
                String prod_desc = jsonObject.getString("product_desc");
                String brand_id = jsonObject.getString("brand_id");
                String brand_name = jsonObject.getString("brand_name");
                String total_sold = jsonObject.getString("total_views");
                String shipping_cost = jsonObject.getString("shipping_cost");
                String shipping_avail = jsonObject.getString("shipping_available");
                //String total_faviourtedby = jsonObject.getString("total_faviourtedby");
                String gallery = jsonObject.getString("gallery");
                String offer_text = jsonObject.getString("offer_text");
                String mkt_price = jsonObject.getString("mkt_price");
                String reference = jsonObject.getString("reference");
                String our_price = jsonObject.getString("our_price");
                String emi_price = jsonObject.getString("emi_price");
                String rating = jsonObject.getString("rating");
                String attributes = jsonObject.getString("attributes");
                String product_features = jsonObject.getString("product_features");
                String reviews = jsonObject.getString("reviews");
                String total_reviews = jsonObject.getString("reviews_count");
                String avail_location = jsonObject.getString("avail_location");
                String is_sold_out = jsonObject.getString("is_sold_out");
                String share_link = jsonObject.getString("link");
                String similarProducts = jsonObject.getString("similar_products");
                String is_modular_category = jsonObject.getString("is_modular_category");

                ProductDetailsB productDetailsB = new ProductDetailsB();

                if (jsonObject.has("pincode_res")) {
                    productDetailsB.setPincodeRes(jsonObject.getString("pincode_res"));
                }

                productDetailsB.setStatus(status);
                productDetailsB.setIs_modular_category(is_modular_category);
                productDetailsB.setCat_id(category_id);
                productDetailsB.setCat_name(category_name);
                productDetailsB.setProduct_id(product_id);
                productDetailsB.setProduct_name(product_name);
                productDetailsB.setBrand_id(brand_id);
                productDetailsB.setBrand_name(brand_name);
                productDetailsB.setTotal_sold(total_sold);
                //productDetailsB.setTotal_favourtiedby(total_faviourtedby);
                productDetailsB.setShipping_avail(shipping_avail);
                productDetailsB.setShipping_cost(shipping_cost);
                productDetailsB.setProduct_desc(prod_desc);
                productDetailsB.setGallery(gallery);
                productDetailsB.setOffer_text(offer_text);
                productDetailsB.setMkt_price(mkt_price);
                productDetailsB.setOur_price(our_price);
                productDetailsB.setReference(reference);
                productDetailsB.setEmi_price(emi_price);
                productDetailsB.setRating(rating);
                productDetailsB.setAttributes(attributes);
                productDetailsB.setProduct_features(product_features);
                productDetailsB.setReivews(reviews);
                productDetailsB.setTotal_reviews(total_reviews);
                productDetailsB.setAvail_location(avail_location);
                productDetailsB.setIs_sold_out(is_sold_out);
                productDetailsB.setShareLink(share_link);
                productDetailsB.setSimilarProducts(similarProducts);
                String attribute_groups = jsonObject.getString("attribute_groups");

                if (!TextUtils.isEmpty(attribute_groups)) {
                    JSONObject attribGroups = new JSONObject(attribute_groups);
                    if (attribGroups.has("groups")) {
                        String attribGrps = attribGroups.getString("groups");
                        productDetailsB.setProudctAttributeGroups(attribGrps);
                    }

                    if (attribGroups.has("attributesMapping")) {
                        String attribMappings = attribGroups.getString("attributesMapping");
                        productDetailsB.setPrductAttributeMappings(attribMappings);
                    }
                }
                return productDetailsB;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * parse ratings of a product
     *
     * @param json string as response
     * @return list of rates in an arraylist of bean
     */
    public static ArrayList<RateB> parseRates(String json) {
        ArrayList<RateB> arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jobj = jsonArray.getJSONObject(i);
                String rate = jobj.getString("rate");
                String rate_link = jobj.getString("rate_link");
                String reviews = jobj.getString("reviews");

                RateB rateB = new RateB();
                rateB.setRate(rate);
                rateB.setRate_link(rate_link);
                rateB.setReviews(reviews);

                arrayList.add(rateB);
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * parse product list
     *
     * @param json
     * @return list of products
     */
    public static ProductListB parseProductList(String json) {
        ProductListB productList = new ProductListB();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            String category_id = jsonObject.getString("category_id");
            String category_name = jsonObject.getString("category_name");
            String filters = jsonObject.getString("filters");
            String product_list = jsonObject.getString("product_list");
            Long totalProducts = jsonObject.getLong("total_products");
            Long totalPages = jsonObject.getLong("total_pages");

            productList.setStatus(status);
            productList.setCat_id(category_id);
            productList.setCat_name(category_name);
            productList.setFilters(filters);
            productList.setProd_list(product_list);
            productList.setTotal_pages(totalPages);
            productList.setTotal_products(totalProducts);


            return productList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * get the list of product items in details
     *
     * @param json response json
     * @return list of products as an array list
     */
    public static ArrayList<ProductListItemB> parseProductListItemB(String json) {
        ArrayList<ProductListItemB> productArrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String product_id = jsonObject.getString("product_id");
                String brand_id = jsonObject.getString("brand_id");
                String image_url = jsonObject.getString("img_url");
                String title = jsonObject.getString("title");
                String offer_text = jsonObject.getString("offer_text");
                String mkt_price = jsonObject.getString("mkt_price");
                String our_price = jsonObject.getString("our_price");
                String popularity = jsonObject.getString("popularity");
                String sold_out = jsonObject.getString("sold_out");
                String is_sold_out = jsonObject.getString("is_sold_out");
                String is_modular_category = jsonObject.getString("is_modular_category");

                ProductListItemB productListItemB = new ProductListItemB();
                productListItemB.setProd_id(product_id);
                productListItemB.setBrand_id(brand_id);
                productListItemB.setImage_url(image_url);
                productListItemB.setTitle(title);
                productListItemB.setOffer_text(offer_text);
                productListItemB.setMkt_price(mkt_price);
                productListItemB.setOur_price(our_price);
                productListItemB.setPopularity(popularity);
                productListItemB.setSold_out(sold_out);
                productListItemB.setIs_sold_out(is_sold_out);
                productListItemB.setIs_modular_category(is_modular_category);

                productArrayList.add(productListItemB);
            }

            return productArrayList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * Important
     * get the authorisation details of a user
     *
     * @param json response
     * @return information of user in a bean
     */
    public static AuthInfoB parseAuthInfo(String json) {
        AuthInfoB authInfoB = new AuthInfoB();
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("provider")) {
                authInfoB.setProvider(jsonObject.getString("provider"));
            } else {
                authInfoB.setProvider("");
            }

            if (jsonObject.has("cartId")) {
                if (jsonObject.getString("cartId") != null
                        && !jsonObject.getString("cartId").equals("null"))
                    authInfoB.setId_cart(jsonObject.getString("cartId"));
            } else {
                authInfoB.setId_cart("");
            }

            if (jsonObject.has("email")) {
                authInfoB.setEmail(jsonObject.getString("email"));
            } else {
                authInfoB.setEmail("");
            }

            if (jsonObject.has("customerId")) {
                authInfoB.setCustomerId(jsonObject.getString("customerId"));
            } else {
                authInfoB.setCustomerId("");
            }

            if (jsonObject.has("secure_key")) {
                authInfoB.setSecure_key(jsonObject.getString("secure_key"));
            } else {
                authInfoB.setSecure_key("");
            }

            if (jsonObject.has("hasAddress")) {
                authInfoB.setHasAddress(jsonObject.getBoolean("hasAddress"));
            } else {
                authInfoB.setHasAddress(false);
            }
            if (jsonObject.has("cart_total_quantity")) {
                authInfoB.setCart_total_quantity(jsonObject.getInt("cart_total_quantity"));
            } else {
                authInfoB.setCart_total_quantity(0);
            }
            if (jsonObject.has("id_default_address")) {
                authInfoB.setId_default_add(jsonObject.getString("id_default_address"));
            } else {
                authInfoB.setId_default_add("");
            }

            if (jsonObject.has("fname")) {
                authInfoB.setFname(jsonObject.getString("fname"));
            } else {
                authInfoB.setFname("");
            }
            if (jsonObject.has("lname")) {
                authInfoB.setLname(jsonObject.getString("lname"));
            } else {
                authInfoB.setLname("");
            }
            if (jsonObject.has("gender")) {
                authInfoB.setGender(jsonObject.getString("gender"));
            } else {
                authInfoB.setGender("");
            }
            if (jsonObject.has("total_ideaboards")) {
                authInfoB.setTotalIdeaboards(jsonObject.getString("total_ideaboards"));
            } else {
                authInfoB.setTotalIdeaboards("0");
            }

            if (jsonObject.has("profilePic")) {
                authInfoB.setProfilePic(jsonObject.getString("profilePic"));
            } else {
                authInfoB.setProfilePic("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return authInfoB;
    }

    /***
     * parse cart details
     *
     * @param json response for cart
     * @return get details of a cart as a bean
     */
    public static CartB parseCart(String json) {
        CartB cartB = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String cart_id = jsonObject.getString("cart_id");
            String call_type = jsonObject.getString("call_type");
            int cartQuantity = jsonObject.getInt("cart_quantity");
            String total_price = jsonObject.getString("total_price");
            //todo:put has condition
            String id_default_add = null;
            if (jsonObject.has("id_default_address")) {
                id_default_add = jsonObject.getString("id_default_address");
            }


            cartB = new CartB();
            cartB.setCart_id(cart_id);
            cartB.setCall_type(call_type);
            cartB.setCart_quantity(cartQuantity);
            cartB.setTotal_price(total_price);
            if (id_default_add != null && id_default_add.length() > 0) {
                cartB.setId_defaultAddress(id_default_add);
            }


            if (call_type.equals("new") ||
                    call_type.equals("add") ||
                    call_type.equals("update") ||
                    call_type.equals("delete")) {
                String msg = jsonObject.getString("msg");
                if (call_type.equals("add") ||
                        call_type.equals("new")) {
                    cartB.setProduct_quantity(jsonObject.getInt("prod_quantity"));
                    cartB.setProd_name(jsonObject.getString("prod_name"));
                }
                if (call_type.equals("add") ||
                        call_type.equals("delete")) {
                    String discounts = jsonObject.getString("discounts");
                    cartB.setDiscounts(discounts);
                }
                cartB.setMsg(msg);
                return cartB;
            }

            int cartTotalQuantity = jsonObject.getInt("cart_total_quantity");
            String total_discounts = jsonObject.getString("discounts");
            String total_shipping = jsonObject.getString("total_shipping");
            String total_tax = jsonObject.getString("total_tax");
            String products = jsonObject.getString("products");
            String discounts = jsonObject.getString("discounts");

            cartB.setProducts(products);
            cartB.setCart_total_quantity(cartTotalQuantity);
            cartB.setTotal_tax(total_tax);
            cartB.setTotal_shipping(total_shipping);
            cartB.setTotal_price(total_price);
            cartB.setDiscounts(discounts);

            return cartB;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cartB;
    }

    /***
     * parsed list of products in a cart
     *
     * @param json
     * @return list of products in cart in an arraylist of a bean
     */
    public static ArrayList<CartProductB> parseCartProducts(String json) {
        ArrayList<CartProductB> arrayList = new ArrayList<>();
        try {
            JSONArray cartProdArray = new JSONArray(json);
            for (int i = 0; i < cartProdArray.length(); i++) {
                JSONObject cartProdObj = cartProdArray.getJSONObject(i);
                String prod_id = cartProdObj.getString("product_id");
                String prod_image = cartProdObj.getString("product_image");
                String prod_code = cartProdObj.getString("product_code");
                String prod_attribute = cartProdObj.getString("product_attribute");
                String prod_name = cartProdObj.getString("product_name");
                String price = cartProdObj.getString("price");
                String quantity = cartProdObj.getString("quantity");
                String total_price = cartProdObj.getString("total_price");
                String seller = cartProdObj.getString("seller");
                String delivery_date = cartProdObj.getString("delivery_date");
                String ship_charge = cartProdObj.getString("shipping_charge");
                String offer = cartProdObj.getString("offer");
                String is_sold_out = cartProdObj.getString("is_sold_out");


                CartProductB cartProductB = new CartProductB();
                cartProductB.setProduct_id(prod_id);
                cartProductB.setProduct_image(prod_image);
                cartProductB.setProduct_code(prod_code);
                cartProductB.setProduct_attribute(prod_attribute);
                cartProductB.setProduct_name(prod_name);
                cartProductB.setPrice(price);
                cartProductB.setQuantity(quantity);
                cartProductB.setTotal_price(total_price);
                cartProductB.setSeller(seller);
                cartProductB.setDelivery_date(delivery_date);
                cartProductB.setShipping_charge(ship_charge);
                cartProductB.setOffer(offer);
                cartProductB.setIs_sold_out(is_sold_out);

                arrayList.add(cartProductB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    /**
     * parse the address
     *
     * @param str the response address string to parse
     * @return the addresses in a list of arraylist of a bean
     */
    public static ArrayList<AddressB> parseAddress(String str) {
        ArrayList<AddressB> addressBs = new ArrayList<AddressB>();
        try {
            JSONObject jsonObject = new JSONObject(str);
            //    String status = jsonObject.getString("status");
            //   String callType = jsonObject.getString("call_type");
            JSONArray addressesArray = jsonObject.getJSONArray("addresses");

            for (int i = 0; i < addressesArray.length(); i++) {
                String addressFull = addressesArray.getJSONObject(i).toString();
                AddressB addressB = parseShippingAddress(addressFull);
                addressBs.add(addressB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return addressBs;
    }

    /**
     * check total product counts in cart
     *
     * @param str the response address string to parse
     * @return totproduct
     */
    public static CheckoutAddressCheck parseAddressTotProdCount(String str) {
        CheckoutAddressCheck checkoutAddressCheck = null;
        try {
            JSONObject jsonObject = new JSONObject(str);
            checkoutAddressCheck = new CheckoutAddressCheck();
            if (jsonObject.has("nb_products")) {
                checkoutAddressCheck.setNbProducts(jsonObject.getString("nb_products"));
            } else {
                checkoutAddressCheck.setNbProducts("0");
            }
            if (jsonObject.has("call_type")) {
                checkoutAddressCheck.setCallType(jsonObject.getString("call_type"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return checkoutAddressCheck;
    }

    /***
     * @param response
     * @return
     */
    public static String parseEmail(String response) {
        String email_status = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            email_status = jsonObject.getString("email_status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return email_status;
    }

    /***
     * parse payment method status
     *
     * @param json response string
     * @return payment method status as a bean
     */
    public static PaymentMethodStatus parsePaymentMethodStatus(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String codEnabled = jsonObject.getString("codEnabled");
            String online_payment_enabled = jsonObject.getString("online_payment_enabled");
            String payments = jsonObject.getString("payments");
            String total_price = jsonObject.getString("total_price");
            String nb_products = jsonObject.getString("nb_products");
            String total_advance_price = jsonObject.getString("total_advance_price");
            String total_later_price = jsonObject.getString("total_later_price");

            PaymentMethodStatus paymentMethodStatus = new PaymentMethodStatus();
            paymentMethodStatus.setNb_products(nb_products);
            paymentMethodStatus.setCodEnabled(codEnabled);
            paymentMethodStatus.setOnline_payment_enabled(online_payment_enabled);
            if (!TextUtils.isEmpty(total_price)) {
                paymentMethodStatus.setTotal_price(total_price);
            } else {
                paymentMethodStatus.setTotal_price("0");
            }
            paymentMethodStatus.setPayments(payments);
            paymentMethodStatus.setTotal_advance_price(total_advance_price);
            paymentMethodStatus.setTotal_later_price(total_later_price);

            return paymentMethodStatus;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<PaymentMethodB> parsePaymentMethodsPP(String json, String paycompCart) {
        ArrayList<PaymentMethodB> payMethodList = new ArrayList<PaymentMethodB>();
        try {
            JSONArray mainJson = new JSONArray(json);
            JSONObject availPayMethComp = new JSONObject(paycompCart);
            for (int i = 0; i < mainJson.length(); i++) {
                JSONObject availPayMethObj = mainJson.getJSONObject(i);
                String idPayment = availPayMethObj.getString("id_payment_method");
                String method_type = availPayMethObj.getString("method_type");
                String method_name = availPayMethObj.getString("method_name");
                String short_hand = availPayMethObj.getString("short_hand");
                String has_components = availPayMethObj.getString("has_components");
                String id_default_payment_module = availPayMethObj.getString("id_default_payment_module");
                String is_available_for_part_payment = availPayMethObj.getString("is_available_for_part_payment");
                String tip_exists = availPayMethObj.getString("tip_exists");
                // String method_type_name = availPayMethObj.getString("method_type_name");
                // String method_type_short_hand = availPayMethObj.getString("method_type_short_hand");

                PaymentMethodB paymentMethodB = new PaymentMethodB();
                paymentMethodB.setId_payment_method(idPayment);
                paymentMethodB.setMethod_type(method_type);
                paymentMethodB.setMethod_name(method_name);
                paymentMethodB.setShort_hand(short_hand);
                paymentMethodB.setHas_components(has_components);
                paymentMethodB.setId_default_payment_module(id_default_payment_module);
                paymentMethodB.setIs_available_for_part_payment(is_available_for_part_payment);
                paymentMethodB.setTip_exists(tip_exists);
                //  paymentMethodB.setMethod_type_name(method_type_name);
                //  paymentMethodB.setMethod_type_short_hand(method_type_short_hand);
                JSONArray availPaymOptions = availPayMethComp.getJSONArray(idPayment);
                if (availPaymOptions instanceof JSONArray) {
                    paymentMethodB.setPaymentMeth_comp_json(availPaymOptions.toString());
                }
                payMethodList.add(paymentMethodB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return payMethodList;
    }

    /***
     * parse payment methods
     *
     * @param json response string of payment methods
     * @return list of payment methods as an arraylist of a bean
     */
    public static ArrayList<PaymentMethodB> parsePaymentMethods(String json) {
        ArrayList<PaymentMethodB> payMethodList = new ArrayList<PaymentMethodB>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject emiOptions = jsonObject.getJSONObject("emiPaymentMethodComponents");
            JSONObject availPayMethComp = jsonObject.getJSONObject("availablePaymentMethodsComponents");
            JSONObject availPayMethCart = jsonObject.getJSONObject("availablePaymentMethodsForCart");
            JSONObject payMethTyp = jsonObject.getJSONObject("paymentMethodType");
            JSONObject util = jsonObject.getJSONObject("util");

            Iterator<?> itr = availPayMethCart.keys();
            while (itr.hasNext()) {
                Object key = itr.next();
                JSONObject availPayMethObj = availPayMethCart.getJSONObject(key.toString());
                String idPayment = availPayMethObj.getString("id_payment_method");
                String method_type = availPayMethObj.getString("method_type");
                String method_name = availPayMethObj.getString("method_name");
                String short_hand = availPayMethObj.getString("short_hand");
                String has_components = availPayMethObj.getString("has_components");
                String id_default_payment_module = availPayMethObj.getString("id_default_payment_module");
                String is_available_for_part_payment = availPayMethObj.getString("is_available_for_part_payment");
                String tip_exists = availPayMethObj.getString("tip_exists");
                String method_type_name = availPayMethObj.getString("method_type_name");
                String method_type_short_hand = availPayMethObj.getString("method_type_short_hand");
                Object obj = availPayMethComp.get(key.toString());
                if (obj instanceof JSONArray) {
                    JSONArray jsonArray = availPayMethComp.getJSONArray(key.toString());
                    PaymentMethodB paymentMethodB = new PaymentMethodB();
                    paymentMethodB.setId_payment_method(idPayment);
                    paymentMethodB.setMethod_type(method_type);
                    paymentMethodB.setMethod_name(method_name);
                    paymentMethodB.setShort_hand(short_hand);
                    paymentMethodB.setHas_components(has_components);
                    paymentMethodB.setId_default_payment_module(id_default_payment_module);
                    paymentMethodB.setIs_available_for_part_payment(is_available_for_part_payment);
                    paymentMethodB.setTip_exists(tip_exists);
                    paymentMethodB.setMethod_type_name(method_type_name);
                    paymentMethodB.setMethod_type_short_hand(method_type_short_hand);

                    if (jsonArray instanceof JSONArray) {
                        if (idPayment.equals(AppConfig.PAY_EMI)) {
                            paymentMethodB.setPaymentEMIMeth_comp_json(emiOptions.toString());
                        }
                        paymentMethodB.setPaymentMeth_comp_json(jsonArray.toString());
                    }
                    payMethodList.add(paymentMethodB);
                } else if (idPayment.equals(AppConfig.PAY_Cash_OD)
                        || idPayment.equals(AppConfig.PAY_PARTIAL_PAYMENT)) {   //if obj not jsonarray
                    PaymentMethodB paymentMethodB = new PaymentMethodB();
                    paymentMethodB.setId_payment_method(idPayment);
                    paymentMethodB.setMethod_type(method_type);
                    paymentMethodB.setMethod_name(method_name);
                    paymentMethodB.setShort_hand(short_hand);
                    paymentMethodB.setHas_components(has_components);
                    paymentMethodB.setId_default_payment_module(id_default_payment_module);
                    paymentMethodB.setIs_available_for_part_payment(is_available_for_part_payment);
                    paymentMethodB.setTip_exists(tip_exists);
                    paymentMethodB.setMethod_type_name(method_type_name);
                    paymentMethodB.setMethod_type_short_hand(method_type_short_hand);
                    if (idPayment.equals(AppConfig.PAY_PARTIAL_PAYMENT)) {
                        if (jsonObject.has("partPaymentMethods")) {
                            JSONArray partPaymentMethods = jsonObject.getJSONArray("partPaymentMethods");
                            paymentMethodB.setPartialPayment_json(partPaymentMethods.toString());
                        }
                        paymentMethodB.setAvailPayOptions_json(availPayMethComp.toString());
                    }
                    payMethodList.add(paymentMethodB);
                } else {
                    Logger.i(TAG, key.toString() + ":" + obj.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return payMethodList;
    }

    /***
     * parse payment method components
     *
     * @param json resposne strinf
     * @return list of payment method components as an arraylist of a bean
     */
    public static ArrayList<PaymentMethodCompB> paresPayMethComponents(String json) {
        ArrayList<PaymentMethodCompB> arrayList = new ArrayList<>();
        try {
            JSONArray paymethArray = new JSONArray(json);
            for (int i = 0; i < paymethArray.length(); i++) {
                JSONObject payMethObj = paymethArray.getJSONObject(i);
                String idPaymentMethComp = payMethObj.getString("id_payment_method_component");
                String paymentMethName = payMethObj.getString("payment_method_component_name");
                String idPaymentMeth = payMethObj.getString("id_payment_method");
//                        String idPaymentP=payMethObj.getString("position");
//                        String idPayment=payMethObj.getString("active");
                //String idPayment=payMethObj.getString("id_payment_module");
                String paymentShortName = payMethObj.getString("payment_module_short_name");
                String paymentCompCode = payMethObj.getString("id_payment_component_code");

                PaymentMethodCompB paymentMethodComp = new PaymentMethodCompB();
                paymentMethodComp.setId_payment_method_component(idPaymentMethComp);
                paymentMethodComp.setPayment_method_component_name(paymentMethName);
                paymentMethodComp.setId_payment_method(idPaymentMeth);
                paymentMethodComp.setPayment_module_short_name(paymentShortName);
                paymentMethodComp.setId_payment_component_code(paymentCompCode);
                paymentMethodComp.setIsPartial(true);

                arrayList.add(paymentMethodComp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * get emi key from emi json
     *
     * @param emiJson the reponse json as string
     * @param findkey the key that we are to search
     * @return the emi key
     */
    public static String parseGetEMIKey(String emiJson, String findkey) {
        try {
            JSONObject jsonObject = new JSONObject(emiJson);
            Iterator<?> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if (key.equals(findkey)) {
                    return jsonObject.get(key).toString();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /***
     * parse EMI method componen
     *
     * @param json response string to be parsed
     * @return list of emi -payment - method component as a bean
     */
    public static ArrayList<EMIPaymentMethodComp> parseEMIPayMethComp(String json) {
        ArrayList<EMIPaymentMethodComp> arrayList = new ArrayList<>();
        JSONArray emiPaymethArray = null;
        try {
            emiPaymethArray = new JSONArray(json);
            for (int i = 0; i < emiPaymethArray.length(); i++) {
                JSONObject payMethObj = emiPaymethArray.getJSONObject(i);
                String id_emi_pay_meth_comp = payMethObj.getString("id_emi_payment_method_components");
                String emi_option_name = payMethObj.getString("emi_option_name");
                String emi_option_code = payMethObj.getString("emi_option_code");
                String no_of_month = payMethObj.getString("no_of_month");
                String id_payment_method_component = payMethObj.getString("id_payment_method_component");
                String id_payment_module = payMethObj.getString("id_payment_module");
                String rate = payMethObj.getString("rate");
                String active = payMethObj.getString("active");
                String emi_per_month = payMethObj.getString("emi_per_month");
                String total_interest_applicable = payMethObj.getString("total_interest_applicable");
                String total_amount_tobank = payMethObj.getString("total_amount_tobank");

                EMIPaymentMethodComp emiPaymentMethodComp = new EMIPaymentMethodComp();
                emiPaymentMethodComp.setId_emi_payment_method_components(id_emi_pay_meth_comp);
                emiPaymentMethodComp.setId_payment_method_component(id_payment_method_component);
                emiPaymentMethodComp.setEmi_option_code(emi_option_code);
                emiPaymentMethodComp.setEmi_option_name(emi_option_name);
                emiPaymentMethodComp.setNo_of_month(no_of_month);
                emiPaymentMethodComp.setId_payment_module(id_payment_module);
                emiPaymentMethodComp.setRate(rate);
                emiPaymentMethodComp.setActive(active);
                emiPaymentMethodComp.setEmi_per_month(emi_per_month);
                emiPaymentMethodComp.setTotal_interest_applicable(total_interest_applicable);
                emiPaymentMethodComp.setTotal_amount_tobank(total_amount_tobank);

                arrayList.add(emiPaymentMethodComp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * get the parsed payment click for payment
     *
     * @param response
     * @return details of the payment to be made
     */
    public static PayClickB parsePayClick(String response) {
        PayClickB payClickB = new PayClickB();

        try {
            JSONObject jsonObject = new JSONObject(response);
            String action = jsonObject.getString("action");
            String img_ps_dir = jsonObject.getString("img_ps_dir");
            String key = jsonObject.getString("key");
            String txnid = jsonObject.getString("txnid");
            String amount = jsonObject.getString("amount");
            String productinfo = jsonObject.getString("productinfo");
            String firstname = jsonObject.getString("firstname");
            String lastname = jsonObject.getString("lastname");
            String zipcode = jsonObject.getString("zipcode");
            String email = jsonObject.getString("email");
            String phone = jsonObject.getString("phone");
            String surl = jsonObject.getString("surl");
            String furl = jsonObject.getString("furl");
            String curl = jsonObject.getString("curl");
            String hash = jsonObject.getString("hash");
            String pg = jsonObject.getString("pg");
            String code = jsonObject.getString("code");
            String udf1 = jsonObject.getString("udf1");
            String url = jsonObject.getString("url");
            String message = jsonObject.getString("message");
            String cancel_text = jsonObject.getString("cancel_text");
            String this_path_ssl = jsonObject.getString("this_path_ssl");
            String redirect = jsonObject.getString("redirect");
            String redirection_url = jsonObject.getString("redirection_url");

            payClickB.setAction(action);
            payClickB.setImg_ps_dir(img_ps_dir);
            payClickB.setKey(key);
            payClickB.setTxnid(txnid);
            payClickB.setAmount(amount);
            payClickB.setProductinfo(productinfo);
            payClickB.setFirstname(firstname);
            payClickB.setLastname(lastname);
            payClickB.setZipcode(zipcode);
            payClickB.setEmail(email);
            payClickB.setPhone(phone);
            payClickB.setCurl(curl);
            payClickB.setSurl(surl);
            payClickB.setFurl(furl);
            payClickB.setHash(hash);
            payClickB.setPg(pg);
            payClickB.setCode(code);
            payClickB.setUdf1(udf1);
            payClickB.setUrl(url);
            payClickB.setMessage(message);
            payClickB.setCancel_text(cancel_text);
            payClickB.setThis_path_ssl(this_path_ssl);
            payClickB.setRedirect(redirect);
            payClickB.setRedirection_url(redirection_url);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return payClickB;
    }

    /***
     * get the payment information
     *
     * @param json
     * @return payment info as a bean
     */
    public static PayInfoB parsePayInfo(String json) {
        PayInfoB payInfoB = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            payInfoB = new PayInfoB();
            payInfoB.setParams(jsonObject.getString("params"));
            //   payInfoB.setPaymodule(jsonObject.getString("payModule"));
            payInfoB.setAction(jsonObject.getString("action"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return payInfoB;
    }

    /***
     * check availability of an item for a pincode
     *
     * @param json
     * @return product availability information  for the particular pincode
     */
    public static CheckPincodeB parseCheckPincode(String json) {
        CheckPincodeB checkPincodeB = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String pinCodeValid = jsonObject.getString("pincodeValid");
            String availForThisProd = jsonObject.getString("availableForThisPincode");
            String is_cod_available = jsonObject.getString("is_cod_available");
            String prodFullPayEn = jsonObject.getString("productFullPaymentEnabled");
            String prodPartPayEnabl = jsonObject.getString("productPartPaymentEnabled");
            String adv_amt = jsonObject.getString("advance_amount");
            String productCodPayEnable = jsonObject.getString("productCodPaymentEnabled");
            String pincode = jsonObject.getString("pincode");

            checkPincodeB = new CheckPincodeB();
            checkPincodeB.setPincodeValid(pinCodeValid);
            checkPincodeB.setAvailableForThisPincode(availForThisProd);
            checkPincodeB.setIs_cod_available(is_cod_available);
            checkPincodeB.setProductPartPaymentEnabled(prodPartPayEnabl);
            checkPincodeB.setAdvance_amount(adv_amt);
            checkPincodeB.setProductFullPaymentEnabled(prodFullPayEn);
            checkPincodeB.setProductCodPaymentEnabled(productCodPayEnable);
            checkPincodeB.setPincode(pincode);
            return checkPincodeB;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return checkPincodeB;
    }

    /**
     * parse discount info
     *
     * @param json response discount as stringg
     * @return discount infotmation as a bean
     */
    public static DiscountB parseDiscount(String json) {
        DiscountB discountB = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String call_type = jsonObject.getString("call_type");
            discountB = new DiscountB();
            discountB.setCall_type(call_type);
            if (jsonObject.has("has_error")) {
                String has_error = jsonObject.getString("has_error");
                discountB.setHas_error(has_error);
            }
            if (jsonObject.has("error_msg")) {
                discountB.setError_msg(jsonObject.getString("error_msg"));
            }
            if (jsonObject.has("total_price")) {
                discountB.setTotal_price(jsonObject.getString("total_price"));
            }
            if (jsonObject.has("discount_name")) {
                discountB.setDiscount_name(jsonObject.getString("discount_name"));
            }
            if (jsonObject.has("id_discount")) {
                discountB.setId_discount(jsonObject.getString("id_discount"));
            }
            if (jsonObject.has("msg")) {
                discountB.setMsg(jsonObject.getString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return discountB;
    }

    /**
     * parse the discount to be deletd
     *
     * @param json response string
     * @return discount details as a bean
     */
    public static DiscountB parseDelete(String json) {
        DiscountB discountB = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String call_type = jsonObject.getString("call_type");
            String has_error = jsonObject.getString("has_error");
            discountB = new DiscountB();
            discountB.setCall_type(call_type);
            discountB.setError_msg(has_error);
            if (jsonObject.has("error_msg")) {
                discountB.setError_msg(jsonObject.getString("error_msg"));
            }
            if (jsonObject.has("total_price")) {
                discountB.setTotal_price(jsonObject.getString("total_price"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return discountB;
    }

    /**
     * get cart discount info
     *
     * @param json
     * @return list og discounts for a cart in an arrylist of a bean
     */
    public static ArrayList<CartDiscountB> parseCartDiscount(String json) {
        ArrayList<CartDiscountB> cartDiscList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id_discount = jsonObject.getString("id_discount");
                String name = jsonObject.getString("name");
                String value = jsonObject.getString("value");
                String value_real = jsonObject.getString("value_real");
                String description = jsonObject.getString("description");

                CartDiscountB cartDiscountB = new CartDiscountB();
                cartDiscountB.setId_discount(id_discount);
                cartDiscountB.setName(name);
                cartDiscountB.setValue(value);
                cartDiscountB.setValue_real(value_real);
                cartDiscountB.setDescription(description);

                cartDiscList.add(cartDiscountB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cartDiscList;
    }

    /**
     * prepare cart ID
     *
     * @param cartId id of the cart to be generated
     * @return the jsonobject as string
     */
    public static String prepareCartIdJson(String cartId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cartId", cartId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * parse myOrder into OrderMainB
     *
     * @param resp
     * @return parsed response stored in a bean
     */
    public static OrderMainB parseMyOrderMain(String resp) {
        OrderMainB orderMainB = new OrderMainB();
        try {
            JSONObject jsonObject = new JSONObject(resp);
            String status = jsonObject.getString("status");
            JSONArray orders = jsonObject.getJSONArray("orders");

            orderMainB.setStatus(status);
            orderMainB.setOrders(orders.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderMainB;
    }

    /***
     * parse order details
     *
     * @param resp
     * @return parsed list of order-details stored in an arraylist of OrdersB bean
     */
    public static ArrayList<OrdersB> parseOrders(String resp) {
        ArrayList<OrdersB> ordersBs = new ArrayList<OrdersB>();

        try {
//            JSONObject jsonObject = new JSONObject(resp);
//            String status = jsonObject.getString("status");
            JSONArray orders = new JSONArray(resp);
            for (int i = 0; i < orders.length(); i++) {
                JSONObject obj = orders.getJSONObject(i);
                OrdersB ordersB = new OrdersB();


                String id_order = obj.getString("id_order");
                String id_cart = obj.getString("id_cart");
                String id_address_delivery = obj.getString("id_address_delivery");
                String id_address_invoice = obj.getString("id_address_invoice");
                String payment = obj.getString("payment");

                ordersB.setId_order(id_order);
                ordersB.setId_cart(id_cart);
                ordersB.setId_address_delivery(id_address_delivery);
                ordersB.setId_address_invoice(id_address_invoice);
                ordersB.setPayment(payment);

                String module = obj.getString("module");
                String recyclable = obj.getString("recyclable");
                String gift = obj.getString("gift");
                String gift_message = obj.getString("gift_message");
                String shipping_number = obj.getString("shipping_number");

                ordersB.setModule(module);
                ordersB.setRecyclable(recyclable);
                ordersB.setGift(gift);
                ordersB.setGift_message(gift_message);
                ordersB.setShipping_number(shipping_number);

                String total_discounts = obj.getString("total_discounts");
                String total_paid = obj.getString("total_paid");
                String invoice_number = obj.getString("invoice_number");
                String delivery_number = obj.getString("delivery_number");
                String invoice_date = obj.getString("invoice_date");

                ordersB.setTotal_discounts(total_discounts);
                ordersB.setTotal_paid(total_paid);
                ordersB.setInvoice_number(invoice_number);
                ordersB.setDelivery_number(delivery_number);
                ordersB.setInvoice_date(invoice_date);

                String delivery_date = obj.getString("delivery_date");
                String valid = obj.getString("valid");
                String is_minimum_payment = obj.getString("is_minimum_payment");
                String nb_products = obj.getString("nb_products");
                String id_order_state = obj.getString("id_order_state");

                ordersB.setDelivery_date(delivery_date);
                ordersB.setValid(valid);
                ordersB.setIs_minimum_payment(is_minimum_payment);
                ordersB.setNb_products(nb_products);
                ordersB.setId_order_state(id_order_state);

                String order_state = obj.getString("order_state");
                String invoice = obj.getString("invoice");
                String virtual = obj.getString("virtual");
                String orderTrackLink = obj.getString("orderTrackLink");
                JSONArray products = obj.getJSONArray("products");

                ordersB.setOrder_state(order_state);
                ordersB.setInvoice(invoice);
                ordersB.setVirtual(virtual);
                ordersB.setOrderTrackLink(orderTrackLink);
                ordersB.setProducts(products.toString());

                ordersBs.add(ordersB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ordersBs;
    }

    /**
     * parse details of the products in an order
     *
     * @param resp
     * @return list of products in an order stored as an arraylist of OrdersProductB bean
     */
    public static ArrayList<OrdersProductB> parseOrderProducts(String resp) {
        ArrayList<OrdersProductB> ordersProductBs = new ArrayList<OrdersProductB>();
        try {
            JSONArray jsonArray = new JSONArray(resp);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String id_product_attribute = obj.getString("id_product_attribute");
                String id_product = obj.getString("id_product");
                String name = obj.getString("name");
                String description_short = obj.getString("description_short");
                String available_now = obj.getString("available_now");
                String quantity = obj.getString("quantity");
                String price = obj.getString("price");
                String minimal_quantity = obj.getString("minimal_quantity");
                String price_attribute = obj.getString("price_attribute");
                String unique_id = obj.getString("unique_id");
                String reference = obj.getString("reference");
                String supplier_reference = obj.getString("supplier_reference");
                String total = obj.getString("total");
                String id_image = obj.getString("id_image");
                JSONArray features = obj.getJSONArray("features");

                OrdersProductB ordersProductB = new OrdersProductB();
                ordersProductB.setId_product_attribute(id_product_attribute);
                ordersProductB.setId_product(id_product);
                ordersProductB.setName(name);
                ordersProductB.setDescription_short(description_short);
                ordersProductB.setAvailable_now(available_now);
                ordersProductB.setQuantity(quantity);
                ordersProductB.setPrice(price);
                ordersProductB.setMinimal_quantity(minimal_quantity);
                ordersProductB.setPrice_attribute(price_attribute);
                ordersProductB.setUnique_id(unique_id);
                ordersProductB.setReference(reference);
                ordersProductB.setSupplier_reference(supplier_reference);
                ordersProductB.setId_image(id_image);
                ordersProductB.setTotal(total);
                ordersProductB.setFeatures(features.toString());

                if (obj.has("order_status") && !TextUtils.isEmpty(obj.getString("order_status"))) {
                    JSONObject orderStatObj = obj.getJSONObject("order_status");
                    ordersProductB.setCurrentOrderStatusId(orderStatObj.getString("id_order_status"));
                    ordersProductB.setCurrentOrderStatusDesc(orderStatObj.getString("order_status_desc"));
                }

                ordersProductBs.add(ordersProductB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ordersProductBs;
    }

    /**
     * called to get product-features of a product in an order
     *
     * @param features response containing features
     * @return list of features of a product in an order stored as an arraylist of a bean called OrdersProdFeature
     */
    public static ArrayList<OrdersProdFeaturesB> parseOrderProFeatures(String features) {
        ArrayList<OrdersProdFeaturesB> bs = new ArrayList<OrdersProdFeaturesB>();
        try {
            JSONArray jsonArray = new JSONArray(features);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String id_feature = obj.getString("id_feature");
                String id_product = obj.getString("id_product");
                String id_feature_value = obj.getString("id_feature_value");

                OrdersProdFeaturesB ordersProdFeaturesB = new OrdersProdFeaturesB();
                ordersProdFeaturesB.setId_feature(id_feature);
                ordersProdFeaturesB.setId_product(id_product);
                ordersProdFeaturesB.setId_feature_value(id_feature_value);

                bs.add(ordersProdFeaturesB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bs;
    }

    /***
     * called to parse the product list of an ideaboard
     *
     * @param resp server response containing the ideaboard details
     * @return list of products stored as an arraylist of ProductDetailsB bean
     */
    public static ArrayList<ProductDetailsB> parsegetProdIdeaBoard(String resp) {
        ArrayList<ProductDetailsB> getProdIdeaBoardbs = new ArrayList<ProductDetailsB>();
        try {
            JSONObject jsonObject = new JSONObject(resp);
            String status = jsonObject.getString("status");
            String call_type = jsonObject.getString("call_type");

            JSONArray jsonArray = jsonObject.getJSONArray("product_list");
            for (int i = 0; i < jsonArray.length(); i++) {
                getProdIdeaBoardbs.add(parseProductDetails(jsonArray.getString(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getProdIdeaBoardbs;
    }

    /**
     * called to get list of ideaboard details
     *
     * @param wishlist response from server containing ideaboard details
     * @return parsed list of ideaboard details stored as an arraylist of a bean called IdaBoardItemsB
     */
    public static ArrayList<IdeaboardItemB> parseIdeaboardItems(String wishlist) {
        ArrayList<IdeaboardItemB> bs = null;
        try {
            JSONArray jsonArray = new JSONArray(wishlist);
            bs = new ArrayList<IdeaboardItemB>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                String id_wishlist = obj.getString("id_wishlist");
                String name = obj.getString("name");
                String token = obj.getString("token");
                String date_add = obj.getString("date_add");
                String date_upd = obj.getString("date_upd");
                String counter = obj.getString("counter");

                IdeaboardItemB ideaBoard = new IdeaboardItemB();
                if (obj.has("total_no_products")) {
                    ideaBoard.setTotal_no_products(obj.getString("total_no_products"));
                }
                if (obj.has("total_products")) {
                    ideaBoard.setTotal_products(obj.getString("total_products"));
                }

                ideaBoard.setId_wishlist(id_wishlist);
                ideaBoard.setName(name);
                ideaBoard.setToken(token);
                ideaBoard.setDate_add(date_add);
                ideaBoard.setDate_upd(date_upd);
                ideaBoard.setCounter(counter);

                bs.add(ideaBoard);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bs;
    }

    /**
     * called to get details of all ideaboards of a customer
     *
     * @param resp response from server containing all ideaboards of a customer
     * @return details of all ideaboards created by a customer
     */
    public static IdeaboardB parseIdeaBoard(String resp) {
        IdeaboardB b = null;
        try {
            JSONObject jsonObject = new JSONObject(resp);
            b = new IdeaboardB();

            if (jsonObject.has("wishlists")) {
                b.setWishlists(jsonObject.getString("wishlists"));
            }

            if (jsonObject.has("id_customer")) {
                b.setId_customer(jsonObject.getString("id_customer"));
            }
            if (jsonObject.has("errors")) {
                b.setErrors(jsonObject.getString("errors"));
            }
            if (jsonObject.has("call_type")) {
                b.setCall_type(jsonObject.getString("call_type"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * called to parse search results
     *
     * @param resp response from the server containing search query
     * @return parsed details of a search response sent by the server stored as the bean SearchResultB
     */
    public static SearchResultB parseSearchResult(String resp) {
        SearchResultB searchResultB = null;
        try {
            JSONObject jsonObject = new JSONObject(resp);
            Long totalProducts = jsonObject.getLong("total_products");
            Long totalPages = jsonObject.getLong("total_pages");
            String search_result = jsonObject.getString("search_result");
            String filters = jsonObject.getString("filters");
            searchResultB = new SearchResultB();
            searchResultB.setSearchResult(search_result);
            searchResultB.setTotal_pages(totalPages);
            searchResultB.setTotal_products(totalProducts);
            searchResultB.setFilters(filters);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchResultB;
    }

    /**
     * called to parse search-filters available
     *
     * @param resp response from server containing search
     * @return the total search filters available
     */
    public static SearchB parseSearchB(String resp) {
        SearchB searchB = new SearchB();
        try {
            JSONObject jsonObject = new JSONObject(resp);
            String categories = jsonObject.getString("categories");
            String attributes = jsonObject.getString("attributes");
            String features = jsonObject.getString("features");
            if (jsonObject.has("price_range")) {
                String price_ranges = jsonObject.getString("price_ranges");
                searchB.setPrice_ranges(price_ranges);
            }
            String max_price = jsonObject.getString("max_price");
            String min_price = jsonObject.getString("min_price");

            searchB.setCategories(categories);
            searchB.setAttributes(attributes);
            searchB.setFeatures(features);
            searchB.setMax_price(max_price);
            searchB.setMin_price(min_price);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchB;
    }

    /**
     * called to get details of categories available for search filters
     *
     * @param cat the category of search filter
     * @return the details of filters available for that category
     */
    public static ArrayList<SearchFiltersB> parseSearchCat(String cat) {
        ArrayList<SearchFiltersB> searchCategoryBs = null;
        try {
            JSONArray jsonArray = new JSONArray(cat);
            searchCategoryBs = new ArrayList<SearchFiltersB>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String id_category = obj.getString("id_category");
                String name = obj.getString("name");
                String result_count = obj.getString("result_count");

                SearchFiltersB catb = new SearchFiltersB();
                catb.setId(id_category);
                catb.setName(name);
                catb.setResult_count(result_count);
                catb.setIsChecked(false);

                searchCategoryBs.add(catb);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchCategoryBs;
    }

    /**
     * called to parse search attribute-details for a search-filter
     *
     * @param attri the response from server which contains all attribute details
     * @return the parsed details of a search sttribute
     */
    public static ArrayList<SearchFiltersB> parseSearchAttri(String attri) {
        ArrayList<SearchFiltersB> searchAttributes = null;
        try {
            JSONArray jsonArray = new JSONArray(attri);
            searchAttributes = new ArrayList<SearchFiltersB>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String id_attribute_group = obj.getString("id_attribute_group");
                String name = obj.getString("name");
                String is_color_group = obj.getString("is_color_group");
                String result_count = obj.getString("result_count");
                JSONArray childArray = obj.getJSONArray("child");

                SearchFiltersB attrib = new SearchFiltersB();
                attrib.setId(id_attribute_group);
                attrib.setName(name);
                attrib.setIs_color_group(is_color_group);
                attrib.setResult_count(result_count);
                attrib.setChild(childArray.toString());
                attrib.setChildCount(childArray.length());
                attrib.setIsChecked(false);
                searchAttributes.add(attrib);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchAttributes;
    }

    /**
     * called to get parsed list of search features
     *
     * @param feature response from server containing feature list of a search filter
     * @return list of filters as an arraylist of searchfilterB
     */
    public static ArrayList<SearchFiltersB> parseSearchFeature(String feature) {
        ArrayList<SearchFiltersB> searchFeatureBs = null;
        try {
            JSONArray jsonArray = new JSONArray(feature);
            searchFeatureBs = new ArrayList<SearchFiltersB>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String id_feature = obj.getString("id_feature");
                String name = obj.getString("name");
                String result_count = obj.getString("result_count");
                JSONArray childArray = obj.getJSONArray("child");

                SearchFiltersB featb = new SearchFiltersB();
                featb.setId(id_feature);
                featb.setName(name);
                featb.setResult_count(result_count);
                featb.setChild(childArray.toString());
                featb.setChildCount(childArray.length());
                featb.setIsChecked(false);

                searchFeatureBs.add(featb);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchFeatureBs;
    }

    /**
     * called to get the parsed list of price range
     *
     * @param priceRange the response from server containing price-range
     * @return list of price ranges
     */
    public static ArrayList<SearchFiltersB> parseSearchPriceRange(String priceRange) {
        ArrayList<SearchFiltersB> searchPriceBs = null;
        try {
            JSONArray jsonArray = new JSONArray(priceRange);
            searchPriceBs = new ArrayList<SearchFiltersB>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String range = obj.getString("range");
                String text = obj.getString("text");

                SearchFiltersB priceb = new SearchFiltersB();
                priceb.setRange(range);
                priceb.setName(text);

                searchPriceBs.add(priceb);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchPriceBs;
    }

    /**
     * called to get search-child-attribute
     *
     * @param child response from server
     * @return list of search-child as an arraylist of search-attribute-child
     */
    public static ArrayList<SearchChildB> parseSearchAttributeChild(String child) {
        ArrayList<SearchChildB> childBs = null;
        try {
            JSONArray jsonArray = new JSONArray(child);
            childBs = new ArrayList<SearchChildB>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String id_attribute = obj.getString("id_attribute");
                String name = obj.getString("name");
                String colr = obj.getString("color");
                String result_count = obj.getString("result_count");

                SearchChildB childB = new SearchChildB();
                childB.setId(id_attribute);
                childB.setName(name);
                childB.setColor(colr);
                childB.setResult_count(result_count);
                childB.setIsChecked(false);
                childBs.add(childB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return childBs;
    }

    /**
     * called to get search-child-attribute
     *
     * @param child response from server
     * @return list of search-child as an arraylist of search-attribute-child
     */
    public static ArrayList<SearchChildB> parseSearchFeatureChild(String child) {
        ArrayList<SearchChildB> childBs = null;
        try {
            JSONArray jsonArray = new JSONArray(child);
            childBs = new ArrayList<SearchChildB>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String id_attribute = obj.getString("id_feature_value");
                String name = obj.getString("name");
                //  String colr = obj.getString("color");
                String result_count = obj.getString("result_count");

                SearchChildB childB = new SearchChildB();
                childB.setId(id_attribute);
                childB.setName(name);
                //  childB.setColor(colr);
                childB.setIsChecked(false);
                childB.setResult_count(result_count);
                childBs.add(childB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return childBs;
    }

    /**
     * called to get a list of attributes of a group
     *
     * @param group response from server containing group-attributes
     * @return list of attributes as an arraylist of AttributeGrpB bean
     */
    public static ArrayList<AttributeGrpB> parseAttriGrp(String group) {
        ArrayList<AttributeGrpB> attributeGrpBs = null;
        try {
            JSONObject jsonObject = new JSONObject(group);
            attributeGrpBs = new ArrayList<AttributeGrpB>();
            Iterator<String> iterator = jsonObject.keys();

            while (iterator.hasNext()) {

                String current = iterator.next();
                JSONObject currentObj = jsonObject.getJSONObject(current);
                String name = currentObj.getString("name");
                String is_color_group = currentObj.getString("is_color_group");
                String defaultStr = currentObj.getString("default");
                String attributes = currentObj.getString("attributes");

                AttributeGrpB attributeGrpB = new AttributeGrpB();
                attributeGrpB.setId(current);
                attributeGrpB.setGrp_name(name);
                attributeGrpB.setGrp_is_color_group(is_color_group);
                attributeGrpB.setGrp_default(defaultStr);
                attributeGrpB.setGrp_attributes(attributes);

                attributeGrpBs.add(attributeGrpB);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attributeGrpBs;
    }

    /***
     * called to get list of group attributes
     *
     * @param grpAttribute response from server containing group-attributes
     * @return list of group-attributes as an arraylist of a bean : AttributeGrpAttriB
     */
    public static ArrayList<AttributeGrpAttriB> parseAttriGrpAttri(String grpAttribute) {
        ArrayList<AttributeGrpAttriB> bs = null;
        try {
            JSONObject attributes = new JSONObject(grpAttribute);
            bs = new ArrayList<AttributeGrpAttriB>();
            Iterator<String> stringIterator = attributes.keys();
            while (stringIterator.hasNext()) {

                String key = stringIterator.next();
                JSONObject attribObj = attributes.getJSONObject(key);
                String attribute_name = attribObj.getString("attribute_name");
                String attribute_quantity = attribObj.getString("attribute_quantity");
                AttributeGrpAttriB b = new AttributeGrpAttriB();
                b.setId_attribute(key);
                b.setAttribute_name(attribute_name);
                b.setAttribute_quantity(attribute_quantity);
                if (attribObj.has("color_value")) {
                    b.setColor_value(attribObj.getString("color_value"));
                }

                bs.add(b);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bs;
    }

    /**
     * called to get the HashMap of market price and the price offered by MEBELKART
     *
     * @param attriMapping response from server containing attributes as a string
     * @return HashMap of the Market Price and MKart Price
     */
    public static HashMap<String, AttributeMappingB> parseAttributeMapping(String attriMapping) {
        HashMap<String, AttributeMappingB> bs = null;
        try {
            JSONObject jsonObject = new JSONObject(attriMapping);
            bs = new HashMap<String, AttributeMappingB>();
            Iterator<String> iter = jsonObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                JSONObject attribMapObj = jsonObject.getJSONObject(key);
                String id = attribMapObj.getString("id");
                String our_price = attribMapObj.getString("our_price");
                String mkt_price = attribMapObj.getString("mkt_price");


                AttributeMappingB b = new AttributeMappingB();
                b.setId(id);
                b.setAttributeId(key);
                b.setMkt_price(mkt_price);
                b.setOur_price(our_price);

                bs.put(key, b);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bs;
    }

    /**
     * called to get details of an order
     *
     * @param resp response from the server containing details of an order
     * @return parsed details of an order stored as the bean: MyOrderDetailsB
     */
    public static MyOrderDetailsB parseMyOrderDetails(String resp) {
        MyOrderDetailsB b = null;
        try {
            JSONObject jsonObject = new JSONObject(resp);
            String discounts = jsonObject.getString("discounts");
            String shippingAddress = jsonObject.getString("shippingAddress");
            String order_id = jsonObject.getString("order_id");
            String order_date = jsonObject.getString("order_date");
            String total_products = jsonObject.getString("total_products");
            String total_discount = jsonObject.getString("total_discount");
            String total_paid = jsonObject.getString("total_paid");
            String total_to_pay = jsonObject.getString("total_to_pay");
            String paymentMode = jsonObject.getString("paymentMode");
            String orderIdState = jsonObject.getString("id_order_state");
            String orderTrackLink = jsonObject.getString("orderTrackLink");
            String paymentStatus = jsonObject.getString("order_state");
            String products = jsonObject.getString("products");

            b = new MyOrderDetailsB();
            b.setDiscounts(discounts);
            b.setShippingAddress(shippingAddress);
            b.setOrder_id(order_id);
            b.setOrder_date(order_date);
            b.setTotal_products(total_products);
            b.setTotal_discount(total_discount);
            b.setTotal_paid(total_paid);
            b.setPaymentMode(paymentMode);
            b.setOrderIdState(orderIdState);
            b.setOrderTrackLink(orderTrackLink);
            b.setPaymentStatus(paymentStatus);
            b.setProducts(products);
            b.setTotal_to_pay(total_to_pay);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * parse shipping address
     *
     * @param addressStr response from server containing shipping address
     * @return details of shipping address parsed and stored as the bean: AddressB
     */
    public static AddressB parseShippingAddress(String addressStr) {
        //  String alias = addObject.getString("alias");
        AddressB addressB = null;

        try {
            JSONObject addObject = new JSONObject(addressStr);
            String name = addObject.getString("name");
            String company = addObject.getString("company");
            String address = addObject.getString("address");
            String landmark = addObject.getString("landmark");
            String postcode = addObject.getString("postcode");
            String city = addObject.getString("city");
            String phone_mobile = addObject.getString("phone_mobile");
            String is_guest_address = addObject.getString("is_guest_address");
            String state = addObject.getString("state");

            addressB = new AddressB();

            if (addObject.has("id_address")) {
                String id_address = addObject.getString("id_address");
                addressB.setId_address(id_address);
            }
            if (addObject.has("deleted")) {
                String deleted = addObject.getString("deleted");
                addressB.setDeleted(deleted);
            }

            //   addressB.setAlias(alias);
            addressB.setName(name);
            addressB.setCompany(company);
            addressB.setAddress1(address);
            addressB.setAddress2(landmark);
            addressB.setPostcode(postcode);
            addressB.setCity(city);
            addressB.setPhone_mobile(phone_mobile);
            addressB.setState(state);
            addressB.setIsGuest(is_guest_address);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return addressB;
    }

    /**
     * called to get list of product-reviews od a product
     *
     * @param respReviews response from server containing product - reviews
     * @return list of detailed-reviews of a particular product
     */
    public static ArrayList<ProdReviewsB> parseReviews(String respReviews) {
        ArrayList<ProdReviewsB> prodReviewsBs = null;
        ProdReviewsB prodReviewsB = null;
        try {
            JSONArray jsonArray = new JSONArray(respReviews);
            prodReviewsBs = new ArrayList<ProdReviewsB>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String title = jsonObject.getString("title");
                String content = jsonObject.getString("content");
                String rating = jsonObject.getString("rating");
                String adddate = jsonObject.getString("adddate");
                String customername = jsonObject.getString("customername");

                prodReviewsB = new ProdReviewsB();
                prodReviewsB.setTitle(title);
                prodReviewsB.setContent(content);
                prodReviewsB.setRating(rating);
                prodReviewsB.setAdddate(adddate);
                prodReviewsB.setCustomername(customername);

                prodReviewsBs.add(prodReviewsB);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return prodReviewsBs;
    }

    public static ProductImageB parseProdImage(String jsonImage) {
        ProductImageB productImageB = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonImage);
            String large = jsonObject.getString("id_image_large");
            String xlarge = jsonObject.getString("id_image_xlarge");

            productImageB = new ProductImageB();
            productImageB.setlarge(large);
            productImageB.setxlarge(xlarge);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productImageB;
    }

    public static AppConfigB parseAppConfig(String jsonResponse) {
        AppConfigB appConfigB = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            String baseUrl = jsonObject.getString("base_url");
            String basePath = jsonObject.getString("base_path");
            String selectedCity = jsonObject.getString("selected_city");
            String payPP = jsonObject.getString("pay_pp");
            String payCOD = jsonObject.getString("pay_cod");
            String payEMI = jsonObject.getString("pay_emi");
            String payCC = jsonObject.getString("pay_cc");
            String payDC = jsonObject.getString("pay_dc");
            String payNB = jsonObject.getString("pay_nb");
            String payMI = jsonObject.getString("pay_mi");
            String payCA = jsonObject.getString("pay_ca");
            String payWT = jsonObject.getString("pay_wt");
            String payOF = jsonObject.getString("pay_of");
            String payCC1 = jsonObject.getString("pay_cc_1");
            String payCC2 = jsonObject.getString("pay_cc_2");
            String payCC3 = jsonObject.getString("pay_cc_3");
            String payCC4 = jsonObject.getString("pay_cc_4");
            String payDC1 = jsonObject.getString("pay_dc_1");
            String payDC2 = jsonObject.getString("pay_dc_2");
            String payDC3 = jsonObject.getString("pay_dc_3");
            String payWallet1 = jsonObject.getString("pay_wallet_1");
            String payWallet2 = jsonObject.getString("pay_wallet_2");
            String payWallet3 = jsonObject.getString("pay_wallet_3");
            String paymentStatus = jsonObject.getString("payment_status");
            String paymentSuccess = jsonObject.getString("payment_success");
            String paymentFailed = jsonObject.getString("payment_failed");
            String paymentFailedOrder = jsonObject.getString("payment_failed_order");
            String paymentFailedOrderDetail = jsonObject.getString("payment_failed_order_detail");

            appConfigB = new AppConfigB();
            if (TextUtils.isEmpty(baseUrl)) {
                return null;
            } else if (TextUtils.isEmpty(basePath)) {
                return null;
            } else if (TextUtils.isEmpty(selectedCity)) {
                return null;
            } else if (TextUtils.isEmpty(payPP)) {
                return null;
            } else if (TextUtils.isEmpty(payCOD)) {
                return null;
            } else if (TextUtils.isEmpty(payEMI)) {
                return null;
            } else if (TextUtils.isEmpty(payCC)) {
                return null;
            } else if (TextUtils.isEmpty(payDC)) {
                return null;
            } else if (TextUtils.isEmpty(payNB)) {
                return null;
            } else if (TextUtils.isEmpty(payMI)) {
                return null;
            } else if (TextUtils.isEmpty(payCA)) {
                return null;
            } else if (TextUtils.isEmpty(payWT)) {
                return null;
            } else if (TextUtils.isEmpty(payOF)) {
                return null;
            } else if (TextUtils.isEmpty(payCC1)) {
                return null;
            } else if (TextUtils.isEmpty(payCC2)) {
                return null;
            } else if (TextUtils.isEmpty(payCC3)) {
                return null;
            } else if (TextUtils.isEmpty(payCC4)) {
                return null;
            } else if (TextUtils.isEmpty(payDC1)) {
                return null;
            } else if (TextUtils.isEmpty(payDC2)) {
                return null;
            } else if (TextUtils.isEmpty(payDC3)) {
                return null;
            } else if (TextUtils.isEmpty(payWallet1)) {
                return null;
            } else if (TextUtils.isEmpty(payWallet2)) {
                return null;
            } else if (TextUtils.isEmpty(payWallet3)) {
                return null;
            } else if (TextUtils.isEmpty(paymentStatus)) {
                return null;
            } else if (TextUtils.isEmpty(paymentSuccess)) {
                return null;
            } else if (TextUtils.isEmpty(paymentFailed)) {
                return null;
            } else if (TextUtils.isEmpty(paymentFailedOrder)) {
                return null;
            } else if (TextUtils.isEmpty(paymentFailedOrderDetail)) {
                return null;
            } else {
                appConfigB.setBASE_URL(baseUrl);
                appConfigB.setBASE_PATH(basePath);
                appConfigB.setSELECTED_CITY(selectedCity);
                appConfigB.setPAY_CC(payCC);
                appConfigB.setPAY_DC(payDC);
                appConfigB.setPAY_NB(payNB);
                appConfigB.setPAY_MI(payMI);
                appConfigB.setPAY_CA(payCA);
                appConfigB.setPAY_WT(payWT);
                appConfigB.setPAY_OF(payOF);
                appConfigB.setPAY_PARTIAL_PAYMENT(payPP);
                appConfigB.setPAY_Cash_OD(payCOD);
                appConfigB.setPAY_EMI(payEMI);
                appConfigB.setPAY_CC_1(payCC1);
                appConfigB.setPAY_CC_2(payCC2);
                appConfigB.setPAY_CC_3(payCC3);
                appConfigB.setPAY_CC_4(payCC4);
                appConfigB.setPAY_DC_1(payDC1);
                appConfigB.setPAY_DC_2(payDC2);
                appConfigB.setPAY_DC_3(payDC3);
                appConfigB.setPAY_WALLET_1(payWallet1);
                appConfigB.setPAY_WALLET_2(payWallet2);
                appConfigB.setPAY_WALLET_3(payWallet3);
                appConfigB.setPAYMENT_STATUS(paymentStatus);
                appConfigB.setPAYMENT_SUCCESS(paymentSuccess);
                appConfigB.setPAYMENT_FAILED(paymentFailed);
                appConfigB.setPAYMENT_FAILED_ORDER(paymentFailedOrder);
                appConfigB.setPAYMENT_FAILED_ORDER_DETAIL(paymentFailedOrderDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appConfigB;
    }

    /**
     * To parse the similar Products
     *
     * @param resp - response from Products detail API
     * @return ArrayList<ProductListItemB>
     */
    public static ArrayList<ProductListItemB> parseSimilarProd(String resp) {
        return RestResponse.parseProductListItemB(resp);
    }

    public static PushB paresePush(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            String type = jsonObject.getString("type");
            String title = jsonObject.getString("title");
            String msgTitle = jsonObject.getString("msgTitle");
            String id = jsonObject.getString("id");
            String img_url = jsonObject.getString("img_url");

            PushB pushB = new PushB();
            pushB.setType(type);
            pushB.setTitle(title);
            pushB.setId(id);
            pushB.setImage_url(img_url);
            pushB.setMsgTitle(msgTitle);
            return pushB;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}