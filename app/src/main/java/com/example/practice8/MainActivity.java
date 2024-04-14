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

public class MainActivity extends AppCompatActivity {
    public final String TAG = "RRR";
    Button bStart, btJustDoIt;
    private Data data = new Data.Builder().putString("key1", "String1").putInt("key2", 123).build();;
    private  OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class).setInputData(data).build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bStart = findViewById(R.id.btStart);
        btJustDoIt= findViewById(R.id.btJustDoIt);
        // устанавливаем обработчик на кнопку "Начать в потоке"
        btJustDoIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance(getApplicationContext()).enqueue(work);
            }
        });

        // устанавливаем обработчик на кнопку "Начать не в потоке"
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Work is in progress");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Work finished");
            }
        });
        //Получаем данные переданные из потока MyWorker
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(work.getId()).observe(
                this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        Log.d("RRR","State = " + workInfo.getState());
                        Log.d("RRR", "key="+workInfo.getOutputData().getString("key3"));
                        Log.d("RRR", "key="+workInfo.getOutputData().getInt("key0",0));
                    }
                }
        );

    }
}
