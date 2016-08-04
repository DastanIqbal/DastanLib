package com.dastanapps.dastanlib.Analytics;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SearchEvent;

/**
 * Created by IQBAL-MEBELKART on 5/20/2016.
 */
public class DFabric {

    public static void logUser(String user, String email, String userName) {
        Crashlytics.setUserIdentifier(user);
        Crashlytics.setUserEmail(email);
        Crashlytics.setUserName(userName);
    }

    public static void log(String tag, String log) {
        Crashlytics.log(0, tag, log);
    }

    public static void logSearch(String searchTerm) {
        Answers.getInstance().logSearch(new SearchEvent()
                .putQuery("mobile analytics")
                .putCustomAttribute("Search:", searchTerm));
    }
}
