package com.ps.xh.facefile.login;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * 用户实体类
 */
public class UserBean extends BmobUser {

    public List<String> getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(List<String> faceUrl) {
        this.faceUrl = faceUrl;
    }

    private List<String> faceUrl;


}
