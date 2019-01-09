package com.example.rushik.creatingui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        LinearLayoutCompat.LayoutParams params =
                new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
//---create a layout---
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);


//---create a textview---
        TextView tv = new TextView(this);
        tv.setText("Enter Name :");
        tv.setLayoutParams(params);

/* Edit text*/
        final EditText name1 = new EditText(this);
        name1.setWidth(500);
        name1.setLayoutParams(params);

//---create a button---
        Button btn = new Button(this);
        btn.setText("OK");


        /* Handlig Events of button view */
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this,"Your Name : " + name1.getText(),Toast.LENGTH_SHORT).show();
            }
        });
        btn.setLayoutParams(params);




        layout.addView(tv);

        layout.addView(name1);

        layout.addView(btn);






//---create a layout param for the layout---
        LinearLayoutCompat.LayoutParams layoutParam =
                new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT);



        this.addContentView(layout, layoutParam);

    }
}
