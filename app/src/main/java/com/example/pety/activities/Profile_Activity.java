package com.example.pety.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.pety.R;
import com.example.pety.objects.User;
import com.example.pety.utils.FirebaseDB;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

public class Profile_Activity extends AppCompatActivity {
    public static final String PROFILE = "profile";
    public static final String UPDATE = "update";
    public static final String NEW = "new";

    FloatingActionButton fab_add_photo;
    AppCompatImageView imgProfile;
    TextInputLayout profile_LAY_firstName;
    TextInputLayout profile_LAY_lastName;
    MaterialButton profile_BTN_continue;

    FirebaseDB firebaseDB = FirebaseDB.getInstance();

    String imageFileName;
    Uri imageUri;
    String option = NEW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        option = getIntent().getStringExtra(UPDATE);

        findViews();
        initViews();

        if(option != null){
            updateUserProfile();
        }
    }

    /**
     * Update user details
     */
    private void updateUserProfile() {
        User currentUser = firebaseDB.getUser();
        imageUri = Uri.parse(currentUser.getImage_url());
        profile_BTN_continue.setText("Save");
        profile_LAY_firstName.getEditText().setText(currentUser.getF_name());
        profile_LAY_lastName.getEditText().setText(currentUser.getL_name());
        Glide.with(this).load(currentUser.getImage_url()).into(imgProfile);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            imageUri = data.getData(); //Image Uri will not be null for RESULT_OK
            File file = ImagePicker.Companion.getFile(data);
            imgProfile.setImageURI(imageUri);
            imgProfile.setScaleType(ImageView.ScaleType.FIT_XY);
            imageFileName = file.getName();
            Log.d(PROFILE, "onActivityResult: Image Uri:  " + imageFileName);
        }
    }

    /**
     * Upload user details to realtime firebase
     */
    private void uploadUserProfile() {
        User user = new User();
        String firstName = profile_LAY_firstName.getEditText().getText().toString();
        String LastName = profile_LAY_lastName.getEditText().getText().toString();
        user.setF_name(firstName);
        user.setL_name(LastName);

        firebaseDB.updateFirstNameAndLastNameAndImage(user, imageFileName, imageUri);
        Intent myIntent = new Intent(this, Main_Activity.class);
        startActivity(myIntent);
        finish();
        return;
    }

    private void initViews() {
        fab_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(Profile_Activity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        profile_BTN_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUserProfile();
            }
        });
    }

    private void findViews() {
        profile_LAY_firstName = findViewById(R.id.profile_LAY_firstName);
        profile_LAY_lastName = findViewById(R.id.profile_LAY_lastName);
        profile_BTN_continue = findViewById(R.id.profile_BTN_continue);
        fab_add_photo = findViewById(R.id.fab_add_photo);
        imgProfile = findViewById(R.id.imgProfile);
    }
}