package com.example.meet.listview;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
public class MainActivity extends ListActivity {
    String[] district = {
            "Amreli",
            "Rajkot",
            "Ahmedabad",
            "Jamnagar",
            "Junagadh",
            "Vadodara",
            "Surat",
    };
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, district));
    }
    public void onListItemClick(
            ListView parent, View v, int position, long id)
    {
        Toast.makeText(this,
                "You have selected " + district[position],
                Toast.LENGTH_SHORT).show();
    }

}