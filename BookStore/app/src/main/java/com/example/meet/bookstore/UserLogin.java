package com.example.meet.bookstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserLogin extends Activity {
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);
    }
    public void ulogin(View view) {
        startActivity(new Intent("com.example.meet.bookstore.UserStandard"));
    }
    public void usignup(View view) {
        startActivity(new Intent("com.example.meet.bookstore.UserSignup"));
    }
}
