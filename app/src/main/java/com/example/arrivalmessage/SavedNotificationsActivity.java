package com.example.arrivalmessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.arrivalmessage.VK_Module.NotificationData;

import java.util.ArrayList;
import java.util.List;

public class SavedNotificationsActivity extends AppCompatActivity {
    TableLayout tableLayout;
    List<NotificationData> datas=MainActivity.datas;
    NotificationData curData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_notifications);
        addListenerOnButton();

        tableLayout = findViewById(R.id.notificationsList);




        for (int i = 0; i < MainActivity.datas.size(); i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setMinimumHeight(170);
            TableRow tableRow1 = new TableRow(this);
            tableRow1.setMinimumWidth(30);
            TableRow tableRow2 = new TableRow(this);
            tableRow2.setMinimumWidth(30);
            TextView notificationText = new TextView(this);
            Button imageButton=new Button(this);
            imageButton.setBackground(getResources().getDrawable(R.drawable.edit));
            final Switch toggle = new Switch(this);
            toggle.setTag(i);
            toggle.setChecked(datas.get(i).isEnabled_==1);
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                  if(datas.get((int)buttonView.getTag()).isEnabled_==1)
                      datas.get((int)buttonView.getTag()).isEnabled_=0;
                  else
                      datas.get((int)buttonView.getTag()).isEnabled_=1;

                    // @todo update isEnabled in DB
                }
            });


            notificationText.setText(MainActivity.datas.get(i).location_);
            notificationText.setTextColor(-1);
            notificationText.setWidth(500);
            notificationText.setTypeface(null, Typeface.BOLD);
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            notificationText.setGravity(Gravity.CENTER_HORIZONTAL);

            tableRow.addView(imageButton,80,80);
            tableRow.addView(tableRow2);
            tableRow.addView(notificationText);
            tableRow.addView(tableRow1);
            tableRow.addView(toggle);


            tableLayout.addView(tableRow);
        }
    }

    public void addListenerOnButton() {
        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(SavedNotificationsActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
        );
    }
}
