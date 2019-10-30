package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.layout.BlueToothActivity;
import com.example.myapplication.view.ABCView;
import com.example.myapplication.view.EcgView;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Android 1.0 开始支持 OpenGL ES 1.0 及 1.1
 * Android 2.2 开始支持 OpenGL ES 2.0
 * Android 4.3 开始支持 OpenGL ES 3.0
 * Android 5.0 开始支持 OpenGL ES 3.1
 */
public class MainActivity extends AppCompatActivity {

    ConcurrentLinkedQueue<Short> queue = new ConcurrentLinkedQueue<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EcgView ecgView = findViewById(R.id.ecg_view);
        ecgView.setQueue(queue);
    }


}
