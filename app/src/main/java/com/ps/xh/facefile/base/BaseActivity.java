package com.ps.xh.facefile.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.app.TakePhotoFragmentActivity;
import com.jph.takephoto.model.TResult;
import com.ps.xh.facefile.utils.LoadingUtils;
import com.ps.xh.facefile.utils.ToastUtil;

import butterknife.ButterKnife;


public abstract class BaseActivity extends TakePhotoFragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(addContentView());
        ButterKnife.bind(this);
        LoadingUtils.onDestory();
        initView();
        initData();
    }


    /**
     * 初始化数据
     */
    public void initData() {

    }

    @LayoutRes
    protected abstract int addContentView();

    protected abstract void initView();

    protected void toast(String msg){
        ToastUtil.show(this,msg);
    }

    protected void showLoding(){
        LoadingUtils.showLoading(this);
    }
    protected void hideLoading(){
        LoadingUtils.hideLoading();
    }

    protected void startAct(Intent intent){
        startActivity(intent);
    }

    protected void startAct(Class mClass){
        Intent intent1 = new Intent(this,mClass);
        startActivity(intent1);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
    }


    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        toast("拍照失败"+msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }
}
