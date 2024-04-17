package com.example.practice8;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MyWorker extends Worker {
    public final String TAG = "MY_TAG";
    private ImageView imageView;
    Bitmap mIcon = null;
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        try {
            InputStream in = new URL("https://random.dog/woof.json").openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Log.v(TAG, "Work finished");
        Data data = new Data.Builder().put("key",mIcon).build();
        return Worker.Result.success();//success(data)
    }
}
