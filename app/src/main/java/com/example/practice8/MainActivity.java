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
    private Integer i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //binding.startBtn.setEnabled(false);
                try {
                    OneTimeWorkRequest work1 = new OneTimeWorkRequest.Builder(MyWorker.class).build();
                    OneTimeWorkRequest work2 = new OneTimeWorkRequest.Builder(MyWorker.class).build();
                    OneTimeWorkRequest work3 = new OneTimeWorkRequest.Builder(MyWorker.class).build();
                    WorkManager.getInstance(MainActivity.this).beginWith(work1).then(work2).then(work3).enqueue();
                    WorkManager.getInstance(MainActivity.this).getWorkInfoByIdLiveData(work1.getId()).observe(
                            MainActivity.this, new Observer<WorkInfo>() {
                                @Override
                                public void onChanged(WorkInfo workInfo) {
                                    Log.d("RRR","state="+workInfo.getState());
                                    textCounter1.setText(workInfo.getOutputData().getString("key1")+i);
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                    i++;
                                }
                            }
                    );
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
    private void init()
    {
        textCounter1 = findViewById(R.id.textView);
        startBtn = findViewById(R.id.startBtn);
    }
}
