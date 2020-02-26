package com.example.arrivalmessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Bitmap;


import com.example.arrivalmessage.VK_Module.VKUser;
import com.example.arrivalmessage.VK_Module.VK_Controller;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class SelectUserActivity extends AppCompatActivity {


    TableLayout tableLayout;
    List<VKUser> friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_user);
        addListenerOnButton();
        friends = VK_Controller.friends;

        tableLayout = findViewById(R.id.userList);
        createUserList();
    }
    public void createUserList(){
        for (int i = 0; i < friends.size(); i++){
            TableRow tableRow = new TableRow(this);
            ImageView photo = new ImageView(this);
            TextView firstName  = new TextView(this);
            TextView lastName = new TextView(this);

            CheckBox check = new CheckBox(this);

//            String userPhotoUrl = friends.get(i).photo;
//            if (userPhotoUrl != null){
//            Picasso.get().load()
//                    .resize(50,50)
//                    .into(photo);
//            }

            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

            firstName.setText(friends.get(i).firstname);
            firstName.setTextColor(-1);

            lastName.setText(friends.get(i).lastname);
            lastName.setTextColor(-1);

            tableRow.addView(firstName);
            tableRow.addView(lastName);
            tableRow.addView(check);

            tableLayout.addView(tableRow);
        }
    }
    public void addListenerOnButton(){
        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SelectUserActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
        );
        ImageButton next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SelectUserActivity.this, SelectLocationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
        );
    }

}
