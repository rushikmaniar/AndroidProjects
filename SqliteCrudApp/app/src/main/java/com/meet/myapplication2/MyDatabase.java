package com.meet.myapplication2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "MyDB.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
    * Select Query
    * */
    public Cursor getContacts() {

        SQLiteDatabase db = getWritableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
/*
        String[] sqlSelect = {"0 id", "name", "email"};
        String sqlTables = "contacts";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);*/
        String[] sqlSelect = {"0 id", "name", "email"};
/*
        Cursor c = db.query("contacts", sqlSelect, null, null,
                null, null, null);
*/
        //custom query with where

        //Don;t Forget implementation 'com.readystatesoftware.sqliteasset:sqliteassethelper:+' in build.gradle (App)

        //delete query wuth binding params
        int num_affected_row = db.delete("contacts","_id > ?",new String[]{"0"});
        Log.d("Affected rows:",String.valueOf(num_affected_row));

        //Any Custom Query void method
        db.execSQL("INSERT INTO contacts (_id,name,email)values(4,'rushi','rushi@gmail.com')");

        //update query
        //Insert
        ContentValues cn = new ContentValues();
        cn.put("name","Neha");

        int affected_rows = db.update("contacts",cn,"name = 'rushi'",new String[]{});
        Log.d("Last Insert Id:",String.valueOf(affected_rows));


        //Select query
        Cursor c = db.rawQuery("SELECT * FROM contacts WHERE _id>0",new String[]{});

        //Insert
        ContentValues cn1 = new ContentValues();
        cn.put("name","Neha");
        cn.put("email","neha@gmail.com");

        Long last_insert_id = db.insert("contacts",null,cn1);
        Log.d("Last Insert Id:",String.valueOf(last_insert_id));



        return c;

    }


    //---opens the database---
}
