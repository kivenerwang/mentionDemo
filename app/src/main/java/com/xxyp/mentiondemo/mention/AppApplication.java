package com.xxyp.mentiondemo.mention;

import android.app.Application;

public class AppApplication extends Application {
    public static Application sApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static Application getsApplication() {
        return sApplication;
    }
}
