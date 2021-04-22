package com.suyunpan.http;

import android.util.Log;

import com.suyunpan.appdata.AppData;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class MoveFileHttp implements Runnable {

    private String url = "http://" + AppData.URL + ":8080/copyfile";
    private boolean isFile;
    private String resourcePath;
    private String targetPath;
    private String userId;
    private String isCopy;

    @Override
    public void run() {
        if (isCopy.equals("copy")) {
            OkHttpUtils
                    .get()
                    .url(url)
                    .addParams("userId", userId)
                    .addParams("resourcePath", resourcePath)
                    .addParams("targetPath", targetPath)
                    .addParams("isFile", String.valueOf(isFile))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.d("SSSS", "ERR");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response.equals("true")) {
                                FileOperationHttp fileOperationHttp = new FileOperationHttp();
                                fileOperationHttp.setPath(AppData.cloudCurrentDirectory);
                                new Thread(fileOperationHttp).start();
                            }
                        }
                    });
        } else if (isCopy.equals("cut")) {
            url = "http://" + AppData.URL + ":8080/cutfile";// 换url执行
            OkHttpUtils
                    .get()
                    .url(url)
                    .addParams("userId", userId)
                    .addParams("resourcePath", resourcePath)
                    .addParams("targetPath", targetPath)
                    .addParams("isFile", String.valueOf(isFile))
                    .addParams("uuid", AppData.user.getUUID())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.d("SSSS", "ERR");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (response.equals("true")) {
                                FileOperationHttp fileOperationHttp = new FileOperationHttp();
                                fileOperationHttp.setPath(AppData.cloudCurrentDirectory);
                                fileOperationHttp.setIsCopy("cut");
                                new Thread(fileOperationHttp).start();
                            } else {
                                Log.d("SSSS", response);
                            }
                        }
                    });
        }
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsCopy() {
        return isCopy;
    }

    public void setIsCopy(String isCopy) {
        this.isCopy = isCopy;
    }
}
