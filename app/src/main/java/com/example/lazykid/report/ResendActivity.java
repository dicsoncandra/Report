package com.example.lazykid.report;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ResendActivity extends AppCompatActivity {

    private EditText reEmail;

    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resend);

        reEmail = (EditText) findViewById(R.id.reEmail);
        Button bReset = (Button) findViewById(R.id.bReset);

        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = reEmail.getText().toString().trim();

                mProgress.setMessage("Sending password reset email...");
                mProgress.show();

                mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(),"Email sent", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(intent);

                        } else {
                            mProgress.dismiss();
                            Toast.makeText(getApplicationContext(),"Error: email address not registered", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });
    }
}
