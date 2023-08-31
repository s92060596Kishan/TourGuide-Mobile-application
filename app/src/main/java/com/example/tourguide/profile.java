package com.example.tourguide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity {

    private TextView occupationTextView, nameTextView, emailTextView, phoneTextView;
    private CircleImageView userImageView;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //logout Function
        View logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // Animate logout and navigate to login
                Animation animation = AnimationUtils.loadAnimation(profile.this, R.anim.fade_out);
                logoutButton.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {
                        // Do nothing

                    }
            @Override
                public void onAnimationEnd(Animation animation) {
                    Intent intent = new Intent(profile.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); startActivity(intent);
                    // Display a message to confirm user logged out successfully
                    Toast.makeText(profile.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
                }
                @Override public void onAnimationRepeat(Animation animation) {
                    // Do nothing
                    }
            });
        }
    });


        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Get the currently logged-in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();

        // Get a reference to the user node in the database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);


        occupationTextView = findViewById(R.id.occupation_textview);
        nameTextView = findViewById(R.id.name_textview);
        emailTextView = findViewById(R.id.email_textview);
        phoneTextView = findViewById(R.id.phone_textview);
        userImageView = findViewById(R.id.user_imageview);

        // Read the user data from the database
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        String fullName = user.getFullName();
                        String email = user.getEmail();
                        String mobile = user.getPhone();
                        String username = user.getUsername();

                        nameTextView.setText(fullName);
                        emailTextView.setText(email);
                        occupationTextView.setText(username);
                        phoneTextView.setText(mobile);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("ProfileActivity", "Failed to read user data.", error.toException());
            }
        });

    }
}
