package com.example.tourguide;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
public class Signup extends AppCompatActivity {

    private EditText fullNameEditText, emailEditText, mobileEditText, usernameEditText, passwordEditText;
    private Button signupButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Set up the login button
        TextView loginTextView = findViewById(R.id.Login);
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(Signup.this, Login.class)); }
        });

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        fullNameEditText = findViewById(R.id.fullname);
        emailEditText = findViewById(R.id.email);
        mobileEditText = findViewById(R.id.phone);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        signupButton = findViewById(R.id.buttonSignUp);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void signup() {
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String mobile = mobileEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate user input
        if (TextUtils.isEmpty(fullName)) {
            Toast.makeText(getApplicationContext(), "Enter full name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Enter a valid email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mobile) || mobile.length() != 10) {
            Toast.makeText(getApplicationContext(), "Enter a valid 10-digit mobile number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Enter username!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            Toast.makeText(getApplicationContext(), "Password must be at least 6 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one symbol!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user with email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();

                            // Create a new User object with signup details
                            User user = new User(fullName, email, mobile, username);
                            user.setpassword(password); // Set the password

                            // Save the user details in the database
                            DBHelper.getInstance().saveUser(user);

                            startActivity(new Intent(Signup.this, Login.class));
                        } else {
                            // If sign up fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Registration failed! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isPasswordValid(String password) {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
        return password.matches(pattern);
    }
}
