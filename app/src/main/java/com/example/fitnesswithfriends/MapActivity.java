package com.example.fitnesswithfriends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MapActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initHomeButton();
        initMapButton();
        initCreateWorkoutButton();
    }

    private void initHomeButton() {
        ImageButton ibList = findViewById(R.id.navHome);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    /**
     * Need to change MapsActivity.xml file to include the fragment. currently only shows the map.
     * could also add a corner settings button..but that would probably double the work to do
     *
     *
     */
    private void initMapButton() {
        ImageButton ibList = findViewById(R.id.navMap);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, MapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initCreateWorkoutButton() {
        ImageButton ibList = findViewById(R.id.navCreateWorkout);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, CreateWorkoutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}