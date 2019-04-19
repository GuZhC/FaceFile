package com.ps.xh.facefile.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class FaceHttp {

//    public void getCode() {
//        RetrofitUtil.getInstance().initRetrofit().getVerityCode().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseBody>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(ResponseBody value) {
//                        InputStream is=value.byteStream();
//                        Bitmap bitmap = BitmapFactory.decodeStream(is);
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
