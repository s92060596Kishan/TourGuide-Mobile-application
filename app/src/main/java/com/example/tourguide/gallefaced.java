package com.example.tourguide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class gallefaced extends AppCompatActivity {

    private String[] imageUrls = {
            "https://firebasestorage.googleapis.com/v0/b/tourguide-57cff.appspot.com/o/Colombo-Blog.jpg?alt=media&token=341cbe69-364b-4f24-85be-af07116ba8d2",
            "https://firebasestorage.googleapis.com/v0/b/tourguide-57cff.appspot.com/o/Colombo_-_Galle_Face.jpg?alt=media&token=b781ad75-6a97-47a9-b186-8574986c37de",
            "https://firebasestorage.googleapis.com/v0/b/tourguide-57cff.appspot.com/o/Untitled.jpg?alt=media&token=6bc58b0c-e0c7-45f0-802b-ba1a133cb55a"
    };
    private int currentPosition = 0;
    private ImageView imageView;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallefaced);

        Context context = this; // Save the context in a variable

        // Initialize Firebase Storage
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance();

        imageView = findViewById(R.id.imageView);
        Button directionButton = findViewById(R.id.directionButton);

        // Set initial image
        loadImage(imageUrls[currentPosition]);

        // Set swipe listeners
        imageView.setOnTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeRight() {
                if (currentPosition > 0) {
                    currentPosition--;
                    loadImage(imageUrls[currentPosition]);
                }
            }

            @Override
            public void onSwipeLeft() {
                if (currentPosition < imageUrls.length - 1) {
                    currentPosition++;
                    loadImage(imageUrls[currentPosition]);
                }
            }
        });

        // Set direction button click listener
        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle map direction button click
                Toast.makeText(gallefaced.this, "Get Directions clicked!", Toast.LENGTH_SHORT).show();
                // Add your code to launch map and provide directions
                startActivity(new Intent(gallefaced.this, galleface.class));
            }
        });

        // Set details about Galle Face
        TextView detailsTextView = findViewById(R.id.detailsTextView);
        detailsTextView.setText(R.string.galle_face_details);
    }

    private void loadImage(String imageUrl) {
        Picasso.get().load(imageUrl).into(imageView);
    }

    // Custom swipe touch listener class
    public abstract static class OnSwipeTouchListener implements View.OnTouchListener {

        private static final int MIN_DISTANCE = 100;
        private float downX;
        private float downY;
        private long downTime;
        private Context context; // Add a context field

        public OnSwipeTouchListener(Context context) {
            this.context = context;
        }

        public abstract void onSwipeRight();

        public abstract void onSwipeLeft();

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    downTime = System.currentTimeMillis();
                    return true;
                case MotionEvent.ACTION_UP:
                    float upX = event.getX();
                    float upY = event.getY();
                    float deltaX = downX - upX;
                    float deltaY = downY - upY;
                    long deltaTime = System.currentTimeMillis() - downTime;

                    if (Math.abs(deltaX) > MIN_DISTANCE && Math.abs(deltaY) < MIN_DISTANCE && deltaTime < 200) {
                        if (deltaX > 0) {
                            onSwipeLeft();
                        } else {
                            onSwipeRight();
                        }
                    }

                    return true;
            }
            return false;
        }
    }
}
