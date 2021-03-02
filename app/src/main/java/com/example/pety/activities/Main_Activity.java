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
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.pety.R;
import com.example.pety.fragments.BeautyHealthFragment;
import com.example.pety.fragments.FamilyFragment;
import com.example.pety.fragments.InfoFragment;
import com.example.pety.fragments.MyFavFamilyFragment;
import com.example.pety.fragments.PetFragment;
import com.example.pety.fragments.WalkFeedFragment;
import com.example.pety.interfaces.InsertDialogInterface;
import com.example.pety.enums.Fab;
import com.example.pety.interfaces.SendDataCallback;
import com.example.pety.objects.Family;
import com.example.pety.enums.FamilyFlag;
import com.example.pety.fragments.InsertFamilyDialog;
import com.example.pety.fragments.InsertPetDialog;
import com.example.pety.fragments.InsertTimeDateDialog;
import com.example.pety.fragments.InsertTimeDialog;
import com.example.pety.fragments.LoadingDialog;
import com.example.pety.objects.Pet;
import com.example.pety.objects.User;
import com.example.pety.utils.FirebaseDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main_Activity extends AppCompatActivity {
    public static final String MAIN = "main";
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    BottomNavigationView bottomNavigationView;
    Toolbar main_toolbar;
    FloatingActionButton fab_button;

    //Fragments
    Fragment selectedFragment = null;
    PetFragment petFragment;
    PetFragment petHomeFragment;
    FamilyFragment familyFragment;
    InfoFragment infoFragment;
    MyFavFamilyFragment myFavFamilyFragment;
    WalkFeedFragment walkFeedFragment;
    BeautyHealthFragment beautyHealthFragment;

    //Dialogs
    InsertFamilyDialog insertFamilyDialog;
    InsertPetDialog insertPetDialog;
    InsertTimeDateDialog insertTimeDateDialog;
    InsertTimeDialog insertTimeDialog;
    CircleImageView user_image_view;
    TextView navMenuUserNameDisplay;
    TextView navMenuPhoneDisplay;

    FirebaseDB firebaseDB = FirebaseDB.getInstance();
    FamilyFlag flag = FamilyFlag.HOME;
    Fab fab = Fab.FAMILY_FAB;
    Family family;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();

        petHomeFragment = new PetFragment(this);
        petHomeFragment.setDataCallback(sendDataCallback);

        loadingDialog();
        setSupportActionBar(main_toolbar);
        initFragments();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, main_toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        familyFragment = new FamilyFragment(this);
        familyFragment.setDataCallback(sendDataCallback);
        myFavFamilyFragment = new MyFavFamilyFragment(this);
        myFavFamilyFragment.setDataCallback(sendDataCallback);

        firebaseDB.retrieveUserDataFromDB(sendDataCallback);
        setInterfaces();
        firebaseDB.getImageUser(user_image_view, this);
    }

    /**
     * Set loading dialog until the info will upload from database
     */
    private void loadingDialog(){
        long delay = 5000;
        LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fab_button.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, petHomeFragment).commit();
                loadingDialog.dismissDialog();
            }
        }, delay);
    }

    private void setInterfaces(){
        insertFamilyDialog.setInsertDialogInterface(insertDialogInterface);
        insertPetDialog.setInsertDialogInterface(insertDialogInterface);
        insertTimeDialog.setInsertDialogInterface(insertDialogInterface);
        insertTimeDateDialog.setInsertDialogInterface(insertDialogInterface);
    }

    private void initFragments() {
        insertFamilyDialog = new InsertFamilyDialog();
        insertPetDialog = new InsertPetDialog();
        insertTimeDialog = new InsertTimeDialog();
        insertTimeDateDialog = new InsertTimeDateDialog();
        petFragment = new PetFragment(this);
        petFragment.setDataCallback(sendDataCallback);
        infoFragment = new InfoFragment();
    }

    /**
     * This callback allow to communicate between fragments and main activity
     */
    private SendDataCallback sendDataCallback = new SendDataCallback() {
        @Override
        public void sendFamily(Family fam , FamilyFlag familyFlag) {
            family = fam;
            if(FamilyFlag.SEND_TO_FAV_FAMILY_FRAGMENT == familyFlag){
                myFavFamilyFragment.setFamily(family);
            }else if(FamilyFlag.SEND_TO_PET_FRAGMENT == familyFlag){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, petFragment).addToBackStack(null).commit();
                fab = Fab.PET_FAB;
                petFragment.displayReceivedData(family);
            }else{
                fab = Fab.PET_FAB;
                petHomeFragment.displayReceivedData(family);
            }
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
            walkFeedFragment = new WalkFeedFragment(Main_Activity.this);
            walkFeedFragment.setDataCallback(sendDataCallback);
            beautyHealthFragment = new BeautyHealthFragment(Main_Activity.this);
            beautyHealthFragment.setDataCallback(sendDataCallback);
            walkFeedFragment.setInsertTimeDialog(insertTimeDialog);
            beautyHealthFragment.setInsertTimeDateDialog(insertTimeDateDialog);

            if (Fab.WALK_FAB == chose_fab || Fab.FEED_FAB == chose_fab) {

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, walkFeedFragment).addToBackStack(null).commit();
                walkFeedFragment.setPet(family, pet, chose_fab);
            } else if (Fab.BEAUTY_FAB == chose_fab || Fab.HEALTH_FAB == chose_fab) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, beautyHealthFragment).addToBackStack(null).commit();
                beautyHealthFragment.setPet(family, pet, chose_fab);
            }
        }

        @Override
        public void sendFamilies(ArrayList<Family> families) {
            myFavFamilyFragment.setFamilies(families);
        }

        @Override
        public void sendActionPetUi(int fillProgressBar, int maxElements,Fab fab) {
            if(flag == FamilyFlag.HOME){
                petHomeFragment.refreshPetUI(fillProgressBar,maxElements,fab);
            }else{
                petFragment.refreshPetUI(fillProgressBar,maxElements,fab);
            }
        }
    };

    /**
     * This interface allow to communicate between dialogs to another fragments and main activity
     */
    private InsertDialogInterface insertDialogInterface = new InsertDialogInterface() {
        @Override
        public void applyTexts(String familyName, String imageName, Uri imageUri) {
            familyFragment.setItem(familyName, imageName, imageUri);
        }

        @Override
        public void applyAttPet(String petName, String petType, String birthday, String petImagePath, Uri imageUri) {
            //set item pet
            if(flag == FamilyFlag.HOME){
                petHomeFragment.setPetItem(petName, petType, birthday, petImagePath, imageUri);
                petFragment.displayReceivedData(family);
            }else if(flag == FamilyFlag.FAMILY){
                petFragment.setPetItem(petName, petType, birthday, petImagePath, imageUri);
                petHomeFragment.displayReceivedData(family);
            }
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
                case R.id.nav_home_family:
                    fab_button.setVisibility(View.INVISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFavFamilyFragment).commit();
                    break;
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
                    fab_button.setVisibility(View.VISIBLE);
                    flag = FamilyFlag.HOME;
                    fab = Fab.PET_FAB;
                    selectedFragment = petHomeFragment;
                    break;
                case R.id.nav_my_families:
                    flag = FamilyFlag.FAMILY;
                    fab_button.setVisibility(View.VISIBLE);
                    fab = Fab.FAMILY_FAB;
                    selectedFragment = familyFragment;
                    break;
                case R.id.nav_info:
                    selectedFragment = infoFragment;
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
        //Init Nav view
        nav_view.setNavigationItemSelectedListener(navItemSelectedListener);
        //Init Bottom Nav View
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        fab_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (fab) {
                    case PET_FAB:
                        fab_button.setVisibility(View.VISIBLE);
                        insertPetDialog.show(getSupportFragmentManager(), "Insert pet");
                        break;
                    case FAMILY_FAB:
                        fab_button.setVisibility(View.VISIBLE);
                        insertFamilyDialog.show(getSupportFragmentManager(), "Insert family");
                        insertFamilyDialog.setInsertDialogInterface(insertDialogInterface);
                        break;
                    case WALK_FAB:
                        fab_button.setVisibility(View.VISIBLE);
                        insertTimeDialog.show(getSupportFragmentManager(), "Insert walk");
                        break;
                    case FEED_FAB:
                        fab_button.setVisibility(View.VISIBLE);
                        insertTimeDialog.show(getSupportFragmentManager(), "Insert feed");
                        break;
                    case BEAUTY_FAB:
                        fab_button.setVisibility(View.VISIBLE);
                        insertTimeDateDialog.show(getSupportFragmentManager(), "Insert beauty");
                        break;
                    case HEALTH_FAB:
                        fab_button.setVisibility(View.VISIBLE);
                        insertTimeDateDialog.show(getSupportFragmentManager(), "Insert health");
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
        user_image_view = nav_view.getHeaderView(0).findViewById(R.id.user_image_view);
        navMenuUserNameDisplay = nav_view.getHeaderView(0).findViewById(R.id.navMenuUserNameDisplay);
        navMenuPhoneDisplay = nav_view.getHeaderView(0).findViewById(R.id.navMenuPhoneDisplay);
    }
}
