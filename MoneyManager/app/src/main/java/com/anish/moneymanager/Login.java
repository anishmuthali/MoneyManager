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

public class Login extends Activity {
    EditText mEmailBox;
    EditText mPasswordBox;
    Button mSignInButton;
    String mEmail;
    String mPassword;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mListener;
    ProgressBar pbar;
    boolean mMet;
    Button mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        System.out.println("Started");
        mEmailBox = (EditText) findViewById(R.id.emailbox);
        mPasswordBox = (EditText) findViewById(R.id.passwordbox);
        mSignInButton = (Button) findViewById(R.id.signin);
        pbar = (ProgressBar) findViewById(R.id.pbar);
        mAuth = FirebaseAuth.getInstance();
        mSignUp = (Button) findViewById(R.id.signup);

        pbar.setIndeterminate(true);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignup = new Intent(Login.this, signup.class);
                startActivity(toSignup);
            }
        });

        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user =firebaseAuth.getCurrentUser();
                System.out.println("got user");
                if(user != null){
                    /*Intent toHome = new Intent(Login.this, home.class);
                    toHome.putExtra("username", user.getDisplayName());
                    System.out.println("Going home 2");
                    startActivity(toHome);*/
                    /*UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName("Anish").build();
                    user.updateProfile(profileUpdates);*/

                    //do nothing???
                }
            }
        };
        System.out.println("Declared");

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                if(mEmailBox.getText().toString() == null){
                    pbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, "Please enter an email", Toast.LENGTH_SHORT).show();
                }
                else if(mPasswordBox.getText().toString()==null){
                    pbar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                }
                else{
                    mEmail = mEmailBox.getText().toString();
                    mPassword = mPasswordBox.getText().toString();
                    if((mEmail != null || !mEmail.matches("")) && (mPassword != null || !mPassword.matches(""))) {
                        mAuth.signInWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    System.out.println("task successful");
                                    mMet = true;
                                    Intent toHome = new Intent(Login.this, home.class);
                                    System.out.println("Going home");
                                    pbar.setVisibility(View.INVISIBLE);
                                    startActivity(toHome);
                                } else if (!task.isSuccessful()) {
                                    pbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(Login.this, "One or more fields are not filled out", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });




    }
    @Override
    public void onStart(){
        super.onStart();
        //mAuth.addAuthStateListener(mListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mListener != null){
            //mAuth.removeAuthStateListener(mListener);
        }
    }
}
