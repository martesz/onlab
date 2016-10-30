package org.martin.getfreaky.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import org.martin.getfreaky.R;
import org.martin.getfreaky.dataObjects.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by martin on 2016. 10. 25..
 */

public class FriendRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private CheckBox lastChecked;
    private int lastCheckedPos;

    private List<User> friends;
    private Context context;

    public FriendRecyclerAdapter(List<User> friends, Context context) {
        this.friends = friends;
        this.context = context;
    }

    public FriendRecyclerAdapter(Context context) {
        friends = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_friend, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setFriendIdText(friends.get(position).getId());
        holder.setFriendNameText(friends.get(position).getName());
        holder.setFriendEmailText(friends.get(position).getEmail());
        holder.getCheckBox().setTag(position);

        // Default select the first one
        if(position == 0 && lastChecked == null) {
            lastChecked = holder.getCheckBox();
            holder.getCheckBox().setChecked(true);
            lastCheckedPos = 0;
        }

        holder.getCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = (CheckBox) view;
                int clickedPosition = (Integer) checkBox.getTag();
                if (checkBox.isChecked()) {
                    if (lastChecked != null && lastChecked != checkBox) {
                        lastChecked.setChecked(false);
                    }
                    lastChecked = checkBox;
                    lastCheckedPos = clickedPosition;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, User user) {
        friends.add(position, user);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(User user) {
        int position = friends.indexOf(user);
        friends.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        friends.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<User> friends) {
        this.friends.addAll(friends);
        notifyDataSetChanged();
    }

    public String getSelectedUserId() {
        if(lastChecked != null) {
            return friends.get(lastCheckedPos).getId();
        } else {
            return null;
        }
    }
}
