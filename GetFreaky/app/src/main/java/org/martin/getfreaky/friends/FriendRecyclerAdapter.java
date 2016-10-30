package org.martin.getfreaky.friends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.martin.getfreaky.R;
import org.martin.getfreaky.dataObjects.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by martin on 2016. 10. 25..
 */

public class FriendRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<User> friends;
    Context context;

    public FriendRecyclerAdapter(List<User> friends, Context context) {
        this.friends = friends;
        this.context = context;
    }

    public FriendRecyclerAdapter(Context context){
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

    public void clear(){
        friends.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<User> friends){
        this.friends.addAll(friends);
        notifyDataSetChanged();
    }
}
