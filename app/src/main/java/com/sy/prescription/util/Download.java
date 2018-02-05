package com.sy.prescription.util;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ygs on 16-11-20.
 */

public class Download extends Thread {


    private Handler mHandler;
    private String mSavePath = "";
    private String url = "";
    private Context mContext;

    public Download(Context context, String url, Handler handler) {
        mContext = context;
        this.url = url;
        mHandler = handler;
    }

    @Override
    public void run() {
        try {
            // 判断SD卡是否存在，并且是否具有读写权限
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获得存储卡的路径
                String sdpath = Environment.getExternalStorageDirectory()
                        + "/";
                mSavePath = sdpath + "download/";
                URL url = new URL(this.url);
                // 创建连接
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                // 获取文件大小
                int length = conn.getContentLength();
                // 创建输入流
                InputStream is = conn.getInputStream();

                File file = new File(mSavePath);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdir();
                }
                File apkFile = new File(mSavePath, "saliya.apk");
                if (!apkFile.exists()) {
                    apkFile.delete();
                }
                //apkFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(apkFile);
                int count = 0;
                // 缓存
                byte buf[] = new byte[1024];
                // 写入到文件中
                int progress;
                while (true) {
                    int numread = is.read(buf);
                    count += numread;
                    // 计算进度条位置
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(progress);
                    if (numread <= 0) {
                        Message msg = new Message();
                        msg.what = -1;
                        msg.obj = mSavePath;
                        // 下载完成
                        mHandler.sendMessage(msg);
                        break;
                    }
                    // 写入文件
                    fos.write(buf, 0, numread);
                }
                fos.close();
                is.close();
            }
        } catch (MalformedURLException e) {
            mHandler.sendEmptyMessage(-2);
            e.printStackTrace();
        } catch (IOException e) {
            mHandler.sendEmptyMessage(-2);
            e.printStackTrace();
        }
    }


}
