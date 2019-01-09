package activity.android.cc.fragmentorientation;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        int height = display.heightPixels;
        if(width>height){
            fragment1 fragment1 = new fragment1();
            fragmentTransaction.replace(android.R.id.content,fragment1);
        }
        else
        {
            fragment2 fragment2 = new fragment2();
            fragmentTransaction.replace(android.R.id.content,fragment2);
        }
        fragmentTransaction.commit();
    }
}
