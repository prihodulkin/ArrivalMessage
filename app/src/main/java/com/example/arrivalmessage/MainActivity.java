package com.example.arrivalmessage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.arrivalmessage.VK_Module.NotificationData;
import com.example.arrivalmessage.VK_Module.VK_Controller;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    public static List<NotificationData> data;
    public static NotificationData curData = new NotificationData();
    public static NotificationData reserveData;
    //Переменная для работы с БД
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    public static VK_Controller controller;
    public static double defLatitude=47.216724;
    public static double defLongitude=39.628510;
    public static String defMessage="";
    public static String defLocation="улица Мильчакова 8А, Ростов-на-Дону";
    private LocationManager manager;
    private LocationCallback locationCallback;

    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i("Смена местоположения", location.getLatitude() + " " + location.getLongitude());
            for (NotificationData d : data) {
                if (d.isEnabled == 1) {
                    LatLng latLng = new LatLng(d.latitude, d.longitude);
                    if (calculationByDistance(latLng, new LatLng(location.getLatitude(), location.getLongitude())) <= 0.1) {

                       /* AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Внимание!");
                        builder.setMessage("Сообщение отправлено: "+d.writtenText_);
                        builder.setCancelable(false);
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                //startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));// Отпускает диалоговое окно
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();*/
                        Log.i("Sent message", "Сообщение " + d.writtenText + " отправлено!");
                        for (int id : d.idChosenFriends)
                            AuthActivity.controller.SendMessage(id, d.writtenText);

                        d.isEnabled = 0;

                    }


                }
            }


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }


        @Override
        public void onProviderDisabled(String provider) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Внимание!");
            builder.setMessage("Включите передачу местоположения");
            builder.setCancelable(false);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));// Отпускает диалоговое окно
                }
            });
            AlertDialog dialog = builder.create();

            dialog.show();
            TextView aText = dialog.findViewById(android.R.id.message);
            aText.setTypeface(Typeface.createFromAsset(getAssets(), "font/centurygothic.ttf"));
        }
    };

    public double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Внимание!");
            builder.setMessage("Для работы приложения нужен доступ к местоположению");
            builder.setCancelable(false);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1
                    );// Отпускает диалоговое окно
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        //

        //requestQueue.start();


        /*if (controller == null) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            controller = new VK_Controller(requestQueue, getResources().getString(R.string.Access_Key), getResources().getString(R.string.Group_id));
        }

        if (!VK.isLoggedIn()) {
            VK.login(this, Arrays.asList(VKScope.FRIENDS));
        } else {
            controller.UpdateFriends();
            controller.UpdateUserID();
        }
*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == 0) {
            manager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

        }
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

        if (data != null)
            return;
        data = new ArrayList();


        Cursor cursor = mDb.rawQuery("SELECT * FROM users", null);
        //mDb.execSQL("DELETE FROM" + " users");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(0);
            double latitude = cursor.getDouble(1);
            double longitude = cursor.getDouble(2);
            String message = cursor.getString(3);
            int isEnabled = cursor.getInt(4);
            String location = cursor.getString(5);
            int user_id = cursor.getInt(6);
            int days = cursor.getInt(7);
            int hours = cursor.getInt(8);
            int minutes = cursor.getInt(9);
            int flag = cursor.getInt(10);
            if (user_id == VK_Controller.UserID)
                data.add(new NotificationData(id, latitude, longitude, message, isEnabled, location, user_id, days, hours, minutes, flag));
            cursor.moveToNext();
        }
        cursor.close();
        data.contains(1);


    }

    public void addListenerOnButton() {
        Button create_btn = findViewById(R.id.create_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectUserActivity.class);
                startActivity(intent);

            }
        });
        ImageButton settings_btn = findViewById(R.id.settings_btn);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        Button saved_btn = findViewById(R.id.saved_btn);
        saved_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SavedNotificationsActivity.class);
                startActivity(intent);
            }
        });
        Button change_acc_btn = findViewById(R.id.change_acc_btn);
        change_acc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VK.logout();

                Intent i = getBaseContext().getPackageManager().
                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        curData.toString();
        data.contains(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Выход")
                .setMessage("Вы действительно хотите выйти из приложения?")
                .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // SelectUserActivity.chosenFriends.clear();
                        //  SelectUserActivity.friends.clear();
                        mDb.execSQL("DELETE FROM" + " users");
                        if (data.size() > 0) {

                            for (NotificationData d : data) {
                                ContentValues cv = new ContentValues();
                                String users_ids = "";
                                for (int id : d.idChosenFriends) {
                                    users_ids += id + " ";
                                }
                                cv.put("users_ids", users_ids);
                                cv.put("latitude", d.latitude);
                                cv.put("longitude", d.longitude);
                                cv.put("message", d.writtenText);
                                cv.put("isEnabled", d.isEnabled);
                                cv.put("location", d.location);
                                cv.put("user_id", d.user_id);
                                cv.put("days", d.days);
                                cv.put("hours", d.hours);
                                cv.put("minutes", d.minutes);
                                cv.put("flag", d.flag);
                                mDb.insert("users", null, cv);
                            }
                        }
                        data.clear();
                        data = null;

                        finish();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.contains(1);
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView aText = alertDialog.findViewById(android.R.id.message);
        aText.setTypeface(Typeface.createFromAsset(getAssets(), "font/centurygothic.ttf"));
    }

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, new VKAuthCallback() {
            @Override
            public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                VK_Controller.UserID = vkAccessToken.getUserId();
                controller.UpdateUserID();
                controller.UpdateFriends();
            }

            @Override
            public void onLoginFailed(int i) {
                Intent n = getIntent();
                finish();
                startActivity(n);
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/


}