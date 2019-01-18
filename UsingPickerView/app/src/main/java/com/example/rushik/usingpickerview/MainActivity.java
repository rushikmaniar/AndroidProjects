package com.example.rushik.usingpickerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TimePicker timePicker1 = (TimePicker)findViewById(R.id.timepicker);
        final DatePicker datePicker1 = (DatePicker)findViewById(R.id.datepicker);

        final EditText selectTime = (EditText)findViewById(R.id.seletectTime);
        final EditText selectDate = (EditText)findViewById(R.id.seletectDate);

        //on select Time Click
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker1.setVisibility(View.VISIBLE);
                selectTime.setVisibility(View.GONE);
            }
        });

        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Integer temp = hourOfDay;
                Integer temp1 = minute;

                //Toast.makeText(MainActivity.this,i.toString(),Toast.LENGTH_SHORT).show();
                selectTime.setText(temp.toString() + " Hrs " + temp1.toString() + " Minutes ");
                selectTime.setVisibility(View.VISIBLE);
                timePicker1.setVisibility(View.GONE);
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker1.setVisibility(View.VISIBLE);
                selectDate.setVisibility(View.GONE);
            }
        });

        datePicker1.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Integer temp = year;
                Integer temp1 = monthOfYear;
                Integer temp2 = dayOfMonth;

                selectDate.setText(temp2.toString() + "/" + temp1.toString() + "/" + temp.toString());
                selectDate.setVisibility(View.VISIBLE);
                datePicker1.setVisibility(View.GONE);
            }
        });


        //on select Time Date



    }
}
