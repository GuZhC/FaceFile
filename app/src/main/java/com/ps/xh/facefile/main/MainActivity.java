package com.ps.xh.facefile.main;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ps.xh.facefile.R;
import com.ps.xh.facefile.base.BaseActivity;
import com.ps.xh.facefile.login.LoginActivity;
import com.ps.xh.facefile.utils.SPUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_img_empty)
    ImageView mainImgEmpty;
    @BindView(R.id.main_tv_chose)
    TextView mainTvChose;
    @BindView(R.id.main_rl_empty)
    RelativeLayout mainRlEmpty;
    private ArrayList<String> docPaths;
    private TextView name;

    @Override
    protected int addContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setupNavMenu();
    }

    /**
     * 设置侧边栏
     */
    private void setupNavMenu() {
        navigationView.setNavigationItemSelectedListener(this);
        View headView = navigationView.getHeaderView(0);
        name = headView.findViewById(R.id.tv_name);
        name.setText(SPUtils.read(this, "USER", "PHONE"));
    }

    /**
     * 侧边栏点击事件
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.navItemLogout:
                startAct(LoginActivity.class);
                finish();
                return true;
            case R.id.navItemLock:
                toast("lock");
                return true;
            case R.id.navItemface:
                toast("navItemface");
                return true;
            default:
                return false;
        }
    }


    @OnClick(R.id.main_tv_chose)
    public void onViewClicked() {
        choseFile();
    }

    /**
     * 文件选择
     */
    private void choseFile() {
        ArrayList<String> selectedFile = new ArrayList<>();
        selectedFile.add("/storage");
         FilePickerBuilder.Companion.getInstance().setMaxCount(5)
                .setSelectedFiles(selectedFile)
                .setActivityTheme(R.style.LibAppTheme)
                .pickFile(MainActivity.this, FilePickerConst.REQUEST_CODE_DOC);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_DOC:
                if(resultCode== Activity.RESULT_OK && data!=null) {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                }
                break;
        }
        name.setText(docPaths.toString());
    }
}
