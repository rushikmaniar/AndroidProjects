package com.example.rushik.progrebarviewapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
int i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ProgressBar progressbar2 = (ProgressBar)findViewById(R.id.progressBar2);
        final ProgressBar progressbar1 = (ProgressBar)findViewById(R.id.progressBar);


        CountDownTimer timer = new CountDownTimer(7000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressbar2.setProgress(i);
                progressbar1.setProgress(i,true);

                i += 20;
            }
            @Override
            public void onFinish() {
                progressbar1.setVisibility(View.GONE);
                Intent i = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(i);
            }
        }.start();


    }
}
