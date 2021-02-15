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

import com.example.pety.R;
import com.example.pety.fragments.FamilyFragment;
import com.example.pety.fragments.HomeFragment;
import com.example.pety.fragments.PetFragment;
import com.example.pety.objects.InsertDialog;
import com.example.pety.objects.User;
import com.example.pety.utils.FirebaseDB;
import com.example.pety.utils.MySP;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class Main_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    FirebaseDB firebaseDB = FirebaseDB.getInstance();
    DrawerLayout drawer_layout;
    NavigationView nav_view;
    BottomNavigationView bottomNavigationView;
    Toolbar main_toolbar;
    FloatingActionButton fab_button;

    //My shared preference singleton class
    MySP mySP;
    User currentUser;

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
        setSupportActionBar(main_toolbar);
        initFragments();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, main_toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();

        firebaseDB.retrieveUserDataFromDB();
        currentUser = getCurrentUserFromSP();
        Log.d("TAG", currentUser.toString());
        familyFragment = new FamilyFragment(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,homeFragment).commit();

        if(currentUser.getFamilies_keys().size() == 0){
            insertDialog.show(getSupportFragmentManager(),"Insert item");
            insertDialog.setInsertDialogInterface(insert_dialogInterface);
        }

    }



    private void initFragments(){
        insertDialog = new InsertDialog();
        homeFragment = new HomeFragment();
        petFragment = new PetFragment();
    }

   private User getCurrentUserFromSP(){
        mySP = MySP.getInstance();
       return mySP.readDataFromStorage();
   }


    private InsertDialog.insert_DialogInterface insert_dialogInterface = new InsertDialog.insert_DialogInterface() {
        @Override
        public void applyTexts(String familyName, String imageName,Uri imageUri) {
            Log.d("TAG", "familyName: "+ familyName + "imageName: " + imageName + "imageUri: " + imageUri);
            familyFragment.setItem(familyName,imageName, imageUri);
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.nav_home:
                        Log.d("TAG", "nav home");
                        fab_button.setVisibility(View.VISIBLE);
                        selectedFragment = homeFragment;
                    break;
                case R.id.nav_my_families:
                        Log.d("TAG", "nav my families");
                        selectedFragment = familyFragment;
//                        fab_button.setVisibility(View.VISIBLE);

                    break;
                case R.id.nav_share_families:
                        Log.d("TAG", "nav share families ");
//                        selectedFragment = new PetFragment();
//                        fab_button.setVisibility(View.VISIBLE);
                    break;
                case R.id.nav_info:
                        Log.d("TAG", "nav info");
                        fab_button.setVisibility(View.INVISIBLE);
                    break;

            }

            if(selectedFragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
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
                insertDialog.show(getSupportFragmentManager(),"Insert item");
                insertDialog.setInsertDialogInterface(insert_dialogInterface);
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
