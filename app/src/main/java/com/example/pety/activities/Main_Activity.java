package com.example.pety.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.pety.R;
import com.example.pety.objects.Family;
import com.example.pety.objects.Pet;
import com.example.pety.objects.Type;
import com.example.pety.objects.User;
import com.example.pety.objects.Walk;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main_Activity extends AppCompatActivity {

    private MaterialButton main_BTN_updateUser1;
    private MaterialButton main_BTN_updateUser2;
    private MaterialButton main_BTN_newFamily;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();


//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String user_id = firebaseUser.getUid();
//        String phone_number = firebaseUser.getPhoneNumber();
//        String f_name = "D";
//        String l_name = "M";
//
//        User user = new User(user_id,null,f_name,l_name,phone_number,"");
//        Pet pet = new Pet("p0001","Bony",new Date(), Type.Dog,"",null,null,null,null);
//        ArrayList<Walk> walks = new ArrayList<Walk>();
//        walks.add(new Walk("11:00",false));
//        pet.setWalks(walks);
//        Map<String, Pet> pets = new HashMap<>();
//        pets.put("p0001",pet);
//        Family f = new Family("f0001", pets, "Zada");
//
//        ArrayList<String> families_keys = new ArrayList<>();
//        families_keys.add("f0001");
//        user.setFamilies_keys(families_keys);
//        // Write a message to the database
//        database = FirebaseDatabase.getInstance();
//        DatabaseReference userRef = database.getReference("users");
//        DatabaseReference familyRef = database.getReference("families");
//        userRef.child(user_id).setValue(user);
//        familyRef.child("f0001").setValue(f);

    }

    private void updateUser() {

    }

    private void updateFamily() {
    }

    private void initViews() {

        main_BTN_updateUser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });


        main_BTN_updateUser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFamily();
            }
        });
    }

    private void findViews() {
        main_BTN_updateUser1 = findViewById(R.id.main_BTN_updateUser1);
        main_BTN_updateUser2 = findViewById(R.id.main_BTN_updateUser2);
        main_BTN_newFamily = findViewById(R.id.main_BTN_newFamily);
    }
}