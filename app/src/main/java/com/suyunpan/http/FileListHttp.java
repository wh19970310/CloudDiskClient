package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.suyunpan.appdata.AppData;
import com.suyunpan.home.sharefile.ShareFileActivity;
import com.suyunpan.login.LoginInfo;
import com.suyunpan.main.HomeFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class FileListHttp implements Runnable {

    private String path = AppData.cloudCurrentDirectory;
    private String url = "http://" + AppData.URL + ":8080/filelist";
    private String forList;
    private Message message = new Message();

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
                        Log.d("SSSS", "ERR");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (forList == null) {
                            HomeFragment.jsonString = response;
                            message.what = 1;
                            HomeFragment.handler.sendMessage(message);
                        } else if (forList.equals("share")) {
                            ShareFileActivity.jsonString = response;
                            message.what = 0;
                            ShareFileActivity.handler.sendMessage(message);
                        }
                    }
                });
    }

    public String getForList() {
        return forList;
    }

    public void setForList(String forList) {
        this.forList = forList;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
