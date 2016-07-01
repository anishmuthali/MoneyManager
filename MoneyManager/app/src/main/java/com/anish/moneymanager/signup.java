package com.anish.moneymanager;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class signup extends Activity {
    EditText mNameBox;
    String mName;
    EditText mEmailBox;
    EditText mPasswordBox;
    String mEmail;
    String mPassword;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mListener;
    ProgressBar pbar;
    boolean mSuccess = false;
    Button mSignUp;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);

        mNameBox = (EditText) findViewById(R.id.namebox);
        mEmailBox = (EditText) findViewById(R.id.emailbox);
        mPasswordBox = (EditText) findViewById(R.id.passwordbox);
        pbar = (ProgressBar) findViewById(R.id.pbar);
        mAuth = FirebaseAuth.getInstance();
        mSignUp = (Button) findViewById(R.id.signup);
        mRef = FirebaseDatabase.getInstance().getReference("users");

        pbar.setIndeterminate(true);

        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null && !(mName.matches(""))){
                    UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setDisplayName(mName).build();
                    user.updateProfile(changeRequest);
                    pbar.setVisibility(View.INVISIBLE);

                    if(user.getDisplayName() != null) {
                        mRef.child(user.getDisplayName()).child("balance").setValue(0);
                        SimpleDateFormat dfmt = new SimpleDateFormat("MM/dd/yyyy");
                        Date date = new Date();
                        String currDate = dfmt.format(date);
                        mRef.child(user.getDisplayName()).child("date").setValue(currDate);
                        SimpleDateFormat hrfmt = new SimpleDateFormat("HH:mm");
                        Date hr = new Date();
                        String currTime = hrfmt.format(hr);
                        mRef.child(user.getDisplayName()).child("time").setValue(currTime);
                        mRef.child(user.getDisplayName()).child("name").setValue(user.getDisplayName());
                        Intent intent = new Intent(signup.this, home.class);
                        intent.putExtra("name", user.getDisplayName());
                        intent.putExtra("uid", user.getUid());
                        startActivity(intent);
                        //System.out.println("Would start intent because " + user.getDisplayName());
                    }
                }
            }
        };


    }

    @Override
    public void onResume(){
        super.onResume();
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                mName = mNameBox.getText().toString();
                mEmail = mEmailBox.getText().toString();
                mPassword = mPasswordBox.getText().toString();
                if(!mEmail.matches("") && !mPassword.matches("") && !mName.matches("")){
                    mAuth.addAuthStateListener(mListener);
                    mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mSuccess = true;
                            }
                        }
                    });
                }
                else{
                    pbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(signup.this, "One or more fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onBackPressed(){
        this.finish();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mListener != null){
            mAuth.removeAuthStateListener(mListener);
        }
    }
}
