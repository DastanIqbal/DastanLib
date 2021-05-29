
/*
 * Copyright (C) 2016 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dastanapps.dastanlib.ads.mediation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The {@link AdMobRewardedVideoAd} class is a simple rewarded video ad object. The sample
 * rewarded video ad is parcelable, so an instance of it can be passed between activities.
 */
public class AdMobRewardedVideoAd implements Parcelable {

    /**
     * A {@link Creator}, needed for an object to be parcelable.
     */
    public static final Creator<AdMobRewardedVideoAd> CREATOR =
            new Creator<AdMobRewardedVideoAd>() {
                @Override
                public AdMobRewardedVideoAd createFromParcel(Parcel in) {
                    return new AdMobRewardedVideoAd(in);
                }

                @Override
                public AdMobRewardedVideoAd[] newArray(int size) {
                    return new AdMobRewardedVideoAd[size];
                }
            };

    /**
     * A simple name for the rewarded video ad.
     */
    private final String adName;

    /**
     * The rewarded amount associated with this native ad.
     */
    private final int rewardAmount;

    /**
     * @return {@link #adName}.
     */
    public String getAdName() {
        return this.adName;
    }

    /**
     * @return {@link #rewardAmount}.
     */
    public int getRewardAmount() {
        return this.rewardAmount;
    }

    /**
     * Constructor with parcel needed for an object to be parcelable.
     *
     * @param in an Android {@link Parcel} object.
     */
    public AdMobRewardedVideoAd(Parcel in) {
        this.adName = in.readString();
        this.rewardAmount = in.readInt();
    }

    /**
     * The default constructor for sample rewarded video ad. Requires a name and reward amount.
     *
     * @param adName       a simple name for the rewarded video ad.
     * @param rewardAmount reward amount to be provided when a user completes watching this ad.
     */
    AdMobRewardedVideoAd(String adName, int rewardAmount) {
        this.adName = adName;
        this.rewardAmount = rewardAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(adName);
        dest.writeInt(rewardAmount);
    }
}