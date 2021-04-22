package com.suyunpan.my.mysharelink;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suyunpan.R;
import com.suyunpan.utils.model.ShareFile;

import java.util.List;

public class ShareAdapter extends BaseAdapter {

    private Context context;
    private List<ShareFile> myShareList;  //定义数据源
    private int resourceId;   //定义布局资源Id
    private ShareAdapter.ViewHolder viewHolder = null;

    class ViewHolder {
        TextView textFileName;
        TextView textShareCode;
    }

    public ShareAdapter(Activity context, List<ShareFile> list, int resourceId) {
        this.context = context;
        this.myShareList = list;
        this.resourceId = resourceId;
    }


    @Override
    public int getCount() {
        return myShareList.size();
    }

    @Override
    public Object getItem(int position) {
        return myShareList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resourceId, null);
            viewHolder = new ShareAdapter.ViewHolder();
            viewHolder.textFileName = convertView.findViewById(R.id.textShareFileName);
            viewHolder.textShareCode = convertView.findViewById(R.id.textShareCode);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ShareAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.textFileName.setText(myShareList.get(position).getFilename());
        viewHolder.textShareCode.setText(myShareList.get(position).getSharecode());

        return convertView;
    }
}
