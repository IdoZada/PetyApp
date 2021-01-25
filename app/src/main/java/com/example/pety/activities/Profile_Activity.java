package com.example.pety.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pety.R;
import com.example.pety.utils.FirebaseDB;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_Activity extends AppCompatActivity {

    TextInputLayout profile_LAY_firstName;
    TextInputLayout profile_LAY_lastName;
    MaterialButton profile_BTN_continue;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViews();
        initViews();

    }

    private void initViews() {

        profile_BTN_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
    }

    private void updateUserProfile() {
        String firstName = profile_LAY_firstName.getEditText().getText().toString();
        String LastName = profile_LAY_lastName.getEditText().getText().toString();
        //Update first name and last name in the firebase database
        firebaseDB.updateFirstNameAndLastName(firstName,LastName);
        Intent myIntent = new Intent(this,Main_Activity.class);
        startActivity(myIntent);
        finish();
        return;
    }

    private void findViews() {
        profile_LAY_firstName = findViewById(R.id.profile_LAY_firstName);
        profile_LAY_lastName = findViewById(R.id.profile_LAY_lastName);
        profile_BTN_continue = findViewById(R.id.profile_BTN_continue);
    }
}