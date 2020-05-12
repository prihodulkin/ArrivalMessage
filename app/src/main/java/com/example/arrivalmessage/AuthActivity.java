package com.example.arrivalmessage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                        while(logoTimer < 5000)
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
                            while(logoTimer < 5000)
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
