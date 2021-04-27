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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = EditProfileActivity.class.getSimpleName();
    Button btnsave;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private TextView textViewemailname;
    private EditText editTextFirstName, editTextLastName;
    private ImageView profileImageView;
    private Spinner genderSpinner, favWorkoutSpinner, fitLevelSpinner;
    private static int PICK_IMAGE = 123;
    Uri imagePath;
    private StorageReference storageReference;

    String userID;

    public EditProfileActivity() {

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
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (fAuth.getCurrentUser() == null) {
            //User is not logged in
            startActivity(new Intent(EditProfileActivity.this, LoginActivity.class));
            finish();
        }

        editTextFirstName = (EditText)findViewById(R.id.EditTextFirstName);
        editTextLastName = (EditText)findViewById(R.id.EditTextLastName);
        btnsave = (Button)findViewById(R.id.btnSaveButton);
        FirebaseUser user = fAuth.getCurrentUser();
        btnsave.setOnClickListener(this);
        textViewemailname=(TextView)findViewById(R.id.textViewEmailAddress);
        textViewemailname.setText(user.getEmail());
        profileImageView = findViewById(R.id.update_imageView);
        genderSpinner = (Spinner) findViewById(R.id.GenderSpinner);
        favWorkoutSpinner = (Spinner) findViewById(R.id.FavWorkoutSpinner);
        fitLevelSpinner = (Spinner) findViewById(R.id.FitLevelSpinner);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent();
                profileIntent.setType("image/*");
                profileIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(profileIntent, "Select Image."), PICK_IMAGE);
            }
        });

        userID = fAuth.getCurrentUser().getUid();

        List<String> genderArray = new ArrayList<>();
        genderArray.add("Male");
        genderArray.add("Female");
        genderArray.add("Other");

        fStore.collection("allUsers").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                editTextFirstName.setText((String) task.getResult().getData().get("firstName"));
                editTextLastName.setText((String) task.getResult().getData().get("lastName"));
                String gender = (String) task.getResult().getData().get("gender");
                Integer genderIndex = genderArray.indexOf(gender);
                genderSpinner.setSelection(genderIndex);
            }
        });

    }

    private void userInformation(){
        String first_name = editTextFirstName.getText().toString().trim();
        String last_name = editTextLastName.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String fav_workout = favWorkoutSpinner.getSelectedItem().toString();
        String fit_level = fitLevelSpinner.getSelectedItem().toString();
        String userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("allUsers").document(userID);
        Map<String,Object> userData = new HashMap<>();
        userData.put("userID",userID);
        userData.put("firstName",first_name);
        userData.put("lastName",last_name);
        userData.put("gender", gender);
        userData.put("favWorkout", fav_workout);
        userData.put("fitLevel", fit_level);

        documentReference.update(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"User information updated",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnsave){
            if (imagePath == null) {

                Drawable drawable = this.getResources().getDrawable(R.drawable.defavatar);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.defavatar);
                userInformation();
                finish();
                startActivity(new Intent(EditProfileActivity.this, HomeActivity.class));
            }
            else {
                userInformation();
                sendUserData();
                finish();
                startActivity(new Intent(EditProfileActivity.this, HomeActivity.class));
            }
        }
    }

    private void sendUserData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        // Get "User UID" from Firebase > Authentification > Users.
        DatabaseReference databaseReference = firebaseDatabase.getReference(fAuth.getUid());
        StorageReference imageReference = storageReference.child(fAuth.getUid()).child("Images").child("Profile Pic"); //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Error: Uploading profile picture", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditProfileActivity.this, "Profile picture uploaded", Toast.LENGTH_SHORT).show();
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

    private void initHomeButton() {
        ImageButton ibList = findViewById(R.id.navHome);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

}