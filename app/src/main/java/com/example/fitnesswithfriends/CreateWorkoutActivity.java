package com.example.fitnesswithfriends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateWorkoutActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    FirebaseUser user;
    String userID;
    Button createBtn;

    private EditText editTextWorkoutName;
    private EditText editTextDescription;
    private Spinner workoutTypeSpinner, fitLevelSpinner, workoutDurationSpinner;
    private EditText editTextLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        editTextWorkoutName = findViewById(R.id.editTextWorkoutName);
        editTextDescription = findViewById(R.id.editTextDescription);
        workoutTypeSpinner = findViewById(R.id.WorkoutTypeSpinner);
        fitLevelSpinner = findViewById(R.id.FitLevelSpinner);
        workoutDurationSpinner = findViewById(R.id.WorkoutDurationSpinner);
        editTextLocation = findViewById(R.id.editTextLocation);
        createBtn = findViewById(R.id.doneButton);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String workout_name = editTextWorkoutName.getText().toString().trim();
                String workout_description = editTextDescription.getText().toString().trim();
                String workout_type = workoutTypeSpinner.getSelectedItem().toString();
                String fit_level = fitLevelSpinner.getSelectedItem().toString();
                String workout_duration = workoutDurationSpinner.getSelectedItem().toString();
                String workout_loc = editTextLocation.getText().toString().trim();

                user = fAuth.getCurrentUser();
                userID = user.getUid();
                DocumentReference documentReference = fStore.collection("workouts").document();
                Map<String,Object> workoutData = new HashMap<>();
                workoutData.put("workoutName",workout_name);
                workoutData.put("workoutDescription",workout_description);
                workoutData.put("workoutType", workout_type);
                workoutData.put("fitLevel", fit_level);
                workoutData.put("workoutDuration", workout_duration);
                workoutData.put("workoutLocation", workout_loc);
                workoutData.put("createdBy", userID);

                documentReference.set(workoutData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateWorkoutActivity.this,"Workout created",Toast.LENGTH_LONG).show();
                        resetInputs();
                    }
                });
            }
        });

        initHomeButton();
        initMapButton();
        initCreateWorkoutButton();
    }

    //clears user inputs for new workoutActivity
    private void resetInputs() {
        editTextWorkoutName.setText("");
        editTextDescription.setText("");
        editTextLocation.setText("");
        workoutTypeSpinner.setSelection(0);
        fitLevelSpinner.setSelection(0);
        workoutDurationSpinner.setSelection(0);
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
}