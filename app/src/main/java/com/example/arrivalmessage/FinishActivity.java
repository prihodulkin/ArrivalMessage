package com.example.arrivalmessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.database.SQLException;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.arrivalmessage.VK_Module.NotificationData;
import com.example.arrivalmessage.VK_Module.VKUser;
import com.example.arrivalmessage.VK_Module.VK_Controller;
import com.google.android.gms.maps.model.LatLng;

public class FinishActivity extends AppCompatActivity {


    TableLayout tableLayout;
    TableLayout tableInfo;
    String users_ids = "";

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        addListenerOnButton();
        Bundle arguments = getIntent().getExtras();
        tableLayout = findViewById(R.id.userList);
        tableInfo = findViewById(R.id.tableInfo);
        createDisplayList();
        createInfoTable();
        for (int id : MainActivity.curData.idChosenFriends) {
            users_ids += id + " ";
        }
        mDBHelper = new DatabaseHelper(this);
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        ContentValues cv = new ContentValues();
        cv.put("users_ids", users_ids);
        cv.put("latitude", MainActivity.curData.latitude);
        cv.put("longitude", MainActivity.curData.longitude);
        cv.put("message", MainActivity.curData.writtenText);
        cv.put("isEnabled", 1);
        cv.put("location", MainActivity.curData.location);

        MainActivity.data.add(new NotificationData(users_ids, MainActivity.curData.latitude, MainActivity.curData.longitude, MainActivity.curData.writtenText, 1, MainActivity.curData.location, VK_Controller.UserID, MainActivity.curData.days, MainActivity.curData.hours, MainActivity.curData.minutes, MainActivity.curData.flag));

        mDb.insert("users", null, cv);

    }

    public void addListenerOnButton() {
        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        Button main_menu_btn = findViewById(R.id.main_menu_btn);
        main_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });
    }

    @Override
    public void onBackPressed() {
        FinishActivity.super.finish();
        FinishManager.finishActivity(SelectUserActivity.class);
        FinishManager.finishActivity(SelectLocationActivity.class);
        FinishManager.finishActivity(ChooseTextActivity.class);
        finish();

    }

    public void finish() {
        FinishActivity.super.finish();
        FinishManager.finishActivity(SelectUserActivity.class);
        FinishManager.finishActivity(SelectLocationActivity.class);
        FinishManager.finishActivity(ChooseTextActivity.class);
        if (MainActivity.reserveData == null)
            MainActivity.curData = new NotificationData();
        else
        {
            MainActivity.curData=MainActivity.reserveData;
            MainActivity.reserveData=null;
        }


    }

    public void createDisplayList() {
        for (int i = 0; i < MainActivity.curData.displayFriends.length; i++) {
            final String friendName = MainActivity.curData.displayFriends[i];
            TableRow tableRow = new TableRow(this);
            TextView fullName = new TextView(this);
            tableRow.setGravity(Gravity.CENTER);
            fullName.setText(friendName);
            fullName.setTextColor(-1);
            fullName.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
            fullName.setGravity(Gravity.CENTER_HORIZONTAL);
//fullName.setTypeface(null, Typeface.BOLD);
            tableRow.addView(fullName, ActionBar.LayoutParams.WRAP_CONTENT);
            tableLayout.addView(tableRow);
        }
    }

    public void createInfoTable() {
        int textWidth=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
        TableRow tableRowAddress = new TableRow(this);
        TableRow addressT = getHeadRow("Адрес: ");
        TableRow messageT = getHeadRow("Сообщение: ");
        TableRow usersT = getHeadRow("Пользователи: ");

        TableRow tableRowMessage = new TableRow(this);
        TextView addressView = new TextView(this);
        addressView.setWidth(textWidth);
        TextView textMessageView = new TextView(this);
        textMessageView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        textMessageView.setWidth(textWidth);
        String address1 = MainActivity.curData.location;
        addressView.setText(address1);
        addressView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        textMessageView.setText(MainActivity.curData.writtenText);
        addressView.setTextColor(-1);
        addressView.setGravity(Gravity.CENTER);
        textMessageView.setTextColor(-1);
        textMessageView.setGravity(Gravity.CENTER);
        tableRowAddress.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRowMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        int pxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
        int pxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        tableRowAddress.addView(addressView, pxWidth, pxHeight);
        tableRowMessage.addView(textMessageView, pxWidth, pxHeight);
        tableInfo.addView(addressT);
        tableInfo.addView(tableRowAddress);
        tableInfo.addView(messageT);
        tableInfo.addView(tableRowMessage);
        tableInfo.addView(usersT);
    }

    TableRow getHeadRow(String text) {
        TextView addressHeadView = new TextView(this);
        addressHeadView.setText(text);
        addressHeadView.setTextColor(-1);
        addressHeadView.setGravity(Gravity.CENTER_HORIZONTAL);
        addressHeadView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        TableRow tableRowHeadAddress = new TableRow(this);
        tableRowHeadAddress.setGravity(Gravity.CENTER_HORIZONTAL);
        int minHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        tableRowHeadAddress.setMinimumHeight(minHeight);
        int pxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
        int pxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        tableRowHeadAddress.addView(addressHeadView, pxWidth,pxHeight);
        return tableRowHeadAddress;

    }

}
