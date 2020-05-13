package com.example.arrivalmessage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.arrivalmessage.VK_Module.VK_Controller;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


public class AuthActivity extends AppCompatActivity {

    public static VK_Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth);
        //addListenerOnButton();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Внимание!");
        builder.setMessage("Для работы приложения нужен доступ к интернету");
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent n = getIntent();
                finish();
                startActivity(n);
            }
        }
        );
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        if(!hasConnection(this))
        {
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (controller == null) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            controller = new VK_Controller(requestQueue, getResources().getString(R.string.Access_Key), getResources().getString(R.string.Group_id));
        }

        if (!VK.isLoggedIn()) {
            VK.login(this, Arrays.asList(VKScope.FRIENDS));
        } else {
            controller.UpdateFriends();
            controller.UpdateUserID();
            Thread logoTimer = new Thread()
            {
                public void run()
                {
                    try
                    {
                        int logoTimer = 0;
                        while(VK_Controller.friends.size()==0)
                        {
                            sleep(100);
                            logoTimer = logoTimer +100;
                            if(logoTimer==1000)
                            {
                                controller.UpdateFriends();
                                logoTimer = 0;
                            }
                        };
                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        finish();
                    }
                }
            };
            logoTimer.start();
        }

    }
    public void addListenerOnButton() {
        /*Button create_btn = findViewById(R.id.start_but);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        });*/
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {

        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, new VKAuthCallback() {
            @Override
            public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                //VK_Controller.UserID = vkAccessToken.getUserId();
                controller.UpdateFriends();
                controller.UpdateUserID();
                Thread logoTimer = new Thread()
                {
                    public void run()
                    {
                        try
                        {
                            int logoTimer = 0;
                            while(logoTimer < 3000)
                            {
                                sleep(100);
                                logoTimer = logoTimer +100;
                            };
                            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        finally
                        {
                            finish();
                        }
                    }
                };
                logoTimer.start();
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
    }

}
