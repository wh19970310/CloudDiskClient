package com.suyunpan.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.suyunpan.R;
import com.suyunpan.appdata.AppData;
import com.suyunpan.filesystem.FileSizeChange;
import com.suyunpan.http.GetUserInfoHttp;
import com.suyunpan.login.LoginActivity;
import com.suyunpan.my.mysharelink.MyShareLinkActivity;
import com.suyunpan.my.updateinfo.UpdateInfoActivity;
import com.suyunpan.utils.model.User;

public class MyFragment extends Fragment {
    public static Handler handler;
    private TextView textViewNickName;
    private TextView textViewFreeSpace;
    private TextView textViewUpdateInfo;
    private TextView textViewSearchFile;
    private TextView textViewInputShareLink;
    private TextView textViewMyShareLink;
    private TextView textViewSignOut;
    private TextView textViewAbout;
    private GetUserInfoHttp getUserInfoHttp = new GetUserInfoHttp();
    private MyDialog myDialog = new MyDialog();


    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_fragment, container, false);
        getUserInfoHttp.setUserId(String.valueOf(AppData.user.getUserid()));
        new Thread(getUserInfoHttp).start();
        textViewNickName = view.findViewById(R.id.textViewUserId);
        textViewFreeSpace = view.findViewById(R.id.textViewFreeSpace);
        textViewUpdateInfo = view.findViewById(R.id.textViewUpdateInfo);
        textViewSearchFile = view.findViewById(R.id.textViewUpdateNickName);
        textViewInputShareLink = view.findViewById(R.id.textViewInputShareLink);
        textViewMyShareLink = view.findViewById(R.id.textViewMyShareLink);
        textViewSignOut = view.findViewById(R.id.textViewSignOut);
        textViewAbout = view.findViewById(R.id.textViewAbout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textViewUpdateInfo.setOnClickListener(myListener);
        textViewSearchFile.setOnClickListener(myListener);
        textViewInputShareLink.setOnClickListener(myListener);
        textViewMyShareLink.setOnClickListener(myListener);
        textViewSignOut.setOnClickListener(myListener);
        textViewAbout.setOnClickListener(myListener);

        handler = new Handler((msg -> {
            if (msg.obj != null) {
                AppData.user = (User) msg.obj;
                textViewNickName.setText("昵称：" + AppData.user.getNickname());
                textViewFreeSpace.setText("剩余空间："
                        + FileSizeChange.sizeChange(Long.valueOf(AppData.user.getFreespace())) + "/100GB");
            } else if (msg.what == 0) {
                Toast.makeText(getActivity(), "昵称修改完成！", Toast.LENGTH_SHORT).show();
                textViewNickName.setText("昵称：" + AppData.user.getNickname());
            }
            return true;
        }));

    }


    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.textViewUpdateInfo:
                    startActivity(new Intent(getActivity(), UpdateInfoActivity.class));
                    break;
                case R.id.textViewUpdateNickName:
                    myDialog.showNameDialog(getContext());
                    break;
                case R.id.textViewInputShareLink:
                    myDialog.showShareDialog(getContext());
                    break;
                case R.id.textViewMyShareLink:
                    startActivity(new Intent(getActivity(), MyShareLinkActivity.class));
                    break;
                case R.id.textViewSignOut:
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                    break;
                case R.id.textViewAbout:
                    AboutDialog aboutDialog = new AboutDialog();
                    aboutDialog.showNameDialog(getContext());
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getUserInfoHttp.setUserId(String.valueOf(AppData.user.getUserid()));
        new Thread(getUserInfoHttp).start();
        textViewNickName.setText("昵称：" + AppData.user.getNickname());
        textViewFreeSpace.setText("剩余空间："
                + FileSizeChange.sizeChange(Long.valueOf(AppData.user.getFreespace())) + "/100GB");
    }
}