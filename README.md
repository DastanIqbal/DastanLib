# DastanLib
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
<img src="https://img.shields.io/badge/license-MIT-green.svg?style=flat">
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

Developed this library to reduce boiler plate code.
Tried to add all basic methods or code which is require for almost all apps,
so we don't need to write and just call with single line. This library can save your time so you can foucs on other things. 

### Use ready made code
Extend Application class with DastanLibApp if not using Ads Module
```
open class App : DastanLibApp() {}
```

Extend Application class with DastanAdsApp if using Ads Module
```
open class App : DastanAdsApp() {}
```

#### Common Utitlity Methods 
```
implementation "com.dastanapps.dastanlib:common:0.3.2-alpha"
```

To Implement Push Notification just register your receiver and you will get the push
```
class PushNotificatinoMsgsReceiver : BroadcastReceiver() {
    private val TAG = this::class.java.simpleName
    override fun onReceive(ctxt: Context, intent: Intent?) {
        intent?.run {
            val msg = intent.getStringExtra("msg")
            Logger.d(TAG, msg)
            //Extract data
            CommonUtils.openNotification2(PushNotificationB(title,content,banner_url,push_type))
        }
    }
 }

```
To receive Push token
```
class SendNewTokenReceiver : BroadcastReceiver() {
    private val TAG = this::class.java.simpleName
    override fun onReceive(ctxt: Context, intent: Intent?) {
        if (DastanLibApp.INSTANCE.isRelease)
            intent?.getStringExtra("token")?.let { token ->
            val bundle = Bundle().apply {
                    putString("TOKEN", token)
            }
            //Sent token to server
            AppWorkManager.networkWorker(AppWorkManager.TASK_SENDTOKEN, bundle)
        }
     }
}
```

In `App` register Reciver
```
open class App : DastanAdsApp() {
       override fun onCreate() {
              super.onCreate()
              INSTANCE = this
              LocalBroadcastManager.getInstance(this)
                            .registerReceiver(
                                    PushNotificatinoMsgsReceiver(),
                                    IntentFilter(SendBroadcast.GCM_MESSAGE)
              LocalBroadcastManager.getInstance(this)
                .registerReceiver(
                        SendNewTokenReceiver(),
                        IntentFilter(getString(R.string.token_refresh_broadcast))
                )
      }
}
```

Thats it, you will start receving Push and Token in both Receiver. Simple!!

Use `DGson` for converting Json to Object, and Object to Json.
Common utility class `Bitmap, DateTime, Display, File, Network etc`
To find more check `:common` module

#### UI Utility Methods
```
implementation "com.dastanapps.dastanlib:ui:0.1.4-alpha"
```

Call readymade method, Eg:
Just to load webpage use `DWebView` for eg:
```
  startActivity(Intent(this, DWebView::class.java).apply {
                        putExtra(DWebView.DWEBVIEW_URL, "http://privacypolicy.com/privacypolicy")
                    })
```

Date and Time Picker use `DatePickerFragment`,`TimePickerFragment`
```
     private val datePickerFragment by lazy { DatePickerFragment() }
     datePickerFragment.defaultFmt = "yyyy-MM-dd"
     datePickerFragment.idatepickerresult = object : DatePickerFragment.IDatePickerResult {
            override fun onDateSet(date: String, mCalendar: Calendar) {
                val currentDate = DateTimeUtils.currentDate("yyyy-MM-dd")
            }
        }
     datePickerFragment.show(requireFragmentManager(), "Date Picker")   
```
Launch LoggerActivity to see log files. 

To show Toast or Snackbar
```
 ViewUtils.showToast(ctxt, "This is Toast")
 
 ViewUtils.showSnack("This is Snack, viewGroup(FrameLayout))
```

To find more method check `:ui` module

#### Integrade FacebookAds,AdMob and others
```
implementation 'com.dastanapps.dastanlib:ads:0.1.4-alpha'
```

Check `AdsConfiguration` class to configure Facebook and Admob Ads
For eg for AdMob:
```
open class App : DastanAdsApp() {

    override fun onCreate() {
        super.onCreate()
        //Admob
        adsConfiguration.adMobAppId = getString(R.string.admob_app_id)
        adsConfiguration.adMobRewardId = getString(R.string.admob_reward_ad)
        adsConfiguration.adMobBannerId = getString(R.string.admob_ads_banner)
        adsConfiguration.adMobInterstialAd = getString(R.string.admob_ads_interstial)
        adsConfiguration.adMobNativeAd = getString(R.string.admob_ads_native)
 }
```

Use ads where want to use in class using `DastanAdsApp.adsInstance().getAdMobAds()` instance
```
val admobads = DastanAdsApp.adsInstance().getAdMobAds()
protected fun bannerAds(view: ViewGroup) {
    admobads.loadBannerAds("banner", DastanAdsApp.INSTANCE.adsConfiguration.adMobBannerId!!)
    admobads.setAdsListener(object : IMarvelAds {
        override fun adLoaded(adLoaded: Any) {
            Logger.onlyDebug("adMob:banner:adLoaded")
            view.removeAllViews()
            view.addView(adLoaded as View)
        }

        override fun adError(error: String) {
            Logger.onlyDebug("adMob:banner:adError")
        }

        override fun adDismissed(tag: String) {
            Logger.onlyDebug("adMob:banner:adDismissed")
        }

        override fun addDisplayed() {
            Logger.onlyDebug("adMob:banner:adDisplayed")
        }
    }, "banner")
}

protected fun interstitialAds() {
    admobads.loadInterstialAd("Interstitial")
    admobads.setAdsListener(object : IMarvelAds {

        override fun adLoaded(adLoaded: Any) {
            Logger.onlyDebug("adMob:Interstitial:adLoaded")
            admobads.showInterstialAd("Interstitial", adLoaded)
        }

        override fun adError(error: String) {
            Logger.onlyDebug("adMob:Interstitial:adError")
        }

        override fun adDismissed(tag: String) {
            Logger.onlyDebug("adMob:Interstitial:adDismissed")
        }

        override fun addDisplayed() {
            Logger.onlyDebug("adMob:Interstitial:adDisplayed")
        }

    }, "Interstitial")
 }
```

