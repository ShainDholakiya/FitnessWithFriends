package com.example.fitnesswithfriends;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateWorkoutActivity extends MapActivity  {

    private GoogleMap map;
    private static String TAG = "initializing CreateWorkoutActivity";
    private EditText mWorkoutName;
    private EditText mLocationText;
    private EditText mCreateWkText;


    //initializing firebase elements
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String WK_TITLE = "Workout Title";
    private String WK_DESCRIP = "Description:";
    private String WK_LOCATION = "Workout Location";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        //receives user input
        mWorkoutName = (EditText) findViewById(R.id.editTextWorkoutName);
        mCreateWkText = (EditText) findViewById(R.id.editTextDescription);
        mLocationText = (EditText) findViewById(R.id.editTextLocation);

        //bottom navigation bar
        initHomeButton();
        initMapButton();
        initLocationText();
        initCreateWorkoutButton();



    }


    private void initLocationText() {
        Log.d(TAG, "initializing location text");
        mLocationText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        ||keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute code for searching
                    geoLocate();
                }
                return false;
            }
        });
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String locationString = mLocationText.getText().toString();

        Geocoder geocoder = new Geocoder(CreateWorkoutActivity.this);
        List<Address> locationList = new ArrayList<>();
        try{
            locationList = geocoder.getFromLocationName(locationString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geolocate: IOException: " + e.getMessage()  );
        }
        if(locationList.size() > 0 ) {
            Address list = locationList.get(0);



            Log.d(TAG, "geoLocate: found a location: " + list.toString());

            //Attempt to invoke virtual method 'com.google.android.gms.maps.model.Marker com.google.android.gms.maps.GoogleMap.addMarker(com.google.android.gms.maps.model.MarkerOptions)' on a null object reference
//            map.addMarker(new MarkerOptions()
//                    .title("TESTING")
//                    .position(new LatLng(list.getLatitude(),list.getLongitude())));
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                    new LatLng(list.getLatitude(),
//                            list.getLongitude()),15));
            }


//            super.getDeviceLocation() {
//             listLoca
//
//            }
//            super.Ca
//            super.moveCamera(CameraUpdateFactory.newLatLng(list.getLatitude(),list.getLongitude()), 15);
//            LatLng latLng = null;
//            String title = null;
//            MarkerOptions workouts = new MarkerOptions()
//                    .position(latLng)
//                    .title(title);
//

    }

    private void initHomeButton() {
        ImageButton ibList = findViewById(R.id.navHome);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CreateWorkoutActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initMapButton() {
        ImageButton ibList = findViewById(R.id.navMap);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CreateWorkoutActivity.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initCreateWorkoutButton() {
        ImageButton ibList = findViewById(R.id.navCreateWorkout);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CreateWorkoutActivity.this, CreateWorkoutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void createWorkout(View view) {
        String title = mWorkoutName.getText().toString();
        String location = mLocationText.getText().toString();

        Map<String, Object> allWorkouts = new HashMap<>();
        allWorkouts.put(WK_TITLE, title);
        allWorkouts.put(WK_LOCATION, location);

        db.collection("Workout Notebook").document("added workout").set(allWorkouts)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateWorkoutActivity.this, "Workout saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateWorkoutActivity.this, "ERROR Workout not saved", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });
    }


}