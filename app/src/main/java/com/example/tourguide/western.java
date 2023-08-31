package com.example.tourguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class western extends AppCompatActivity {

    private ListView places;

    private String[] place = {
            "Galleface Beach",
            "Viharamahadevi Park",
            "Dutch Fort",
            "Bentota Beach",
            "Beruwala Lighthouse",
            "Gem museums ",
            "Dehiwala Zoo",
            "Henarathgoda Botanical Garden ",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.western);

        places = findViewById(R.id.placeListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                place
        );

        places.setAdapter(adapter);

        places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedPlace = place[position];
                Toast.makeText(western.this, "Selected: " + selectedPlace, Toast.LENGTH_SHORT).show();
                navigateToselectedPlaceActivity(selectedPlace);
            }
        });
    }

    private void navigateToselectedPlaceActivity(String selectedPlace) {
        Class<?> place = null;
        switch (selectedPlace) {
            case "Galleface Beach":
                place = gallefaced.class;
                break;
            case "Viharamahadevi Park":
                place = galleface.class;
                break;
            case "Dutch Fort":
                place = galleface.class;
                break;
            case "Bentota Beach":
                place = galleface.class;
                break;
            case "Beruwala Lighthouse":
                place = galleface.class;
                break;
            case "Gem museums":
                place = galleface.class;
                break;
            case "Henarathgoda Botanical Garden":
                place = galleface.class;
                break;
            case "Dehiwala Zoo":
                place =galleface.class;
        }

        if (place != null) {
            Intent intent = new Intent(western.this, place);
            startActivity(intent);
        }
    }
}
