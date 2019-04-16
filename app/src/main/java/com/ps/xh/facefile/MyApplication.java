package com.ps.xh.facefile;

import android.app.Application;

import cn.bmob.v3.Bmob;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "f739bf6ff37fce4a3400ba2e32961be9");
    }
}
