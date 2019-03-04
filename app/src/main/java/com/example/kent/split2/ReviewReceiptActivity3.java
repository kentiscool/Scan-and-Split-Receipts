package com.example.kent.split2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.kent.split2.RecyclerViewReciept3.RCAdapterReceipt3;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ReviewReceiptActivity3 extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Receipt mReceipt;
    TextView mTotalTextView;
    FloatingActionButton mFab;
    FirebaseAuth mAuth;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_receipt3);
        mAuth = FirebaseAuth.getInstance();

        List<Product> listOfProducts = new ArrayList<>();
        Bitmap bitmap = this.getIntent().getParcelableExtra("BitmapImage");
        ArrayList<String> data = this.getIntent().getStringArrayListExtra("data");
        for (int i =0; i < data.size(); i = i+2) {
            Product p =new Product(data.get(i),Double.valueOf(data.get(i+1)));
            listOfProducts.add(p);
        }
        title = this.getIntent().getExtras().getString("title");
        System.out.println("title1: " + title);
        mTotalTextView = findViewById(R.id.totalTextView);
        mTotalTextView.setText("0");
        mReceipt = new Receipt(listOfProducts,bitmap);
        mReceipt.setTotal(this.getIntent().getExtras().getDouble("total"));
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RCAdapterReceipt3(mReceipt, mTotalTextView);
        mRecyclerView.setAdapter(mAdapter);

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> data = new ArrayList<>();
                ArrayList<Integer> data2 = new ArrayList<>();
                for (int i = 0; i < mReceipt.getItemsBoughtAfterTaxes().size(); i++) {
                    Product p = mReceipt.getItemsBoughtAfterTaxes().get(i);
                    data.add(p.getName());
                    data.add(String.valueOf(p.getPrice()));
                    if(!p.getUsers().isEmpty()) {
                        data2.add(i);
                    }
                }

                Intent intent = new Intent(getApplicationContext(), ChooseReceiverActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("data",data);
                intent.putExtra("data2",data2);
                intent.putExtra("total", mReceipt.getTotal());
                startActivity(intent);

            }
        });
    }
}
