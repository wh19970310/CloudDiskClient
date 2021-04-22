package com.suyunpan.home.movefile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.suyunpan.R;
import com.suyunpan.appdata.AppData;
import com.suyunpan.filesystem.CloudFileOperation;
import com.suyunpan.filesystem.FileAdapter;
import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.filesystem.SetFileSizeIcon;
import com.suyunpan.http.FileOperationHttp;
import com.suyunpan.http.MoveFileHttp;
import com.suyunpan.login.LoginInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MoveFileActivity extends AppCompatActivity {

    public static Handler handler;
    private static String cloudCurrentDirectory = "";
    private static String cloudCurrentFloder = "";
    private ListView listViewFileList;
    private List<FileInfo> fileInfoList = new ArrayList<>();
    private FileAdapter fileAdapter;
    private Button btnCopy;
    private String fileName;
    private String resourcePath;
    private boolean isFile;
    private String isCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloudfileoperation);
        Intent intent = getIntent();
        isCopy = intent.getStringExtra("isCopy");
        isFile = Boolean.parseBoolean(intent.getStringExtra("isFile"));
        fileName = intent.getStringExtra("fileName");
        resourcePath = intent.getStringExtra("resourcePath");
        cloudCurrentDirectory = AppData.cloudCurrentDirectory;
        AppData.cloudCurrentDirectory = "";
        btnCopy = findViewById(R.id.btnCopy);
        listViewFileList = findViewById(R.id.listCloudFileOperation);
        fileAdapter = new FileAdapter(this, fileInfoList, R.layout.item_fileinfo);
        listViewFileList.setAdapter(fileAdapter);

        FileOperationHttp fileOperationHttp = new FileOperationHttp();
        new Thread(fileOperationHttp).start();
        btnCopy.setOnClickListener(myListener);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj != null) {
                    if (msg.what == 1) {
                        Toast.makeText(MoveFileActivity.this, "粘贴完成", Toast.LENGTH_SHORT).show();
                    }
                    List<FileInfo> list = JSON.parseArray((String) msg.obj, FileInfo.class);
                    fileInfoList.clear();
                    SetFileSizeIcon.setFileIc(list);
                    CloudFileOperation cloudFileOperation = new CloudFileOperation();
                    cloudFileOperation.setFileInfoList(list);
                    cloudFileOperation.setCopyCloudFileOperation();
                    fileInfoList.addAll(list);
                    fileAdapter.notifyDataSetChanged();
                }
            }
        };
    }

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCopy:
                    MoveFileHttp moveFileHttp = new MoveFileHttp();
                    moveFileHttp.setUserId(LoginInfo.getUserId());
                    moveFileHttp.setFile(isFile);
                    moveFileHttp.setResourcePath(resourcePath);
                    moveFileHttp.setIsCopy(isCopy);
                    if (isFile) {
                        moveFileHttp.setTargetPath(AppData.cloudCurrentDirectory + "/" + fileName);
                    } else {
                        moveFileHttp.setTargetPath(AppData.cloudCurrentDirectory);
                    }
                    new Thread(moveFileHttp).start();
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            finish();
                        }
                    };
                    timer.schedule(task, 2000);
                    break;
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        AppData.cloudCurrentDirectory = cloudCurrentDirectory;
    }

    public static String getCloudCurrentDirectory() {
        return cloudCurrentDirectory;
    }

    public static void setCloudCurrentDirectory(String cloudCurrentDirectory) {
        MoveFileActivity.cloudCurrentDirectory = cloudCurrentDirectory;
    }

    public static String getCloudCurrentFloder() {
        return cloudCurrentFloder;
    }

    public static void setCloudCurrentFloder(String cloudCurrentFloder) {
        MoveFileActivity.cloudCurrentFloder = cloudCurrentFloder;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }
}