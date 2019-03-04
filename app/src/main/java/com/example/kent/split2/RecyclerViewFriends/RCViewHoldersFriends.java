package com.example.kent.split2.RecyclerViewFriends;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kent.split2.R;

public class RCViewHoldersFriends extends RecyclerView.ViewHolder{

    public TextView mEmail;

    public RCViewHoldersFriends(View itemView){
        super(itemView);
        mEmail = itemView.findViewById(R.id.email);
    }

}
