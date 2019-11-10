package com.basant.yesicbap.tourism;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgetAndChangePassword extends AppCompatActivity {



    private TextView mTitle, mMainTitle;
    private EditText mEmail;
    private Button mSubmit;

    private FirebaseAuth auth;
    private ProgressDialog PD;

    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_and_change_password);


        //for progressbar
        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        //getting firebase instance
        auth = FirebaseAuth.getInstance();

        //referencing
        mTitle = findViewById(R.id.forget_title);
        mEmail = findViewById(R.id.forget_email);
        mSubmit = findViewById(R.id.forget_submit_button);
        mMainTitle = findViewById(R.id.forget_main_title);

        //receiving intent
        Intent i = getIntent();
        mode = i.getIntExtra("Mode", 0);


        if (mode == 0) {
            mMainTitle.setText("Forget Password");
            mEmail.setHint("Enter Registered Email");
            mTitle.setText("Enter Registered Email");
        } else if (mode == 1) {
            mMainTitle.setText("Change Password");
            mEmail.setHint("Enter New Password");
            mTitle.setText("Enter New Password");
        } else if (mode == 2) {
            mMainTitle.setText("Change Email");
            mEmail.setHint("Enter New Email");
            mTitle.setText("Enter New Email");
        } else {

            mMainTitle.setText("Delete User");
            mTitle.setText("Are you sure .");
            mEmail.setVisibility(View.GONE);
        }

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                callFunction(mode);
            }
        });
    } // end create

    private void callFunction(int mode) {

        final FirebaseUser user = auth.getCurrentUser();
        final String modeStr = mEmail.getText().toString();
        if (mode == 0){
            if (TextUtils.isEmpty(modeStr)) {
                mEmail.setError("Value Required");
            } else {
                PD.show();
                auth.sendPasswordResetEmail(modeStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgetAndChangePassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            Intent intentGo = new Intent(ForgetAndChangePassword.this, MainActivity.class);
                            startActivity(intentGo);


                        } else {
                            Toast.makeText(ForgetAndChangePassword.this, "Failed to send reset email! Please try again", Toast.LENGTH_SHORT).show();
                        }
                        PD.dismiss();

                    }
                });
            }
        } else if (mode == 1) {
            if (TextUtils.isEmpty(modeStr)) {
                mEmail.setError("Value Required");
            } else {
                PD.show();
                user.updatePassword(modeStr)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetAndChangePassword.this, "Password is updated!", Toast.LENGTH_SHORT).show();
                                    Intent intentGoo = new Intent(ForgetAndChangePassword.this, MainActivity.class);
                                    startActivity(intentGoo);

                                } else {
                                    Toast.makeText(ForgetAndChangePassword.this, "Failed to update password. ! Please try again.", Toast.LENGTH_SHORT).show();
                                }
                                PD.dismiss();
                            }

                        });
            }
        } else if (mode == 2) {
            if (TextUtils.isEmpty(modeStr)) {
                mEmail.setError("Value Required");
            } else {
                PD.show();
                user.updateEmail(modeStr)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetAndChangePassword.this, "Email address is updated.", Toast.LENGTH_LONG).show();
                                    Intent intentGooo = new Intent(ForgetAndChangePassword.this, MainActivity.class);
                                    startActivity(intentGooo);

                                } else {
                                    Toast.makeText(ForgetAndChangePassword.this, "Failed to update email! Please try again.", Toast.LENGTH_LONG).show();
                                }
                                PD.dismiss();
                            }
                        });
            }
        } else {

           if (user != null) {
                PD.show();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override    public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetAndChangePassword.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                    Intent intentGoooo = new Intent(ForgetAndChangePassword.this, RegisterActivity.class);
                                    startActivity(intentGoooo);

                                } else {
                                    Toast.makeText(ForgetAndChangePassword.this, "Failed to delete your account! Please try again.", Toast.LENGTH_SHORT).show();
                                }
                                PD.dismiss();
                            }
                        });
            }

        }

    }// call end function






}// end main class
