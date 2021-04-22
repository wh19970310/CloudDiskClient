package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.suyunpan.appdata.AppData;
import com.suyunpan.home.sharefile.ShareFileActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class ShareFileHttp implements Runnable {

    private String url = "http://" + AppData.URL + ":8080/shareFile";
    private String fileMD5;
    private String userId = String.valueOf(AppData.user.getUserid());
    private String fileName;
    private Message message = new Message();

    @Override
    public void run() {

        OkHttpUtils
                .get()
                .url(url)
                .addParams("fileMD5", fileMD5)
                .addParams("userId", userId)
                .addParams("fileName", fileName)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR Rename");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        message.obj = response;
                        ShareFileActivity.handler.sendMessage(message);
                    }
                });

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileMD5() {
        return fileMD5;
    }

    public void setFileMD5(String fileMD5) {
        this.fileMD5 = fileMD5;
    }
}
