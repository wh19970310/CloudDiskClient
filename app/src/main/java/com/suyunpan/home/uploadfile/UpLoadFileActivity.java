package com.suyunpan.home.uploadfile;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.suyunpan.R;
import com.suyunpan.appdata.AppData;
import com.suyunpan.filesystem.FileAdapter;
import com.suyunpan.filesystem.FileInfo;

import java.util.ArrayList;
import java.util.List;

public class UpLoadFileActivity extends AppCompatActivity {

    private ListView listViewDeleteLocalFile;
    private FileAdapter fileAdaper;
    public static Handler handler;
    private String path = AppData.localPath;
    private List<FileInfo> fileInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletefile);
        listViewDeleteLocalFile = findViewById(R.id.listShareFile);//绑定列表
        fileAdaper = new FileAdapter(this, fileInfoList, R.layout.item_fileinfo);
        listViewDeleteLocalFile.setAdapter(fileAdaper);
        UpLoadFileList upLoadFileList = new UpLoadFileList();
        upLoadFileList.setPath(path);
        if (upLoadFileList.getLocalFile(AppData.localFileInfoList).equals("refresh")) {
            fileInfoList.clear();
            fileInfoList.addAll(AppData.localFileInfoList);
            fileAdaper.notifyDataSetChanged();
        }
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.obj != null) {
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                } else if (msg.what == 1) {
                    fileInfoList.clear();
                    fileInfoList.addAll(AppData.localFileInfoList);
                    fileAdaper.notifyDataSetChanged();
                } else if (msg.what == 0) {
                    Toast.makeText(getApplicationContext(), "文件已秒传！", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 2) {
                    Toast.makeText(getApplicationContext(), "文件已存在！", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 3) {
                    Toast.makeText(getApplicationContext(), "剩余空间不足！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        AppData.localPath = "/storage/emulated/0";
        path = AppData.localPath;
        UpLoadFileList upLoadFileList = new UpLoadFileList();
        upLoadFileList.setPath(path);
        if (upLoadFileList.getLocalFile(AppData.localFileInfoList).equals("refresh")) {
            fileInfoList.clear();
            fileInfoList.addAll(AppData.localFileInfoList);
            fileAdaper.notifyDataSetChanged();
        }
    }
}
