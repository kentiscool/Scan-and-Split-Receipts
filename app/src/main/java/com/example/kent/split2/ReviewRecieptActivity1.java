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

import com.example.kent.split2.RecyclerViewReciept1.RCAdapterReceipt1;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ReviewRecieptActivity1 extends AppCompatActivity implements RecheckDialog.FragmentAListener{

    private int total;
    private int totalTax;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Receipt mReceipt;
    private TextView mTotalTextView;
    Bitmap bitmap;
    FloatingActionButton mfab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_reciept1);

        List<Product> listOfProducts = new ArrayList<>();
        Bitmap bitmap = this.getIntent().getParcelableExtra("BitmapImage");
        ArrayList<String> data = this.getIntent().getStringArrayListExtra("data");
        for (int i =0; i < data.size(); i = i+2) {
            Product p =new Product(data.get(i),Double.valueOf(data.get(i+1)));
            listOfProducts.add(p);
        }
        mReceipt = new Receipt(listOfProducts,bitmap);
        double largest=0;
        for (int i = 0; i < mReceipt.getItemsBoughtAfterTaxes().size(); i++) {
            double CurrentPrice = mReceipt.getItemsBoughtAfterTaxes().get(i).getPrice();
            if (CurrentPrice > largest) {
                largest = CurrentPrice;
            }
        }
        mReceipt.setTotal(largest);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RCAdapterReceipt1(mReceipt,getApplication());
        mRecyclerView.setAdapter(mAdapter);


        mfab = findViewById(R.id.fab);
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> data = new ArrayList<>();

                for (int i = 0; i < mReceipt.getItemsBoughtAfterTaxes().size(); i++) {
                    data.add(mReceipt.getItemsBoughtAfterTaxes().get(i).getName());
                    data.add(String.valueOf(mReceipt.getItems().get(i).getPrice()));
                }

                Intent intent = new Intent(getApplicationContext(), ReviewRecieptActivity2.class);
                intent.putExtra("data",data);
                intent.putExtra("total", mReceipt.getTotal());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onInputASent(Boolean b) {
        if (b) {
            Intent intent = new Intent(getApplicationContext(), ReviewReceiptActivity3.class);
            //intent.putExtra("data",data);
            intent.putExtra("total", mReceipt.getTotal());
            startActivity(intent);
        }
    }

    private ArrayList<Product> results = new ArrayList<>();
    private ArrayList<Product> getDataSet() {
        listenForData();
        return results;
    }

    private void listenForData() {
        for (int i = 0; i< mReceipt.getItemsBoughtAfterTaxes().size(); i++) {
            results.add(mReceipt.getItemsBoughtAfterTaxes().get(i));
        }

    }


}
