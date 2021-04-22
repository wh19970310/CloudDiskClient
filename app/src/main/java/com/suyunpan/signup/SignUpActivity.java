package com.suyunpan.signup;

import android.app.AlertDialog;
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
import com.suyunpan.http.SingUpHttp;
import com.suyunpan.utils.PassWordHash;
import com.suyunpan.utils.model.User;

import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class SignUpActivity extends AppCompatActivity {

    private AlertDialog.Builder alertDialog;
    public static Handler handler;
    private EditText editTextPhoneNum;
    private EditText editTextVerificationCode;
    private EditText editTextSignPassWord;
    private EditText editTextSignPassWordCheck;
    private TextView textViewGetCode;
    private Button btnSignUP;
    private String phoneNum;
    private String passWord;
    private String passWordCheck;
    private String checkCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editTextPhoneNum = findViewById(R.id.editTextPhoneNum);
        editTextVerificationCode = findViewById(R.id.editTextVerificationCode);
        editTextSignPassWord = findViewById(R.id.editTextSignPassWord);
        editTextSignPassWordCheck = findViewById(R.id.editTextSignPassWordCheck);
        textViewGetCode = findViewById(R.id.textViewGetCode);
        btnSignUP = findViewById(R.id.btnSignUP);
        alertDialog = new AlertDialog.Builder(SignUpActivity.this);
        btnSignUP.setOnClickListener(myListener);
        textViewGetCode.setOnClickListener(myListener);

        handler = new Handler(msg -> {
            if (msg.obj != null) {
                Dialog dialog = new Dialog();
                dialog.setUser((User) msg.obj);
                dialog.setAlertDialog(alertDialog);
                dialog.showDialog();
            }
            if (msg.what == 0) {
                textViewGetCode.setTextColor(0xFF000000);
                textViewGetCode.setEnabled(true);
            } else if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "短信已下发，请注意查收！", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                textViewGetCode.setEnabled(true);
                Toast.makeText(getApplicationContext(), "手机号已被注册！", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                Toast.makeText(getApplicationContext(), "注册失败，请检查信息！", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 4) {
                finish();
            }
            return true;
        });

    }

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            phoneNum = editTextPhoneNum.getEditableText().toString();
            checkCode = editTextVerificationCode.getEditableText().toString();
            passWord = editTextSignPassWord.getEditableText().toString();
            passWordCheck = editTextSignPassWordCheck.getEditableText().toString();

            switch (v.getId()) {
                case R.id.textViewGetCode:
                    if (phoneNum.equals("") || !CheckInfo.checkPhoneNum(phoneNum)) {
                        Toast.makeText(getApplicationContext(), "手机号不正确！", Toast.LENGTH_SHORT).show();
                    } else {
                        textViewGetCode.setTextColor(0xFF808080);
                        textViewGetCode.setEnabled(false);
                        if (CheckInfo.checkPhoneNum(phoneNum)) {
                            GetCodeHttp getCodeHttp = new GetCodeHttp();
                            getCodeHttp.setPhoneNum(phoneNum);
                            getCodeHttp.setForCode("signUp");
                            new Thread(getCodeHttp).start();
                        }

                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                message.what = 0;
                                SignUpActivity.handler.sendMessage(message);
                            }
                        };
                        timer.schedule(task, 60 * 1000);
                    }
                    break;
                case R.id.btnSignUP:

                    if (passWord != null && passWordCheck != null && !passWordCheck.equals(passWord)) {
                        Toast.makeText(getApplicationContext(), "密码不相同！", Toast.LENGTH_SHORT).show();
                    } else if (passWordCheck.equals(passWord) && (passWord.length() > 15 || passWord.length() < 6)) {
                        Toast.makeText(getApplicationContext(), "密码长度应该在6-15位之间", Toast.LENGTH_SHORT).show();
                    } else if (checkCode != null && !CheckInfo.checkCode(checkCode)) {
                        Toast.makeText(getApplicationContext(), "验证码不正确！", Toast.LENGTH_SHORT).show();
                    } else if (phoneNum.equals("") || !CheckInfo.checkPhoneNum(phoneNum)) {
                        Toast.makeText(getApplicationContext(), "手机号不正确！", Toast.LENGTH_SHORT).show();
                    } else {
                        PassWordHash passWordHash = new PassWordHash();
                        passWordHash.setPassWord(passWord);
                        try {
                            passWord = passWordHash.sha1();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        SingUpHttp singUpHttp = new SingUpHttp();
                        singUpHttp.setCheckCode(checkCode);
                        singUpHttp.setPassWord(passWord);
                        singUpHttp.setPhoneNum(phoneNum);
                        new Thread(singUpHttp).start();
                    }
                    break;
            }
        }
    };
}