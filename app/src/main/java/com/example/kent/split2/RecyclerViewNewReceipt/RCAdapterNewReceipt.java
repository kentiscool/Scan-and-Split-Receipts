package com.example.kent.split2.RecyclerViewNewReceipt;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kent.split2.Product;
import com.example.kent.split2.R;
import com.example.kent.split2.Receipt;

public class RCAdapterNewReceipt extends RecyclerView.Adapter<RCAdapterNewReceipt.RcViewHoldersReceipt>{

    private Receipt mReceipt;
    private TextView mTotalTextView;
    private TextView mTaxTextView;
    private TextView mCalculatedTotalTextView;

    public RCAdapterNewReceipt(Receipt mReceipt, TextView total, TextView tax, TextView calcTotal){
        this.mReceipt = mReceipt;
        mTotalTextView = total;
        mTaxTextView = tax;
        mCalculatedTotalTextView = calcTotal;
    }

    @Override
    public void onBindViewHolder(@NonNull final RCAdapterNewReceipt.RcViewHoldersReceipt holder, int position) {
        Product current = mReceipt.getItemsBoughtAfterTaxes().get(position);
        holder.mName.setText(current.getName());
        holder.mPrice.setText(Double.toString(current.getPrice()));
    }

    @NonNull
    @Override
    public RCAdapterNewReceipt.RcViewHoldersReceipt onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reciept_item, parent,false);
        RCAdapterNewReceipt.RcViewHoldersReceipt rcv = new RCAdapterNewReceipt.RcViewHoldersReceipt(layoutView);
        return rcv;
    }

    @Override
    public int getItemCount() {
        return mReceipt.getItemsBoughtAfterTaxes().size();
    }

    public class RcViewHoldersReceipt extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView mName;
        TextView mPrice;

        RcViewHoldersReceipt(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.name);
            mPrice = itemView.findViewById(R.id.price);
            CardView cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnCreateContextMenuListener(this); //REGISTER ONCREATE MENU LISTEN
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        setAsTotal(getAdapterPosition());
                        break;
                    case 1:
                        Delete(getAdapterPosition());
                        break;
                    case 2:
                        setAsTax(getAdapterPosition());
                        break;
                }
                return true;
            }
        };

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Total = menu.add(Menu.NONE, 0, 0, "Set as Total");
            MenuItem Remove = menu.add(Menu.NONE,1,1,"Remove");//Remove
            MenuItem Tax = menu.add(Menu.NONE,2,2,"Set as Tax");//
            Total.setOnMenuItemClickListener(onEditMenu);
            Remove.setOnMenuItemClickListener(onEditMenu);
            Tax.setOnMenuItemClickListener(onEditMenu);
        }

    }

    private void setAsTotal(int position) {
        mReceipt.setTotal(position);
        Delete(position);
        mTotalTextView.setText(Double.toString(mReceipt.getTotal()));
    }

    private void Delete(int position) {
        mReceipt.removeItem(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mReceipt.getItemsBoughtAfterTaxes().size());
        mCalculatedTotalTextView.setText(Double.toString(mReceipt.calculateTotal()));
    }

    private void setAsTax(int position) {
        mReceipt.addTax(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mReceipt.getItemsBoughtAfterTaxes().size());
        mTaxTextView.setText(Double.toString(mReceipt.calculateTaxPercentage()));
        mCalculatedTotalTextView.setText(Double.toString(mReceipt.calculateTotal()));
    }

}
