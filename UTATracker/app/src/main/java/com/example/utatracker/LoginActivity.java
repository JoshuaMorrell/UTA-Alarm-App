package com.example.utatracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private EditText emailInput, passwordInput;
    private Button loginBtn;
    private TextView registerBtn;
    private TextView forgotPasswordBtn;
    private ImageButton googleLoginBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeUI();

        mAuth = FirebaseAuth.getInstance();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(false, v);
            }
        });
        googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(true, v);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                loginFailed("Google sign in failed, please try again.");
            }
        }
    }

    private void loginUser(boolean GoogleUser, View v) {
        progressBar.setVisibility(View.VISIBLE);
        loginBtn.setEnabled(false);
        googleLoginBtn.setEnabled(false);
        registerBtn.setEnabled(false);
        forgotPasswordBtn.setEnabled(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        v.requestFocus();

        if (GoogleUser) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if(account != null){
                firebaseAuthWithGoogle(account);
            }
            else {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        }
        else {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (TextUtils.isEmpty(email)) {
                loginFailed("Please enter your email");
                return;
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                loginFailed("Enter a valid email address");
                return;
            }
            else if (TextUtils.isEmpty(password)) {
                loginFailed("Please enter your password!");
                return;
            }
            else {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginSuccess();
                        }
                        else {
                            loginFailed("Sign in failed, please try again.");
                        }
                        }
                    });
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        try {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginSuccess();
                        } else {
                            loginFailed("Authentication Failed.");
                        }
                    }
                });
        }
        catch (Exception e) {
            loginFailed(e.getMessage());
        }
    }

    private void loginSuccess(){
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }

    private void loginFailed(String toastMessage){
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
        loginBtn.setEnabled(true);
        googleLoginBtn.setEnabled(true);
        registerBtn.setEnabled(true);
        forgotPasswordBtn.setEnabled(true);
    }

    private void initializeUI() {
        emailInput = findViewById(R.id.loginEmailInput);
        passwordInput = findViewById(R.id.loginPasswordInput);
        loginBtn = findViewById(R.id.loginLoginBtn);
        progressBar = findViewById(R.id.loginProgressBar);
        googleLoginBtn = findViewById(R.id.loginGoogleSignInBtn);
        registerBtn = findViewById(R.id.loginRegisterUserText);
        forgotPasswordBtn = findViewById(R.id.loginForgotPasswordText);

        SpannableString content = new SpannableString("Create an Account");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        registerBtn.setText(content);

        SpannableString content1 = new SpannableString("Forgot your password?");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        forgotPasswordBtn.setText(content1);

        progressBar.setVisibility(View.INVISIBLE);
    }
}