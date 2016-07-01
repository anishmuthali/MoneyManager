package com.anish.moneymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends Activity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Intent toHome = new Intent(splash.this, home.class);
                    toHome.putExtra("uid", user.getUid());
                    toHome.putExtra("name", user.getDisplayName());
                    startActivity(toHome);
                }
                else{
                    Intent toLogin = new Intent(splash.this, Login.class);
                    startActivity(toLogin);
                }
            }
        };

    }

    @Override
    public void onStart(){
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth.addAuthStateListener(mListener);
            }
        }, 3000);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mListener!=null){
            mAuth.removeAuthStateListener(mListener);
        }
    }
}
