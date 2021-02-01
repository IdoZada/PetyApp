package com.example.pety.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.pety.R;
import com.example.pety.fragments.FamilyFragment;
import com.example.pety.fragments.HomeFragment;
import com.example.pety.fragments.PetFragment;
import com.example.pety.objects.Family;
import com.example.pety.objects.InsertDialog;
import com.example.pety.objects.User;
import com.example.pety.utils.FirebaseDB;
import com.example.pety.utils.MySP;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Main_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    BottomNavigationView bottomNavigationView;

    Toolbar main_toolbar;
    FloatingActionButton fab_button;

    User currentUser;

    //My shared preference singleton class
    MySP mySP;

    //Fragments
    Fragment selectedFragment = null;
    InsertDialog insertDialog;
    HomeFragment homeFragment;
    PetFragment petFragment;
    FamilyFragment familyFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySP.initialize(Main_Activity.this);


        findViews();
        initViews();

        insertDialog = new InsertDialog();
        homeFragment = new HomeFragment();
        petFragment = new PetFragment();

        nav_view.setNavigationItemSelectedListener(this);
        setSupportActionBar(main_toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, main_toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();



        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


//        String uid = firebaseDB.getFirebaseAuth().getCurrentUser().getUid();
//        DatabaseReference myRef =  firebaseDB.getDatabase().getReference("users");

        //String userName = dataSnapshot.getValue(String.class);
//        Log.d("ttttt", "onChildAdded:userName: " + userName);
//        View headerView = nav_view.getHeaderView(0);
//        TextView navUsername = headerView.findViewById(R.id.navMenuUserNameDisplay);
//        navUsername.setText(userName);

        firebaseDB.retrieveUserDataFromDB();
        currentUser = getCurrentUser();
//        Log.d("ttttt", currentUser.toString());
        familyFragment = new FamilyFragment(this);
//        ArrayList<Family> families = MySP.getInstance().readFamiliesFromStorage();
//        Log.d("tttttt", "main " + families);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homeFragment).commit();

        if(currentUser.getFamilies_keys().size() == 0){
            insertDialog.show(getSupportFragmentManager(),"Insert item");
            insertDialog.setInsertDialogInterface(insert_dialogInterface);
        }


        fab_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDialog.show(getSupportFragmentManager(),"Insert item");
                insertDialog.setInsertDialogInterface(insert_dialogInterface);
            }
        });


//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    toolbarFragment).commit();
//            nav_view.setCheckedItem(R.id.nav_family);
//        }
        //final FloatingActionButton fab = findViewById(R.id.fab_main);

        //setSupportActionBar(toolbar);
//        itemData = new ArrayList<>();
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
//
//
//        itemAdapter = new ItemAdapter(itemData,this);
//
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(itemAdapter);

    }


   private User getCurrentUser(){
        mySP = MySP.getInstance();
       return mySP.readDataFromStorage();
   }


    private InsertDialog.insert_DialogInterface insert_dialogInterface = new InsertDialog.insert_DialogInterface() {
        @Override
        public void applyTexts(String familyName) {
            Log.d("ttttt", "familyName: "+ familyName);
            familyFragment.setItem(familyName);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_home:
                        fab_button.setVisibility(View.VISIBLE);
                        selectedFragment = homeFragment;
                    break;
                case R.id.nav_my_families:
                        selectedFragment = familyFragment;
//                        fab_button.setVisibility(View.VISIBLE);
                    Log.d("ttttt", "nav_my_families ");
                    break;
                case R.id.nav_share_families:
                    Log.d("ttttt", "nav_share_families ");
//                        selectedFragment = new PetFragment();
//                        fab_button.setVisibility(View.VISIBLE);
                    break;
                case R.id.nav_info:
                        fab_button.setVisibility(View.INVISIBLE);
                    break;

            }

            if(selectedFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            }
            return true;
        }
    };

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        MenuInflater menuInflater = getMenuInflater();
////        menuInflater.inflate(R.menu.item_menu,menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.iconInsert: {
//                InsertDialog dialog = new InsertDialog();
//                dialog.show(getSupportFragmentManager(),"Insert Item");
//            }
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void initViews() {

    }

    private void findViews() {
        fab_button = findViewById(R.id.fab_button);
        main_toolbar = findViewById(R.id.main_toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        drawer_layout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

//    @Override
//    public void applyTexts(String familyName) {
//        Family family = new Family(R.drawable.ic_baseline_android_24,UUID.randomUUID(),null,familyName);
//        Log.d("ttst", "applyTexts: " + family.getF_name() + " UUID: " + family.getFamily_id());
//        itemData.add(0,family);
//        itemAdapter.notifyItemInserted(0);
//    }
}


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