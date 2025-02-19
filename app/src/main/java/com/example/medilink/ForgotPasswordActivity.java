package com.example.medilink;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private Button btnResetPassword;
    private TextInputEditText emailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnBack = findViewById(R.id.btnBack);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        emailInput = findViewById(R.id.emailInput);

        btnBack.setOnClickListener(v -> finish());

        btnResetPassword.setOnClickListener(v -> {
            if (validateInput()) {
                // Add your password reset logic here
                Toast.makeText(ForgotPasswordActivity.this, 
                    "Password reset instructions sent to your email", 
                    Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private boolean validateInput() {
        String email = emailInput.getText().toString().trim();

        if (email.isEmpty()) {
            emailInput.setError("Email is required");
            return false;
        }

        return true;
    }
} 