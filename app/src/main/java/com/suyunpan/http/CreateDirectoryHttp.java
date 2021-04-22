package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.suyunpan.appdata.AppData;
import com.suyunpan.login.LoginInfo;
import com.suyunpan.main.HomeFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class CreateDirectoryHttp implements Runnable {


    private String url = "http://" + AppData.URL + ":8080/createdirectory";
    private String resourcePath;
    private String userId;

    @Override
    public void run() {
        userId = LoginInfo.getUserId();
        resourcePath = AppData.cloudCurrentDirectory;
        OkHttpUtils
                .get()
                .url(url)
                .addParams("userId", userId)
                .addParams("resourcePath", resourcePath)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR DeleteCloud");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.equals("true")) {
                            new Thread(new FileListHttp()).start();
                            Message message = new Message();
                            message.what = 2;
                            HomeFragment.handler.sendMessage(message);
                        }

                    }
                });

    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
