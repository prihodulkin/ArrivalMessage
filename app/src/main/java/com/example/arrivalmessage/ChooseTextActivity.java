package com.example.arrivalmessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
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


public class ChooseTextActivity extends AppCompatActivity {

    EditText textMessage;
    int[] idChosenFriends;
    double latitude;
    double longitude;
    String location;
    String writtenText;
    String[] displayFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_text);
        addListenerOnButton();
        textMessage = findViewById(R.id.text_message);
        textMessage.setPadding(20,10,20,10);
        textMessage.setTextColor(-1);


        Bundle arguments = getIntent().getExtras();
        idChosenFriends = arguments.getIntArray("lst1");
        latitude = arguments.getDouble("firstCoordinate");
        longitude = arguments.getDouble("secondCoordinate");
        location = arguments.getString("location");
        displayFriends = arguments.getStringArray("displayLst");
    }

    public void addListenerOnButton() {
        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChooseTextActivity.this, SelectLocationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
        );
        Button ready_btn = findViewById(R.id.ready_btn);
        ready_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (textMessage.getText().toString().equals(""))
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChooseTextActivity.this);
                            builder.setTitle("Внимание!");
                            builder.setMessage("Введите текст сообщения!");
                            builder.setCancelable(false);
                            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        else {
                            Intent intent = new Intent(ChooseTextActivity.this, FinishActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            writtenText = textMessage.getText().toString();
                            intent.putExtra("firstCoordinate1", latitude);
                            intent.putExtra("secondCoordinate1", longitude);
                            intent.putExtra("lst2", idChosenFriends);
                            intent.putExtra("text", writtenText);
                            intent.putExtra("location", location);
                            intent.putExtra("display", displayFriends);
                            startActivity(intent);
                        }


                    }
                }
        );
    }
}


