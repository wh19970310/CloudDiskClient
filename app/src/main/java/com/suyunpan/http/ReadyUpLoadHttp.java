package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.suyunpan.appdata.AppData;
import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.home.uploadfile.UpLoadFileActivity;
import com.suyunpan.login.LoginInfo;
import com.suyunpan.socketclient.instructtransfer.UpLoadLoadInstruct;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class ReadyUpLoadHttp implements Runnable {

    private String path;
    private String url = "http://" + AppData.URL + ":8080/readyupload";
    private String MD5;
    private String fileSize;
    private String fileName;
    private FileInfo fileInfo;

    @Override
    public void run() {
        OkHttpUtils
                .get()
                .url(url)
                .addParams("MD5", MD5)
                .addParams("fileSize", fileSize)
                .addParams("userId", LoginInfo.getUserId())
                .addParams("fileName", fileName)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("SSSS", "ERR UPLOAD");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Message message = new Message();
                        if (response.equals("readyUpLoad")) {
                            UpLoadLoadInstruct upLoadLoadInstruct = new UpLoadLoadInstruct();
                            upLoadLoadInstruct.setFileInfo(fileInfo);
                            upLoadLoadInstruct.setMD5(MD5);
                            upLoadLoadInstruct.setPath(path);
                            new Thread(upLoadLoadInstruct).start();

                            message.obj = "开始上传！请在传输界面查看上传进度！";

                        } else if (response.equals("secondpass")) {
                            message.what = 0;
                        } else if (response.equals("FileExists")) {
                            message.what = 2;
                        } else if (response.equals("SpaceNotEnough")) {
                            message.what = 3;
                        }
                        UpLoadFileActivity.handler.sendMessage(message);
                    }
                });
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }
}
