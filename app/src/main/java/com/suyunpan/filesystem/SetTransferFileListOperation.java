package com.suyunpan.filesystem;

import java.util.List;

public class SetTransferFileListOperation {
    public static void setFileOperation(List<FileInfo> fileInfoList) {
        for (FileInfo fileInfo : fileInfoList) {
            if (fileInfo.getFileTransferRate() != null && fileInfo.getFileTransferRate().equals("")) {
                continue;
            } else if (fileInfo.getFileTransferRate() != null && (fileInfo.getFileTransferRate().equals("下载完成") || fileInfo.getFileTransferRate().equals("100%"))) {
                continue;
            } else if (fileInfo.getFileOperation() != null && fileInfo.getFileOperation().equals("继续")) {
                continue;
            }
            if (!fileInfo.getFileTransferRate().equals("下载完成") && !fileInfo.getFileTransferRate().equals("100%")) {
                fileInfo.setFileOperation("暂停");
            }
        }
    }
}
