package com.myApplication.blackjack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_sign_up extends AppCompatActivity {
    private EditText etName, etEmail, etPassword;
    private Button btnSignUp;
    private TextView tvLogin;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Find elements by ID
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnSignUp = findViewById(R.id.btn_sign_up);
        tvLogin = findViewById(R.id.tv_login);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference();

                // Get user input
                String username = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                double balance = 100;

                // Input validation
                if (username.isEmpty()) {
                    etName.setError("Username is required");
                    etName.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    etEmail.setError("Email is required");
                    etEmail.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Enter a valid email");
                    etEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    etPassword.setError("Password is required");
                    etPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    etPassword.setError("Password must be at least 6 characters");
                    etPassword.requestFocus();
                    return;
                }

                // Save data to Firebase
                HelperClass helperClass = new HelperClass(username, email, password, balance);
                reference.child(username).setValue(helperClass)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Signed up successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(activity_sign_up.this, OpeningActivity.class);
                                startActivity(intent);
                                finish(); // Close the sign-up activity
                            } else {
                                Toast.makeText(getApplicationContext(), "Sign-up failed. Try again.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

            }
        });

        tvLogin.setOnClickListener(view -> {
            // Open Login Activity
            Intent intent = new Intent(activity_sign_up.this, activity_login.class);
            startActivity(intent);
            finish(); // Close the sign-up activity
        });

    }
}
