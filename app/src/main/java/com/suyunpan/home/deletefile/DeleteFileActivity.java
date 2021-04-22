package com.suyunpan.home.deletefile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.suyunpan.R;
import com.suyunpan.appdata.AppData;
import com.suyunpan.filesystem.CloudFileOperation;
import com.suyunpan.filesystem.FileAdapter;
import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.filesystem.SetFileSizeIcon;
import com.suyunpan.http.DeleteCloudListHttp;
import com.suyunpan.login.LoginInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeleteFileActivity extends AppCompatActivity {

    public static String jsonString;
    private ListView listViewDeleteFile;
    private FileAdapter localFileAdapter;
    private FileAdapter cloudFileAdapter;
    public static Handler handler;
    private String path = "/storage/emulated/0/AndroidPanDown";
    private List<FileInfo> fileInfoList = new ArrayList<>();
    private AlertDialog.Builder deleteAlertDialog;
    private SetDeleteList setDeleteList;
    private boolean isDeleteLocal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletefile);
        deleteAlertDialog = new AlertDialog.Builder(DeleteFileActivity.this);
        listViewDeleteFile = findViewById(R.id.listShareFile);//绑定列表
        localFileAdapter = new FileAdapter(this, fileInfoList, R.layout.item_fileinfo);// 初始化本地文件列表适配器
        cloudFileAdapter = new FileAdapter(this, fileInfoList, R.layout.item_fileinfo);// 初始化云端文件列表适配器

        if (isDeleteLocal) {
            setTitle("删除本地文件");
            fileInfoList.clear();
            listViewDeleteFile.setAdapter(localFileAdapter);// 为列表设置适配器
            setDeleteList = new SetDeleteList();
            setDeleteList.setPath(path);
            if (setDeleteList.refreshList(fileInfoList).equals("refresh")) {
                localFileAdapter.notifyDataSetChanged();
            }
        } else {
            setTitle("删除云端文件");
            fileInfoList.clear();
            listViewDeleteFile.setAdapter(cloudFileAdapter);
            cloudFileAdapter.notifyDataSetChanged();
        }

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.obj != null && !msg.obj.equals("") && isDeleteLocal) {//非空字符串
                    DeleteLocalAltertDialog deleteLocalAltertDialog = new DeleteLocalAltertDialog();
                    deleteLocalAltertDialog.setDeleteFile(new File((String) msg.obj));
                    deleteLocalAltertDialog.setAlertDialog(deleteAlertDialog);
                    deleteLocalAltertDialog.showDialogDeleteLocal();
                } else if (msg.what == 1 && isDeleteLocal) {//刷新本地
                    fileInfoList.clear();
                    if (setDeleteList.refreshList(fileInfoList).equals("refresh")) {
                        localFileAdapter.notifyDataSetChanged();
                    }
                    Toast.makeText(DeleteFileActivity.this, "文件已删除", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 1 && !isDeleteLocal) {//刷新云端
                    List<FileInfo> list = JSON.parseArray(jsonString, FileInfo.class);
                    fileInfoList.clear();
                    fileInfoList.addAll(list);
                    CloudFileOperation cloudFileOperation = new CloudFileOperation();
                    cloudFileOperation.setFileInfoList(fileInfoList);
                    cloudFileOperation.setDeleteFileOperation();
                    SetFileSizeIcon.setFileIc(fileInfoList);
                    cloudFileAdapter.notifyDataSetChanged();
                    if (msg.obj != null && ((String) msg.obj).equals("deletesuccess")) {
                        Toast.makeText(DeleteFileActivity.this, "文件已删除", Toast.LENGTH_SHORT).show();
                    }
                } else if (msg.what == 0 && msg.obj != null) {
                    DeleteLocalAltertDialog deleteLocalAltertDialog = new DeleteLocalAltertDialog();
                    deleteLocalAltertDialog.setResourcePath(AppData.cloudCurrentDirectory + "/" + msg.obj);
                    deleteLocalAltertDialog.setUserId(LoginInfo.getUserId());
                    deleteLocalAltertDialog.setAlertDialog(deleteAlertDialog);
                    deleteLocalAltertDialog.showDialogDeleteCloud();
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
                isDeleteLocal = !isDeleteLocal;
                if (!isDeleteLocal) {// 删除云端文件界面
                    setTitle("删除云端文件");
                    fileInfoList.clear();
                    listViewDeleteFile.setAdapter(cloudFileAdapter);
                    new Thread(new DeleteCloudListHttp()).start();
                } else { // 删除本地文件界面
                    setTitle("删除本地文件");
                    fileInfoList.clear();
                    listViewDeleteFile.setAdapter(localFileAdapter);// 为列表设置适配器
                    setDeleteList = new SetDeleteList();
                    setDeleteList.setPath(path);
                    if (setDeleteList.refreshList(fileInfoList).equals("refresh")) {
                        localFileAdapter.notifyDataSetChanged();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
