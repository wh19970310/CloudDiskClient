package com.suyunpan.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.suyunpan.appdata.AppData;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class GetShareHttp implements Runnable {

    private String url = "http://" + AppData.URL + ":8080/getShare";
    private String userId = String.valueOf(AppData.user.getUserid());
    private String shareLink;
    private String shareCode;
    private Context packageContext;

    @Override
    public void run() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("userId", userId)
                .addParams("shareLink", shareLink)
                .addParams("shareCode", shareCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR GETSHARE");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(packageContext, response, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void setPackageContext(Context packageContext) {
        this.packageContext = packageContext;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }
}
