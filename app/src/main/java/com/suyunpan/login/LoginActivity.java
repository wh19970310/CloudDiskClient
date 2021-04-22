package com.suyunpan.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.suyunpan.R;
import com.suyunpan.http.LoginHttp;
import com.suyunpan.main.MainActivity;
import com.suyunpan.retrieve.RetrieveActivity;
import com.suyunpan.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    public static Handler handler;
    private EditText textUserName;
    private EditText textPassWord;
    private Button btnSingUp;
    private Button btnLogin;
    private TextView textRetrieve;
    private CheckBox checkBoxRememberPwd;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");

        checkBoxRememberPwd = findViewById(R.id.checkBoxRemberPwd);
        textUserName = findViewById(R.id.textUserName);
        textPassWord = findViewById(R.id.textPassWord);
        btnSingUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        textRetrieve = findViewById(R.id.retrieve);

        pref = getSharedPreferences("prefRememberPwd", Context.MODE_PRIVATE);
        editor = pref.edit();

        boolean rememberPwd = pref.getBoolean("rememberPwd", false);
        if (rememberPwd) {//如果为true
            String userNames = pref.getString("userName", null);
            String passWords = pref.getString("passWord", null);
            textUserName.setText(userNames);
            textPassWord.setText(passWords);
            checkBoxRememberPwd.setChecked(true);
        } else {
            checkBoxRememberPwd.setChecked(false);
        }

        View.OnClickListener mylistener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnLogin: {
                        String userId = String.valueOf(textUserName.getEditableText());
                        String passWord = String.valueOf(textPassWord.getEditableText());
                        LoginInfo.setUserId(userId);
                        LoginInfo.setPwd(passWord);
                        new Thread(new LoginHttp()).start();
                        break;
                    }
                    case R.id.btnSignUp: {
                        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                        break;
                    }
                    case R.id.retrieve: {
                        startActivity(new Intent(LoginActivity.this, RetrieveActivity.class));
                        break;
                    }
                }
            }
        };

        btnLogin.setOnClickListener(mylistener);
        btnSingUp.setOnClickListener(mylistener);
        textRetrieve.setOnClickListener(mylistener);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    if (checkBoxRememberPwd.isChecked()) {
                        editor.putBoolean("rememberPwd", true);
                        editor.putString("userName", textUserName.getEditableText().toString());
                        editor.putString("passWord", textPassWord.getEditableText().toString());
                        editor.commit();
                    }
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else if (msg.what == -1) {
                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    editor.clear().commit();
                } else if (msg.what == -2) {
                    Toast.makeText(LoginActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                    editor.clear().commit();
                }
            }

        };
    }
}