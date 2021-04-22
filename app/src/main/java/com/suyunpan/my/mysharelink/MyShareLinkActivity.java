package com.suyunpan.my.mysharelink;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.suyunpan.R;
import com.suyunpan.home.sharefile.ShareDialog;
import com.suyunpan.http.ShareListHttp;
import com.suyunpan.utils.model.ShareFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyShareLinkActivity extends AppCompatActivity {

    @BindView(R.id.listShareFile)
    ListView listShareFile;

    public static Handler handler;
    private List<ShareFile> shareList = new ArrayList<>();
    private ShareAdapter shareAdapter = new ShareAdapter(this, shareList, R.layout.item_sharelink);
    private ShareListHttp shareListHttp = new ShareListHttp();
    private ShareDialog shareDialog = new ShareDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysharelink);
        ButterKnife.bind(this);

        listShareFile.setAdapter(shareAdapter);
        new Thread(shareListHttp).start();

        listShareFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shareDialog.setIsMyshare("myShare");
                shareDialog.setShareLinkWithCode(shareList.get(position).getSharelink()
                        + "  分享码：" + shareList.get(position).getSharecode());
                shareDialog.setShareLink(shareList.get(position).getSharelink());
                shareDialog.showDialog(MyShareLinkActivity.this);
            }
        });

        handler = new Handler(msg -> {

            if (msg.obj != null) {
                shareList.clear();
                shareList.addAll(JSON.parseArray((String) msg.obj, ShareFile.class));
                shareAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "您还未分享任何文件！", Toast.LENGTH_SHORT).show();
            }
            return true;
        });

    }

}
