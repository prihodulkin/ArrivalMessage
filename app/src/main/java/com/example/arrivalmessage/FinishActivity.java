package com.example.arrivalmessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.location.Geocoder;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.arrivalmessage.VK_Module.NotificationData;

import java.util.Locale;

import java.io.IOException;
import java.util.HashMap;

import static android.os.Build.ID;

public class FinishActivity extends AppCompatActivity {

    int[] idChosenFriends;
    TableLayout tableLayout;
    TableLayout tableInfo;

    String users_ids = "";
    double latitude;
    double longitude;
    String message;
    String location;
    String[] displayFriends;
    //���������� ��� ������ � ��
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        addListenerOnButton();
        Bundle arguments = getIntent().getExtras();
        idChosenFriends = arguments.getIntArray("lst2");
        latitude = arguments.getDouble("firstCoordinate1");
        longitude = arguments.getDouble("secondCoordinate1");
        location = arguments.getString("location");
        message = arguments.getString("text");
        displayFriends = arguments.getStringArray("display");
        tableLayout = findViewById(R.id.userList);
        tableInfo = findViewById(R.id.tableInfo);
        createDisplayList();
        createInfoTable();

        for (int id : idChosenFriends) {
            users_ids += id + " ";
        }

        mDBHelper = new DatabaseHelper(this);

        /*try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }*/

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        ContentValues cv = new ContentValues();

        cv.put("users_ids", users_ids);
        cv.put("latitude", latitude);
        cv.put("longitude", longitude);
        cv.put("message", message);
        cv.put("isEnabled", 1);
        cv.put("location", location);

        MainActivity.datas.add(new NotificationData(users_ids,latitude,longitude,message,1,location));

        mDb.insert("users", null, cv);

    }
    public void addListenerOnButton() {
        ImageButton main_menu_btn = findViewById(R.id.main_menu_btn);
        main_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FinishActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void createDisplayList(){
        for (int i = 0; i < displayFriends.length; i++){
            final String friendName = displayFriends[i];
            TableRow tableRow = new TableRow(this);
            TextView fullName  = new TextView(this);



            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            fullName.setText(friendName);
            fullName.setTextColor(-1);
            fullName.setGravity(Gravity.CENTER_HORIZONTAL);
            fullName.setTypeface(null, Typeface.BOLD);


            tableRow.addView(fullName,500,80);

            tableLayout.addView(tableRow);
        }
    }
    public void createInfoTable(){
        TableRow tableRowAddress = new TableRow(this);
        TableRow tableRowMessage = new TableRow(this);

        TextView addressView  = new TextView(this);
        TextView textMessageView  = new TextView(this);

        String address = "Место отправки сообщения: " + location;
        String textMessage = "Сообщение: " + message;

        addressView.setText(address);
        textMessageView.setText(textMessage);

        addressView.setTextColor(-1);
        addressView.setGravity(Gravity.CENTER_HORIZONTAL);
        addressView.setTypeface(null, Typeface.BOLD);
        textMessageView.setTextColor(-1);
        textMessageView.setGravity(Gravity.CENTER_HORIZONTAL);
        textMessageView.setTypeface(null, Typeface.BOLD);

        tableRowAddress.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRowMessage.setGravity(Gravity.CENTER_HORIZONTAL);

        tableRowAddress.addView(addressView, 900,150);
        tableRowMessage.addView(textMessageView,900,150);

        tableInfo.addView(tableRowAddress);
        tableInfo.addView(tableRowMessage);
    }

}
