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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SavedNotificationsActivity extends AppCompatActivity {

    String ids = "";

    int[] idChosenFriends;
    double latitude;
    double longitude;
    String writtenText;

    List<mydata> datas;

    //Переменная для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    public class mydata {
        int [] idChosenFriends_;
        double latitude_ ;
        double longitude_;
        String writtenText_;
        public mydata(String isCF, double lat, double longt, String wT)
        {
            String[] idss = isCF.split(" ");
            idChosenFriends_ = new int[idss.length];
            for (int i = 0; i < idss.length; i++)
                idChosenFriends_[i] = Integer.parseInt(idss[i]);
            latitude_ = lat;
            longitude_ = longt;
            writtenText_ = wT;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_notifications);
        addListenerOnButton();

        mDBHelper = new DatabaseHelper(this);

       /*try {
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

        Cursor cursor = mDb.rawQuery("SELECT * FROM users1", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ids = cursor.getString(0);
            latitude = cursor.getDouble(1);
            longitude = cursor.getDouble(2);
            writtenText = cursor.getString(3);

            datas.add(new mydata(ids, latitude,longitude,writtenText));

            cursor.moveToNext();
        }
        cursor.close();



    }
    public void addListenerOnButton(){
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
