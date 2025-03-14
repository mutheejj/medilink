package com.example.medilink;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.medilink.services.AuthService;
import com.example.medilink.activities.HomeActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText fullNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button btnRegister;
    private AuthService authService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        authService = AuthService.getInstance();
        
        fullNameInput = findViewById(R.id.fullNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        btnRegister = findViewById(R.id.btnRegister);
        
        btnRegister.setOnClickListener(v -> handleRegisterClick());
    }
    
    private void handleRegisterClick() {
        String fullName = fullNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        
        if (validateInput(fullName, email, password, confirmPassword)) {
            // Show loading state
            btnRegister.setEnabled(false);
            btnRegister.setText("Creating Account...");
            
            authService.registerStaff(email, password, fullName, "staff", new AuthService.AuthCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        Toast.makeText(RegisterActivity.this, 
                            "Registration successful! Please check your email for verification.", 
                            Toast.LENGTH_LONG).show();
                        // Return to login screen after registration
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    });
                }
                
                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        btnRegister.setEnabled(true);
                        btnRegister.setText("Create Account");
                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_LONG).show();
                    });
                }
            });
        }
    }
    
    private boolean validateInput(String fullName, String email, String password, String confirmPassword) {
        boolean isValid = true;
        
        if (fullName.isEmpty()) {
            fullNameInput.setError("Please enter your full name");
            isValid = false;
        } else if (fullName.length() < 3) {
            fullNameInput.setError("Name must be at least 3 characters");
            isValid = false;
        }
        
        if (email.isEmpty()) {
            emailInput.setError("Please enter your email");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email address");
            isValid = false;
        }
        
        if (password.isEmpty()) {
            passwordInput.setError("Please enter a password");
            isValid = false;
        } else if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            isValid = false;
        } else if (!password.matches(".*\\d.*")) {
            passwordInput.setError("Password must contain at least one number");
            isValid = false;
        }
        
        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Passwords do not match");
            isValid = false;
        }
        
        return isValid
        
        return true;
    }
}