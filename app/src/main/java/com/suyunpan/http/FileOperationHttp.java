package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.suyunpan.appdata.AppData;
import com.suyunpan.home.movefile.MoveFileActivity;
import com.suyunpan.login.LoginInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class FileOperationHttp implements Runnable {

    private String path = "";
    private String url = "http://" + AppData.URL + ":8080/filelist";
    private String isCopy = "";

    @Override
    public void run() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("userPath", LoginInfo.getUserId() + path)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR FILELIST");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Message message = new Message();
                        message.obj = response;
                        if (isCopy.equals("cut")) {
                            message.what = 1;
                        }
                        MoveFileActivity.handler.sendMessage(message);
                    }
                });
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIsCopy() {
        return isCopy;
    }

    public void setIsCopy(String isCopy) {
        this.isCopy = isCopy;
    }
}
