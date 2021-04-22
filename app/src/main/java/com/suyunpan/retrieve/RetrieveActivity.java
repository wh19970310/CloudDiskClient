package com.suyunpan.retrieve;

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
import com.suyunpan.http.GetCodeHttp;
import com.suyunpan.http.RetrieveHttp;
import com.suyunpan.my.updateinfo.CheckUpdateInfo;
import com.suyunpan.utils.model.User;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RetrieveActivity extends AppCompatActivity {

    private String phoneNum;
    private String code;
    public static Handler handler;
    public static User user;

    @BindView(R.id.editRePhone)
    EditText editRePhone;
    @BindView(R.id.editReCode)
    EditText editReCode;
    @BindView(R.id.textReGetCode)
    TextView textReGetCode;
    @BindView(R.id.btnRetrieve)
    Button btnRetrieve;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);
        ButterKnife.bind(this);

        handler = new Handler(msg -> {
            if (msg.obj != null) {
                Toast.makeText(RetrieveActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0) {
                textReGetCode.setEnabled(true);
                textReGetCode.setTextColor(0xFF000000);
            } else if (msg.what == 1) {
                RetrieveDialog dialog = new RetrieveDialog();
                dialog.setUser(user);
                dialog.showDialog(RetrieveActivity.this);
            }
            return true;
        });

    }

    @OnClick({R.id.textReGetCode, R.id.btnRetrieve})
    public void onViewClicked(View view) {
        phoneNum = editRePhone.getEditableText().toString();
        code = editReCode.getEditableText().toString();
        switch (view.getId()) {
            case R.id.textReGetCode:
                if (CheckUpdateInfo.checkPhone(phoneNum)) {
                    GetCodeHttp getCodeHttp = new GetCodeHttp();
                    getCodeHttp.setPhoneNum(phoneNum);
                    getCodeHttp.setForCode("Retrieve");
                    new Thread(getCodeHttp).start();

                    textReGetCode.setEnabled(false);
                    textReGetCode.setTextColor(0xFF808080);
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = 0;
                            RetrieveActivity.handler.sendMessage(message);
                        }
                    };
                    timer.schedule(task, 60 * 1000);
                } else {
                    Toast.makeText(RetrieveActivity.this, "手机号不正确！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRetrieve:

                if (CheckUpdateInfo.checkPhone(phoneNum) && CheckUpdateInfo.checkCode(code)) {
                    RetrieveHttp retrieveHttp = new RetrieveHttp();
                    retrieveHttp.setPhoneNum(phoneNum);
                    retrieveHttp.setCode(code);
                    new Thread(retrieveHttp).start();
                } else {
                    Toast.makeText(getApplicationContext(), "信息不正确！", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
