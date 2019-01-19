package com.example.admin2018.uiprogramaticallyapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScrollView scroll = new ScrollView(this);

        LinearLayoutCompat.LayoutParams params =
                new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.FILL_PARENT,
                        LinearLayoutCompat.LayoutParams.FILL_PARENT);
        //---create a layout---
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final Button[] btn_list = new Button[15];

        //create 15 Buttons
        final String[] student_list = getResources().getStringArray(R.array.student_list);

        final String[] s1 = {new String("")};
        for(i=0;i < student_list.length;i++){

            btn_list[i] = new Button(MainActivity.this);
            btn_list[i].setText("Rollno : " + (i+1));
            btn_list[i].setLayoutParams(params);
            Object j = i + 1;
            btn_list[i].setTag(j.toString());

            btn_list[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = Integer.parseInt(v.getTag().toString());
                    String temp_message = " Rollno : " + v.getTag() + " Name :" + student_list[temp];
                    Toast.makeText(MainActivity.this,temp_message,Toast.LENGTH_LONG).show();
                }
            });
            layout.addView(btn_list[i]);
        }

        scroll.addView(layout);
        this.addContentView(scroll,params);


    }

}
