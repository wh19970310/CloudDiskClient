package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.suyunpan.appdata.AppData;
import com.suyunpan.signup.SignUpActivity;
import com.suyunpan.utils.model.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class SingUpHttp implements Runnable {

    private String phoneNum;
    private String passWord;
    private String passWordCheck;
    private String checkCode;
    private String url = "http://" + AppData.URL + ":8080/signup";

    @Override
    public void run() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("phoneNum", phoneNum)
                .addParams("passWord", passWord)
                .addParams("checkCode", checkCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR SIGN");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("SSSS", response);
                        Message message = new Message();
                        if (response.length() == 0) {
                            message.what = 3;
                        } else {
                            User user = JSON.parseObject(response, User.class);
                            message.obj = user;
                        }
                        SignUpActivity.handler.sendMessage(message);
                    }
                });
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPassWordCheck() {
        return passWordCheck;
    }

    public void setPassWordCheck(String passWordCheck) {
        this.passWordCheck = passWordCheck;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

}
