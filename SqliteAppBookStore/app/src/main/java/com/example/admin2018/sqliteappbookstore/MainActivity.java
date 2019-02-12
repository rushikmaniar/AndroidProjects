package com.example.admin2018.sqliteappbookstore;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textview);

        mDBHelper = new DatabaseHelper(this);

       /* try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }*/



    }
    public void btnShowRecord(View view){

        Cursor c = mDBHelper.getAllAdmins();
        String output = "";
        output += c.getCount();
        while (c.moveToNext()){
            output += "Row Id : "+c.getString(0)+" Name : " + c.getString(1) + " Email : "+c.getString(2) + "\n";
        }

        textView.setText(output);
    }
}
