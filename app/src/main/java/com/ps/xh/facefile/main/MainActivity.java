package com.ps.xh.facefile.main;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ps.xh.facefile.R;
import com.ps.xh.facefile.base.BaseActivity;
import com.ps.xh.facefile.login.LoginActivity;
import com.ps.xh.facefile.utils.FileUtils;
import com.ps.xh.facefile.utils.SPUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, DialogInterface.OnDismissListener {

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
    private String LockPath;
    private static final int CHOOSE_FILE_CODE = 0;
    private static final String TAG1 = "FileChoose";

    @Override
    protected int addContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setupNavMenu();
        initLockFile();

    }


    /**
     * 设置加密文件夹
     */
    private void initLockFile() {
        String sdPath = getSDPath();
        if (TextUtils.isEmpty(sdPath)) {
            hintNoSd();
        }
        LockPath = sdPath + "/faceFile/lockFile";
//        Log.e(TAG1, "LockPath: " + LockPath);
        FileUtils.createOrExistsDir(LockPath);
        List<File> sdLockFiles = FileUtils.listFilesInDir(LockPath);
        if (sdLockFiles.size() <= 0) {
            toast("暂无加密文件");
        }
    }

    /**
     * 没有Sd卡
     */
    private void hintNoSd() {
        AlertDialog versionDialog = new AlertDialog.Builder(this).setTitle("提示").setMessage(
                "亲，木有检测到sd" +
                "卡啊-_-!!").setPositiveButton(getString(R.string.no_sd_out),
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
     * @return 根目录
     */
    public String getSDPath() {
        File sdDir = null;
        //判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (!sdCardExist) {
            return "";
        }
        //获取跟目录
        sdDir = Environment.getExternalStorageDirectory();
        return sdDir.toString();
    }

    /**
     * 文件选择
     */
    private void choseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Choose File"), CHOOSE_FILE_CODE);
        } catch (ActivityNotFoundException e) {
            toast("亲，木有文件管理器啊-_-!!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_FILE_CODE) {
                //得到uri，后面就是将uri转化成file的过程。
                Uri uri = data.getData();
                String chosePath = FileUtils.getPath(this,uri);
                Log.e("path", ":"+chosePath);
                copyAndLockFile(chosePath);
            }
        } else {
            Log.e(TAG1, "onActivityResult() error, resultCode: " + resultCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 拷贝以及加密文件
     *
     * @param chosePath
     */
    private void copyAndLockFile(String chosePath) {
        showLoding();
        FileCopy.getInstance(this).copyAssetsToSD(chosePath,LockPath+"/"+FileUtils.getFileName(chosePath))
                .setFileOperateCallback(new FileCopy.FileOperateCallback() {
            @Override
            public void onSuccess() {
                //  文件复制成功时，主线程回调
                toast("成功");
                hideLoading();
            }

            @Override
            public void onFailed(String error) {
                //  文件复制失败时，主线程回调
                toast("失败");
                hideLoading();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finish();
    }
}
