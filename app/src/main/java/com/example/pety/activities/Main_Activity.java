package com.example.pety.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pety.R;
import com.example.pety.fragments.FamilyFragment;
import com.example.pety.fragments.HomeFragment;
import com.example.pety.fragments.PetFragment;
import com.example.pety.fragments.ShareFamilyFragment;
import com.example.pety.fragments.WalkFeedFragment;
import com.example.pety.interfaces.InsertDialogInterface;
import com.example.pety.objects.Fab;
import com.example.pety.objects.Family;
import com.example.pety.objects.InsertFamilyDialog;
import com.example.pety.objects.InsertPetDialog;
import com.example.pety.objects.InsertTimeDialog;
import com.example.pety.objects.Pet;
import com.example.pety.objects.User;
import com.example.pety.utils.FirebaseDB;
import com.example.pety.utils.MySP;
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
    ShareFamilyFragment shareFamilyFragment;
    Fab fab = Fab.FAMILY_FAB;

    WalkFeedFragment walkFeedFragment;
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

        user_image_view = nav_view.getHeaderView(0).findViewById(R.id.user_image_view);
        navMenuUserNameDisplay = nav_view.getHeaderView(0).findViewById(R.id.navMenuUserNameDisplay);
        navMenuPhoneDisplay = nav_view.getHeaderView(0).findViewById(R.id.navMenuPhoneDisplay);

        firebaseDB.getImageUser(user_image_view,this);

    }


    private void initFragments() {
        insertFamilyDialog = new InsertFamilyDialog();
        insertPetDialog = new InsertPetDialog();
        insertTimeDialog = new InsertTimeDialog();
        homeFragment = new HomeFragment();
        petFragment = new PetFragment(this);
        shareFamilyFragment = new ShareFamilyFragment();
        walkFeedFragment = new WalkFeedFragment(this);
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
            navMenuUserNameDisplay.setText(user.getF_name() + " " +  user.getL_name());
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
            } else {
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,walkFeedFragment).commit();
//                walkFeedFragment.setPet(family,pet,care);
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
                case R.id.nav_share_families:
                    Log.d("TAG", "nav share families ");
                    selectedFragment = shareFamilyFragment;
                    fab = Fab.SHARE_MY_FAMILY_FAB;
                    break;
                case R.id.nav_info:
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
