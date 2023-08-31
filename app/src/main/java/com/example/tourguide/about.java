package com.example.tourguide;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class about extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    VideoView videoView;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Check if the activity's configuration is null before accessing it
        if (getResources().getConfiguration() != null) {
            // Access the activity's configuration
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // do something for landscape orientation
            } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                // do something for portrait orientation
            }
        }

        //animation
        TextView textView = findViewById(R.id.textView2);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_animation);
        textView.startAnimation(animation);

        //navigation items
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.navigation_home:
                    startActivity(new Intent(about.this, home.class));
                    break;
                case R.id.navigation_map:
                    startActivity(new Intent(about.this, Map.class));
                    break;
                case R.id.navigation_about:
                    startActivity(new Intent(about.this, about.class));
                    break;
                case R.id.navigation_profile:
                    startActivity(new Intent(about.this, profile.class));
                    break;
            }
            // Uncheck the previously selected item
            bottomNavigationView.getMenu().findItem(itemId).setChecked(true);
            return true;
        });
        //Media Player
        videoView = findViewById(R.id.video);
        mediaController = new MediaController(this);
        // Attach the MediaController to the VideoView
        videoView.setMediaController(mediaController);
        videoView.setVideoPath("android.resource://" + getPackageName()
                + "/" + R.raw.tour);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // Set the anchor view for the MediaController
                mediaController.setAnchorView(videoView);
                // Show the MediaController as an overlay view
                mediaController.show();
            }
        });
        videoView.start();

    }
}
