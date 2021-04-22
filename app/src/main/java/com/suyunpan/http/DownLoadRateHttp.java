package com.suyunpan.http;

import android.os.Message;
import android.util.Log;

import com.suyunpan.appdata.AppData;
import com.suyunpan.filesystem.FileAdapter;
import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.home.transfer.TransferActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Iterator;

import okhttp3.Call;

import static java.lang.Thread.sleep;

public class DownLoadRateHttp implements Runnable {

    private String url = "http://" + AppData.URL + ":8080/downloadrate";
    private FileInfo fileInfo;
    private String getResponse = "0%";
    public static FileAdapter fileAdaper = null;

    @Override
    public void run() {
        while (!getResponse.equals("100%") && !getResponse.equals("pause")) {
            OkHttpUtils
                    .get()
                    .url(url)
                    .addParams("fileMD5", fileInfo.getFileMD5())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.d("SSSS", "ERR");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            getResponse = response;
                            if ("0%".equals(response) && TransferActivity.handler != null) {
                                fileInfo.setFileTransferRate("连接中…");
                                Message message = new Message();
                                message.what = 1;
                                TransferActivity.handler.sendMessage(message);
                            } else if (!"pause".equals(response) && TransferActivity.handler != null) {
                                fileInfo.setFileTransferRate(response);
                                Message message = new Message();
                                message.what = 1;
                                TransferActivity.handler.sendMessage(message);
                            }
                        }
                    });
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (getResponse.equals("pause")) {
            fileInfo.setFileTransferRate("暂停");
            fileInfo.setFileOperation("继续");
            Iterator iterator = AppData.downLoadFileInfoList.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                FileInfo cursorFileInfo = (FileInfo) iterator.next();
                if (cursorFileInfo.getFileName().equals(fileInfo.getFileName())) {
                    iterator.remove();
                    break;
                }
                index++;
            }
            AppData.downLoadFileInfoList.add(index, fileInfo);
        } else if (getResponse.equals("100%")) {
            fileInfo.setFileTransferRate("下载完成");
        }
        if (TransferActivity.handler != null) {
            Message message = new Message();
            message.what = 1;
            TransferActivity.handler.sendMessage(message);
        }
    }


    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public FileAdapter getFileAdaper() {
        return fileAdaper;
    }

    public void setFileAdaper(FileAdapter fileAdaper) {
        this.fileAdaper = fileAdaper;
    }
}
