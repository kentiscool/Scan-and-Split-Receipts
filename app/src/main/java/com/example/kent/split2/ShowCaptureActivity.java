package com.example.kent.split2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ShowCaptureActivity extends AppCompatActivity {

    public static ArrayList<Line> totalComponents = new ArrayList<>();
    public static ArrayList<List<Line>> lines = new ArrayList<>();
    public static List<Product> itemsBought = new ArrayList<>();
    Receipt mReceipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_capture);

        totalComponents.clear();
        lines.clear();
        itemsBought.clear();
        mReceipt = null;

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        String path = this.getIntent().getExtras().getString("capture");

        if(path!=null) {
            ImageView imageView = findViewById(R.id.imageCaptured);
            Bitmap decodedBitmap = loadImageFromStorage(path);
            Bitmap rotateBitmap = rotate(decodedBitmap);

            imageView.setImageBitmap(rotateBitmap);
            try {
                getTextFromImage(rotateBitmap);
            }
            catch (Exception e) {
                e.printStackTrace();
            }


        }

        Button mSend = findViewById(R.id.send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> data = new ArrayList<>();

                for (int i = 0; i < mReceipt.getItems().size(); i++) {
                    data.add(mReceipt.getItems().get(i).getName());
                    data.add(String.valueOf(mReceipt.getItems().get(i).getPrice()));
                }
                double total = mReceipt.getTotal();
                System.out.println("loki: " + total);
                Intent intent = new Intent(getApplicationContext(), ReviewReceiptActivity.class);
                intent.putExtra("data",data);
                intent.putExtra("total",total);
                startActivity(intent);

            }
        });

    }

    private Bitmap rotate(Bitmap decodedBitmap) {
        int w = decodedBitmap.getWidth();
        int h = decodedBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        return Bitmap.createBitmap(decodedBitmap,0,0,w,h,matrix,true);
    }

    private void getTextFromImage(final Bitmap picture) {
        try {
            TextRecognizer textRecognizer = new TextRecognizer.Builder(this.getApplicationContext()).build();

            if (!textRecognizer.isOperational()) {
                Toast.makeText(this.getApplicationContext(), "Could not get the Text", Toast.LENGTH_LONG).show();
            } else {
                Frame frame = new Frame.Builder().setBitmap(picture).build();
                SparseArray<TextBlock> items = textRecognizer.detect(frame);
                for (int i = 0; i < items.size(); i++) {
                    TextBlock myItem = items.valueAt(i);
                    List<Line> components = (List<Line>) myItem.getComponents();
                    totalComponents.addAll(components);
                }

            }

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    List<Integer> indexHistory = new ArrayList<Integer>();
                    List<Line> history = new ArrayList<Line>();
                    indexHistory.clear();

                    for (int i = 0; i < totalComponents.size(); i++) {
                        if (!history.contains(totalComponents.get(i))) {
                            System.out.println("boii" + totalComponents.get(i).getValue());
                            history.add(totalComponents.get(i));
                            List<Line> currentLine = new ArrayList<Line>();
                            Line line_1 = totalComponents.get(i);
                            currentLine.add(line_1);
                            int left_1 = line_1.getCornerPoints()[0].x;
                            int right_1 = line_1.getCornerPoints()[1].x;
                            int yMid_1 = line_1.getCornerPoints()[3].y - (line_1.getCornerPoints()[3].y - line_1.getCornerPoints()[0].y) / 2;

                            for (int a = i + 1; a < totalComponents.size(); a++) {
                                Line line_2 = totalComponents.get(a);
                                int left_2 = line_2.getCornerPoints()[0].x;
                                int right_2 = line_2.getCornerPoints()[1].x;
                                int yMax_2 = line_2.getCornerPoints()[0].y;
                                int yMin_2 = line_2.getCornerPoints()[3].y;
                                if (!((left_1 < right_2 && left_1 > left_2) || (right_1 > left_2 && right_1 < right_2)) &&
                                        (yMid_1 < yMin_2 && yMid_1 > yMax_2) &&
                                        (!history.contains(line_2))) {
                                    currentLine.add(line_2);
                                    history.add(line_2);
                                }
                            }
                            lines.add(currentLine);
                        }

                    }
                    for (int i = 0; i < lines.size(); i++) {
                        List<Line> line = lines.get(i);

                        for (int a = line.size() - 1; a > 0; a--) {
                            int rightMost = a;
                            for (int b = a - 1; b >= 0; b--) {
                                if (line.get(rightMost).getCornerPoints()[0].x < line.get(b).getCornerPoints()[0].x) {
                                    Line temp = line.get(rightMost);
                                    line.set(rightMost, line.get(b));
                                    line.set(b, temp);
                                    rightMost = b;
                                }
                            }
                        }
                    }

                    // Scans through text to find product name and price
                    for (int i = 0; i < lines.size(); i++) {
                        List<Line> line = lines.get(i);
                        String lineString = "";
                        for (int a = 0; a < line.size(); a++) {
                            lineString += " " + line.get(a).getValue();
                        }
                        System.out.println("yo " + lineString);
                        if (checkForPrice(lineString)) {
                            boolean falsePositive = false;
                            for (int a = lineString.length() - 1; a >= 0; a--) {
                                if (lineString.charAt(a) == '.') {
                                    int b, c;
                                    for (b = a - 1; b >= 0 && lineString.charAt(b) != ' '; b--) {
                                        while (lineString.charAt(b) != ' ') {
                                            if (isLegalCharacter(lineString.charAt(b))) {
                                                b--;
                                            } else {
                                                falsePositive = true;
                                                break;
                                            }
                                        }
                                        c = a + 1;
                                        while (lineString.charAt(c) != ' ' && c < lineString.length() - 1 && !falsePositive) {
                                            if (isLegalCharacter(lineString.charAt(c))) {
                                                c++;
                                            } else {
                                                falsePositive = true;
                                                break;
                                            }
                                        }
                                        if (!falsePositive) {
                                            String price = "";
                                            String name = "";
                                            for (int k = b + 1; k < c + 1; k++) {
                                                price += lineString.charAt(k);
                                            }
                                            for (int k = 0; k < b; k++) {
                                                name += lineString.charAt(k);
                                            }
                                            System.out.println("product: " + name + " = " + price);
                                            Product p = new Product(name, Double.valueOf(price));
                                            itemsBought.add(p);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    mReceipt = new Receipt(itemsBought);
                }
            };

            Thread thread = new Thread(r);
            thread.start();
        }catch (Exception e) {
            System.out.println("er: " + e);
        }
    }

    public static boolean checkForPrice(String s) {
        return s.matches("^.* +(\\p{Sc}|[a-zA-Z])*(\\d*\\.)?\\d+.$");
    }

    public static boolean isLegalCharacter(char c) {
        //return Character.isDigit(c) || (!Character.isDigit(c) && (c == ',' && Character.isLetter(c)));
        return Character.isDigit(c) || (!Character.isDigit(c) && (c == ','));
    }

    private Bitmap loadImageFromStorage(String path) {
        Bitmap b = null;
        try {
            File f =new File(path, "receipt.jpg");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

}
