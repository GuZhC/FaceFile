package com.ps.xh.facefile.login;

import android.content.Context;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class BmobHelper {
    final BmobUser user;
    final Context context;
   public BmobHelper(Context context){
       user = new BmobUser();
       this.context = context;
    }

    public void login(BmobUser user ){
        user.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                if (e == null) {
                } else {
                }
            }
        });
    }

}
