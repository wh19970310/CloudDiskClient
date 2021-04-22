package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.suyunpan.appdata.AppData;
import com.suyunpan.my.mysharelink.MyShareLinkActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class ShareListHttp implements Runnable {

    private String url = "http://" + AppData.URL + ":8080/getShareList";
    private String userId = String.valueOf(AppData.user.getUserid());
    private Message message = new Message();

    @Override
    public void run() {

        OkHttpUtils
                .get()
                .url(url)
                .addParams("userId", userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR ShareListHttp");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.length() > 0) {
                            message.obj = response;
                        } else {
                            message.obj = null;
                        }
                        MyShareLinkActivity.handler.sendMessage(message);

                    }
                });

    }
}
