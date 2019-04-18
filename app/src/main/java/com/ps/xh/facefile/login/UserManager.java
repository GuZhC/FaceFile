package com.ps.xh.facefile.login;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理 单例
 */
public class UserManager {

    private UserBean userBean;

    private UserManager() {
    }

    private static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (UserBean.class) {
                if (instance == null) {
                    instance = new UserManager();
                }
            }
        }
        return instance;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getUserName() {
        return userBean.getUsername();
    }

    public List<String> getUserFace() {
        if (userBean.getFaceUrl() == null) {
            return new ArrayList<String>();
        }
        return userBean.getFaceUrl();
    }
}
