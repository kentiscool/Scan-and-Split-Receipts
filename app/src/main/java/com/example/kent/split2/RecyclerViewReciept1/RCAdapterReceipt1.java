package com.example.kent.split2.RecyclerViewReciept1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kent.split2.Product;
import com.example.kent.split2.R;
import com.example.kent.split2.Receipt;

public class RCAdapterReceipt1 extends RecyclerView.Adapter<RCAdapterReceipt1.RcViewHoldersReceipt>{

    Receipt mReceipt;
    private Context context;

    public RCAdapterReceipt1(Receipt mReceipt, Context context){
        this.mReceipt = mReceipt;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final RcViewHoldersReceipt holder, int position) {
        Product current = mReceipt.getItemsBoughtAfterTaxes().get(position);
        holder.mName.setText(current.getName());
        holder.mPrice.setText(Double.toString(current.getPrice()));
    }

    @NonNull
    @Override
    public RcViewHoldersReceipt onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reciept_item, null);
        RcViewHoldersReceipt rcv = new RcViewHoldersReceipt(layoutView);
        System.out.println("created");
        return rcv;
    }

    @Override
    public int getItemCount() {
        return this.mReceipt.getItemsBoughtAfterTaxes().size();
    }

    public class RcViewHoldersReceipt extends RecyclerView.ViewHolder implements OnCreateContextMenuListener{
        public TextView mName;
        public TextView mPrice;

        public RcViewHoldersReceipt(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mPrice = itemView.findViewById(R.id.price);
            CardView cardView = itemView.findViewById(R.id.mCardView);

            itemView.setOnCreateContextMenuListener(this); //REGISTER ONCREATE MENU LISTEN
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1:
                        setAsTotal(getAdapterPosition());

                }
                return true;
            }
        };

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Total = menu.add(Menu.NONE, 1, 1, "Set as Total");
            Total.setOnMenuItemClickListener(onEditMenu);
        }

        }

        public void setAsTotal(int position) {
            mReceipt.setTotal(position);
            removeAt(position);
        }

        public void removeAt(int position) {
            mReceipt.removeItem(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mReceipt.getItemsBoughtAfterTaxes().size());
        }

    }

