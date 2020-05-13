package com.example.arrivalmessage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.FontStyle;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

//import com.example.arrivalmessage.DownloadImagesTask;
import com.example.arrivalmessage.VK_Module.NotificationData;
import com.example.arrivalmessage.VK_Module.VKUser;
import com.example.arrivalmessage.VK_Module.VK_Controller;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;


public class SelectUserActivity extends AppCompatActivity {
    // TableLayout tableLayout;
    static List<VKUser> friends;
    static  List<VKUser> displayedFriends;
    static HashSet<VKUser> chosenFriends;
    ImageView table;
    String[] displayFriends;
    SearchView searchView;
    RecyclerView listView;

    static HashMap<Integer, CircularImageView> images;

    UserAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        addListenerOnButton();
        if (chosenFriends != null)
            chosenFriends.clear();
        if (friends == null)
            friends = VK_Controller.friends;
        for (VKUser user : friends) {
            user.isCheked = false;
            for (int id : MainActivity.curData.idChosenFriends)
                if (id == user.id) {
                    user.isCheked = true;

                }
        }
        Comparator<VKUser> comparator = new Comparator<VKUser>() {
            public int compare(VKUser o1, VKUser o2) {
                return o1.lastname.compareTo(o2.lastname);
            }

        };
        displayedFriends = new ArrayList<VKUser>();
        displayedFriends.addAll(friends);
        chosenFriends = new HashSet<VKUser>();
        if (images == null) {
            images = new HashMap<>();

        }

        
        table = findViewById(R.id.users_table);
        searchView = findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        adapter = new UserAdapter(SelectUserActivity.this){
        };
        listView = findViewById(R.id.list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        //listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
        listView.smoothScrollToPosition(-21);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapter.getFilter().filter(s);
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    public void addListenerOnButton() {
        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (VKUser user : friends)
                            if (user.isCheked)
                                chosenFriends.add(user);
                        int j = 0;
                        MainActivity.curData.idChosenFriends = new int[chosenFriends.size()];
                        MainActivity.curData.displayFriends = new String[chosenFriends.size()];
                        for (VKUser chosenFriend : chosenFriends) {
                            MainActivity.curData.idChosenFriends[j] = chosenFriend.id;
                            MainActivity.curData.displayFriends[j] = chosenFriend.firstname + " " + chosenFriend.lastname;
                            j++;
                        }
                        if (MainActivity.reserveData != null) {
                            MainActivity.curData = MainActivity.reserveData;
                            MainActivity.reserveData = null;
                        }
                        chosenFriends.clear();
                        // Intent intent = new Intent(SelectUserActivity.this, MainActivity.class);
                        SelectUserActivity.super.finish();
                        // startActivity(intent);
                    }
                }

        );
        Button next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (VKUser user : friends)
                            if (user.isCheked)
                                chosenFriends.add(user);
                        if (chosenFriends.size() == 0) {
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
                            TextView aText = dialog.findViewById(android.R.id.message);
                            aText.setTypeface(Typeface.createFromAsset(getAssets(), "font/centurygothic.ttf"));
                            return;
                        }
                        int j = 0;
                        MainActivity.curData.idChosenFriends = new int[chosenFriends.size()];
                        MainActivity.curData.displayFriends = new String[chosenFriends.size()];
                        for (VKUser chosenFriend : chosenFriends) {
                            MainActivity.curData.idChosenFriends[j] = chosenFriend.id;
                            MainActivity.curData.displayFriends[j] = chosenFriend.firstname + " " + chosenFriend.lastname;
                            j++;
                        }
                        Intent intent = new Intent(SelectUserActivity.this, SelectLocationActivity.class);
                        FinishManager.addActivity(SelectUserActivity.this);
                        chosenFriends.clear();
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        for (VKUser user : friends)
            if (user.isCheked)
                chosenFriends.add(user);
        int j = 0;
        MainActivity.curData.idChosenFriends = new int[chosenFriends.size()];
        MainActivity.curData.displayFriends = new String[chosenFriends.size()];
        for (VKUser chosenFriend : chosenFriends) {
            MainActivity.curData.idChosenFriends[j] = chosenFriend.id;
            MainActivity.curData.displayFriends[j] = chosenFriend.firstname + " " + chosenFriend.lastname;
            j++;
        }
        if (MainActivity.reserveData != null) {
            MainActivity.curData = MainActivity.reserveData;
            MainActivity.reserveData = null;
        }
        chosenFriends.clear();
        super.onBackPressed();
    }
}


