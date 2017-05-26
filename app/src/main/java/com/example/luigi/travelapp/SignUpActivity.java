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

public class SignUpActivity extends Activity {

    private TextView welcomeText;
    private TextView goToLoginText;
    private EditText editEmail;
    private EditText editPassword;
    private EditText repeatPassword;
    private Button buttonConfirm;
    private ProgressBar progressCircle;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        createAuthStateListener();

        Bundle extras = getIntent().getExtras();
        boolean firstLaunch = extras.getBoolean(FIRSTLAUNCH);

        editEmail = (EditText) findViewById(R.id.editEmailS);
        editPassword = (EditText) findViewById(R.id.editPasswordS);
        repeatPassword = (EditText) findViewById(R.id.editRepeatPassword);
        buttonConfirm = (Button) findViewById(R.id.buttonConfirmS);
        progressCircle = (ProgressBar) findViewById(R.id.progressCircleS);

        welcomeText = (TextView) findViewById(R.id.welcomeTextS);
        welcomeText.setText(firstLaunch ? R.string.firstLaunchString : R.string.rememberToRegisterString);

        goToLoginText = (TextView)findViewById(R.id.textAlreadyRegistered);
        goToLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.putExtra(FIRSTLAUNCH, true);
                startActivity(intent);
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editEmail.getText().toString();
                final String pass = editPassword.getText().toString();
                final String repeatpass = repeatPassword.getText().toString();

                if (email.isEmpty())
                    editEmail.setError(getString(R.string.campoObbligatorio));
                else if (pass.isEmpty())
                    editPassword.setError(getString(R.string.campoObbligatorio));
                else if (pass.length() < 6)
                    editPassword.setError(getString(R.string.erroreLunghezzaPass));
                else if (repeatpass.isEmpty())
                    repeatPassword.setError(getString(R.string.campoObbligatorio));
                else if (!repeatpass.equals(pass))
                    repeatPassword.setError(getString(R.string.passwordMismatch));
                else {
                    progressCircle.setVisibility(View.VISIBLE);
                    createAccount(email, pass);
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
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                user.sendEmailVerification();
                                Toast.makeText(getApplicationContext(), "Ti è stata mandata una e-mail di conferma!", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(SignUpActivity.this, TripListActivity.class);
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