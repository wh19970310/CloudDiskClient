package com.suyunpan.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.suyunpan.R;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private AppBarConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this, R.id.fragment);
        configuration = new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, configuration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private boolean isExit = false;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //2s内按2次返回键退出
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {//第2次返回键
                finish();
            } else {//第一次返回键
                isExit = true;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
                Toast.makeText(this, "再按一次退出!", Toast.LENGTH_LONG).show();
                return true;// true:表示自己已经处理按键事件, false:表示系统处理
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
