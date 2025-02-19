package com.example.medilink;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private TextView forgotPasswordLink;
    private Button loginButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        loginButton = findViewById(R.id.loginButton);
        
        loginButton.setOnClickListener(v -> handleLoginClick());

        // Forgot password link click
        findViewById(R.id.forgotPasswordLink).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        // Create account link click
        findViewById(R.id.createAccountLink).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
    
    private void handleLoginClick() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        
        if (validateInput(email, password)) {
            // Implement your login logic here
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish(); // Close login activity so user can't go back
        }
    }
    
    private boolean validateInput(String email, String password) {
        // Implement your validation logic here
        return !email.isEmpty() && !password.isEmpty();
    }
} 