package com.suyunpan.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.suyunpan.R;
import com.suyunpan.appdata.AppData;
import com.suyunpan.http.GetShareHttp;
import com.suyunpan.http.UpdateHttp;

public class MyDialog {

    private android.app.AlertDialog.Builder AlertDialog;

    private String nickName;
    private String shareLink;
    private String shareCode;

    public void showNameDialog(final Context packageContext) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(packageContext);
        final View dialogView = View.inflate(packageContext, R.layout.dialog_name, null);
        final EditText editText;
        editText = dialogView.findViewById(R.id.editName);
        builder.setView(dialogView);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nickName = editText.getEditableText().toString();
                if (nickName.length() == 0) {
                    Toast.makeText(packageContext, "请输入昵称", Toast.LENGTH_SHORT).show();
                } else {
                    AppData.user.setNickname(nickName);
                    UpdateHttp updateHttp = new UpdateHttp();
                    updateHttp.setUser(AppData.user);
                    new Thread(updateHttp).start();
                }
            }
        });

        builder.setTitle("修改昵称");
        builder.show();

    }

    public void showShareDialog(final Context packageContext) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(packageContext);
        final View dialogView = View.inflate(packageContext, R.layout.dialog_sharelink, null);
        final EditText editShareLink = dialogView.findViewById(R.id.editShareLink);
        final EditText editShareCode = dialogView.findViewById(R.id.editShareCode);
        builder.setView(dialogView);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shareLink = editShareLink.getEditableText().toString();
                shareCode = editShareCode.getEditableText().toString();
                if (shareLink.length() == 0 || shareCode.length() != 4) {
                    Toast.makeText(packageContext, "请修正信息！", Toast.LENGTH_SHORT).show();
                } else {
                    GetShareHttp getShareHttp = new GetShareHttp();
                    getShareHttp.setShareLink(shareLink);
                    getShareHttp.setShareCode(shareCode);
                    getShareHttp.setPackageContext(packageContext);
                    new Thread(getShareHttp).start();
                }
            }
        });

        builder.setTitle("输入分享链接");
        builder.show();


    }
}
