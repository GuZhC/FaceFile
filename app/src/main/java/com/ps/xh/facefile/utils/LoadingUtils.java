package com.ps.xh.facefile.utils;

import android.content.Context;
import android.content.DialogInterface;

public class LoadingUtils {

    private static HLoading loading = null;

    public static void showLoading(Context context) {
        if (loading == null) {
            loading = new HLoading(context);
        }
        loading.show();
    }

    public static void hideLoading() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    public static void onhideLoading(DialogInterface.OnDismissListener listener) {
        if (loading != null && loading.isShowing()) {
            loading.setOnDismissListener(listener);
        }
    }

    /**
     * 在Activity的 onCreate 为了保证此dialog 属于此Activity！！！！！ 解决隐藏BUG
     * 在destory 防止内存泄漏
     */
    public static void onDestory() {
        if (loading != null) {
            loading = null;
        }
    }


}
