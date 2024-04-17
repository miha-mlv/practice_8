package com.example.practice8;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.practice8.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button startBtn;
    ImageView downloadedImage, downloadedImage2, image;
    private OneTimeWorkRequest loadImageRequest2 = new OneTimeWorkRequest.Builder(MyWorker.class).build();
    private OneTimeWorkRequest loadImageRequest1 = new OneTimeWorkRequest.Builder(MyWorker.class).build();
    private List<OneTimeWorkRequest> listRequests = new ArrayList<>();
    private String imageUrl = "https://random.dog/woof.json";
    private Bitmap mIcon;
    Thread thread1, thread2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadInSeparateThread();
                //Создаем потоки и указываем ему задачи, которые он должен будет совершить
                thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadInSeparateThread();
                    }
                });
                thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadInSeparateThread();
                    }
                });
                //Запускаем потоки и для отличия двух потоков, одному из потоков задаем имя
                thread1.setName("1");
                thread1.start();
                thread2.start();
            }
        });
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(loadImageRequest1.getId()).observe(
                this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {

                    }
                }
        );

    }
    private void loadInSeparateThread() {

        try {
            Log.v("Image", "Loading");
            //
            final Bitmap bitmap = loadImageFromNetwork(imageUrl);
            if (bitmap != null) {
                if (Thread.currentThread().getName() == "1") {
                    downloadedImage.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("Image", "Loaded");
                            downloadedImage.setImageBitmap(bitmap);
                        }
                    });
                } else {
                    downloadedImage2.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("Image", "Loaded");
                            downloadedImage2.setImageBitmap(bitmap);
                        }
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Bitmap loadImageFromNetwork(String imageUrl) throws IOException {
        Log.v("Image", "2 loading");
        try {
            //Скачиваем фотографию по прямой ссылке
            InputStream in = new URL("https://random.dog/adbf2147-7bfa-4933-b3be-c42358c2fecb.jpg").openStream();
            mIcon = BitmapFactory.decodeStream(in);
            if(mIcon != null) { in.close(); }
            return mIcon;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void init()
    {
        downloadedImage = findViewById(R.id.image1);
        startBtn = findViewById(R.id.startBtn);
        downloadedImage2 = findViewById(R.id.image2);
    }
}
