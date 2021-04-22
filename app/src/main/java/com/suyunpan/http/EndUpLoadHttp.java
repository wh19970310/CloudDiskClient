package com.suyunpan.http;

import android.util.Log;

import com.suyunpan.appdata.AppData;
import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.login.LoginInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class EndUpLoadHttp implements Runnable {

    private String path;
    private String url = "http://" + AppData.URL + ":8080/endupload";
    private String MD5;
    private FileInfo fileInfo;

    @Override
    public void run() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("MD5", MD5)
                .addParams("userId", LoginInfo.getUserId())
                .addParams("fileName", fileInfo.getFileName())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR ENDLOAD");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("SSSS", response);
                    }
                });
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }
}
