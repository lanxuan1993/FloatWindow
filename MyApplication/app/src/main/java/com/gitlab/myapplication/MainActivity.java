package com.gitlab.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.gitlab.myapplication.floatwindow.FloatActionController;
import com.gitlab.myapplication.floatwindow.permission.FloatPermissionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.tv1);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isPermission = FloatPermissionManager.getInstance().applyFloatWindow(MainActivity.this);
                //有对应权限或者系统版本小于7.0
                if (isPermission || Build.VERSION.SDK_INT < 24) {
                    //开启悬浮窗
                    FloatActionController.getInstance().startFloatServer(MainActivity.this);
                }else{
                    FloatActionController.getInstance().showPermissionDialog(MainActivity.this);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (!FloatActionController.getInstance().hasPermission(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                FloatActionController.getInstance().startFloatServer(MainActivity.this);
            }
        }
    }
}
