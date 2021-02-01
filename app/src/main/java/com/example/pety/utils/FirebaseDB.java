package com.example.pety.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pety.objects.Family;
import com.example.pety.objects.Pet;
import com.example.pety.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.google.firebase.storage.StorageReference;


public class FirebaseDB {
    private static final String TAG = "RealTimeDatabase";
    private FirebaseAuth authDatabase;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private static FirebaseDB firebaseDB;
    private FirebaseStorage storage;

    public static final String USERS = "users";
    public static final String FIRST_NAME = "f_name";
    public static final String LAST_NAME = "l_name";
    public static final String PHONE_NUMBER = "phone_number";

    public static final String FAMILIES = "families";
    public static final String PETS = "pets";



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
                    Family family = new Family();
                    String familyName = snapshot.child("f_name").getValue().toString();
                    String family_id = snapshot.child("family_id").getValue().toString();
                    String imageURL = snapshot.child("imageURL").getValue().toString();
                    if(snapshot.hasChild("pets")){
                        pets = (Map<String, Pet>) snapshot.child("pets").getValue();
                        family.setPets(pets);
                    }
                    family.setF_name(familyName);
                    family.setFamily_id(family_id);
                    family.setImageURL(imageURL);
                    families.add(family);
//                    if(finalI == families_keys.size() - 1){
//                        Log.d("tttttt", "onDataChange: " + families.toString());
//                        MySP.getInstance().writeFamiliesToStorage(families);
//                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
//    public String getFirstNameFromDB(){
//        String uid = getFirebaseAuth().getCurrentUser().getUid();
//        String firstName = getDatabase().getReference(USERS).child(uid).child("firstName").
//        return firstName;
//    }

    /**
     * This function store in the database new family
     * @param family
     */
    public void writeNewFamilyToDB(Family family , List<String> families){
        String key = getDatabase().getReference().child(FAMILIES).push().getKey();
        Map<String, Object> familyValues = family.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + FAMILIES + "/" + key, familyValues);
        getDatabase().getReference().updateChildren(childUpdates);


        String uid = getFirebaseAuth().getCurrentUser().getUid();
        families.add(key);
        //Map<String, Object> childUpdatesFamily = new HashMap<>();
        childUpdates.put("/" + USERS + "/" + uid + "/" + FAMILIES + "/", families);
        getDatabase().getReference().updateChildren(childUpdates);
    }

//    public void writeFamilyKeyToUser(List<String> families, String key){
//        String uid = getFirebaseAuth().getCurrentUser().getUid();
//        families.add(key);
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/users/" + uid + "/families/", families);
//        getDatabase().getReference().updateChildren(childUpdates);
//    }

    public void readFamiliesFromDB(){

    }

    public void addNewPet(String family_id,String petName){

    }

//    public void updateField(String docId, String path, String field, String newValue) {
//        getDatabase().collection(path).document(docId).update(field, newValue);
//    }
//
//
//    public void updateListField(String docId, String path, String field, String newValue) {
//        getDatabase().collection(path).document(docId).update(field, FieldValue.arrayUnion(newValue));
//    }
//
//    public void removeListField(String docId, String path, String field, String newValue) {
//        getDatabase().collection(path).document(docId).update(field, FieldValue.arrayRemove(newValue));
//    }
//
//    public void writeSocialMediaSetting(String userId, SocialMedia socialMedia) {
//        getDatabase()
//                .collection(FireStoreDatabase.SOCIAL_MEDIA_STORAGE)
//                .document(userId).set(socialMedia)
//                .addOnSuccessListener(aVoid -> Log.d(TAG, "Social media has been write to storage successfully! "))
//                .addOnFailureListener(e -> Log.d(TAG, "Failed to upload social media object! "));
//    }
//
//    public void writeUserSetting(String userId, StudentSetting studentSetting) {
//        getDatabase()
//                .collection(FireStoreDatabase.SETTING_STORAGE)
//                .document(userId).set(studentSetting)
//                .addOnSuccessListener(aVoid -> Log.d(TAG, "Setting has been write to storage successfully! "))
//                .addOnFailureListener(e -> Log.d(TAG, "Failed to upload user's setting! "));
//    }
//
//    public void getUserImageFromDatabase(Student student) {
//        if (!student.getUri().matches("")) {
//            StorageReference storageReference = fireStoreDatabase.getStorageDatabase().getReference().child((student.getEmail()));
//            storageReference.getDownloadUrl().addOnCompleteListener(task -> {
//                if (task.isSuccessful() && task.getResult() != null) {
//                    student.setUri(task.getResult().toString());
//                }
//            }).addOnFailureListener(e -> Log.d(TAG, "getImageFromDatabase: " + "Download image failed"));
//        }
//
//    }
    public String encodeDot(String caller) {
        return caller.replace('.', ':');
    }

}
