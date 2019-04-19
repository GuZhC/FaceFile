package com.ps.xh.facefile.face;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.ps.xh.facefile.http.DetectBeen;
import com.ps.xh.facefile.login.UserBean;
import com.ps.xh.facefile.login.UserManager;
import com.ps.xh.facefile.main.MainActivity;
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
    @BindView(R.id.tv_face_4)
    TextView tvFace4;
    @BindView(R.id.img_face_4)
    ImageView imgFace4;
    @BindView(R.id.ll_face_4)
    LinearLayout llFace4;
    @BindView(R.id.tv_face_5)
    TextView tvFace5;
    @BindView(R.id.img_face_5)
    ImageView imgFace5;
    @BindView(R.id.ll_face_5)
    LinearLayout llFace5;

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
        faceFile = Environment.getExternalStorageDirectory().toString() + "/faceFile/face";
        imageViews = new ArrayList<>();
        imageViews.add(imgFaceHappy);
        imageViews.add(imgFaceSad);
        imageViews.add(imgFaceNormal);
        imageViews.add(imgFace4);
        imageViews.add(imgFace5);
//        UserManager userManager = UserManager.getInstance();
//        userFace = userManager.getUserFace();
//        if (userFace.size() > 0) {
//            for (int i = 0; i < 3; i++) {
//                if (!TextUtils.isEmpty(userFace.get(i))){
//                    Glide.with(this).load(userFace.get(i)).placeholder(R.mipmap.pic_ing).into
// (imageViews.get(i));
//                }
//            }
//        }
        Glide.with(this).load(faceFile + "/happy.png").error(R.mipmap.icon_face_add).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imgFaceHappy);
        Glide.with(this).load(faceFile + "/sad.png").error(R.mipmap.icon_face_add).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imgFaceSad);
        Glide.with(this).load(faceFile + "/normal.png").error(R.mipmap.icon_face_add).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imgFaceNormal);
        Glide.with(this).load(faceFile + "/face4.png").error(R.mipmap.icon_face_add).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imgFace4);
        Glide.with(this).load(faceFile + "/face5.png").error(R.mipmap.icon_face_add).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imgFace5);
    }


    @OnClick({R.id.img_face_happy, R.id.img_face_sad, R.id.img_face_normal, R.id.img_addface_back
            , R.id.tv_addface_save, R.id.img_face_4, R.id.img_face_5})
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
            case R.id.img_face_4:
                wht = 3;
                takePhto("/face4.png");
                break;
            case R.id.img_face_5:
                wht = 4;
                takePhto("/face5.png");
                break;
            case R.id.tv_addface_save:
                if (FileUtils.listFilesInDir(faceFile).size() < 5) {
                    toast("请上传全部表情");
                } else {
                    startAct(MainActivity.class);
                    finish();
                }
                break;
        }
    }


    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        String originalPath = result.getImage().getOriginalPath();
        String compressPath = result.getImage().getCompressPath();
        Log.d("detect", "onSuccess: " + originalPath + "||" + compressPath);
        FileUtils.moveFile(compressPath, originalPath);
//        FileUtils.deleteFile(originalPath);
        Glide.with(this).load(originalPath).error(R.mipmap.icon_face_add).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).into(imageViews.get(wht));
        getDetect(originalPath);
//        postFile(originalPath);
    }

    /**
     * 照片识别
     */
    public void getDetect(final String path) {
        showLoding();
        OkGo.<String>post("https://api-cn.faceplusplus.com/facepp/v3/detect")
                .tag(this)
                .params("return_attributes", "emotion")
                .params("image_file", new File(path))
                .params("beauty_score_max",100)
                .execute(new StringCallback() {
                    /**
                     * @param response
                     */
                    @Override
                    public void onSuccess(Response<String> response) {
                        hideLoading();
                        Gson gson = new Gson();
                        DetectBeen detectBeen = gson.fromJson(response.body(), DetectBeen.class);
                        List<DetectBeen.FacesBean> faces = detectBeen.getFaces();
                        if (faces.size()==0){
                            toast("未识别到表情");
                            FileUtils.deleteFile(path);
                            Glide.with(FaceAddActivity.this).load(path).error(R.mipmap.icon_face_add).diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true).into(imageViews.get(wht));
                            return;
                        }
                        Log.d("detect", "onSuccess: " + response.body());
                        switch (wht) {
                            case 0:
                                faceIsOk(path, "表情不够高兴，再来一次吧", faces.get(0).getAttributes().getEmotion().getHappiness()) ;
                                break;
                            case 1:
                                faceIsOk(path, "表情不够伤心，再来一次吧", faces.get(0).getAttributes().getEmotion().getSadness());
                                break;
                            case 2:
                                faceIsOk(path, "表情不够平静，再来一次吧", faces.get(0).getAttributes().getEmotion().getNeutral());
                                break;
                            case 3:
                                faceIsOk(path, "表情不够惊讶，再来一次吧", faces.get(0).getAttributes().getEmotion().getSurprise());
                                break;
                            case 4:
                                faceIsOk(path, "表情不够愤怒，再来一次吧", faces.get(0).getAttributes().getEmotion().getAnger());
                                break;
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        toast("检测失败，请重试");
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
     * 表情是否正确
     * @param path
     * @param s
     * @param happiness
     * @return
     */
    private boolean faceIsOk(String path, String s, double happiness) {
        if (happiness < 40.0f) {
            toast(s);
            FileUtils.deleteFile(path);
            Glide.with(FaceAddActivity.this).load(path).error(R.mipmap.icon_face_add).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(imageViews.get(wht));
            return false;
        }
        return true;
    }

    /**
     * 拍照
     *
     * @param s
     */
    private void takePhto(String s) {
        String mPaht = faceFile + s;
        FileUtils.deleteFile(mPaht);
        FileUtils.createOrExistsFile(mPaht);
        File file = FileUtils.getFileByPath(mPaht);
        Uri uri = Uri.fromFile(file);
        TakePhoto takePhoto = getTakePhoto();
        takePhoto.onEnableCompress(new
                        CompressConfig.Builder().setMaxSize(500 * 1024).setMaxPixel(1000).create(),
                true);
        takePhoto.onPickFromCapture(uri);

    }

    /**
     * *********************************** 以下代码无用（上传服务器，修改废弃）
     */

    /**
     * 上传文件
     */
    private void postFile(String originalPath) {
        showLoding();
        final BmobFile bmobFile = new BmobFile(new File(originalPath));
        //详细示例可查看BmobExample工程中BmobFileActivity类
        final String[] filePaths = new String[1];
        filePaths[0] = originalPath;
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    //do something
                    saveUrl(urls.get(0));
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                toast("上传文件失败：" + errormsg);
                Log.e("upfile", statuscode + errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
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


}
