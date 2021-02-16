package com.example.pety.utils;

import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.pety.objects.Family;
import com.example.pety.objects.Pet;
import com.example.pety.objects.User;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class FirebaseDB {
    private static final String TAG = "RealTimeDatabase";
    public static final String USERS = "users";
    public static final String FIRST_NAME = "f_name";
    public static final String LAST_NAME = "l_name";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String FAMILIES = "families";
    public static final String PETS = "pets";

    private FirebaseAuth authDatabase;
    private FirebaseDatabase database;
    private static FirebaseDB firebaseDB;
    private FirebaseStorage storage;
    Family family;

    private FirebaseDB() {
        database = FirebaseDatabase.getInstance();
        authDatabase = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public static FirebaseDB getInstance() {
        if (firebaseDB == null) {
            firebaseDB = new FirebaseDB();
        }
        return firebaseDB;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public FirebaseAuth getFirebaseAuth() {
        return authDatabase;
    }

    public FirebaseStorage getFirebaseStorage() {
        return storage;
    }

    /**
     *
     * @param uid
     * @param phone_number
     */
    public void addNewUserProfile(String uid, String phone_number) {
        DatabaseReference userRef = database.getReference(USERS);
        userRef.child(uid);
        userRef.child(uid).child(PHONE_NUMBER).setValue(phone_number);
    }

    /**
     * @param firstName
     * @param LastName
     */
    public void updateFirstNameAndLastName(String firstName,String LastName){
        String uid = getFirebaseAuth().getCurrentUser().getUid();
        DatabaseReference userRef = database.getReference(USERS);
        userRef.child(uid).child(FIRST_NAME).setValue(firstName);
        userRef.child(uid).child(LAST_NAME).setValue(LastName);
    }

    public void retrieveUserDataFromDB(){
        User user = new User();
        String uid = getFirebaseAuth().getCurrentUser().getUid();
        DatabaseReference myRef = getDatabase().getReference(USERS).child(uid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> listOfFamiliesKey;
                String firstName = snapshot.child(FIRST_NAME).getValue().toString();
                String lastName = snapshot.child(LAST_NAME).getValue().toString();
                String phoneNumber = snapshot.child(PHONE_NUMBER).getValue().toString();
                if(snapshot.hasChild(FAMILIES)){
                    listOfFamiliesKey = (List<String>) snapshot.child(FAMILIES).getValue();
                    user.setFamilies_keys(listOfFamiliesKey);
                }
                user.setF_name(firstName);
                user.setL_name(lastName);
                user.setPhone_number(phoneNumber);
                Log.d("ttttt", "onDataChange: " + user.toString());
                MySP.getInstance().writeDataToStorage(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void retrieveAllFamilies(List<String> families_keys , ArrayList<Family> families){
        DatabaseReference myFamilies = getDatabase().getReference().child(FAMILIES);
        int i;

        for(i = 0 ; i < families_keys.size(); i++){
            int finalI = i;
            myFamilies.child(families_keys.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Map<String , Pet> pets;
                    family = new Family();
                    String family_key = snapshot.child("family_key").getValue().toString();
                    String familyName = snapshot.child("f_name").getValue().toString();
                    String familyImagePath = snapshot.child("imageURL").getValue().toString();
                    if(snapshot.hasChild("pets")){
                        pets = (Map<String, Pet>) snapshot.child("pets").getValue();
                        family.setPets(pets);
                    }
                    family.setFamily_key(family_key);
                    family.setF_name(familyName);
                    family.setImageUrl(familyImagePath);
                    families.add(family);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    /**
     * This function store in the database new family
     * @param family
     */
    public void writeNewFamilyToDB(Family family , List<String> families , String imageName , Uri contentUri){
        final StorageReference image = getFirebaseStorage().getReference().child("pictures/"+ FAMILIES +"/" + imageName);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        family.setImageUrl(uri.toString());
                        String key = getDatabase().getReference().child(FAMILIES).push().getKey();
                        family.setFamily_key(key);
                        Map<String, Object> familyValues = family.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/" + FAMILIES + "/" + key, familyValues);
                        getDatabase().getReference().updateChildren(childUpdates);

                        String uid = getFirebaseAuth().getCurrentUser().getUid();
                        families.add(key);
                        childUpdates.put("/" + USERS + "/" + uid + "/" + FAMILIES + "/", families);
                        getDatabase().getReference().updateChildren(childUpdates);
                    }
                    });
                Log.d("TAG", "onSuccess: Upload family successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: Upload family failed");
            }
        });
    }
}
