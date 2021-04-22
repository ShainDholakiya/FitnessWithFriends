package com.example.fitnesswithfriends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button signOut;
    private TextView myWorkoutsList;
    String userID;
    private List<Workout> workouts;
    List<Workout> myWorkouts;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

//        fStore.collection("workouts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//            }
//        });


//        fStore.collection("workouts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (queryDocumentSnapshots.isEmpty()) {
//                    return;
//                } else {
//                    List<Object> allDocuments = new ArrayList<>(queryDocumentSnapshots.getDocuments());
//                    for (int i = 0; i < allDocuments.size(); i++) {
//                        Object document = allDocuments.get(i);
//
//                        System.out.println(document);
//                    }
////                    System.out.println(allDocuments);
//                }
//            }
//        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

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

        myWorkoutsList = findViewById(R.id.myWorkoutsListTextView);
        userID = mAuth.getCurrentUser().getUid();
        workouts = new ArrayList<>();
        myWorkouts = new ArrayList<>();

        getMyWorkouts();

        initHomeButton();
        initMapButton();
        initCreateWorkoutButton();
    }

    private void getMyWorkouts() {
//        FirebaseDatabase.getInstance().getReference().child("workouts").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                workouts.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Workout workout = snapshot.getValue(Workout.class);
//
//                    workouts.add(workout);
//                    System.out.println("Hi");
//
////                    for (String id : idList) {
////                        if (workout.getId().equals(userID)) {
////                            workouts.add(workout);
////                        }
////                    }
//                }
//                System.out.println("------------------");
//                System.out.println(workouts);
////                Log.d("list f", workouts.toString());
////                userAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


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

                String allText = "";

                for (Workout workout : myWorkouts) {
                    String string = String.format("%s | %s | %s | %s | %s | %s", workout.workoutName, workout.workoutDescription, workout.workoutType, workout.fitLevel, workout.workoutDuration, workout.workoutLocation);
                    allText = allText + string + "\n\n";
                }

                myWorkoutsList.setText(allText);
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