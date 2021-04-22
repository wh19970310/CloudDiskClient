package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.suyunpan.appdata.AppData;
import com.suyunpan.home.deletefile.DeleteFileActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class DeleteCloudFileHttp implements Runnable {

    private String url = "http://" + AppData.URL + ":8080/deletefile";
    private String resourcePath;
    private String userId;

    @Override
    public void run() {

        OkHttpUtils
                .get()
                .url(url)
                .addParams("userId", userId)
                .addParams("resourcePath", resourcePath)
                .addParams("uuid", AppData.user.getUUID())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR DeleteCloud");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        new Thread(new DeleteCloudListHttp()).start();
                        if (response.equals("true")) {
                            Message message = new Message();
                            message.what = 1;
                            message.obj = "deletesuccess";
                            DeleteFileActivity.handler.sendMessage(message);
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
