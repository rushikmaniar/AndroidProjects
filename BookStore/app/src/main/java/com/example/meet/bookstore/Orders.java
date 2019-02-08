package com.example.meet.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class Orders extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
    }
    public void receivedorder(View view) {
        startActivity(new Intent("com.example.meet.bookstore.ReceivedOrder"));
    }
    public void stockbook(View view) {
        startActivity(new Intent("com.example.meet.bookstore.StockSubject"));
    }
    public void placedorder(View view) {
        startActivity(new Intent("com.example.meet.bookstore.PlacedOrder"));
    }
}
