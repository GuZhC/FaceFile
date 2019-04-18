package com.ps.xh.facefile.main;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jph.takephoto.model.TResult;
import com.ps.xh.facefile.R;
import com.ps.xh.facefile.base.BaseActivity;
import com.ps.xh.facefile.face.FaceAddActivity;
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
    @BindView(R.id.img_main_menu)
    ImageView imgMainMenu;
    @BindView(R.id.img_main_add)
    ImageView imgMainAdd;
    @BindView(R.id.ll_main_toolbar)
    LinearLayout llMainToolbar;
    @BindView(R.id.recycler_main)
    RecyclerView recyclerMain;
    @BindView(R.id.img_main_takephoto)
    TextView img_main_takephoto;
    private ArrayList<String> docPaths;
    private TextView name;
    private String LockPath;
    private static final int CHOOSE_FILE_CODE = 0;
    private static final String TAG1 = "FileChoose";
    private List<FileBean> mData;
    private LockFileAdapter adapter;

    public static final int FILE_LOCK_0 = 0;
    public static final int FILE_LOCK_1 = 1;
    public static final int FILE_LOCK_2 = 2;
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
        updateLockFile();
    }

    /**
     * 更新加密文件
     */
    private void updateLockFile() {
        List<File> sdLockFiles = FileUtils.listFilesInDir(LockPath);
        if (sdLockFiles.size() <= 0) {
            mainRlEmpty.setVisibility(View.VISIBLE);
            recyclerMain.setVisibility(View.GONE);
            return;
        }
        mData = new ArrayList<>();
        for (File file : sdLockFiles) {
            FileBean fileBean = new FileBean();
            String fileAbsolutePath = file.getAbsolutePath();
            String fileName = FileUtils.getFileName(fileAbsolutePath);
            int dotIndex = fileName.lastIndexOf(".");
            String end = fileName.substring(dotIndex, fileName.length()).toLowerCase();
            if (TextUtils.equals(end, LockFileHelper.CIPHER_TEXT_SUFFIX)) {
                fileBean.setIsLock(MainActivity.FILE_LOCK_1);
            } else if (TextUtils.equals(end, LockFileHelper.CIPHER_TEXT_SUFFIX2)) {
                fileBean.setIsLock(MainActivity.FILE_LOCK_2);
            } else {
                fileBean.setIsLock(MainActivity.FILE_LOCK_0);
            }
            fileBean.setName(fileName);
            fileBean.setPath(fileAbsolutePath);
            mData.add(fileBean);
        }
        mainRlEmpty.setVisibility(View.GONE);
        recyclerMain.setVisibility(View.VISIBLE);
        recyclerMain.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LockFileAdapter(mData);
        recyclerMain.setAdapter(adapter);
        //长按
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
                hintDeleat(position);
                return false;
            }
        });
