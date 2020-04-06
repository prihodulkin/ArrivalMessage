package com.example.arrivalmessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class SavedNotificationsActivity extends AppCompatActivity {
    TableLayout tableLayout;
    List<mydata> datas;

    //Переменная для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    public class mydata {
        int[] idChosenFriends_;
        double latitude_;
        double longitude_;
        String writtenText_;
        String location_;
        int isEnabled_;

        mydata(String isCF, double lat, double longt, String wT, int isEn, String location) {
            String[] idss = isCF.split(" ");
            idChosenFriends_ = new int[idss.length];
            for (int i = 0; i < idss.length; i++)
                idChosenFriends_[i] = Integer.parseInt(idss[i]);
            latitude_ = lat;
            longitude_ = longt;
            writtenText_ = wT;
            isEnabled_ = isEn;
            location_ = location;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_notifications);
        addListenerOnButton();

        tableLayout = findViewById(R.id.notificationsList);

        mDBHelper = new DatabaseHelper(this);

        /* try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }*/

        try {
            mDb = mDBHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        datas = new ArrayList();


        Cursor cursor = mDb.rawQuery("SELECT * FROM users", null);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            String id = cursor.getString(0);
            double latitude = cursor.getDouble(1);
            double longitude = cursor.getDouble(2);
            String message = cursor.getString(3);
            int isEnabled = cursor.getInt(4);
            String location = cursor.getString(5);

            datas.add(new mydata(id, latitude, longitude, message, isEnabled, location));

            cursor.moveToNext();
        }
        cursor.close();

        for (int i = 0; i < datas.size(); i++) {
            TableRow tableRow = new TableRow(this);
            TextView notificationText = new TextView(this);

            Switch toggle = new Switch(this);
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // @todo update isEnabled in DB
                }
            });

            notificationText.setText(datas.get(i).location_);
            notificationText.setGravity(Gravity.CENTER_HORIZONTAL);

            tableRow.addView(notificationText);
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
