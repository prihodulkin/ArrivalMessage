package com.example.arrivalmessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.arrivalmessage.VK_Module.NotificationData;
import com.example.arrivalmessage.VK_Module.VKUser;


public class ChooseTextActivity extends AppCompatActivity {

    EditText textMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_text);
        addListenerOnButton();
        textMessage = findViewById(R.id.text_message);
        textMessage.setPadding(20, 10, 20, 10);
        textMessage.setTextColor(-1);

        textMessage.setText(MainActivity.reserveData==null? MainActivity.defMessage:MainActivity.curData.writtenText);
        Bundle arguments = getIntent().getExtras();

    }

    public void finish() {
        ChooseTextActivity.super.finish();
        FinishManager.finishActivity(SelectUserActivity.class);
        FinishManager.finishActivity(SelectLocationActivity.class);
        MainActivity.curData = MainActivity.reserveData;
        MainActivity.reserveData = null;
    }

    @Override
    public void onBackPressed() {
        MainActivity.curData.writtenText = textMessage.getText().toString();
        ChooseTextActivity.super.finish();
    }


    public void addListenerOnButton() {
        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChooseTextActivity.super.finish();
                    }
                }
        );
        Button ready_btn = findViewById(R.id.ready_btn);
        ready_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.curData.writtenText = textMessage.getText().toString();

                        if (textMessage.getText().toString().equals("")) {
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
                            TextView aText = dialog.findViewById(android.R.id.message);
                            aText.setTypeface(Typeface.createFromAsset(getAssets(), "font/centurygothic.ttf"));
                        } else {
                            if (MainActivity.reserveData != null) {
                                finish();
                                return;
                            }
                            Intent intent = new Intent(ChooseTextActivity.this, FinishActivity.class);
                            FinishManager.addActivity(ChooseTextActivity.this);
                            startActivity(intent);
                        }


                    }
                }
        );
    }
}


