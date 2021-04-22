package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.suyunpan.appdata.AppData;
import com.suyunpan.my.updateinfo.UpdateInfoActivity;
import com.suyunpan.retrieve.RetrieveActivity;
import com.suyunpan.signup.SignUpActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class GetCodeHttp implements Runnable {

    private String url = "http://" + AppData.URL + ":8080/getcode";
    private String phoneNum;
    private String forCode;
    private Message message = new Message();

    @Override
    public void run() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("phoneNum", phoneNum)
                .addParams("forCode", forCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR CODE");
                    }

                    @Override
                    public void onResponse(String response, int id) {


                        if (forCode.equals("signUp")) {
                            if (response.equals("true")) {
                                message.what = 1;
                            } else {
                                message.what = 2;
                            }
                            SignUpActivity.handler.sendMessage(message);
                        } else if (forCode.equals("updateInfo")) {
                            if (response.equals("true")) {
                                message.obj = "短信已下发，请注意查收！";
                            }
                            UpdateInfoActivity.handler.sendMessage(message);
                        } else if (forCode.equals("Retrieve")) {
                            if (response.equals("true")) {
                                message.obj = "短信已下发，请注意查收！";
                            } else {
                                message.obj = "手机号未注册！";
                            }
                            RetrieveActivity.handler.sendMessage(message);
                        }

                    }
                });
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getForCode() {
        return forCode;
    }

    public void setForCode(String forCode) {
        this.forCode = forCode;
    }
}
