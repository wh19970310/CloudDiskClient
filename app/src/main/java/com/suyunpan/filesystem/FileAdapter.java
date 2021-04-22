package com.suyunpan.filesystem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.suyunpan.R;

import java.util.List;

public class FileAdapter extends BaseAdapter {

    private Context context;
    private List<FileInfo> fileInfoList;  //定义数据源
    private int resourceId;   //定义布局资源Id
    private ViewHolder viewHolder = null;

    class ViewHolder {
        Button btnFileOperation;
        ImageView imgFileType;
        TextView textFileName;
        TextView textFileSize;
        TextView textFileTransferStat;
        boolean isCloudOperation = false;
    }

    public FileAdapter(Activity context, List<FileInfo> list, int resourceId) {
        this.context = context;
        this.fileInfoList = list;
        this.resourceId = resourceId;
    }

    @Override
    public int getCount() {
        return fileInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.imgFileType = (ImageView) view.findViewById(R.id.imgFileType);
            viewHolder.textFileName = (TextView) view.findViewById(R.id.textFileName);
            viewHolder.textFileSize = (TextView) view.findViewById(R.id.textFileSize);
            viewHolder.btnFileOperation = view.findViewById(R.id.btnFileOperation);
            viewHolder.textFileTransferStat = view.findViewById(R.id.textFileTransferStat);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.imgFileType.setImageResource(fileInfoList.get(position).getImgId());
        String fileName = fileInfoList.get(position).getFileName();
        if (fileName.length() > 14) {
            fileName = fileName.substring(0, 13) + "…";
        }
        viewHolder.isCloudOperation = fileInfoList.get(position).isCloudOperation();
        viewHolder.textFileName.setText(fileName);
        viewHolder.textFileSize.setText(fileInfoList.get(position).getFileSize());
        viewHolder.btnFileOperation.setText(fileInfoList.get(position).getFileOperation());
        viewHolder.textFileTransferStat.setText(fileInfoList.get(position).getFileTransferRate());
        viewHolder.btnFileOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BtnFileInfoClick.btnClick(fileInfoList.get((Integer) v.getTag()), FileAdapter.this);
            }
        });
        if (viewHolder.textFileTransferStat.getText().equals("下载完成") || viewHolder.textFileTransferStat.getText().equals("100%")) {
            viewHolder.textFileTransferStat.setText("");
            viewHolder.btnFileOperation.setVisibility(View.INVISIBLE);
        } else if (viewHolder.isCloudOperation && viewHolder.btnFileOperation.getText().equals("下载")) {
            viewHolder.btnFileOperation.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.btnFileOperation.setVisibility(View.VISIBLE);
        }
        viewHolder.btnFileOperation.setTag(position);
        return view;
    }
}