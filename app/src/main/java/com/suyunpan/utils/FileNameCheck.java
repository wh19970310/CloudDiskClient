package com.suyunpan.utils;

import com.suyunpan.filesystem.FileInfo;

import java.util.Iterator;
import java.util.List;

public class FileNameCheck {

    private List<FileInfo> fileInfoList;
    private String fileRename;

    public String fileNameCheck() {
        Iterator iterator = fileInfoList.iterator();
        while (iterator.hasNext()) {
            FileInfo fileInfo = (FileInfo) iterator.next();
            if (fileInfo.getFileName().equals(fileRename)) {
                return "文件名不可相同！";
            } else if (fileRename == null || fileRename.equals("")) {
                return "文件名不可为空！";
            }
        }
        return "";
    }

    public List<FileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void setFileInfoList(List<FileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }

    public String getFileRename() {
        return fileRename;
    }

    public void setFileRename(String fileRename) {
        this.fileRename = fileRename;
    }
}
