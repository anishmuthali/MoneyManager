package com.anish.moneymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class home extends Activity {
    TextView mGreeting;
    FirebaseAuth.AuthStateListener mListener;
    DatabaseReference mRef;
    String mName;
    ChildEventListener mChildren;
    FirebaseAuth mAuth;
    ValueEventListener mValListener;
    String mUID;
    TextView mBalanceView;
    EditText mValue;
    Button mAdd;
    Button mSubtract;
    Double mCurrVal;
    Button mSummary;
    Button mBack;
    ProgressBar mBar;
    DatabaseReference mDateRef;
    DatabaseReference mTimeRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        mUID = getIntent().getExtras().getString("uid");
        mName = getIntent().getExtras().getString("name");
        mGreeting = (TextView) findViewById(R.id.greeting);
        mRef = FirebaseDatabase.getInstance().getReference("users").child(mName).child("balance");
        mDateRef = FirebaseDatabase.getInstance().getReference("users").child(mName).child("date");
        mTimeRef = FirebaseDatabase.getInstance().getReference("users").child(mName).child("time");
        System.out.println("assigned");
        mAuth = FirebaseAuth.getInstance();
        mBalanceView = (TextView) findViewById(R.id.balance);
        mValue = (EditText) findViewById(R.id.value);
        mAdd = (Button) findViewById(R.id.add);
        mSubtract = (Button) findViewById(R.id.subtract);
        mSummary = (Button) findViewById(R.id.summary);
        mBack = (Button) findViewById(R.id.back);
        mBar = (ProgressBar) findViewById(R.id.pbar);

        mBar.setVisibility(View.VISIBLE);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        mValListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCurrVal = Double.parseDouble(dataSnapshot.getValue().toString());
                mBar.setVisibility(View.INVISIBLE);
                mBalanceView.setText("Balance: " + dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        /*mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    System.out.println("Name: " + user.getDisplayName());
                    mGreeting.setText("Hello, " + user.getDisplayName() + ". Your UID is: " + user.getUid());
                    mName = user.getUid();
                }
            }
        };*/

        mGreeting.setText("Greetings, " + mName);

        mSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSummary = new Intent(home.this, summary.class);
                startActivity(toSummary);
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        //mAuth.addAuthStateListener(mListener);
    }

    @Override
    public void onResume(){
        super.onResume();
        mRef.addValueEventListener(mValListener);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mRef.setValue(mCurrVal + Double.parseDouble(mValue.getText().toString()));
                    SimpleDateFormat dfmt = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = new Date();
                    String currDate = dfmt.format(date);
                    mDateRef.setValue(currDate);
                    SimpleDateFormat hrfmt = new SimpleDateFormat("HH:mm");
                    Date hr = new Date();
                    String currTime = hrfmt.format(hr);
                    mTimeRef.setValue(currTime);

                } catch(NumberFormatException e){
                    if(mValue.getText().toString().matches("")){
                        Toast.makeText(home.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(home.this, "Invalid number", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        mSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mRef.setValue(mCurrVal - Double.parseDouble(mValue.getText().toString()));
                    SimpleDateFormat dfmt = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = new Date();
                    String currDate = dfmt.format(date);
                    mDateRef.setValue(currDate);
                    SimpleDateFormat hrfmt = new SimpleDateFormat("HH:mm");
                    Date hr = new Date();
                    String currTime = hrfmt.format(hr);
                    mTimeRef.setValue(currTime);
                } catch(NumberFormatException e){
                    if(mValue.getText().toString().matches("")){
                        Toast.makeText(home.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(home.this, "Invalid number", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mListener != null){
            mAuth.removeAuthStateListener(mListener);
        }
    }
}
