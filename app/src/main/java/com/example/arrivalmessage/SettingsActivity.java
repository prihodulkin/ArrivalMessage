package com.example.arrivalmessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class SettingsActivity extends AppCompatActivity {

    static public TextView defLocationText;
    EditText defMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        defLocationText=findViewById(R.id.cur_def_loc);
        defLocationText.setText(MainActivity.defLocation);
        defMessage=findViewById(R.id.defMessage);
        defMessage.setText(MainActivity.defMessage);

        addListenerOnButton();

    }
    public void addListenerOnButton() {
        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.defMessage=defMessage.getText().toString();
                        SettingsActivity.super.finish();
                    }
                }
        );
/*
        Button days_plus_btn = findViewById(R.id.daysplus);
        days_plus_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView days = findViewById(R.id.days);
                        String str = days.getText().toString();
                        int dayscount = Integer.parseInt(str.substring(0,str.length()-1));
                        dayscount++;
                        MainActivity.defDays = dayscount;
                        days.setText(dayscount+" ");
                    }
                }
        );

        Button days_min_btn = findViewById(R.id.daysminus);
        days_min_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView days = findViewById(R.id.days);
                        String str = days.getText().toString();
                        int dayscount = Integer.parseInt(str.substring(0,str.length()-1));
                        dayscount--;
                        MainActivity.defDays = dayscount;
                        days.setText(dayscount+" ");
                    }
                }
        );

        Button hours_plus_btn = findViewById(R.id.hoursplus);
        hours_plus_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView days = findViewById(R.id.hours);
                        String str = days.getText().toString();
                        int hourcount = Integer.parseInt(str.substring(0,str.length()-1));
                        hourcount++;
                        MainActivity.defHours = hourcount;
                        days.setText(hourcount+" ");
                    }
                }
        );

        Button hours_minus_btn = findViewById(R.id.hoursminus);
        hours_minus_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView days = findViewById(R.id.hours);
                        String str = days.getText().toString();
                        int hourscount = Integer.parseInt(str.substring(0,str.length()-1));
                        hourscount--;
                        MainActivity.defHours = hourscount;
                        days.setText(hourscount+" ");
                    }
                }
        );

        Button min_plus_btn = findViewById(R.id.minplus);
        min_plus_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView days = findViewById(R.id.mins);
                        String str = days.getText().toString();
                        int mincount = Integer.parseInt(str.substring(0,str.length()-1));
                        mincount++;
                        MainActivity.defMinutes = mincount;
                        days.setText(mincount+" ");
                    }
                }
        );

        Button mins_min_btn = findViewById(R.id.minmin);
        mins_min_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView days = findViewById(R.id.mins);
                        String str = days.getText().toString();
                        int mincount = Integer.parseInt(str.substring(0,str.length()-1));
                        mincount--;
                        MainActivity.defMinutes = mincount;
                        days.setText(mincount+" ");
                    }
                }
        );
*/
        Button chadneDefLoc=findViewById(R.id.change_def_loc2);
        chadneDefLoc.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SettingsActivity.this,SelectDefaultLocationActivity.class);
                startActivity(intent);
           }
        });
    }

    @Override
    public void onBackPressed() {
        MainActivity.defMessage=defMessage.getText().toString();
        super.onBackPressed();
    }
}

