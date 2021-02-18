package com.example.pety.utils;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pety.fragments.FamilyFragment;
import com.example.pety.objects.Family;
import com.example.pety.objects.Pet;
import com.example.pety.objects.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    public void updateFirstNameAndLastName(String firstName, String LastName) {
        String uid = getFirebaseAuth().getCurrentUser().getUid();
        DatabaseReference userRef = database.getReference(USERS);
        userRef.child(uid).child(FIRST_NAME).setValue(firstName);
        userRef.child(uid).child(LAST_NAME).setValue(LastName);
    }

    public void retrieveUserDataFromDB(FamilyFragment.SendFamilyCallback sendFamilyCallback) {
        User user = new User();
        String uid = getFirebaseAuth().getCurrentUser().getUid();
        DatabaseReference myRef = getDatabase().getReference(USERS).child(uid);
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Error getting user data");
                } else {
                    Map<String, String> families_map;
                    String firstName = task.getResult().child(FIRST_NAME).getValue().toString();
                    String lastName = task.getResult().child(LAST_NAME).getValue().toString();
                    String phoneNumber = task.getResult().child(PHONE_NUMBER).getValue().toString();
                    if (task.getResult().hasChild(FAMILIES)) {
                        families_map = (Map<String, String>) task.getResult().child(FAMILIES).getValue();
                        Log.d(TAG, "onComplete: retrieveUserDataFromDB size: " + families_map.size());
                        user.setFamilies_map(families_map);
                    }
                    user.setF_name(firstName);
                    user.setL_name(lastName);
                    user.setPhone_number(phoneNumber);
                    sendFamilyCallback.sendUser(user);
                    Log.d("ttttt", "onDataChange: " + user.toString());
                }
            }
        });
    }


    public void retrieveAllFamilies(Map<String, String> families_map, ArrayList<Family> families) {
        DatabaseReference myFamilies = getDatabase().getReference().child(FAMILIES);
        int i;

        Log.d(TAG, "retrieveAllFamilies: " + families_map.size());

        for (String key: families_map.keySet()) {
            myFamilies.child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Error getting families data");
                    } else {
                        Map<String, Pet> pets;
                        family = new Family();
                        String family_key = task.getResult().child("family_key").getValue().toString();
                        String familyName = task.getResult().child("f_name").getValue().toString();
                        String familyImagePath = task.getResult().child("imageURL").getValue().toString();
                        if (task.getResult().hasChild("pets")) {
                            pets = (Map<String, Pet>) task.getResult().child("pets").getValue();
                            family.setPets(pets);
                        }
                        family.setFamily_key(family_key);
                        family.setF_name(familyName);
                        family.setImageUrl(familyImagePath);
                        families.add(family);
                    }
                }
            });
        }

    }

    /**
     * This function store in the database new family
     *
     * @param family
     */
    public void writeNewFamilyToDB(Family family, Map<String, String> families_map, String imageName, Uri contentUri) {
        String key = getDatabase().getReference().child(FAMILIES).push().getKey();
        final StorageReference image = getFirebaseStorage().getReference().child("pictures/" + FAMILIES + "/" + key + "/" + imageName);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(uri -> {
                    family.setImageUrl(uri.toString());

                    family.setFamily_key(key);
                    Map<String, Object> familyValues = family.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/" + FAMILIES + "/" + key, familyValues);
                    getDatabase().getReference().updateChildren(childUpdates);

                    String uid = getFirebaseAuth().getCurrentUser().getUid();
                    families_map.put(key, key);
                    //families.add(key);
                    childUpdates.put("/" + USERS + "/" + uid + "/" + FAMILIES + "/", families_map);
                    getDatabase().getReference().updateChildren(childUpdates);
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

    public void deleteFamilyFromDB(User user, String family_key) {
        Log.d("TAG", "deleteFamilyFromDB: " + family_key);
        DatabaseReference myFamilies = getDatabase().getReference().child(FAMILIES).child(family_key);
        myFamilies.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
                Log.d("TAG", "Deleted " + snapshot.child("family_key").toString() + " family from " + snapshot.child("f_name").toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String uid = getFirebaseAuth().getCurrentUser().getUid();
        DatabaseReference myRef = getDatabase().getReference(USERS).child(uid).child(FAMILIES);
        myRef.child(family_key).removeValue();
        //gs://pety-47235.appspot.com/pictures/families/-MTpqJwQ3olUjsrJpFId
        //TODO Remove image from database storage
        StorageReference myPic = getFirebaseStorage().getReference().child("/pictures/" + FAMILIES + "/" + family_key);
        myPic.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "onSuccess: delete pic from storage database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: unable to delete pic from storage database");
            }
        });
    }
}
