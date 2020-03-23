package com.example.arrivalmessage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.arrivalmessage.VK_Module.VK_Controller;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKAuthParams;
import com.vk.api.sdk.auth.VKAuthResult;
import com.vk.api.sdk.auth.VKScope;
import com.vk.api.sdk.ui.VKWebViewAuthActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    VK_Controller controller;

    private  LocationManager manager;
    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
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
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=0) {
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();

        controller = new VK_Controller(requestQueue, getResources().getString(R.string.Access_Key), getResources().getString(R.string.Group_id));

        if(!VK.isLoggedIn())
        {
            VK.login(this, Arrays.asList(VKScope.FRIENDS));
        }
        else {
            controller.UpdateFriends();
            controller.UpdateUserID();
        }



        manager=(LocationManager) getSystemService(this.LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,listener);

    }
    public void addListenerOnButton(){
        ImageButton create_btn = findViewById(R.id.create_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectUserActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        ImageButton settings_btn = findViewById(R.id.settings_btn);
        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        ImageButton saved_btn = findViewById(R.id.saved_btn);
        saved_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SavedNotificationsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Выход")
                .setMessage("Вы действительно хотите выйти из приложения?")
                .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(data == null || !VK.onActivityResult(requestCode, resultCode, data, new VKAuthCallback() {
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
        }))
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }






}