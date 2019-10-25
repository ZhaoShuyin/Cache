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
