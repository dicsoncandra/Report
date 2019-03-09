package com.example.lazykid.report;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ResetActivity extends AppCompatActivity {

    private EditText cPassword, nPassword, newPassword;
    private Button bUpdate;

    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private FirebaseUser user;

    private String cP, nP, newP, password, cPass, nPass, newPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        cPassword = (EditText) findViewById(R.id.cPassword);
        nPassword = (EditText) findViewById(R.id.nPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        bUpdate = (Button) findViewById(R.id.bUpdate);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mProgress = new ProgressDialog(this);

        SharedPreferences sp1=this.getSharedPreferences("Login", 0);
        password = sp1.getString("Psw", null);
        bUpdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                startReset();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startReset() {
        cP = cPassword.getText().toString();
        nP = nPassword.getText().toString();
        newP = newPassword.getText().toString();
        cPass = cP.trim();
        nPass = nP.trim();
        newPass = newP.trim();

        if(TextUtils.isEmpty(cPass) || TextUtils.isEmpty(nPass) || TextUtils.isEmpty(newPass)){
            Toast.makeText(getApplicationContext(),"Please fill in all fields", Toast.LENGTH_LONG).show();

        } else if (Objects.equals(nP, newP)) {
            if (nP.length()>=6 && newP.length()>=6 && cP.length()>=6) {

                if (!Objects.equals(nPass, password)) {

                    if(Objects.equals(cPass,password)) {
                        mProgress.setMessage("Updating password...");
                        mProgress.show();

                        user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mProgress.dismiss();
                                    Toast.makeText(getApplicationContext(),"Password updated successfully", Toast.LENGTH_SHORT).show();
                                    Intent resetIntent = new Intent(ResetActivity.this, MainActivity.class);
                                    resetIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(resetIntent);

                                } else {
                                    mProgress.dismiss();
                                    Toast.makeText(getApplicationContext(),"Please logout and try again", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(),"You entered an incorrect current password", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(),"The new password you entered is your current password", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(),"Password must be at least 6 characters", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "The 2 passwords don't match", Toast.LENGTH_LONG).show();
        }
    }
}
