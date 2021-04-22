package com.suyunpan.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AboutDialog {

    private android.app.AlertDialog.Builder AlertDialog;

    public void showNameDialog(final Context packageContext) {

        AlertDialog = new AlertDialog.Builder(packageContext);

        AlertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog.setTitle("关于软件");
        AlertDialog.setMessage("这是基于安卓开发的云盘软件，客户端使用到JetPack + ButterKnife技术。\n" +
                "\n服务器端使用了SpringBoot + MySQL + Redis + Mybatis技术。");
        AlertDialog.show();
    }

    public android.app.AlertDialog.Builder getAlertDialog() {
        return AlertDialog;
    }

    public void setAlertDialog(android.app.AlertDialog.Builder alertDialog) {
        AlertDialog = alertDialog;
    }

}
