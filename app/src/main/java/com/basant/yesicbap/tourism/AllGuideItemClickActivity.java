package com.basant.yesicbap.tourism;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AllGuideItemClickActivity extends AppCompatActivity {

    private TextView mTextViewName;
    private ImageView mImageViewPhoto;
    private TextView mTextViewPhoneNumber;
    private TextView mTextViewAge;
    private TextView mTextViewGender;
    private TextView mTextViewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_guide_item_click);
        mTextViewName = findViewById(R.id.activity_all_guide_item_click_name);
        mImageViewPhoto = findViewById(R.id.activity_all_guide_item_click_image);
        mTextViewPhoneNumber = findViewById(R.id.activity_all_guide_item_click_phone_number);
        mTextViewAge = findViewById(R.id.activity_all_guide_item_click_age);
        mTextViewGender = findViewById(R.id.activity_all_guide_item_click_gender);
        mTextViewDescription = findViewById(R.id.activity_all_guide_item_click_description);


        Log.d("yo", " NAME = "+ getIntent().getStringExtra("name"));
        Log.d("yo", " PHOTO = "+ getIntent().getStringExtra("image"));
        Log.d("yo", " PHONE NUMBER = "+ getIntent().getStringExtra("phone"));
        Log.d("yo", " AGE = "+ getIntent().getIntExtra("age", 0));
        Log.d("yo", " GENDER = "+ getIntent().getBooleanExtra("gender", false));
        Log.d("yo", " Description = "+ getIntent().getStringExtra("description"));


        mTextViewName.setText("Name: "+getIntent().getStringExtra("name"));
        Picasso.get().load(getIntent().getStringExtra("image")).into(mImageViewPhoto);
        mTextViewPhoneNumber.setText("Phone Number: "+getIntent().getStringExtra("phone"));
        mTextViewAge.setText( "Age: "+getIntent().getIntExtra("age", 0));
        if(getIntent().getBooleanExtra("gender", false)){
            mTextViewGender.setText("Gender: Male");
            //true == Male
        }else{
            mTextViewGender.setText("Gender: Female");
            //false == Female
        }
        mTextViewDescription.setText("DESCRIPTION :\n\n"+getIntent().getStringExtra("description"));








    }
}
