package com.example.arrivalmessage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.arrivalmessage.VK_Module.VK_Controller;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startactivity);

        VK.login(this, Arrays.asList(VKScope.FRIENDS));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();

        VK_Controller controller = new VK_Controller(requestQueue,getResources().getString(R.string.Access_Key));

        if (VK.isLoggedIn()) {
            System.out.println(controller.GetFriends());
            controller.SendMessage(1111111, "Object is near");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(data == null || !VK.onActivityResult(requestCode, resultCode, data, new VKAuthCallback() {
            @Override
            public void onLogin(@NotNull VKAccessToken vkAccessToken) {

            }

            @Override
            public void onLoginFailed(int i) {

            }
        }))
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

