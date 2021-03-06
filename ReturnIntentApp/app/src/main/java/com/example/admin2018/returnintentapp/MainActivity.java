package com.example.admin2018.returnintentapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.net.Uri;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public Intent data1;
    int request_Code = 1;
    //public Intent data1 = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loginbtnclick(View view){
        Intent i = new Intent(MainActivity.this,SecondActivity.class);
        startActivityForResult(i,request_Code);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        data1 = data;
        if (requestCode == request_Code) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(this,data.getStringExtra("username"),
                        Toast.LENGTH_SHORT).show();
                Toast.makeText(this,data.getStringExtra("password"),
                        Toast.LENGTH_SHORT).show();



                CountDownTimer timer = new CountDownTimer(3000,2000) {
                    @Override

                    public void onTick(long millisUntilFinished) {

                    }
                    @Override
                    public void onFinish() {
                        Toast.makeText(MainActivity.this,data1.getStringExtra("password"),Toast.LENGTH_SHORT).show();
                    }
                }.start();





            }
        }
    }
    public void myfunc(Intent data1){
        Toast.makeText(this,data1.getStringExtra("password"),
                Toast.LENGTH_SHORT).show();
    }

}
