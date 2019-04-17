package com.ps.xh.facefile.main;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.ps.xh.facefile.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FileCopy {
    private static FileCopy instance;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    private Context context;
    private FileOperateCallback callback;
    private volatile boolean isSuccess;
    private String errorStr;

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
                copyAssetsToDst(srcPath, sdPath);
            }
        }).start();
        return this;
    }

    public void setFileOperateCallback(FileOperateCallback callback) {
        this.callback = callback;
    }

    private void copyAssetsToDst( String srcPath, String dstPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(srcPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(srcPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(dstPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                handler.obtainMessage(SUCCESS).sendToTarget();
                inStream.close();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
            handler.obtainMessage(FAILED, errorStr).sendToTarget();
        }
    }

    public interface FileOperateCallback {
        void onSuccess();

        void onFailed(String error);
    }

}
