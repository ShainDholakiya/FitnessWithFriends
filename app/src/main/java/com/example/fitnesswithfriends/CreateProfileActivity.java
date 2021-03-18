package com.example.fitnesswithfriends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class CreateProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CreateProfileActivity.class.getSimpleName();
    Button btnsave;
    private FirebaseAuth mAuth;
    private TextView textViewemailname;
    private DatabaseReference databaseReference;
    private EditText editTextFirstName, editTextLastName;
    private ImageView profileImageView;
    private Spinner genderSpinner, favWorkoutSpinner, fitLevelSpinner;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE = 123;
    Uri imagePath;
    private StorageReference storageReference;

    public CreateProfileActivity() {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            //User is not logged in
            startActivity(new Intent(CreateProfileActivity.this, LoginActivity.class));
            finish();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        editTextFirstName = (EditText)findViewById(R.id.EditTextFirstName);
        editTextLastName = (EditText)findViewById(R.id.EditTextLastName);
        btnsave = (Button)findViewById(R.id.btnSaveButton);
        FirebaseUser user = mAuth.getCurrentUser();
        btnsave.setOnClickListener(this);
        textViewemailname=(TextView)findViewById(R.id.textViewEmailAddress);
        textViewemailname.setText(user.getEmail());
        profileImageView = findViewById(R.id.update_imageView);
        genderSpinner = (Spinner) findViewById(R.id.GenderSpinner);
        favWorkoutSpinner = (Spinner) findViewById(R.id.FavWorkoutSpinner);
        fitLevelSpinner = (Spinner) findViewById(R.id.FitLevelSpinner);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent();
                profileIntent.setType("image/*");
                profileIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(profileIntent, "Select Image."), PICK_IMAGE);
            }
        });
    }

    private void userInformation(){
        String first_name = editTextFirstName.getText().toString().trim();
        String last_name = editTextLastName.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String fav_workout = favWorkoutSpinner.getSelectedItem().toString();
        String fit_level = fitLevelSpinner.getSelectedItem().toString();
        User user_info = new User(first_name,last_name,gender,fav_workout,fit_level);
        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(user_info);
        Toast.makeText(getApplicationContext(),"User information updated",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view == btnsave){
            if (imagePath == null) {

                Drawable drawable = this.getResources().getDrawable(R.drawable.defavatar);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.defavatar);
                userInformation();
                finish();
                startActivity(new Intent(CreateProfileActivity.this, HomeActivity.class));
            }
            else {
                userInformation();
                sendUserData();
                finish();
                startActivity(new Intent(CreateProfileActivity.this, HomeActivity.class));
            }
        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // Get "User UID" from Firebase > Authentification > Users.
        DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getUid());
        StorageReference imageReference = storageReference.child(mAuth.getUid()).child("Images").child("Profile Pic"); //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateProfileActivity.this, "Error: Uploading profile picture", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(CreateProfileActivity.this, "Profile picture uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openSelectProfilePictureDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        TextView title = new TextView(this);
        title.setText("Profile Picture");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);
        TextView msg = new TextView(this);
        msg.setText("Please select a profile picture \n Tap the sample avatar logo");
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        alertDialog.setView(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
            }
        });
        new Dialog(getApplicationContext());
        alertDialog.show();
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);   // Set Position
        okBT.setTextColor(Color.BLUE);
        okBT.setLayoutParams(neutralBtnLP);
    }
}