package com.ps.xh.facefile.login;

import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.ps.xh.facefile.main.MainActivity;
import com.ps.xh.facefile.R;
import com.ps.xh.facefile.base.BaseActivity;
import com.ps.xh.facefile.utils.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity {


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


    @Override
    protected int addContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        String readPhone = SPUtils.read(LoginActivity.this, "USER", "PHONE");
        if (!TextUtils.isEmpty(readPhone)){
            phone.setText(readPhone);
        }
    }

    @OnClick({R.id.rbLogin, R.id.rbRegister, R.id.btnServerLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rbLogin:
                setRbBg();
                break;
            case R.id.rbRegister:
                setRbBg();
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
        final BmobUser  user = new BmobUser();
        user.setUsername(mPhone);
        user.setPassword(mPsd);
        showLoding();
        user.signUp(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser user, BmobException e) {
                hideLoading();
                if (e == null) {
                   toast("注册成功");
                    rbLogin.setChecked(true);
                    setRbBg();
                }else if (e.getErrorCode()==202){
                    toast("该账号已被注册");
                }
                else {
                    toast("注册失败:"+ e.getMessage());
                    Log.e("register",e.getErrorCode()+"|"+ e.getMessage());
                }
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
        final BmobUser user = new BmobUser();
        //此处替换为你的用户名
        user.setUsername(mPhone);
        //此处替换为你的密码
        user.setPassword(mPsd);
        showLoding();
        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                hideLoading();
                if (e == null) {
                    BmobUser user = BmobUser.getCurrentUser(BmobUser.class);
                    SPUtils.save(LoginActivity.this,"USER","PHONE",mPhone);
                    startAct(MainActivity.class);
                    finish();
                }else if (e.getErrorCode() ==101){
                    toast("帐号或密码错误");
                } else {
                    toast("登录失败"+e.getMessage());
                }
            }
        });
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

}
