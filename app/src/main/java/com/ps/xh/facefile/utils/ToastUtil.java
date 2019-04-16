package com.ps.xh.facefile.utils;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.ps.xh.facefile.R;

import java.util.ArrayList;



/**
 * Toast工具类
 * 解决在5.0后某些机型上关闭通知权限Toast不显示问题
 */
public class ToastUtil {
    private static final String TAG = "ToastUtil";
    private static Toast toast;
    private static ArrayList<ToastInterceptor> interceptors = new ArrayList<>();

    private static Context getAppContext(@NonNull Context context) {
        return context.getApplicationContext();
    }

    public static void show(@NonNull Context context, @NonNull String content, int time) {
        boolean interceptor = checkIsInterceptor(content);
        if (interceptor) {
            //拦截该toast
            return;
        }
        if (toast == null) {
            toast = new Toast(getAppContext(context));
        }
        View inflate = View.inflate(getAppContext(context), R.layout.toast_view, null);
        ((TextView) inflate.findViewById(R.id.toast_tv)).setText(content);
        inflate.setAnimation(AnimationUtils.loadAnimation(getAppContext(context), R.anim.app_msg_in));
        toast.setView(inflate);
        toast.setGravity(Gravity.BOTTOM, 0, 350);
        toast.setDuration(time);
        toast.show();
    }

    public static void show(@NonNull Context context, @NonNull String content) {
        boolean interceptor = checkIsInterceptor(content);
        if (interceptor) {
            //拦截该toast
            return;
        }
        if (toast == null) {
            toast = new Toast(getAppContext(context));
        }
        View inflate = View.inflate(getAppContext(context), R.layout.toast_view, null);
        ((TextView) inflate.findViewById(R.id.toast_tv)).setText(content);
        inflate.setAnimation(AnimationUtils.loadAnimation(getAppContext(context), R.anim.app_msg_in));
        toast.setView(inflate);
        toast.setGravity(Gravity.BOTTOM, 0, 350);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void addInterceptor(ToastInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public static void removeInterceptor(ToastInterceptor interceptor) {
        interceptors.remove(interceptor);
    }

    public static void clearInterceptor() {
        interceptors.clear();
    }

    private static boolean checkIsInterceptor(String content) {
        if (TextUtils.isEmpty(content)) {
            return true;
        }
        if (interceptors == null || interceptors.size() == 0) {
            return false;
        }
        for (ToastInterceptor interceptor:
             interceptors) {
            boolean b = interceptor.interceptorToast(content);
            if (b) {
                return true;
            }
        }
        return false;
    }


    /**
     * toast 拦截器
     */
    public interface ToastInterceptor {
        /**
         *
         * 是否拦截当前toast
         * @param content toast 内容
         * @return 如果toast到内容不符合用户自定义的规范，将不弹toast
         */
        boolean interceptorToast(String content);
    }


}
