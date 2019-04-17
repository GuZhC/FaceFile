package com.ps.xh.facefile.main;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ps.xh.facefile.utils.FileUtils;

public class FileCopy {
    private static FileCopy instance;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    private Context context;
    private FileOperateCallback callback;
    private String errorStr = "copy fail";

    public static FileCopy getInstance(Context context) {
        if (instance == null) {
            instance = new FileCopy(context);
        }
        return instance;
    }

    private FileCopy(Context context) {
        this.context = context;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (callback != null) {
                if (msg.what == SUCCESS) {
                    callback.onSuccess();
                }
                if (msg.what == FAILED) {
                    callback.onFailed(msg.obj.toString());
                }
            }
        }
    };

    public FileCopy copyAssetsToSD(final String srcPath, final String sdPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (FileUtils.moveFile(srcPath, sdPath)) {
                    handler.obtainMessage(SUCCESS).sendToTarget();
                } else {
                    handler.obtainMessage(FAILED, errorStr).sendToTarget();
                }
            }
        }).start();
        return this;
    }

    public void setFileOperateCallback(FileOperateCallback callback) {
        this.callback = callback;
    }

    private void copyAssetsToDst(String mSrcPath, String mDestPath) {

//            final File srcDir = FileUtils.getFileByPath(mSrcPath);
//            File destDir = FileUtils.getFileByPath(mDestPath);
//            String srcPath = srcDir.getPath() + File.separator;
//            String destPath = destDir.getPath() + File.separator;
//            if (destPath.contains(srcPath)) handler.obtainMessage(FAILED, errorStr).sendToTarget();
//            if (!srcDir.exists() || !srcDir.isDirectory()) handler.obtainMessage(FAILED, errorStr).sendToTarget();
//            if (destDir.exists()) {
//                    handler.obtainMessage(SUCCESS).sendToTarget();
//            }
//            if (!createOrExistsDir(destDir)) handler.obtainMessage(FAILED, errorStr).sendToTarget();
//            File[] files = srcDir.listFiles();
//            for (File file : files) {
//                File oneDestFile = new File(destPath + file.getName());
//                if (file.isFile()) {
//                    if (!copyOrMoveFile(file, oneDestFile, listener, isMove)) return false;
//
//                    if (file == null || oneDestFile == null)handler.obtainMessage(FAILED, errorStr).sendToTarget();
//                    // srcFile equals destFile then return false
//                    if (file.equals(oneDestFile)) handler.obtainMessage(FAILED, errorStr).sendToTarget();
//                    // srcFile doesn't exist or isn't a file then return false
//                    if (!file.exists() || !file.isFile())handler.obtainMessage(FAILED, errorStr).sendToTarget();
//                    if (file.exists()) {
//
//                            handler.obtainMessage(SUCCESS).sendToTarget();
//                    }
//                    if (!createOrExistsDir(oneDestFile.getParentFile())) handler.obtainMessage(FAILED, errorStr).sendToTarget();
//                    try {
//                        return writeFileFromIS(oneDestFile, new FileInputStream(file))
//                                && !(isMove && !deleteFile(file));
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                        handler.obtainMessage(FAILED, errorStr).sendToTarget();
//                    }
//                } else if (file.isDirectory()) {
////                    if (!copyOrMoveDir(file, oneDestFile, listener, isMove)) return false;
//                }
//            }
//            return !isMove || deleteDir(srcDir);
//            int bytesum = 0;
//            int byteread = 0;
//            File oldfile = new File(srcPath);
//            if (oldfile.exists()) { //文件存在时
//                InputStream inStream = new FileInputStream(srcPath); //读入原文件
//                FileOutputStream fs = new FileOutputStream(dstPath);
//                byte[] buffer = new byte[1444];
//                int length;
//                while ( (byteread = inStream.read(buffer)) != -1) {
//                    bytesum += byteread; //字节数 文件大小
//                    System.out.println(bytesum);
//                    fs.write(buffer, 0, byteread);
//                }
//
//                inStream.close();
//            }
//        }
//        catch (Exception e) {
//            System.out.println("复制单个文件操作出错");
//            e.printStackTrace();
//
        }

    public interface FileOperateCallback {
        void onSuccess();

        void onFailed(String error);
    }

}
