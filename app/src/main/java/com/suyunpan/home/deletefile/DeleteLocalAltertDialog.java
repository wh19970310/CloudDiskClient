package com.suyunpan.home.deletefile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;

import com.suyunpan.http.DeleteCloudFileHttp;

import java.io.File;

public class DeleteLocalAltertDialog {

    private AlertDialog.Builder AlertDialog;
    private File deleteFile = null;
    private String resourcePath;
    private String userId;


    public void showDialogDeleteLocal() {

        AlertDialog.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFile.delete();
                Message message = new Message();
                message.what = 1;
                DeleteFileActivity.handler.sendMessage(message);
            }
        });

        AlertDialog.setNegativeButton("取消删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog.setTitle("选择操作");
        AlertDialog.setMessage("删除文件？");
        AlertDialog.show();
    }

    public void showDialogDeleteCloud() {

        AlertDialog.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                DeleteCloudFileHttp deleteCloudFileHttp = new DeleteCloudFileHttp();
                deleteCloudFileHttp.setResourcePath(resourcePath);
                deleteCloudFileHttp.setUserId(userId);
                Thread thread = new Thread(deleteCloudFileHttp);
                thread.start();
            }
        });

        AlertDialog.setNegativeButton("取消删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog.setTitle("选择操作");
        AlertDialog.setMessage("删除文件？");
        AlertDialog.show();
    }

    public android.app.AlertDialog.Builder getAlertDialog() {
        return AlertDialog;
    }

    public void setAlertDialog(android.app.AlertDialog.Builder alertDialog) {
        AlertDialog = alertDialog;
    }

    public File getDeleteFile() {
        return deleteFile;
    }

    public void setDeleteFile(File deleteFile) {
        this.deleteFile = deleteFile;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
