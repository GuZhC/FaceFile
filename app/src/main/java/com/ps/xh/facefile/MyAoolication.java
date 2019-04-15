package com.ps.xh.facefile;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * <P> org: 北京云玺科技有限责任公司
 * <P> @author GuZhC
 * <P> email < a href="zhangchi@yunxitech.cn">guzhongcai@yunxitech.cn</ a>
 * <P> package: com.ps.xh.facefile
 * <P> fileName:MyAoolication
 * <P> date: 2019/4/15
 * <p>
 * <P> describe:
 * <p>
 * <p>
 * <p>
 * <P> 页面编号: []
 * <P> 页面设计图参考地址: < a href="">页面设计图参考地址</ a>
 * <P> 接口编号: []
 * <P> 接口参考文档地址: < a href="">接口参考文档地址</ a>
 * <P> 参考文档: < a href="">参考文档</ a>
 * <p>
 * <P> @version 1.0
 */
public class MyAoolication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "f739bf6ff37fce4a3400ba2e32961be9");
    }
}
