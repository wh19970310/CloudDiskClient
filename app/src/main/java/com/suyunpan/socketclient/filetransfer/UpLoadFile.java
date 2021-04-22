package com.suyunpan.socketclient.filetransfer;

import android.os.Message;

import com.suyunpan.appdata.AppData;
import com.suyunpan.filesystem.BtnFileInfoClick;
import com.suyunpan.filesystem.FileAdapter;
import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.home.transfer.TransferActivity;
import com.suyunpan.http.EndUpLoadHttp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.Iterator;

public class UpLoadFile implements Runnable, BtnFileInfoClick.CallPause {

    private String MD5;
    private FileInfo fileInfo;
    private String path;
    private RandomAccessFile rad;//方便跳着读文件
    private Socket socket = null;
    private DataOutputStream dos;
    private DataInputStream dis;
    private byte[] buf = new byte[2 * 1024];
    private long hasUpSize;
    public static FileAdapter fileAdaper = null;
    private boolean isSet = false;

    @Override
    public void run() {
        int index = 0;

        try {
            rad = new RandomAccessFile(path, "r");
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            long offset = hasUpSize;//字节偏移量
            int length;
            fileInfo.setFileOperation("暂停");
            Iterator iterator = AppData.upLoadFileInfoList.iterator();
            while (iterator.hasNext()) {
                FileInfo cursorFileInfo = (FileInfo) iterator.next();
                if (cursorFileInfo.getFileName().equals(fileInfo.getFileName())) {
                    iterator.remove();
                    break;
                }
                index++;
            }
            AppData.upLoadFileInfoList.add(index, fileInfo);
            if (offset < rad.length()) {
                rad.seek(offset);
                while ((length = rad.read(buf)) > 0) {
                    if (!isSet && fileAdaper != null) {
                        BtnFileInfoClick.callPause = this;
                        isSet = true;
                    }
                    dos.write(buf, 0, length);
                    dos.flush();
                    hasUpSize += length;
                    fileInfo.setFileTransferRate((int) (((hasUpSize + 0.0) / rad.length()) * 100) + "%");
                    if (TransferActivity.handler != null) {
                        Message message = new Message();
                        message.what = 2;
                        TransferActivity.handler.sendMessage(message);
                    }
                }
            }
            dis.close();
            dos.close();
            rad.close();
            socket.close();
            EndUpLoadHttp endUpLoadHttp = new EndUpLoadHttp();
            endUpLoadHttp.setMD5(MD5);
            endUpLoadHttp.setFileInfo(fileInfo);
            new Thread(endUpLoadHttp).start();
        } catch (IOException e) {
            try {
                dis.close();
                dos.close();
                rad.close();
                socket.close();
            } catch (IOException ee) {
            }
        } finally {
            try {
                dis.close();
                dos.close();
                rad.close();
                socket.close();
            } catch (IOException e) {
            }

        }
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public String getMD5() {
        return MD5;
    }

    public void setMD5(String MD5) {
        this.MD5 = MD5;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getHasUpSize() {
        return hasUpSize;
    }

    public void setHasUpSize(long hasUpSize) {
        this.hasUpSize = hasUpSize;
    }

    public static FileAdapter getFileAdaper() {
        return fileAdaper;
    }

    public static void setFileAdaper(FileAdapter fileAdaper) {
        UpLoadFile.fileAdaper = fileAdaper;
    }

    @Override
    public void pause(String pause) throws IOException {
        if (("pause" + fileInfo.getFileName()).equals(pause)) {
            fileInfo.setFileTransferRate("暂停");
            fileInfo.setFileOperation("继续");
            dos.close();
            dis.close();
            rad.close();
            socket.close();
            if (TransferActivity.handler != null) {
                Message message = new Message();
                message.what = 2;
                TransferActivity.handler.sendMessage(message);
            }
        }
    }
}
