package com.example.lazykid.report;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private Button bLogin;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private TextView tvRegister, tvResend;
    private FirebaseOptions options;
    private DatabaseReference mDatabaseUser;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvResend = (TextView) findViewById(R.id.tvResend);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUser.keepSynced(true);

        if(mAuth.getCurrentUser() != null) {
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
        tvRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

            }
        });

        tvResend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResendActivity.class));

            }
        });

        bLogin.setOnClickListener(this);

    }
    private void userLogin() {
        email =  etEmail.getText().toString().trim();
        password =  etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password) && TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    SharedPreferences sp=getSharedPreferences("Login", 0);
                    SharedPreferences.Editor Ed=sp.edit();
                    Ed.putString("Psw", password);
                    Ed.apply();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);

                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Please check your email and password", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == bLogin) {
            userLogin();

        }

    }
}
