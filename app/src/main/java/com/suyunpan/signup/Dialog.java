package com.suyunpan.signup;

import android.content.DialogInterface;
import android.os.Message;

import com.suyunpan.utils.model.User;

public class Dialog {

    private android.app.AlertDialog.Builder AlertDialog;
    private User user;

    public void showDialog() {

        AlertDialog.setPositiveButton("去登录", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message message = new Message();
                message.what = 4;
                SignUpActivity.handler.sendMessage(message);
            }
        });

        AlertDialog.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog.setTitle("注册成功");
        AlertDialog.setMessage("您的账号为： " + user.getUserid());
        AlertDialog.show();
    }


    public android.app.AlertDialog.Builder getAlertDialog() {
        return AlertDialog;
    }

    public void setAlertDialog(android.app.AlertDialog.Builder alertDialog) {
        AlertDialog = alertDialog;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
