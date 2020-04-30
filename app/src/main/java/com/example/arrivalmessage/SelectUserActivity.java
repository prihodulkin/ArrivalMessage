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
import com.example.arrivalmessage.VK_Module.VKUser;
import com.example.arrivalmessage.VK_Module.VK_Controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;


public class SelectUserActivity extends AppCompatActivity {
    // TableLayout tableLayout;
    List<VKUser> friends;
    List<VKUser> displayedFriends;
    List<VKUser> chosenFriends;
    int[] idsChosenFriends;
    ImageView table;
    String[] displayFriends;
    SearchView searchView;
    ListView listView;
    static HashMap<Integer, CircularImageView> images;

    static class ViewHolder {
        protected CircularImageView avatar;
        protected CheckBox check;
        protected TextView fullName;
        protected  boolean isCheked;
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

            /*if(images.containsKey(friend.id))
                avatar=images.get(friend.id);
            else {*/
                viewHolder.fullName = view.findViewById(R.id.full_name);
                viewHolder.check = view.findViewById(R.id.checkbox);
                view.setTag(viewHolder);
                viewHolder.check.setTag(displayedFriends.get(position));
                viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        VKUser element = (VKUser) viewHolder.check
                                .getTag();
                        element.isCheked=buttonView.isChecked();
                       /* if (isChecked) {
                            viewHolder.isCheked=buttonView.isChecked();
                           // friend.isCheked=true;
                           // chosenFriends.add(friend);
                          //  viewHolder.check.setTag(true);
                        } else {
                            viewHolder.isCheked=buttonView.isChecked();
                           //friend.isCheked=false;
                           // chosenFriends.remove(friend);
                          //  viewHolder.check.setTag(false);
                        }*/
                    }
                });
            } else {
                view = convertView;
                ((ViewHolder) view.getTag()).check.setTag(displayedFriends.get(position));
            }
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.fullName.setText(friend.firstname + ' ' + friend.lastname + '\n');
            viewHolder.avatar.setImageDrawable(getResources().getDrawable(R.drawable.no_avatar));
            viewHolder.avatar.setTag(friend.photo);
               /*if(images.containsKey(friend.id))
                avatar=images.get(friend.id);
            else {*/
            DownloadImagesTask downloadImagesTask = new DownloadImagesTask(friend.id);
            downloadImagesTask.execute(viewHolder.avatar);
            VKUser user = (VKUser) viewHolder.check.getTag();
            viewHolder.check.setChecked(friend.isCheked);
            //  }

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

                    //constraint is the result from text you want to filter against.
                    //objects is your data set you will filter from
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
                        //following two lines is very important
                        //as publish result can only take FilterResults objects
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

        friends = VK_Controller.friends;
        Comparator<VKUser> comparator = new Comparator<VKUser>() {
            public int compare(VKUser o1, VKUser o2) {
                return o1.lastname.compareTo(o2.lastname);
            }

        };
        displayedFriends = new ArrayList<VKUser>();
        displayedFriends.addAll(friends);

//        friends.sort(comparator);
        adapter = new UserAdapter(SelectUserActivity.this);
        chosenFriends = new ArrayList();
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
        images = new HashMap<Integer, CircularImageView>();


        //  tableLayout = findViewById(R.id.userList);
        table = findViewById(R.id.users_table);
        searchView = findViewById(R.id.searchView);

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


        createUserList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void createUserList() {




       /* for (int i = 0; i < friends.size(); i++){
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
            avatar.setImageDrawable(getResources().getDrawable( R.drawable.no_avatar ));
            avatar.setTag(friend.photo);
            DownloadImagesTask downloadImagesTask=new DownloadImagesTask();
            downloadImagesTask.execute(avatar);
            //tableRow.setLayoutParams(new TableLayout.LayoutParams(Math.round(((float)table.getWidth())*(float)0.5), Math.round(((float)table.getWidth())*(float)0.5)));
            tableRow.setMinimumHeight(170);

            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            fullName.setText(friend.firstname+' '+friend.lastname+'\n');
            fullName.setTextColor(-1);
            fullName.setGravity(Gravity.CENTER_HORIZONTAL);
            fullName.setTypeface(null, Typeface.BOLD);
            tableRow.addView(avatar,150,150);
            tableRow.addView(fullName,450,150);
            tableRow.addView(check);

            TableRow tableRow1 = new TableRow(this);




            tableLayout.addView(tableRow);
        }*/
    }

    public void addListenerOnButton() {
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
                        images.containsKey(1);
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

                        idsChosenFriends = new int[chosenFriends.size()];
                        for (int j = 0; j < chosenFriends.size(); j++) {
                            final VKUser chosenFriend = chosenFriends.get(j);
                            idsChosenFriends[j] = chosenFriend.id;
                        }
                        displayFriends = new String[chosenFriends.size()];
                        for (int j = 0; j < chosenFriends.size(); j++) {
                            final VKUser chosenFriend = chosenFriends.get(j);
                            displayFriends[j] = chosenFriend.firstname + " " + chosenFriend.lastname;
                        }
                        Intent intent = new Intent(SelectUserActivity.this, SelectLocationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("lst", idsChosenFriends);
                        intent.putExtra("displayList", displayFriends);
                        startActivity(intent);
                    }
                }
        );
    }

}


