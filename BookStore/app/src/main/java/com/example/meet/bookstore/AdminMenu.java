package com.example.meet.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

public class AdminMenu extends Activity {
    int request_Code = 1;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admenu);
    }
    public void onClick(View view) {
        startActivityForResult(new Intent("com.example.meet.bookstore.Main"),request_Code);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == request_Code) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this,data.getData().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void login(View view) {
        startActivity(new Intent("com.example.meet.bookstore.AdminLogin"));
    }
    public void signup(View view) {
        startActivity(new Intent("com.example.meet.bookstore.AdminSignup"));
    }
    public void orders(View view) {
        startActivity(new Intent("com.example.meet.bookstore.Orders"));
    }
    public void bill(View view) {
        startActivity(new Intent("com.example.meet.bookstore.UserBillDetalis"));
    }
    public void orderlist(View view) {
        startActivity(new Intent("com.example.meet.bookstore.AdminOrderList"));
    }
}
