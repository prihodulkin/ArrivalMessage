package com.example.arrivalmessage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    List<VKUser> displayedFriends;
    static HashSet<VKUser> chosenFriends;
    ImageView table;
    String[] displayFriends;
    SearchView searchView;
    ListView listView;

    static HashMap<Integer, CircularImageView> images;


    static class ViewHolder {
        protected CircularImageView avatar;
        protected CheckBox check;
        protected TextView fullName;
        protected boolean isCheked;
    }


    private class UserAdapter extends ArrayAdapter<VKUser> {
        public UserAdapter(Context context) {
            super(context, R.layout.list_item, displayedFriends);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final VKUser friend = getItem(position);
            if (view == null) {
                view = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_item, null);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.avatar = (CircularImageView) view.findViewById(R.id.avatar);
                viewHolder.fullName = view.findViewById(R.id.full_name);
                viewHolder.check = view.findViewById(R.id.checkbox);
                view.setTag(viewHolder);
                viewHolder.fullName.setTag(friend);
                viewHolder.check.setTag(displayedFriends.get(position));
                viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        VKUser element = (VKUser) viewHolder.check
                                .getTag();
                        element.isCheked = buttonView.isChecked();
                    }
                });
            } else {
                ((ViewHolder) view.getTag()).check.setTag(displayedFriends.get(position));

            }
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            VKUser user = (VKUser) viewHolder.fullName.getTag();
            viewHolder.fullName.setText(friend.firstname + ' ' + friend.lastname + '\n');
            CircularImageView avatarCpy = new CircularImageView(getApplicationContext());

            avatarCpy.setTag(friend);
            if (!images.containsKey(friend.id)) {
                images.put(friend.id, avatarCpy);
                Picasso.Builder picassoBuilder = new Picasso.Builder(SelectUserActivity.this);
                Picasso picasso = picassoBuilder.build();
                if (images.get(friend.id).getDrawable() == null)
                    picasso.load(friend.photo).into(viewHolder.avatar);
                picasso.load(friend.photo).into(avatarCpy);
            }
            if (avatarCpy.getTag() == null)
                avatarCpy.setTag(friend);
            viewHolder.avatar.setImageDrawable(images.get(friend.id).getDrawable());
            if (images.get(friend.id).getDrawable() == null) {
                DownloadImagesTask downloadImagesTask = new DownloadImagesTask(avatarCpy);
                downloadImagesTask.execute(viewHolder.avatar);
            }
            viewHolder.check.setChecked(friend.isCheked);
            return view;
        }


        @NonNull
        @Override
        public Filter getFilter() {
            Filter myFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    List<VKUser> tempList = new ArrayList<VKUser>();
                    if (constraint != null && friends != null) {
                        int length = friends.size();
                        int i = 0;
                        while (i < length) {
                            VKUser item = friends.get(i);
                            String fullName = item.firstname + ' ' + item.lastname;
                            if (fullName.contains(constraint))
                                tempList.add(item);
                            i++;
                        }

                        filterResults.values = tempList;
                        filterResults.count = tempList.size();
                    }
                    return filterResults;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence contraint, FilterResults results) {
                    displayedFriends.clear();
                    displayedFriends.addAll((ArrayList<VKUser>) results.values);
                    if (results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return myFilter;
        }
    }

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
        //  if (friends != null)
        displayedFriends.addAll(friends);
//        friends.sort(comparator);
        if (chosenFriends == null)
            chosenFriends = new HashSet<VKUser>();

        if (images == null) {
            images = new HashMap<>();

        }

        //  tableLayout = findViewById(R.id.userList);
        table = findViewById(R.id.users_table);
        searchView = findViewById(R.id.searchView);
        adapter = new UserAdapter(SelectUserActivity.this);
        listView = findViewById(R.id.list_view);
        listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
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
                        // Intent intent = new Intent(SelectUserActivity.this, MainActivity.class);
                        SelectUserActivity.super.finish();
                        // startActivity(intent);
                    }
                }

        );
        ImageButton next_btn = findViewById(R.id.next_btn);
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
        super.onBackPressed();
    }
}


