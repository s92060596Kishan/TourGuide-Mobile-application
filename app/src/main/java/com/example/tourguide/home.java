package com.example.tourguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationBarView;
public class home extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private GridLayout provincesGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.navigation);

        provincesGrid = findViewById(R.id.provinces_grid);


        ImageView westernProvinceImage = findViewById(R.id.western_province_image);
        TextView westernProvinceText = findViewById(R.id.western_province_text);
        ImageView easternProvinceImage = findViewById(R.id.eastern_province_image);
        TextView easternProvinceText = findViewById(R.id.eastern_province_text);
        ImageView northernProvinceImage = findViewById(R.id.northern_province_image);
        TextView northernProvinceText = findViewById(R.id.northern_province_text);
        ImageView southernProvinceImage = findViewById(R.id.southern_province_image);
        TextView southernProvinceText = findViewById(R.id.southern_province_text);
        ImageView centralProvinceImage = findViewById(R.id.central_province_image);
        TextView centralProvinceText = findViewById(R.id.central_province_text);
        ImageView northcentralProvinceImage = findViewById(R.id.northcentral_province_image);
        TextView northcentralProvinceText = findViewById(R.id.northcentral_province_text);
        ImageView northwesternProvinceImage = findViewById(R.id.northwestern_province_image);
        TextView northwesternProvinceText = findViewById(R.id.northwestern_province_text);
        ImageView sabaragamuwaProvinceImage = findViewById(R.id.sabaragamuwa_province_image);
        TextView sabaragamuwaProvinceText = findViewById(R.id.sabaragamuwa_province_text);
        ImageView uvaProvinceImage = findViewById(R.id.uva_province_image);
        TextView uvaProvinceText = findViewById(R.id.uva_province_text);

        // Set the images and texts for each province
        westernProvinceImage.setImageResource(R.drawable.western_province_image);
        westernProvinceText.setText("Western Province");
        easternProvinceImage.setImageResource(R.drawable.eastern_province_image);
        easternProvinceText.setText("Eastern Province");
        northernProvinceImage.setImageResource(R.drawable.northern_province_image);
        northernProvinceText.setText("Northern Province");
        southernProvinceImage.setImageResource(R.drawable.southern_province_image);
        southernProvinceText.setText("Southern Province");
        centralProvinceImage.setImageResource(R.drawable.central_province_image);
        centralProvinceText.setText("Central Province");
        northcentralProvinceImage.setImageResource(R.drawable.northcentral_province_image);
        northcentralProvinceText.setText("North Central Province");
        northwesternProvinceImage.setImageResource(R.drawable.northwestern_province_image);
        northwesternProvinceText.setText("North Western Province");
        sabaragamuwaProvinceImage.setImageResource(R.drawable.sabaragamuwa_province_image);
        sabaragamuwaProvinceText.setText("Sabaragamuwa Province");
        uvaProvinceImage.setImageResource(R.drawable.uva_province_image);
        uvaProvinceText.setText("Uva Province");

        // Set click listeners for province texts
        westernProvinceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(home.this, western.class));;
            }
        });

        easternProvinceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(home.this, western.class));};
        });

        northernProvinceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(home.this, western.class));}
        });

        southernProvinceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(home.this, western.class));}
        });

        centralProvinceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(home.this, western.class));}
        });

        northcentralProvinceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(home.this, western.class));}
        });

        northwesternProvinceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(home.this, western.class));}
        });

        sabaragamuwaProvinceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(home.this, western.class));}
        });

        uvaProvinceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(new Intent(home.this, western.class));}
        });

        //navigation items
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.navigation_home:
                    startActivity(new Intent(home.this, home.class));
                    break;
                case R.id.navigation_map:
                    startActivity(new Intent(home.this, Map.class));
                    break;
                case R.id.navigation_about:
                    startActivity(new Intent(home.this, about.class));
                    break;
                case R.id.navigation_profile:
                    startActivity(new Intent(home.this, profile.class));
                    break;
            }
            // Uncheck the previously selected item
            bottomNavigationView.getMenu().findItem(itemId).setChecked(true);
            return true;
        });
    }

}
