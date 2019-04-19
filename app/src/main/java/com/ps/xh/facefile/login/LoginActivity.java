package com.ps.xh.facefile.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TResult;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.ps.xh.facefile.R;
import com.ps.xh.facefile.base.BaseActivity;
import com.ps.xh.facefile.face.FaceAddActivity;
import com.ps.xh.facefile.http.ComperBean;
import com.ps.xh.facefile.http.DetectBeen;
import com.ps.xh.facefile.main.MainActivity;
import com.ps.xh.facefile.utils.FileUtils;
import com.ps.xh.facefile.utils.SPUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity implements DialogInterface.OnDismissListener {


    @BindView(R.id.rbLogin)
    RadioButton rbLogin;
    @BindView(R.id.rbRegister)
    RadioButton rbRegister;
    @BindView(R.id.phone)
    TextInputEditText phone;
    @BindView(R.id.etPassword)
    TextInputEditText etPassword;
    @BindView(R.id.btnServerLogin)
    Button btnServerLogin;
    private String faceFile;


    @Override
    protected int addContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        addPermission();
        faceFile = Environment.getExternalStorageDirectory().toString() + "/faceFile/face";

        String readPhone = SPUtils.read(LoginActivity.this, "USER", "PHONE");
        if (!TextUtils.isEmpty(readPhone)) {
            phone.setText(readPhone);
        }
    }

    /**
     * 权限请求
     */
    @SuppressLint("CheckResult")
    private void addPermission() {
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEachCombined(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
//                            toast("ok");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            showAlertDialog();
                        } else {
                            showAlertDialog();
                        }
                    }
                });

    }

    private void showAlertDialog() {
        AlertDialog versionDialog =
                new AlertDialog.Builder(LoginActivity.this).setTitle("权限提示").setMessage(
                        "亲，请到设置页面开启权限").setPositiveButton(getString(R.string.no_p_out),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
        versionDialog.setOnDismissListener(this);
        versionDialog.setCanceledOnTouchOutside(false);
        versionDialog.setCancelable(false);
        versionDialog.show();
    }

    @OnClick({R.id.rbLogin, R.id.rbRegister, R.id.btnServerLogin,R.id.btnServerLoginFace})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rbLogin:
                setRbBg();
                break;
            case R.id.rbRegister:
                setRbBg();
                break;
                case R.id.btnServerLoginFace:
                    if (FileUtils.isFileExists(faceFile+"/normal.png")){
                        takePhto();
                    }else {
                        toast("没有可识别图片");
                    }
                break;
            case R.id.btnServerLogin:
                String mPhone = phone.getText().toString().trim();
                String mPsd = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(mPhone)) {
                    toast("请输入电话号码");
                    break;
                }
                if (TextUtils.isEmpty(mPsd)) {
                    toast("请输入密码");
                    break;
                }

                if (rbLogin.isChecked()) {
                    login(mPhone, mPsd);
                } else {
                    register(mPhone, mPsd);
                }
                break;
        }
    }

    /**
     * 注册
     *
     * @param mPhone
     * @param mPsd
     */
    private void register(String mPhone, String mPsd) {
        final UserBean user = new UserBean();
        user.setUsername(mPhone);
        user.setPassword(mPsd);
        List<String> url = new ArrayList<>();
        url.add("");
        url.add("");
        url.add("");
        user.setFaceUrl(url);
        showLoding();
        user.signUp(new SaveListener<UserBean>() {
            @Override
            public void done(UserBean user, BmobException e) {
                hideLoading();
                if (e == null) {
                    toast("注册成功");
                    rbLogin.setChecked(true);
                    setRbBg();
                } else if (e.getErrorCode() == 202) {
                    toast("该账号已被注册");
                } else {
                    toast("注册失败:" + e.getMessage());
                    Log.e("register", e.getErrorCode() + "|" + e.getMessage());
                }
            }
        });
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showLoding();
        OkGo.<String>post("https://api-cn.faceplusplus.com/facepp/v3/compare")
                .tag(this)
                .params("image_file1", new File(result.getImage().getCompressPath()))
                .params("image_file2", new File(faceFile+"/normal.png"))
                .execute(new StringCallback() {
                    /**
                     * @param response
                     */
                    @Override
                    public void onSuccess(Response<String> response) {
                        hideLoading();
                        Log.d("detect", "onSuccess: " + response.body());
                        Gson gson = new Gson();
                        ComperBean comperBean = gson.fromJson(response.body(), ComperBean.class);
                        if (comperBean.getConfidence()<90.0){
                            toast("面部匹配不成功");
                            return;
                        }
                        FileUtils.createOrExistsDir(faceFile);
                        if (FileUtils.listFilesInDir(faceFile).size()<5){
                            startAct(FaceAddActivity.class);
                            finish();
                        }else {
                            startAct(MainActivity.class);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        toast("识别错误，请重试");
                        hideLoading();
                        Log.d("detect", "onError: " + response.body());
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        System.out.println("uploadProgress: " + progress);
                    }
                });
    }

    /**
     * 登录
     *
     * @param mPhone
     * @param mPsd
     */
    private void login(final String mPhone, String mPsd) {
        final UserBean user = new UserBean();
        //此处替换为你的用户名
        user.setUsername(mPhone);
        //此处替换为你的密码
        user.setPassword(mPsd);
        showLoding();
        user.login(new SaveListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                hideLoading();
                if (e == null) {
                    loginSucess(userBean, mPhone);
                } else if (e.getErrorCode() == 101) {
                    toast("帐号或密码错误");
                } else {
                    toast("登录失败" + e.getMessage());
                }
            }
        });
    }

    /**
     * 登录成功
     * @param userBean
     * @param mPhone
     */
    private void loginSucess(UserBean userBean, String mPhone) {
        UserManager.getInstance().setUserBean(userBean);
        SPUtils.save(LoginActivity.this, "USER", "PHONE", mPhone);
        FileUtils.createOrExistsDir(faceFile);
        if (FileUtils.listFilesInDir(faceFile).size()<5){
            startAct(FaceAddActivity.class);
            finish();
        }else {
            startAct(MainActivity.class);
            finish();
        }

//        if (userBean.getFaceUrl() != null&&userBean.getFaceUrl().size() >= 3) {
//            for (String url: userBean.getFaceUrl()) {
//                if (TextUtils.isEmpty(url)) {
//                    startAct(FaceAddActivity.class);
//                    finish();
//                    return;
//                }
//            }
//            startAct(MainActivity.class);
//            finish();
//        } else {
//            startAct(FaceAddActivity.class);
//            finish();
//        }
//        finish();
    }

    /**
     * 修改Rb背景
     */
    private void setRbBg() {
        if (rbLogin.isChecked()) {
            rbLogin.setTextColor(getResources().getColor(R.color.white));
            rbRegister.setTextColor(getResources().getColor(R.color.black));
            btnServerLogin.setText(getString(R.string.login));
        } else {
            rbLogin.setTextColor(getResources().getColor(R.color.black));
            rbRegister.setTextColor(getResources().getColor(R.color.white));
            btnServerLogin.setText(getString(R.string.register));
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finish();
    }


    /**
     * 拍照
     *
     */
    private void takePhto() {
        String mPaht =  Environment.getExternalStorageDirectory().toString() + "/faceFile/other/" + System.currentTimeMillis()+".png";
        FileUtils.createOrExistsFile(mPaht);
        File file = FileUtils.getFileByPath(mPaht);
        Uri uri = Uri.fromFile(file);
        TakePhoto takePhoto = getTakePhoto();
        takePhoto.onEnableCompress(new
                        CompressConfig.Builder().setMaxSize(500 * 1024).setMaxPixel(1000).create(),
                true);
        takePhoto.onPickFromCapture(uri);
    }
}
