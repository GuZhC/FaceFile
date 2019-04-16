package com.ps.xh.facefile.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.ps.xh.facefile.R;


public class HLoading extends Dialog {


    private Context context;
    TextView avi;

    public HLoading(@NonNull Context context) {
        super(context, R.style.progress);
        this.context = context;
        init();
    }

    private void init(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_hloading);

//        avi = findViewById(R.id.tv_hloading);
        //设置SelectPicPopupWindow弹出窗体的背景
        getWindow().setBackgroundDrawableResource(R.color.transparent);

    }


    @Override
    public void onBackPressed() {
        dismiss();
    }
}
