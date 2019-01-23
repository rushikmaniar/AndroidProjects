package com.razormist.simplelistviewwithimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    ListView lv_content;

    String[] name_list = {
            "ASUS VivoBook Max X441UR",
            "Acer Aspire One D270-268",
            "Lenovo Ideapad 100S 11",
            "Dell Inspiron 11-3162",
            "HP 11-D002TU",
            "MSI GV62 7RC",
            "Sony Vaio VPCW126AG"
    };

    int[] laptop = {
            R.drawable.asus_vivobook_max_x441ur_l,
            R.drawable.acer_aspire_one_d270_268_l,
            R.drawable.dell_inspiron_11_3162_l,
            R.drawable.lenovo_ideapad_100s_11_ph_l,
            R.drawable.hp_11_d002tu_l,
            R.drawable.msi_gv62_7rc_l,
            R.drawable.sony_vpcw_126ag_l_5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_content = (ListView)findViewById(R.id.lv_content);
        ListHandler listHandler = new ListHandler(MainActivity.this, name_list, laptop);
        listHandler.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        lv_content.setAdapter(listHandler);

    }
}
