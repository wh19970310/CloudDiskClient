package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.suyunpan.appdata.AppData;
import com.suyunpan.home.deletefile.DeleteFileActivity;
import com.suyunpan.login.LoginInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class DeleteCloudListHttp implements Runnable {

    private String path = AppData.cloudCurrentDirectory;
    private String url = "http://" + AppData.URL + ":8080/filelist";

    @Override
    public void run() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("userPath", LoginInfo.getUserId() + path)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR DelList");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        DeleteFileActivity.jsonString = response;
                        Message message = new Message();
                        message.what = 1;
                        DeleteFileActivity.handler.sendMessage(message);
                    }
                });
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
