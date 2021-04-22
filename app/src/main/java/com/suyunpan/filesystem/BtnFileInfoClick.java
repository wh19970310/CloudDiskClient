package com.suyunpan.filesystem;

import android.os.Message;

import com.suyunpan.appdata.AppData;
import com.suyunpan.home.deletefile.DeleteFileActivity;
import com.suyunpan.home.uploadfile.UpLoadFileActivity;
import com.suyunpan.home.uploadfile.UpLoadFileList;
import com.suyunpan.http.FileListHttp;
import com.suyunpan.http.FileOperationHttp;
import com.suyunpan.http.ShareFileHttp;
import com.suyunpan.main.HomeFragment;
import com.suyunpan.socketclient.filetransfer.DownLoadFile;
import com.suyunpan.socketclient.filetransfer.UpLoadFile;
import com.suyunpan.socketclient.instructtransfer.DownLoadInstruct;
import com.suyunpan.utils.ReadMD5;

import java.io.IOException;
import java.util.Iterator;

public class BtnFileInfoClick {
    private static int index = 0;
    public static CallPause callPause;

    public interface CallPause {
        public void pause(String isPause) throws IOException;
    }

    public static void btnClick(FileInfo fileInfo, FileAdapter fileAdaper) {

        String deleteFilePath = "/storage/emulated/0/AndroidPanDown/";
        String path = "";

        if ("打开".equals(fileInfo.getFileOperation())) {
            if (fileInfo.isCloudOperation()) {
                path = AppData.cloudCurrentDirectory + "/" + fileInfo.getFileName();
                AppData.cloudCurrentDirectory = path;
                FileOperationHttp fileOperationHttp = new FileOperationHttp();
                fileOperationHttp.setPath(path);
                new Thread(fileOperationHttp).start();

            } else if (!fileInfo.getFilePosition().equals("local")) {
                FileListHttp fileListHttp = new FileListHttp();
                if (fileInfo.getFilePosition().equals("cloud")) {
                    fileListHttp.setForList("share");
                }
                path = AppData.cloudCurrentDirectory + "/" + fileInfo.getFileName();
                AppData.cloudCurrentDirectory = path;

                fileListHttp.setPath(path);
                new Thread(fileListHttp).start();
            } else {
                if (fileInfo.getFileName().equals("emulated")) {
                    fileInfo.setFileName(fileInfo.getFileName() + "/0");
                }
                path = AppData.localPath + "/" + fileInfo.getFileName();
                AppData.localPath = path;
                UpLoadFileList upLoadFileList = new UpLoadFileList();
                upLoadFileList.setPath(path);
                if (upLoadFileList.getLocalFile(AppData.localFileInfoList).equals("refresh") && UpLoadFileActivity.handler != null) {
                    Message message = new Message();
                    message.what = 1;
                    UpLoadFileActivity.handler.sendMessage(message);
                }
            }
        } else if ("下载".equals(fileInfo.getFileOperation())) {
            DownLoadInstruct downLoadInstruct = new DownLoadInstruct();
            downLoadInstruct.setFileInfo(fileInfo);
            new Thread(downLoadInstruct).start();
            Iterator iterator = AppData.downLoadFileInfoList.iterator();
            while (iterator.hasNext()) {
                FileInfo cursorFileInfo = (FileInfo) iterator.next();
                if (cursorFileInfo.getFileName().equals(fileInfo.getFileName())) {
                    iterator.remove();
                    break;
                }
                index++;
            }
            AppData.downLoadFileInfoList.add(index, fileInfo);
            index = 0;
            Message message = new Message();
            message.what = 0;
            HomeFragment.handler.sendMessage(message);

        } else if ("暂停".equals(fileInfo.getFileOperation())) {
            UpLoadFile.setFileAdaper(fileAdaper);
            DownLoadFile.setFileAdaper(fileAdaper);
            try {
                callPause.pause("pause" + fileInfo.getFileName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("继续".equals(fileInfo.getFileOperation())) {
            if (!fileInfo.getFilePosition().equals("local")) {
                DownLoadInstruct downLoadInstruct = new DownLoadInstruct();
                downLoadInstruct.setFileInfo(fileInfo);
                new Thread(downLoadInstruct).start();
                Iterator iterator = AppData.downLoadFileInfoList.iterator();
                while (iterator.hasNext()) {
                    FileInfo cursorFileInfo = (FileInfo) iterator.next();
                    if (cursorFileInfo.getFileName().equals(fileInfo.getFileName())) {
                        iterator.remove();
                        break;
                    }
                    index++;
                }
                fileInfo.setFileOperation("暂停");
                AppData.downLoadFileInfoList.add(index, fileInfo);
                index = 0;
            } else {//是上传文件的继续
                String md5Path = AppData.localPath + "/" + fileInfo.getFileName();
                fileInfo.setFileTransferRate("连接中…");
                ReadMD5 readMD5 = new ReadMD5();
                readMD5.setFileInfo(fileInfo);
                readMD5.setPath(md5Path);
                new Thread(readMD5).start();
            }
        } else if ("删除".equals(fileInfo.getFileOperation())) {
            if (fileInfo.getFilePosition().equals("local")) {//是本地文件
                Message message = new Message();
                message.obj = deleteFilePath + fileInfo.getFileName();//  0：询问是否删除
                DeleteFileActivity.handler.sendMessage(message);
            } else if (fileInfo.getFilePosition().equals("cloud") && fileInfo.isCloudOperation()) {
                Message message = new Message();
                message.obj = fileInfo.getFileName();//  0：询问是否删除
                message.what = 0;
                DeleteFileActivity.handler.sendMessage(message);
            }
        } else if ("上传".equals(fileInfo.getFileOperation())) {
            String md5Path = AppData.localPath + "/" + fileInfo.getFileName();
            ReadMD5 readMD5 = new ReadMD5();
            readMD5.setFileInfo(fileInfo);
            readMD5.setPath(md5Path);
            new Thread(readMD5).start();
        } else if ("分享".equals(fileInfo.getFileOperation())) {
            ShareFileHttp shareFileHttp = new ShareFileHttp();
            shareFileHttp.setFileMD5(fileInfo.getFileMD5());
            shareFileHttp.setFileName(fileInfo.getFileName());
            new Thread(shareFileHttp).start();
        }
    }
}