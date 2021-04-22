package com.suyunpan.filesystem;

import android.os.Message;

import com.suyunpan.home.deletefile.DeleteFileActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeleteLocal implements Runnable {

    private String path = "/storage/emulated/0/AndroidPanDown";
    private List<FileInfo> fileInfoList = new ArrayList<>();

    private FileAdapter fileAdapter;

    @Override
    public void run() {
        File file = new File(path);
        File[] listFiles = file.listFiles();
        for (File f : listFiles) {                    //遍历File[]数组
            FileInfo fileInfo = new FileInfo();
            if (f.isFile()) {
                fileInfo.setFileName(f.getName());
                fileInfo.setFileOperation("删除文件");
                fileInfo.setFileSize(String.valueOf(f.length()));
            } else {
                fileInfo.setFileSize("-1");
                fileInfo.setFileName(f.getName());
                fileInfo.setFileOperation("删除文件夹");
            }
            fileInfoList.add(fileInfo);
        }
        SetFileSizeIcon.setFileIc(fileInfoList);
        fileAdapter.notifyDataSetChanged();
        Message message = new Message();
        message.what = 1;
        DeleteFileActivity.handler.sendMessage(message);
    }

    public FileAdapter getFileAdaper() {
        return fileAdapter;
    }

    public void setFileAdaper(FileAdapter fileAdapter) {
        this.fileAdapter = fileAdapter;
    }

}
