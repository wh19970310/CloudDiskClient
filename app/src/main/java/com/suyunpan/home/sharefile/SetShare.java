package com.suyunpan.home.sharefile;

import com.suyunpan.filesystem.FileInfo;

import java.util.Iterator;
import java.util.List;

public class SetShare {


    public static void set(List<FileInfo> fileInfoList) {
        FileInfo fileInfo = null;
        Iterator iterator = fileInfoList.iterator();
        while (iterator.hasNext()) {
            fileInfo = (FileInfo) iterator.next();
            if (!fileInfo.getFileSize().equals("-1")) {
                fileInfo.setFileOperation("分享");
            } else {
                fileInfo.setFilePosition("cloud");
            }
        }
    }
}
