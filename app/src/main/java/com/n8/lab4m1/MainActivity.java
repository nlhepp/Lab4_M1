package com.n8.lab4m1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button startButton;
    private Button stopButton;
    private volatile boolean stopThread = false;
    private TextView progTxt;
    private ProgressBar progBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.startBtn);
        stopButton = findViewById(R.id.stopBtn);
        progBar = findViewById(R.id.progressBar);
        progBar.setVisibility(View.INVISIBLE);
        progTxt = findViewById(R.id.textProgress);
    }

    public void mockFileDownloader(){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                startButton.setText("Downloading...");
            }
        });

        for (int downloadProgress = 0; downloadProgress <= 100; downloadProgress = downloadProgress+10){
            if (stopThread){
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        startButton.setText("Start");
                        progTxt.setVisibility(View.INVISIBLE);
                        progBar.setVisibility(View.INVISIBLE);
                    }
                });
                return;
            }

            Log.d(TAG, "Download Progress: " + downloadProgress + "%");
            int finalDownloadProgress = downloadProgress;
            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    progTxt.setText(finalDownloadProgress +"% downloaded");
                    progBar.setProgress(finalDownloadProgress);
                }
            });
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable(){
           @Override
            public void run(){
               startButton.setText("Start");
               progTxt.setVisibility(View.INVISIBLE);
               progBar.setVisibility(View.INVISIBLE);
               progBar.setProgress(0);
           }
        });
    }

    public void startDownload(View view){
        //mockFileDownloader();
        ExampleRunnable runnable = new ExampleRunnable();
        stopThread=false;
        progTxt.setVisibility(View.VISIBLE);
        progBar.setVisibility(View.VISIBLE);
        new Thread(runnable).start();
    }

    public void stopDownload(View view){
        stopThread = true;
    }

    class ExampleRunnable implements Runnable{
        @Override
        public void run(){
            mockFileDownloader();
        }
    }
}



