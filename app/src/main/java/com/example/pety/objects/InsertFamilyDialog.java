package com.example.pety.objects;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.pety.R;
import com.example.pety.interfaces.InsertDialogInterface;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

public class InsertFamilyDialog extends AppCompatDialogFragment {

    InsertDialogInterface insertDialogInterface;
    TextInputLayout insertDialog_LAY_familyName;
    FloatingActionButton fab_add_photo;
    AppCompatImageView imgProfile;

    String imageFileName;
    Uri fileUri;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.insert_family_dialog, null);

        findViews(view);
        initViews();
        builder.setView(view)
                .setTitle("Add New Family")
                .setNegativeButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String familyName = insertDialog_LAY_familyName.getEditText().getText().toString();
                        if(familyName.isEmpty() || fileUri == null){
                            Log.d("TAG", "onClick:  STRING IS EMPTY");
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Error")
                                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            return;
                                        }
                                    })
                                    .setMessage("You can't let the fields empty")
                                    .show();
                            fileUri = null;
                        }else{
                            //"pictures/" + firebaseDB.FAMILIES + "/" + imageFileName
                            insertDialogInterface.applyTexts(familyName, imageFileName, fileUri);
                        }
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data.getData();
            File file = ImagePicker.Companion.getFile(data);
            imgProfile.setImageURI(fileUri);
            imgProfile.setScaleType(ImageView.ScaleType.FIT_XY);
            imageFileName = file.getName();
            Log.d("TAG", "onActivityResult: Gallery Image Uri:  " + imageFileName);
        }
    }

    void findViews(View view){
        insertDialog_LAY_familyName = view.findViewById(R.id.insertDialog_LAY_familyName1);
        fab_add_photo = view.findViewById(R.id.fab_add_photo);
        imgProfile = view.findViewById(R.id.imgProfile);
    }

    void initViews(){
        fab_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(InsertFamilyDialog.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    public void setInsertDialogInterface(InsertDialogInterface insertDialogInterface){
        this.insertDialogInterface = insertDialogInterface;
    }

//    public interface insert_DialogInterface {
//        void applyTexts(String familyName,String familyImagePath,Uri imageUri);
//    }
}
