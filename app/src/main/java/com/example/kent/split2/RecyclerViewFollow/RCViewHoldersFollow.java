package com.example.kent.split2.RecyclerViewFollow;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kent.split2.R;

public class RCViewHoldersFollow extends RecyclerView.ViewHolder{
    public TextView mEmail;
    public Button mFollow;

    public RCViewHoldersFollow(View itemView){
        super(itemView);
        mEmail = itemView.findViewById(R.id.email);
        mFollow = itemView.findViewById(R.id.follow);
    }
}
