package com.example.utatracker;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends Activity {

    ProgressBar progressBar;
    Button cancel, register;
    EditText email, password, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeUI();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                cancel.setEnabled(false);
                register.setEnabled(false);
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                v.requestFocus();

                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                String password2Text = password2.getText().toString();

                if (TextUtils.isEmpty(emailText)) {
                    failedRegistration("Enter your email address");
                    return;
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                    failedRegistration("Enter a valid email address");
                    return;
                }
                else if (TextUtils.isEmpty(passwordText)) {
                    failedRegistration("Enter a password");
                    return;
                }
                else if (TextUtils.isEmpty(password2Text)) {
                    failedRegistration("Confirm your password");
                    return;
                }
                else if (!TextUtils.equals(passwordText, password2Text)){
                    failedRegistration("Passwords do not match");
                    return;
                }
                else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            }
                            else {
                                failedRegistration("Registration failed, please try again.");
                                return;
                            }
                            }
                        });
                }
            }
        });

    }

    private void failedRegistration(String toastMessage){
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.INVISIBLE);
        cancel.setEnabled(true);
        register.setEnabled(true);
    }

    private void initializeUI(){
        progressBar = findViewById(R.id.registerProgressBar);
        cancel = findViewById(R.id.registerCancelBtn);
        register = findViewById(R.id.registerRegisterBtn);
        email = findViewById(R.id.registerEmailInput);
        password = findViewById(R.id.registerPasswordInput);
        password2 = findViewById(R.id.registerPassword2Input);
    }
}
