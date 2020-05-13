package com.example.arrivalmessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.arrivalmessage.VK_Module.NotificationData;
import com.example.arrivalmessage.VK_Module.VKUser;

import java.util.HashMap;
import java.util.List;

public class SavedNotificationsActivity extends AppCompatActivity {
    TableLayout tableLayout;
    List<NotificationData> data = MainActivity.data;
    NotificationData curData;
    TextView nothingText;
    HashMap<TableRow, NotificationData> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_notifications);
        addListenerOnButton();
        tableLayout = findViewById(R.id.notificationsList);
        nothingText = new TextView(this);
        nothingText.setText("Созданных уведомлений нет");
        nothingText.setTextColor(-1);
        nothingText.setTextSize(20);
        map=new HashMap<>();


        //nothingText.setTypeface(T);
        nothingText.setGravity(Gravity.CENTER_HORIZONTAL);
        nothingText.setGravity(Gravity.CENTER_HORIZONTAL);


        if (data.size() == 0) {
            tableLayout.addView(nothingText);

        }
        for (int i = 0; i < MainActivity.data.size(); i++) {
            final TableRow tableRow = new TableRow(this);
            map.put(tableRow,MainActivity.data.get(i));
            // tableRow.setMinimumHeight(170);
            TableRow actionsRow = new TableRow(this);
            actionsRow.setGravity(Gravity.CENTER);
            // actionsRow.setPadding(0, 10, 0, 0);
            final TextView notificationText = new TextView(this);
            final Button editButton = new Button(this);
            editButton.setTag(tableRow);
            editButton.setBackground(getResources().getDrawable(R.drawable.edit));
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SavedNotificationsActivity.this, SelectUserActivity.class);
                    MainActivity.reserveData = MainActivity.curData;
                    MainActivity.curData = map.get((TableRow) editButton.getTag());
                    startActivity(intent);
                }
            });
            TableRow space = new TableRow(this);
            final Button deleteButton = new Button(this);
            deleteButton.setBackground(getResources().getDrawable(R.drawable.delete));
            deleteButton.setTag(tableRow);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(final View view) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(SavedNotificationsActivity.this);
                                                    builder
                                                            .setMessage("Вы действительно хотите удалить уведомление?")
                                                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                    // SelectUserActivity.chosenFriends.clear();
                                                                    //  SelectUserActivity.friends.clear();
                                                                    tableLayout.removeAllViews();
                                                                    data.remove((map.get( view.getTag())));
                                                                    map.remove((View)view.getTag());
                                                                    for(TableRow t:map.keySet())
                                                                    {
                                                                        tableLayout.addView(t);
                                                                    }





                                                                    if (data.isEmpty())
                                                                        tableLayout.addView(nothingText);
                                                                    dialog.cancel();
                                                                }
                                                            })
                                                            .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.cancel();
                                                                }
                                                            });
                                                    AlertDialog alertDialog = builder.create();
                                                    alertDialog.show();
                                                    TextView aText = alertDialog.findViewById(android.R.id.message);
                                                    aText.setTypeface(Typeface.createFromAsset(getAssets(), "font/centurygothic.ttf"));

                                                }
                                            }


            );
            final Switch toggle = new Switch(this);
            Resources r = getResources();
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());

            toggle.setTag(i);
            toggle.setWidth(px);
            toggle.setChecked(data.get(i).isEnabled == 1);
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (data.get((int) buttonView.getTag()).isEnabled == 1) {
                        data.get((int) buttonView.getTag()).isEnabled = 0;

                    } else {
                        data.get((int) buttonView.getTag()).isEnabled = 1;

                    }
                }
            });

            notificationText.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 5f));

            notificationText.setText(MainActivity.data.get(i).location + "\n");
            notificationText.setTextColor(-1);
            notificationText.setWidth(px * 8);
            //   notificationText.setTypeface(face);
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            notificationText.setGravity(Gravity.CENTER_HORIZONTAL);


            actionsRow.addView(editButton, px, px);
            actionsRow.addView(space, px / 3, px / 3);
            actionsRow.addView(deleteButton, px, px);
            actionsRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT, 2f));

            tableRow.addView(toggle, px * 2, px * 2);
            tableRow.addView(notificationText);
            tableRow.addView(actionsRow);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            tableLayout.addView(tableRow);
        }
    }

    public void addListenerOnButton() {
        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SavedNotificationsActivity.super.finish();
                    }
                }
        );
    }
}
