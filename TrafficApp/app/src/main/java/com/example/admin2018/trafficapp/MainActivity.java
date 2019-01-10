package com.example.admin2018.trafficapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.view.*;

public class MainActivity extends AppCompatActivity {


    public RadioGroup btn_group;
    public RadioButton r1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_group = (RadioGroup)findViewById(R.id.btn_group);
        btn_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton r = (RadioButton)findViewById(checkedId);
                CharSequence color = r.getText();
                if(color.equals("Red"))
                {
                    btn_group.getRootView().getRootView().setBackgroundColor(Color.RED);
                }
                else if(color.equals("Yellow")){
                    btn_group.getRootView().setBackgroundColor(Color.YELLOW);
                }else{
                    btn_group.getRootView().setBackgroundColor(Color.GREEN);
                }
            }
        });


    }
}
