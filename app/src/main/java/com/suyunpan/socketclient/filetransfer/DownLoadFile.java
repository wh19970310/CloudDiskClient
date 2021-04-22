package com.suyunpan.socketclient.filetransfer;

import android.os.Message;

import com.suyunpan.filesystem.BtnFileInfoClick;
import com.suyunpan.filesystem.FileAdapter;
import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.home.transfer.TransferActivity;
import com.suyunpan.http.DownLoadRateHttp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;

public class DownLoadFile implements Runnable, BtnFileInfoClick.CallPause {

    private FileInfo fileInfo;
    private Socket socket = null;
    private DataOutputStream dos;
    private DataInputStream dis;
    private RandomAccessFile rad;
    private String path = "/storage/emulated/0/AndroidPanDown/";
    private File file;
    private long size = 0;
    private String respon;
    private int length;
    private byte[] buf = new byte[2 * 1024];
    private Thread thread;
    private boolean isSet = false;
    public static FileAdapter fileAdaper = null;

    @Override
    public void run() {
        DownLoadRateHttp downLoadRateHttp = new DownLoadRateHttp();
        downLoadRateHttp.setFileInfo(fileInfo);
        thread = new Thread(downLoadRateHttp);
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF(fileInfo.getFileMD5());//发送了文件MD5
            dos.flush();
            respon = dis.readUTF();
            if ("OK_FILEMD5".equals(respon)) {
                thread.start();
                path = path + fileInfo.getFileName();
                file = new File(path);
                if (file.exists() && file.isFile()) {
                    size = file.length();
                } else {
                    file.createNewFile();
                }
                dos.writeLong(size);
                dos.flush();
                rad = new RandomAccessFile(path, "rw");
                rad.seek(size);
                while ((length = dis.read(buf, 0, buf.length)) != -1) {
                    rad.write(buf, 0, length);
                    if (!isSet && fileAdaper != null) {
                        BtnFileInfoClick.callPause = this;
                        isSet = true;
                    }
                }
                dos.close();
                dis.close();
                socket.close();
                rad.close();
            }

        } catch (IOException e) { /* Do Nothing */} finally {
            try {
                dos.close();
                dis.close();
                rad.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public static FileAdapter getFileAdaper() {
        return fileAdaper;
    }

    public static void setFileAdaper(FileAdapter fileAdaper) {
        DownLoadFile.fileAdaper = fileAdaper;
    }

    @Override
    public void pause(String pause) throws IOException {
        if (("pause" + fileInfo.getFileName()).equals(pause)) {
            dos.close();
            dis.close();
            rad.close();
            socket.close();
            Message message = new Message();
            message.what = 1;
            TransferActivity.handler.sendMessage(message);
        }
    }
}
