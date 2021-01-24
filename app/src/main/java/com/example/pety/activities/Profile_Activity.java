package com.example.pety.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.pety.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_Activity extends AppCompatActivity {

    TextInputLayout profile_LAY_firstName;
    TextInputLayout profile_LAY_lastName;
    MaterialButton profile_BTN_continue;
    FirebaseDatabase database;

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
                //updateUserProfile();
            }
        });
    }

    private void updateUserProfile() {
    }

    private void findViews() {
        profile_LAY_firstName = findViewById(R.id.profile_LAY_firstName);
        profile_LAY_lastName = findViewById(R.id.profile_LAY_lastName);
        profile_BTN_continue = findViewById(R.id.profile_BTN_continue);
    }
}