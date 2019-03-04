package com.example.kent.split2.RecyclerViewReciept3;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kent.split2.Product;
import com.example.kent.split2.R;
import com.example.kent.split2.Receipt;
import com.example.kent.split2.RecyclerViewFriends.FriendObject;
import com.google.firebase.auth.FirebaseAuth;

public class RCAdapterReceipt3 extends RecyclerView.Adapter<RCAdapterReceipt3.RcViewHoldersReceipt>{
    private Receipt mReceipt;
    private FriendObject user;
    private TextView mTotalTextView;
    private double total;

    public RCAdapterReceipt3(Receipt mReceipt, TextView mTotalTextView){
        this.mReceipt = mReceipt;
        user = new FriendObject(FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseAuth.getInstance().getCurrentUser().getEmail());
        this.mTotalTextView = mTotalTextView;
    }

    @Override
    public void onBindViewHolder(@NonNull final RcViewHoldersReceipt holder, final int position) {
        Product current = mReceipt.getItemsBoughtAfterTaxes().get(position);
        holder.mName.setText(current.getName());
        holder.mPrice.setText(Double.toString(current.getPrice()));

        if (mReceipt.getItems().get(position).getUsers().isEmpty()) {
            holder.mCheckBox.setChecked(false);
        }else {
            holder.mCheckBox.setChecked(true);
        }

    }

    @NonNull
    @Override
    public RcViewHoldersReceipt onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_reciept_item_choose, null);
        RcViewHoldersReceipt rcv = new RcViewHoldersReceipt(layoutView);
        return rcv;
    }

    @Override
    public int getItemCount() {
        return this.mReceipt.getItemsBoughtAfterTaxes().size();
    }

    public class RcViewHoldersReceipt extends RecyclerView.ViewHolder{
        TextView mName;
        TextView mPrice;
        CheckBox mCheckBox;
        CardView mCardView;

        public RcViewHoldersReceipt(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mPrice = itemView.findViewById(R.id.price);
            mCheckBox = itemView.findViewById(R.id.checkBox);
            mCardView = itemView.findViewById(R.id.cardView);
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product p = mReceipt.getItemsBoughtAfterTaxes().get(getLayoutPosition());
                    if (!p.getUsers().isEmpty()) {
                        p.removeUser(user);
                        total -= p.getPrice();
                    }else {
                        p.addUser(user);
                        total += p.getPrice();
                    }
                    mTotalTextView.setText(Double.toString(total));
                    notifyDataSetChanged();
                }
            });
        }
    }
}