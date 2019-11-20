package com.basant.yesicbap.tourism;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainItemClickActivity extends AppCompatActivity {
    private TextView mTextViewTitle;
    private ImageView mImageViewPhoto;
    private TextView mTextViewDescription;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_item_click);

        mTextViewTitle = findViewById(R.id.activity_main_item_click_title);
        mImageViewPhoto = findViewById(R.id.activity_main_item_click_image);
        mTextViewDescription = findViewById(R.id.activity_main_item_click_description);


        Log.d("yo", " TITLE = "+ getIntent().getStringExtra("title"));
        Log.d("yo", " Photo = "+ getIntent().getStringExtra("image"));
        Log.d("yo", " Description = "+ getIntent().getStringExtra("description"));

        mTextViewTitle.setText("TITLE: "+ getIntent().getStringExtra("title"));
        Picasso.get().load( getIntent().getStringExtra("image")).into(mImageViewPhoto);
        mTextViewDescription.setText("DESCRIPTION: \n\n"+getIntent().getStringExtra("description"));




    }
}
