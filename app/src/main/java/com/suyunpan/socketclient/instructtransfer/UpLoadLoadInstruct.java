package com.suyunpan.socketclient.instructtransfer;

import com.suyunpan.appdata.AppData;
import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.socketclient.filetransfer.UpLoadFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class UpLoadLoadInstruct implements Runnable {

    private String MD5;
    private FileInfo fileInfo;
    private String IP = AppData.URL;
    private int PORT = 9999;
    private Socket socket = null;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String path;
    private long hasUpSize = 0;

    @Override
    public void run() {

        try {
            socket = new Socket(IP, PORT);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("UPLOAD");//1.发送一个DOWNLOAD
            dos.flush();
            String response = dis.readUTF();//2.期待读到一个UPLOAD_OK
            if ("UPLOAD_OK".equals(response)) {
                dos.writeUTF(MD5);
                dos.flush();
                if ((hasUpSize = dis.readLong()) != -1) {
                    UpLoadFile upLoadFile = new UpLoadFile();
                    upLoadFile.setMD5(MD5);
                    upLoadFile.setFileInfo(fileInfo);
                    upLoadFile.setSocket(socket);
                    upLoadFile.setPath(path);
                    upLoadFile.setHasUpSize(hasUpSize);
                    new Thread(upLoadFile).start();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}