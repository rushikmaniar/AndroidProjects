package com.example.rushik.activitybehaviour;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.d("StateInfo", "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("StateInfo", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("StateInfo", "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("StateInfo", "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("StateInfo", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("StateInfo", "onDestroy");
    }
}
