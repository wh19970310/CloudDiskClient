package com.suyunpan.utils;

import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.http.ReadyUpLoadHttp;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadMD5 implements Runnable {

    private String path;
    private String MD5;
    private FileInfo fileInfo;

    @Override
    public void run() {

        File file = new File(path);
        try {
            MD5 = DigestUtils.md5Hex(new FileInputStream(file));
            fileInfo.setFileMD5(MD5);
            ReadyUpLoadHttp readyUpLoadHttp = new ReadyUpLoadHttp();
            readyUpLoadHttp.setMD5(MD5);
            readyUpLoadHttp.setPath(path);
            readyUpLoadHttp.setFileInfo(fileInfo);
            readyUpLoadHttp.setFileSize(String.valueOf(file.length()));
            readyUpLoadHttp.setFileName(file.getName());
            new Thread(readyUpLoadHttp).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }
}
