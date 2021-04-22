package com.suyunpan.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.suyunpan.R;
import com.suyunpan.appdata.AppData;
import com.suyunpan.filesystem.FileAdapter;
import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.filesystem.SetFileSizeIcon;
import com.suyunpan.home.deletefile.DeleteFileActivity;
import com.suyunpan.home.sharefile.ShareFileActivity;
import com.suyunpan.home.transfer.TransferActivity;
import com.suyunpan.home.uploadfile.UpLoadFileActivity;
import com.suyunpan.http.CreateDirectoryHttp;
import com.suyunpan.http.FileListHttp;
import com.suyunpan.utils.TrimChar;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static Handler handler;
    public static String jsonString;
    private ListView listViewFileList;
    private List<FileInfo> fileInfoList = new ArrayList<>();
    private FileAdapter fileAdaper;
    private String path = "";
    private AlertDialog.Builder fileAlertDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppData.cloudCurrentDirectory = path;
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        listViewFileList = view.findViewById(R.id.listViewFileIist);
        setHasOptionsMenu(true);
        new Thread(new FileListHttp()).start();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.home_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuBack:
                AppData.cloudCurrentDirectory = new TrimChar().trimFirstAndLastChar();
                new Thread(new FileListHttp()).start();
                return true;
            case R.id.menuDelete:
                startActivity(new Intent().setClass(getContext(), DeleteFileActivity.class));
                return true;
            case R.id.menuTransfer:
                startActivity(new Intent().setClass(getContext(), TransferActivity.class));
                return true;
            case R.id.menuUpLoad:
                startActivity(new Intent().setClass(getContext(), UpLoadFileActivity.class));
                return true;
            case R.id.menuCreateDirectory:
                new Thread(new CreateDirectoryHttp()).start();
                return true;
            case R.id.menuShareFile:
                startActivity(new Intent().setClass(getContext(), ShareFileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fileAlertDialog = new AlertDialog.Builder(getActivity());
        fileAdaper = new FileAdapter(getActivity(), fileInfoList, R.layout.item_fileinfo);
        listViewFileList.setAdapter(fileAdaper);

        listViewFileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {//点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FileAlertDialog dialog = new FileAlertDialog();
                if (fileInfoList.get(position).getSizeInByte() != null) {//有实际大小即可，否则会空指针闪退
                    dialog.setFile(true);
                } else {
                    dialog.setFile(false);
                }
                dialog.setFileInfoList(fileInfoList);
                dialog.setAlertDialog(fileAlertDialog);
                dialog.setResourcePath(AppData.cloudCurrentDirectory + "/" + fileInfoList.get(position).getFileName());
                dialog.showDialog(fileInfoList.get(position), getContext());
            }
        });

        listViewFileList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//长按点击事件
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {//  1：刷新页面
                    List<FileInfo> list = JSON.parseArray(jsonString, FileInfo.class);
                    fileInfoList.clear();
                    fileInfoList.addAll(list);
                    SetFileSizeIcon.setFileIc(fileInfoList);
                    fileAdaper.notifyDataSetChanged();
                } else if (msg.what == 0) {//  0：下载提示
                    Toast.makeText(getActivity(), "开始下载，可在传输界面查看详情", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 2) {
                    Toast.makeText(getActivity(), "新建成功！", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 3) {
                    Toast.makeText(getActivity(), "重命名成功！", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new FileListHttp()).start();
    }
}