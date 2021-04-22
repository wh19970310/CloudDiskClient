package com.suyunpan.retrieve;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import com.suyunpan.R;
import com.suyunpan.http.UpdateHttp;
import com.suyunpan.my.updateinfo.CheckUpdateInfo;
import com.suyunpan.utils.PassWordHash;
import com.suyunpan.utils.model.User;

import java.security.NoSuchAlgorithmException;

public class RetrieveDialog {


    private android.app.AlertDialog.Builder AlertDialog;

    private User user;
    private String pwd;

    public void showDialog(final Context packageContext) {

        final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(packageContext);
        final View dialogView = View.inflate(packageContext, R.layout.dialog_name, null);
        final EditText editText;
        editText = dialogView.findViewById(R.id.editName);
        builder.setView(dialogView);
        TextChangedListener.StringWatcher(editText);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pwd = editText.getEditableText().toString();
                if (CheckUpdateInfo.checkPassWord(pwd)) {
                    PassWordHash passWordHash = new PassWordHash();
                    passWordHash.setPassWord(pwd);
                    try {
                        user.setPwd(passWordHash.sha1());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    UpdateHttp updateHttp = new UpdateHttp();
                    updateHttp.setUser(user);
                    updateHttp.setForRetrieve("forRetrieve");
                    new Thread(updateHttp).start();
                }
                dialog.dismiss();
            }
        });

        builder.setTitle("忘记密码");
        builder.setMessage("账号为：" + user.getUserid());
        builder.show();

    }


    public void setUser(User user) {
        this.user = user;
    }

}


