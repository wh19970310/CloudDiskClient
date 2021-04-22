package com.suyunpan.my.updateinfo;

import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;

public class MyEditWatcher implements TextWatcher {

    private String beforeStr;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeStr = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!beforeStr.equals(s.toString())) {
            Message message = new Message();
            message.what = 0;
            UpdateInfoActivity.handler.sendMessage(message);
        }
    }
}