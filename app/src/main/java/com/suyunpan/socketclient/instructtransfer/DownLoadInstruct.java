package com.suyunpan.socketclient.instructtransfer;

import com.suyunpan.appdata.AppData;
import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.socketclient.filetransfer.DownLoadFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DownLoadInstruct implements Runnable {
    private FileInfo fileInfo;
    private String IP = AppData.URL;
    private int PORT = 9999;
    private Socket socket = null;
    private DataOutputStream dos;
    private DataInputStream dis;

    @Override
    public void run() {
        try {

            socket = new Socket(IP, PORT);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("DOWNLOAD");//1.发送一个DOWNLOAD
            dos.flush();
            String respone = dis.readUTF();//2.期待读到一个DOWNLOAD_OK
            if ("DOWNLOAD_OK".equals(respone)) {
                DownLoadFile downLoadFile = new DownLoadFile();
                fileInfo.setFileTransferRate("0%");
                downLoadFile.setFileInfo(fileInfo);
                downLoadFile.setSocket(socket);
                Thread thread = new Thread(downLoadFile);
                thread.start();
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
}
