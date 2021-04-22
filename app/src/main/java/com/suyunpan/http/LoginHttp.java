package com.suyunpan.http;

import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.suyunpan.appdata.AppData;
import com.suyunpan.login.LoginActivity;
import com.suyunpan.login.LoginInfo;
import com.suyunpan.utils.PassWordHash;
import com.suyunpan.utils.model.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Handler;

import okhttp3.Call;

public class LoginHttp implements Runnable {

    public static Handler handler;
    private String url = "http://" + AppData.URL + ":8080/userlogin";
    private Message message = new Message();

    @Override
    public void run() {
        String hashCode = "";
        PassWordHash passWordHash = new PassWordHash();
        passWordHash.setPassWord(LoginInfo.getPwd());
        try {
            hashCode = passWordHash.sha1();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        OkHttpUtils
                .get()
                .url(url)
                .addParams("userId", LoginInfo.getUserId())
                .addParams("passWord", hashCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        message.what = -2;//连接失败的标识
                        LoginActivity.handler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.equals("")) {
                            message.what = -1;//连接失败的标识
                        } else {
                            AppData.user = JSON.parseObject(response, User.class);
                            message.what = 1;//连接成功的标识
                        }
                        LoginActivity.handler.sendMessage(message);
                    }
                });
    }
}
