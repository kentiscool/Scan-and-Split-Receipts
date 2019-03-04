package com.example.kent.split2.RecyclerViewRequests;

import android.content.Context;
import android.content.Intent;
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

import com.example.kent.split2.ChooseReceiverActivity;
import com.example.kent.split2.R;
import com.example.kent.split2.Receipt;
import com.example.kent.split2.ReceiptActivity;
import com.example.kent.split2.ReceiptDatabaseItem;
import com.example.kent.split2.RecyclerViewFollow.UsersObject;
import com.example.kent.split2.RecyclerViewFriends.RCAdapterFriends;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RCAdapterRequests extends RecyclerView.Adapter<RCAdapterRequests.RCViewHoldersRequests>{

    ArrayList<String> users = new ArrayList<>();
    List<ReceiptDatabaseItem> receiptList;
    private Context context;

    public RCAdapterRequests(List<ReceiptDatabaseItem> receiptList, Context context) {
        this.receiptList = receiptList;
        this.context = context;
    }

    @NonNull
    @Override
    public RCViewHoldersRequests onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_request_item, parent,false);
        RCAdapterRequests.RCViewHoldersRequests rcv = new RCAdapterRequests.RCViewHoldersRequests(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull RCAdapterRequests.RCViewHoldersRequests holder, int position) {
        ReceiptDatabaseItem current = receiptList.get(position);
        String currentId = current.getUid();
        holder.mTitle.setText(receiptList.get(position).getReceiptName());
    }

    @Override
    public int getItemCount() {
        return receiptList.size();
    }

    public class RCViewHoldersRequests extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView mTitle;
        CardView mCardView;
        //public TextView mUsers;

        public RCViewHoldersRequests(View itemView) {
            super(itemView);
            //mUsers = itemView.findViewById(R.id.users);
            mTitle = itemView.findViewById(R.id.title);
            mCardView = itemView.findViewById(R.id.cardView);
            mCardView.setOnCreateContextMenuListener(this);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        open(getAdapterPosition());

                    case 2:
                        remove(getAdapterPosition());
                }
                return true;
            }
        };

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem openReceipt = menu.add(Menu.NONE, 1, 1, "Open");
            MenuItem removeReceipt = menu.add(Menu.NONE, 2, 2, "Remove");
            openReceipt.setOnMenuItemClickListener(onEditMenu);
            removeReceipt.setOnMenuItemClickListener(onEditMenu);
        }

        public void open(int position) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String receiptId = receiptList.get(position).getUid(); // Get id from recieptList Array from requestFragment
                                                                               // through intent like in Review Receipt
            Intent intent = new Intent(context,ReceiptActivity.class);
            intent.putExtra("userId",userId);
            intent.putExtra("receiptId",receiptId);
            context.startActivity(intent);
        }

        public void remove(int position) {

        }

    }

}
