package com.example.kent.split2.RecyclerViewFriends;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kent.split2.R;
import com.example.kent.split2.RecyclerViewFollow.RCViewHoldersFollow;
import com.example.kent.split2.RecyclerViewFollow.UsersObject;
import com.example.kent.split2.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RCAdapterFriends extends RecyclerView.Adapter<RCViewHoldersFriends>{
    private List<FriendObject> usersList;
    private Context context;

    public RCAdapterFriends(ArrayList<FriendObject> usersList, Context context){
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public RCViewHoldersFriends onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_friend_item, parent,false);
        RCViewHoldersFriends rcv = new RCViewHoldersFriends(layoutView);
        return rcv;
    }


    @Override
    public void onBindViewHolder(@NonNull RCViewHoldersFriends holder, int position) {
        if (usersList.get(position).getEmail() != null){
         holder.mEmail.setText(usersList.get(position).getEmail());}
    }

    @Override
    public int getItemCount() {
        return this.usersList.size();
    }
}
