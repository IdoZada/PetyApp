package com.example.pety.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pety.R;
import com.example.pety.utils.FirebaseDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Login_Activity extends AppCompatActivity {
    public static final String LOGIN = "login";

    private enum LOGIN_STATE {
        ENTERING_NUMBER,
        ENTERING_CODE,
    }

    private String phoneInput = "";
    private LOGIN_STATE login_state = LOGIN_STATE.ENTERING_NUMBER;

    MaterialButton signUp_BTN_continue;
    TextInputLayout signUp_LAY_phone;

    FirebaseDB firebaseDB = FirebaseDB.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        validateUser();
        findViews();
        initViews();
    }

    /**
     * This method check if user already registered
     */
    private void validateUser() {
        FirebaseUser firebaseUser = firebaseDB.getFirebaseAuth().getCurrentUser();
        if (firebaseUser != null) {
            Log.d(LOGIN, "User Exist In The Firebase");
            Intent myIntent = new Intent(this, Main_Activity.class);
            startActivity(myIntent);
            finish();
            return;
        }
        Log.d(LOGIN, "User Not Exist In The Firebase");
    }

    /**
     * This method switch between enter phone number screen to enter code screen
     */
    private void continueClicked() {
        if (login_state == LOGIN_STATE.ENTERING_NUMBER) {
            startLoginProcess();
        } else if (login_state == LOGIN_STATE.ENTERING_CODE) {
            codeEntered();
        }
    }

    /**
     * This method authenticate in the login process
     */
    private void codeEntered() {
        String smsVerificationCode = signUp_LAY_phone.getEditText().getText().toString();
        Log.d(LOGIN, "verificationCode: " + smsVerificationCode);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseAuthSettings firebaseAuthSettings = firebaseAuth.getFirebaseAuthSettings();
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneInput, smsVerificationCode);

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneInput)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(onVerificationStateChangedCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    /**
     * This method sends a verification code to the phone
     */
    private void startLoginProcess() {
        phoneInput = signUp_LAY_phone.getEditText().getText().toString();
        Log.d(LOGIN, "phoneInput:" + phoneInput);

        FirebaseAuth firebaseAuth = firebaseDB.getFirebaseAuth();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneInput)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(onVerificationStateChangedCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Log.d(LOGIN, "onCodeSent: " + verificationId);
            login_state = LOGIN_STATE.ENTERING_CODE;
            updateUI();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d(LOGIN, "theVerificationCompleted: ");
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.d(LOGIN, "onVerificationFailed: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(Login_Activity.this, "VerificationFailed" + e.getMessage(), Toast.LENGTH_SHORT);
            login_state = LOGIN_STATE.ENTERING_NUMBER;
            updateUI();
        }
    };

    private void updateUI() {
        if (login_state == LOGIN_STATE.ENTERING_NUMBER) {
            signUp_LAY_phone.getEditText().setText("+972551111111");
            signUp_LAY_phone.setHint(getString(R.string.phone_number));
            signUp_LAY_phone.setPlaceholderText("+972 52 5389788");
            signUp_BTN_continue.setText(getString(R.string.continue_));
        } else if (login_state == LOGIN_STATE.ENTERING_CODE) {
            signUp_LAY_phone.getEditText().setText("");
            signUp_LAY_phone.setHint(getString(R.string.enter_code));
            signUp_LAY_phone.setPlaceholderText("******");
            signUp_BTN_continue.setText(getString(R.string.login));
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = firebaseDB.getFirebaseAuth();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOGIN, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            checkAndCreateUser(user);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(LOGIN, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    /**
     * Check if your user exist in firebase, if not create new user
     * @param firebaseUser this parameter allow to access info of specific firebase user
     */
    private void checkAndCreateUser(FirebaseUser firebaseUser) {
        String phone_number = firebaseUser.getPhoneNumber();
        DatabaseReference myRef = firebaseDB.getDatabase().getReference("users");
        myRef.orderByChild("phone_number").equalTo(phone_number).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    //it means user already registered
                    Log.d(LOGIN, "onDataChange:  User already registered");
                    Intent myIntent = new Intent(Login_Activity.this, Main_Activity.class);
                    startActivity(myIntent);
                } else {
                    Log.d(LOGIN, "onDataChange:  New user");
                    Intent myIntent = new Intent(Login_Activity.this, Profile_Activity.class);
                    startActivity(myIntent);
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initViews() {
        signUp_BTN_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueClicked();
            }
        });
    }

    private void findViews() {
        signUp_BTN_continue = findViewById(R.id.signUp_BTN_continue);
        signUp_LAY_phone = findViewById(R.id.signUp_LAY_phone);
    }
}