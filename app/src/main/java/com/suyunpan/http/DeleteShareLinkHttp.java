package com.suyunpan.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.suyunpan.appdata.AppData;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class DeleteShareLinkHttp implements Runnable {

    private String url = "http://" + AppData.URL + ":8080/deleteShareLink";
    private String shareLink;
    private Context packageContext;

    @Override
    public void run() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("shareLink", shareLink)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR DeleteShareLink");
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if ("deleteShareLink".equals(response)) {
                            Toast.makeText(packageContext, "删除成功！", Toast.LENGTH_SHORT).show();
                            new Thread(new ShareListHttp()).start();
                        } else if ("deleteError".equals(response)) {
                            Toast.makeText(packageContext, "删除失败！", Toast.LENGTH_SHORT).show();
                        }

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
}
