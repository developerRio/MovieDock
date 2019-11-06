package com.originalstocks.moviedock.Utils;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    public static MyApplication mInstance;

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


}
