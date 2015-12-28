package com.wmtw.clinic;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class MyApp extends Application {
    private static final String APPLICATION_ID = "gnBP24fXzjuNBKNAAVfOAFtkw8b003BOQXNnNHCS";
    private static final String CLIENT_KEY = "NWgXdcp9JxbTLhELmurFGiRHLNhroHUoceBMEkLB";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
