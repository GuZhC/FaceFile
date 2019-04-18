package com.ps.xh.facefile.face;

import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.model.TResult;
import com.ps.xh.facefile.R;
import com.ps.xh.facefile.base.BaseActivity;
import com.ps.xh.facefile.login.UserBean;
import com.ps.xh.facefile.login.UserManager;
import com.ps.xh.facefile.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

public class FaceAddActivity extends BaseActivity {

    @BindView(R.id.img_addface_back)
    ImageView imgAddfaceBack;
    @BindView(R.id.ll_main_toolbar)
    LinearLayout llMainToolbar;
    @BindView(R.id.tv_face_happy)
    TextView tvFaceHappy;
    @BindView(R.id.img_face_happy)
    ImageView imgFaceHappy;
    @BindView(R.id.ll_face_happy)
    LinearLayout llFaceHappy;
    @BindView(R.id.tv_face_sad)
    TextView tvFaceSad;
    @BindView(R.id.img_face_sad)
    ImageView imgFaceSad;
    @BindView(R.id.ll_face_sad)
    LinearLayout llFaceSad;
    @BindView(R.id.tv_face_normal)
    TextView tvFaceNormal;
    @BindView(R.id.img_face_normal)
    ImageView imgFaceNormal;
    @BindView(R.id.ll_face_normal)
    LinearLayout llFaceNormal;
    @BindView(R.id.tv_addface_save)
    TextView tvAddfaceSave;

    private String faceFile;
    private List<String> userFace;
    private List<ImageView> imageViews;
    int wht = 0;

    @Override
    protected int addContentView() {
        return R.layout.activity_face_add;
    }

    @Override
    protected void initView() {
        imageViews = new ArrayList<>();
        imageViews.add(imgFaceHappy);
        imageViews.add(imgFaceSad);
        imageViews.add(imgFaceNormal);
        faceFile = Environment.getExternalStorageDirectory().toString() + "/faceFile/face";
        UserManager userManager = UserManager.getInstance();
        userFace = userManager.getUserFace();
        if (userFace.size() > 0) {
            for (int i = 0; i < 3; i++) {
                if (!TextUtils.isEmpty(userFace.get(i))){
                    Glide.with(this).load(userFace.get(i)).placeholder(R.mipmap.pic_ing).into(imageViews.get(i));
                }
            }
        }
    }



    @OnClick({R.id.img_face_happy, R.id.img_face_sad, R.id.img_face_normal, R.id.img_addface_back
            , R.id.tv_addface_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_face_happy:
                takePhto("/happy.png");
                wht = 0;
                break;
            case R.id.img_face_sad:
                takePhto("/sad.png");
                wht = 1;
                break;
            case R.id.img_face_normal:
                takePhto("/normal.png");
                wht = 2;
                break;
            case R.id.img_addface_back:
                finish();
                break;
            case R.id.tv_addface_save:
//                for (BmobFile file : userFace) {
//                    if (file == null){
//                        toast("请设置所有表情");
//                        return;
//                    }
//                }
                break;
        }
    }

    private void saveUrl(String fileUrl) {
        userFace.set(wht, fileUrl);
        final UserBean user = BmobUser.getCurrentUser(UserBean.class);
        user.setFaceUrl(userFace);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    hideLoading();
                    UserManager.getInstance().getUserBean().setFaceUrl(userFace);
                    toast("保存成功");
                } else {
                    hideLoading();
                    toast("保存失败");
                }
            }
        });
    }


    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        String originalPath = result.getImage().getOriginalPath();
        Glide.with(this).load(originalPath).into(imageViews.get(wht));
        postFile(originalPath);
    }

    /**
     * 上传文件
     */
    private void postFile(String originalPath) {
        showLoding();
        final BmobFile bmobFile = new BmobFile(new File(originalPath));
//        bmobFile.uploadblock(new UploadFileListener() {
//            @Override
//            public void done(BmobException e) {
//                if (e == null) {
//                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
//                    saveUrl(bmobFile.getFileUrl());
//                } else {
//                    hideLoading();
//                    toast("上传文件失败：" + e.getMessage());
//                    Log.e("upfile",e.getErrorCode()+e.getMessage());
//                }
//
//            }
//
//            @Override
//            public void onProgress(Integer value) {
//                // 返回的上传进度（百分比）
//            }
//        });
        //详细示例可查看BmobExample工程中BmobFileActivity类
        final String[] filePaths = new String[1];
        filePaths[0] = originalPath;
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files,List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if(urls.size()==filePaths.length){//如果数量相等，则代表文件全部上传完成
                    //do something
                    saveUrl(urls.get(0));
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                toast("上传文件失败：" + errormsg);
                    Log.e("upfile",statuscode+errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
    }

    /**
     * 拍照
     *
     * @param s
     */
    private void takePhto(String s) {
        String mPaht = faceFile + s;
        FileUtils.createOrExistsFile(mPaht);
        File file = FileUtils.getFileByPath(mPaht);
        Uri uri = Uri.fromFile(file);
        getTakePhoto().onPickFromCapture(uri);
    }
}
