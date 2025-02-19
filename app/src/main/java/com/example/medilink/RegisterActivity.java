package com.example.medilink;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    private EditText fullNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button btnRegister;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        fullNameInput = findViewById(R.id.fullNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        btnRegister = findViewById(R.id.btnRegister);
        
        btnRegister.setOnClickListener(v -> handleRegisterClick());
    }
    
    private void handleRegisterClick() {
        String fullName = fullNameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();
        
        if (validateInput(fullName, email, password, confirmPassword)) {
            // Implement your registration logic here
        }
    }
    
    private boolean validateInput(String fullName, String email, String password, String confirmPassword) {
        // Implement your validation logic here
        return !fullName.isEmpty() && !email.isEmpty() && 
               !password.isEmpty() && password.equals(confirmPassword);
    }
} 