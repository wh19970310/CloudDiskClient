package com.suyunpan.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.suyunpan.R;
import com.suyunpan.appdata.AppData;
import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.home.movefile.MoveFileActivity;
import com.suyunpan.http.RenameFileHttp;
import com.suyunpan.utils.FileNameCheck;

import java.util.List;

public class FileAlertDialog {
    private AlertDialog.Builder AlertDialog;
    private String resourcePath;
    private boolean isFile;
    private List<FileInfo> fileInfoList;

    public void showDialog(final FileInfo fileInfo, final Context packageContext) {

        AlertDialog.setPositiveButton("复制", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(packageContext, MoveFileActivity.class);
                intent.putExtra("fileName", fileInfo.getFileName());
                intent.putExtra("resourcePath", resourcePath);
                intent.putExtra("isFile", String.valueOf(isFile));
                intent.putExtra("isCopy", "copy");
                packageContext.startActivity(intent);
            }
        });

        AlertDialog.setNegativeButton("剪切", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(packageContext, MoveFileActivity.class);
                intent.putExtra("fileName", fileInfo.getFileName());
                intent.putExtra("resourcePath", resourcePath);
                intent.putExtra("isFile", String.valueOf(isFile));
                intent.putExtra("isCopy", "cut");
                packageContext.startActivity(intent);
            }
        });

        AlertDialog.setNeutralButton("重命名", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, int which) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(packageContext);
                final View dialogView = View.inflate(packageContext, R.layout.dialog_name, null);
                final EditText editText;
                editText = dialogView.findViewById(R.id.editName);
                editText.setText(fileInfo.getFileName());
                builder.setView(dialogView);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fileRename = editText.getText().toString();
                        FileNameCheck fileNameCheck = new FileNameCheck();
                        fileNameCheck.setFileInfoList(fileInfoList);
                        fileNameCheck.setFileRename(fileRename);
                        String checkRes = fileNameCheck.fileNameCheck();
                        if (checkRes.equals("")) {
                            RenameFileHttp renameFileHttp = new RenameFileHttp();
                            renameFileHttp.setOldFilePath(AppData.cloudCurrentDirectory + "/" + fileInfo.getFileName());
                            renameFileHttp.setNewFilePath(AppData.cloudCurrentDirectory + "/" + fileRename);
                            new Thread(renameFileHttp).start();
                        } else {
                            Toast.makeText(packageContext, checkRes, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setTitle("重命名");
                builder.create().show();
            }
        });

        AlertDialog.setTitle("选择操作");
        AlertDialog.setMessage("文件操作选择：");
        AlertDialog.show();
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public android.app.AlertDialog.Builder getAlertDialog() {
        return AlertDialog;
    }

    public void setAlertDialog(android.app.AlertDialog.Builder alertDialog) {
        AlertDialog = alertDialog;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public List<FileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void setFileInfoList(List<FileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }
}
