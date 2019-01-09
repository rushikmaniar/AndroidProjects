package com.example.admin2018.bookapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.view.*;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnok_onClick(View view){
        String output = "";
        CheckBox chk1 = (CheckBox)findViewById(R.id.checkbox_book1);
        CheckBox chk2 = (CheckBox)findViewById(R.id.checkbox_book2);
        CheckBox chk3 = (CheckBox)findViewById(R.id.checkbox_book3);
        TextView outputText = (TextView)findViewById(R.id.output_text);

        outputText.setText("");

        if(chk1.isChecked())
            output += " " + chk1.getText();
        if(chk2.isChecked())
            output += " " + chk2.getText();
        if(chk3.isChecked())
            output += " " + chk3.getText();
        outputText.setText(output);


    }


}
