package com.example.fitnesswithfriends;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private EditText editTextFirstName, editTextLastName;
    private ImageView profileImageView;
    private TextView textViewemailname;
    Button btnSave;
    private Spinner genderSpinner, favWorkoutSpinner, fitLevelSpinner;

//    public EditProfileActivity() {
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        editTextFirstName = (EditText)findViewById(R.id.EditTextFirstName);
        editTextLastName = (EditText)findViewById(R.id.EditTextLastName);
        btnSave = (Button)findViewById(R.id.btnSaveButton);
        FirebaseUser user = mAuth.getCurrentUser();
//        btnsave.setOnClickListener(this);
        textViewemailname = (TextView)findViewById(R.id.textViewEmailAddress);
        textViewemailname.setText(user.getEmail());
        profileImageView = findViewById(R.id.update_imageView);
        genderSpinner = (Spinner) findViewById(R.id.GenderSpinner);
        favWorkoutSpinner = (Spinner) findViewById(R.id.FavWorkoutSpinner);
        fitLevelSpinner = (Spinner) findViewById(R.id.FitLevelSpinner);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getUid());
        StorageReference storageReference = firebaseStorage.getReference();

        // Get the image stored on Firebase via "User id/Images/Profile Pic.jpg".
        storageReference.child(mAuth.getUid()).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Using "Picasso" (http://square.github.io/picasso/) after adding the dependency in the Gradle.
                // ".fit().centerInside()" fits the entire image into the specified area.
                // Finally, add "READ" and "WRITE" external storage permissions in the Manifest.
                Picasso.get().load(uri).fit().centerInside().into(profileImageView);
            }
        });

        if (mAuth.getCurrentUser() == null) {
            //User is not logged in
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                User userInfo = dataSnapshot.getValue(User.class);

                editTextFirstName.setText(userInfo.getFirstName());
                editTextLastName.setText(userInfo.getLastName());

                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(EditProfileActivity.this, R.array.gender_array, android.R.layout.simple_spinner_item);
                genderSpinner.setSelection(adapter1.getPosition(userInfo.getGender()));
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(EditProfileActivity.this, R.array.fav_workout_array, android.R.layout.simple_spinner_item);
                favWorkoutSpinner.setSelection(adapter2.getPosition(userInfo.getFavWorkout()));
                ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(EditProfileActivity.this, R.array.fit_level_array, android.R.layout.simple_spinner_item);
                fitLevelSpinner.setSelection(adapter3.getPosition(userInfo.getFitLevel()));
            }
            @Override
            public void onCancelled( DatabaseError databaseError) {
                Toast.makeText(EditProfileActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInformation();
                finish();
                startActivity(new Intent(EditProfileActivity.this, HomeActivity.class));
            }
        });
    }

    private void userInformation(){
        String first_name = editTextFirstName.getText().toString().trim();
        String last_name = editTextLastName.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String fav_workout = favWorkoutSpinner.getSelectedItem().toString();
        String fit_level = fitLevelSpinner.getSelectedItem().toString();

        User user_info = new User(first_name,last_name,gender,fav_workout,fit_level)  ;
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase.child(user.getUid()).setValue(user_info);
        Toast.makeText(getApplicationContext(),"User information updated",Toast.LENGTH_LONG).show();
    }


//    @Override
//    public void onClick(View view) {
//        if (view == btnsave){
//            userInformation();
//            finish();
//            startActivity(new Intent(EditProfileActivity.this, HomeActivity.class));
//        }
//    }
}