//        点击
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final FileBean fileBean = mData.get(position);
                if (fileBean.getIsLock() != 0) {
                    AlertDialog versionDialog =
                            new AlertDialog.Builder(MainActivity.this).setTitle("提示").setMessage(
                                    "是否解密？").setPositiveButton(getString(R.string.dolog_unlock_file),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            decryptFile(fileBean.getPath(), fileBean.getIsLock());
                                        }
                                    }).setNegativeButton(getString(R.string.dolog_lock_file_no),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).create();
                    versionDialog.setCanceledOnTouchOutside(true);
                    versionDialog.setCancelable(true);
                    versionDialog.show();
                } else {
                    AlertDialog versionDialog =
                            new AlertDialog.Builder(MainActivity.this).setTitle("提示").setMessage(
                            "请选择要进行的操作。").setPositiveButton(getString(R.string.dolog_lock_file),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    lockWat(fileBean.getPath());
                                }
                            }).setNegativeButton(getString(R.string.dolog_look_file),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    openFile(MainActivity.this, fileBean.getPath());
                                }
                            }).create();
                    versionDialog.setCanceledOnTouchOutside(true);
                    versionDialog.setCancelable(true);
                    versionDialog.show();
                }
            }
        });
    }

    /**
     * 解密
     *
     * @param path
     * @return
     */
    private void decryptFile(String path, int watLock) {
        if (watLock == FILE_LOCK_1) {
            toast("方式一解密");
        } else {
            toast("方式二解密");
        }
        showLoding();
        boolean isDecrypt = LockFileHelper.decrypt(path, new LockFileHelper.CipherListener() {
            @Override
            public void onProgress(long current, long total) {
            }
        }, watLock);
        if (isDecrypt) {
            hideLoading();
            toast("解密成功");
        } else {
            hideLoading();
            toast("解密失败");
        }
        updateLockFile();
    }

    /**
     * 加密
     *
     * @param path
     * @return
     */
    private void encryptFile(String path, int watLock) {

        showLoding();
        boolean isEncrypt = LockFileHelper.encrypt(watLock, path, new LockFileHelper.CipherListener() {
            @Override
            public void onProgress(long current, long total) {
            }
        });
        if (isEncrypt) {
            hideLoading();
            toast("加密成功");
        } else {
            hideLoading();
            toast("加密失败");
        }
        updateLockFile();
    }


    /**
     * 删除提醒
     *
     * @param position
     */
    private void hintDeleat(final int position) {
        AlertDialog versionDialog =
                new AlertDialog.Builder(MainActivity.this).setTitle("删除").setMessage(
                "确定删除？").setPositiveButton(getString(R.string.dolog_lock_file_sure),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (FileUtils.delete(mData.get(position).getPath())) {
                            updateLockFile();
                        } else {
                            toast("删除失败");
                        }
                    }
                }).setNegativeButton(getString(R.string.dolog_lock_file_no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        versionDialog.setCanceledOnTouchOutside(true);
        versionDialog.setCancelable(true);
        versionDialog.show();
    }

    public void openFile(Context context, String path) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri;
            File file = new File(path);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //FileProvider.getUriForFile();第一个参数是context.
                // 第二个值。比较关键、这个也就是我们在manifest里面的provider里面的
                //android:authorities="com.example.zongm.testapplication.provider"
                //因为我用的就是AppId.所以。这里就直接用BuildConfig.APPLICATION_ID了。
                //如果你的android:authorities="test.provider"。那这里第二个参数就应该是test.provider
                uri = FileProvider.getUriForFile(context, "com.ps.xh.facefile.provider", file);
//                uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig
// .APPLICATION_ID + ".provider", file);
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                uri = Uri.fromFile(file);
            }
            intent.setDataAndType(uri, MapTable.getMIMEType(path));
            context.startActivity(intent);
            Intent.createChooser(intent, "请选择对应的软件打开该附件！");
        } catch (ActivityNotFoundException e) {
            toast("sorry附件不能打开，请下载相关软件！");
        }
    }

    /**
     * 没有Sd卡
     */
    private void hintNoSd() {
        AlertDialog versionDialog = new AlertDialog.Builder(this).setTitle("提示").setMessage(
                "亲，木有检测到sd卡啊-_-!!").setPositiveButton(getString(R.string.no_sd_out),
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
        String name = SPUtils.read(this, "USER", "PHONE");
        if (TextUtils.isEmpty(name)) startAct(LoginActivity.class);
        this.name.setText(name);
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
                choseFile();
                return true;
            case R.id.navItemface:
                startAct(FaceAddActivity.class);
                return true;
            default:
                return false;
        }
    }

    @OnClick({R.id.img_main_menu, R.id.img_main_add, R.id.main_tv_chose, R.id.img_main_takephoto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_main_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.img_main_add:
                choseFile();
                break;
            case R.id.main_tv_chose:
                choseFile();
                break;
            case R.id.img_main_takephoto:
                takePhto("/"+System.currentTimeMillis()+"pic.png");
                break;
        }
    }
    /**
     * 拍照
     *
     * @param s
     */
    private void takePhto(String s) {
        String mPaht = Environment.getExternalStorageDirectory().toString() + "/faceFile/lockFile" + s;
        FileUtils.createOrExistsFile(mPaht);
        File file = FileUtils.getFileByPath(mPaht);
        Uri uri = Uri.fromFile(file);
        getTakePhoto().onPickFromCapture(uri);
    }
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        String originalPath = result.getImage().getOriginalPath();
        lockWat(originalPath);
    }
    /**
     * @return 根目录
     */
    public String getSDPath() {
        File sdDir = null;
        //判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
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
                String chosePath = FileUtils.getPath(this, uri);
                Log.e("path", ":" + chosePath);
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
        final String newFilePath = LockPath + "/" + FileUtils.getFileName(chosePath);
        FileCopy.getInstance(this).copyAssetsToSD(chosePath, newFilePath)
                .setFileOperateCallback(new FileCopy.FileOperateCallback() {
                    @Override
                    public void onSuccess() {
                        //  文件复制成功时，主线程回调
                        hideLoading();
                        //加密
                        lockWat(newFilePath);
                    }

                    @Override
                    public void onFailed(String error) {
                        //  文件复制失败时，主线程回调
                        toast("复制文件失败");
                        hideLoading();
                    }
                });
    }

    /**
     * 加密方式
     *
     * @param newFilePath
     */
    private void lockWat(final String newFilePath) {
        final String[] items = {"普通加密", "高级加密", "多重加密"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(MainActivity.this);
        listDialog.setTitle("加密方式");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (items[which]) {
                    case "普通加密":
                        updateLockFile();
                        break;
                    case "高级加密":
                        encryptFile(newFilePath, FILE_LOCK_1);
                        updateLockFile();
                        break;
                    case "多重加密":
                        encryptFile(newFilePath, FILE_LOCK_2);
                        updateLockFile();
                        break;
                }
            }
        });
        listDialog.show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        finish();
    }
}
