package com.meet.myapplication2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;

public class MainActivity extends AppCompatActivity {
    public SQLiteDatabase mydatabase;
    public DBAdapter myadapter;
    public TextView textview1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview1 = (TextView) findViewById(R.id.textview1);

       /* //create database
        mydatabase = openOrCreateDatabase(getApplicationContext().getExternalCacheDir()+"/rushik",MODE_PRIVATE,null);

        //creat table
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS student(name VARCHAR,age INT);");*/

       myadapter = new DBAdapter(getApplicationContext());

       myadapter.open();
       myadapter.insertContact("rushik","rus@gmail.com");


    }
    public void btnShowRecord(View view){
        //insert record
        /*mydatabase.execSQL("INSERT INTO student VALUES('rushik',1);");

        //select records
        Cursor resultSet = mydatabase.rawQuery("Select * from student",null);
        resultSet.moveToFirst();
        String name = resultSet.getString(0);
        String age = resultSet.getString(1);

        TextView textview1 = (TextView) findViewById(R.id.textview1);
        String output = "Name : " + name + " Age : " + age;
        textview1.setText(output);*/

        Cursor c = myadapter.getAllContacts();
        String output = "";
        while (c.moveToNext()){
            output += "Row Id : "+c.getString(0)+" Name : " + c.getString(1) + " Email : "+c.getString(2) + "\n";
        }

        textview1.setText(output);
    }
    public void btnInsertRecord(View view){
        //insert record

        TextView name = (TextView)findViewById(R.id.editText_name);
        TextView email = (TextView)findViewById(R.id.editText_email);

        myadapter.insertContact(name.getText().toString(),email.getText().toString());

    }

    public void btndeleteContact(View view){
        TextView name = (TextView)findViewById(R.id.editText_name);
        myadapter.deleteContact(name.getText().toString());
    }

    public void btnUpdateContact(View view){
        TextView name = (TextView)findViewById(R.id.editText_name);
        TextView email = (TextView)findViewById(R.id.editText_email);
        TextView rowid = (TextView)findViewById(R.id.editText_row_id);
        Long l = Long.parseLong(rowid.getText().toString());
        myadapter.updateContact(l,name.getText().toString(),email.getText().toString());

    }
}
