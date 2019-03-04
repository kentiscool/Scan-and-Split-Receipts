package com.example.kent.split2.RecyclerViewRecipient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kent.split2.R;

import java.util.List;

public class RCAdapterRecipient extends RecyclerView.Adapter<RCAdapterRecipient.RcViewHoldersRecipient>{

    private List<RecipientObject> usersList;
    private Context context;

    public RCAdapterRecipient(List<RecipientObject> usersList, Context context){
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public RcViewHoldersRecipient onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_recipient_item, null);
        RcViewHoldersRecipient rcv = new RcViewHoldersRecipient(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final RcViewHoldersRecipient holder, int position) {
        holder.mEmail.setText(usersList.get(position).getEmail());
        holder.mEmail.setText(usersList.get(position).getEmail());
        holder.mReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean receiveState = !usersList.get(holder.getLayoutPosition()).getRecieve();
                usersList.get(holder.getLayoutPosition()).setRecieve(receiveState);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.usersList.size();
    }

    public class RcViewHoldersRecipient extends RecyclerView.ViewHolder{
        public TextView mEmail;
        public CheckBox mReceive;

        public RcViewHoldersRecipient(View itemView){
            super(itemView);
            mEmail = itemView.findViewById(R.id.email);
            mReceive = itemView.findViewById(R.id.receive);
        }

    }

}
