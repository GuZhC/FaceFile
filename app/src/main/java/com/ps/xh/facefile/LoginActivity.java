package com.ps.xh.facefile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
                if (TextUtils.isEmpty(mPhone)){
                    Toast.makeText(this,"请输入电话号码",Toast.LENGTH_SHORT);
                    return;
                }
                if (TextUtils.isEmpty(mPsd)){
                    Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT);
                    return;
                }

                if (rbLogin.isChecked()){
                    login(mPhone,mPsd);
                }else {
                    register(mPhone,mPsd);
                }
                break;
        }
    }

    /**
     * 注册
     * @param mPhone
     * @param mPsd
     */
    private void register(String mPhone, String mPsd) {
        Toast.makeText(this,"登录",Toast.LENGTH_SHORT);
    }

    /**
     * 登录
     * @param mPhone
     * @param mPsd
     */
    private void login(String mPhone, String mPsd) {
        Toast.makeText(this,"注册",Toast.LENGTH_SHORT);
    }

    /**
     * 修改Rb背景
     */
    private void setRbBg() {
        if (rbLogin.isChecked()){
            rbLogin.setTextColor(getResources().getColor( R.color.black));
            rbRegister.setTextColor(getResources().getColor( R.color.white));
        }else {
            rbLogin.setTextColor(getResources().getColor( R.color.white));
            rbRegister.setTextColor(getResources().getColor( R.color.black));
        }
    }
}
