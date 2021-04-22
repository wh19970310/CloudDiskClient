package com.suyunpan.home.sharefile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import com.suyunpan.R;
import com.suyunpan.http.*;

public class ShareDialog {

    private android.app.AlertDialog.Builder AlertDialog;

    private String shareLinkWithCode;
    private String isMyshare;
    private String shareLink;

    public void showDialog(final Context packageContext) {

        final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(packageContext);
        final View dialogView = View.inflate(packageContext, R.layout.dialog_name, null);
        final EditText editText;
        editText = dialogView.findViewById(R.id.editName);
        builder.setView(dialogView);
        editText.setText(shareLinkWithCode);

        if (shareLinkWithCode.equals("分享出错！")) {
            editText.setVisibility(View.GONE);
            builder.setMessage(shareLinkWithCode);
        }

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        if ("myShare".equals(isMyshare)) {
            builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DeleteShareLinkHttp deleteShareLink = new DeleteShareLinkHttp();
                    deleteShareLink.setPackageContext(packageContext);
                    deleteShareLink.setShareLink(shareLink);
                    new Thread(deleteShareLink).start();
                }
            });
        }

        builder.setTitle("分享链接");
        builder.show();

    }

    public void setIsMyshare(String isMyshare) {
        this.isMyshare = isMyshare;
    }

    public String getShareLinkWithCode() {
        return shareLinkWithCode;
    }

    public void setShareLinkWithCode(String shareLinkWithCode) {
        this.shareLinkWithCode = shareLinkWithCode;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }
}


