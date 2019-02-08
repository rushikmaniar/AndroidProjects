package com.example.meet.bookstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.net.Uri;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View view) {
        Intent data = new Intent();
//---get the EditText view---
        EditText txt_username = (EditText)findViewById(R.id.edt_a_uid);
//---set the data to pass back---
        data.setData(Uri.parse( txt_username.getText().toString()));
        setResult(RESULT_OK, data);
//---closes the activity---
        if( txt_username.getText().toString().equals("admin"))
        {
            startActivity(new Intent("com.example.meet.bookstore.AdminMenu"));
            finish();
        }
        else
        {
            startActivity(new Intent("com.example.meet.bookstore.UserPage"));
            finish();
        }
        finish();
    }
}