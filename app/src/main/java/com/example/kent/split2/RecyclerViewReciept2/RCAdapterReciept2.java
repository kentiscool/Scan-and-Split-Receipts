package com.example.kent.split2.RecyclerViewReciept2;

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

public class RCAdapterReciept2 extends RecyclerView.Adapter<RCAdapterReciept2.RcViewHoldersReciept>{

    Receipt mReceipt;
    private Context context;

    public RCAdapterReciept2(Receipt mReceipt, Context context){
        this.mReceipt = mReceipt;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final RcViewHoldersReciept holder, int position) {
        Product current = mReceipt.getItemsBoughtAfterTaxes().get(position);
        holder.mName.setText(current.getName());
        holder.mPrice.setText(Double.toString(current.getPrice()));
    }

    @NonNull
    @Override
    public RcViewHoldersReciept onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reciept_item, null);
        RcViewHoldersReciept rcv = new RcViewHoldersReciept(layoutView);
        return rcv;
    }

    @Override
    public int getItemCount() {
        return this.mReceipt.getItemsBoughtAfterTaxes().size();
    }

    public class RcViewHoldersReciept extends RecyclerView.ViewHolder implements OnCreateContextMenuListener{
        public TextView mName;
        public TextView mPrice;

        public RcViewHoldersReciept(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mPrice = itemView.findViewById(R.id.price);
            CardView cardView = itemView.findViewById(R.id.mCardView);

            cardView.setOnCreateContextMenuListener(this); //REGISTER ONCREATE MENU LISTEN
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1:
                        setAsTax(getAdapterPosition());
                        break;

                    case 2:
                        removeAt(getAdapterPosition());
                        break;

                }
                return true;
            }
        };

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1, 1, "Set as Tax");
            MenuItem Delete = menu.add(Menu.NONE, 2, 2, "Delete");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        public void removeAt(int position) {
            mReceipt.removeItem(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mReceipt.getItemsBoughtAfterTaxes().size());

        }

        public void setAsTax(int position) {
            mReceipt.addTax(position);
            removeAt(position);
        }

    }

}
