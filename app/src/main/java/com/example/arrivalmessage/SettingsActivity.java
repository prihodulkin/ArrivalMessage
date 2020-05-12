package com.example.arrivalmessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

        Button chadneDefLoc=findViewById(R.id.change_def_loc);
        chadneDefLoc.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingsActivity.this,SelectDefaultLocationActivity.class);
                startActivity(intent);
            }
        });
    }

}
