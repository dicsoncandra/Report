package com.example.lazykid.report;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * Created by Lazykid on 30/6/17.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText rEmail, rPassword, rePassword, rName;
    private Button bRegister;

    String sE, sP, sRP, name, email, password, repassword, staffEmail;

    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabaseUser;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rEmail = (EditText) findViewById(R.id.rEmail);
        rPassword = (EditText) findViewById(R.id.rPassword);
        rePassword = (EditText) findViewById(R.id.rePassword);
        rName = (EditText) findViewById(R.id.rName);
        bRegister = (Button) findViewById(R.id.bRegister);
        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");

        bRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startRegister() {
        sP = rPassword.getText().toString();
        sRP = rePassword.getText().toString();
        sE = rEmail.getText().toString();
        name = rName.getText().toString().trim();
        email = rEmail.getText().toString().trim();
        staffEmail = email + "@cei.com.sg";
        password = rPassword.getText().toString().trim();
        repassword = rePassword.getText().toString().trim();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)){
            Toast.makeText(getApplicationContext(),"Please fill in all fields", Toast.LENGTH_LONG).show();

        } else if (Objects.equals(sP, sRP)) {
            if (sP.length()>=6 && sRP.length()>=6) {

                mProgress.setMessage("Signing up...");
                mProgress.show();

                mAuth.createUserWithEmailAndPassword(staffEmail, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createNewUser(task.getResult().getUser());

                            mProgress.dismiss();
                            finish();

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);

                        } else {
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(),"Error: please enter a valid email address", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(),"Password must be at least 6 characters", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "The 2 passwords don't match", Toast.LENGTH_LONG).show();
        }
    }

    private void createNewUser(FirebaseUser user) {
        String userId = user.getUid();

        mDatabaseUser.child(userId).child("name").setValue(name);
    }
}
