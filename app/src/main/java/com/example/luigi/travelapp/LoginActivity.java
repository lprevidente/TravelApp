package com.example.luigi.travelapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private Button buttonConfirm;
    private ProgressBar progressCircle;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        buttonConfirm = (Button)findViewById(R.id.buttonConfirm);
        progressCircle = (ProgressBar)findViewById(R.id.progressCircle);

        mAuth = FirebaseAuth.getInstance();

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editEmail.getText().toString();
                final String pass = editPassword.getText().toString();

                if (email.isEmpty())
                    editEmail.setError(getString(R.string.campoObbligatorio));
                else if (pass.isEmpty())
                    editPassword.setError(getString(R.string.campoObbligatorio));
                else {
                    progressCircle.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (mAuth.getCurrentUser() != null) {
                                            Intent intent = new Intent(LoginActivity.this, TripListActivity.class);
                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(getApplicationContext(), "AUTENTICATO", Toast.LENGTH_SHORT).show();
                                        } else {
                                            createAccount(email, pass);
                                        }
                                    } else {
                                        createAccount(email, pass);
                                    }
                                }
                            });
                }
            }
        });
    }

    private void createAccount(String email, String pass) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent intent = new Intent(getApplicationContext(), TripListActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
