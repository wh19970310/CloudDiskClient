package com.suyunpan.home.sharefile;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.suyunpan.R;
import com.suyunpan.filesystem.FileAdapter;
import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.filesystem.SetFileSizeIcon;
import com.suyunpan.http.FileListHttp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareFileActivity extends AppCompatActivity {

    public static String jsonString;
    public static Handler handler;
    private List<FileInfo> fileInfoList = new ArrayList<>();
    private FileAdapter shareAdapter;
    private FileListHttp fileListHttp = new FileListHttp();
    private ShareDialog shareDialog = new ShareDialog();

    @BindView(R.id.listShareFile)
    ListView listShareFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharefile);
        ButterKnife.bind(this);

        shareAdapter = new FileAdapter(this, fileInfoList, R.layout.item_fileinfo);// 初始化适配器
        listShareFile.setAdapter(shareAdapter);
        fileListHttp.setForList("share");
        new Thread(fileListHttp).start();


        handler = new Handler(msg -> {
            if (msg.what == 0) {
                List<FileInfo> list = JSON.parseArray(jsonString, FileInfo.class);
                fileInfoList.clear();
                fileInfoList.addAll(list);
                SetShare.set(fileInfoList);
                SetFileSizeIcon.setFileIc(fileInfoList);
                shareAdapter.notifyDataSetChanged();
            }
            if (msg.obj != null) {
                shareDialog.setShareLinkWithCode((String) msg.obj);
                shareDialog.showDialog(ShareFileActivity.this);
            }
            return true;
        });
    }
}
