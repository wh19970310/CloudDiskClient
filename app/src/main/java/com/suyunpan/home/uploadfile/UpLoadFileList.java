package com.suyunpan.home.uploadfile;

import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.filesystem.SetFileSizeIcon;

import java.io.File;
import java.util.List;

public class UpLoadFileList {

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

    public String getLocalFile(List<FileInfo> fileInfoList) {

        fileInfoList.clear();
        File file = new File(path);
        File[] listFiles = file.listFiles();
        for (File f : listFiles) {                    //遍历File[]数组
            FileInfo fileInfo = new FileInfo();
            if (f.isFile()) {
                fileInfo.setFileOperation("上传");
                fileInfo.setFileSize(String.valueOf(f.length()));
            } else {
                fileInfo.setFileSize("-1");
                fileInfo.setFileOperation("打开");
            }
            fileInfo.setFileName(f.getName());
            fileInfo.setFilePosition("local");
            fileInfoList.add(fileInfo);
        }
        SetFileSizeIcon.setFileIc(fileInfoList);
        return "refresh";
    }
}
