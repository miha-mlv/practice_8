package com.example.practice8;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.practice8.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private TextView textCounter1, textCounter2, textCounter3;
    private Button startBtn;
    private ActivityMainBinding binding;
    private Handler handler1, handler2, handler3;
    private Thread thread1, thread2, thread3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initThread();
        init();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        handler1 = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                //Обработка сообщений, ориентируясь на имя потока
                if (Thread.currentThread().getName() == "thread1") {
                    Log.d("Counter1", "set value");
                    int n = msg.getData().getInt("key");
                    textCounter1.setText("N: " + n);
                } else if (Thread.currentThread().getName() == "thread2") {
                    int n = msg.getData().getInt("key");
                    Log.d("Counter2", "set value");
                    textCounter2.setText("N: " + n);
                } else if (Thread.currentThread().getName() == "thread3") {
                    int n = msg.getData().getInt("key");
                    Log.d("Counter3", "set value");
                    textCounter3.setText("N: " + n);
                }
                // здесь мы будем ждать сообщения из другого потока
                //int n = msg.getData().getInt("key");
                //binding.textCounter1.setText("N: "+n);
                //if(n==49) binding.startBtn.setEnabled(true);
            }
        };
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //binding.startBtn.setEnabled(false);
                try {
                    thread1.start();
                    thread2.start();
                    thread3.start();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //    public void doSlow() {
//        for(int i=0;i<50;i++) {
//            Message message = new Message();
//            Bundle bundle = new Bundle();
//            bundle.putInt("key",i);
//            message.setData(bundle);
//            handler1.sendMessage(message);
//        }
//    }
    private void initThread() {
        thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("key", i);
                    message.setData(bundle);
                    handler1.sendMessage(message);
                }
            }
        });
        thread1.setName("thread1");

        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("key", i);
                    message.setData(bundle);
                    handler1.sendMessage(message);
                }
            }
        });
        thread2.setName("thread2");

        thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 15; i++) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("key", i);
                    message.setData(bundle);
                    handler1.sendMessage(message);
                }
            }
        });
        thread3.setName("thread3");
    }

    private void init()
    {
        textCounter1 = findViewById(R.id.textCounter1);
        textCounter2 = findViewById(R.id.textCounter2);
        textCounter3 = findViewById(R.id.textCounter3);
        startBtn = findViewById(R.id.startBtn);
    }
}
