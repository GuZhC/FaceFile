package com.ps.xh.facefile;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;
import okhttp3.OkHttpClient;


public class MyApplication extends Application {
    public static MyApplication myApp;
    public static final int TIMEOUT = 60;
    private static OkHttpClient mOkHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "f739bf6ff37fce4a3400ba2e32961be9");
        myApp = this;
    }

    /**
     * 全局httpclient
     *
     * @return
     */
    public static OkHttpClient initOKHttp() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)//设置写入超时时间
//                    .addInterceptor(InterceptorUtil.LogInterceptor())//添加日志拦截器
//                    //cookie
//                    .addInterceptor(new CookieReadInterceptor())
//                    .addInterceptor(new CookiesSaveInterceptor())
                    .build();
        }
        return mOkHttpClient;
    }

}
