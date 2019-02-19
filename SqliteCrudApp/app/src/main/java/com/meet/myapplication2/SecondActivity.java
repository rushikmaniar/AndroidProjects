package com.meet.myapplication2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    public MyDatabase mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        mydb = new MyDatabase(this);

        Cursor c = mydb.getContacts();
        String output = "";
        while (c.moveToNext()){
            output += "Row Id : "+c.getString(0)+" Name : " + c.getString(1) + " Email : "+c.getString(2) + "\n";
        }
        TextView textview1 = (TextView)findViewById(R.id.textView);
        textview1.setText(output);
    }

}
