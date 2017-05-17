package com.example.luigi.travelapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail;
    EditText editPassword;
    Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        buttonConfirm = (Button)findViewById(R.id.buttonConfirm);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editEmail.getText().toString();
                final String pass = editPassword.getText().toString();

                if (!name.isEmpty() && !pass.isEmpty()) {
                    final FirebaseAuth mAuth = FirebaseAuth.getInstance();

                    mAuth.signInWithEmailAndPassword(name, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (mAuth.getCurrentUser() != null) {
                                            // mi sono autenticato
                                            Toast.makeText(getApplicationContext(), "AUTENTICATO", Toast.LENGTH_SHORT).show();
                                            // torna alla main activity
                                        } else {
                                            createAccount(name, pass);
                                            //Toast.makeText(getApplicationContext(), "NON TI CONOSCO", Toast.LENGTH_SHORT).show();
                                            // devo visualizzare errore
                                        }
                                    } else {
                                        createAccount(name, pass);
                                        //Toast.makeText(getApplicationContext(), "NON TI CONOSCO", Toast.LENGTH_SHORT).show();
                                        // devo visualizzare errore
                                    }
                                }
                            });
                }
            }
        });
    }

    void createAccount(String name, String pass) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(name, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        /*if (!task.isSuccessful()) {
                            Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }*/

                        // ...
                    }
                });
    }
}
