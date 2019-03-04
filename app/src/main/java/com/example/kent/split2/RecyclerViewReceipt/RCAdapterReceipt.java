package com.example.kent.split2.RecyclerViewReceipt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kent.split2.ProductDatabaseItem;
import com.example.kent.split2.R;
import com.example.kent.split2.RecyclerViewFriends.FriendObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RCAdapterReceipt extends RecyclerView.Adapter<RCAdapterReceipt.RcViewHoldersReceipt> {

    private ArrayList<ProductDatabaseItem> products;
    private Context context;
    private String receiptId;
    private FriendObject currentUser;

    public RCAdapterReceipt(final ArrayList<ProductDatabaseItem> products, Context context, String receiptId) {
        this.products = products;
        this.context = context;
        this.receiptId = receiptId;
        currentUser = new FriendObject(FirebaseAuth.getInstance().getCurrentUser().getEmail(),FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public void onBindViewHolder(@NonNull final RcViewHoldersReceipt holder, int position) {
        final ProductDatabaseItem current = products.get(position);
        final DatabaseReference userListDB = FirebaseDatabase.getInstance().getReference().child("receipt").child(receiptId).child("products").child(current.getProductId()).child("userList");

        holder.mUsers.setText(current.getUsersString());
        holder.mCheckBox.setChecked(current.isSelected());
        holder.mName.setText(current.getProductName());
        holder.mPrice.setText(Double.toString(current.getProductPrice()));

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean found = false;
                for (int i =0 ; i < current.getUsers().size(); i++) {
                    if (current.getUsers().get(i).getEmail().equals(currentUser.getEmail())) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    userListDB.child(currentUser.getUid()).removeValue();
                    current.getUsers().remove(currentUser);
                }else {
                    String email = currentUser.getEmail();
                    String userUid = currentUser.getUid();
                    userListDB.child(userUid).setValue(email);
                }
                notifyDataSetChanged();
            }
        });

    }

    @NonNull
    @Override
    public RcViewHoldersReceipt onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_request_receipt_item_choose, null);
        return new RcViewHoldersReceipt(layoutView);
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }

    public class RcViewHoldersReceipt extends RecyclerView.ViewHolder{
        TextView mName;
        TextView mPrice;
        TextView mUsers;
        CheckBox mCheckBox;
        CardView mCardView;

        RcViewHoldersReceipt(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mPrice = itemView.findViewById(R.id.price);
            mUsers = itemView.findViewById(R.id.users);
            mCheckBox = itemView.findViewById(R.id.checkBox);
            mCardView = itemView.findViewById(R.id.cardView);
        }

    }

}

