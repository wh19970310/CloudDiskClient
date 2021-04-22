package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.suyunpan.appdata.AppData;
import com.suyunpan.retrieve.RetrieveActivity;
import com.suyunpan.utils.model.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class RetrieveHttp implements Runnable {

    private String phoneNum;
    private String code;

    private String url = "http://" + AppData.URL + ":8080/Retrieve";
    private Message message = new Message();


    @Override
    public void run() {

        OkHttpUtils
                .get()
                .url(url)
                .addParams("phoneNum", phoneNum)
                .addParams("code", code)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.length() == 0) {
                            message.obj = "没有该用户！";

                        } else {
                            message.what = 1;
                            RetrieveActivity.user = JSON.parseObject(response, User.class);
                        }
                        RetrieveActivity.handler.sendMessage(message);
                    }
                });

    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
