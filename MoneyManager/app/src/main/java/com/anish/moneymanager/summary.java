package com.anish.moneymanager;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class summary extends Activity {

    DatabaseReference mRef;
    int mCount = 0;
    LinearLayout mLayout;
    ProgressBar mBar;
    Button mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_summary);
        mRef = FirebaseDatabase.getInstance().getReference("users");

        mLayout = (LinearLayout) findViewById(R.id.linlayout);
        mBar = (ProgressBar) findViewById(R.id.pbar);
        mBack = (Button) findViewById(R.id.back);
        mBar.setVisibility(View.VISIBLE);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    String name = snap.child("name").getValue().toString();
                    double val = Double.parseDouble(snap.child("balance").getValue().toString());
                    String date = "Most recent change happened at " + snap.child("time").getValue().toString() + " on " + snap.child("date").getValue().toString();
                    CardView currCard = (CardView) (mLayout.getChildAt(mCount));
                    TextView curr = (TextView) (currCard.getChildAt(0));
                    TextView dateInfo = (TextView) (currCard.getChildAt(1));
                    curr.setText(name+ ": " + val);
                    dateInfo.setText(date);
                    mCount++;
                }
                ViewGroup grp = (ViewGroup) findViewById(R.id.linlayout);
                for(int i = mCount; i<mLayout.getChildCount(); i++){
                    grp.addView(mLayout.getChildAt(i));
                }
                grp.removeAllViews();
                mBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }
}
