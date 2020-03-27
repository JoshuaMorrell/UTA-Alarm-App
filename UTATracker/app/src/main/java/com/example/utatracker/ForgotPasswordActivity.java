package com.example.utatracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ForgotPasswordActivity extends Activity {

    ProgressBar progressBar;
    Button cancel, submit;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initializeUI();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            cancel.setEnabled(false);
            submit.setEnabled(false);
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            v.requestFocus();

            String emailText = email.getText().toString();

            if (TextUtils.isEmpty(emailText)) {
                failedRegistration("Enter your email");
                return;
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                failedRegistration("Enter a valid email");
                return;
            }
            else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailText).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Password reset email has been sent", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Failed to send password reset email", Toast.LENGTH_LONG).show();
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
        submit.setEnabled(true);
    }

    private void initializeUI(){
        progressBar = findViewById(R.id.forgotPasswordProgressBar);
        cancel = findViewById(R.id.forgotPasswordCancelBtn);
        submit = findViewById(R.id.forgotPasswordSubmitBtn);
        email = findViewById(R.id.forgotPasswordEmail);
    }
}
