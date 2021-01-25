package com.example.pety.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;


public class FirebaseDB {
    private static final String TAG = "RealTimeDatabase";
    private FirebaseAuth authDatabase;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private static FirebaseDB firebaseDB;

    public static final String USERS = "users";
    public static final String FIRST_NAME = "f_name";
    public static final String LAST_NAME = "l_name";
    public static final String PHONE_NUMBER = "phone_number";

    public static final String FAMILIES = "families";
    public static final String PETS = "pets";



    private FirebaseDB() {
        database = FirebaseDatabase.getInstance();
        authDatabase = FirebaseAuth.getInstance();
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
