package com.example.meet.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class UserPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);
    }
    public void ulogin(View view) {
        startActivity(new Intent("com.example.meet.bookstore.UserLogin"));
    }
    public void usignup(View view) {
        startActivity(new Intent("com.example.meet.bookstore.UserSignup"));
    }
}
