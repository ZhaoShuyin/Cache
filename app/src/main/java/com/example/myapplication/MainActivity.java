package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.layout.BlueToothActivity;
import com.example.myapplication.view.ABCView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*findViewById(R.id.tv_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BlueToothActivity.class));
            }
        });*/
        final ABCView abcView = findViewById(R.id.abcview);
        abcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = new Random().nextInt(90) + 5;
                abcView.setValue(value);
                Toast.makeText(MainActivity.this, "开启动画"+value, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
