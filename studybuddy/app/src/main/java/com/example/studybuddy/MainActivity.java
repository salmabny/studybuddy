package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser la base de données
        dbHelper = new DatabaseHelper(this);

        initializeViews();
        setupClickListeners();

        // Vérifier si un email vient de l'inscription
        checkRegisteredEmail();
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void checkRegisteredEmail() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("registered_email")) {
            String email = intent.getStringExtra("registered_email");
            etEmail.setText(email);
            etPassword.requestFocus();
        }
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (validateInputs(email, password)) {
            performLogin(email, password);
        }
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return false;
        }

        if (!isValidEmail(email)) {
            etEmail.setError("Invalid email format");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void performLogin(String email, String password) {
        // Vérifier dans la base de données
        boolean userExists = dbHelper.checkUser(email, password);

        if (userExists) {
            // Récupérer les informations de l'utilisateur
            User user = dbHelper.getUserByEmail(email);
            String welcomeMessage = "Welcome back, " + user.getFullName() + "!";
            Toast.makeText(this, welcomeMessage, Toast.LENGTH_SHORT).show();

            // ICI VOUS POUVEZ REDIRIGER VERS LE DASHBOARD PLUS TARD
        } else {
            // Vérifier aussi le compte test pour compatibilité
            if (email.equals("test@studybuddy.com") && password.equals("123456")) {
                Toast.makeText(this, "Welcome back, Test User!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Email or password incorrect", Toast.LENGTH_SHORT).show();
            }
        }
    }
}