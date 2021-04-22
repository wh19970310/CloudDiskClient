package com.suyunpan.my.updateinfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.suyunpan.R;
import com.suyunpan.appdata.AppData;
import com.suyunpan.http.GetCodeHttp;
import com.suyunpan.http.UpdateForCodeHttp;
import com.suyunpan.utils.PassWordHash;
import com.suyunpan.utils.model.User;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateInfoActivity extends AppCompatActivity {

    @BindView(R.id.textViewUserId)
    TextView textViewUserId;
    @BindView(R.id.editPhoneNum)
    EditText editPhoneNum;
    @BindView(R.id.editUserName)
    EditText editUserName;
    @BindView(R.id.editPassWord)
    EditText editPassWord;
    @BindView(R.id.editCheckCode)
    EditText editCheckCode;
    @BindView(R.id.editIdCardNum)
    EditText editIdCardNum;
    @BindView(R.id.textViewGetCode)
    TextView textViewGetCode;
    @BindView(R.id.btnUpdateInfo)
    Button btnUpdateInfo;

    public static Handler handler;
    private String userName;
    private String idCardNum;
    private String phone;
    private String passWord;
    private String checkCode;
    private String oldPassWord;
    private PassWordHash passWordHash = new PassWordHash();
    private User user = AppData.user;
    private UpdateForCodeHttp updateForCodeHttp = new UpdateForCodeHttp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateinfo);
        ButterKnife.bind(this);
        textViewGetCode.setTextColor(0xFF808080);
        textViewGetCode.setEnabled(false);

        textViewUserId.setText("账号：" + AppData.user.getUserid());
        editPassWord.setText(AppData.user.getPwd().substring(0, 9));
        editIdCardNum.setText(AppData.user.getIdcardnum());
        editPhoneNum.setText(AppData.user.getPhonenum());
        editUserName.setText(AppData.user.getUsername());
        oldPassWord = editPassWord.getEditableText().toString();

        editPassWord.addTextChangedListener(new MyEditWatcher());
        editIdCardNum.addTextChangedListener(new MyEditWatcher());
        editPhoneNum.addTextChangedListener(new MyEditWatcher());
        editUserName.addTextChangedListener(new MyEditWatcher());

        handler = new Handler(msg -> {
            if (msg.obj != null) {
                Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0) {
                textViewGetCode.setEnabled(true);
                textViewGetCode.setTextColor(0xFF000000);
            }
            return true;
        });
    }

    @OnClick({R.id.textViewGetCode, R.id.btnUpdateInfo})
    public void onViewClicked(View view) {

        passWord = editPassWord.getEditableText().toString();
        idCardNum = editIdCardNum.getEditableText().toString();
        userName = editUserName.getEditableText().toString();
        phone = editPhoneNum.getEditableText().toString();
        checkCode = editCheckCode.getEditableText().toString();

        switch (view.getId()) {
            case R.id.textViewGetCode:
                GetCodeHttp getCodeHttp = new GetCodeHttp();
                getCodeHttp.setPhoneNum(AppData.user.getPhonenum());
                getCodeHttp.setForCode("updateInfo");
                new Thread(getCodeHttp).start();
                textViewGetCode.setEnabled(false);
                textViewGetCode.setTextColor(0xFF808080);
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = 0;
                        UpdateInfoActivity.handler.sendMessage(message);
                    }
                };
                timer.schedule(task, 60 * 1000);
                break;
            case R.id.btnUpdateInfo:
                try {
                    if (CheckUpdateInfo.checkCode(checkCode)
                            && CheckUpdateInfo.checkId(idCardNum)
                            && CheckUpdateInfo.checkPassWord(passWord)
                            && CheckUpdateInfo.checkPhone(phone)) {

                        updateForCodeHttp.setPhoneNum(AppData.user.getPhonenum());
                        updateForCodeHttp.setCheckCode(checkCode);
                        user.setIdcardnum(idCardNum);
                        user.setPhonenum(phone);
                        user.setUsername(userName);

                        if (!oldPassWord.equals(editPassWord.getEditableText().toString())) {
                            passWordHash.setPassWord(passWord);
                            user.setPwd(passWordHash.sha1());
                        } else {
                            user.setPwd(AppData.user.getPwd());
                        }
                        updateForCodeHttp.setUser(user);
                        new Thread(updateForCodeHttp).start();

                    } else {
                        Toast.makeText(getApplicationContext(), "信息不正确！", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}