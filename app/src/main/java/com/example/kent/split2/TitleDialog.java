package com.example.kent.split2;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TitleDialog extends DialogFragment {

    private static final String TAG = "MyCustomDialog";

    public interface OnInputListener{
        void sendInput(String input);
    }
    public OnInputListener mOnInputListener;
    private EditText mEditTextTitle;
    private Button mBtnTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.title_dialog, container, false);

        mBtnTitle = view.findViewById(R.id.btnTitle);
        mEditTextTitle = view.findViewById(R.id.editTextTitle);

        mBtnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: capturing input");

                String input = mEditTextTitle.getText().toString();
                if(!input.equals("")){

                    //Easiest way: just set the value
                   ((ReviewReceiptActivity)getActivity()).mTitleTextView.setText(input);

               }else {
                    System.out.println("null");
                }

                //"Best Practice" but it takes longer
                //mOnInputListener.sendInput(input);
                getDialog().dismiss();

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }
}
