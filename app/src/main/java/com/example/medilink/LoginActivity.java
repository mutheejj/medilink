package com.example.medilink;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.medilink.services.AuthService;
import com.example.medilink.activities.HomeActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private TextView forgotPasswordLink;
    private Button loginButton;
    private AuthService authService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        authService = AuthService.getInstance();
        
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
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        
        if (validateInput(email, password)) {
            // Show loading state
            loginButton.setEnabled(false);
            loginButton.setText("Signing in...");
            
            authService.loginStaff(email, password, new AuthService.AuthCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        // Clear activity stack and start HomeActivity
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish(); // Finish current activity
                    });
                }
                
                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        loginButton.setEnabled(true);
                        loginButton.setText("Sign In");
                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                    });
                }
                
                @Override
                public void onLoading(boolean isLoading) {
                    runOnUiThread(() -> {
                        loginButton.setEnabled(!isLoading);
                        loginButton.setText(isLoading ? "Signing in..." : "Sign In");
                    });
                }

                @Override
                public void onEmailVerificationRequired() {
                    // Ignore email verification, proceed with login
                    onSuccess();
                }
            });
        }
    }
    
    private boolean validateInput(String email, String password) {
        boolean isValid = true;
        
        if (email.isEmpty()) {
            emailInput.setError("Please enter your email");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email address");
            isValid = false;
        }
        
        if (password.isEmpty()) {
            passwordInput.setError("Please enter your password");
            isValid = false;
        }
        
        return isValid;
    }
}