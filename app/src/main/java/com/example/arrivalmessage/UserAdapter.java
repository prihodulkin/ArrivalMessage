package com.example.arrivalmessage;



import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arrivalmessage.VK_Module.VKUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements Filterable {


    Context context;

    UserAdapter(Context context) {
        this.context=context;
    }
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the layout view you have created for the list rows here
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserAdapter.ViewHolder viewHolder, int position) {
        VKUser friend = SelectUserActivity.displayedFriends.get(position);


        viewHolder.fullName.setTag(friend);
        viewHolder.check.setTag(SelectUserActivity.displayedFriends.get(position));
        viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VKUser element = (VKUser) viewHolder.check
                        .getTag();
                element.isCheked = buttonView.isChecked();
            }
        });
      /*  viewHolder.fullName.setText(friend.firstname + ' ' + friend.lastname + '\n');
        Picasso.Builder picassoBuilder = new Picasso.Builder(context);
        Picasso picasso = picassoBuilder.build();
        picasso.load(friend.photo).into(viewHolder.avatar);*/
        viewHolder.check.setChecked(friend.isCheked);
        CircularImageView avatarCpy = new CircularImageView(context);
        viewHolder.fullName.setText(friend.firstname + ' ' + friend.lastname + '\n');

        avatarCpy.setTag(friend);

        if (!SelectUserActivity.images.containsKey(friend.id)) {
            SelectUserActivity.images.put(friend.id, avatarCpy);
            Picasso.Builder picassoBuilder = new Picasso.Builder(context);
            Picasso picasso = picassoBuilder.build();
            if ( SelectUserActivity.images.get(friend.id).getDrawable() == null)
                picasso.load(friend.photo).into(viewHolder.avatar);
            picasso.load(friend.photo).into(avatarCpy);
        }
        if (avatarCpy.getTag() == null)
            avatarCpy.setTag(friend);
        viewHolder.avatar.setImageDrawable( SelectUserActivity.images.get(friend.id).getDrawable());
        if ( SelectUserActivity.images.get(friend.id).getDrawable() == null) {
            DownloadImagesTask downloadImagesTask = new DownloadImagesTask(avatarCpy);
            downloadImagesTask.execute(viewHolder.avatar);
        }
    }





    @Override
    public int getItemCount() {
        return SelectUserActivity.displayedFriends.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<VKUser> tempList = new ArrayList<VKUser>();
                if (constraint != null && SelectUserActivity.friends != null) {
                    int length = SelectUserActivity.friends.size();
                    int i = 0;
                    while (i < length) {
                        VKUser item = SelectUserActivity.friends.get(i);
                        String fullName = item.firstname + ' ' + item.lastname;
                        if (fullName.toLowerCase().contains(constraint.toString().toLowerCase()))
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
                SelectUserActivity.displayedFriends.clear();
                SelectUserActivity.displayedFriends.addAll((ArrayList<VKUser>) results.values);
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetChanged();
                }
            }
        };
        return myFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected CircularImageView avatar;
        protected CheckBox check;
        protected TextView fullName;
        protected boolean isCheked;
        ViewHolder(View view){
            super(view);
            avatar = (CircularImageView) view.findViewById(R.id.avatar);
            fullName = view.findViewById(R.id.full_name);
            check = view.findViewById(R.id.checkbox);
            isCheked=false;
        }
    }
}