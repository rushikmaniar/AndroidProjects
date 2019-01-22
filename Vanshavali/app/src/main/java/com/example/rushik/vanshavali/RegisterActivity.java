package com.example.rushik.vanshavali;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        EditText username = (EditText)findViewById(R.id.editText_username);
        username.setText("Hello");
    }
    public void goToSignIn(View v){
        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(i);
    }

}
