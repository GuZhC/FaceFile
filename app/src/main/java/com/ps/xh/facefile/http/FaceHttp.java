package com.ps.xh.facefile.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class FaceHttp {

//    public void getDetect() {
//        HashMap<String,String> map = new HashMap();
//        map.put("api_key",ApiAddress.Key);
//        map.put("api_secret",ApiAddress.Secret);
//        map.put("return_attributes","gender");
//        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
//        RetrofitUtil.getInstance().initRetrofit().getDetect(map,fileBody).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<DetectBeen>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(DetectBeen detectBeen) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//                });
//    }

}
