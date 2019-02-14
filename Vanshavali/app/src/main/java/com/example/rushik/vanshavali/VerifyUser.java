package com.example.rushik.vanshavali;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class VerifyUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user);
    }


    public void btn_verifyOnClick(View view){
        //check if User Verified.and verify user
        EditText verify_code = (EditText)findViewById(R.id.)

    }
    public void goToSignIn(View view){
        Intent i = new Intent(VerifyUser.this, LoginActivity.class);
        startActivity(i);
    }
}
