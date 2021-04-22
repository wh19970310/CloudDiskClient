package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.suyunpan.appdata.AppData;
import com.suyunpan.main.MyFragment;
import com.suyunpan.retrieve.RetrieveActivity;
import com.suyunpan.utils.model.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class UpdateHttp implements Runnable {

    private String url = "http://" + AppData.URL + ":8080/updateInfo";
    private User user;
    private String forRetrieve;
    private Message message = new Message();

    @Override
    public void run() {

        OkHttpUtils
                .get()
                .url(url)
                .addParams("userJson", JSON.toJSONString(user))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR NICKNAME");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.equals("true") && forRetrieve == null) {
                            message.what = 0;
                            MyFragment.handler.sendMessage(message);
                        } else {
                            message.obj = "密码重置成功！";
                            RetrieveActivity.handler.sendMessage(message);
                        }

                    }
                });

    }

    public void setForRetrieve(String forRetrieve) {
        this.forRetrieve = forRetrieve;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
