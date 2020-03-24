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
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class SelectUserActivity extends AppCompatActivity {


    TableLayout tableLayout;
    List<VKUser> friends;
    List<VKUser> chosenFriends;
    int[] idsChosenFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_user);
        addListenerOnButton();
        friends = VK_Controller.friends;
        chosenFriends = new ArrayList();
        tableLayout = findViewById(R.id.userList);
        createUserList();
    }
    public void createUserList(){
        for (int i = 0; i < friends.size(); i++){
            final VKUser friend = friends.get(i);
            TableRow tableRow = new TableRow(this);
            TextView fullName  = new TextView(this);


            CheckBox check = new CheckBox(this);

            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        chosenFriends.add(friend);
                    }
                    else {
                        chosenFriends.remove(friend);
                    }
                }
            });

            CircularImageView avatar=new CircularImageView(this);
            ImageManager.fetchImage(friend.photo,avatar);

            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            tableRow.setMinimumHeight(170);
            fullName.setText(friend.firstname+' '+friend.lastname);
            fullName.setTextColor(-1);
            fullName.setGravity(Gravity.CENTER_HORIZONTAL);
            fullName.setTypeface(null, Typeface.BOLD);

            



            tableRow.addView(avatar,150,150);
            tableRow.addView(fullName,450,50);
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
                        if(chosenFriends.size()==0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(SelectUserActivity.this);
                            builder.setTitle("Внимание!");
                            builder.setMessage("Выберете друзей для отправки сообщения!");
                            builder.setCancelable(false);
                            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return;
                        }

                        idsChosenFriends = new int[chosenFriends.size()];
                        for (int j = 0; j < chosenFriends.size(); j++) {
                            final VKUser chosenFriend = chosenFriends.get(j);
                            idsChosenFriends[j] = chosenFriend.id;
                        }
                        Intent intent = new Intent(SelectUserActivity.this, SelectLocationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("lst", idsChosenFriends);
                        startActivity(intent);
                    }
                }
        );
    }

}
