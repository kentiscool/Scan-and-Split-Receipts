package com.example.kent.split2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.kent.split2.RecyclerViewNewReceipt.RCAdapterNewReceipt;

import java.util.ArrayList;
import java.util.List;

public class ReviewReceiptActivity extends AppCompatActivity implements RecheckDialog.FragmentAListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Receipt mReceipt;
    TextView mTitleTextView;
    TextView mTotalTextView;
    TextView mCalculatedTotalTextView;
    private TextView mTaxTextView;
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_review_receipt);

        mTotalTextView = findViewById(R.id.totalTextView);
        mTaxTextView = findViewById(R.id.taxTextView);
        mCalculatedTotalTextView = findViewById(R.id.calculatedTotalTextView);

        mTitleTextView = findViewById(R.id.titleTextView);
        mTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TitleDialog dialog = new TitleDialog();
                dialog.show(getFragmentManager(), "Title Dialog");
            }
        });

        List<Product> listOfProducts = new ArrayList<>();
        Bitmap bitmap = this.getIntent().getParcelableExtra("BitmapImage");
        ArrayList<String> data = this.getIntent().getStringArrayListExtra("data");

        for (int i =0; i < data.size(); i = i+2) {
            Product p =new Product(data.get(i),Double.valueOf(data.get(i+1)));
            listOfProducts.add(p);
        }

        mReceipt = new Receipt(listOfProducts,bitmap);
        double total = this.getIntent().getExtras().getDouble("total");
        mReceipt.setTotal(total);
        mReceipt.setTotal(mReceipt.getLargest().getPrice());
        mReceipt.removeItem(mReceipt.getLargest());

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new RCAdapterNewReceipt(mReceipt, mTotalTextView, mTaxTextView, mCalculatedTotalTextView);
        mRecyclerView.setAdapter(mAdapter);

        mTotalTextView.setText(Double.toString(mReceipt.getTotal()));
        mTaxTextView.setText("0");
        mCalculatedTotalTextView.setText(Double.toString(mReceipt.calculateTotal()));

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("pressed");
                if (Double.parseDouble(mTotalTextView.getText().toString()) - mReceipt.getCalculatedTotal()
                        != 0) // Math.abs(Double.parseDouble(mCalculatedTotalTextView.getText().toString()))*0.1) {
                {
                    System.out.println("works");
                    RecheckDialog dialog = new RecheckDialog();
                    dialog.show(getFragmentManager(), "MyCustomDialog");
                }else {
                    newActivity();
                }

            }
        });

        TitleDialog dialog = new TitleDialog();
        dialog.show(getFragmentManager(), "MyCustomDialog");

    }

    public void newActivity() {
        ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < mReceipt.getItemsBoughtAfterTaxes().size(); i++) {
            data.add(mReceipt.getItemsBoughtAfterTaxes().get(i).getName());
            data.add(String.valueOf(mReceipt.getItems().get(i).getPrice()));
        }

        Intent intent = new Intent(getApplicationContext(), ReviewReceiptActivity3.class);
        intent.putExtra("title",mTitleTextView.getText());
        intent.putExtra("data",data);
        intent.putExtra("total", mReceipt.getTotal());
        startActivity(intent);

    }

    @Override
    public void onInputASent(Boolean b) {
        if (b) { newActivity(); }
    }
}
