package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.suyunpan.appdata.AppData;
import com.suyunpan.login.LoginInfo;
import com.suyunpan.main.HomeFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class RenameFileHttp implements Runnable {

    private String url = "http://" + AppData.URL + ":8080/renamefile";
    private String oldFilePath;
    private String newFilePath;

    @Override
    public void run() {

        OkHttpUtils
                .get()
                .url(url)
                .addParams("newFilePath", newFilePath)
                .addParams("oldFilePath", oldFilePath)
                .addParams("userId", LoginInfo.getUserId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR Rename");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        new Thread(new FileListHttp()).start();
                        if (response.equals("true")) {
                            Message message = new Message();
                            message.what = 3;
                            HomeFragment.handler.sendMessage(message);
                        }
                    }
                });

    }

    public String getOldFilePath() {
        return oldFilePath;
    }

    public void setOldFilePath(String oldFilePath) {
        this.oldFilePath = oldFilePath;
    }

    public String getNewFilePath() {
        return newFilePath;
    }

    public void setNewFilePath(String newFilePath) {
        this.newFilePath = newFilePath;
    }
}
