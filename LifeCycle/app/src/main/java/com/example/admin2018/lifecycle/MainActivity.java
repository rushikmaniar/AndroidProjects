package com.example.admin2018.lifecycle;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.*;


public class MainActivity extends AppCompatActivity {

    String tag = "Lifecycle Step";
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

        Toast.makeText(this,"In the Oncreate() event",
                Toast.LENGTH_SHORT).show();


    }

    public void onStart(){
        super.onStart();
        Toast.makeText(this,"In the onStart() event",
                Toast.LENGTH_SHORT).show();

    }

    public void onRestart(){
        super.onRestart();
        Toast.makeText(this,"In the onRestart() event",
                Toast.LENGTH_SHORT).show();
    }

    public void onResume(){
        super.onResume();
        Toast.makeText(this,"In the onResume() event",
                Toast.LENGTH_SHORT).show();
    }

    public void onPause(){
        super.onPause();
        Toast.makeText(this,"In the onPause() event",
                Toast.LENGTH_SHORT).show();
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
}
