package com.basant.yesicbap.tourism;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class GuideActivity extends AppCompatActivity {

    private static final String TAG = "GuideActivity";
    private static final String KEY_FIRST_NAME = "FirstName";
    private static final String KEY_LAST_NAME = "LastName";
    private static final String KEY_PHONE_NUMBER = "PhoneNumber";
    private static final Integer KEY_AGE = 28;
    private static final String KEY_DESCRIPTION = "Description";
    private static final Boolean KEY_STATUS = true;


 private EditText mFirstName;
 private EditText mLastName;
 private EditText mPhoneNumber;
 private EditText mAge;
 private EditText mDescription;
 private Button mSubmit;
 private Boolean checked;

 private Boolean status;
 private  String firstName, lastName;
 private  String phoneNumber;
 private int age;
 private String description;


    //firebase reference
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference guideRef = db.document("Guide/Information");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        //referencing
        mFirstName = findViewById(R.id.guide_guide_first_name);
        mLastName = findViewById(R.id.guide_guide_last_name);
        mPhoneNumber = findViewById(R.id.guide_guide_phone_number);
        mAge = findViewById(R.id.guide_guide_age);
        mDescription = findViewById(R.id.guide_guide_describtion);
        mSubmit = findViewById(R.id.guide_submit_button);




        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perform some task
                sendGuideInformationToDatabase();

            }
        });



    }// end create

    private void sendGuideInformationToDatabase() {

        if (mFirstName.getText().toString().trim().isEmpty() || mLastName.getText().toString().trim().isEmpty() || mPhoneNumber.getText().toString().trim().isEmpty() ||
              mAge.getText().toString().trim().isEmpty() || mDescription.getText().toString().trim().isEmpty() || (! checked)){
            Log.d(TAG, "sendGuideInformationToDatabase: filed are empty");
        }else {
            firstName  = mFirstName.getText().toString();
            lastName = mLastName.getText().toString();
            phoneNumber = mPhoneNumber.getText().toString();
            if(mAge.getText().toString().trim().isEmpty()){
                Toast.makeText(getApplicationContext(),"DATA FIELDS ARE NOT FILLED PROPERLY !!!", Toast.LENGTH_LONG).show();
            }else {
                age = Integer.parseInt(mAge.getText().toString());
            }
            description = mDescription.getText().toString();
            // there is status as boolean value









        }// end else



    }




    //start radio button
    public void onRadioButtonClicked(View view) {

        checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.guide_guide_male:
                if (checked){
                    // male is checked
                    status = true;
                }

                break;
            case R.id.guide_guide_female:
                if (checked){
                    // female is checked
                    status = false;
                }

                break;

        }
    }

    //end radio button







}
