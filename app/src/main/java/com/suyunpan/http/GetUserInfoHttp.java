package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.suyunpan.appdata.AppData;
import com.suyunpan.main.MyFragment;
import com.suyunpan.utils.model.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class GetUserInfoHttp implements Runnable {

    private String url = "http://" + AppData.URL + ":8080/getuserinfo";
    private String userId;
    private String uuid;

    @Override
    public void run() {
        uuid = AppData.user.getUUID();
        OkHttpUtils
                .get()
                .url(url)
                .addParams("userId", userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR GET USERINFO");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        User user = JSON.parseObject(response, User.class);
                        user.setUUID(uuid);
                        Message message = new Message();
                        message.obj = user;
                        MyFragment.handler.sendMessage(message);
                    }
                });
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
