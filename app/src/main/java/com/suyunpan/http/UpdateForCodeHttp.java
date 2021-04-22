package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.suyunpan.appdata.AppData;
import com.suyunpan.my.updateinfo.UpdateInfoActivity;
import com.suyunpan.utils.model.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class UpdateForCodeHttp implements Runnable {

    private String url = "http://" + AppData.URL + ":8080/updateInfo";
    private User user;
    private String phoneNum;
    private String checkCode;
    private Message message;

    @Override
    public void run() {

        OkHttpUtils
                .get()
                .url(url)
                .addParams("userJson", JSON.toJSONString(user))
                .addParams("checkCode", checkCode)
                .addParams("phoneNum", phoneNum)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR UPDATEUSERINFO");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        message = new Message();
                        if (response.equals("true")) {
                            message.obj = "修改成功！";

                        } else {
                            message.obj = "错误，检查信息！";
                        }
                        UpdateInfoActivity.handler.sendMessage(message);
                    }
                });


    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
}
