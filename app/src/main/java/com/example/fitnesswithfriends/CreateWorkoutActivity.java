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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateWorkoutActivity extends AppCompatActivity {


    private static final String TAG = "Location text working??";
    //widgets
    private EditText mLocationText;
    private EditText mWkText; //editable workout title text



    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String Workout_Title = "Workout Title";
    private String Workout_Location = "Workout Location: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        mLocationText = (EditText) findViewById(R.id.input_location);
        mWkText = (EditText) findViewById(R.id.editTitleWk);
        initHomeButton();
        initMapButton();
        initCreateWorkoutButton();
        initLocationText();
    }

    private void initUserWorkout() {
        // Create a new user with a first and last name
        Map<String, Object> userWorkout = new HashMap<>();
        userWorkout.put("first", "Ada");
        userWorkout.put("last", "Lovelace");
        userWorkout.put("born", 1815);

// Add a new document with a generated ID
        db.collection("users")
                .add(userWorkout)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
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

            //moveCamera(new LatLng(list.getLatitude(),list.getLongitude(), DEFAULT_ZOOM));
//            LatLng latLng = null;
//            String title = null;
//            MarkerOptions workouts = new MarkerOptions()
//                    .position(latLng)
//                    .title(title);


        }


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

    /**
     * change intent to go to MapsActivity.class. otherwise will need to click twice to see map
     *
     */
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
        String title = mWkText.getText().toString();
        String location = mLocationText.getText().toString();

        Map<String, Object> allWorkouts = new HashMap<>();
        allWorkouts.put(Workout_Title,title);
        allWorkouts.put(Workout_Location,location);

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