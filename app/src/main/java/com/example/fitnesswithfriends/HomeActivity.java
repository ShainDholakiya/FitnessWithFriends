package com.example.fitnesswithfriends;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button signOut;
    private ListView myWorkoutsList;
    private ListView availableWorkoutsList;
    String userID;
    private List<Workout> workouts;
    List<Workout> myWorkouts;

    private ArrayAdapter<String> myWorkoutsAdapter;
    private ArrayList<String> myWorkoutsArrayList;
    private ArrayAdapter<String> availableWorkoutsAdapter;
    private ArrayList<String> availableWorkoutsArrayList;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() == null) {
            //User is not logged in
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        setContentView(R.layout.activity_home);

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        signOut = (Button) findViewById(R.id.sign_out);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        myWorkoutsList = findViewById(R.id.myWorkoutsListView);
        availableWorkoutsList = findViewById(R.id.availableWorkoutsListView);
        userID = mAuth.getCurrentUser().getUid();
        workouts = new ArrayList<>();
        myWorkouts = new ArrayList<>();

        myWorkoutsArrayList = new ArrayList<String>();
        myWorkoutsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, myWorkoutsArrayList);
        myWorkoutsList.setAdapter(myWorkoutsAdapter);

        availableWorkoutsArrayList = new ArrayList<String>();
        availableWorkoutsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, availableWorkoutsArrayList);
        availableWorkoutsList.setAdapter(availableWorkoutsAdapter);

        getMyWorkouts();
        getAvailableWorkouts();

        initHomeButton();
        initMapButton();
        initCreateWorkoutButton();
    }

    private void getMyWorkouts() {

        fStore.collection("workouts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                workouts.clear();
                if (queryDocumentSnapshots.isEmpty()) {
                    return;
                } else {
                    workouts = queryDocumentSnapshots.toObjects(Workout.class);

                    for (int i = 0; i < workouts.size(); i++) {
                        if (workouts.get(i).getCreatedBy().equals(userID)) {
                            myWorkouts.add(workouts.get(i));
                        }
//                        System.out.println(myWorkouts);
                    }
                }

                for (Workout workout : myWorkouts) {
                    myWorkoutsArrayList.add(workout.workoutName);
                    myWorkoutsAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    private void getAvailableWorkouts() {

        fStore.collection("workouts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                workouts.clear();
                if (queryDocumentSnapshots.isEmpty()) {
                    return;
                } else {
                    workouts = queryDocumentSnapshots.toObjects(Workout.class);

                    for (int i = 0; i < workouts.size(); i++) {
                        if (workouts.get(i).getCreatedBy().equals(userID)) {
                            workouts.remove(i);
                        }
//                        System.out.println(myWorkouts);
                    }
                }

                for (Workout workout : workouts) {
                    availableWorkoutsArrayList.add(workout.workoutName);
                    availableWorkoutsAdapter.notifyDataSetChanged();
                }

            }
        });

    }


    private void initHomeButton() {
        ImageButton ibList = findViewById(R.id.navHome);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initMapButton() {
        ImageButton ibList = findViewById(R.id.navMap);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initCreateWorkoutButton() {
        ImageButton ibList = findViewById(R.id.navCreateWorkout);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CreateWorkoutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    //sign out method
    public void signOut() {
        mAuth.signOut();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }

}