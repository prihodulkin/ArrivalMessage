package com.example.arrivalmessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.database.SQLException;
import android.widget.ScrollView;
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
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
        for (int i = 0; i < MainActivity.curData.displayFriends.length; i++) {
            final String friendName = MainActivity.curData.displayFriends[i];
            TableRow tableRow = new TableRow(this);
            TextView fullName = new TextView(this);
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            fullName.setText(friendName);
            fullName.setTypeface(Typeface.createFromAsset(getAssets(), "font/centurygothic.ttf"));
            fullName.setTextColor(-1);
            fullName.setTextSize(px/5);
            fullName.setGravity(Gravity.CENTER_HORIZONTAL);
//fullName.setTypeface(null, Typeface.BOLD);
            tableRow.addView(fullName, ActionBar.LayoutParams.WRAP_CONTENT);
            tableLayout.addView(tableRow);
        }
    }

    public void createInfoTable() {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
        int textWidth=px*8;
        TableRow tableRowAddress = new TableRow(this);
        TableRow addressT = getHeadRow("Адрес:");
        TableRow messageT = getHeadRow("Сообщение:");
        TableRow usersT = getHeadRow("Пользователи:");



        TableRow tableRowMessage = new TableRow(this);
        TextView addressView = new TextView(this);

        TextView textMessageView = new TextView(this);
        textMessageView.setTypeface(Typeface.createFromAsset(getAssets(), "font/centurygothic.ttf"));
        textMessageView.setTextSize(px/5);

        textMessageView.setGravity(Gravity.CENTER);
        addressView.setText(MainActivity.curData.location);
        addressView.setTypeface(Typeface.createFromAsset(getAssets(), "font/centurygothic.ttf"));
        addressView.setTextSize(px/5);
        textMessageView.setText(MainActivity.curData.writtenText);
        addressView.setTextColor(-1);
        addressView.setGravity(Gravity.CENTER);
        textMessageView.setTextColor(-1);

        tableRowAddress.setGravity(Gravity.CENTER_HORIZONTAL);
        tableRowMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        int pxHeight = px*2;
        int pxWidth = px*10;
        tableRowAddress.addView(addressView,pxWidth,pxHeight);
        tableRowMessage.addView(textMessageView, pxWidth, pxHeight);
        ScrollView scrollView=new ScrollView(this);
        scrollView.addView(tableRowMessage);
        tableInfo.addView(addressT);
        tableInfo.addView(tableRowAddress);
        tableInfo.addView(messageT);
        tableInfo.addView(scrollView);
        tableInfo.addView(usersT);
    }

    TableRow getHeadRow(String text) {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
        TextView addressHeadView = new TextView(this);
        addressHeadView.setText(text);
        addressHeadView.setTextColor(-1);
        addressHeadView.setGravity(Gravity.CENTER_HORIZONTAL);
        addressHeadView.setTextSize(px/4);
        TableRow tableRowHeadAddress = new TableRow(this);
        tableRowHeadAddress.setGravity(Gravity.CENTER_HORIZONTAL);
        int pxHeight = px;
        int pxWidth = px*8;
        tableRowHeadAddress.addView(addressHeadView, pxWidth,pxHeight);
        return tableRowHeadAddress;
    }
}
