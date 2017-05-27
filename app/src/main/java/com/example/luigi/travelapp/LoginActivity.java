package com.example.luigi.travelapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.luigi.travelapp.costanti.Constants.FIRSTLAUNCH;

public class LoginActivity extends Activity {

    private TextView welcomeText;
    private TextView goToSignUpText;
    private EditText editEmail;
    private EditText editPassword;
    private Button buttonConfirm;
    private ProgressBar progressCircle;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        createAuthStateListener();

        Bundle extras = getIntent().getExtras();
        boolean firstLaunch = extras.getBoolean(FIRSTLAUNCH);

        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        buttonConfirm = (Button)findViewById(R.id.buttonConfirm);
        progressCircle = (ProgressBar)findViewById(R.id.progressCircle);

        welcomeText = (TextView)findViewById(R.id.welcomeText);
        welcomeText.setText(firstLaunch ? R.string.firstLaunchString : R.string.rememberToRegisterString);

        goToSignUpText = (TextView)findViewById(R.id.textNotRegistered);
        goToSignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                intent.putExtra(FIRSTLAUNCH, false);
                startActivity(intent);
            }
        });

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
                    signIn(email, pass);
                }
            }
        });
    }

    private void signIn(String email, String pass) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser() != null) {
                                Toast.makeText(getApplicationContext(), "Autenticato!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Qualcosa non è andata a buon fine...", Toast.LENGTH_SHORT).show();
                                progressCircle.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Qualcosa non è andata a buon fine...", Toast.LENGTH_SHORT).show();
                            progressCircle.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void createAuthStateListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, TripListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
