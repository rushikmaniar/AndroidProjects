package com.example.rushik.vanshavali;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;

public class FamilyList extends AppCompatActivity {


    final static ArrayList<HashMap<String, ?>> data = new ArrayList<HashMap<String, ?>>();

    static{
        HashMap<String, Object> row  = new HashMap<String, Object>();
        row.put("Icon", R.drawable.family_tree);
        row.put("FamilyName", "Family 1");
        row.put("Members_Count", "5 Members");
        data.add(row);
        row  = new HashMap<String, Object>();
        row.put("Icon", R.drawable.family_tree);
        row.put("FamilyName", "Family 2");
        row.put("Members_Count", "5 Members");
        data.add(row);
        row  = new HashMap<String, Object>();
        row.put("Icon", R.drawable.family_tree);
        row.put("FamilyName", "Family 3");
        row.put("Members_Count", "5 Members");
        data.add(row);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_list);

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.mipmap.ic_launcher_foreground);
        menu.setDisplayUseLogoEnabled(true);

        ListView family_list = (ListView)findViewById(R.id.family_list);
        SimpleAdapter adapter = new SimpleAdapter(this,
                data,
                R.layout.family_list_row,
                new String[] {"Icon","FamilyName","Members_Count"},
                new int[] { R.id.family_list_row, R.id.family_name,R.id.family_members_count});
        family_list.setAdapter(adapter);

    }

}
