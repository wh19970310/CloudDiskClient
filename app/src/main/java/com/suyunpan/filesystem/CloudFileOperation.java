package com.suyunpan.filesystem;

import java.util.Iterator;
import java.util.List;

public class CloudFileOperation {

    private List<FileInfo> fileInfoList;
    private FileInfo fileInfo;

    public void setCopyCloudFileOperation() {
        Iterator iterator = fileInfoList.iterator();
        while (iterator.hasNext()) {
            fileInfo = (FileInfo) iterator.next();
            fileInfo.setCloudOperation(true);
        }
    }

    public void setDeleteFileOperation() {
        Iterator iterator = fileInfoList.iterator();
        while (iterator.hasNext()) {
            fileInfo = (FileInfo) iterator.next();
            fileInfo.setFilePosition("cloud");
            fileInfo.setCloudOperation(true);
            fileInfo.setFileOperation("删除");
        }
    }

    public List<FileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void setFileInfoList(List<FileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }
}
