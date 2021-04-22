package com.suyunpan.home.transfer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.suyunpan.R;
import com.suyunpan.appdata.AppData;
import com.suyunpan.filesystem.FileAdapter;
import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.filesystem.SetTransferFileListOperation;
import com.suyunpan.http.DownLoadRateHttp;
import com.suyunpan.socketclient.filetransfer.DownLoadFile;
import com.suyunpan.socketclient.filetransfer.UpLoadFile;

import java.util.ArrayList;
import java.util.List;

public class TransferActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView tansferList;
    private FileAdapter downFileAdaper;
    private FileAdapter upFileAdaper;
    public static Handler handler;
    private List<FileInfo> fileTransferList = new ArrayList<>();
    private static boolean isDownList = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        tansferList = findViewById(R.id.listTransfer);
        downFileAdaper = new FileAdapter(this, fileTransferList, R.layout.item_fileinfo);
        upFileAdaper = new FileAdapter(this, fileTransferList, R.layout.item_fileinfo);

        if (isDownList) {
            tansferList.setAdapter(downFileAdaper);
            fileTransferList.clear();
            fileTransferList.addAll(AppData.downLoadFileInfoList);
            SetTransferFileListOperation.setFileOperation(fileTransferList);
            DownLoadRateHttp.fileAdaper = downFileAdaper;
            DownLoadFile.fileAdaper = downFileAdaper;
        } else {
            tansferList.setAdapter(upFileAdaper);
            setTitle("上传传输列表");
            fileTransferList.clear();
            fileTransferList.addAll(AppData.upLoadFileInfoList);
            SetTransferFileListOperation.setFileOperation(fileTransferList);
            upFileAdaper.notifyDataSetChanged();
            UpLoadFile.setFileAdaper(upFileAdaper);
        }


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1 && isDownList) {
                    fileTransferList.clear();
                    fileTransferList.addAll(AppData.downLoadFileInfoList);
                    SetTransferFileListOperation.setFileOperation(fileTransferList);
                    downFileAdaper.notifyDataSetChanged();
                } else if (msg.what == 2 && !isDownList) {//上传
                    fileTransferList.clear();
                    fileTransferList.addAll(AppData.upLoadFileInfoList);
                    SetTransferFileListOperation.setFileOperation(fileTransferList);
                    upFileAdaper.notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.change_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuChangeView:
                isDownList = !isDownList;//true->false
                if (!isDownList) {//如果是false说明是上传界面
                    tansferList.setAdapter(upFileAdaper);
                    setTitle("上传传输列表");
                    fileTransferList.clear();
                    fileTransferList.addAll(AppData.upLoadFileInfoList);
                    SetTransferFileListOperation.setFileOperation(fileTransferList);
                    upFileAdaper.notifyDataSetChanged();
                    UpLoadFile.setFileAdaper(upFileAdaper);
                } else {
                    setTitle("下载传输列表");
                    tansferList.setAdapter(downFileAdaper);
                    fileTransferList.clear();
                    fileTransferList.addAll(AppData.downLoadFileInfoList);
                    SetTransferFileListOperation.setFileOperation(fileTransferList);
                    DownLoadRateHttp.fileAdaper = downFileAdaper;
                    DownLoadFile.fileAdaper = downFileAdaper;
                    downFileAdaper.notifyDataSetChanged();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
