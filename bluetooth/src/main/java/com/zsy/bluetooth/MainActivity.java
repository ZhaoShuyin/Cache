package com.zsy.bluetooth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zsy.bluetooth.api.BasicActivity;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv_show);
    }

    public void waitService(View view) {
        BtTool.initAcceptService(this);//开启等待服务
    }


    public void receive(View view) {
        BtTool.receive(new BtTool.ServiceListener() {
            @Override
            public void received(final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(textView.getText().toString()+"\n 接收: "+s);
                    }
                });
            }
        });
    }


    public void send(View view) {
        String msg = String.valueOf(System.currentTimeMillis());
        BtTool.send(msg);
        textView.setText(textView.getText().toString()+"\n 发送: "+msg);
    }


    public void basicuse(View view) {
        startActivity(new Intent(this, BasicActivity.class));
    }
}
