package com.example.pety.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pety.R;
import com.example.pety.fragments.BeautyHealthFragment;
import com.example.pety.fragments.FamilyFragment;
import com.example.pety.fragments.HomeFragment;
import com.example.pety.fragments.InfoFragment;
import com.example.pety.fragments.PetFragment;
import com.example.pety.fragments.WalkFeedFragment;
import com.example.pety.interfaces.InsertDialogInterface;
import com.example.pety.objects.Fab;
import com.example.pety.objects.Family;
import com.example.pety.objects.InsertFamilyDialog;
import com.example.pety.objects.InsertPetDialog;
import com.example.pety.objects.InsertTimeDateDialog;
import com.example.pety.objects.InsertTimeDialog;
import com.example.pety.objects.Pet;
import com.example.pety.objects.User;
import com.example.pety.utils.FirebaseDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    BottomNavigationView bottomNavigationView;
    Toolbar main_toolbar;
    FloatingActionButton fab_button;


    //Fragments
    Fragment selectedFragment = null;
    InsertFamilyDialog insertFamilyDialog;
    InsertPetDialog insertPetDialog;
    HomeFragment homeFragment;
    PetFragment petFragment;
    FamilyFragment familyFragment;
    InfoFragment infoFragment;
    Fab fab = Fab.FAMILY_FAB;

    WalkFeedFragment walkFeedFragment;
    BeautyHealthFragment beautyHealthFragment;
    InsertTimeDateDialog insertTimeDateDialog;
    InsertTimeDialog insertTimeDialog;
    CircleImageView user_image_view;
    TextView navMenuUserNameDisplay;
    TextView navMenuPhoneDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();
        setSupportActionBar(main_toolbar);
        initFragments();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, main_toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();


        familyFragment = new FamilyFragment(this);
        familyFragment.setSendFamilyCallback(sendFamilyCallback);

        petFragment = new PetFragment(this);
        petFragment.setSendFamilyCallback(sendFamilyCallback);

        firebaseDB.retrieveUserDataFromDB(sendFamilyCallback);

        Log.d("TAG", "after init family fragment ");


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).addToBackStack(null).commit();

        insertFamilyDialog.setInsertDialogInterface(insertDialogInterface);
        insertPetDialog.setInsertDialogInterface(insertDialogInterface);
        insertTimeDialog.setInsertDialogInterface(insertDialogInterface);
        walkFeedFragment.setInsertTimeDialog(insertTimeDialog);
        insertTimeDateDialog.setInsertDialogInterface(insertDialogInterface);
        beautyHealthFragment.setInsertTimeDateDialog(insertTimeDateDialog);

        user_image_view = nav_view.getHeaderView(0).findViewById(R.id.user_image_view);
        navMenuUserNameDisplay = nav_view.getHeaderView(0).findViewById(R.id.navMenuUserNameDisplay);
        navMenuPhoneDisplay = nav_view.getHeaderView(0).findViewById(R.id.navMenuPhoneDisplay);
        nav_view.setNavigationItemSelectedListener(navItemSelectedListener);
        firebaseDB.getImageUser(user_image_view, this);

    }


    private void initFragments() {
        insertFamilyDialog = new InsertFamilyDialog();
        insertPetDialog = new InsertPetDialog();
        insertTimeDialog = new InsertTimeDialog();
        insertTimeDateDialog = new InsertTimeDateDialog();
        homeFragment = new HomeFragment();
        petFragment = new PetFragment(this);
        infoFragment = new InfoFragment();
        walkFeedFragment = new WalkFeedFragment(this);
        beautyHealthFragment = new BeautyHealthFragment(this);
    }

    private FamilyFragment.SendFamilyCallback sendFamilyCallback = new FamilyFragment.SendFamilyCallback() {
        @Override
        public void sendFamily(Family family) {
            Log.d("TAG", "implement family callback: " + "familyName: " + family.getF_name() + "imageName: " + family.getImageUrl());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, petFragment).addToBackStack(null).commit();
            fab = Fab.PET_FAB;
            petFragment.displayReceivedData(family);
        }

        @Override
        public void sendUser(User user) {
            navMenuUserNameDisplay.setText(user.getF_name() + " " + user.getL_name());
            navMenuPhoneDisplay.setText(user.getPhone_number());

            if (user.getFamilies_map().size() == 0) {
                insertFamilyDialog.show(getSupportFragmentManager(), "Insert item");

            } else if (user.getFamilies_map().size() > 0) {
                familyFragment.setCurrentUser(user);
            }

        }

        @Override
        public void sendPet(Family family, Pet pet, Fab chose_fab) {
            fab = chose_fab;
            if (Fab.WALK_FAB == chose_fab || Fab.FEED_FAB == chose_fab) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, walkFeedFragment).addToBackStack(null).commit();
                walkFeedFragment.setPet(family, pet, chose_fab);
            } else if (Fab.BEAUTY_FAB == chose_fab || Fab.HEALTH_FAB == chose_fab) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, beautyHealthFragment).addToBackStack(null).commit();
                beautyHealthFragment.setPet(family, pet, chose_fab);
            }

        }
    };

    private InsertDialogInterface insertDialogInterface = new InsertDialogInterface() {
        @Override
        public void applyTexts(String familyName, String imageName, Uri imageUri) {
            Log.d("TAG", "familyName: " + familyName + "imageName: " + imageName + "imageUri: " + imageUri);
            familyFragment.setItem(familyName, imageName, imageUri);
        }

        @Override
        public void applyAttPet(String petName, String petType, String birthday, String petImagePath, Uri imageUri) {
            //set item pet
            petFragment.setPetItem(petName, petType, birthday, petImagePath, imageUri);
        }

        @Override
        public void applyTime(String time, String op) {
            if (op.equals(InsertTimeDialog.UPDATE)) {
                if (fab == Fab.WALK_FAB) {
                    //Create set walk update method
                    walkFeedFragment.updateWalkItem(time);
                } else {
                    //Create set feed update method
                    walkFeedFragment.updateFeedItem(time);
                }
            } else {
                if (fab == Fab.WALK_FAB) {
                    Log.d("TAG", "applyTime: from main activity (Walk)" + time);
                    walkFeedFragment.setWalkItem(time);
                } else {
                    Log.d("TAG", "applyTime: from main activity (Feed)" + time);
                    walkFeedFragment.setFeedItem(time);
                }
            }
        }

        @Override
        public void applyTimeDate(String time, String op) {
            if (op.equals(InsertTimeDialog.UPDATE)) {
                if (fab == Fab.BEAUTY_FAB) {
                    //Create set beauty update method
                    beautyHealthFragment.updateBeautyItem(time);
                } else {
                    //Create set health update method
                    beautyHealthFragment.updateHealthItem(time);
                }
            } else {
                if (fab == Fab.BEAUTY_FAB) {
                    Log.d("TAG", "applyTimeDate: from main activity (Beauty)" + time);
                    beautyHealthFragment.setBeautyItem(time);
                } else {
                    Log.d("TAG", "applyTimeDate: from main activity (Health)" + time);
                    beautyHealthFragment.setHealthItem(time);
                }
            }
        }
    };

    private NavigationView.OnNavigationItemSelectedListener navItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_profile:
                    Intent myIntent = new Intent(Main_Activity.this, Profile_Activity.class);
                    myIntent.putExtra(Profile_Activity.UPDATE, Profile_Activity.UPDATE);
                    startActivity(myIntent);
                    break;
                case R.id.nav_logout:
                    firebaseDB.getFirebaseAuth().signOut();
                    Intent intent = new Intent(Main_Activity.this, Login_Activity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            drawer_layout.closeDrawer(GravityCompat.START);
            return true;
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_home:
                    Log.d("TAG", "nav home");
                    fab_button.setVisibility(View.VISIBLE);
                    fab = Fab.PET_FAB;
                    selectedFragment = homeFragment;
                    break;
                case R.id.nav_my_families:
                    Log.d("TAG", "nav my families");
                    selectedFragment = familyFragment;
                    fab = Fab.FAMILY_FAB;
                    break;
                case R.id.nav_info:
                    selectedFragment = infoFragment;
                    Log.d("TAG", "nav info");
                    fab_button.setVisibility(View.INVISIBLE);
                    break;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        }
    };

    private void initViews() {
        nav_view.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        fab_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (fab) {
                    case PET_FAB:
                        insertPetDialog.show(getSupportFragmentManager(), "Insert item");
                        Log.d("TAG", "onClick: PET_FAB");
                        break;
                    case FAMILY_FAB:
                        Log.d("TAG", "onClick: FAMILY_FAB");
                        insertFamilyDialog.show(getSupportFragmentManager(), "Insert item");
                        insertFamilyDialog.setInsertDialogInterface(insertDialogInterface);
                        break;
                    case SHARE_MY_FAMILY_FAB:
                        Log.d("TAG", "onClick: SHARE_MY_FAMILY");
                        break;
                    case WALK_FAB:
                        insertTimeDialog.show(getSupportFragmentManager(), "Insert walk");
                        Log.d("TAG", "onClick: WALK_FAB");
                        break;
                    case FEED_FAB:
                        insertTimeDialog.show(getSupportFragmentManager(), "Insert feed");
                        Log.d("TAG", "onClick: FEED_FAB");
                        break;
                    case BEAUTY_FAB:
                        insertTimeDateDialog.show(getSupportFragmentManager(), "Insert beauty");
                        Log.d("TAG", "onClick: BEAUTY_FAB");
                        break;
                    case HEALTH_FAB:
                        insertTimeDateDialog.show(getSupportFragmentManager(), "Insert health");
                        Log.d("TAG", "onClick: HEALTH_FAB");
                        break;
                }

            }
        });
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
}
